package org.me.gcu.honors_diabetic_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import javax.sql.StatementEvent;

public class BloodLevelActivity extends AppCompatActivity implements View.OnClickListener {

    /*
	blood level - 15
	best blood level range - 7
	1 unit of inslin for every 3 above 7

	30 - 7 / 3 = 8 units
	20g / 5 = 4 unit
	1 unit for every 5g
*/
    private EditText carbtxt;
    private TextView estimatedlbl, welcomelbl, currentbloodlbl;
    private Button scanbtn, calculatebtn, low_suggestionbtn, view_detailbtn;
    private ListView lowsuggestionlist;
    private int blood_level = 20;
    private String user_name;
    private boolean is_clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_level);

        carbtxt = (EditText) findViewById(R.id.carbtxt);
        estimatedlbl = (TextView) findViewById(R.id.estimatedinsluinlbl);
        currentbloodlbl = (TextView)findViewById(R.id.currentbloodlbl);

        lowsuggestionlist = (ListView)findViewById(R.id.lowsuggestionlist);
        lowsuggestionlist.setVisibility(View.GONE);

        scanbtn = (Button)findViewById(R.id.scanbtn);
        scanbtn.setOnClickListener(this);

        calculatebtn = (Button) findViewById(R.id.calculatebtn);
        calculatebtn.setOnClickListener(this);

        low_suggestionbtn = (Button) findViewById(R.id.lowsuggestionsbtn);
        low_suggestionbtn.setOnClickListener(this);

        view_detailbtn = (Button) findViewById(R.id.viewdetailbtn);
        view_detailbtn.setOnClickListener(this);

        welcomelbl = (TextView)findViewById(R.id.welcomelbl);
        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        welcomelbl.setText("Hello " + user_name);
    }

    public void onClick(View view) {
        if(view == scanbtn) {
            display_blood_level(user_name);
        }

        if(view == calculatebtn) {
            calculate_units(user_name);
        }

        if(view == low_suggestionbtn) {
            if(!is_clicked) {
                lowsuggestionlist.setVisibility(View.VISIBLE);
                String suggestions[] = {"Full Sugar Fizzy Juice", "Gluco Juice", "Glucose Tablets", "Jelly Babies"};
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.row, suggestions);
                lowsuggestionlist.setAdapter(arrayAdapter);
                System.out.println("Clicked Low");
                is_clicked = true;
            } else {
                lowsuggestionlist.setVisibility(View.GONE);
                is_clicked = false;
            }
        }

        if (view == view_detailbtn) {
            DBManager db = new DBManager();

            Intent intent = getIntent();
            String user_name = intent.getStringExtra("user_name");
            String password = intent.getStringExtra("password");

            User user = db.user_login(user_name, password);

            if(user != null) {
                Intent user_details_intent = new Intent(BloodLevelActivity.this, UserInfoActivity.class);
                user_details_intent.putExtra("user_name", user_name);
                user_details_intent.putExtra("password", password);
                this.startActivity(user_details_intent);
            }
        }
    }

    public void calculate_units(String user_name) {
        String carb_intake = carbtxt.getText().toString();
        DBManager db = new DBManager();
        HashMap<String, User> users = db.load_users();

        try {
            db.set_up_connection();
            db.connection = DriverManager.getConnection(db.connection_string, db.sql_user_name, db.sql_password);

            if(users.containsKey(user_name)) {
                String query = "SELECT unit_over_seven, carb_ratio FROM User WHERE user_name = '" + user_name + "'";
                Statement statement = db.connection.createStatement();
                ResultSet rs = statement.executeQuery(query);

                while (rs.next()) {
                    String unit_over_seven = rs.getString("unit_over_seven");
                    String carb_ratio = rs.getString("carb_ratio");

                    if(carb_intake.equals("")) {
                        Toast.makeText(BloodLevelActivity.this, "carb input can't be empty", Toast.LENGTH_SHORT).show();
                    }
                    else if(carb_intake.equals("0")) {
                        int correction_math = (blood_level - 7) / Integer.parseInt(unit_over_seven);
                        System.out.print("math: " + correction_math);
                        estimatedlbl.setText("Estimated Units: " + correction_math);
                    }
                    else {
                        int correction = (blood_level - 7) / Integer.parseInt(unit_over_seven);
                        System.out.println("Correction: " + correction);
                        int correction_with_carbs = correction + Integer.parseInt(carb_intake) / Integer.parseInt(carb_ratio);
                        estimatedlbl.setText("Estimated Units: " + correction_with_carbs);
                    }
                }
            }
        }
        catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }
    }

    public void display_blood_level(String user_name) {
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
                    String low_blood_trigger = rs.getString("low_blood_trigger");
                    String high_blood_trigger = rs.getString("high_blood_trigger");

                    if(blood_level < Integer.parseInt(low_blood_trigger)) {
                        currentbloodlbl.setText("Current Blood Level: LOW");
                    }
                    else if(blood_level > Integer.parseInt(high_blood_trigger) && blood_level < 32) {
                        currentbloodlbl.setText("Current Blood Level: High");
                    }
                    else if(blood_level > 32) {
                        currentbloodlbl.setText("Current Blood Level: DANGER");
                    }
                    else {
                        currentbloodlbl.setText("Current Blood Level: " + blood_level);
                    }
                }
             }
        }
        catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }
    }
}