package com.shouter.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kikigames.menudemo2.R;
import com.shouter.entities.JGroup;
import com.shouter.entities.JMessage;
import com.shouter.entities.JUser;
import com.shouter.fragments.ChangePhoto;
import com.shouter.fragments.CustomDrawerAdapter;
import com.shouter.fragments.DrawerItem;
import com.shouter.fragments.FragmentOne;
import com.shouter.fragments.Groupwall;
import com.shouter.fragments.ListGroups;
import com.shouter.fragments.ListMyGroups;
import com.shouter.fragments.MyAdminGroups;
import com.shouter.fragments.NewSubMessage;
import com.shouter.fragments.ProfileFragment;
import com.shouter.fragments.SubMessageFragment;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * This class subscribes or usubscribes the user by sending two different "put" querys to Heroku
 * and changing the button text name to one or another
 *
 */
class Subscribe extends AsyncTask<String, Integer, Boolean> {

    private static final String TAG = ListGroups.class.getSimpleName();
    JUser juser;
    JGroup jgroup;
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    Boolean done = false;
    String respStr;
    View view;
    Button button;

    public Subscribe(View v){
        this.view = v;
        button = (Button)v.findViewById(R.id.button);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Log.d(TAG, "doinbackground");
        HttpClient httpClient = new DefaultHttpClient();
        HttpPut put = new HttpPut("error");

        try {
            this.juser = gson.fromJson(MainActivity.juser, JUser.class);
            this.jgroup = Groupwall.jgroup;

            if(juser != null){
                try{

                    if(button.getText().toString().equals("Suscribirse")){
                        put = new HttpPut("http://immense-badlands-1533.herokuapp.com/addedit/subscribe/"+juser.getUserId()+"/"+jgroup.getGroupId());
                        put.setHeader("content-type", "application/json");

                    }else if(button.getText().toString().equals("Eliminar suscripcion")){
                        put = new HttpPut("http://immense-badlands-1533.herokuapp.com/addedit/unsubscribe/"+juser.getUserId()+"/"+jgroup.getGroupId());
                        put.setHeader("content-type", "application/json");
                    }

                    try {
                        HttpResponse resp = httpClient.execute(put);
                        respStr = EntityUtils.toString(resp.getEntity());
                    } catch (Exception ex) {
                        Log.e("ServicioRest", "Error subscribing", ex);

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

                done = true;

            }

        } catch (Exception e) {
            Log.d(TAG, "pet");


        }

        return done;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        if(!done){
            Log.d(TAG, "ha fallado el thread");
            Toast.makeText(MainActivity.context, "Haz login para poder suscribirte", Toast.LENGTH_SHORT).show();

        }else{
           // jgroup = gson.fromJson(respStr, JGroup.class);
            Log.d(TAG, "se ha suscrito en =" + respStr);


            if(button.getText().toString().equals("Suscribirse")){
                if(!MainActivity.subscriptions.containsKey(jgroup.getGroupId())){
                    MainActivity.subscriptions.put(jgroup.getGroupId(), true);
                    ListMyGroups.callChangeToGroupWall((Activity) MainActivity.context, respStr);
                }

            }else if(button.getText().toString().equals("Eliminar suscripcion")){
                if(MainActivity.subscriptions.containsKey(jgroup.getGroupId())){
                    MainActivity.subscriptions.remove(jgroup.getGroupId());
                    button.setText("Suscribirse");
                }
            }







        }
    }
}


public class MainActivity extends Activity {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private static final String TAG = MainActivity.class.getSimpleName();
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    public static String juser;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;
    public static Context context;
    public static JMessage jmessage;
    List<DrawerItem> dataList = new ArrayList<>();
    EditText textSub;
    public static HashMap<Integer, Boolean> subscriptions = new HashMap<>();
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        context = this;


        ActionBar bar = getActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff30b7ff")));
        bar.setIcon(R.drawable.ic_onactionbar);
        bar.setTitle("");

        mTitle = mDrawerTitle = getTitle();
         mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
         mDrawerList = (ListView) findViewById(R.id.left_drawer);

         mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                 GravityCompat.START);
         
         //dataList.add(new DrawerItem(true)); // adding a spinner to the list

        dataList.add(new DrawerItem("General"));

        dataList.add(new DrawerItem("Perfil", R.drawable.ic_action_labels));
        dataList.add(new DrawerItem("Suscripciones", R.drawable.ic_action_group));
        dataList.add(new DrawerItem("Mis Grupos", R.drawable.ic_action_group));
        dataList.add(new DrawerItem("Grupos", R.drawable.ic_action_group));

        dataList.add(new DrawerItem("Otras opciones")); // adding a header to the list
        dataList.add(new DrawerItem("Ajustes", R.drawable.ic_action_settings));
        dataList.add(new DrawerItem("Ayuda", R.drawable.ic_action_help));
         
         
         adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                 dataList);

