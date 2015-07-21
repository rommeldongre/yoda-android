package com.greylabs.yoda.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;

import com.greylabs.yoda.R;
import com.greylabs.yoda.enums.Month;
import com.greylabs.yoda.enums.Quarter;
import com.greylabs.yoda.enums.SubValue;
import com.greylabs.yoda.enums.TimeBoxOn;
import com.greylabs.yoda.enums.TimeBoxTill;
import com.greylabs.yoda.enums.TimeBoxWhen;
import com.greylabs.yoda.enums.WeekDay;
import com.greylabs.yoda.enums.Year;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;

import java.util.Set;
import java.util.TreeSet;

public class ActAddTimeBox extends ActionBarActivity implements RadioGroup.OnCheckedChangeListener,CompoundButton.OnCheckedChangeListener {

    private Toolbar toolbar;
    private CheckBox cbWhenEarlyMorning;
    private CheckBox cbWhenMorning;
    private CheckBox cbWhenAfternoon;
    private CheckBox cbWhenEvening;
    private CheckBox cbWhenNight;
    private CheckBox cbWhenLateNight;

    private RadioGroup rgOn;
    private RadioButton rbOnDaily;
    private RadioButton rbOnWeekly;private CheckBox cbOnWeeklySun,cbOnWeeklyMon,cbOnWeeklyTue,cbOnWeeklyWed,cbOnWeeklyThu,cbOnWeeklyFri,cbOnWeeklySat;
    private RadioButton rbOnMonthly;private CheckBox cbOnMonthly1Week,cbOnMonthly2Week,cbOnMonthly3Week,cbOnMonthly4Week;
    private RadioButton rbOnQuaterly;private CheckBox cbOnQuaterly1Month,cbOnQuaterly2Month,cbOnQuaterly3Month;
    private RadioButton rbOnYearly;private CheckBox cbOnYearlyJan,cbOnYearlyFeb,cbOnYearlyMar,cbOnYearlyApr,cbOnYearlyMay,cbOnYearlyJun,
            cbOnYearlyJul,cbOnYearlyAug,cbOnYearlySep,cbOnYearlyOct,cbOnYearlyNov,cbOnYearlyDec;

    private RadioButton rbTillWeek,rbTillMonth,rbTillQuarter,rbTillYear,rbTillForever;

