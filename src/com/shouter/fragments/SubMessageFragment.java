package com.shouter.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shouter.activities.MainActivity;
import com.shouter.entities.JMessage;
import com.kikigames.menudemo2.R;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by admin on 28/05/2015.
 * This class returns a view with Submessages
 */

class SubmessagesAdapter extends ArrayAdapter<JMessage> {
    private static final String TAG = ListGroups.class.getSimpleName();
    Activity context;
    List<JMessage> messages;
    JMessage jmessage;


    SubmessagesAdapter(Activity context, List<JMessage> messages, JMessage jmessage) {
        super(context, R.layout.group_miniature_final, messages);
        this.context = context;
        this.messages = messages;
        this.jmessage = jmessage;
        this.messages.add(0, null);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.submessage_response, null);

        if (position == 0) {
            Log.d(TAG, " el jmessage padre es: "+jmessage);
            item = inflater.inflate(R.layout.menssage_responses, null);
            TextView messageTittle = (TextView) item.findViewById(R.id.messageTittle);
            messageTittle.setText(jmessage.getMessageTittle());
            TextView dateMessage = (TextView) item.findViewById(R.id.dateMessage);
            DateFormat df = new SimpleDateFormat("d"+" "+"MMMM"+" "+"yyyy");
            Date dayCreation =  jmessage.getMessageTsCreated();
            String CreationMessage = df.format(dayCreation);
            dateMessage.setText(CreationMessage);
            TextView userMessage = (TextView) item.findViewById(R.id.userMessage);
            userMessage.setText(jmessage.getMessageUser().getUserName());
            TextView nSubMessages = (TextView) item.findViewById(R.id.nSubMessages);
            nSubMessages.setText(jmessage.getResponses()+" respuestas");
            TextView messageText = (TextView) item.findViewById(R.id.messageText);
            messageText.setText(jmessage.getMessageText());

            ImageView im = (ImageView)item.findViewById(R.id.imageView);

            switch (jmessage.getMessageUser().getUserImage()) {
                case "calimero":
                    Picasso.with(context).load(R.drawable.calimero).into(im);
                    break;
                case "kiriki_azul":
                    Picasso.with(context).load(R.drawable.kiriki_azul).into(im);
                    break;
                case "kiriki_rojo":
                    Picasso.with(context).load(R.drawable.kiriki_rojo).into(im);
                    break;
                default:
                    Log.d("prueba picasso", "prueba picasso: " + jmessage.getMessageUser().getUserImage().replaceAll("__", "/"));
                    Picasso.with(context).load(jmessage.getMessageUser().getUserImage().replaceAll("__", "/")).into(im);
                    break;
            }

        } else{


            TextView dateMessage = (TextView) item.findViewById(R.id.dateMessage);

            DateFormat df = new SimpleDateFormat("d"+" "+"MMMM"+" "+"yyyy");
            Date dayCreation =  messages.get(position).getMessageTsCreated();
            String CreationMessage = df.format(dayCreation);
            dateMessage.setText(CreationMessage);
            TextView userMessage = (TextView) item.findViewById(R.id.userMessage);
            userMessage.setText(messages.get(position).getMessageUser().getUserName());
            TextView messageText = (TextView) item.findViewById(R.id.messageText);
            messageText.setText(messages.get(position).getMessageText());
            ImageView image = (ImageView)item.findViewById(R.id.imageView);
            Log.d("asdf", "imagen = " + messages.get(position).getMessageUser().getUserImage());




            if(messages.get(position).getMessageUser().getUserImage().equals("calimero")) {
                Picasso.with(context).load(R.drawable.calimero).into(image);

            }else if(messages.get(position).getMessageUser().getUserImage().equals("kiriki_azul")){
                Picasso.with(context).load(R.drawable.kiriki_azul).into(image);
            }else if(messages.get(position).getMessageUser().getUserImage().equals("kiriki_rojo")) {
                Picasso.with(context).load(R.drawable.kiriki_rojo).into(image);
            }else{
                Picasso.with(context).load(messages.get(position).getMessageUser().getUserImage().replaceAll("__", "/")).into(image);
                Log.d("picasso_prueba", messages.get(position).getMessageUser().getUserImage().replaceAll("__", "/"));
            }


        }

        return (item);


    }


}


/**
 * Thread that makes a Get query to Heroku and load the submessages
 */
class GetSubmessages extends AsyncTask<String, Integer, Boolean> {
    private static final String TAG = ListGroups.class.getSimpleName();

    private static Gson gson = new GsonBuilder().setDateFormat(
            "yyyy-MM-dd HH:mm:ss").create();
    String respStr;
    Activity activity;
    View view;
    protected List<JMessage> messages = new ArrayList<>();
    String jsonmessage;
    JMessage jmessage;


    public GetSubmessages(Activity activity, View view, String jsonmessage) {
        this.activity = activity;
        this.view = view;
        this.jsonmessage = jsonmessage;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        boolean resul = true;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet get;


        try {
            jmessage = gson.fromJson(jsonmessage, JMessage.class);
            get = new HttpGet("http://immense-badlands-1533.herokuapp.com/gets/getsubmessages/" + jmessage.getMessageId());
            get.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(get);
                respStr = EntityUtils.toString(resp.getEntity());

            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return resul;
    }

    protected void onPostExecute(Boolean result) {
        if (result) {
            Log.d(TAG, "json = " + respStr);

            try {
                messages = gson.fromJson(respStr, new TypeToken<ArrayList<JMessage>>() {
                }.getType());
                ListView listView1 = (ListView) view.findViewById(R.id.listView);
                SubmessagesAdapter adaptador = new SubmessagesAdapter(activity, messages, jmessage);
                listView1.setAdapter(adaptador);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}


/**
 * Created by Aitor on 22/5/15.
 *
 * Fragment that contains the submessages under the messages
 */
public class SubMessageFragment extends Fragment {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    String intent_message;
    static JMessage jmessage;
    EditText textSub;
    static Activity ct;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.submessages_fragment, container,
                false);
        Bundle bundle = this.getArguments();
        this.intent_message = bundle.getString("jsonmessage");
        jmessage = gson.fromJson(intent_message, JMessage.class);
        ct = getActivity();
        GetSubmessages gg = new GetSubmessages(ct, view, intent_message);
        gg.execute();
        textSub = (EditText)view.findViewById(R.id.text_sub);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return view;
    }

//    public static void callChangeToSubMessages(Activity a, String jmessage) {
//        MainActivity activity = (MainActivity) a;
//        activity.loadSubmessageFragment(jmessage);
//
//
//    }

    /**
     * method that makes an intent to MainActivity
     */
    public static void loadMainActivity() {
        Intent act = new Intent(ct, MainActivity.class);
        act.putExtra("jmessage", gson.toJson(jmessage));
        ct.startActivity(act);
    }




}


