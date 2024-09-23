package com.morg.cacheandroidsample;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.morg.cacheandroid.databinding.ActivityMainBinding;
import com.morg.cacheandroidsample.jobscheduler.JobServiceManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MYJOBID = 1;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //        init();

        ObjectCache<Object> cache = new ObjectCache<>(this);
        binding.buttonWrite.setOnClickListener(v -> {
            List<Student> student = new ArrayList<>();
            student.add(new Student("Test 1", "Class 1"));
            student.add(new Student("Test 2", "Class 2"));
            student.add(new Student("Test 3", "Class 3"));
            student.add(new Student("Test 4", "Class 4"));
            student.add(new Student("Test 5", "Class 5"));

            cache.setValue("students", student);
        });
        binding.buttonRead.setOnClickListener(v -> {
            List<Student> students = (List<Student>) cache.getValue("students", new TypeToken<List<Student>>() {
            }.getType());
            binding.textView.setText(new Gson().toJson(students));
        });
        binding.buttonDelete.setOnClickListener(v -> {
            cache.deleteValue("students");
        });


        binding.buttonWriteStudent.setOnClickListener(v -> {
            cache.setValue("student", new Student("Test 1", "Class 1"));
        });
        binding.buttonReadStudent.setOnClickListener(v -> {
            Student student = (Student) cache.getValue("student", new TypeToken<Student>() {
            }.getType());

            binding.textView.setText(new Gson().toJson(student));
        });
        binding.buttonDeleteStudent.setOnClickListener(v -> {
            cache.deleteValue("student");
        });


        binding.buttonDeleteAll.setOnClickListener(view -> {
            cache.deleteAllValues();
        });
    }

    private void init() {

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName jobService =
                new ComponentName(getBaseContext(), JobServiceManager.class);
        JobInfo jobInfo = new JobInfo.Builder(MYJOBID, jobService)
                .setRequiresDeviceIdle(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPeriodic(15 * 60 * 1000)
                .setBackoffCriteria(10000, JobInfo.BACKOFF_POLICY_LINEAR)
                .setPersisted(true)
                .build();
        jobScheduler.schedule(jobInfo);

    }

    private boolean isInternetConnection() {
        try {
            ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            return conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}