package com.framework.utils.email;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.framework.core.interfaces.impl.internal.ImplementedBy;
import com.framework.utils.email.impl.OutlookMailImpl;

@ImplementedBy(OutlookMailImpl.class)
public interface OutlookMail {

    /**
     * @summary - Returns the list of Subject Title of Unread eMail from outlook account
     * @param -
     *            - fromEmail: email address of the FROM email, from which the email was received
     */
    List<String> getEmailSubjects(String fromEmail);

    /**
     * @summary - Returns the Subject Title of last unread eMail from outlook account
     * @param -
     *            - fromEmail: email address of the FROM email, from which the email was received
     */
    String getSingleEmailSubject(String fromEmail);

    /**
     * @summary - Returns the list of Body of the eMail from outlook account
     * @param -
     *            - fromEmail: email address of the FROM email, from which the email was received
     */
    List<String> getEmailBody(String fromEmail);

    /**
     * @summary - Returns Body of last unread eMail from outlook account
     * @param -
     *            - fromEmail: email address of the FROM email, from which the email was received
     */
    String getLastSingleEmailBody(String fromEmail);

    /**
     * @summary - Returns HTML Body of last unread eMail from outlook account
     * @param -
     *            - fromEmail: email address of the FROM email, from which the email was received
     */
    String getSingleEmailBodyHTML(String fromEmail);

    /**
     * @summary - Returns all available Links of last unread eMail from outlook account
     * @param -
     *            - fromEmail: email address of the FROM email, from which the email was received
     */
    HashMap<String, String> getAllLinksFromSingleEmailBody(String fromEmail);

    /**
     * @summary - Returns String of particular Link from last unread eMail under outlook account
     * @param -
     *            - fromEmail: email address of the FROM email, from which the email was received
     *            - linkText: visible text string of the link
     */
    String getSingleLinkFromEmailBodyText(String fromEmail, String linkText);

    /**
     * @summary - Returns the list of FROM eMail address from outlook account where the eMail is received
     * @param -
     *            - fromEmail: email address of the FROM email, from which the email was received
     */
    List<String> getEmailFromAddress(String fromEmail);

    /**
     * @summary - Returns the integer value of under eMail's. Method do not update the flag of unread email to READ
     * @param -
     *            - none
     */
    int getUnreadEmailCount();

    /**
     * @summary - Dynamically waits until a folder under inbox receives an Email from a specific user
     * @param -
     *            - fromEmail: email address of the FROM email, from which the email was received
     */
    void syncMail(String fromEmail);

    /**
     * @summary - Outputs all unread email with parsed metadata to console
     * @param -
     *            - none
     */
    void printUnreadEmailMetaData();

    /**
     * @summary - Returns metadata Hashmap of all unread email
     * @param -
     *            - none
     * @return
     */
    HashMap<String, String> getUnreadEmailMetaData();

    /**
     * @summary - Return Unread email body string whose subject line matches with subjectLine parameter from a specific user email id
     * @param -
     *            - fromEmail: email address of the FROM email, from which the email was received
     *            - subjectLine: subject line of an unread email which need to be searched
     * @return - email body String whose subject line matches with @param subjectLine
     */
    String getEmailBodyWithMatchingSubject(String fromEmail, String subjectLine);

    /**
     * @summary - Compare if the email body text is expected with the passed argument
     * @param -
     *            - fromEmail: email address of the FROM email, from which the email was received
     *            - subjectLine: subject line of an unread email which need to be searched
     *            - bodyText: any text string or whole text from the email body which needs to be compared
     * @return - boolean
     */
    boolean isBodyExpected(String fromEmail, String subjectLine, String bodyText);

    /**
     * Old mail methods to get last outlook email's HTML body
     *
     * @deprecated use @getLastSingleEmailBody instead.
     */
    @Deprecated
    String oldGetBodyText();

    /**
     * Old mail methods to get last outlook email's Subject Line
     *
     * @deprecated use @getLastSingleEmailBody instead.
     */
    @Deprecated
    String oldGetSubject();

    /**
     * Old mail methods to get last outlook email's TimeStamp
     *
     * @deprecated use @getLastSingleEmailBody instead.
     */
    @Deprecated
    Date oldGetTimeStamp();

    /**
     * Old mail methods to get last outlook email's From name
     *
     * @deprecated use @getLastSingleEmailBody instead.
     */
    @Deprecated
    String oldGetFrom();
}