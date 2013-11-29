package com.swacorp.oncallhelper.communications;

import java.util.concurrent.CountDownLatch;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountEvent;

/**
 * Created by developer on 11/28/13.
 */
public class ValidatorMessageReceiver extends MessageReceiverAdapter {

    private final CountDownLatch countDownLatch;
    private final String messageToVerify;
    private final String senderEmail;

    public ValidatorMessageReceiver(CountDownLatch countDownLatch, String subjetToVerify, String senderEmail) {
        this.countDownLatch = countDownLatch;
        this.messageToVerify=subjetToVerify;
        this.senderEmail=senderEmail;
    }

    @Override
    public void messagesAdded(MessageCountEvent ev) {
        Message[] msgs = ev.getMessages();

        // Just dump out the new messages
        for (int i = 0; i < msgs.length; i++) {
            try {
                String subject = msgs[i].getSubject();
                final String from = msgs[i].getFrom()[0].toString();
                final Address destinationEmail = msgs[i].getFrom()[0];

                if (subject.equalsIgnoreCase(this.messageToVerify) && from.contains(this.senderEmail)) {
                    this.countDownLatch.countDown();
                }

            } catch (Exception ex) {

            }
        }
    }
}
