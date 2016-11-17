package com.sunil.firebasedatabasetest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.sunil.firebasedatabasetest.fragment.AddNoteFragment;
import com.sunil.firebasedatabasetest.fragment.MainFragment;
import com.sunil.firebasedatabasetest.fragment.SignInFragment;
import com.sunil.firebasedatabasetest.fragment.SignUpFragment;
import com.sunil.firebasedatabasetest.fragment.UserFragment;
import com.sunil.firebasedatabasetest.fragment.UserNotesFragment;
import com.sunil.firebasedatabasetest.utils.SharedPreferenceUtils;
import com.sunil.firebasedatabasetest.utils.Utility;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener,
        SignUpFragment.OnFragmentInteractionListener,
        SignInFragment.OnFragmentInteractionListener,
        UserNotesFragment.OnFragmentInteractionListener,
        AddNoteFragment.OnFragmentInteractionListener,
        UserFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState== null){
            callMainFragment();
        }
    }

    private void callMainFragment() {
        Fragment fragment = MainFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(MainFragment.TAG).replace(R.id.fram_container, fragment, MainFragment.TAG).commit();
    }

    @Override
    public void onSignClick() {
        Fragment fragment = SignInFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(SignInFragment.TAG).replace(R.id.fram_container, fragment, SignInFragment.TAG).commit();

        }

    @Override
    public void onSignUpClick() {
            Fragment fragment = SignUpFragment.newInstance();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(SignUpFragment.TAG).replace(R.id.fram_container, fragment, SignUpFragment.TAG).commit();
        }

    @Override
    public void getUserClicked() {
        if (SharedPreferenceUtils.getInstance(this).getUUID()!= null) {
            Fragment fragment = UserNotesFragment.newInstance();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(UserNotesFragment.TAG).replace(R.id.fram_container, fragment, UserNotesFragment.TAG).commit();
        }else{
            Toast.makeText(this, "Please login to access notes.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getUserProfileClicked() {
        Fragment fragment = UserFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(UserFragment.TAG).replace(R.id.fram_container, fragment, UserFragment.TAG).commit();
    }

    @Override
    public void onSignUpDone() {
        Toast.makeText(this, "SignUp completed", Toast.LENGTH_LONG).show();
        if(getFragmentManager().getBackStackEntryCount() > 1){
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onSignInDone() {
        Toast.makeText(this, "SigIn completed", Toast.LENGTH_LONG).show();
        if(getFragmentManager().getBackStackEntryCount() > 1){
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void addNoteClick() {
        Fragment fragment = AddNoteFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(AddNoteFragment.TAG).replace(R.id.fram_container, fragment, AddNoteFragment.TAG).commit();
    }


    @Override
    public void notesAddedDone() {
        Toast.makeText(this, "Note Added successfully", Toast.LENGTH_LONG).show();
        if(getFragmentManager().getBackStackEntryCount() > 1){
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void updateProfileDone() {
        Utility.showDialogMessage(this, "Upload Completed");
    }
}
