package org.me.gcu.honors_diabetic_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameEditText, passwordEditText;
    private Button loginbtn, registerbtn;
    private TextView forgotPassbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginbtn = (Button) findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(this);

        registerbtn = (Button) findViewById(R.id.registerbtn);
        registerbtn.setOnClickListener(this);

        forgotPassbtn = (TextView)findViewById(R.id.forgotPassbtn);
        forgotPassbtn.setOnClickListener(this);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.loginbtn)
            login();

        if(v.getId() == R.id.registerbtn) {
            Intent register_intent = new Intent(MainActivity.this, RegisterActivity.class);
            this.startActivity(register_intent);
        }

        if(v.getId() == R.id.forgotPassbtn) {
            Intent forgot_pass_intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
            this.startActivity(forgot_pass_intent);
        }
    }
    public void login() {
        String user_name = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        DBManager db = new DBManager();

        User user = db.user_login(user_name, password);

        if(user != null) {
            Intent login_intent = new Intent(MainActivity.this, BloodLevelActivity.class);
            login_intent.putExtra("user_name", user_name);
            login_intent.putExtra("password", password);
            this.startActivity(login_intent);
        }else if(user_name.equals("") || password.equals("")) {
            Toast.makeText(MainActivity.this, "one or more fields are null", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this, "Invalid Login!", Toast.LENGTH_SHORT).show();
        }
    }
}