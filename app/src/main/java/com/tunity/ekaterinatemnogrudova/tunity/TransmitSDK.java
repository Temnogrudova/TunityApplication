package com.tunity.ekaterinatemnogrudova.tunity;

import android.os.AsyncTask;

import java.util.Arrays;
import java.util.List;

public class TransmitSDK {

    private static final String GoodPassword = "goodpassword";
    private static final String GoodPincode = "12345";
    private static final boolean GoodFinger = true;

    public interface OnListResult {
        void onComplete(List<Authenticator> result);
    }

    public interface OnResult {
        void onComplete();

        void onReject(String error);
    }

    public enum Authenticator {
        PASSWORD, PINCODE, FINGERPRINT;
    }

    private static final TransmitSDK instance = new TransmitSDK();

    public static TransmitSDK getInstance() {
        return instance;
    }

    public void authenticatorsList(OnListResult onResult) {
        new ListAsync(onResult).execute();
    }

    public void authenticateWithPassword(String password, OnResult onResult) {
        password = password.trim();
        new AuthAsync(Authenticator.PASSWORD, password, onResult).execute();
    }

    public void authenticateWithPincode(String pincode, OnResult onResult) {
        new AuthAsync(Authenticator.PINCODE, pincode, onResult).execute();
    }

    public void authenticateWithFingerprint(boolean useFingerprint, OnResult onResult) {
        new AuthAsync(Authenticator.FINGERPRINT, useFingerprint, onResult).execute();
    }

    private static class AuthAsync extends AsyncTask<Void, Void, Boolean> {
        private final Authenticator type;
        private final Object content;
        private final OnResult onResult;

        private AuthAsync(Authenticator type, Object content, OnResult onResult) {
            this.type = type;
            this.content = content;
            this.onResult = onResult;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Thread.sleep(720);
                if (type == Authenticator.PASSWORD) {
                    return content.equals(GoodPassword);
                } else if (type == Authenticator.PINCODE) {
                    return content.equals(GoodPincode);
                } else if (type == Authenticator.FINGERPRINT) {
                    return content.equals(GoodFinger);
                }
            } catch (Exception e) {
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            super.onPostExecute(isSuccess);
            if (isSuccess) {
                onResult.onComplete();
            } else {
                onResult.onReject("Authenticator failed due to invalid input.");
            }
        }
    }

    private static class ListAsync extends AsyncTask<Void, Void, List<Authenticator>> {
        private final OnListResult onResult;

        private ListAsync(OnListResult onResult) {
            this.onResult = onResult;
        }

        @Override
        protected List<Authenticator> doInBackground(Void... voids) {
            List<Authenticator> list = Arrays.asList(Authenticator.values());
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                return null;
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Authenticator> authenticators) {
            super.onPostExecute(authenticators);
            onResult.onComplete(authenticators);
        }
    }

}