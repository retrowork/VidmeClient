package com.example.vidme.login;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vidme.R;
import com.example.vidme.model.Auth;
import com.example.vidme.model.AuthResponse;
import com.example.vidme.network.VidmeService;
import com.example.vidme.videolist.VideoListFragment;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static com.example.vidme.navigation.MainActivity.FEED;
import static com.example.vidme.navigation.MainActivity.LIST_TYPE;

public class LoginFragment extends Fragment {

    public OnLogin listener;

    public interface OnLogin {
        void onLoginListener();
    }

    private String TAG = getClass().getSimpleName();


    @BindView(R.id.username) TextInputLayout mUsernameWrapper;
    @BindView(R.id.password) TextInputLayout mPasswordWrapper;

    @BindView(R.id.username_edittext) EditText mUsernameEditText;
    @BindView(R.id.password_edittext) EditText mPasswordEditText;

    @BindView(R.id.login_button) Button mLoginButton;

    private VidmeService mVidmeService;

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mVidmeService = new VidmeService();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnLogin) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    @OnClick(R.id.login_button)
    public void onClick() {
        mPasswordWrapper.setErrorEnabled(false);
        mUsernameWrapper.setErrorEnabled(false);
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        boolean validPassword = isValidPassword(password);
        boolean validUsername = isValidUsername(username);

        if (!validPassword) {
            mPasswordWrapper.setError("Password is too short");
        }

        if (!validUsername) {
            mUsernameWrapper.setError("Username is too short");
        }

        if (!validPassword || !validUsername) {
            return;
        }

        Observer<AuthResponse> authObserver = new Observer<AuthResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AuthResponse value) {
                if (value.getStatus()) {
                    Auth auth = value.getAuth();
                    String token = auth.getToken();
                    createAuthSession(token);
                    showVideosList();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException) {
                    HttpException response = (HttpException) e;
                    if (response.code() == 400) {
                        mPasswordWrapper.setError(getString(R.string.incorrect_credentials_message));
                        mUsernameWrapper.setError(getString(R.string.incorrect_credentials_message));
                        mPasswordEditText.getText().clear();
                    }
                } else {
                    ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (manager.getActiveNetworkInfo() == null) {
                        Toast.makeText(getActivity(), R.string.no_internet_toast, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onComplete() {

            }
        };

        try {
            mVidmeService.createAuthSession(username, password)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(authObserver);
        } catch (IOException e) {
            Log.v(TAG, e.getMessage());
        }
    }

    private boolean isValidUsername(String username) {
        if (username.length() < 3) {
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 3)
            return false;
        return true;
    }

    private void createAuthSession(String token) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("ACCESS_TOKEN", token);
        editor.apply();
    }

    private void showVideosList() {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        args.putInt(LIST_TYPE, FEED);
        fragment.setArguments(args);
        listener.onLoginListener();
        ((ContainerFragment)getParentFragment())
                .showVideosList();
    }
}
