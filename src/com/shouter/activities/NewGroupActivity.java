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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kikigames.menudemo2.R;
import com.shouter.entities.JGroup;
import com.shouter.entities.JTag;
import com.shouter.entities.JUser;
import com.shouter.fragments.ListGroups;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This activity calls the thread that creates a new group
 */
public class NewGroupActivity extends Activity {
    private static final String TAG = ListGroups.class.getSimpleName();
    EditText etName;
    EditText etDescription;
    String name,description;
    JUser user;
    public static Context context;

    private static Gson gson = new GsonBuilder().setDateFormat(
            "yyyy-MM-dd HH:mm:ss").create();


    /**
     * We check if the editText is empty or not by checking its length
     * @param etText editText
     * @return true if it's empty
     */
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        context = this;
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff30b7ff")));
        bar.setIcon(R.drawable.ic_onactionbar);
        bar.setTitle("");

        etName = (EditText) findViewById(R.id.editText6);
        etDescription = (EditText) findViewById(R.id.editText7);
    }

    /**
     * Validates both edit text and then calls the thread that makes a query that creates a new group
     * @param v view
     */
    public void onClickCreateGroup(View v){
        Set<JTag> jtags = new HashSet<>();
        String error ="";
        Date date = new Date();
        String userAux = MainActivity.juser;

        if(isEmpty(etName)){
            error = "entra un titulo valido";
        }else if(isEmpty(etDescription)) {
            error = "entra una descripcion valida";
        }else{
            name = etName.getText().toString();
            description = etDescription.getText().toString();
        }

        if(!error.equals("")){
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }else {

            RadioGroup rg = (RadioGroup)findViewById(R.id.radiog_photo);
            int checked = rg.getCheckedRadioButtonId();
            Toast.makeText(this, String.valueOf(checked), Toast.LENGTH_SHORT);
            String image ="user";
            if(checked == R.id.radiop1) {
                image = "party2";

            }else if (checked == R.id.radiop2){
                image = "bcn";
            }else if (checked == R.id.radiop3){
                image = "mountain";
            }



            user = gson.fromJson(userAux, JUser.class);
            JGroup jg = new JGroup(name, description, user, date);
            jg.setGroupImage("default_android");
            jg.setTags(jtags);
            jg.setGroupType("public");

            jg.setGroupImage(image);

            Log.d(TAG, "json = " + gson.toJson(jg));

            NewGroup ng = new NewGroup(gson.toJson(jg));
            ng.execute();
        }

    }


    /**
     * intent to the MainActivity
     */
    public static void loadMainActivity() {
        Intent act = new Intent(context, MainActivity.class);
        act.putExtra("from", "fromnewgroup");
        context.startActivity(act);
    }
}

/**
 * Thread that sends a "post" query to Heroku and creates a new group
 */
class NewGroup extends AsyncTask<String,Integer,Boolean> {

    private static final String TAG = ListGroups.class.getSimpleName();

    String respStr;
    String jsonGroup="";

    public NewGroup(String jsonGroup) {
        this.jsonGroup = jsonGroup;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        boolean resul = true;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = null;
        try {
            post = new HttpPost("http://immense-badlands-1533.herokuapp.com/addedit/creategroup/"+URLEncoder.encode(jsonGroup, "UTF-8").replace("+", "%20"));
            try {
                HttpResponse resp = httpClient.execute(post);
                respStr = EntityUtils.toString(resp.getEntity());

            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setHeader("content-type", "application/json");



        return resul;
    }

    protected void onPostExecute(Boolean result) {
        if(result){
            Log.d(TAG, "json = " + respStr);

            try {
                NewGroupActivity.loadMainActivity();
            }catch(Exception e){
                e.printStackTrace();

            }
        }
    }




}

