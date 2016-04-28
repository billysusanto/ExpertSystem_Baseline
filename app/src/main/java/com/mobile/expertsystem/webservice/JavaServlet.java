package com.mobile.expertsystem.webservice;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class JavaServlet extends AsyncTask<Integer, Void, String> {

    URL url;
    URLConnection urlConn;
    HttpURLConnection httpConn;
    //String IP = "10.0.2.2"; //Localhost
    //String IP = "192.168.0.12"; //Wifi Rumah
    //String IP = "10.2.44.23"; //Wifi IT
    String IP = "192.168.56.1"; //Virtual Box
    //String IP = "192.168.43.68"; //Lenovo K900

    String SERVLET_URL = "http://"+ IP +":8080/appengine-helloworld/";
    String SERVLET_URL_ORGAN = SERVLET_URL + "?organ";

    int response;

    @Override
    protected String doInBackground(Integer... params) {
        String msg = "";

        if(params[0] == 1 && params[1] == 0) {
            try {
                url = new URL(SERVLET_URL_ORGAN);
                urlConn = url.openConnection();
                httpConn = (HttpURLConnection) urlConn;

                response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    InputStream is = httpConn.getInputStream();

                    if (is != null) {
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);

                        String value;
                        while ((value = br.readLine()) != null) {
                            msg += value;
                        }
                    }
                }
            } catch (MalformedURLException e) {
                Log.e("MalformedURLEx : ", e.toString());
            } catch (IOException e) {
                Log.e("IOException : ", e.toString());
            }
        }

        if(params[0] == 2){
            try {
                url = new URL(SERVLET_URL + params[1] + "/?gejala");

                urlConn = url.openConnection();
                httpConn = (HttpURLConnection) urlConn;

                response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    InputStream is = httpConn.getInputStream();

                    if (is != null) {
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);

                        String value;
                        while ((value = br.readLine()) != null) {
                            msg += value;
                        }
                    }
                }
            } catch (MalformedURLException e) {
                Log.e("MalformedURLEx : ", e.toString());
            } catch (IOException e) {
                Log.e("IOException : ", e.toString());
            }
        }

        if(params[0] == 3){
            try {
                url = new URL(SERVLET_URL + params[1] + "/?aturan");
                urlConn = url.openConnection();
                httpConn = (HttpURLConnection) urlConn;

                response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    InputStream is = httpConn.getInputStream();

                    if (is != null) {
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);

                        String value;
                        while ((value = br.readLine()) != null) {
                            msg += value;
                        }
                    }
                }
            } catch (MalformedURLException e) {
                Log.e("MalformedURLEx : ", e.toString());
            } catch (IOException e) {
                Log.e("IOException : ", e.toString());
            }
        }

        if(params[0] == 4){
            try {
                url = new URL(SERVLET_URL + params[1] + "/?penyakit");
                urlConn = url.openConnection();
                httpConn = (HttpURLConnection) urlConn;

                response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    InputStream is = httpConn.getInputStream();

                    if (is != null) {
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);

                        String value;
                        while ((value = br.readLine()) != null) {
                            msg += value;
                        }
                    }
                }
            } catch (MalformedURLException e) {
                Log.e("MalformedURLEx : ", e.toString());
            } catch (IOException e) {
                Log.e("IOException : ", e.toString());
            }
        }

        return msg;
    }



    protected void onPreExecute(){

    }
}
