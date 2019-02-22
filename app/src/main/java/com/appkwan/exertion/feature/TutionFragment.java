package com.appkwan.exertion.feature;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appkwan.exertion.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutionFragment extends Fragment {


    public TutionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tution, container, false);
    }

}
