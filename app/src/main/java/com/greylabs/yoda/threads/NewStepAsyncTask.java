package com.greylabs.yoda.threads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.greylabs.yoda.database.NewStep;
import com.greylabs.yoda.database.QuickStart;
import com.greylabs.yoda.models.Slot;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;

import java.util.List;
import java.util.ServiceLoader;

public class NewStepAsyncTask extends AsyncTask<String, String, String> {

    ProgressDialog progressDialog;
    Context context;
    Handler myHandler;

    public NewStepAsyncTask(Context activityContext, Handler handler) {
        this.context = activityContext;
        this.myHandler = handler;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(Constants.MSG_INITIALIZING_QUICK_START);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        Prefs prefs=Prefs.getInstance(context);
        NewStep newStep = new NewStep(context);
        newStep.newStep();
        Slot slot=new Slot(context);
        slot.setDefaultGoalDetails();
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        Logger.log("InitCalendarAsyncTask", "Calendar Initialized");
        Message message = new Message();
        message.obj = result;
        myHandler.sendMessage(message);
    }
}