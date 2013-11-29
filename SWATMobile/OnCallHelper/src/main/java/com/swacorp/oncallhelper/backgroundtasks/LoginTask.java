package com.swacorp.oncallhelper.backgroundtasks;

import android.os.AsyncTask;

import com.swacorp.oncallhelper.communications.ValidatorMessageReceiver;
import com.swacorp.oncallhelper.email.GlobantEmailSender;
import com.swacorp.oncallhelper.exceptions.SWATOnCallRuntimeException;
import com.swacorp.oncallhelper.utils.SWAUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;

/**
 * Created by developer on 11/28/13.
 */
public class LoginTask extends AsyncTask<String, Void, Boolean> {

    private static String serverEmail = "swat.primary@globant.com";
    private static String serverPass = "Qwerty12$";
    private static String destinattion = "swat.primary@globant.com";


    private ExecutorService service = Executors.newFixedThreadPool(1);
    private EmailListenerRunnable listener;

    @Override
    protected Boolean doInBackground(String... strings) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ValidatorMessageReceiver myMessageAdapter = new ValidatorMessageReceiver(countDownLatch, "Login=x9246/Pass", serverEmail);

        GlobantEmailSender sender = new GlobantEmailSender(serverEmail, serverPass);
        try {
            sender.sendMail("justo.vargas@globant.com","Login=x9246/Pass","");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            listener = new EmailListenerRunnable(serverEmail, serverPass,true);
            listener.setMyMessageAdapter(myMessageAdapter);
            service.execute(listener);
            //listener.checkEmail();
            Long timeout = Long.valueOf(6000);
            if (!countDownLatch.await(timeout, TimeUnit.SECONDS)) {
                return false;
            }
        } catch (Exception e){
            return false;
        }
        return true;

    }

    @Override
    protected void onPostExecute(final Boolean result) {
        if (result){
            String k;
        }
    }
}
