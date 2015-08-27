package com.greylabs.yoda.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.api.client.util.DateTime;
import com.greylabs.yoda.R;
import com.greylabs.yoda.adapters.AdapterRecyclerViewActGoalList;
import com.greylabs.yoda.adapters.DragSortRecycler;
import com.greylabs.yoda.apis.googleacc.GoogleSync;
import com.greylabs.yoda.interfaces.OnClickOfRecyclerViewActGoalList;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActGoalList  extends AppCompatActivity implements OnClickOfRecyclerViewActGoalList {

    private Toolbar toolbar;
    TextView emptyViewActGoalList;
    ArrayList<Goal> goalArrayList;
    boolean isOperationEdit = false, isOrderChanged = false;
    Menu menu;

    RecyclerView recyclerView;
    AdapterRecyclerViewActGoalList mAdapter;
    LinearLayoutManager mLayoutManager;

    private YodaCalendar yodaCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_list);
        initialize();
    }

    private void initialize() {
        goalArrayList = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolBarActGoalList);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.titleActGoalList));

        emptyViewActGoalList = (TextView) findViewById(R.id.tvEmptyViewActGoalList);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewActGoalList);
        recyclerView.setHasFixedSize(true);
        getGoalArrayFromLocal();
        mAdapter = new AdapterRecyclerViewActGoalList(this, goalArrayList, isOperationEdit);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(null);

        DragSortRecycler dragSortRecycler = new DragSortRecycler();
        dragSortRecycler.setViewHandleId(R.id.btnHandleRecyclerItemActGoalList);
        dragSortRecycler.setFloatingAlpha(0.8F);
        dragSortRecycler.setFloatingBgColor(getResources().getColor(R.color.ColorPrimary));
