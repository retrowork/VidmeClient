package com.example.vidme.navigation;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.vidme.login.ContainerFragment;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

public class NavigationPresenter extends MvpBasePresenter<NavigationView> {
    SharedPreferences mSharedPreferences;

    NavigationPresenter(SharedPreferences sp) {
        mSharedPreferences = sp;
    }

    void logout() {
        removeAuthToken();
        getView().showLoginForm();
        getView().hidePopupMenuButton();
    }

    void removeAuthToken() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove("ACCESS_TOKEN");
        editor.apply();
    }
}
