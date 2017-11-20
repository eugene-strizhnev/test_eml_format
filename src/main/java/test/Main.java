package test;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Yevhen_Strizhnev on 11/20/2017.
 */
public class Main {

    public static void main(String... args) throws IOException, MessagingException {
        Properties props = new Properties();
        Session mailSession = Session.getDefaultInstance(props, null);
        InputStream source = new FileInputStream("Test_Message.eml");
        MimeMessage message = new MimeMessage(mailSession, source);
        System.out.println("Subject : " + message.getSubject());
        System.out.println("From : " + message.getFrom()[0]);
        System.out.println("--------------");
        System.out.println("Body : " + message.getContent());
        String contentType = message.getContentType();
        if (contentType.contains("multipart")) {
            System.out.println("Multipart EMail File");
            Multipart multiPart = (Multipart) message.getContent();
            int numberOfParts = multiPart.getCount();
            System.out.println("Parts:::" + numberOfParts);
            String wi = "IM-67890-PROCESS";
            String saveDir = System.getProperty("user.dir") + "\\Docs";
            saveDir = saveDir + File.separator + wi;
            boolean file = new File(saveDir).mkdir();
            if (file) {
                System.out.println("Directory: " + wi + " created");
                // logger.debug("Directory: " + workItem + " created");
            }

            for (int partCount = 0; partCount < numberOfParts; partCount++) {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                String disposition = part.getDisposition();
                if (Part.ATTACHMENT.equalsIgnoreCase(disposition)) {
                    // this part is attachment
                    String fileName = part.getFileName();
                    String extension = "";
                    System.out.println("Attached File Name::" + fileName);
                    saveDir = saveDir + File.separator + fileName;
                    int i = fileName.lastIndexOf(".");
                    if (i > 0) {
                        extension = fileName.substring(i + 1);
                    }
                    if (extension.equalsIgnoreCase("eml")) {
                        part.saveFile(saveDir);
                        extractEML(saveDir, wi);
                        System.out.println("This is a eml file");
                    } else if (extension.equalsIgnoreCase("msg")) {
                        part.saveFile(saveDir);
//                        extractMSG(saveDir,wi);
                        System.out.println("This is a msg file");
                    } else {
                        System.out.println("This is other file");
                    }
                } else {
                    Multipart content = (Multipart) part.getContent();
                    for (int i = 0; i < content.getCount(); i++) {
                        BodyPart bodyPart = content.getBodyPart(i);
                        if ("text/plain; charset=\"UTF-8\"".equals(bodyPart.getContentType())) {
                            System.out.println(bodyPart.getContent());
                        }
                    }

                }
            }
        }
    }


    public static void extractEML(String emlPath, String wi) throws MessagingException, IOException {
        //String fileName="";
        String path = System.getProperty("user.dir") + File.separator + "Docs" + File.separator + wi + File.separator + "EML_PDF";
        boolean file = new File(path).mkdir();
        if (file) {
            System.out.println("Folder EML_PDF Created Successfully");
        }
        Properties props = new Properties();
        Session mailSession = Session.getDefaultInstance(props, null);
        InputStream source = new FileInputStream(emlPath);
        MimeMessage message = new MimeMessage(mailSession, source);
        System.out.println("Subject : " + message.getSubject());
        System.out.println("From : " + message.getFrom()[0]);
        System.out.println("--------------");
        System.out.println("Body : " + message.getContent());
        String contentType = message.getContentType();
        if (contentType.contains("multipart")) {
            System.out.println("Multipart EMail File");
            Multipart multiPart = (Multipart) message.getContent();
            int numberOfParts = multiPart.getCount();
            System.out.println("Parts:::" + numberOfParts);
            for (int partCount = 0; partCount < numberOfParts; partCount++) {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    String fileName = part.getFileName();
                    String extension = "";
                    path = path + File.separator + fileName;
                    int i = fileName.lastIndexOf(".");
                    if (i > 0) {
                        extension = fileName.substring(i + 1);
                    }
                    if (extension.equalsIgnoreCase("pdf")) {
                        part.saveFile(path);
                    }
                }
            }
        }
    }
}
