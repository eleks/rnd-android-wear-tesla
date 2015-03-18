package com.eleks.tesla.mainApp.fragments;

import android.app.Fragment;

import com.eleks.tesla.mainApp.MainActivity;

/**
 * Created by maryan.melnychuk on 18.02.2015.
 */
public class BaseTeslaFragment extends Fragment {

    public MainActivity getOwnActivity(){
        return (MainActivity) getActivity();
    }
}
