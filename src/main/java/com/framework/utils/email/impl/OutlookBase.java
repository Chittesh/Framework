package com.framework.utils.email.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.framework.utils.Base64Coder;
import com.framework.utils.Constants;
import com.framework.utils.TestReporter;
import com.framework.utils.WaitAllegis;

class OutlookBase {
    private static final String MIME_TYPE = "text/plain";

	private final String sslFactory = "javax.net.ssl.SSLSocketFactory",
            mailImaps = "mail.imaps.",
            portImaps = "993",
            mailSmpt = "mail.smtp.",
            portSmpt = "587",
            mailPop3 = "mail.pop3.",
            portPop3 = "995";

    protected final String emailFrom = "FROM",
            emailSubject = "SUBJECT",
            emailBody = "BODY",
            emailDate = "DATE",
            emailDetails = "DETAILS";

    protected int unreadMessageCount;
    private int timeoutInSec = Constants.MAIL_TIMEOUT;

    String username = null,
            password = null;

    protected HashMap<String, String> metadataMap = new HashMap<>();

    private boolean mailSeenFlag = true;
    private Store store;

    protected String bodyHTML;
    private String stripText = "This electronic mail";

    protected Properties setOutlookProps(String connectionProtocol) {
        switch (connectionProtocol.toLowerCase()) {
            case "imap":
                TestReporter.logDebug("Creating mail connection with IMPA Protocol");
                Properties imapProps = System.getProperties();
                imapProps.setProperty(mailImaps + "socketFactory.class", sslFactory);
                imapProps.setProperty(mailImaps + "socketFactory.fallback", "false");
                imapProps.setProperty(mailImaps + "port", portImaps);
                imapProps.setProperty(mailImaps + "socketFactory.port", portImaps);
                imapProps.put(mailImaps + "host", "imap-mail.outlook.com");
                return imapProps;

            case "smpt":
                TestReporter.logDebug("Creating mail connection with SMPT Protocol");
                Properties smptProps = new Properties();
                smptProps.setProperty(mailSmpt + "auth", "true");
                smptProps.put(mailSmpt + "host", "outlook.office365.com");
                smptProps.put(mailSmpt + "port", portSmpt);
                smptProps.put(mailSmpt + "starttls.enable", "true");
                smptProps.put(mailSmpt + "auth", true);
                return smptProps;

            case "pop3":
                TestReporter.logDebug("Creating mail connection with POP3 Protocol");
                Properties pop3Props = new Properties();
                pop3Props.put(mailPop3 + "host", "outlook.office365.com");
                pop3Props.put(mailPop3 + "port", portPop3);
                pop3Props.put(mailPop3 + "starttls.enable", "true");
                return pop3Props;

            default:
                TestReporter.logFailure("Provide a valid mail connection Protocol");
                break;
        }
        return null;
    }

