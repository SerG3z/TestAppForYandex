package com.example.serg.testwork.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.serg.testwork.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by serg3z on 15.04.16.
 * <p/>
 * Show this fragment if connection error
 */
public class ErrorConnectionFragment extends Fragment {

    private ErrorConnectionFragmentListener listener;

    public static ErrorConnectionFragment newInstance() {
        return new ErrorConnectionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.error_network, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Activity activity = getActivity();
        if (activity instanceof ErrorConnectionFragmentListener) {
            this.listener = (ErrorConnectionFragmentListener) activity;
        }
    }

    @OnClick(R.id.button_repeat)
    void onClickButtonRepeat() {
        if (listener != null) {
            listener.onErrorConnectiontClicked();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public interface ErrorConnectionFragmentListener {
        void onErrorConnectiontClicked();
    }

}
