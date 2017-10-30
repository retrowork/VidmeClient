package com.example.vidme.navigation;


import android.view.View;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface NavigationView extends MvpView {
    void showPopupMenu(View view);

    void hidePopupMenuButton();

    void showPopupMenuButton();

    void showLoginForm();
}
