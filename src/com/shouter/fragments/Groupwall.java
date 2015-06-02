package com.shouter.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kikigames.menudemo2.R;
import com.shouter.activities.MainActivity;
import com.shouter.entities.JGroup;
import com.shouter.entities.JMessage;
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
 * This class returns a view with the messages of a group
 */
class GroupMessagesAdapter extends ArrayAdapter<JMessage> {

    Activity context;
    List<JMessage> messages;
    JGroup jgroup;

    GroupMessagesAdapter(Activity context, List<JMessage> messages, JGroup jgroup) {
        super(context, R.layout.group_miniature_final, messages);
        this.context = context;
        this.messages = messages;
        this.jgroup = jgroup;
        this.messages.add(0, null);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.menssage_responses, null);

        if (position == 0) {

            item = inflater.inflate(R.layout.grupo_intro, null);
            TextView groupTittle = (TextView) item.findViewById(R.id.groupTittle);
            groupTittle.setText(jgroup.getGroupName());

            TextView nMembers = (TextView) item.findViewById(R.id.nMembers);
            TextView nMessages = (TextView) item.findViewById(R.id.nMessages);

            nMembers.setText(jgroup.getnUsers()+" miembros");
            nMessages.setText(jgroup.getnMessages()+" mensajes");
            //Hacer que al clicar muestre los users_ongroup
            Button button = (Button) item.findViewById(R.id.button);

            ImageView im = (ImageView)item.findViewById(R.id.imageView4);


            switch(jgroup.getGroupImage()){
                case "bcn":
                    im.setBackgroundResource(R.drawable.bcn);
                    break;
                case "party2":
                    im.setBackgroundResource(R.drawable.party2);
                    break;
                case "mountain":
                    im.setBackgroundResource(R.drawable.mountain);
                    break;
                default:
                    im.setBackgroundResource(R.drawable.bcn);
                    break;


            }


            if(MainActivity.subscriptions.containsKey(jgroup.getGroupId())){
                button.setText("Eliminar suscripcion");
            }
        } else if (messages.get(position).getIdParent() == 1) {

            TextView messageTittle = (TextView) item.findViewById(R.id.messageTittle);
            messageTittle.setText(messages.get(position).getMessageTittle());
            TextView dateMessage = (TextView) item.findViewById(R.id.dateMessage);

            DateFormat df = new SimpleDateFormat("d"+" "+"MMMM"+" "+"yyyy");


            Date dayCreation =  messages.get(position).getMessageTsCreated();

            String CreationMessage = df.format(dayCreation);

            dateMessage.setText(CreationMessage);


            TextView userMessage = (TextView) item.findViewById(R.id.userMessage);
            userMessage.setText(messages.get(position).getMessageUser().getUserName());

            TextView nSubMessages = (TextView) item.findViewById(R.id.nSubMessages);

            nSubMessages.setText(messages.get(position).getResponses() + " respuestas");

            TextView messageText = (TextView) item.findViewById(R.id.messageText);
            messageText.setText(messages.get(position).getMessageText());

            ImageView image = (ImageView)item.findViewById(R.id.imageView);
            Log.d("asdf", "imagen = " + messages.get(position).getMessageUser().getUserImage());




            if(messages.get(position).getMessageUser().getUserImage().equals("calimero")) {
                Picasso.with(context).load(R.drawable.calimero).into(image);
            }else if(messages.get(position).getMessageUser().getUserImage().equals("kiriki_azul")) {
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
 * Thread that sends a Get query to Heroku and load the messages of a group
 */
class GetGroupMessages extends AsyncTask<String, Integer, Boolean> {
    private static final String TAG = ListGroups.class.getSimpleName();

    private static Gson gson = new GsonBuilder().setDateFormat(
            "yyyy-MM-dd HH:mm:ss").create();
    String respStr;
    Activity activity;
    View view;
    protected List<JMessage> messages = new ArrayList<>();
    String jsongroup;
    JGroup jgroup;



    public GetGroupMessages(Activity activity, View view, String jsongroup) {
        this.activity = activity;
        this.view = view;
        this.jsongroup = jsongroup;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        boolean resul = true;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet get;


        try {
            jgroup = gson.fromJson(jsongroup, JGroup.class);
            get = new HttpGet("http://immense-badlands-1533.herokuapp.com/gets/getgroupmessages/" + jgroup.getGroupId());
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
                GroupMessagesAdapter adaptador = new GroupMessagesAdapter(activity, messages, jgroup);
                listView1.setAdapter(adaptador);

                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.d(TAG, "he clickado el item "+i);
                        MainActivity.jmessage = messages.get(i);
                        Groupwall.jmessage = messages.get(i);
                        Groupwall.callChangeToSubMessages((Activity) view.getContext(), gson.toJson(messages.get(i)));


                    }


                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}


/**
 * Created by Aitor on 22/5/15.
 * The fragment that contains the messages of a group
 */
public class Groupwall extends Fragment {
    private static final String TAG = ListGroups.class.getSimpleName();
    private static Gson gson = new GsonBuilder().setDateFormat(
            "yyyy-MM-dd HH:mm:ss").create();
      public static JGroup jgroup;
    public static JMessage jmessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_groups, container,
                false);
        Bundle bundle = this.getArguments();
        String intent_group = bundle.getString("jsongroup");
        try{
            jgroup = gson.fromJson(intent_group, JGroup.class);
        }catch(Exception e){
            e.printStackTrace();
        }
        Activity ct = getActivity();
        GetGroupMessages gg = new GetGroupMessages(ct, view, intent_group);
        gg.execute();
        MainActivity.jmessage = null;
        ((MainActivity) getActivity()).setActionBarTitle(jgroup.getGroupName());

        return view;
    }


    /**
     * load the method loadSubmessageFragment of the MainActivity
     * @param a activity
     * @param jmessage json JMessage
     */
    public static void callChangeToSubMessages(Activity a, String jmessage){

        Log.d(TAG, "estoy dentro del callChangeToSubMessages");
        MainActivity activity = (MainActivity) a;
        activity.loadSubmessageFragment(jmessage);


    }


    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar_addmessage, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.add_message:

               MainActivity.callNewMessage();




                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}