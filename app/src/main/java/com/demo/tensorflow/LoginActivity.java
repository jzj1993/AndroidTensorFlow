package com.demo.tensorflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText mUser;
    private EditText mPassword;
    private Button mButton;
    private View mSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUser = findViewById(R.id.user);
        mPassword = findViewById(R.id.password);
        mButton = findViewById(R.id.button);
        mSignUp = findViewById(R.id.sign_up);
        mSignUp.setVisibility(View.VISIBLE);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void signUp() {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    private void login() {
        String user = mUser.getText().toString();
        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(user)) {
            toast("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            toast("请输入密码");
            return;
        }
        boolean success = UserManager.login(this, user, password);
        if (success) {
            toast("登录成功");
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            toast("账号密码错误");
        }
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
