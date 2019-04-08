package com.demo.tensorflow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private EditText mUser;
    private EditText mPassword;
    private Button mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUser = findViewById(R.id.user);
        mPassword = findViewById(R.id.password);
        mButton = findViewById(R.id.button);
        mButton.setText("注册");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
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
        UserManager.signUp(this, user, password);
        toast("注册成功");
        finish();
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
