package com.swacorp.oncallhelper.backgroundtasks;

import android.os.AsyncTask;

import com.swacorp.oncallhelper.email.GlobantEmailSender;
import com.swacorp.oncallhelper.exceptions.SWATOnCallRuntimeException;
import com.swacorp.oncallhelper.utils.SWAUtils;

import javax.mail.MessagingException;

/**
 * Created by developer on 11/28/13.
 */
public class SendEmailTask extends AsyncTask<String, Void, Boolean> {

    private static final String ERROR_ON_EMAIL = "Error when Trying to send an E-mail";
    @Override
    protected Boolean doInBackground(String... params) {
        GlobantEmailSender sender = new GlobantEmailSender(params[0], params[1]);
        try {
            return sender.sendMail(params[2], params[3], params[4]);
        } catch (MessagingException e) {
            if (SWAUtils.isNotNull(e)){
                throw new SWATOnCallRuntimeException(e,ERROR_ON_EMAIL);
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(final Boolean result) {
        //Do something with result
        if (result != null) {

        }

    }
}
