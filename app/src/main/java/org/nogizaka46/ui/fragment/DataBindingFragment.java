package org.nogizaka46.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.nogizaka46.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataBindingFragment extends Fragment {


    public DataBindingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_binding, container, false);
    }

}
