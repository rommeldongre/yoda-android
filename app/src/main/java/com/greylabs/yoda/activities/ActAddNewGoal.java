package com.greylabs.yoda.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.greylabs.yoda.R;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.views.MyFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ActAddNewGoal extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText edtObjective, edtKeyResult, edtNickName, edtGoalReason, edtGoalReward, edtGoalBuddy;//edtTime,
    Button btnShowAdvanced, btnHideAdvanced;
    CardView cardViewAdvanced;
    ScrollView scrollView;
    Toolbar toolbar;
    MyFloatingActionButton btnAddFirstStep;
    Spinner timeSpinner;
    ArrayAdapter<String> spinnerArrayAdapter;
    List<TimeBox> timeBoxList;
    ArrayList<String> timeBoxNames = new ArrayList<>();
//    boolean timeSelected = false;
    Goal goal;
    boolean isSaved = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_goal);
        initialize();
    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolBarActAddNewGoal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.titleActAddNewGoal));

        edtObjective = (EditText) findViewById(R.id.edtObjectiveActAddNewGoal);
        edtKeyResult = (EditText) findViewById(R.id.edtKeyResultActAddNewGoal);
//        edtTime = (EditText) findViewById(R.id.edtTimeActAddNewGoal);
        edtNickName = (EditText) findViewById(R.id.edtNickNameActAddNewGoal);
        cardViewAdvanced = (CardView) findViewById(R.id.cardViewAdvancedActAddNewGoal);
        edtGoalReason = (EditText) findViewById(R.id.edtGoalReasonActAddNewGoal);
        edtGoalReward = (EditText) findViewById(R.id.edtGoalRewardHintActAddNewGoal);
        edtGoalBuddy = (EditText) findViewById(R.id.edtGoalBuddyActAddNewGoal);
        btnShowAdvanced = (Button) findViewById(R.id.btnShowAdvancedActAddNewGoal);
        btnHideAdvanced = (Button) findViewById(R.id.btnHideAdvancedActAddNewGoal);
        btnAddFirstStep = (MyFloatingActionButton) findViewById(R.id.btnAddFirstStepActAddNewGoal);
        timeSpinner = (Spinner) findViewById(R.id.spinnerTimeActAddNewGoal);
        scrollView = (ScrollView) findViewById(R.id.scrollViewAvtAddNewGoal);

        getTimeBoxListAndPopulate();

//        edtTime.setOnClickListener(this);
        btnShowAdvanced.setOnClickListener(this);
        btnHideAdvanced.setOnClickListener(this);
        btnAddFirstStep.setOnClickListener(this);
        timeSpinner.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        if(intent.getBooleanExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, false)){
            goal = (Goal)intent.getSerializableExtra(Constants.GOAL_OBJECT);

            getSupportActionBar().setTitle(goal.getNickName());

            edtNickName.setText(goal.getNickName().toString());
            timeSpinner.setSelection(spinnerArrayAdapter.getPosition(intent.getExtras().getString(Constants.TIMEBOX_NICK_NAME)));

            edtObjective.setText(goal.getObjective());
            edtKeyResult.setText(goal.getKeyResult());
            edtGoalReason.setText(goal.getReason());
            edtGoalReward.setText(goal.getReward());
            edtGoalBuddy.setText(goal.getBuddyEmail());

            btnAddFirstStep.setVisibility(View.GONE);
            btnShowAdvanced.setVisibility(View.GONE);
            cardViewAdvanced.setVisibility(View.VISIBLE);

        }else if(!intent.getExtras().getBoolean(Constants.GOAL_ATTACHED_IN_EXTRAS)){
            goal = new Goal(this);
        }
    }

    private void getTimeBoxListAndPopulate() {
        TimeBox timeBox  = new TimeBox(this);
        timeBoxList = timeBox.getAll();
        if(timeBoxList != null && !timeBoxList.isEmpty()){
            for(int i=0; i<timeBoxList.size();i++){
                timeBoxNames.add(timeBoxList.get(i).getNickName());
            }
        }
        timeBoxNames.add(getResources().getString(R.string.addNewTimeBoxSpinnerItemActAddNewGoal));//add new TB option
        spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeBoxNames);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(spinnerArrayAdapter);
        timeSpinner.setSelection(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_add_new_goal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                Intent intent1 = new Intent();
                intent1.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, false);
                setResult(1, intent1);
                this.finish();
                break;
            case R.id.actionSaveActAddNewGoal :
                saveGoal();
                if(isSaved){
                    Intent intent2 = new Intent();
                    intent2.putExtra(Constants.GOAL_OBJECT, goal);
                    intent2.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, true);
                    setResult(1, intent2);
                    this.finish();}
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveGoal() {
        if(edtNickName.getText() != null && edtNickName.getText().length() > 0){
            goal.setNickName(edtNickName.getText().toString());
            goal.setTimeBoxId(timeBoxList.get(timeSpinner.getSelectedItemPosition()).getId());
            goal.setObjective(edtObjective.getText().toString());
            goal.setKeyResult(edtKeyResult.getText().toString());
            goal.setReason(edtGoalReason.getText().toString());
            goal.setReward(edtGoalReward.getText().toString());
            goal.setBuddyEmail(edtGoalBuddy.getText().toString());
            goal.save();

            isSaved = true;
            Logger.showMsg(this, getResources().getString(R.string.msgGoalSavedActAddNewGoal));
        }else {
            Logger.showMsg(this, getResources().getString(R.string.msgEnterGoalNickNameActAddNewGoal));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnAddFirstStepActAddNewGoal :
                saveGoal();
                if(isSaved){
                    Intent i = new Intent(this, ActAddNewStep.class);
                    i.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, true);
                    i.putExtra(Constants.GOAL_OBJECT, goal);
                    startActivity(i);
                    this.finish();
                }
                break;

            case R.id.btnShowAdvancedActAddNewGoal:
                btnShowAdvanced.setVisibility(View.GONE);
                cardViewAdvanced.setVisibility(View.VISIBLE);
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
                break;

            case R.id.btnHideAdvancedActAddNewGoal :
                cardViewAdvanced.setVisibility(View.GONE);
                btnShowAdvanced.setVisibility(View.VISIBLE);
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_UP);
                    }
                });
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position+1 == timeBoxNames.size()){
            startActivity(new Intent(this, ActCreateTimeBox.class));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeSpinner.setSelection(0);
    }
}