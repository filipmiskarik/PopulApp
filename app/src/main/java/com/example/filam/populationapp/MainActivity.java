package com.example.filam.populationapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
String responseStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean check = isInternetConnection();
        if(check){
            AsyncTask asyncTask = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {

                    OkHttpClient client = new OkHttpClient();
                    com.squareup.okhttp.Request request = new Request.Builder()
                            .url("http://api.population.io:80/1.0/countries")
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
                    String json = o.toString();
                    responseStr = o.toString();
                }
            }.execute();
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

    }

    public void testIntent(View view)
    {
        boolean check = isInternetConnection();
        if(check){
            Intent intent = new Intent(this, SpinnerSearch.class);
            intent.putExtra("response", responseStr);
            startActivity(intent);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

    }
    public void GenderAgeIntent(View view)
    {
        boolean check = isInternetConnection();
        if(check){
            Intent intent = new Intent(this, AgeGender.class);
            intent.putExtra("response", responseStr);
            startActivity(intent);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

    }
    public void YearAgeIntent(View view)
    {
        boolean check = isInternetConnection();
        if(check){
            Intent intent = new Intent(this, PopulationByYearAge.class);
            intent.putExtra("response", responseStr);
            startActivity(intent);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

    }
    public  boolean isInternetConnection()
    {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;
        }
        return connected;
    }
}