    protected void setUserCredentials(String role) {
        ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);
        username = userCredentialRepo.getString(role.toUpperCase());
        password = Base64Coder.decodeString(userCredentialRepo.getString(role.toUpperCase() + "_PASSWORD"));
    }

    /**
     * @summary - Returns the FROM eMail address from outlook account where the eMail is received
     * @param -
     *            - emailId: email address for the Outlook account
     *            - password: valid password String for above email
     *            - fromEmail: email address of the FROM email, from which the email was received
     *            - folderPath: as mainFolder/subFolder/subSubFolder/....
     */
    protected List<String> getMailData(String fromEmail, String folderPath, String dataType) {
        List<String> details = null;
        try {
            details = getEmailDataMap(fromEmail, folderPath).get(dataType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }

    protected Folder getMailFolder(String folderPath) {
        Folder mailFolder = null;
        try {
            Session session = Session.getDefaultInstance(System.getProperties(), null);
            this.store = session.getStore("imaps");
            store.connect("imap-mail.outlook.com", Integer.parseInt(portImaps), this.username, this.password);
            mailFolder = store.getFolder(folderPath);
            mailFolder.open(Folder.READ_WRITE);
            unreadMessageCount = mailFolder.getUnreadMessageCount();
            TestReporter.logDebug("Unread messages = " + unreadMessageCount);
            return mailFolder;
        } catch (AuthenticationFailedException e) {
            TestReporter.logFailure("Authentication failed to the Outlook mail account with username = '" + this.username + "' and password = " + this.password);
            e.printStackTrace();
        } catch (Exception e) {
            TestReporter.logFailure("Unable to read unread emails");
            e.printStackTrace();
        }
        return mailFolder;
    }

    protected HashMap<String, String> getAllLinks() {
        bodyHTML = null;

        Document doc = Jsoup.parse(bodyHTML);
        Elements links = doc.select("a[href]");
        HashMap<String, String> hm = new HashMap<>();
        for (Element link : links) {
            hm.put(link.text().trim(), link.attr("href"));
            TestReporter.logDebug("SIZE: " + links.size() + ", TEXT: " + link.text().trim() + ", LINK: " + link.attr("href"));
        }
        return hm;
    }

    protected String getSingleLink(String linkText) {
        HashMap<String, String> hmLinks = getAllLinks();
        TestReporter.log("Link list size = " + hmLinks.size());
        for (@SuppressWarnings("rawtypes")
        Map.Entry mapEntry : hmLinks.entrySet()) {
            TestReporter.logDebug(">Key: " + mapEntry.getKey() + " >Value: " + mapEntry.getValue());
        }
        return hmLinks.get(linkText);
    }

    private int getEmailCount(String folderPath) {
        try {
            return getMailFolder(folderPath).getUnreadMessageCount();
        } catch (NumberFormatException | MessagingException e) {
            e.printStackTrace();
        }
        return unreadMessageCount;
    }

    @SuppressWarnings("unused")
    private void waitSyncMail(int timeoutInSec, String folderPath) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        int unreadEmailCount = getEmailCount(folderPath);
        while ((System.currentTimeMillis() - startTime) < timeoutInSec * 1000) {
            unreadEmailCount = getEmailCount(folderPath);
            WaitAllegis.hardWait(7);
            if (unreadEmailCount > 0) {
                break;
            }
        }
    }

    protected void syncMailFromUser(String folderPath, String fromEmail) {
        mailSeenFlag = false;
        long startTime = System.currentTimeMillis();
        int unreadEmailCount = 0;

        while ((System.currentTimeMillis() - startTime) < timeoutInSec * 1000) {
            unreadEmailCount = getEmailCount(folderPath);
            if (unreadEmailCount > 0) {
                List<String> fromList = getMailData(fromEmail, folderPath, emailFrom);
                for (int i = 0; i < fromList.size(); i++) {
                    String email = fromList.get(i).toString();
                    if (email.contains(fromEmail)) {
                        TestReporter.logDebug("Enter IF loop #syncMailFromUser with Email as = " + email);
                        break;
                    }
                }
            }
        }
        mailSeenFlag = true;
    }

    private HashMap<String, List<String>> getEmailDataMap(String fromEmail, String folderPath) throws IOException {
        Folder inbox;
        List<String> details = new ArrayList<>();
        try {
            inbox = getMailFolder(folderPath);
            Message messages[] = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            inbox.fetch(messages, fp);
            reportAllUnreadEmailMessagesToConsole(messages, false);

            for (int i = 0; i < messages.length; i++) {
            	Message message = messages[i];
                Address[] a = message.getFrom();
                String subject = message.getSubject();
                String from = "";
                if (a != null) {
                    for (int j = 0; j < a.length; j++) {
                        from = a[j].toString();
                        if (StringUtils.containsIgnoreCase(from, fromEmail)) {
                            details.add("FROM SeLenIuM::::: " + (a[j]).toString());
                        }
                    }
                }
                if (StringUtils.containsIgnoreCase(from, fromEmail)) {
                    String content;
                    Date messageDate = message.getReceivedDate();
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    
                    if (messageDate != null) 
                    	details.add("DATE SeLenIuM::::: " + df.format(messageDate));

                    details.add("SUBJECT SeLenIuM::::: " + subject);
                    String strippedText = "";
                    content = message.getContent().toString();
                    bodyHTML = content;
                    try {
                    	strippedText = Jsoup.parse(content).text();
                        	
                        if (message.isMimeType(MIME_TYPE)) 
                        	strippedText = message.getContent().toString();
                            
                        if (message.isMimeType("multipart/*")) {
                        	MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                        	strippedText = getTextFromMimeMultipart(mimeMultipart);
                        	bodyHTML = strippedText;
                        }
                            
                        if (message.getContent() instanceof MimeMultipart) 
                            strippedText = getTextFromMimeMultipart((MimeMultipart) message.getContent());
                            
                     } 
                    catch (Exception e) {
                    	 strippedText = content.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
                    }

                    if (strippedText.contains(stripText)) {
                            String[] line = strippedText.split("(?=" + stripText + ")");
                            if (line[0].contains(stripText)) {
                                line = line[0].split(System.lineSeparator());
                            }
                            details.add("BODY SeLenIuM::::: " + (line[0]).trim());
                    } else {
                        details.add(("BODY SeLenIuM::::: " + strippedText).trim());
                    }
                }
                messages[i].setFlag(Flags.Flag.SEEN, mailSeenFlag);
            }
            inbox.close(true);
            this.store.close();
        } catch (MessagingException e) {
            System.exit(2);
        }

        ArrayList<String> fromList    = new ArrayList<>();
        ArrayList<String> subjectList = new ArrayList<>();
        ArrayList<String> bodyList    = new ArrayList<>();
        ArrayList<String> dateList    = new ArrayList<>();

        for (String detail : details) {
            String[] data = detail.split("SeLenIuM::::: ");
            
            if (data.length > 1) {
                String dataWithNoHTML = removeHTMLTags(data[1]);

                fromList    = addTo(fromList   , dataWithNoHTML, data[0].contains(emailFrom));
                subjectList = addTo(subjectList, dataWithNoHTML, data[0].contains(emailSubject));
                bodyList    = addTo(bodyList   , dataWithNoHTML, data[0].contains(emailBody));
                dateList    = addTo(dateList   , dataWithNoHTML, data[0].contains(emailDate));

            } 
        }

        HashMap<String, List<String>> hmap = new HashMap<>();
        
        hmap.put(emailFrom, new ArrayList<>(fromList));
        hmap.put(emailSubject, new ArrayList<>(subjectList));
        hmap.put(emailBody, new ArrayList<>(bodyList));
        hmap.put(emailDate, new ArrayList<>(dateList));
        hmap.put(emailDetails, new ArrayList<>(details));

        return hmap;
    }
    
    private ArrayList<String> addTo(ArrayList<String> list, String value, Boolean shouldAdd) {
    	ArrayList<String> updatedList = list;
    	
    	if (Boolean.TRUE.equals(shouldAdd))
    		updatedList.add(value);
    	
    	return updatedList;
    }

    protected ArrayList<String> getEmailBodyWithMultipleSubject(String fromEmail, String subjectLine, String folderPath) {
        ArrayList<String> bodyFromEmail = new ArrayList<String>();
        HashMap<String, List<String>> hmap = null;
        try {
            hmap = getEmailDataMap(fromEmail, folderPath);
        } catch (Exception e) {
        }
        
        if (hmap == null)
        	throw new IllegalStateException("email data map is empty!");
        
        for (int i = 0; i < hmap.get(emailFrom).size(); i++) {
            if (StringUtils.containsIgnoreCase((hmap.get(emailFrom).get(i)), fromEmail)) {
                if (StringUtils.containsIgnoreCase((hmap.get(emailSubject).get(i)), subjectLine)) {
                    bodyFromEmail.add(hmap.get(emailBody).get(i));
                }
            }
        }
        return bodyFromEmail;
    }

    protected String getEmailBodyWithUniqueSubject(String fromEmail, String subjectLine, String folderPath) {
        String messageBody = null;
        ArrayList<String> allBodyTextFromEmail = getEmailBodyWithMultipleSubject(fromEmail, subjectLine, folderPath);
        int subjectListSize = allBodyTextFromEmail.size();
        if (subjectListSize == 1) {
            messageBody = allBodyTextFromEmail.get(0);
        } else if (subjectListSize > 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < subjectListSize; i++) {
                sb.append(allBodyTextFromEmail.get(i) + " , ");
            }
            messageBody = sb.toString();
        }
        return messageBody;
    }

    protected boolean isEmailBodyExpected(String fromEmail, String subjectLine, String folderPath, String bodyText) {
        String emailBody = getEmailBodyWithUniqueSubject(fromEmail, subjectLine, folderPath);
        return StringUtils.containsIgnoreCase(emailBody.trim(), bodyText.trim());
    }

    private String removeHTMLTags(String source) {
    	// TODO: Should use StringEscapeUtils - https://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/StringEscapeUtils.html#unescapeHtml(java.io.Writer,%20java.lang.String)
        String nonHTML = source;
        String[] htmlTag = { "&quot;", "&amp;", "&rsquo;", "&nbsp;", "&#8230;", "&#8217;" };
        String[] htmlChar = { "\"", "&", "\u2019", "", "\u2026", "\u2019" };
        int index = 0;

        while (index < htmlTag.length) {
            nonHTML = this.replaceAll(nonHTML, htmlTag[index], htmlChar[index]);
            index++;
        }
        return nonHTML;
    }

    private String replaceAll(String source, String pattern, String replacement) {
        if (source == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int index;
        int patIndex = 0;
        while ((index = source.indexOf(pattern, patIndex)) != -1) {
            sb.append(source.substring(patIndex, index));
            sb.append(replacement);
            patIndex = index + pattern.length();
        }
        sb.append(source.substring(patIndex));
        return sb.toString();
    }

    protected void outputAllUnreadEmailData(String folderPath, Boolean storehashmap, Boolean debugLogs) throws MessagingException, IOException {
        if (debugLogs) {
            TestReporter.setDebugLevel(2);
        }
        Folder in;
        in = getMailFolder(folderPath);
        Message messages[] = in.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        in.fetch(messages, fp);
        reportAllUnreadEmailMessagesToConsole(messages, storehashmap);
        TestReporter.setDebugLevel(1);
    }

    private void reportAllUnreadEmailMessagesToConsole(Message[] msgs, Boolean storehashmap) throws MessagingException, IOException {
        for (int i = 0; i < msgs.length; i++) {
            TestReporter.logDebug("Email MESSAGE #" + (i + 1) + ":");
            metadataFromEmail(msgs[i], storehashmap, (i + 1));
        }
    }

    private void metadataFromEmail(Message message, Boolean storehashmap, int i) throws MessagingException, IOException {
    	
    	if (Boolean.FALSE.equals(storehashmap))
    		return;
    	
        Address[] a = message.getFrom();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

        if (a != null) {
            for (int j = 0; j < a.length; j++) {
                String fromData = a[j].toString();
                metadataMap.put("From#" + i, fromData);
            }
        }
        if (message.getSubject() != null) 
           metadataMap.put("Subject#" + i, message.getSubject());

        if (message.getReceivedDate() != null) 
            metadataMap.put("ReceivedDate#" + i, df.format(message.getReceivedDate()));
        
        if (message.getSentDate() != null) 
            metadataMap.put("SentDate#" + i, df.format(message.getSentDate()));
        
        if (message.getContent().toString() != null) 
            metadataMap.put("Email Content#" + i, getTextFromMessage(message));

    }

    private String getTextFromMessage(Message message) throws IOException, MessagingException {
        String result = "";
        if (message.isMimeType(MIME_TYPE)) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        } else {
            result = Jsoup.parse(message.getContent().toString()).text();
        }
        return result;
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType(MIME_TYPE)) {
                result = result + "\n" + bodyPart.getContent();
                break;
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }

    class OutlookBaseOld {
        Folder inbox;

        protected Message makeConnectionAndGetLastMessage(String folderPath) {
            Message lastMessage = null;
            try {
                inbox = getMailFolder(folderPath);
                Message messages[] = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
                FetchProfile fp = new FetchProfile();
                fp.add(FetchProfile.Item.ENVELOPE);
                inbox.fetch(messages, fp);
                lastMessage = messages[messages.length - 1];
            } catch (Exception e) {
                e.printStackTrace();
            }
            return lastMessage;
        }

        protected void closeConnection() {
            try {
                inbox.close(false);
                store.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected String getTextHTML(String folderPath) throws IOException, MessagingException {
            String lastMessageHTML = null;
            lastMessageHTML = makeConnectionAndGetLastMessage(folderPath).getContent().toString();
            closeConnection();
            return lastMessageHTML;
        }

        protected String getSubject(String folderPath) throws MessagingException {
            String lastMessageSubject = null;
            lastMessageSubject = makeConnectionAndGetLastMessage(folderPath).getSubject();
            closeConnection();
            return lastMessageSubject;
        }

        protected String getFrom(String folderPath) throws MessagingException {
            String lastMessageFrom = null;
            lastMessageFrom = makeConnectionAndGetLastMessage(folderPath).getFrom()[0].toString();
            closeConnection();
            return lastMessageFrom;
        }

        protected Date getDate(String folderPath) throws MessagingException {
            Date lastMessageDate = null;
            lastMessageDate = makeConnectionAndGetLastMessage(folderPath).getSentDate();
            closeConnection();
            return lastMessageDate;
        }
    }
}