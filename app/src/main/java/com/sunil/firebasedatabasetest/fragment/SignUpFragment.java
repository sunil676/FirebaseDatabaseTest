package com.sunil.firebasedatabasetest.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sunil.firebasedatabasetest.R;
import com.sunil.firebasedatabasetest.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sunil on 11/6/16.
 */

public class SignUpFragment extends Fragment{

    public static final String TAG = SignUpFragment.class.getSimpleName();
    private View rootView;

    @BindView(R.id.SignUp)
    Button buttonSignUp;

    @BindView(R.id.FirstNameLayout)
    TextInputLayout FirstNameLayout;
    @BindView(R.id.secondNameLayout)
    TextInputLayout secondNameLayout;
    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;
    @BindView(R.id.mobileLayout)
    TextInputLayout mobileLayout;
    @BindView(R.id.passwordLayout)
    TextInputLayout passwordLayout;


    @BindView(R.id.firstName)
    EditText firstNameEditText;
    @BindView(R.id.lastName)
    EditText lastNameEditText;
    @BindView(R.id.email)
    EditText emailEditText;
    @BindView(R.id.mobile)
    EditText mobileEditText;
    @BindView(R.id.password)
    EditText passwordEditText;

    OnFragmentInteractionListener mListener;
    private FirebaseAuth mFirebaseAuth;

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
    }

    public SignUpFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_signup, container, false);
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


    @OnClick(R.id.SignUp)
    public void onClickSignUp(){
        if (!valid()){
            return;
        }else {

            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                      mListener.onSignUpDone();
                    }else{
                        Utility.showDialog(getActivity(), task);
                    }

                }
            });
        }
    }


    public interface OnFragmentInteractionListener {
        void onSignUpDone();
    }

    private boolean valid(){
        boolean isValid;
        if (Utility.nullCheck(FirstNameLayout, "First Name")){
            isValid = false;
        }
        else if (Utility.nullCheck(secondNameLayout, "Last Name")){
            isValid = false;
        }
        else if (!Utility.validateEmail(emailLayout)){
            isValid = false;
        }
        else if (!Utility.validateMobile(mobileLayout)){
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
