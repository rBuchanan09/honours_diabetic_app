package org.me.gcu.honors_diabetic_app;

public class User {
    private String user_name, first_name, last_name, email, phone_number, password;
    private int units_over_seven, carb_ratio, low_blood_trigger, high_blood_trigger;

    // getters
    public String get_user_name() {
        return user_name;
    }

    public String get_first_name() {
        return first_name;
    }

    public String get_last_name() {
        return last_name;
    }

    public String get_email() { return  email; }

    public String get_phone_number() { return phone_number; }

    public String get_password() {
        return password;
    }

    public int get_units_over_seven() { return units_over_seven; }

    public int get_carb_ratio() { return carb_ratio; }

    public int get_Low_blood_trigger() { return low_blood_trigger; }

    public int get_high_blood_trigger() { return high_blood_trigger; }

    // default constructor
    public User() {
        user_name = "";
        first_name = "";
        last_name = "";
        email = "";
        phone_number = "";
        password = "";
        units_over_seven = 0;
        carb_ratio = 0;
        low_blood_trigger = 0;
        high_blood_trigger = 0;
    }

    // overloaded constructor to set values of variables
    public User(String user_name_in, String first_name_in, String last_name_in, String email_in, String phone_number_in, String password_in, int units_over_seven_in, int carb_ratio_in, int low_blood_trigger_in, int high_blood_trigger_in) {
        user_name = user_name_in;
        first_name = first_name_in;
        last_name = last_name_in;
        email = email_in;
        phone_number = phone_number_in;
        password = password_in;
        units_over_seven = units_over_seven_in;
        carb_ratio = carb_ratio_in;
        low_blood_trigger = low_blood_trigger_in;
        high_blood_trigger = high_blood_trigger_in;
    }
}

