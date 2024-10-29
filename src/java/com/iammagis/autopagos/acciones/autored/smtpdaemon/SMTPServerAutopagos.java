/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.autored.smtpdaemon;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import org.subethamail.smtp.helper.SimpleMessageListenerAdapter;
import org.subethamail.smtp.server.SMTPServer;

/**
 *
 * @author Usuario
 */
public class SMTPServerAutopagos {

    private final MailSaver mailSaver = new MailSaver();
    private final MailListener myListener = new MailListener(mailSaver);
    private final SMTPServer smtpServer = new SMTPServer(new SimpleMessageListenerAdapter(myListener), new SMTPAuthHandlerFactory());

    public void startServer(int port, InetAddress bindAddress) {
        System.out.println("Starting server on port {}: " + port);
        try { 
            smtpServer.setBindAddress(bindAddress);
            smtpServer.setPort(port); 
            smtpServer.start();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void stopServer() {
        if (smtpServer.isRunning()) {
            System.out.println("STOPPING server");
            smtpServer.stop();
        }
    }

    public SMTPServer getSmtpServer() {
        return smtpServer;
    }

    public static void main(String[] args) throws UnknownHostException, IOException, NoSuchAlgorithmException {
          

        SMTPServerAutopagos sMTPServerAutopagos = new SMTPServerAutopagos();
        sMTPServerAutopagos.startServer(25, null);

    }

}
