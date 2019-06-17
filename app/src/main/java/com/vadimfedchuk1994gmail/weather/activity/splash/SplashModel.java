package com.vadimfedchuk1994gmail.weather.activity.splash;

import android.content.Context;
import android.util.Log;

import com.vadimfedchuk1994gmail.weather.Weather;
import com.vadimfedchuk1994gmail.weather.WorkManager.DownloadWorkManager;
import com.vadimfedchuk1994gmail.weather.WorkManager.SaveDataWorkManager;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class SplashModel {

    public void loadSaveData(String locality, String countryCode, Context context, OnCompleteCallback callback) {
        if(!locality.isEmpty() && !countryCode.isEmpty()) {
            Data data = new Data.Builder()
                    .putString("locality", locality)
                    .putString("countryCode", countryCode)
                    .build();
            OneTimeWorkRequest requestDownload = new
                    OneTimeWorkRequest.Builder(DownloadWorkManager.class).setInputData(data).build();
            OneTimeWorkRequest requestSaveData = new
                    OneTimeWorkRequest.Builder(SaveDataWorkManager.class).build();

            WorkManager workManager = WorkManager.getInstance();
            workManager.beginWith(requestDownload).then(requestSaveData).enqueue();
            LiveData<WorkInfo> status = workManager.getWorkInfoByIdLiveData(requestSaveData.getId());
            status.observe((LifecycleOwner)context, new Observer<WorkInfo>() {
                @Override
                public void onChanged(WorkInfo workInfo) {
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        Log.i("TESTTEST", "success");
                        callback.onComplete();
                    }
                }
            });

        } else {
            callback.onComplete();
        }
    }

    interface OnCompleteCallback {
        void onComplete();
    }
}
