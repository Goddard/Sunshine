// COMPLETED (2) Make sure you've imported the jobdispatcher.JobService, not job.JobService

// COMPLETED (3) Add a class called SunshineFirebaseJobService that extends jobdispatcher.JobService

//  COMPLETED (4) Declare an ASyncTask field called mFetchWeatherTask

//  COMPLETED (5) Override onStartJob and within it, spawn off a separate ASyncTask to sync weather data
//              COMPLETED (6) Once the weather data is sync'd, call jobFinished with the appropriate arguments

//  COMPLETED (7) Override onStopJob, cancel the ASyncTask if it's not null and return true

package com.example.android.sunshine.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;

public class SunshineFirebaseJobService extends JobService {
    private AsyncTask<Void, Void, Void> mFetchWeatherTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        mFetchWeatherTask = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                SunshineSyncTask.syncWeather(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(jobParameters, false);
            }
        };

        mFetchWeatherTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mFetchWeatherTask != null) {
            mFetchWeatherTask.cancel(true);
        }
        return true;
    }
}