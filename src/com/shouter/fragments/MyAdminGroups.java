package com.shouter.fragments;

/**
 * Created by Alex on 31/05/2015.
 *
 * This class returns a view with groups that owns the user
 */

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
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kikigames.menudemo2.R;
import com.shouter.activities.MainActivity;
import com.shouter.entities.JGroup;
import com.shouter.entities.JUser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Thread that sends a Get query to Heroku and load the groups that owns the user
 */
class GetAdminGroups extends AsyncTask<String,Integer,Boolean> {
    private static final String TAG = ListGroups.class.getSimpleName();

    private static Gson gson = new GsonBuilder().setDateFormat(
            "yyyy-MM-dd HH:mm:ss").create();
    String respStr;
    Activity activity;
    View view;
    int iduser;
    protected List<JGroup> groups = new ArrayList<>();


    public GetAdminGroups(Activity activity, View view, int iduser) {
        this.activity = activity;
        this.view = view;
        this.iduser = iduser;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        boolean resul = true;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet get = new HttpGet("http://immense-badlands-1533.herokuapp.com/gets/getowngroups/"+iduser);
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
 * Created by Alex on 31/05/2015.
 * The fragment that contains the groups that owns the user
 */
public class MyAdminGroups extends Fragment {
    private static final String TAG = ListGroups.class.getSimpleName();

    private static Gson gson = new GsonBuilder().setDateFormat(
            "yyyy-MM-dd HH:mm:ss").create();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_msg, container,
                false);

        Activity ct = getActivity();
        try{
            JUser juser = gson.fromJson(MainActivity.juser, JUser.class);
            Log.d(TAG, "EL objeto en el fragment ="+juser.toString());
            GetAdminGroups gag = new GetAdminGroups(ct, view, juser.getUserId());
            gag.execute();
        }catch(Exception e){
            e.printStackTrace();
        }



        ((MainActivity) getActivity()).setActionBarTitle("Mis Grupos");
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

                //aqui a�adir el newgroup

                MainActivity.callNewGroup();




                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}