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
import com.shouter.entities.JUserLogin;
import com.shouter.fragments.ListGroups;
import com.kikigames.menudemo2.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Activity that allows user to register to our app
 */
public class RegisterActivity extends Activity {

    private static final String TAG = ListGroups.class.getSimpleName();
    private static Context mContext;
    Activity a = this;
    EditText etUser;
    EditText etMail;
    EditText etPass1;
    EditText etPass2;
    String user, mail, pass1, pass2;
    boolean registerclicked = false;
    boolean fromregister;
    String regerror;

    private static Gson gson = new GsonBuilder().setDateFormat(
            "yyyy-MM-dd HH:mm:ss").create();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff30b7ff")));
        bar.setIcon(R.drawable.ic_onactionbar);
        bar.setTitle("");

        etUser = (EditText) findViewById(R.id.editText4);
        etMail = (EditText) findViewById(R.id.editText2);
        etPass1 = (EditText) findViewById(R.id.editText5);
        etPass2 = (EditText) findViewById(R.id.editText3);

        try {
            fromregister = (boolean) getIntent().getSerializableExtra("tryregister");
            regerror = (String) getIntent().getSerializableExtra("register_eror");
        }catch(Exception e){
            e.printStackTrace();
        }

        if(fromregister){


            if(regerror.equals("exists")){
                Toast.makeText(this, "This user exists", Toast.LENGTH_SHORT).show();
            }else if(regerror.equals("server_error")){
                Toast.makeText(this, "Server down", Toast.LENGTH_SHORT).show();
            }
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
     * Regexp to validate mails
     */
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Method that validates the mail
     * @param emailStr mail
     * @return true if it's valid, false if it isn't
     */
    public  boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    /**
     * Validates the parameters ands sends it to the thread that add a register
     * @param v view view
     */
    public void onClickRegisterSend(View v){

        String error ="";

        if(isEmpty(etUser)){
           error = "enter a valid user";
        }else if(isEmpty(etMail)){
            error = "enter a valid email";
        }else if(!validate(etMail.getText().toString())){
            error = "enter a valid email";
        }else if(isEmpty(etPass1)){
            error = "enter a valid password";
        }else if(isEmpty(etPass2)){
            error = "rewrite the password";
        }else if(!etPass1.getText().toString().equals(etPass2.getText().toString())){
            error= "passwords don't match";

        }else{
            user = etUser.getText().toString();
            mail = etMail.getText().toString();
            pass1 = etPass1.getText().toString();
            pass2 = etPass2.getText().toString();
        }

        if(!error.equals("")){
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }else{
            JUserLogin jul =  new JUserLogin(mail,user,pass1);
            gson.toJson(jul);

            if(!registerclicked) {
                Log.d(TAG, "antes del thread");
                AddRegister gl = new AddRegister(gson.toJson(jul),a);
                gl.execute();
                registerclicked = true;
            }
        }





    }

    /**
     * intent that calls LoginActivity
     * @param a this activity
     */
    public static void callLoginActivity(Activity a) {
        Log.d(TAG, " en callLoginActivity");
        Intent act = new Intent(a, LoginActivity.class);
        mContext.startActivity(act);
    }

    /**
     * Reload the activity if the paramaters are not ok
     * @param a this activity
     * @param error string that contains the error
     */
    public static void onErrorRegister(Activity a, String error) {
        Intent in = new Intent(a, RegisterActivity.class);
        in.putExtra("tryregister", true);
        in.putExtra("register_eror", error);
        mContext.startActivity(in);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);

    }
}


/**
 * Thread that makes a Post query to Heroku that registers a new user
 */
class AddRegister extends AsyncTask<String,Integer,Boolean> {

    private static final String TAG = ListGroups.class.getSimpleName();


    String respStr;
    String jsonUserLogin="";
    Activity a;
    public AddRegister(String jsonUserLogin, Activity a) {
        this.jsonUserLogin = jsonUserLogin;
        this.a = a;
        Log.d(TAG, "en el thread");
    }

    @Override
    protected Boolean doInBackground(String... params) {

        boolean resul = true;
        try {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://immense-badlands-1533.herokuapp.com/addedit/register/"+ URLEncoder.encode(jsonUserLogin, "UTF-8"));
        post.setHeader("content-type", "application/json");


            HttpResponse resp = httpClient.execute(post);
            respStr = EntityUtils.toString(resp.getEntity());
            Log.d(TAG, respStr);
        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
            resul = false;
        }

        return resul;
    }

    protected void onPostExecute(Boolean result) {
        if(result){
            if(respStr.equals("error")){
                RegisterActivity.onErrorRegister(a, "exists");
            }else {
                RegisterActivity.callLoginActivity(a);
            }
        }else{
            RegisterActivity.onErrorRegister(a, "server_error");
        }
    }



}
