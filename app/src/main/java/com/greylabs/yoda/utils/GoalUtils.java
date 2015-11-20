package com.greylabs.yoda.utils;

import android.content.Context;

import com.google.api.client.util.DateTime;
import com.greylabs.yoda.activities.Yoda;
import com.greylabs.yoda.apis.googleacc.GoogleSync;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.Slot;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.scheduler.AlarmScheduler;
import com.greylabs.yoda.scheduler.YodaCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Jaybhay Vijay on 11/2/2015.
 */
public class GoalUtils {

    public static void rescheduleAllSteps(){
        List<PendingStep> pendingSteps=new PendingStep(Yoda.getContext()).getAll(PendingStep.PendingStepStatus.TODO);
        Calendar calendar=Calendar.getInstance();
        if(pendingSteps!=null) {
            AlarmScheduler alarmScheduler = new AlarmScheduler(Yoda.getContext());
            for (PendingStep pendingStep : pendingSteps) {
                Slot slot = new Slot(Yoda.getContext()).get(pendingStep.getSlotId());
                if(slot.getScheduleDate().compareTo(calendar.getTime())>0) {
                    alarmScheduler.setStepId(pendingStep.getId());
                    alarmScheduler.setSubStepId(pendingStep.getSubStepOf());
                    alarmScheduler.setPendingStepType(pendingStep.getPendingStepType());
                    alarmScheduler.setStartTime(slot.getWhen().getStartTime());
                    alarmScheduler.setDuration(pendingStep.getTime());
                    alarmScheduler.setAlarmDate(slot.getScheduleDate());
                    alarmScheduler.cancel();//cancel previous alarm if any
                    alarmScheduler.setAlarm();
                }
            }
        }
    }

    public static void performActionDeleteGoalYes(Goal goal){
        Prefs prefs = Prefs.getInstance(Yoda.getContext());
        Goal stretchGoal=new Goal(Yoda.getContext()).get(prefs.getStretchGoalId());

        PendingStep pendingStep = new PendingStep(Yoda.getContext());
        List<PendingStep> temp=pendingStep.getAll(PendingStep.PendingStepStatus.TODO, PendingStep.PendingStepDeleted.SHOW_NOT_DELETED,goal.getId());
        List<PendingStep> pendingSteps=new ArrayList<>();
        if(temp!=null){
            pendingSteps.addAll(temp);
            temp=pendingStep.getAll(PendingStep.PendingStepStatus.COMPLETED, PendingStep.PendingStepDeleted.SHOW_NOT_DELETED,goal.getId());
            if(temp!=null){
                pendingSteps.addAll(temp);
            }
        }
        for(PendingStep ps:pendingSteps){
            switch (ps.getPendingStepType()){
                case SPLIT_STEP:
                case SERIES_STEP:
                    temp=ps.getAllSubSteps(PendingStep.PendingStepStatus.TODO, PendingStep.PendingStepDeleted.SHOW_NOT_DELETED,ps.getId(),goal.getId());
                    if(temp!=null){
                        List<PendingStep> subSteps=new ArrayList<>();
                        subSteps.addAll(temp);
                        temp=ps.getAllSubSteps(PendingStep.PendingStepStatus.COMPLETED, PendingStep.PendingStepDeleted.SHOW_NOT_DELETED,ps.getId(),goal.getId());
                        if(temp!=null)
                            subSteps.addAll(temp);
                        if(subSteps==null){
                            PendingStepUtils.deletePendingStep(ps);
                            continue;
                        }
                        for(PendingStep substep:subSteps){
                            PendingStepUtils.movePendingStepToStretchGoal(substep, stretchGoal);
                        }
                    }
                    PendingStepUtils.movePendingStepToStretchGoal(ps, stretchGoal);
                    break;
                case SINGLE_STEP:
                    PendingStepUtils.movePendingStepToStretchGoal(ps, stretchGoal);
            }
        }
        long oldTimeBoxId=goal.getTimeBoxId();
        goal.setDeleted(true);
        goal.setTimeBoxId(0);//No TimeBox
        goal.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        goal.save();
        YodaCalendar yodaCalendar = new YodaCalendar(Yoda.getContext());
        yodaCalendar.detachTimeBox(oldTimeBoxId);
        //move steps to Stretch Goal
        TimeBox timeBox = new TimeBox(Yoda.getContext()).get(prefs.getUnplannedTimeBoxId());
        yodaCalendar.setTimeBox(timeBox);
        yodaCalendar.rescheduleSteps(prefs.getStretchGoalId());
    }

    public static void performActionDeleteGoalNo(Goal goal) {
        //delete goal here and all the steps related to it
        PendingStep ps = new PendingStep(Yoda.getContext());
        ps.setGoalId(goal.getId());
        List<PendingStep> pendingSteps = ps.getAll(goal.getId());
        if (pendingSteps != null){
            for (PendingStep pendingStep : pendingSteps) {
                switch (pendingStep.getPendingStepType()) {
                    case SUB_STEP:
                    case SERIES_STEP:
                        List<PendingStep> subSteps = pendingStep.getAllSubSteps(pendingStep.getId(), goal.getId());
                        if(subSteps==null){
                            PendingStepUtils.deletePendingStep(pendingStep);
                        }else {
                            for (PendingStep subStep : subSteps) {
                                PendingStepUtils.deletePendingStep(subStep);
                            }
                            PendingStepUtils.deletePendingStep(pendingStep);
                        }
                        break;
                    case SINGLE_STEP:
                        PendingStepUtils.deletePendingStep(pendingStep);
                        break;
                }
            }
        }
        long timeBoxId=goal.getTimeBoxId();
        goal.setDeleted(true);
        goal.setUpdated(new DateTime(new Date()));
        goal.setTimeBoxId(0);
        goal.save();
        YodaCalendar yodaCalendar = new YodaCalendar(Yoda.getContext());
        yodaCalendar.detachTimeBox(timeBoxId);
        Prefs prefs=Prefs.getInstance(Yoda.getContext());
        //reschedule stretch Goal steps;
        TimeBox timeBox = new TimeBox(Yoda.getContext()).get(prefs.getUnplannedTimeBoxId());
        yodaCalendar.setTimeBox(timeBox);
        yodaCalendar.rescheduleSteps(prefs.getStretchGoalId());
    }

}