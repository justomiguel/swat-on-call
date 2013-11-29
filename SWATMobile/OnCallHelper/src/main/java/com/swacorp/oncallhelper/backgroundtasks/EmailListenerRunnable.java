package com.swacorp.oncallhelper.backgroundtasks;

import com.sun.mail.imap.IMAPFolder;
import com.swacorp.oncallhelper.communications.MessageReceiverAdapter;
import com.swacorp.oncallhelper.communications.ValidatorMessageReceiver;

import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class EmailListenerRunnable implements Runnable {

    private static final String PORT = "993";

    private final String user;
    private final String pass;

    private String PROTOCOL = "imap";
    private String SWA_HOST = "swamail.swacorp.com";
    private String GLB_HOST = "imap.gmail.com";

    private Folder folder = null;

    private String NOT_LISTENING = "NOT LISTENING";
    private String LISTENING = "LISTENING";

    private boolean notTimeToDieYet;
    private Store store;
    private MessageReceiverAdapter myMessageAdapter;


    public EmailListenerRunnable(String user, String pass, boolean useGLB) {
        this.user = user;
        this.pass = pass;
        notTimeToDieYet = false;

        Properties props = new Properties();
        //IMAPS protocol
        props.setProperty("mail.store.protocol", "imaps");
        //Set host address
        props.setProperty("mail.imaps.host", "imaps.gmail.com");
        //Set specified port
        props.setProperty("mail.imaps.port", "993");
        //Using SSL
        props.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.imaps.socketFactory.fallback", "false");
        //Setting IMAP session
        Session imapSession = Session.getInstance(props);
        store = null;
        try {
            store = imapSession.getStore("imaps");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        //Connect to server by sending username and password.
        //Example mailServer = imap.gmail.com, username = abc, password = abc
        try {
            if (useGLB){
                store.connect(GLB_HOST, user, pass);
            } else {
                store.connect(SWA_HOST, user,pass);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public MessageReceiverAdapter getMyMessageAdapter() {
        return myMessageAdapter;
    }

    public void setMyMessageAdapter(MessageReceiverAdapter myMessageAdapter) {
        this.myMessageAdapter = myMessageAdapter;
    }

    public void checkEmail() throws MessagingException, InterruptedException {

        folder = store.getDefaultFolder();
        folder = store.getFolder("Inbox");
        if (folder == null || !folder.exists()) {
            System.out.println("Invalid folder");
            throw new NoSuchElementException("Missing Folder!");
        }
        folder.open(Folder.READ_WRITE);

        folder.addMessageCountListener(myMessageAdapter);

        int freq = 100;
        boolean supportsIdle = false;
        try {
            if (folder instanceof IMAPFolder) {
                IMAPFolder f = (IMAPFolder) folder;
                f.idle();
                supportsIdle = true;
            }
        } catch (FolderClosedException fex) {
            throw fex;
        } catch (MessagingException mex) {
            supportsIdle = false;
        }

        while (!notTimeToDieYet) {
            if (supportsIdle && folder instanceof IMAPFolder) {
                IMAPFolder f = (IMAPFolder) folder;
                f.idle();
            } else {
                Thread.sleep(freq); // sleep for freq milliseconds
                // This is to force the IMAP server to send us
                // EXISTS notifications.
                folder.getMessageCount();
            }
        }
    }

    public void endJob() {
        notTimeToDieYet = true;
        try {
            if (folder.isOpen()){
                folder.close(true);
            }
            folder.removeMessageCountListener(myMessageAdapter);
            store.close();
        }
        catch (MessagingException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            checkEmail();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
