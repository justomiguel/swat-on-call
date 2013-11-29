package com.swacorp.oncallhelper.backgroundtasks;

import android.os.AsyncTask;

import com.swacorp.oncallhelper.communications.MessageReceiverAdapter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by developer on 11/28/13.
 */
public class EmailReceiverTask extends AsyncTask<String, Void, Boolean>  {

    private CountDownLatch countDownLatch;
    private ExecutorService service = Executors.newFixedThreadPool(1);
    private EmailListenerRunnable listener;

    private MessageReceiverAdapter receiver;

    public EmailReceiverTask(MessageReceiverAdapter receiver, CountDownLatch countDownLatch ) {
        this.receiver = receiver;
        this.countDownLatch = countDownLatch;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            listener = new EmailListenerRunnable(strings[0], strings[1],true);
            listener.setMyMessageAdapter(receiver);
            service.execute(listener);
            Long timeout = Long.valueOf(5000);
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
        listener.endJob();
    }
}
