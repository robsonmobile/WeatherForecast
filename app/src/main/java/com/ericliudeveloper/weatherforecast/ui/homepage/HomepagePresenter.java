package com.ericliudeveloper.weatherforecast.ui.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ericliudeveloper.weatherforecast.entity.User;
import com.ericliudeveloper.weatherforecast.entity.WeatherInfo;
import com.ericliudeveloper.weatherforecast.mvp_framework.DisplayView;
import com.ericliudeveloper.weatherforecast.mvp_framework.RequestStatus;
import com.ericliudeveloper.weatherforecast.mvp_framework.ViewModel;
import com.ericliudeveloper.weatherforecast.mvp_framework.base.BasePresenter;


/**
 * Created by ericliu on 12/01/2016.
 */
public class HomepagePresenter extends BasePresenter {


    public HomepagePresenter(int presenterId, DisplayView displayView, ViewModel viewModel) {
        super(presenterId, displayView, viewModel);
    }


    @Override
    public void loadInitialData(Bundle args, boolean isConfigurationChange) {
        if (isConfigurationChange) {
            onUpdateComplete(mModel, HomepageQueryRequest.GET_USER);
            onUpdateComplete(mModel, HomepageQueryRequest.REFRESH_WEATHER);

        } else {

            mDisplayView.displayData(null, HomepageRefreshDisplay.START_USER_PROGRESS_BAR);
            mDisplayView.displayData(null, HomepageRefreshDisplay.START_WEATHERINFO_PROGRESS_BAR);
            mModel.onInitialModelUpdate(0, null);
        }
    }


    @Override
    public void onUpdateComplete(ViewModel model, ViewModel.QueryEnum query) {
        RequestStatus status = model.getRequestStatus(query);

        if (query.getId() == HomepageQueryRequest.GET_USER.getId()) {

            if (status == RequestStatus.SUCESS) {

                User user = ((HomepageModel) model).getUser();
                mDisplayView.displayData(user, HomepageRefreshDisplay.DISPLAY_USER);

                mDisplayView.displayData(null, HomepageRefreshDisplay.SHOW_USER_FIELDS);
                mDisplayView.displayData(null, HomepageRefreshDisplay.STOP_USER_PROGRESS_BAR);

            } else if (status == RequestStatus.LOADING) {
                mDisplayView.displayData(null, HomepageRefreshDisplay.START_USER_PROGRESS_BAR);
            }
            return;

        }

        if (query.getId() == HomepageQueryRequest.REFRESH_WEATHER.getId()) {

            if (status == RequestStatus.SUCESS) {
                WeatherInfo weatherInfo = ((HomepageModel) model).getmWeatherInfo();
                mDisplayView.displayData(weatherInfo, HomepageRefreshDisplay.DISPLAY_WEATHER);


                mDisplayView.displayData(null, HomepageRefreshDisplay.SHOW_WEATHERINFO_FIELDS);
                mDisplayView.displayData(null, HomepageRefreshDisplay.STOP_WEATHERINFO_PROGRESS_BAR);
            }else if (status == RequestStatus.LOADING) {
                mDisplayView.displayData(null, HomepageRefreshDisplay.START_WEATHERINFO_PROGRESS_BAR);
            }
            return;

        }


    }

    @Override
    public void onUserAction(UserActionEnum action, @Nullable Bundle args) {
        int actionId = action.getId();

        if (actionId == HomepageUserAction.REFRESH_BUTTON_CLICKED.getId()) {
            mDisplayView.displayData(null, HomepageRefreshDisplay.START_USER_PROGRESS_BAR);
            mDisplayView.displayData(null, HomepageRefreshDisplay.HIDE_USER_FIELDS);
            mModel.onStartModelUpdate(0, HomepageQueryRequest.GET_USER, null);


            mDisplayView.displayData(null, HomepageRefreshDisplay.START_WEATHERINFO_PROGRESS_BAR);
            mDisplayView.displayData(null, HomepageRefreshDisplay.HIDE_WEATHERINFO_FIELDS);
            mModel.onStartModelUpdate(0, HomepageQueryRequest.REFRESH_WEATHER, null);
        }
    }


    public enum HomepageQueryRequest implements ViewModel.QueryEnum {
        GET_USER,
        REFRESH_WEATHER,;


        @Override
        public int getId() {
            return this.ordinal();
        }
    }


    public enum HomepageUserAction implements UserActionEnum {
        REFRESH_BUTTON_CLICKED;


        @Override
        public int getId() {
            return this.ordinal();
        }
    }


    public enum HomepageRefreshDisplay implements RefreshDisplayEnum {
        START_USER_PROGRESS_BAR, STOP_USER_PROGRESS_BAR, DISPLAY_WEATHER, DISPLAY_USER, HIDE_USER_FIELDS, SHOW_USER_FIELDS, START_WEATHERINFO_PROGRESS_BAR, STOP_WEATHERINFO_PROGRESS_BAR, HIDE_WEATHERINFO_FIELDS, SHOW_WEATHERINFO_FIELDS;

        @Override
        public int getId() {
            return this.ordinal();
        }
    }


}