//        setAutoScrollSpeed(float)
//        How fast it auto scrolls when you get to the top or bottom of the screen. Should acheive similar results across devices.
//
//        setAutoScrollWindow(float)
//        Sets where it starts to autoscroll, this is a fraction of the total height of the RecyclerView. So a value of 0.1 will mean that it will start scrolling at the bottom 10% and top 90% of the view.

        dragSortRecycler.setOnItemMovedListener(new DragSortRecycler.OnItemMovedListener() {
            @Override
            public void onItemMoved(int from, int to) {
                if (from != to){
                    goalArrayList.add(to, goalArrayList.remove(from));
                    isOrderChanged = true;
                    menu.findItem(R.id.actionSaveActGoalList).setVisible(true);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        recyclerView.addItemDecoration(dragSortRecycler);
        recyclerView.addOnItemTouchListener(dragSortRecycler);
        recyclerView.setOnScrollListener(dragSortRecycler.getScrollListener());
    }

    private void getGoalArrayFromLocal() {
        goalArrayList.clear();
        Goal goal = new Goal(this);
        List<Goal> temp=goal.getAll(Goal.GoalDeleted.SHOW_NOT_DELETED);
        if(temp!=null)
            goalArrayList.addAll(temp);
        checkForEmptyViewVisibility();
    }

    private void checkForEmptyViewVisibility() {
        if (goalArrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyViewActGoalList.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyViewActGoalList.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_goal_list, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                if(isOperationEdit){
                    menu.findItem(R.id.actionEditActGoalList).setVisible(true);
                    menu.findItem(R.id.actionSaveActGoalList).setVisible(false);
                    menu.findItem(R.id.actionAddActGoalList).setVisible(false);
                    isOperationEdit = false;
                    mAdapter = new AdapterRecyclerViewActGoalList(this, goalArrayList, isOperationEdit);
                    recyclerView.setAdapter(mAdapter);
                }else {
                    this.finish();
                }
                break;
            case R.id.actionAddActGoalList :
                Intent intent = new Intent(this, ActAddNewGoal.class);
                intent.putExtra(Constants.CALLER, Constants.ACT_HOME);
                intent.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, false);
                this.startActivity(intent);
                break;
            case R.id.actionEditActGoalList :
                menu.findItem(R.id.actionEditActGoalList).setVisible(false);
                menu.findItem(R.id.actionAddActGoalList).setVisible(true);
                isOperationEdit = true;
                mAdapter = new AdapterRecyclerViewActGoalList(this, goalArrayList, isOperationEdit);
                recyclerView.setAdapter(mAdapter);
                break;
            case R.id.actionSaveActGoalList :
                menu.findItem(R.id.actionEditActGoalList).setVisible(true);
                menu.findItem(R.id.actionSaveActGoalList).setVisible(false);
                isOperationEdit = false;
                mAdapter = new AdapterRecyclerViewActGoalList(this, goalArrayList, isOperationEdit);
                recyclerView.setAdapter(mAdapter);
                saveGoalsByNewOrder();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveGoalsByNewOrder() {
        for(int i=0; i<goalArrayList.size(); i++ ){
            goalArrayList.get(i).setOrder(i+1);
            goalArrayList.get(i).save();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGoalArrayFromLocal();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickRecyclerView(final int Position, String operation) {
        if(isOperationEdit){
            switch (operation){
                case Constants.OPERATION_EDIT :
                    if(!goalArrayList.get(Position).getNickName().equals(Constants.NICKNAME_STRETCH_GOAL)){
//                        Intent intent = new Intent(ActGoalList.this, ActGoalDetailsOld.class);
//                        intent.putExtra(Constants.GOAL_OBJECT, goalArrayList.get(Position));
//                        intent.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, true);
//                        startActivity(intent);
                        Goal currentGoal = goalArrayList.get(Position);
                        TimeBox currentTimeBox = new TimeBox(this).get(goalArrayList.get(Position).getTimeBoxId());
                        Intent intent = new Intent(this, ActAddNewGoal.class);
                        intent.putExtra(Constants.GOAL_OBJECT, currentGoal);
                        intent.putExtra(Constants.CALLER, Constants.ACT_GOAL_DETAILS);
                        intent.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, true);
                        intent.putExtra(Constants.TIMEBOX_NICK_NAME, currentTimeBox.getNickName());
                        this.startActivity(intent);
                    }else {
                        AlertDialog.Builder alertLogout = new AlertDialog.Builder(this);
                        alertLogout.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        alertLogout.setMessage(Constants.MSG_CANT_EDIT_DELETE_GOAL);
                        alertLogout.show();
                    }
                    break;

                case Constants.OPERATION_DELETE :
                    if(!goalArrayList.get(Position).getNickName().equals(Constants.NICKNAME_STRETCH_GOAL)){
                        AlertDialog.Builder alertLogout = new AlertDialog.Builder(this);
                        alertLogout.setPositiveButton("Move", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                performActionDeleteGoalYes(Position);
                            }
                        });
                        alertLogout.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                performActionDeleteGoalNo(Position);
                            }
                        });
                        alertLogout.setNeutralButton("Cancel", null);
                        alertLogout.setMessage(Constants.MSG_DELETE_GOAL);
                        alertLogout.show();
                    }else {
                        AlertDialog.Builder alertLogout = new AlertDialog.Builder(this);
                        alertLogout.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        alertLogout.setMessage(Constants.MSG_CANT_EDIT_DELETE_GOAL);
                        alertLogout.show();
                    }
                    break;
            }
        }else if(!isOperationEdit && operation.equals(Constants.OPERATION_SHOW_STEPS)){
            Intent i = new Intent(this, ActStepList.class);
            i.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, true);
            i.putExtra(Constants.GOAL_OBJECT, goalArrayList.get(Position));
            i.putExtra(Constants.CALLER, Constants.ACT_GOAL_LIST);
            startActivity(i);
        }
    }

    private void performActionDeleteGoalYes(int position){
        Prefs prefs = Prefs.getInstance(ActGoalList.this);
        Goal goal = goalArrayList.get(position);
        Goal stretchGoal=new Goal(this).get(prefs.getStretchGoalId());

        PendingStep pendingStep = new PendingStep(ActGoalList.this);
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
                        for(PendingStep substep:subSteps){
                            substep.cancelAlarm();
                            substep.setStringId("");
                            substep.setGoalStringId(stretchGoal.getStringId());
                            substep.setGoalId(stretchGoal.getId());
                            substep.freeSlot();
                            substep.save();
                        }
                    }
                    ps.cancelAlarm();
                    ps.freeSlot();
                    ps.setStringId("");
                    ps.setGoalStringId(stretchGoal.getStringId());
                    ps.setGoalId(stretchGoal.getId());
                    ps.save();
                    break;
                case SINGLE_STEP:
                    ps.cancelAlarm();
                    ps.setStringId("");
                    ps.setGoalStringId(stretchGoal.getStringId());
                    ps.setGoalId(stretchGoal.getId());
                    ps.freeSlot();
                    ps.save();
            }
        }
        long oldTimeBoxId=goal.getTimeBoxId();
        goal.setDeleted(true);
        goal.setTimeBoxId(0);//No TimeBox
        goal.setUpdated(new DateTime(new Date()));
        goal.save();
        getGoalArrayFromLocal();
        mAdapter.notifyDataSetChanged();
        yodaCalendar = new YodaCalendar(ActGoalList.this);
        yodaCalendar.detachTimeBox(oldTimeBoxId);
        //move steps to Stretch Goal
        TimeBox timeBox = new TimeBox(ActGoalList.this).get(prefs.getUnplannedTimeBoxId());
        yodaCalendar.setTimeBox(timeBox);
        yodaCalendar.rescheduleSteps(prefs.getStretchGoalId());