     mDrawerList.setAdapter(adapter);
     
     mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
     
     getActionBar().setDisplayHomeAsUpEnabled(true);
     getActionBar().setHomeButtonEnabled(true);
      
     mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                 R.drawable.ic_drawer, R.string.drawer_open,
                 R.string.drawer_close) {
           public void onDrawerClosed(View view) {
                 getActionBar().setTitle(mTitle);
                 invalidateOptionsMenu(); // creates call to
                                                           // onPrepareOptionsMenu()
           }
      
           public void onDrawerOpened(View drawerView) {
                 getActionBar().setTitle(mDrawerTitle);
                 invalidateOptionsMenu(); // creates call to
                                                           // onPrepareOptionsMenu()
           }
     };
      
     mDrawerLayout.setDrawerListener(mDrawerToggle);
      
     if (savedInstanceState == null) {
        SelectItem(0);
  }
     
     
	}

	public void SelectItem(int possition) {
		 
        Fragment fragment;
        Bundle args = new Bundle();
        switch (possition) {

            case 0:
                fragment = new ListMyGroups();
                args.putString(FragmentOne.ITEM_NAME, dataList.get(possition)// FRAGMENTONE.ITEM....
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());

                break;
            case 1:
                fragment = new ProfileFragment();
                args.putString(FragmentOne.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList
                        .get(possition).getImgResID());

                break;

            case 2:
                fragment = new ListMyGroups();
                args.putString(FragmentOne.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;
            case 3:
                fragment = new MyAdminGroups();
                args.putString(FragmentOne.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;

            case 4:
                fragment = new ListGroups();
                args.putString(FragmentOne.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;

            default:
                fragment = new FragmentOne();
                args.putString(FragmentOne.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;
        }

        args.putString("jsonLogin", juser);
        fragment.setArguments(args);
        FragmentManager frgManager = getFragmentManager();
        frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                    .commit();

        mDrawerList.setItemChecked(possition, true);
        setTitle(dataList.get(possition).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);

  }



    /**
     * This method load the GroupWall in his fragment
     * @param groupJson Json of the JGroup
     */
	public void loadGroupWall(String groupJson){
        Fragment fragment;
        fragment = new Groupwall();
        Bundle args = new Bundle();
        args.putString("jsongroup", groupJson);
        fragment.setArguments(args);
        FragmentManager frgManager = getFragmentManager();
        frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                .commit();


    }

	@Override
	public void setTitle(CharSequence title) {
	      mTitle = title;


            getActionBar().setTitle(mTitle);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
	      super.onPostCreate(savedInstanceState);
	      // Sync the toggle state after onRestoreInstanceState has occurred.
	      mDrawerToggle.syncState();
	}

	@Override//
	public boolean onOptionsItemSelected(MenuItem item) {
	      // The action bar home/up action should open or close the drawer.
	      // ActionBarDrawerToggle will take care of this.
        return mDrawerToggle.onOptionsItemSelected(item);

    }


    /**
     * Starts the activity NewMessageActivity to write a new message in the group
     */
    public static void callNewMessage() {
        String jsonGroup="";
        Intent act = new Intent(context, NewMessageActivity.class);
        act.putExtra("json", jsonGroup);

        context.startActivity(act);

    }

    /**
     * Starts the activity NewGroupActivity to create a new group
     */
    public static void callNewGroup() {
        Intent act = new Intent(context, NewGroupActivity.class);

        context.startActivity(act);
    }




	@Override//
	public void onConfigurationChanged(Configuration newConfig) {
	      super.onConfigurationChanged(newConfig);
	      // Pass any configuration change to the drawer toggles
	      mDrawerToggle.onConfigurationChanged(newConfig);
	}



    private class DrawerItemClickListener implements ListView.OnItemClickListener {
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (dataList.get(position).getTitle() == null) {
                SelectItem(position);
          }

}
}

    /**
     * Load the submessages in his fragment
     * @param jmessage json of JMessage
     */
    public void loadSubmessageFragment(String jmessage) {
        Fragment fragment;
        fragment = new SubMessageFragment();
        Bundle args = new Bundle();
        args.putString("jsonmessage", jmessage);
        fragment.setArguments(args);
        FragmentManager frgManager = getFragmentManager();
        frgManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        Log.d(TAG, "estoy en el loadSubMessageFragment");
    }

    /**
     * Starts the Method Subscribe
     * @param v view
     */
    public void onClickSubscribe(View v){
        Subscribe s = new Subscribe(v);
        s.execute();

    }


    /**
     * load the groups in the fragment ListMyGroups();
     */
    public void loadMyGroups(){
        Fragment fragment = null;
        Bundle args = new Bundle();
        args.putString("jsonLogin", juser);
        fragment.setArguments(args);
        fragment = new ListMyGroups();
        FragmentManager frgManager = getFragmentManager();
        frgManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        Log.d(TAG, "estoy en el loadSubMessageFragment");
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            String group = (String)getIntent().getSerializableExtra("jsongroup");
            String message = (String)getIntent().getSerializableExtra("jmessage");
            String from = (String)getIntent().getSerializableExtra("from");
            String newmes = (String)getIntent().getSerializableExtra("json_from_new_mes");
            Log.d(TAG, "EL GRUPO A VOLVER A CARGAR: "+group);
            if ( group != null){
                loadGroupWall(group);

            }

            if (message != null){
                loadSubmessageFragment(message);
            }

            if(from.equals("fromnewgroup")){
                loadMyGroups();
            }

            if(newmes != null){
                loadGroupWall("json_from_new_mes");

            }
        }catch(Exception e){
            e.printStackTrace();
        }    
    }

    public  void setActionBarTitle(String title){
        ActionBar ab = getActionBar();
        ab.setTitle(title);
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
     * Validates the message content and sends it to a thread
     * that makes a query to Heroku and add the new submessage
     * @param view view
     */
    public void onClickSubmessage(View view) {
        String error ="";
        String msgtext="";
        this.textSub = (EditText)findViewById(R.id.text_sub);
       // Toast.makeText(this, textSub.getText().toString(), Toast.LENGTH_SHORT).show();
         JMessage jmsg;

        if(isEmpty(textSub)){
            error = "entra una respesta valida";
        }else{
           msgtext = textSub.getText().toString();
        }

        if(!error.equals("")){
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }else {
            String msgtittle = "Es un submensaje";
            jmsg = new JMessage(msgtext, msgtittle, gson.fromJson(MainActivity.juser, JUser.class), jmessage.getMessageId(), Groupwall.jgroup.getGroupId());
            Log.d(TAG, "submensaje: " + jmsg);
            NewSubMessage nm = new NewSubMessage(gson.toJson(jmsg));
            nm.execute();
        }

    }

    public void onClickEditProfile(View view){
        RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup);
        int checked = rg.getCheckedRadioButtonId();
        Toast.makeText(this, String.valueOf(checked), Toast.LENGTH_SHORT);
        EditText url;
        url = (EditText)findViewById(R.id.url_photo);
        String image;
        ChangePhoto cp;
        if(checked == R.id.radio1) {
            image = "kiriki_rojo";
            cp = new ChangePhoto(image);
            cp.execute();
        }else if (checked == R.id.radio2){
            image = "kiriki_azul";
            cp = new ChangePhoto(image);
            cp.execute();
        }else if (checked == R.id.radio3){
            image = "calimero";
            cp = new ChangePhoto(image);
            cp.execute();
        }else if (checked == R.id.radio4){

            if(!isEmpty(url)){

                if(url.getText().toString().contains("imgur")){
                    image = url.getText().toString().replaceAll("/", "__");
                    cp = new ChangePhoto(image);
                    cp.execute();
                }
                else{
                    Toast.makeText(this, "el enlace debe ser de imgur", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(this, "Introduce una url", Toast.LENGTH_SHORT).show();

            }
        }







    }


    private static long back_pressed;
    @Override
    public void onBackPressed()
    {
        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
        else Toast.makeText(getBaseContext(), "Pulsa de nuevo para salir", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        back_pressed = System.currentTimeMillis();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        Fragment fragment;
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if(MainActivity.jmessage != null){

                fragment = new Groupwall();
                Bundle args = new Bundle();
                args.putString("jsongroup", gson.toJson(Groupwall.jgroup));
                fragment.setArguments(args);
                FragmentManager frgManager = getFragmentManager();
                frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                        .commit();

            }else {

                fragment = new ListMyGroups();
                Bundle args = new Bundle();
                args.putString("jsongroup", gson.toJson(Groupwall.jgroup));
                fragment.setArguments(args);
                FragmentManager frgManager = getFragmentManager();
                frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                        .commit();
            }
        }
        return super.onKeyDown(keyCode, event);
    }




}
