package com.sunil.firebasedatabasetest.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sunil.firebasedatabasetest.adapter.NoteAdapter;
import com.sunil.firebasedatabasetest.model.NotesModel;
import com.sunil.firebasedatabasetest.R;
import com.sunil.firebasedatabasetest.utils.SharedPreferenceUtils;
import com.sunil.firebasedatabasetest.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sunil on 11/7/16.
 */

public class AddNoteFragment extends Fragment{

    public static final String TAG = AddNoteFragment.class.getSimpleName();
    private View rootView;

    OnFragmentInteractionListener mListener;
    DatabaseReference myRef;
    DatabaseReference database;
    private FirebaseAuth mFirebaseAuth;

    @BindView(R.id.titleLayout)
    TextInputLayout titleLayout;
    @BindView(R.id.discriptionLayout)
    TextInputLayout descriptionLayout;

    @BindView(R.id.title)
    EditText titleEditText;
    @BindView(R.id.discription)
    EditText descriptionEditText;

    public static AddNoteFragment newInstance() {
        AddNoteFragment fragment = new AddNoteFragment();
        return fragment;
    }

    public AddNoteFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_add_note, container, false);
        ButterKnife.bind(this, rootView);

        database = FirebaseDatabase.getInstance().getReference();

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
        void notesAddedDone();
    }

    @OnClick(R.id.addNote)
    public void onClickAddNote(){
        if (!valid()){
            return;
        }else {

            String title = titleEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            addNotes(title, description);
        }
    }

    private void addNotes(final String title, final String description){
        final String userId = SharedPreferenceUtils.getInstance(getActivity()).getUUID();
        final String userEmail = SharedPreferenceUtils.getInstance(getActivity()).getEmail();
        //NotesModel model = new NotesModel(userEmail, userId,title, description);
       // database.child("notes").setValue(model);
        database.child("notes").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get note value
                        writeNewNote(userId, userEmail, title, description);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    private void writeNewNote(String userId, String username, String title, String description) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = database.child("notes").push().getKey();
        NotesModel notesModel = new NotesModel(username,userId, title, description);
        Map<String, Object> postValues = notesModel.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/notes/" + key, postValues);

        database.updateChildren(childUpdates);

        mListener.notesAddedDone();
    }

    private boolean valid(){
        boolean isValid;
        if (Utility.nullCheck(titleLayout, "Title")){
            isValid = false;
        }
        else if (Utility.nullCheck(descriptionLayout, "Description")){
            isValid = false;
        }
        else{
            isValid = true;
        }
        return  isValid;
    }

}
