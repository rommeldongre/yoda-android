package com.greylabs.yoda.apis.googleacc;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Prefs;

import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;

/**
 * Created by Jaybhay Vijay on 8/11/2015.
 */
public class GoogleAccount  {

    private static final Level LOGGING_LEVEL = Level.OFF;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    static final String TAG = "GoogleAccount";
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    static final int REQUEST_AUTHORIZATION = 1;
    static final int REQUEST_ACCOUNT_PICKER = 2;
    final HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    GoogleAccountCredential credential;
    com.google.api.services.tasks.Tasks service;
    private Context context;
    SharedPreferences settings;

    public GoogleAccount(Context context){
        this.context=context;
        settings =context.getSharedPreferences(Constants.SHARED_PREFS_ACCOUNT,Context.MODE_PRIVATE);
        settings.edit().putString(PREF_ACCOUNT_NAME,getEmail(context));
        settings.edit().commit();
        credential = GoogleAccountCredential.usingOAuth2(context, Collections.singleton(TasksScopes.TASKS));
        credential.setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, getEmail(context)));
        // Tasks clients
        service = new com.google.api.services.tasks.Tasks.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Google-TasksAndroidSample/1.0").build();
    }

    public Tasks getService() {
        return service;
    }

    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        Dialog dialog =GooglePlayServicesUtil.getErrorDialog(connectionStatusCode, (Activity) context,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /** Check that Google Play services APK is installed and up to date. */
    private boolean checkGooglePlayServicesAvailable() {
        final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        }
        return true;
    }





    public TaskList buildGoal(Goal goal) {
        TaskList taskList=new TaskList();
        taskList.setId(String.valueOf(goal.getId()));
        taskList.setKind("tasks#taskList");
        taskList.setTitle(goal.getNickName());
        return taskList;
    }


    public Task buildPending(PendingStep pendingStep) {
        Task task=new Task();
        task.setId(String.valueOf(pendingStep.getId()));
        task.setKind("tasks#task");
        task.setTitle(pendingStep.getNickName());
        return task;
    }

    public Goal convertToGoal(TaskList taskList) {
        Goal goal=new Goal(context);
        try {
            goal.setId(Integer.parseInt(taskList.getId()));
            goal = goal.get(goal.getId());
            goal.setNickName(taskList.getTitle());
        }catch (Exception e){
            //if their is an exception means that ID is generated by server and this goal is not created
            //by our application, so map all such goals to default-stretch goal
            goal=goal.get(Prefs.getInstance(context).getStretchGoalId());
            goal.setStringId(taskList.getId());
        }
        return goal;
    }

    public PendingStep convertToPendingStep(Task task) {
        PendingStep pendingStep=new PendingStep(context);
        try {
            //old step,needs to update
            pendingStep.setId(Integer.parseInt(task.getId()));
            pendingStep = pendingStep.get(pendingStep.getId());
        }catch (Exception e){
            //if their is exception means that ID is generated by server and this step is not created
            //by our application
            //new step ,need to insert
            pendingStep.setId(pendingStep.getIdIfExists(task.getId()));
            pendingStep.setTime(Constants.MAX_SLOT_DURATION);
            pendingStep.setStringId(task.getId());
            pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
            pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        }
        pendingStep.setNickName(task.getTitle());
        return pendingStep;
    }

    private String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }
    private Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        } return account;
    }

}
