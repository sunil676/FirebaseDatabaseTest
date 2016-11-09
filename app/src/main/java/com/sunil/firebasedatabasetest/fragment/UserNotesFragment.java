package com.sunil.firebasedatabasetest.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sunil.firebasedatabasetest.R;
import com.sunil.firebasedatabasetest.adapter.NoteAdapter;
import com.sunil.firebasedatabasetest.model.NotesModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sunil on 11/10/16.
 */

public class UserNotesFragment extends Fragment {

        public static String TAG = UserNotesFragment.class.getSimpleName();
        private View rootView;

        @BindView(R.id.recycler_view)
        RecyclerView mRecyclerView;

        OnFragmentInteractionListener mListener;
        DatabaseReference database;
        private FirebaseAuth mFirebaseAuth;
        FirebaseUser mFirebaseUser;
        private String mUserId;

        public static UserNotesFragment newInstance() {
            UserNotesFragment fragment = new UserNotesFragment();
            return fragment;
        }

        public UserNotesFragment() {
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
            rootView = inflater.inflate(R.layout.fragment_userlist, container, false);
            ButterKnife.bind(this, rootView);

            database = FirebaseDatabase.getInstance().getReference();
            // Initialize FirebaseAuth
            mFirebaseAuth = FirebaseAuth.getInstance();

            mFirebaseUser = mFirebaseAuth.getCurrentUser();

            if (mFirebaseUser != null) {
                mUserId = mFirebaseUser.getUid();

            }

            LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
            mManager.setReverseLayout(true);
            mManager.setStackFromEnd(true);
            mRecyclerView.setLayoutManager(mManager);
            mRecyclerView.setHasFixedSize(true);

            DatabaseReference usernameRef = database.child("notes");
            Query queryRef = usernameRef.orderByKey();
            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<NotesModel> notesModels = new ArrayList<NotesModel>();
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        String title = (String) child.child("title").getValue();
                        String description = (String) child.child("description").getValue();
                        String user = (String) child.child("user").getValue();
                        NotesModel notesModel = new NotesModel();
                        notesModel.setDescription(description);
                        notesModel.setUser(user);
                        notesModel.setTitle(title);
                        notesModels.add(notesModel);

                    }

                    NoteAdapter adapter = new NoteAdapter(getActivity(), notesModels);
                    mRecyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, databaseError.getMessage());
                }
            });

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
            void addNoteClick();
        }

        @OnClick(R.id.create)
        public void addNoteClick(){
            mListener.addNoteClick();
        }

}
