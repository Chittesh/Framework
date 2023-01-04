package com.framework.utils.email.impl;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;

import com.framework.utils.TestReporter;

public class TransportMail {

    public void smptMail() throws MessagingException {
        String port = "587";
        String userName = "connected_user@allegisgroup.com";
        String password = "P@ssw0rd6%";
        String host = "outlook.office365.com";

        Properties props = null;
        props = System.getProperties();
        props.put("mail.smtp.user", userName);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", port);

        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(true);

        Transport transport = session.getTransport("smtp");
        transport.connect(host, userName, password);
        Folder mailFolder = null;
        int unreadEmailCount;
        Store store = session.getStore("smpt");
        mailFolder = store.getFolder("Inbox");
        mailFolder.open(Folder.READ_WRITE);
        unreadEmailCount = mailFolder.getUnreadMessageCount();
        TestReporter.logDebug("Unread messages = " + unreadEmailCount);
        transport.close();
    }
}