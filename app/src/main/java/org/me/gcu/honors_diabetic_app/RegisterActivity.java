package org.me.gcu.honors_diabetic_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText reg_usernametxt, reg_firstnametxt, reg_lastnametxt, reg_emailtxt, reg_phonenumbertxt, reg_passwordtxt, reg_units_over_seven_txt, reg_carb_ratio, reg_low_blood_trigger_txt, reg_high_blood_trigger_txt;
    private Button registerbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_usernametxt = (EditText) findViewById(R.id.reg_usernametxt);
        reg_firstnametxt = (EditText) findViewById(R.id.reg_firstnametxt);
        reg_lastnametxt = (EditText) findViewById(R.id.reg_lastnametxt);
        reg_emailtxt = (EditText) findViewById(R.id.reg_emailtxt);
        reg_phonenumbertxt = (EditText) findViewById(R.id.reg_phonenumbertxt);
        reg_passwordtxt = (EditText) findViewById(R.id.reg_passwordtxt);

        reg_units_over_seven_txt = (EditText) findViewById(R.id.reg_units_over_seven_txt);
        reg_carb_ratio = (EditText) findViewById(R.id.reg_carb_ratio);
        reg_low_blood_trigger_txt = (EditText) findViewById(R.id.reg_low_blood_trigger_txt);
        reg_high_blood_trigger_txt = (EditText) findViewById(R.id.reg_high_blood_trigger_txt);

        registerbtn = (Button) findViewById(R.id.registerbtn);
        registerbtn.setOnClickListener(this);
    }

    public void onClick(View v) {
        String user_name = reg_usernametxt.getText().toString();
        String first_name = reg_firstnametxt.getText().toString();
        String last_name = reg_lastnametxt.getText().toString();
        String email = reg_emailtxt.getText().toString();
        String phone_number = reg_phonenumbertxt.getText().toString();
        String password = reg_passwordtxt.getText().toString();
        String units_over_seven = reg_units_over_seven_txt.getText().toString();
        String carb_ratio = reg_carb_ratio.getText().toString();
        String low_blood_trigger = reg_low_blood_trigger_txt.getText().toString();
        String high_blood_trigger = reg_high_blood_trigger_txt.getText().toString();

        if (v == registerbtn) {
            if (user_name.equals("") || first_name.equals("") || last_name.equals("") || email.equals("") || phone_number.equals("") || password.equals("") || units_over_seven.equals("") || carb_ratio.equals("") || low_blood_trigger.equals("") || high_blood_trigger.equals("")) {
                Toast.makeText(RegisterActivity.this, "one or more fields are null!", Toast.LENGTH_SHORT).show();
            }
            DBManager db = new DBManager();
            HashMap<String, User> users = db.load_users();

            if (users.containsKey(user_name))
                Toast.makeText(RegisterActivity.this, "username already in use", Toast.LENGTH_SHORT).show();
            else {
                try {
                    User user = new User(user_name, first_name, last_name, email, phone_number, password, Integer.parseInt(units_over_seven), Integer.parseInt(carb_ratio), Integer.parseInt(low_blood_trigger), Integer.parseInt(high_blood_trigger));
                    db.register_user(user);

                    Log.e("Success", "User added to db");
                    Intent register_page_intent = new Intent(RegisterActivity.this, MainActivity.class);
                    this.startActivity(register_page_intent);
                } catch (Exception ex) {
                    Log.e("Error", "Failed to register user");
                }
            }
        }
    }
}
