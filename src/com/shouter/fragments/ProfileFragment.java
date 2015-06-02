package com.shouter.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kikigames.menudemo2.R;
import com.shouter.activities.MainActivity;
import com.shouter.entities.JUser;



public class ProfileFragment extends Fragment {
    static Context context;

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    JUser juser = gson.fromJson(MainActivity.juser, JUser.class);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.user_profile_fragment, container, false);
        context = getActivity();
        TextView userName = (TextView) view.findViewById(R.id.profile_name);
        TextView userEmail = (TextView) view.findViewById(R.id.profile_mail);
        userName.setText(juser.getUserName());
        userEmail.setText(juser.getUserEmail());
        RadioGroup rg = (RadioGroup) view.findViewById(R.id.radioGroup);

        switch (juser.getUserImage()) {
            case "kiriki_rojo":
                Log.d("asdf", juser.getUserImage());
                rg.check(R.id.radio1);
                break;
            case "kiriki_azul":
                Log.d("asdf", juser.getUserImage());
                rg.check(R.id.radio2);
                break;
            case "calimero":
                Log.d("asdf", juser.getUserImage());
                rg.check(R.id.radio3);

                break;
        }

        return view;


    }


}
