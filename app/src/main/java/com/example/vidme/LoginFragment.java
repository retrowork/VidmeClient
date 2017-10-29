package com.example.vidme;

import android.accounts.AccountManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Body;

import static com.example.vidme.MainActivity.FEED;
import static com.example.vidme.MainActivity.LIST_TYPE;

public class LoginFragment extends Fragment implements View.OnClickListener {

    public OnLogin listener;

    public interface OnLogin {
        void showVideosList();
    }

    private String TAG = getClass().getSimpleName();

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;

    private VidmeService mVidmeService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        mVidmeService = new VidmeService();
        mUsernameEditText = (EditText) view.findViewById(R.id.email_edittext);
        mPasswordEditText = (EditText) view.findViewById(R.id.password_edittext);
        mLoginButton = (Button) view.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(this);
        return view;
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

    @Override
    public void onClick(View view) {
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        Observer<AuthResponse> authObserver = new Observer<AuthResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AuthResponse value) {
                if (value.getStatus()) {
                    Auth auth = value.getAuth();
                    String token = auth.getToken();
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("ACCESS_TOKEN", token);
                    editor.apply();
                    listener.showVideosList();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.v(TAG, "OnError : " + e.getMessage());
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
}
