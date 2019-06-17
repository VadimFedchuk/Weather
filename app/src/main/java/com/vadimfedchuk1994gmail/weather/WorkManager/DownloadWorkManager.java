package com.vadimfedchuk1994gmail.weather.WorkManager;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class DownloadWorkManager extends Worker {

    public DownloadWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String locality = getInputData().getString("locality");
        String countryCode = getInputData().getString("countryCode");
        Log.i("TESTTEST", "DownloadWorkManager " + locality + " " + countryCode);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("item " + i);
        }
        String[] array = list.toArray(new String[0]);
        Data data = new Data.Builder()
                .putStringArray("list", array)
                .build();
        return Result.success(data);
    }
}
