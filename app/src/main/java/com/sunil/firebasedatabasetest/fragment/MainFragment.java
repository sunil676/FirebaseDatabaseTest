package com.sunil.firebasedatabasetest.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sunil.firebasedatabasetest.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sunil on 11/6/16.
 */

public class MainFragment extends Fragment {

    public static final String TAG = MainFragment.class.getSimpleName();
    private View rootView;

    @BindView(R.id.signup)
    Button buttonSignUp;
    @BindView(R.id.signIn)
    Button buttonSignIn;
    @BindView(R.id.getUser)
    Button buttonGetUser;

    OnFragmentInteractionListener mListener;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public MainFragment() {
        // Required MyFundsCategoriesFragment public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */


    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /*
     * Called when the fragment attaches to the activity
     */
    protected void onAttachToContext(Context context) {
        Activity activity = (Activity) context;
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.signIn)
    public void onClickSignIn(){

        mListener.onSignClick();
    }

    @OnClick(R.id.signup)
    public void onClickSignUp(){

        mListener.onSignUpClick();
    }

    @OnClick(R.id.getUser)
    public void onClickGetUsers(){
        mListener.getUserClicked();
    }

    public interface OnFragmentInteractionListener {
        void onSignClick();
        void onSignUpClick();
        void getUserClicked();
    }
}
