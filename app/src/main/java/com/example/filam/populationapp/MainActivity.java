package com.example.filam.populationapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    }

    public void testIntent(View view)
    {
        Intent intent = new Intent(this, SpinnerSearch.class);
        intent.putExtra("response", responseStr);
        startActivity(intent);
    }
    public void GenderAgeIntent(View view)
    {
        Intent intent = new Intent(this, AgeGender.class);
        intent.putExtra("response", responseStr);
        startActivity(intent);
    }
    public void YearAgeIntent(View view)
    {
        Intent intent = new Intent(this, PopulationByYearAge.class);
        intent.putExtra("response", responseStr);
        startActivity(intent);
    }
}
