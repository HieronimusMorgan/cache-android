package com.morg.cacheandroidsample.jobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;


public class JobServiceManager extends JobService {
    private final String TAG = JobServiceManager.class.getSimpleName();
    private boolean isFirst = true;

    @Override
    public void onCreate() {
        super.onCreate();
        isFirst = true;
        Log.d(TAG, "onCreate: JobServiceManager");
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        if (!isFirst) {
            Log.d(JobServiceManager.class.getSimpleName(), "onStartJob: " + params.getJobId());
        } else {
            isFirst = false;
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(JobServiceManager.class.getSimpleName(), "onStopJob: " + params.getJobId());
        return false;
    }
}
