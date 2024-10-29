/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import de.agitos.dkim.DKIMSigner;
import de.agitos.dkim.SMTPDKIMMessage;
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
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author sebasariz
 */
public class CorreoZoho extends Thread {

    //aqui va el correo
    static PropertiesAccess propertiesAcess = new PropertiesAccess();
    private final String mensaje;
    private final ArrayList<String> usuarios;
    private final String subject;
    private File attach = null;

    public static int inscripcionTemplate = 1;
    public static int modificadoUsuarioTemplate = 2;
    public static int solicitudPagoTemplate = 3;
    public static int recordarUsuarioTemplate = 4;
    public static int nuevaFacturaGeneradaTemplate = 5;
    public static int nuevaFacturaGeneradaLibreTemplate = 6;
    public static int pagoUsuarioTemplate = 7;
    public static int noPagoUsuarioTemplate = 8;
    public static int errorTemplate = 9;
    public static int servicioSuspendidoTemplate = 10;
    public static int recordatorioTemplate = 11;
    public static int solicitudDesembolsoTemplate = 12;
    public static int solicitudDesembolsoUsuarioTemplate = 13;
    public static int invitaiconTemplate = 14;

    public static String getTemplate(int type) {
        String template = "";

        try {
            BufferedReader in = null;
            if (type == inscripcionTemplate) {
                in = new BufferedReader(new FileReader(VariablesSession.context + File.separator + "templates/inscripcionTemplate.html"));
            } else if (type == modificadoUsuarioTemplate) {
                in = new BufferedReader(new FileReader(VariablesSession.context + File.separator + "templates/modificadoUsuarioTemplate.html"));
            } else if (type == solicitudPagoTemplate) {
                in = new BufferedReader(new FileReader(VariablesSession.context + File.separator + "templates/solicitudPagoTemplate.html"));
            } else if (type == recordarUsuarioTemplate) {
                in = new BufferedReader(new FileReader(VariablesSession.context + File.separator + "templates/recordarUsuarioTemplate.html"));
            } else if (type == nuevaFacturaGeneradaTemplate) {
                in = new BufferedReader(new FileReader(VariablesSession.context + File.separator + "templates/nuevaFacturaGeneradaTemplate.html"));
            } else if (type == nuevaFacturaGeneradaLibreTemplate) {
                in = new BufferedReader(new FileReader(VariablesSession.context + File.separator + "templates/nuevaFacturaGeneradaLibreTemplate.html"));
            } else if (type == pagoUsuarioTemplate) {
                in = new BufferedReader(new FileReader(VariablesSession.context + File.separator + "templates/pagoUsuarioTemplate.html"));
            } else if (type == noPagoUsuarioTemplate) {
                in = new BufferedReader(new FileReader(VariablesSession.context + File.separator + "templates/noPagoUsuarioTemplate.html"));
            } else if (type == errorTemplate) {
                in = new BufferedReader(new FileReader(VariablesSession.context + File.separator + "templates/errorTemplate.html"));
            } else if (type == servicioSuspendidoTemplate) {
                in = new BufferedReader(new FileReader(VariablesSession.context + File.separator + "templates/servicioSuspendidoTemplate.html"));
            } else if (type == recordatorioTemplate) {
                in = new BufferedReader(new FileReader(VariablesSession.context + File.separator + "templates/recordatorioTemplate.html"));
            } else if (type == solicitudDesembolsoTemplate) {
                in = new BufferedReader(new FileReader(VariablesSession.context + File.separator + "templates/solicitudDesembolsoTemplate.html"));
            } else if (type == solicitudDesembolsoUsuarioTemplate) {
                in = new BufferedReader(new FileReader(VariablesSession.context + File.separator + "templates/solicitudDesembolsoTemplate.html"));
            } else if (type == invitaiconTemplate) {
                in = new BufferedReader(new FileReader(VariablesSession.context + File.separator + "templates/invitaiconTemplate.html"));
            }
            String str;
            while ((str = in.readLine()) != null) {
                template += str;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return template;
    }

    @Override
    public void run() {
        try {
            enviarAlertas();

        } catch (MessagingException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(CorreoZoho.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    public CorreoZoho(String subject, String mensaje, ArrayList<String> usuarios) {
        this.mensaje = mensaje;
        this.usuarios = usuarios;
        this.subject = subject;
    }

    public CorreoZoho(String subject, String mensaje, ArrayList<String> usuarios, File fileAttach) {
        this.mensaje = mensaje;
        this.usuarios = usuarios;
        this.subject = subject;
        this.attach = fileAttach;
    }

    private void enviarAlertas()
            throws MessagingException, Exception {

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtps.host", propertiesAcess.SMTP_HOST_PORT);
            props.put("mail.smtps.auth", "true");
            SMTPAuthentication auth = new SMTPAuthentication();
            Session mailSession = Session.getInstance(props, auth);

            BodyPart messageBodyPart = new MimeBodyPart();
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
            ///////// beginning of DKIM FOR JAVAMAIL stuff
            // get DKIMSigner object
            File file = new File("keys/private.key.der");
            System.out.println("file: " + file.getAbsolutePath());
            DKIMSigner dkimSigner = new DKIMSigner("autopagos.co", "default", "keys/private.key.der");
            /* set an address or user-id of the user on behalf this message was signed;
             * this identity is up to you, except the domain part must be the signing domain
             * or a subdomain of the signing domain.
             */
            dkimSigner.setIdentity("no-reply@autopagos.co");
            // construct the JavaMail message using the DKIM message type from DKIM for JavaMail
            Message msg = new SMTPDKIMMessage(mailSession, dkimSigner);
            ///////// end of DKIM FOR JAVAMAIL stuff
            msg.setFrom(new InternetAddress("no-reply@autopagos.co"));
            msg.setSubject(subject);
            for (int i = 0; i < usuarios.size(); i++) {
                if (usuarios.get(i) != null && usuarios.get(i).contains("@") && usuarios.get(i).contains(".")) {
                    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(usuarios.get(i)));
                }
            }
            msg.setContent(multipart);
            // send the message by JavaMail
            //		Transport transport = session.getTransport("smtp");
            Transport transport = mailSession.getTransport("smtp");
            transport.connect(propertiesAcess.SMTP_HOST_NAME, Integer.parseInt(propertiesAcess.SMTP_HOST_PORT), propertiesAcess.SMTP_AUTH_USER,
                    propertiesAcess.SMTP_AUTH_PWD);
            if (msg.getRecipients(Message.RecipientType.TO) != null) {
                transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
                transport.close(); 
            }

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Correo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws Exception {
//        String template = getTemplate(inscripcionTemplate);
        ArrayList<String> mail = new ArrayList<String>();
        mail.add("sebasariz@iammagis.com");
//        Correo correo = new Correo("hola", "que mas", mail, "/home/sebasariz/NetBeansProjects/AkroPlatform/build/web/docs/1336772886693icon.png");
        CorreoZoho correo = new CorreoZoho("id:2019-09-24-18:54:27:223t", "<!DOCTYPE html> id:2019-09-24-18:54:27:223t", mail);

        try {
            correo.enviarAlertas();
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }

    }

    class SMTPAuthentication extends javax.mail.Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = "sebasariz";
            String password = "sanignacio";
            return new PasswordAuthentication(username, password);
        }
    }
}
