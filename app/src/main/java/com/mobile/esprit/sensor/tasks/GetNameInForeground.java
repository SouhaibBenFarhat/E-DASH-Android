package com.mobile.esprit.sensor.tasks;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.mobile.esprit.sensor.LoginActivity;

import java.io.IOException;


public class GetNameInForeground extends AbstractGetNameTask{

    public GetNameInForeground(LoginActivity mActivity, String mEmail, String mScope) {
        super(mActivity, mEmail, mScope);
    }

    @Override
    protected String fetchToken() throws IOException {
        try {
            return GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
        } catch (GooglePlayServicesAvailabilityException playEx) {

        } catch (UserRecoverableAuthException urae) {
            mActivity.startActivityForResult(urae.getIntent(), mRequest);
        } catch (GoogleAuthException fatalException) {
            fatalException.printStackTrace();
        }

        return null;
    }
}
