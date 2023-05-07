package org.me.gcu.honors_diabetic_app;

import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DBManager {
    Connection connection;
    String driver = "com.mysql.jdbc.Driver";
    String host = "host_name", port = "port", db_name = "db_name", sql_user_name="sql_user_name", sql_password="sql_pass_word";
    String connection_string = "jdbc:mysql://" + host + ":" + port + db_name;

    public Connection set_up_connection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName(driver).newInstance();
            connection = DriverManager.getConnection(connection_string, sql_user_name, sql_password);
        } catch (Exception ex) {
            Log.e("Error here ", ex.getMessage());
        }
        return  connection;
    }

    public HashMap<String, User> load_users() {
        HashMap<String, User> users = new HashMap<>();

        try {
            set_up_connection();
            connection = DriverManager.getConnection(connection_string, sql_user_name, sql_password);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM User");

            while (resultSet.next()) {
                String user_name = resultSet.getString("user_name");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String phone_number = resultSet.getString("phone_number");
                String password = resultSet.getString("password");
                int units_over_seven = resultSet.getInt("unit_over_seven");
                int carb_ratio = resultSet.getInt("carb_ratio");
                int low_blood_trigger = resultSet.getInt("low_blood_trigger");
                int high_blood_trigger = resultSet.getInt("high_blood_trigger");

                User user = new User(user_name, first_name, last_name, email, phone_number, password, units_over_seven, carb_ratio, low_blood_trigger, high_blood_trigger);
                users.put(user_name, user);
            }
        }catch (Exception ex) {
            Log.e("Error in load_users ", ex.getMessage());
        }finally {
            Log.e("Load_users finnaly", "here");
            return users;
        }
    }

    public User user_login(String user_name, String password) {
        HashMap<String, User> users = load_users();

        if(users.containsKey(user_name)) {
            User user = users.get(user_name);
            if(user.get_password().equals(password))
                return  user;
            else
                return null;
        }
        else {
            return  null;
        }
    }

    public void register_user(User user) {
        try {
            Class.forName(driver);
            set_up_connection();
            connection = DriverManager.getConnection(connection_string, sql_user_name, sql_password);

            Statement statement = connection.createStatement();

            statement.executeUpdate("INSERT INTO User (user_name, first_name, last_name, email, phone_number, password, unit_over_seven, carb_ratio, low_blood_trigger, high_blood_trigger) VALUES (" +
                    "'" + user.get_user_name() + "'," +
                    "'" + user.get_first_name() + "'," +
                    "'" + user.get_last_name() + "'," +
                    "'" + user.get_email() + "'," +
                    "'" + user.get_phone_number() + "'," +
                    "'" + user.get_password() + "'," +
                    "'" + user.get_units_over_seven() + "'," +
                    "'" + user.get_carb_ratio() + "'," +
                    "'" + user.get_Low_blood_trigger() + "'," +
                    "'" + user.get_high_blood_trigger() + "')");

        }
        catch (Exception ex) {
            System.out.println("Failed to register" + ex.getMessage());
        }
    }

    public void update_password(String user_name, String password) {
        HashMap<String, User> users = load_users();

        try {
            if(users.containsKey(user_name)) {
                User user = users.get(user_name);
                if(password != null) {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("UPDATE User SET " + "password = '" + password + "' " + "WHERE user_name= '" + user.get_user_name() + "'");
                    Log.e("Success", "Password Updated!");
                }
            }
        }
        catch (Exception ex) {
            Log.e("Error: ", ex.getMessage());
        }
    }

    public void update_account_details(String user_name, String first_name, String last_name, String email, String phone_number, String password, String units_over_seven, String carb_ratio, String low_blood_trigger, String high_blood_trigger) {
      HashMap<String, User> users = load_users();

        try {
          if(users.containsKey(user_name)) {
              User user = users.get(user_name);

              if(!first_name.equals("") && !last_name.equals("") && !email.equals("") && !phone_number.equals("") && !password.equals("") && !units_over_seven.equals("") && !carb_ratio.equals("") && !low_blood_trigger.equals("") && !high_blood_trigger.equals("")) {
                  Statement statement = connection.createStatement();
                  statement.executeUpdate("UPDATE User SET "
                          + "first_name = '" + first_name + "',"
                          + "last_name = '" + last_name + "',"
                          + "email = '" + email + "',"
                          + "phone_number = '" + phone_number + "',"
                          + "password = '" + password + "',"
                          + "unit_over_seven = '" + units_over_seven + "',"
                          + "carb_ratio = '" + carb_ratio + "',"
                          + "low_blood_trigger = '" + low_blood_trigger + "',"
                          + "high_blood_trigger = '" + high_blood_trigger
                          + "' " + "WHERE user_name = '" + user.get_user_name() + "'");
                  Log.e("Updated Details!", "firtname: " + first_name + "\nlastname: " + last_name + "\nemail: " + email + "\nphone_number: " + phone_number + "\npassword: " + password + "\nunits_over_seven: " + units_over_seven + "\ncarb_ratio: " + carb_ratio + "\nlow_blood_trigger: " + low_blood_trigger + "\nhigh_blood_trigger: " + high_blood_trigger);
              }else{
                  Log.e("Missing Input", "One or more fields are empty");
              }
          }
        }
        catch (Exception ex) {
            System.out.println("Failed to register" + ex.getMessage());
        }
    }
}
