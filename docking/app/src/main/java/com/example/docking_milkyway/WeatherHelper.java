package com.example.docking_milkyway;

import android.content.Context;
import android.net.ConnectivityManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class WeatherHelper {


    public static String excuteGet(String targetURL)
    {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("content-type", "application/json;  charset=utf-8");
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setUseCaches (false);

            InputStream in;
            int status = connection.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK){
                in = connection.getErrorStream();   }
            else{
                in = connection.getInputStream();   }

            BufferedReader RD = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = RD.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            RD.close();
            return response.toString();
        } catch (Exception e) {
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}
