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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shouter.activities.MainActivity;
import com.shouter.entities.JGroup;
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
 * This class returns a view with groups
 */
class GroupAdapter extends ArrayAdapter<JGroup> {

    Activity context;
    List<JGroup> groups;



    GroupAdapter(Activity context, List<JGroup> groups) {
        super(context, R.layout.group_miniature_final, groups);
        this.context = context;
        this.groups = groups;
    }



    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater;
        inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.group_miniature_final, null);


        TextView nameGroup = (TextView)item.findViewById(R.id.nameGroup);
        nameGroup.setText(groups.get(position).getGroupName());


        DateFormat df;
        df = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");

        Date dayCreation =  groups.get(position).getGroupTsCreation();
        String salida = df.format(dayCreation);

        TextView dateCrGroup = (TextView)item.findViewById(R.id.dateCrGroup);

        dateCrGroup.setText(salida);

        TextView nMembers = (TextView)item.findViewById(R.id.nMembers);
        nMembers.setText(groups.get(position).getnUsers() + "miembros");

        TextView nMessages = (TextView)item.findViewById(R.id.nMessages);
        nMessages.setText(groups.get(position).getnMessages()+" mensajes");


        ImageView im = (ImageView)item.findViewById(R.id.imageView3);

        if(groups.get(position).getGroupImage().equals("bcn")) {
            Picasso.with(context).load(R.drawable.bcn).into(im);
        }else if(groups.get(position).getGroupImage().equals("party2")) {
            Picasso.with(context).load(R.drawable.party2).into(im);
        }else if(groups.get(position).getGroupImage().equals("mountain")) {
            Picasso.with(context).load(R.drawable.mountain).into(im);
        }else{
            Picasso.with(context).load(R.drawable.bcn).into(im);
        }

        return(item);
    }
}

/**
 * Thread that sends a Get query to Heroku and load the groups
 */
class GetGroups extends AsyncTask<String,Integer,Boolean> {
    private static final String TAG = ListGroups.class.getSimpleName();

    private static Gson gson = new GsonBuilder().setDateFormat(
            "yyyy-MM-dd HH:mm:ss").create();
    String respStr;
    Activity activity;
    View view;

    protected List<JGroup> groups = new ArrayList<>();



    public GetGroups(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        boolean resul = true;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet get = new HttpGet("http://immense-badlands-1533.herokuapp.com/gets/getallgroups");
        get.setHeader("content-type", "application/json");

        try {
            HttpResponse resp = httpClient.execute(get);
            respStr = EntityUtils.toString(resp.getEntity());

        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
            resul = false;
        }

        return resul;
    }

    protected void onPostExecute(Boolean result) {
        if(result){
            Log.d(TAG, "json = " + respStr);

            try {
                groups = gson.fromJson(respStr, new TypeToken<ArrayList<JGroup>>() {
                }.getType());
                ListView listView1 = (ListView) view.findViewById(R.id.listView);
                GroupAdapter adaptador = new GroupAdapter(activity, groups);
                listView1.setAdapter(adaptador);

                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener(){


                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ListMyGroups.callChangeToGroupWall((Activity)view.getContext(), gson.toJson(groups.get(i)));


                    }


                });


            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }



}


/**
 * Created by Aitor on 22/5/15.
 *The fragment that contains the groups
 *
 */
public class ListGroups extends Fragment{

    public ListGroups() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_msg, container,
                false);

        Activity ct = getActivity();
        GetGroups gg = new GetGroups(ct, view);
        gg.execute();

        ((MainActivity) getActivity()).setActionBarTitle("Grupos");
        return view;
    }


    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar_addgroup, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.add_group:

                //aqui añadir el newgroup

                MainActivity.callNewGroup();




                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
