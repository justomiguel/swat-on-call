package com.swacorp.oncallhelper.communications;

import java.util.concurrent.CountDownLatch;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

/**
 *
 * @author developer
 */
public class MessageReceiverAdapter extends MessageCountAdapter {

    private String GET_STATUS = "Get PROD Status";
    private String ACK_CLASSIC_PAGE = "Ack Classic";
    private String SUCCES_LOGIN = "SUCCES LOGIN FOR:";

    @Override
    public void messagesAdded(MessageCountEvent ev) {
        Message[] msgs = ev.getMessages();

        // Just dump out the new messages
        for (int i = 0; i < msgs.length; i++) {
            try {

                String subject = msgs[i].getSubject();
                final String from = msgs[i].getFrom()[0].toString();
                final Address destinationEmail = msgs[i].getFrom()[0];

                if (subject.equalsIgnoreCase(GET_STATUS)) {
                    System.out.println("Sending the e-mail with the status");

                } else if (subject.equalsIgnoreCase(ACK_CLASSIC_PAGE)) {
                    System.out.println("Sending the e-mail with the status");
                } else if (subject.equalsIgnoreCase(SUCCES_LOGIN)) {
                    System.out.println("Sending the e-mail with the status");
                }
            }
            catch (MessagingException mex) {
                mex.printStackTrace();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
