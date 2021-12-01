package com.fragment;

import androidx.fragment.app.Fragment;

import com.slsindupotha.MyApplication;

public class BaseFragment extends Fragment {
    private MyApplication myApplication;

    public MyApplication getApp() {
        if (getActivity() != null) {
            if (myApplication == null) {
                myApplication = (MyApplication) getActivity().getApplication();
            }
            return myApplication;
        } else {
            return null;
        }
    }
}
