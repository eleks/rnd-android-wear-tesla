package com.eleks.tesla.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eleks.tesla.R;
import com.eleks.tesla.api.Config;
import com.eleks.tesla.api.TeslaApi;
import com.eleks.tesla.utils.PreferencesManager;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends BaseActivity {

    @InjectView(R.id.signIn)
    Button signInBtn;
    @InjectView(R.id.signOut)
    Button signOutBtn;
    @InjectView(R.id.username)
    TextView tvUserName;
    @InjectView(R.id.password)
    TextView tvPassword;
    @InjectView(R.id.signInForm)
    View signInForm;
    @InjectView(R.id.progressContainer)
    View progressContainer;
    @InjectView(R.id.signOutForm)
    View signOutForm;

    private AsyncTask loginTask = new AsyncTask<Object, Object, Object>() {
        @Override
        protected Object doInBackground(Object... voids) {
            try {
                TeslaApi api = TeslaApi.getInstance();
                Log.d(Config.TAG, "Performing login operation...");
                JSONObject loginResult = api.login(tvUserName.getText().toString(), tvPassword.getText().toString());
                String accessToken = loginResult.getString("access_token");
                Log.d(Config.TAG, "Access Token: " + accessToken);
                PreferencesManager.putAccessToken(LoginActivity.this, accessToken);
            } catch (Exception e) {
                return e;
            }
            return new Object();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (o instanceof Exception) {
                Toast.makeText(LoginActivity.this, ((Exception) o).getMessage(), Toast.LENGTH_SHORT).show();
                showLoginForm();
            } else {
                showSignOutForm();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userNameValue = tvUserName.getText().toString().trim();
                final String passwordValue = tvPassword.getText().toString().trim();

                if (TextUtils.isEmpty(userNameValue) || TextUtils.isEmpty(passwordValue)) {
                    Toast.makeText(LoginActivity.this, "Empty values are not allowed", Toast.LENGTH_SHORT).show();
                    return;
                }

                PreferencesManager.putUserName(LoginActivity.this, userNameValue);

                loginTask.execute();
            }
        });
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferencesManager.clear(LoginActivity.this);
                showLoginForm();
            }
        });

        showLoginForm();

        if (PreferencesManager.isLoggedIn(this)) {
            showSignOutForm();
        }
    }

    // TODO not used now
    private void showProgres() {
        signInForm.setVisibility(View.INVISIBLE);
        progressContainer.setVisibility(View.VISIBLE);
        signOutForm.setVisibility(View.INVISIBLE);
    }

    private void showLoginForm() {
        signInForm.setVisibility(View.VISIBLE);
        progressContainer.setVisibility(View.INVISIBLE);
        signOutForm.setVisibility(View.INVISIBLE);
    }

    private void showSignOutForm() {
        signInForm.setVisibility(View.INVISIBLE);
        progressContainer.setVisibility(View.INVISIBLE);
        signOutForm.setVisibility(View.VISIBLE);
    }
}
