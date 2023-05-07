package org.me.gcu.honors_diabetic_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText user_name_txt, first_name_txt, last_name_txt, email_txt, phone_number_txt, password_txt, units_over_seven_txt, carb_ratio_txt, low_blood_trigger_txt, high_blood_trigger_txt;
    private Button updatebtn;

    String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        updatebtn = (Button)findViewById(R.id.detail_updatebtn);
        updatebtn.setOnClickListener(this);
        updatebtn.setText("Update Details");

        user_name_txt = (EditText)findViewById(R.id.detail_username_txt);
        user_name_txt.setEnabled(false);
        first_name_txt = (EditText) findViewById(R.id.detail_firstname_txt);
        last_name_txt = (EditText) findViewById(R.id.detail_lastname_txt);
        email_txt = (EditText) findViewById(R.id.detail_email_txt);
        phone_number_txt = (EditText)findViewById(R.id.detail_phonenumber_txt);
        password_txt = (EditText) findViewById(R.id.detail_password_txt);
        units_over_seven_txt = (EditText) findViewById(R.id.detail_units_over_seven_txt);
        carb_ratio_txt = (EditText) findViewById(R.id.detail_carb_ratio_txt);
        low_blood_trigger_txt = (EditText)findViewById(R.id.detail_low_blood_trigger_txt);
        high_blood_trigger_txt = (EditText) findViewById(R.id.detail_high_blood_trigger_txt);

        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        display_user_details(user_name);
    }

    public void onClick(View view) {
        if(view == updatebtn) {
            DBManager db = new DBManager();

            String first_name = first_name_txt.getText().toString();
            String last_name = last_name_txt.getText().toString();
            String email = email_txt.getText().toString();
            String phone_number = phone_number_txt.getText().toString();
            String password = password_txt.getText().toString();
            String units_over_seven = units_over_seven_txt.getText().toString();
            String carb_ratio = carb_ratio_txt.getText().toString();
            String low_blood_trigger = low_blood_trigger_txt.getText().toString();
            String high_blood_trigger = high_blood_trigger_txt.getText().toString();
            Toast.makeText(UserInfoActivity.this, "Details Updated", Toast.LENGTH_SHORT).show();
            db.update_account_details(user_name, first_name, last_name, email, phone_number, password, units_over_seven, carb_ratio, low_blood_trigger, high_blood_trigger);
        }
    }

    public void display_user_details(String user_name) {
        DBManager db = new DBManager();
        HashMap<String, User> users = db.load_users();

        try {
            db.set_up_connection();
            db.connection = DriverManager.getConnection(db.connection_string, db.sql_user_name, db.sql_password);

            if(users.containsKey(user_name)) {
                String query = "SELECT * FROM User WHERE user_name = '" + user_name + "'";
                Statement statement = db.connection.createStatement();
                ResultSet rs = statement.executeQuery(query);

                while (rs.next()) {
                    String first_name = rs.getString("first_name");
                    String last_name = rs.getString("last_name");
                    String email = rs.getString("email");
                    String phone_number = rs.getString("phone_number");
                    String password = rs.getString("password");
                    String unit_over_seven = rs.getString("unit_over_seven");
                    String carb_ratio = rs.getString("carb_ratio");
                    String low_blood_trigger = rs.getString("low_blood_trigger");
                    String high_blood_trigger = rs.getString("high_blood_trigger");

                    user_name_txt.setText(user_name);
                    first_name_txt.setText(first_name);
                    last_name_txt.setText(last_name);
                    email_txt.setText(email);
                    phone_number_txt.setText(phone_number);
                    password_txt.setText(password);
                    units_over_seven_txt.setText(unit_over_seven);
                    carb_ratio_txt.setText(carb_ratio);
                    low_blood_trigger_txt.setText(low_blood_trigger);
                    high_blood_trigger_txt.setText(high_blood_trigger);
                    Log.e("Updated Details!", "firtname: " + first_name + "\nlastname: " + last_name + "\nemail: " + email + "\nphone_number: " + phone_number + "\npassword: " + password + "\nunits_over_seven: " + unit_over_seven + "\ncarb_ratio: " + carb_ratio + "\nlow_blood_trigger: " + low_blood_trigger + "\nhigh_blood_trigger: " + high_blood_trigger);
                }
                db.connection.close();
            }
        }
        catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }
    }
}