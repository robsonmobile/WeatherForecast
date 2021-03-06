package com.ericliudeveloper.weatherforecast.task;

import android.content.Context;
import android.content.Intent;

import com.ericliudeveloper.weatherforecast.database.DBConstants;
import com.ericliudeveloper.weatherforecast.entity.Status;
import com.ericliudeveloper.weatherforecast.entity.StatusDAO;
import com.ericliudeveloper.weatherforecast.service.RetrieveUserService;
import com.ericliudeveloper.weatherforecast.service.RetrieveWeatherDataService;

/**
 * Created by ericliu on 11/07/15.
 */
public final class MainDisplayProcessor implements DBConstants.CommonColumns {
    private MainDisplayProcessor() {
    }


    public static void refreshUser(Context context, String transactionId) {

        StatusDAO.clearDisplayingFlag();

        // create a Status row
        Status status = new Status();
        status.setTransactionId(transactionId);
        status.setSyncStatus(DBConstants.SyncStatus.PULLING);
        status.setFlag(DBConstants.Flag.DISPLAYING);

        StatusDAO.save(status);


        Intent getUserIntent = new Intent(context, RetrieveUserService.class);
        getUserIntent.putExtra(COL_TRANSACTION_ID, transactionId);
        context.startService(getUserIntent);
    }

    public static void refreshWeatherInfo(Context context, String transactionId) {
        // get weather info
        Intent getWeatherIntent = new Intent(context, RetrieveWeatherDataService.class);
        getWeatherIntent.putExtra(COL_TRANSACTION_ID, transactionId);
        context.startService(getWeatherIntent);
    }
}
