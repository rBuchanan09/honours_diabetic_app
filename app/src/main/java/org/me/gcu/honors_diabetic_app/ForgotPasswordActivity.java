package org.me.gcu.honors_diabetic_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalTime;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private Button reset_password_btn;
    private EditText username_text, newpasswordtxt, confirmpasswordtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        reset_password_btn = (Button) findViewById(R.id.resetpasswordbtn);
        reset_password_btn.setOnClickListener(this);

        username_text = (EditText) findViewById(R.id.usernametxt);
        newpasswordtxt = (EditText) findViewById(R.id.newpasswordtxt);
        confirmpasswordtxt = (EditText) findViewById(R.id.confirmpasswordtxt);
    }

    public void onClick(View v) {
        if(v == reset_password_btn)
            reset_password();
    }

    public void reset_password() {
        String user_name = username_text.getText().toString();
        String new_password = newpasswordtxt.getText().toString();
        String confirm_password = confirmpasswordtxt.getText().toString();

        if(user_name.equals("") || new_password.equals("") || confirm_password.equals("")) {
            Toast.makeText(ForgotPasswordActivity.this, "All Fields must be filled!", Toast.LENGTH_SHORT).show();
            Log.e("Error", "one or more fields empty");
        }
        else if(!new_password.equals(confirm_password)) {
            Toast.makeText(ForgotPasswordActivity.this, "New password and confirm password must be the same!", Toast.LENGTH_SHORT).show();
            Log.e("Error", "new password and confirm password do not match");
        }
        else {
            DBManager db = new DBManager();
            db.update_password(user_name, new_password);

            Intent forgot_password_intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
            this.startActivity(forgot_password_intent);
        }
    }
}