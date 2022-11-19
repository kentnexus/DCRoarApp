package com.paszlelab.dcroarapp.Validations;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.paszlelab.dcroarapp.R;

import java.util.regex.Pattern;

public class FieldsFragment {
    //    required fields must be non-null
    public static boolean requiredField(EditText field, String sfield){
        boolean notEmpty = false;
        if(TextUtils.isEmpty(field.getText()))
        {
            field.setError( sfield+" is required!" );
        }
        else {
            notEmpty = true;
        }
        return notEmpty;
    }

    //    only DC students can register
    public static boolean validEmail(Context context, EditText etEmail){

        String email = etEmail.getText().toString();
        String pattern = ".*@student.douglascollege.ca";
        boolean match = Pattern.matches(pattern, email);

        return match;
    }

    public static String getGender(RadioButton rbM, RadioButton rbF, RadioButton rbN){
        String gender = "";
            if(rbM.isChecked())
                gender = "Male";
            else if (rbF.isChecked())
                gender = "Female";
            else if (rbN.isChecked())
                gender = "Non-binary";
        return gender;
    }
}