//        Logger.showMsg(ActGoalList.this, Constants.MSG_GOAL_DELETED);
        Logger.showSnack(this, toolbar, Constants.MSG_GOAL_DELETED);
        //sync code
        GoogleSync.getInstance(this).sync();
        //sync code
    }

    private void performActionDeleteGoalNo(int position) {
        //delete goal here and all the steps related to it
        Goal goal = goalArrayList.get(position);
        PendingStep ps = new PendingStep(ActGoalList.this);
        ps.setGoalId(goal.getId());
        List<PendingStep> pendingSteps = ps.getAll(goal.getId());
        if (pendingSteps != null){
            for (PendingStep pendingStep : pendingSteps) {
                switch (pendingStep.getPendingStepType()) {
                    case SUB_STEP:
                    case SERIES_STEP:
                        List<PendingStep> subSteps = pendingStep.getAllSubSteps(pendingStep.getId(), goal.getId());
                        for (PendingStep subStep : subSteps) {
                            subStep.cancelAlarm();
                            subStep.setDeleted(true);
                            subStep.save();
                        }
                        pendingStep.setDeleted(true);
                        pendingStep.save();
                        break;
                    case SINGLE_STEP:
                        pendingStep.cancelAlarm();
                        pendingStep.setDeleted(true);
                        pendingStep.save();
                        break;
                }
            }
        }
        //ps.deleteAllPendingSteps();
        long goalId=goal.getId();
        goal.setDeleted(true);
        goal.setUpdated(new DateTime(new Date()));
        goal.setTimeBoxId(0);
        goal.save();
        getGoalArrayFromLocal();
        mAdapter.notifyDataSetChanged();
        yodaCalendar = new YodaCalendar(ActGoalList.this);
        yodaCalendar.detachTimeBox(goalId);
//        Logger.showMsg(ActGoalList.this, Constants.MSG_GOAL_DELETED);
        Logger.showSnack(this, toolbar, Constants.MSG_GOAL_DELETED);
        //sync code
        GoogleSync.getInstance(this).sync();
        //sync code

    }

}