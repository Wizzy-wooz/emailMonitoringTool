package com.vodzianova.service;

import com.vodzianova.domain.EmailDetail;
import com.vodzianova.domain.Profile;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import java.io.File;
import java.util.Properties;

import static org.apache.log4j.Logger.getLogger;

/**
 * @author Elena Vodzianova 11/08/2016
 */
@Service
public class EmailReceiverService {

    //    TODO: test coverage with Mockito

    private static Logger logger = getLogger(EmailReceiverService.class);

    private Profile profile;

    @Autowired
    private EmailDetailService emailDetailService;

    /**
     * Returns a Properties object which is configured for a POP3/IMAP server
     *
     * @param protocol either "imap" or "pop3"
     * @param host
     * @param port
     * @return a Properties object
     */
    private Properties getServerProperties(String protocol, String host,
                                           String port) {
        Properties properties = new Properties();

        // server setting
        properties.put(String.format("mail.%s.host", protocol), host);
        properties.put(String.format("mail.%s.port", protocol), port);

        // SSL setting
        properties.setProperty(
                String.format("mail.%s.socketFactory.class", protocol),
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(
                String.format("mail.%s.socketFactory.fallback", protocol),
                "false");
        properties.setProperty(
                String.format("mail.%s.socketFactory.port", protocol),
                String.valueOf(port));

        return properties;
    }

    /**
     * Downloads new messages and fetches details for each message.
     *
     * @param protocol
     * @param host
     * @param port
     * @param userName
     * @param password
     */
    public void downloadEmails(Profile profile, String protocol, String host, String port,
                               String userName, String password, String saveDirectory) {
        Properties properties = getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(properties);

        try {
            // connects to the message store
            Store store = session.getStore(protocol);
            store.connect(userName, password);

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);

            // fetches new messages from server
            Message[] messages = folderInbox.getMessages();

            for (int i = messages.length - 1; i > 0; i--) {
                Message msg = messages[i];
                Address[] fromAddress = msg.getFrom();
                String from = fromAddress[0].toString();
                String subject = msg.getSubject();
                java.util.Date sentDate = msg.getSentDate();

                String messageContent = "";

                try {
                    Object content = msg.getContent();

                    if (content instanceof String) {
                        messageContent = (String) content;
                    } else if (content instanceof Multipart) {
                        Multipart mp = (Multipart) content;
                        messageContent = mp.getBodyPart(0).getContent().toString();
                    }

                    // store attachment file name, separated by comma
                    String attachFiles = "";
                    String contentType = msg.getContentType();

                    if (contentType.contains("multipart")) {
                        // content may contain attachments
                        Multipart multiPart = (Multipart) msg.getContent();
                        int numberOfParts = multiPart.getCount();
                        for (int partCount = 0; partCount < numberOfParts; partCount++) {
                            MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                                // this part is attachment
                                String fileName = part.getFileName();
                                attachFiles += fileName + ", ";
                                part.saveFile(saveDirectory + File.separator);
                            } else {
                                // this part may be the message content
                                messageContent = part.getContent().toString();
                            }
                        }

                        if (attachFiles.length() > 1) {
                            attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                        }
                    } else if (contentType.contains("text/plain")
                            || contentType.contains("text/html")) {
                        content = msg.getContent();
                        if (content != null) {
                            messageContent = content.toString();
                        }
                    }

                } catch (Exception ex) {
                    messageContent = "[Error downloading content]";
                    logger.error("Error occurred while downloading content: " + ex);
                }

                emailDetailService.save(new EmailDetail(messageContent, subject, from, sentDate, saveDirectory, profile));
                logger.info("Email detail for " + profile.getName() + "is saved...");
            }

            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            logger.error("No provider for protocol: " + protocol);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            logger.error("Could not connect to the message store");
            ex.printStackTrace();
        }
    }

    /**
     * Returns a list of addresses in String format separated by comma
     *
     * @param address an array of Address objects
     * @return a string represents a list of addresses
     */
    private String parseAddresses(Address[] address) {
        String listAddress = "";

        if (address != null) {
            for (Address addres : address) {
                listAddress += addres.toString() + ", ";
            }
        }
        if (listAddress.length() > 1) {
            listAddress = listAddress.substring(0, listAddress.length() - 2);
        }

        return listAddress;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}