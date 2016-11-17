package com.sunil.firebasedatabasetest.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sunil.firebasedatabasetest.R;
import com.sunil.firebasedatabasetest.adapter.NoteAdapter;
import com.sunil.firebasedatabasetest.model.NotesModel;
import com.sunil.firebasedatabasetest.utils.SharedPreferenceUtils;
import com.sunil.firebasedatabasetest.utils.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kuliza-195 on 11/16/16.
 */

public class UserFragment extends Fragment {

    public static final String TAG = UserFragment.class.getSimpleName();
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;

    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.select)
    Button select;
    @BindView(R.id.uplaod)
    Button uplaod;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.register_user_email_edit_text)
    TextInputEditText registerUserEmailEditText;
    @BindView(R.id.email_container)
    TextInputLayout emailContainer;
    @BindView(R.id.update)
    Button update;

    private View rootView;
    OnFragmentInteractionListener mListener;
    DatabaseReference databaseRef;
    private FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    StorageReference storageRef;
    private ProgressDialog mProgressDialog;

    private static final int SELECT_PICTURE = 100;
    private File mFile;
    Uri selectedImageUri;


    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
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
        rootView = inflater.inflate(R.layout.fragment_userprofile, container, false);
        ButterKnife.bind(this, rootView);

        databaseRef = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        // creating an instance of Firebase Storage
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageRef =  firebaseStorage.getReference().child("photos");

        uplaod.setEnabled(false);

        getUserInfo();
        return rootView;
    }

    private void getUserInfo(){
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            String userId = mFirebaseUser.getUid();
            String email = mFirebaseUser.getEmail();
            registerUserEmailEditText.setText(email);
            DatabaseReference usernameRef = databaseRef.child(userId);
            Query queryRef = usernameRef.orderByKey();
            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userProfileUrl = (String) dataSnapshot.child("userProfileUrl").getValue();
                    Log.e(TAG, "Profile Url: "+ userProfileUrl);
                    if (userProfileUrl != null && !userProfileUrl.isEmpty()){
                        Glide.with(getActivity()).load(userProfileUrl).into(profileImage);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, databaseError.getMessage());
                }
            });

        }


        //databaseReference.child(user.getUid()).child("userProfileUrl").setValue(22);
    }
    private void setUserProfile(String url){
        final String userId = SharedPreferenceUtils.getInstance(getActivity()).getUUID();
        databaseRef.child(userId).child("userProfileUrl").setValue(url);
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
        void updateProfileDone();
    }

    @OnClick(R.id.select)
    public void imageSelectFile(){
        if(checkPermissionForExternalStorage(getActivity())) {
            if(checkPermissionForCamera(getActivity())) {
                openFile();
            }
        }
    }

    @OnClick(R.id.uplaod)
    public void uploadClick(){
        if (mFile != null && mFile.exists()) {
            uploadFile(mFile, selectedImageUri );
        }else {
            Toast.makeText(getActivity(), "File does not exist", Toast.LENGTH_LONG).show();
        }

    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void uploadFile(File file, Uri fileUri){
        showProgressDialog();
        InputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(stream != null){

            // Create a reference to "file"
            storageRef = storageRef.child(fileUri.getLastPathSegment());

            UploadTask uploadTask = storageRef.putStream(stream);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    hideProgressDialog();
                    Toast.makeText(getActivity(), "Uploading failed", Toast.LENGTH_LONG).show();
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    hideProgressDialog();
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Log.e("Url", "DownloadUrl: "+downloadUrl);
                    setUserProfile(downloadUrl.toString());
                    uplaod.setEnabled(false);
                    mListener.updateProfileDone();

                }
            });
        }
        else{
            Toast.makeText(getActivity(), "Getting null file", Toast.LENGTH_LONG).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermissionForCamera(Activity context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermissionForExternalStorage(Activity context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(checkPermissionForCamera(getActivity())) {
                        openFile();
                    }
                } else {
                    Toast.makeText(getActivity(), "You have denied permission to access external storage. Please go to settings and enable access to use this feature", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void openFile(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE );
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==SELECT_PICTURE){
                selectedImageUri = data.getData();
                String path = getPath(getActivity(),selectedImageUri);
                mFile = new File(path);
                uplaod.setEnabled(true);
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    ContentResolver cr = getActivity().getContentResolver();
                    Bitmap bitmap;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImageUri);
                        profileImage.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        Log.e("Camera", e.toString());
                    }

                }
            }
        }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

}
