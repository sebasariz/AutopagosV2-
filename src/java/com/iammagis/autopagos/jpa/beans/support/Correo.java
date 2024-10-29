/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author sebastianarizmendy
 */
public class Correo extends Thread {

    //aqui va el correo
    static PropertiesAccess propertiesAcess = new PropertiesAccess();
    private String mensaje;
    private ArrayList<String> usuarios;
    private String subject;
    private File attach = null;
    private String context;

    public void sendCorreo(String subject, String mensaje, ArrayList<String> usuarios ) {
        this.mensaje = mensaje;
        this.usuarios = usuarios;
        this.subject = subject; 
        start();
    }

    public void sendCorreo(String subject, String mensaje, ArrayList<String> usuarios, File fileAttach) {
        this.mensaje = mensaje;
        this.usuarios = usuarios;
        this.subject = subject;
        this.attach = fileAttach; 
        start();
    }

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        try {

            //hacemos el switch con la lectura del archivo 
            Properties props = new Properties();
//            props.put("mail.transport.protocol", "smtps");
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtps.host", propertiesAcess.SMTP_HOST_PORT);
//            props.put("mail.smtps.auth", "true");
            props.put("mail.smtps.auth", "false");
            Session mailSession = Session.getDefaultInstance(props);
            // mailSession.setDebug(true);
            Transport transport = mailSession.getTransport();
            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject(subject);
            for (int i = 0; i < usuarios.size(); i++) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(usuarios.get(i)));
            }
            BodyPart messageBodyPart = new MimeBodyPart();
            message.setFrom(new InternetAddress(propertiesAcess.SMTP_AUTH_USER, "no-reply"));

            messageBodyPart.setContent(mensaje, "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            if (attach != null) {
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attach);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(attach.getName());
                multipart.addBodyPart(messageBodyPart);
            }
            message.setContent(multipart);
            transport.connect(propertiesAcess.SMTP_HOST_NAME, Integer.parseInt(propertiesAcess.SMTP_HOST_PORT), propertiesAcess.SMTP_AUTH_USER,
                    propertiesAcess.SMTP_AUTH_PWD);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            System.out.println("error no enviado");
            Logger.getLogger(CorreoZoho.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Correo.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }
}
