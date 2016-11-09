package com.sunil.firebasedatabasetest.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sunil.firebasedatabasetest.R;
import com.sunil.firebasedatabasetest.utils.SharedPreferenceUtils;
import com.sunil.firebasedatabasetest.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sunil on 11/6/16.
 */

public class SignInFragment extends Fragment {

    public static final String TAG = SignInFragment.class.getSimpleName();
    private View rootView;

    OnFragmentInteractionListener mListener;
    private FirebaseAuth mFirebaseAuth;

    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;
    @BindView(R.id.passwordLayout)
    TextInputLayout passwordLayout;

    @BindView(R.id.email)
    EditText emailEditText;
    @BindView(R.id.password)
    EditText passwordEditText;


    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    public SignInFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_signin, container, false);
        ButterKnife.bind(this, rootView);

        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();

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

    public interface OnFragmentInteractionListener {
        void onSignInDone();
    }

    @OnClick(R.id.signIn)
    public void getSignInClick(){
        if (!valid()){
            return;
        }else {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser user = task.getResult().getUser();
                                Log.d(TAG, "onComplete: uid=" + user.getUid());
                                SharedPreferenceUtils.getInstance(getActivity()).setUUID(user.getUid());
                                SharedPreferenceUtils.getInstance(getActivity()).setEmail(user.getEmail());
                                mListener.onSignInDone();
                            }else{
                                Utility.showDialog(getActivity(), task);
                            }

                        }
                    });
        }
    }

    private boolean valid(){
        boolean isValid;
        if (!Utility.validateEmail(emailLayout)){
            isValid = false;
        }
        else if (Utility.nullCheck(passwordLayout, "Password")){
            isValid = false;
        }else{
            isValid = true;
        }
        return  isValid;
    }
}
