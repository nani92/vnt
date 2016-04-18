package com.nani.vnto;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by nataliajastrzebska on 18/04/16.
 */
public final class Validator {

    public static boolean isEmailValid(Editable email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
