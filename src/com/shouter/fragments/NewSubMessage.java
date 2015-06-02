package com.shouter.fragments;



import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.shouter.activities.NewMessageActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by admin on 30/05/2015.
 *
 * Thread that makes a Post query to Heroku and gets the new SubMessages
 *
 */
public class NewSubMessage extends AsyncTask<String,Integer,Boolean> {

    private static final String TAG = ListGroups.class.getSimpleName();
    String respStr;
    String jsonMessage="";

    public NewSubMessage(String jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        boolean resul = true;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post;

        try {


            String request = "http://immense-badlands-1533.herokuapp.com/addedit/createmessage/"+ URLEncoder.encode(jsonMessage, "UTF-8").replace("+", "%20");
            Log.d(TAG, request);
            post = new HttpPost(request);
            Log.d(TAG, request);
            post.setHeader("content-type", "application/json");
            Log.d(TAG, "Creando mensaje");

            try {
                HttpResponse resp = httpClient.execute(post);
                respStr = EntityUtils.toString(resp.getEntity());
                Log.d(TAG, "Creando mensaje 2");

            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return resul;





    }

    protected void onPostExecute(Boolean result) {
        if(result){
            Log.d(TAG, "json del post= " + respStr);
            if(!respStr.equals("error_cmessage")){
                SubMessageFragment.loadMainActivity();
            }
        }else {
            Toast.makeText(NewMessageActivity.context, "Server down", Toast.LENGTH_SHORT).show();
        }


    }


}
