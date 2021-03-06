package com.example.filam.populationapp;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class AgeGender extends AppCompatActivity {
Spinner genderSpinner;
Spinner countrySpinner;
Spinner daySpinner;
Spinner monthSpinner;
Spinner yearSpinner;
String gender;
String country;
String day;
String month;
String year;
String responseStr;
Button button;
TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_gender);
        responseStr = getIntent().getStringExtra("response");
        genderSpinner = findViewById(R.id.spinnerGender);
        countrySpinner = findViewById(R.id.spinnerCountry);
        daySpinner = findViewById(R.id.spinnerDay);
        monthSpinner = findViewById(R.id.spinnerMonth);
        yearSpinner = findViewById(R.id.spinnerYear);
        button = findViewById(R.id.Search);
        textView = findViewById(R.id.result);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        System.out.println(responseStr);
        ArrayList<String> tmp = new ArrayList<>();
        System.out.println("ok");
        for(int i = 1; i <= 31; i++){
            tmp.add(Integer.toString(i));
        }
        String[] days = new String[tmp.size()];
        days = tmp.toArray(days);
        loadSpinner(days, daySpinner);

        String[] genders = {"Male","Female"};
        loadSpinner(genders, genderSpinner);

        final String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        loadSpinner(months, monthSpinner);

        ArrayList<String> arrayYear = new ArrayList<>();
        for(int i = 1920; i <= 2019; i++) {
            arrayYear.add(Integer.toString(i));
        }
        String[] years = new String[arrayYear.size()];
        years = arrayYear.toArray(years);
        loadSpinner(years, yearSpinner);

        try{
            JSONObject jsonObject = new JSONObject(responseStr);
            System.out.println(jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("countries");
            ArrayList<String> arrayList = new ArrayList<String>();
            for(int i = 0; i < jsonArray.length(); i++){
                System.out.println(jsonArray.get(i).toString());
                arrayList.add(jsonArray.get(i).toString());
            }
            String[] array = new String[arrayList.size()];

            array = arrayList.toArray(array);

            loadSpinner(array, countrySpinner);
            System.out.println(jsonArray.toString());
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        //gender
        String positionGender = preferences.getString("SpinnerGender","");
        ArrayAdapter arrayAdapterG = (ArrayAdapter) genderSpinner.getAdapter();
        int spinnerGenderPosition = arrayAdapterG.getPosition(positionGender);
        genderSpinner.setSelection(spinnerGenderPosition);
        //country
        String positionCountry = preferences.getString("SpinnerCountry1","");
        ArrayAdapter arrayAdapterC = (ArrayAdapter) countrySpinner.getAdapter();
        int spinnerCountryPosition = arrayAdapterC.getPosition(positionCountry);
        countrySpinner.setSelection(spinnerCountryPosition);
        //day
        String positionDay = preferences.getString("SpinnerDay", "");
        ArrayAdapter arrayAdapterD = (ArrayAdapter) daySpinner.getAdapter();
        int spinnerDayPosition = arrayAdapterD.getPosition(positionDay);
        daySpinner.setSelection(spinnerDayPosition);
        //month
        String positionMonth = preferences.getString("SpinnerMonth", "");
        ArrayAdapter arrayAdapterM = (ArrayAdapter) monthSpinner.getAdapter();
        int spinnerMonthPosition = arrayAdapterM.getPosition(positionMonth);
        monthSpinner.setSelection(spinnerMonthPosition);
        //year
        String positionYear = preferences.getString("SpinnerYear", "");
        ArrayAdapter arrayAdapterY = (ArrayAdapter) yearSpinner.getAdapter();
        int spinnerYearPosition = arrayAdapterY.getPosition(positionYear);
        yearSpinner.setSelection(spinnerYearPosition);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = genderSpinner.getSelectedItem().toString();
                editor.putString("SpinnerGender", gender);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country = countrySpinner.getSelectedItem().toString();
                editor.putString("SpinnerCountry1", country);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = daySpinner.getSelectedItem().toString();
                editor.putString("SpinnerDay", day);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = monthSpinner.getSelectedItem().toString();
                editor.putString("SpinnerMonth", month);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = yearSpinner.getSelectedItem().toString();
                editor.putString("SpinnerYear", year);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask asyncTask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        String countr = country.replace(" ", "%20");
                        int monthInt = 0;
                        if(month == "January"){
                            monthInt = 1;
                        } else if(month == "February"){
                            monthInt = 2;
                        } else if(month == "March"){
                            monthInt = 3;
                        } else if(month == "April") {
                            monthInt = 4;
                        } else if(month == "May"){
                            monthInt = 5;
                        } else if(month == "June"){
                            monthInt = 6;
                        } else if(month == "July"){
                            monthInt = 7;
                        } else if(month == "August"){
                            monthInt = 8;
                        } else if(month == "September"){
                            monthInt = 9;
                        } else if(month == "October"){
                            monthInt = 10;
                        } else if(month == "November"){
                            monthInt = 11;
                        } else if(month == "December"){
                            monthInt = 12;
                        }
                        gender = gender.toLowerCase();
                        OkHttpClient client = new OkHttpClient();
                        com.squareup.okhttp.Request request = new Request.Builder()
                                .url("http://api.population.io:80/1.0/life-expectancy/total/"+gender+"/"+ countr +"/"+ year +"-"+ monthInt +"-"+ day +"/")
                                .build();
                        Response response = null;

                        try{
                            response = client.newCall(request).execute();
                            return response.body().string();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o){
                        String resp = o.toString();
                        try {
                            JSONObject object = new JSONObject(resp);
                            int expectancy = object.getInt("total_life_expectancy");

                            textView.setText("The total life expectancy of " + gender + "s with birthday on "
                                    + year + "-" + month + "-" + day + " in " + country + " is " + expectancy);
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                        System.out.println(resp);
                    }
                }.execute();
            }
        });

        System.out.println(gender + country + day + month + year);
    }
    public void loadSpinner(String[] array, Spinner spinner){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }
}
