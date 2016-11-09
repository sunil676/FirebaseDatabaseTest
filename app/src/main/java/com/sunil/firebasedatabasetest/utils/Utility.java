package com.sunil.firebasedatabasetest.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

/**
 * Created by sunil on 11/6/16.
 */

public class Utility {

    public static void showDialog(Context context, Task<AuthResult> task){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(task.getException().getMessage())
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public static boolean nullCheck(TextInputLayout layout , String label) {
        label = label.replace("*","");
        String name = layout.getEditText().getText().toString().trim();
        if(name.isEmpty()){
            showErrorMsg(layout);
            layout.setError(label+ " should not be empty");
            try {
                //In Case Of Edit Text With POP - UP
                if (layout.getEditText().isFocusableInTouchMode()) {
                    layout.getEditText().requestFocus();
                } else {
                    layout.getEditText().setFocusableInTouchMode(true);
                    layout.getEditText().requestFocus();
                    layout.getEditText().setFocusableInTouchMode(false);
                }
            }
            catch (Exception e){
                layout.getEditText().requestFocus();
            }
            return true;
        }
        else{
            removeErrorMsg(layout);
            return false;
        }
    }

    public static void showErrorMsg(TextInputLayout layout){
        if(layout.getChildCount()==2){
            layout.getChildAt(1).setVisibility(View.VISIBLE);
        }
    }

    public static void removeErrorMsg(TextInputLayout layout){
        layout.setError(null);
        layout.setErrorEnabled(false);
        if(layout.getChildCount()==2){
            layout.getChildAt(1).setVisibility(View.GONE);
        }
    }

    public static boolean validateEmail(TextInputLayout emailLayout){
        EditText editText = emailLayout.getEditText();
        String email = editText.getText().toString();
        if (email.isEmpty()){
            showErrorMsg(emailLayout);
            emailLayout.setError("Email Id should not be empty.");
            editText.requestFocus();
            return false;
        } else if(email.contains(" ")){
            showErrorMsg(emailLayout);
            emailLayout.setError("Email Id should not contains any spaces");
            editText.requestFocus();
            return false;
        }
        else if(!isValidEmail(email)){
            showErrorMsg(emailLayout);
            emailLayout.setError("Email Id is not valid");
            editText.requestFocus();
            return false;
        }
        else{
            removeErrorMsg(emailLayout);
            return true;
        }
    }

    public static boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean validateMobile(TextInputLayout nameLayout){
        EditText mobile = nameLayout.getEditText();
        String mobileNo = mobile.getText().toString();
        if (mobileNo.isEmpty()){
            showErrorMsg(nameLayout);
            nameLayout.setError("Mobile No should not be empty");
            mobile.requestFocus();
            return false;
        }
        else if(mobileNo.length()<10){
            showErrorMsg(nameLayout);
            nameLayout.setError("Mobile No should contain 10 digits.");
            mobile.requestFocus();
            return false;
        }
        else if(!mobileNo.matches("[0-9]{10}")){
            showErrorMsg(nameLayout);
            nameLayout.setError("Mobile No should contain only digits.");
            mobile.requestFocus();
            return false;
        }
        else if(mobileNo.matches("9[0-9]{9}") || mobileNo.matches("8[0-9]{9}") || mobileNo.matches("7[0-9]{9}") ){
            removeErrorMsg(nameLayout);
            return true;
        }
        else{
            showErrorMsg(nameLayout);
            nameLayout.setError("Mobile No should start with digits 7,8 or 9.");
            mobile.requestFocus();
            return false;
        }
    }

}
