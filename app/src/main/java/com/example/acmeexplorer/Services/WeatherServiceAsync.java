package com.example.acmeexplorer.Services;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.acmeexplorer.R;
import com.example.acmeexplorer.TripDetailsActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherServiceAsync extends AsyncTask<String, Void, String> {

    private final TripDetailsActivity TripDetailsActivity;
    public WeatherServiceAsync(TripDetailsActivity TripDetailsActivity){
        this.TripDetailsActivity = TripDetailsActivity;
    }

    protected String doInBackground(String... params) {
        String dir_web = "http://api.openweathermap.org/data/2.5/weather?q="+this.TripDetailsActivity.getTripCityEnd()+"&APPID=5391617c203ef792feebc0037f3202ba";
        String body = " ";
        try{
            URL url = new URL(dir_web);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String codigoRespuesta = Integer.toString(urlConnection.getResponseCode());
            if (codigoRespuesta.equals("200")){
                InputStream in = urlConnection.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                if(r != null)
                    r.close();
                in.close();
                body = total.toString();
            }
            urlConnection.disconnect();
        }catch(Exception e){
            body = e.toString();
        }
        return body;
    }

    protected void onPostExecute(String s) {

        try {
            JSONObject jsonObject = new JSONObject(s);


            Log.e("WARNING", jsonObject.toString());

            double humidity = jsonObject.getJSONObject("main").getDouble("humidity");
            double temp = jsonObject.getJSONObject("main").getDouble("temp");
            double tempmin = jsonObject.getJSONObject("main").getDouble("temp_min");
            double tempmax = jsonObject.getJSONObject("main").getDouble("temp_max");
            double windSpeed = jsonObject.getJSONObject("wind").getDouble("speed");
            String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            String icon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");

            String urlImage = "http://openweathermap.org/img/w/" + icon + ".png";
            this.TripDetailsActivity.setWeatherImage(urlImage);

            this.TripDetailsActivity.setHumidity(humidity);
            this.TripDetailsActivity.setTemp(temp);
            this.TripDetailsActivity.setTempMin(tempmin);
            this.TripDetailsActivity.setTempMax(tempmax);
            this.TripDetailsActivity.setWindSpeed(windSpeed);
            this.TripDetailsActivity.setDescription(description);


        }catch (Exception e){
            Log.d("ReadWeatherJSONFeedTask", e.getLocalizedMessage());
        }


    }

}