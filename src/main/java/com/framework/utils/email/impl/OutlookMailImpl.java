package com.framework.utils.email.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;

import com.framework.utils.TestReporter;
import com.framework.utils.email.OutlookMail;

public class OutlookMailImpl extends OutlookBase implements OutlookMail {
    String folderPath = null;
    private static final String IMAP_PORT = "imap";

    public void prep(String role) {
        TestReporter.setDebugLevel(0);
        super.setOutlookProps(IMAP_PORT);
        super.setUserCredentials(role);
    }

    /**
     * @summary - Constructor
     * @param -
     *            - role: User role from "#Email Logins" under Credentials.properties
     *            - folderPath: Folder path under the Outlook account from where the email will be fetched
     */
    public OutlookMailImpl(String role, String folderPath) {
        this.prep(role);
        this.folderPath = folderPath;
    }

    public OutlookMailImpl(String role) {
        this.prep(role);
        this.folderPath = "Inbox";
    }

    @Override
    public List<String> getEmailSubjects(String fromEmail) {
        return getMailData(fromEmail, folderPath, emailSubject);
    }

    // @Override
    public List<String> getOldEmail(String fromEmail) {
        return getMailData(fromEmail, folderPath, emailSubject);
    }

    @Override
    public String getSingleEmailSubject(String fromEmail) {
        List<String> subjectList = getMailData(fromEmail, folderPath, emailSubject);
        return subjectList.get(subjectList.size() - 1);
    }

    @Override
    public List<String> getEmailBody(String fromEmail) {
        return getMailData(fromEmail, folderPath, emailBody);
    }

    @Override
    public String getLastSingleEmailBody(String fromEmail) {
        List<String> bodyList = getMailData(fromEmail, folderPath, emailBody);
        return bodyList.get(bodyList.size() - 1);
    }

    @Override
    public String getSingleEmailBodyHTML(String fromEmail) {
        List<String> eBody = getMailData(fromEmail, folderPath, emailBody);
        return ((eBody.size() < 2) ? eBody.get(0) : bodyHTML);
    }

    @Override
    public HashMap<String, String> getAllLinksFromSingleEmailBody(String fromEmail) {
        return getAllLinks();
    }

    @Override
    public String getSingleLinkFromEmailBodyText(String fromEmail, String linkText) {
        return getSingleLink(linkText);
    }

    @Override
    public List<String> getEmailFromAddress(String fromEmail) {
        return getMailData(fromEmail, folderPath, emailFrom);
    }

    @Override
    public int getUnreadEmailCount() {
        getMailFolder(folderPath);
        TestReporter.logInfo("No. of Unread messages = " + unreadMessageCount);
        return unreadMessageCount;
    }

    @Override
    public void syncMail(String fromEmail)  {
        syncMailFromUser(folderPath, fromEmail);
    }

    @Override
    public void printUnreadEmailMetaData() {
        try {
            outputAllUnreadEmailData(folderPath, false, true);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HashMap<String, String> getUnreadEmailMetaData() {
        try {
            outputAllUnreadEmailData(folderPath, true, false);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return metadataMap;
    }

    @Override
    public String getEmailBodyWithMatchingSubject(String fromEmail, String subjectLine) {
        return getEmailBodyWithUniqueSubject(fromEmail, subjectLine, folderPath);
    }

    @Override
    public boolean isBodyExpected(String fromEmail, String subjectLine, String bodyText) {
        return isEmailBodyExpected(fromEmail, subjectLine, folderPath, bodyText);
    }

    @Override
    public String oldGetBodyText() {
        OutlookBase.OutlookBaseOld abc = this.new OutlookBaseOld();
        String bodyText = null;
        try {
            bodyText = abc.getTextHTML(folderPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bodyText;
    }

    @Override
    public String oldGetSubject() {
        OutlookBase.OutlookBaseOld abc = this.new OutlookBaseOld();
        String subjectLine = null;
        try {
            subjectLine = abc.getSubject(folderPath);
        } catch (Exception e) {
            TestReporter.logTrace(e.toString());
        }
        return subjectLine;
    }

    @Override
    public String oldGetFrom() {
        OutlookBase.OutlookBaseOld abc = this.new OutlookBaseOld();
        String fromAddress = null;
        try {
            fromAddress = abc.getFrom(folderPath);
        } catch (Exception e) {
            TestReporter.logTrace(e.toString());
        }
        return fromAddress;
    }

    @Override
    public Date oldGetTimeStamp() {
        OutlookBase.OutlookBaseOld abc = this.new OutlookBaseOld();
        Date eMailDate = null;
        try {
            eMailDate = abc.getDate(folderPath);
        } catch (Exception e) {
            TestReporter.logTrace(e.toString());
        }
        return eMailDate;
    }
}