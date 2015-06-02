package com.shouter.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shouter.entities.JUser;
import com.shouter.entities.JUserLogin;
import com.shouter.fragments.ListGroups;
import com.kikigames.menudemo2.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;


/**
 * Activity where the user log in to our application
 */
public class LoginActivity extends Activity {
    private static final String TAG = ListGroups.class.getSimpleName();
    public static Context mContext;
    Activity a = this;
    EditText etUser;
    EditText etPass;
    String user="";
    String pass="";
    boolean loginclicked;
    boolean fromLogin;

    private static Gson gson = new GsonBuilder().setDateFormat(
            "yyyy-MM-dd HH:mm:ss").create();

    /**
     * Method onCreate where we paint the action bar and initialize variables
     * @param savedInstanceState .
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ActionBar bar = getActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff30b7ff")));
        bar.setIcon(R.drawable.ic_onactionbar);
        bar.setTitle("");


       loginclicked = false;
       etUser = (EditText) findViewById(R.id.editText4);
       etPass = (EditText) findViewById(R.id.editText5);
       mContext = this;

        try {
            fromLogin = (boolean) getIntent().getSerializableExtra("trylogin");

        }catch(Exception e){
            e.printStackTrace();
        }

        if(fromLogin){
            Toast.makeText(this, "Incorrect user or password", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * We check if the editText is empty or not by checking its length
     * @param etText editText
     * @return true if it's empty
     */
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }

    /**
     * onClickMethod where we login
     * @param v view
     */
    public void onClickLogin(View v){


        if(isEmpty(etUser)){
            Toast.makeText(this, "Enter a valid User", Toast.LENGTH_SHORT).show();

        }else if (isEmpty(etPass)){
            Toast.makeText(this, "Enter a valid password", Toast.LENGTH_SHORT).show();
        }else{
            user = etUser.getText().toString();
            pass = etPass.getText().toString();


            JUserLogin jul = new JUserLogin("samu@gmail.com",user,pass);
            gson.toJson(jul);



            if(!loginclicked) {
                GetLogin gl = new GetLogin(gson.toJson(jul), a);
                gl.execute();
                loginclicked = true;
            }


        }

       // http://immense-badlands-1533.herokuapp.com/gets/login/{'email':'samu@gmail.com','name':'','password':'}

    }


    /**
     * this method call the MainActivity
     * @param jlogin json juser
     * @param a the activity
     */
    public static void callMainActivity(String jlogin, Activity a){
            Log.d(TAG, " en callMainActivity");
            MainActivity.juser = jlogin;
            Intent act = new Intent(a, MainActivity.class);
            act.putExtra("jsonLogin", jlogin);
            mContext.startActivity(act);

    }


    /**
     * method that is called when there is an error in the login
     * @param a the activity
     */
    public static void onErrorLogin(Activity a){
       Intent in = new Intent(a, LoginActivity.class);
       in.putExtra("trylogin", true);
       mContext.startActivity(in);
    }


    /**
     * Method that changes the activity by doing an intent to the RegisterActivity
     * @param v view
     */
    public void onClickRegister(View v){
        String jsonLog = "";
        Intent act = new Intent(this, RegisterActivity.class);
        act.putExtra("jsonLogin", jsonLog);
        startActivity(act);
        finish();
    }

//    public void onClickAnonymous(View v){
//        String jsonLog = "";
//        Intent act = new Intent(this, MainActivity.class);
//        act.putExtra("jsonLogin", jsonLog);
//        startActivity(act);
//        finish();
//    }


    @Override
    protected void onResume() {
        super.onResume();
        this.loginclicked = false;
    }


    private static long back_pressed;
    @Override
    public void onBackPressed()
    {
        if (back_pressed + 2000 > System.currentTimeMillis()){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else Toast.makeText(getBaseContext(), "Pulsa de nuevo para salir", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }


}


/**
 * This Thread makes a Query to Heroku and gets a
 * JUserLogin json that we use to verify the login
 */
class GetLogin extends AsyncTask<String,Integer,Boolean> {

    private static final String TAG = ListGroups.class.getSimpleName();

    private static Gson gson = new GsonBuilder().setDateFormat(
            "yyyy-MM-dd HH:mm:ss").create();
    String respStr;
    String jsonUserLogin="";
    JUser juser;
    Activity a;

    /**
     * Method that initialez our variables
     * @param jsonUserLogin json that belongs to the JUserLogin
     * @param a the activity
     */
    public GetLogin(String jsonUserLogin, Activity a) {
      this.jsonUserLogin = jsonUserLogin;
        this.a = a;
       Log.d(TAG, "empieza el thread");
    }


    @Override
    protected Boolean doInBackground(String... params) {

        boolean resul = true;
        try {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet get = new HttpGet("http://immense-badlands-1533.herokuapp.com/gets/login/"+ URLEncoder.encode(jsonUserLogin,"UTF-8"));
        get.setHeader("content-type", "application/json");


            HttpResponse resp = httpClient.execute(get);
            respStr = EntityUtils.toString(resp.getEntity());
            juser = gson.fromJson(respStr, JUser.class);
            Log.d(TAG, "tengo el json");
            Log.d(TAG, respStr);

        } catch (Exception ex) {
            Log.d("ServicioRest", "Error!", ex);
            resul = false;
        }

        return resul;
    }

    protected void onPostExecute(Boolean result) {
        if(result){
            LoginActivity.callMainActivity(respStr,a);
        }else{
            LoginActivity.onErrorLogin(a);
        }
    }




}
