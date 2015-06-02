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
import com.kikigames.menudemo2.R;
import com.shouter.entities.JGroup;
import com.shouter.entities.JMessage;
import com.shouter.entities.JUser;
import com.shouter.fragments.Groupwall;
import com.shouter.fragments.ListGroups;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * This activity calls the thread that creates a new message
 */
public class NewMessageActivity extends Activity {


    private static final String TAG = ListGroups.class.getSimpleName();
    EditText messageTittle;
    EditText messageText;
    String msgtittle;
    String msgtext;
    public static Context context;
    public static JGroup jgroup;

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public NewMessageActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);


        ActionBar bar = getActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff30b7ff")));
        bar.setIcon(R.drawable.ic_onactionbar);
        bar.setTitle("");

        messageTittle = (EditText) findViewById(R.id.msgtittle);
        messageText = (EditText) findViewById(R.id.msgtext);
        jgroup = Groupwall.jgroup;
    }

    /**
     * We check if the editText is empty or not by checking its length
     *
     * @param etText editText
     * @return true if it's empty
     */
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }

    /**
     * Validates Both edit texts and calls NewMessage thread
     * * @param v view
     */
    public void onClickSendMessage(View v) {

        String error = "";

        if (isEmpty(messageTittle)) {
            error = "entra un titulo valido";
        } else if (isEmpty(messageText)) {
            error = "entra un mensaje valido";
        } else {
            msgtittle = messageTittle.getText().toString();
            msgtext = messageText.getText().toString();

        }

        if (!error.equals("")) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        } else {
            try {
                JMessage jmsg;
                jmsg = new JMessage(msgtext, msgtittle, gson.fromJson(MainActivity.juser, JUser.class), 1, jgroup.getGroupId());
                jmsg.setIdParent(1);


                Log.d(TAG, "este es el jmessage a crear =" + jmsg);
                NewMessage nm = new NewMessage(gson.toJson(jmsg));
                nm.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method that allows users to go back when they press the button return in their phones
     */
    @Override
    public void onBackPressed() {
        //do whatever you want the 'Back' button to do
        //as an example the 'Back' button is set to start a new Activity named 'NewActivity'
        this.startActivity(new Intent(NewMessageActivity.this, MainActivity.class));
        finish();
    }

    /**
     * intent that loads the MainActivity
     */
    public static void loadMainActivity() {
        Intent act = new Intent(context, MainActivity.class);
        act.putExtra("jsongroup", gson.toJson(jgroup));
        context.startActivity(act);
    }


}


/**
 * Thread that sends a Post query to Heroku that creates a new Message in a group
 */
class NewMessage extends AsyncTask<String,Integer,Boolean> {

    private static final String TAG = ListGroups.class.getSimpleName();
    String respStr;
    String jsonMessage="";
    public NewMessage(String jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        boolean resul = true;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post;

        try {


            String request = "http://immense-badlands-1533.herokuapp.com/addedit/createmessage/"+URLEncoder.encode(jsonMessage, "UTF-8").replace("+", "%20");
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
                       NewMessageActivity.loadMainActivity();
                   }
               }else {
                    Toast.makeText(NewMessageActivity.context, "Server down", Toast.LENGTH_SHORT).show();
                }


            }


}