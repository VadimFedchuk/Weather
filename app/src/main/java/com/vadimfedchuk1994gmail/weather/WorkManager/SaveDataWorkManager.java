package com.vadimfedchuk1994gmail.weather.WorkManager;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SaveDataWorkManager extends Worker {

    public SaveDataWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        List<String> list = Arrays.asList(getInputData().getStringArray("list"));
        Log.i("TESTTEST", "SaveDataWorkManager " + list.size());
        return Result.success();
    }
}
