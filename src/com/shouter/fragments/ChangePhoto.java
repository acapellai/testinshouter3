package com.shouter.fragments;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shouter.activities.MainActivity;
import com.shouter.entities.JUser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class ChangePhoto extends AsyncTask<String,Integer,Boolean> {

    private static Gson gson = new GsonBuilder().setDateFormat(
            "yyyy-MM-dd HH:mm:ss").create();
    String respStr;
    String foto;
    JUser juser;



    public ChangePhoto(String foto) {
        this.foto = foto;
        this.juser = gson.fromJson(MainActivity.juser, JUser.class);

    }

    @Override
    protected Boolean doInBackground(String... params) {

        boolean resul = true;
        HttpClient httpClient = new DefaultHttpClient();
        String request = "http://immense-badlands-1533.herokuapp.com/addedit/editphoto/"+juser.getUserId()+"/"+foto;
        Log.d("requests", request);
        HttpPut put = new HttpPut(request);
        put.setHeader("content-type", "application/json");

        try {
            HttpResponse resp = httpClient.execute(put);
            respStr = EntityUtils.toString(resp.getEntity());

        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
            resul = false;
        }

        return resul;
    }

    protected void onPostExecute(Boolean result) {
        if(result){
            Log.d("TAG", respStr);

                Toast.makeText(MainActivity.context, "Perfil actualizado", Toast.LENGTH_SHORT).show();
            MainActivity.juser = respStr;

        }else{
            Toast.makeText(MainActivity.context, "Error al actualizar perfil", Toast.LENGTH_SHORT).show();
        }
    }



}