    private EditText edtSummary;
    private ViewFlipper viewFlipper;
    private View llWeekly;
    private View llMonthly;
    private View llQuarterly;
    private View llYearly;
    private View llDaily;
    private TimeBox currentTimeBox;
    private Set<TimeBoxWhen> timeBoxWhenSet;
    private Set<SubValue> timeBoxOnSubValueSet;
    private TimeBoxTill timeBoxTill;
    private TimeBoxOn timeBoxOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_create_time_box);
        initUI();
        setHandlers();
    }

    private void initUI(){

        toolbar = (Toolbar) findViewById(R.id.toolBarActCreateTimeBox);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.titleActCeateTimeBox));

        //when
        cbWhenEarlyMorning=(CheckBox)findViewById(R.id.cbActCreateTimeBoxEarlyMorning);
        cbWhenMorning=(CheckBox)findViewById(R.id.cbActCreateTimeBoxMorning);
        cbWhenAfternoon=(CheckBox)findViewById(R.id.cbActCreateTimeBoxAfternoon);
        cbWhenEvening=(CheckBox)findViewById(R.id.cbActCreateTimeBoxEvening);
        cbWhenNight=(CheckBox)findViewById(R.id.cbActCreateTimeBoxNight);
        cbWhenLateNight=(CheckBox)findViewById(R.id.cbActCreateTimeBoxLateNight);

        //on
        viewFlipper=(ViewFlipper)findViewById(R.id.vfActCreateTimeBoxOn);
        rgOn=(RadioGroup)findViewById(R.id.rgActCreateTimeBoxOn);
        rbOnDaily=(RadioButton)findViewById(R.id.rbActCreateTimeBoxOnDaily);
        rbOnMonthly=(RadioButton)findViewById(R.id.rbActCreateTimeBoxOnMonthly);
        rbOnQuaterly=(RadioButton)findViewById(R.id.rbActCreateTimeBoxOnQuarterly);
        rbOnWeekly=(RadioButton)findViewById(R.id.rbActCreateTimeBoxOnWeekly);
        rbOnYearly=(RadioButton)findViewById(R.id.rbActCreateTimeBoxOnYearly);

        llDaily=findViewById(R.id.first);
        llWeekly=findViewById(R.id.second);
        llMonthly=findViewById(R.id.third);
        llQuarterly =findViewById(R.id.four);
        llYearly=findViewById(R.id.five);

        cbOnWeeklySun=(CheckBox)llWeekly.findViewById(R.id.cbActCreateTimeBoxOnWeeklySun);
        cbOnWeeklyMon=(CheckBox)llWeekly.findViewById(R.id.cbActCreateTimeBoxOnWeeklyMon);
        cbOnWeeklyTue=(CheckBox)llWeekly.findViewById(R.id.cbActCreateTimeBoxOnWeeklyTue);
        cbOnWeeklyWed=(CheckBox)llWeekly.findViewById(R.id.cbActCreateTimeBoxOnWeeklyWed);
        cbOnWeeklyThu=(CheckBox)llWeekly.findViewById(R.id.cbActCreateTimeBoxOnWeeklyThu);
        cbOnWeeklyFri=(CheckBox)llWeekly.findViewById(R.id.cbActCreateTimeBoxOnWeeklyFri);
        cbOnWeeklySat=(CheckBox)llWeekly.findViewById(R.id.cbActCreateTimeBoxOnWeeklySat);

        cbOnMonthly1Week=(CheckBox)llMonthly.findViewById(R.id.cbActCreateTimeBoxOnMonthly1Week);
        cbOnMonthly2Week=(CheckBox)llMonthly.findViewById(R.id.cbActCreateTimeBoxOnMonthly2Week);
        cbOnMonthly3Week=(CheckBox)llMonthly.findViewById(R.id.cbActCreateTimeBoxOnMonthly3Week);
        cbOnMonthly4Week=(CheckBox)llMonthly.findViewById(R.id.cbActCreateTimeBoxOnMonthly4Week);

        cbOnQuaterly1Month=(CheckBox)llQuarterly.findViewById(R.id.cbActCreateTimeBoxOnQuarterly1Month);
        cbOnQuaterly2Month=(CheckBox)llQuarterly.findViewById(R.id.cbActCreateTimeBoxOnQuarterly2Month);
        cbOnQuaterly3Month=(CheckBox)llQuarterly.findViewById(R.id.cbActCreateTimeBoxOnQuarterly3Month);

        cbOnYearlyJan=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyJan);
        cbOnYearlyFeb=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyFeb);
        cbOnYearlyMar=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyMar);
        cbOnYearlyApr=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyApr);
        cbOnYearlyMay=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyMay);
        cbOnYearlyJun=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyJun);
        cbOnYearlyJul=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyJul);
        cbOnYearlyAug=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyAug);
        cbOnYearlySep=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlySep);
        cbOnYearlyOct=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyOct);
        cbOnYearlyNov=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyNov);
        cbOnYearlyDec=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyDec);

        rbTillWeek=(RadioButton)findViewById(R.id.rbActCreateTimeBoxTillWeek);
        rbTillMonth=(RadioButton)findViewById(R.id.rbActCreateTimeBoxTillMonth);
        rbTillQuarter=(RadioButton)findViewById(R.id.rbActCreateTimeBoxTillQuarter);
        rbTillYear=(RadioButton)findViewById(R.id.rbActCreateTimeBoxTillYear);
        rbTillForever=(RadioButton)findViewById(R.id.rbActCreateTimeBoxTillForever);

        edtSummary=(EditText)findViewById(R.id.edtSummaryActCreateTimeBox);
        timeBoxWhenSet=new TreeSet<>();
        timeBoxOnSubValueSet=new TreeSet<>();
        Intent intent = getIntent();
        switch (intent.getStringExtra(Constants.CALLER)){
            case Constants.ACT_TIMEBOX_LIST :
                if(intent.getExtras().getBoolean(Constants.TIMEBOX_ATTACHED_IN_EXTRAS)){
                    currentTimeBox = (TimeBox) intent.getSerializableExtra(Constants.TIMEBOX_OBJECT);
                    getSupportActionBar().setTitle(getString(R.string.titleActAddNewTimeBoxEdit));
                    timeBoxWhenSet=currentTimeBox.getTimeBoxWhen().getWhenValues();
                    timeBoxOnSubValueSet=currentTimeBox.getTimeBoxOn().getSubValues();
                    timeBoxOn=currentTimeBox.getTimeBoxOn().getOnType();
                    timeBoxTill=currentTimeBox.getTillType();
                    initEditUI();
                    //initialize all the views here from the old values of the timebox
                }
                break;
            case Constants.ACT_ADD_NEW_GOAL :
                currentTimeBox = new TimeBox(this);
                break;
        }
    }
    private void setHandlers(){
        rgOn.setOnCheckedChangeListener(this);

        cbWhenEarlyMorning.setOnCheckedChangeListener(this);
        cbWhenMorning.setOnCheckedChangeListener(this);
        cbWhenAfternoon.setOnCheckedChangeListener(this);
        cbWhenEvening.setOnCheckedChangeListener(this);
        cbWhenNight.setOnCheckedChangeListener(this);
        cbWhenLateNight.setOnCheckedChangeListener(this);

        cbOnWeeklySun.setOnCheckedChangeListener(this);
        cbOnWeeklyMon.setOnCheckedChangeListener(this);
        cbOnWeeklyTue.setOnCheckedChangeListener(this);
        cbOnWeeklyWed.setOnCheckedChangeListener(this);
        cbOnWeeklyThu.setOnCheckedChangeListener(this);
        cbOnWeeklyFri.setOnCheckedChangeListener(this);
        cbOnWeeklySat.setOnCheckedChangeListener(this);

        cbOnMonthly1Week.setOnCheckedChangeListener(this);
        cbOnMonthly2Week.setOnCheckedChangeListener(this);
        cbOnMonthly3Week.setOnCheckedChangeListener(this);
        cbOnMonthly4Week.setOnCheckedChangeListener(this);

        cbOnQuaterly1Month.setOnCheckedChangeListener(this);
        cbOnQuaterly2Month.setOnCheckedChangeListener(this);
        cbOnQuaterly3Month.setOnCheckedChangeListener(this);

        cbOnYearlyJan.setOnCheckedChangeListener(this);
        cbOnYearlyFeb.setOnCheckedChangeListener(this);
        cbOnYearlyMar.setOnCheckedChangeListener(this);
        cbOnYearlyApr.setOnCheckedChangeListener(this);
        cbOnYearlyMay.setOnCheckedChangeListener(this);
        cbOnYearlyJun.setOnCheckedChangeListener(this);
        cbOnYearlyJul.setOnCheckedChangeListener(this);
        cbOnYearlyAug.setOnCheckedChangeListener(this);
        cbOnYearlySep.setOnCheckedChangeListener(this);
        cbOnYearlyOct.setOnCheckedChangeListener(this);
        cbOnYearlyNov.setOnCheckedChangeListener(this);
        cbOnYearlyDec.setOnCheckedChangeListener(this);

        rbTillWeek.setOnCheckedChangeListener(this);
        rbTillMonth.setOnCheckedChangeListener(this);
        rbTillQuarter.setOnCheckedChangeListener(this);
        rbTillYear.setOnCheckedChangeListener(this);
        rbTillForever.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_create_time_box, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                this.finish();
                break;

            case R.id.actionSaveActCreateTimeBox :
                TimeBox timeBox=createTimeBoxObjectFromUI();
                if(timeBox!=null){
                    timeBox.save();
                    Logger.showMsg(this, "TimeBox saved");
                    this.finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
        switch (radioButtonID){
            case R.id.rbActCreateTimeBoxOnDaily:
                rbTillWeek.setEnabled(true);
                rbTillMonth.setEnabled(true);
                rbTillQuarter.setEnabled(true);
                invalidateOnValueSelections();
                timeBoxOn=TimeBoxOn.DAILY;
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.first)));
                break;
            case R.id.rbActCreateTimeBoxOnWeekly:
                rbTillWeek.setEnabled(true);
                rbTillMonth.setEnabled(true);
                rbTillQuarter.setEnabled(true);
                timeBoxOn=TimeBoxOn.WEEKLY;
                invalidateOnValueSelections();
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.second)));
                break;
            case R.id.rbActCreateTimeBoxOnMonthly:
                rbTillWeek.setEnabled(false);
                timeBoxOn=TimeBoxOn.MONTHLY;
                invalidateOnValueSelections();
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.third)));
                break;
            case R.id.rbActCreateTimeBoxOnQuarterly:
                rbTillWeek.setEnabled(false);
                rbTillMonth.setEnabled(false);
                timeBoxOn=TimeBoxOn.QUATERLY;
                invalidateOnValueSelections();
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.four)));
                break;
            case R.id.rbActCreateTimeBoxOnYearly:
                rbTillWeek.setEnabled(false);
                rbTillMonth.setEnabled(false);
                rbTillQuarter.setEnabled(false);
                timeBoxOn=TimeBoxOn.YEARLY;
                invalidateOnValueSelections();
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.five)));
                break;
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
      switch (compoundButton.getId()){
          case R.id.cbActCreateTimeBoxEarlyMorning:
              timeBoxWhenSet.add(TimeBoxWhen.EARLY_MORNING);
              break;
          case R.id.cbActCreateTimeBoxMorning:
              timeBoxWhenSet.add(TimeBoxWhen.MORNING);
              break;
          case R.id.cbActCreateTimeBoxAfternoon:
              timeBoxWhenSet.add(TimeBoxWhen.AFTERNOON);
              break;
          case R.id.cbActCreateTimeBoxEvening:
              timeBoxWhenSet.add(TimeBoxWhen.EVENING);
              break;
          case R.id.cbActCreateTimeBoxNight:
              timeBoxWhenSet.add(TimeBoxWhen.NIGHT);
              break;
          case R.id.cbActCreateTimeBoxLateNight:
              timeBoxWhenSet.add(TimeBoxWhen.LATE_NIGHT);
              break;
          case R.id.cbActCreateTimeBoxOnWeeklyMon:
              timeBoxOnSubValueSet.add(WeekDay.MONDAY);
              break;
          case R.id.cbActCreateTimeBoxOnWeeklyTue:
              timeBoxOnSubValueSet.add(WeekDay.TUESDAY);
              break;
          case R.id.cbActCreateTimeBoxOnWeeklyWed:
              timeBoxOnSubValueSet.add(WeekDay.WEDNESDAY);
              break;
          case R.id.cbActCreateTimeBoxOnWeeklyThu:
              timeBoxOnSubValueSet.add(WeekDay.THURSDAY);
              break;
          case R.id.cbActCreateTimeBoxOnWeeklyFri:
              timeBoxOnSubValueSet.add(WeekDay.FRIDAY);
              break;
          case R.id.cbActCreateTimeBoxOnWeeklySat:
              timeBoxOnSubValueSet.add(WeekDay.SATURDAY);
              break;
          case R.id.cbActCreateTimeBoxOnWeeklySun:
              timeBoxOnSubValueSet.add(WeekDay.SUNDAY);
              break;
          case R.id.cbActCreateTimeBoxOnMonthly1Week:
              timeBoxOnSubValueSet.add(Month.WEEK1);
              break;
          case R.id.cbActCreateTimeBoxOnMonthly2Week:
              timeBoxOnSubValueSet.add(Month.WEEK2);
              break;
          case R.id.cbActCreateTimeBoxOnMonthly3Week:
              timeBoxOnSubValueSet.add(Month.WEEK3);
              break;
          case R.id.cbActCreateTimeBoxOnMonthly4Week:
              timeBoxOnSubValueSet.add(Month.WEEK4);
              break;
          case R.id.cbActCreateTimeBoxOnQuarterly1Month:
              timeBoxOnSubValueSet.add(Quarter.MONTH1);
              break;
          case R.id.cbActCreateTimeBoxOnQuarterly2Month:
              timeBoxOnSubValueSet.add(Quarter.MONTH2);
              break;
          case R.id.cbActCreateTimeBoxOnQuarterly3Month:
              timeBoxOnSubValueSet.add(Quarter.MONTH3);
              break;
          case R.id.cbActCreateTimeBoxOnYearlyJan:
              timeBoxOnSubValueSet.add(Year.JANUARY);
              break;
          case R.id.cbActCreateTimeBoxOnYearlyFeb:
              timeBoxOnSubValueSet.add(Year.FEBRUARY);
              break;
          case R.id.cbActCreateTimeBoxOnYearlyMar:
              timeBoxOnSubValueSet.add(Year.MARCH);
              break;
          case R.id.cbActCreateTimeBoxOnYearlyApr:
              timeBoxOnSubValueSet.add(Year.APRIL);
              break;
          case R.id.cbActCreateTimeBoxOnYearlyMay:
              timeBoxOnSubValueSet.add(Year.MAY);
              break;
          case R.id.cbActCreateTimeBoxOnYearlyJun:
              timeBoxOnSubValueSet.add(Year.JUNE);
              break;
          case R.id.cbActCreateTimeBoxOnYearlyJul:
              timeBoxOnSubValueSet.add(Year.JULY);
              break;
          case R.id.cbActCreateTimeBoxOnYearlyAug:
              timeBoxOnSubValueSet.add(Year.AUGUST);
              break;
          case R.id.cbActCreateTimeBoxOnYearlySep:
              timeBoxOnSubValueSet.add(Year.SEPTEMBER);
              break;
          case R.id.cbActCreateTimeBoxOnYearlyOct:
              timeBoxOnSubValueSet.add(Year.OCTOBER);
              break;
          case R.id.cbActCreateTimeBoxOnYearlyNov:
              timeBoxOnSubValueSet.add(Year.NOVEMBER);
              break;
          case R.id.cbActCreateTimeBoxOnYearlyDec:
              timeBoxOnSubValueSet.add(Year.DECEMBER);
              break;

          case R.id.rbActCreateTimeBoxTillWeek:
              timeBoxTill=TimeBoxTill.WEEK;
              break;
          case R.id.rbActCreateTimeBoxTillMonth:
              timeBoxTill=TimeBoxTill.MONTH;
              break;
          case R.id.rbActCreateTimeBoxTillQuarter:
              timeBoxTill=TimeBoxTill.QUARTER;
              break;
          case R.id.rbActCreateTimeBoxTillYear:
              timeBoxTill=TimeBoxTill.YEAR;
              break;
          case R.id.rbActCreateTimeBoxTillForever:
              timeBoxTill=TimeBoxTill.FOREVER;
              break;
      }
        edtSummary.setText(createSummaryString());
    }
    private void initEditUI(){
        if(currentTimeBox!=null){
            //set when
            for(TimeBoxWhen when:currentTimeBox.getTimeBoxWhen().getWhenValues()) {
                switch (when){
                    case EARLY_MORNING: cbWhenEarlyMorning.setChecked(true);break;
                    case MORNING:cbWhenMorning.setChecked(true);break;
                    case AFTERNOON:cbWhenAfternoon.setChecked(true);break;
                    case EVENING:cbWhenEvening.setChecked(true);break;
                    case NIGHT:cbWhenNight.setChecked(true);break;
                    case LATE_NIGHT:cbWhenLateNight.setChecked(true);break;
                }
            }
            //set on
            switch (currentTimeBox.getTimeBoxOn().getOnType()){
                case DAILY:
                    rbOnDaily.setChecked(true);
                    break;
                case WEEKLY:
                    rbOnWeekly.setChecked(true);
                    //set On Subvalues
                    for(SubValue subValue:currentTimeBox.getTimeBoxOn().getSubValues()){
                        WeekDay weekDay=(WeekDay)subValue;
                        switch (weekDay){
                            case SUNDAY:cbOnWeeklySun.setChecked(true);break;
                            case MONDAY:cbOnWeeklyMon.setChecked(true);break;
                            case TUESDAY:cbOnWeeklyTue.setChecked(true);break;
                            case WEDNESDAY:cbOnWeeklyWed.setChecked(true);break;
                            case THURSDAY:cbOnWeeklyThu.setChecked(true);break;
                            case FRIDAY:cbOnWeeklyFri.setChecked(true);break;
                            case SATURDAY:cbOnWeeklySat.setChecked(true);break;
                        }
                    }
                    break;
                case MONTHLY:
                    rbOnMonthly.setChecked(true);
                    for(SubValue subValue:currentTimeBox.getTimeBoxOn().getSubValues()){
                        Month month=(Month)subValue;
                        switch (month){
                            case WEEK1:cbOnMonthly1Week.setChecked(true);break;
                            case WEEK2:cbOnMonthly2Week.setChecked(true);break;
                            case WEEK3:cbOnMonthly3Week.setChecked(true);break;
                            case WEEK4:cbOnMonthly4Week.setChecked(true);break;
                        }
                    }
                    break;
                case QUATERLY:
                    rbOnQuaterly.setChecked(true);
                    for(SubValue subValue:currentTimeBox.getTimeBoxOn().getSubValues()){
                        Quarter quarter=(Quarter)subValue;
                        switch (quarter){
                            case MONTH1:cbOnQuaterly1Month.setChecked(true);
                            case MONTH2:cbOnQuaterly2Month.setChecked(true);
                            case MONTH3:cbOnQuaterly3Month.setChecked(true);
                        }
                    }
                    break;
                case YEARLY:
                    rbOnYearly.setChecked(true);
                    for(SubValue subValue:currentTimeBox.getTimeBoxOn().getSubValues()){
                        Year year=(Year)subValue;
                        switch (year){
                            case JANUARY:cbOnYearlyJan.setChecked(true);break;
                            case FEBRUARY:cbOnYearlyFeb.setChecked(true);break;
                            case MARCH:cbOnYearlyMar.setChecked(true);break;
                            case APRIL:cbOnYearlyApr.setChecked(true);break;
                            case MAY:cbOnYearlyMay.setChecked(true);break;
                            case JUNE:cbOnYearlyJun.setChecked(true);break;
                            case JULY:cbOnYearlyJul.setChecked(true);break;
                            case AUGUST:cbOnYearlyAug.setChecked(true);break;
                            case SEPTEMBER:cbOnYearlySep.setChecked(true);break;
                            case OCTOBER:cbOnYearlyOct.setChecked(true);break;
                            case NOVEMBER:cbOnYearlyNov.setChecked(true);break;
                            case DECEMBER:cbOnYearlyDec.setChecked(true);break;
                        }
                    }
                    break;
            }
            //set Till
            switch (currentTimeBox.getTillType()){
                case WEEK:rbTillWeek.setChecked(true);break;
                case MONTH:rbTillMonth.setChecked(true);break;
                case QUARTER:rbTillQuarter.setChecked(true);break;
                case YEAR:rbTillYear.setChecked(true);break;
                case FOREVER:rbTillForever.setChecked(true);
            }
        }
    }
    private TimeBox createTimeBoxObjectFromUI(){
      TimeBox timeBox=null;
       if(!timeBoxWhenSet.isEmpty() && !timeBoxOnSubValueSet.isEmpty() && timeBoxTill!=null && timeBoxOn!=null){
           timeBox=new TimeBox(this);
           com.greylabs.yoda.models.TimeBoxWhen timeBoxWhen=new com.greylabs.yoda.models.TimeBoxWhen(this);
           com.greylabs.yoda.models.TimeBoxOn timeBoxOn=new com.greylabs.yoda.models.TimeBoxOn(this,this.timeBoxOn);
           timeBoxWhen.setWhenValues(timeBoxWhenSet);
           timeBoxOn.setSubValues(timeBoxOnSubValueSet);
           timeBox.setTimeBoxWhen(timeBoxWhen);
           timeBox.setTimeBoxOn(timeBoxOn);
           timeBox.setTillType(timeBoxTill);
       }else if(timeBoxWhenSet.isEmpty()){
           Logger.showMsg(this,getString(R.string.msgActCreateTimeBoxSelectWhenTime));
       }else if(timeBoxOn==null || timeBoxOnSubValueSet.isEmpty()){
           Logger.showMsg(this,getString(R.string.msgActCreateTimeBoxSelectOnTime));
       }else if(timeBoxTill==null){
           Logger.showMsg(this,getString(R.string.msgActCreateTimeBoxSelectTillTime));
       }
        if(edtSummary.getText()!=null && !edtSummary.getText().equals("")){
           timeBox.setNickName(edtSummary.getText().toString());
        }
        return timeBox;
    }

    private void invalidateOnValueSelections(){
        cbOnWeeklyMon.setChecked(false);
        cbOnWeeklyTue.setChecked(false);
        cbOnWeeklyWed.setChecked(false);
        cbOnWeeklyThu.setChecked(false);
        cbOnWeeklyFri.setChecked(false);
        cbOnWeeklySat.setChecked(false);
        cbOnWeeklySun.setChecked(false);

        cbOnMonthly1Week.setChecked(false);
        cbOnMonthly2Week.setChecked(false);
        cbOnMonthly3Week.setChecked(false);
        cbOnMonthly4Week.setChecked(false);

        cbOnQuaterly1Month.setChecked(false);
        cbOnQuaterly2Month.setChecked(false);
        cbOnQuaterly3Month.setChecked(false);

        cbOnYearlyJan.setChecked(false);
        cbOnYearlyFeb.setChecked(false);
        cbOnYearlyMar.setChecked(false);
        cbOnYearlyApr.setChecked(false);
        cbOnYearlyMay.setChecked(false);
        cbOnYearlyJun.setChecked(false);
        cbOnYearlyJul.setChecked(false);
        cbOnYearlyAug.setChecked(false);
        cbOnYearlySep.setChecked(false);
        cbOnYearlyOct.setChecked(false);
        cbOnYearlyNov.setChecked(false);
        cbOnYearlyDec.setChecked(false);
    }
    private String createSummaryString(){
        String summary="";
        //for when
        for(TimeBoxWhen when:timeBoxWhenSet)
            summary+=when.getDisplayName()+",";
        if(summary.length()>0) {
            summary = summary.substring(0, summary.length() - 1);
            summary += "-";
        }
        //for timeBoxOnSubValueSet
        if(!timeBoxWhenSet.isEmpty()) {
            if (rbOnDaily.isChecked())
                summary += TimeBoxOn.DAILY.getDisplayName() + "-";
            else if (rbOnWeekly.isChecked()) {
                summary += TimeBoxOn.WEEKLY.getDisplayName() + "-";
                for (SubValue subValue : timeBoxOnSubValueSet) {
                    if(subValue instanceof WeekDay) {
                        WeekDay weekDay = (WeekDay) subValue;
                        summary += weekDay.getDisplayName() + ",";
                    }
                }
            } else if (rbOnMonthly.isChecked()) {
                summary += TimeBoxOn.MONTHLY.getDisplayName() + "-";
                for (SubValue subValue : timeBoxOnSubValueSet) {
                    if(subValue instanceof Month) {
                        Month month = (Month) subValue;
                        summary += month.getDisplayName() + ",";
                    }
                }
            } else if (rbOnQuaterly.isChecked()) {
                summary += TimeBoxOn.QUATERLY.getDisplayName() + "-";
                for (SubValue subValue : timeBoxOnSubValueSet) {
                    if(subValue instanceof Quarter) {
                        Quarter quarter = (Quarter) subValue;
                        summary += quarter.getDisplayName() + ",";
                    }
                }
            } else if (rbOnYearly.isChecked()) {
                summary += TimeBoxOn.YEARLY.getDisplayName() + "-";
                for (SubValue subValue : timeBoxOnSubValueSet) {
                    if(subValue instanceof Year) {
                        Year year = (Year) subValue;
                        summary += year.getDisplayName() + ",";
                    }
                }
            }
            summary=summary.substring(0,summary.length()-1);
        }

        if(!timeBoxOnSubValueSet.isEmpty() && !timeBoxWhenSet.isEmpty()) {
            if (rbTillWeek.isChecked()) {
                summary+=" till this ";
                summary += TimeBoxTill.WEEK.getDisplayName();
            } else if (rbTillMonth.isChecked()) {
                summary+=" till this ";
                summary += TimeBoxTill.MONTH.getDisplayName();
            } else if (rbTillQuarter.isChecked()) {
                summary+=" till this ";
                summary += TimeBoxTill.QUARTER.getDisplayName();
            } else if (rbTillYear.isChecked()) {
                summary+=" till this ";
                summary += TimeBoxTill.YEAR.getDisplayName();
            } else if (rbTillForever.isChecked()) {
                summary+=" till ";
                summary += TimeBoxTill.FOREVER.getDisplayName();
            }
        }
        return  summary;
    }

}