/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import java.util.ResourceBundle;

/**
 *
 * @author Usuario
 */
public class PropertiesAccess {

    private static final String OPTION_FILE_NAME = "com/iammagis/autopagos/server";

    public String SMTP_HOST_NAME;
    public String SMTP_HOST_PORT;
    public String SMTP_AUTH_USER;
    public String SMTP_AUTH_PWD;
    public String SERVER;

    public double IVA;
    public double VALORRETIRO;
    public int TIME_MINUTES;

    public String NIT;
    public String DIRECCION;
    public String TELEFONO;
    public String RESOLUCION;
    public String FACTURADIOR;
    public String CORREO;

    public String CORREOAVVILLAS;
    public String CORREOBANCOLOMBIA;
    public String CORREOBBVA;

    public int tiempoFrecuenciaDaemonPrivado;
    public double VALORFIJOCOMISIONCONVENIO;
    public double VALORVARIABLECOMISIONCONVENIO;
    public double VALORFIJOCOMISIONUSUARIO;
    public double VALORVARIABLECOMISIONUSUARIO;
    public double VALORMINIMOCOMISION;

    public String KEYGOOGLE;

    public String BBVALINKINDEPENDIENTE;

    public String BBVALINK;
    public String BBVALINKLOGIN;
    public String BBVALINKDATA;
    public String BBVAUSER;
    public String BBVAPASS;

    public String BANCOLOMBIALINK;
    public String BANCOLOMBIALINKLOGIN;
    public String BANCOLOMBIALINKDATA;

    public String WINREDIP;
    public int WINREDPORT;
    public String WINREDUSER;
    public String WINREDPASS;
    public String WINREDVERSION;
    public String WINREDCEL;
    public double WINREDMONTOMINIMO;
    public String WINREDTERMINAL;

    public String AVVILLASLINK;
    public String CODIGOCONVENIOAVVILLAS;
    public String DOMAINAPROB;

    public String HOSTFULLCARGA;
    public int PORTFULLCARGA;
    public String TPVFULLCARGA;
    public String CLAVETPVFULLCARGA;
    public String CLAVEENCRIPTADAFULLCARGA;
    public String VERSIONFULLCARGA;
    public String CELLFULLCARGA;
    public double VALORCOMISIONAVVILLAS;
    public String codigoBBVA;
    public String codigoAVVILLAS;
    public String CODIGOCLIENTE;

    public int NIVEL1;
    public int NIVEL2;
    public int NIVEL3;
    public int NIVEL4;
    public int NIVEL5;

    public int ValorLimiteComision;
    public int SALDOINICIAL;

    public String PUNTORED_ENDPOINT;
    public int PUNTORED_ID_MAYORISTA;
    public int PUNTORED_ID_DISTRIBUIDOR;
    public int PUNTORED_ID_ALIADO;
    public int PUNTORED_ID_PUNTO_ATENCION;
    public int PUNTORED_TERMINAL;
    public int PUNTORED_CLAVE_CXR;
    public String PUNTORED_USUARIO_HOST;
    public String PUNTORED_CLAVE;

    public PropertiesAccess() {
        ResourceBundle pe = ResourceBundle.getBundle(OPTION_FILE_NAME);
        SMTP_HOST_NAME = pe.getString("SMTP_HOST_NAME").trim();

        SMTP_AUTH_USER = pe.getString("SMTP_AUTH_USER").trim();
        SMTP_AUTH_PWD = pe.getString("SMTP_AUTH_PWD").trim();
        SERVER = pe.getString("SERVER").trim();
        NIT = pe.getString("NIT").trim();
        DIRECCION = pe.getString("DIRECCION").trim();
        TELEFONO = pe.getString("TELEFONO").trim();
        RESOLUCION = pe.getString("RESOLUCION").trim();
        FACTURADIOR = pe.getString("FACTURADIOR").trim();
        CORREO = pe.getString("CORREO").trim();
        KEYGOOGLE = pe.getString("KEYGOOGLE").trim();
        BBVALINKINDEPENDIENTE = pe.getString("BBVALINKINDEPENDIENTE").trim();
        BBVALINK = pe.getString("BBVALINK").trim();
        BBVALINKLOGIN = pe.getString("BBVALINKLOGIN").trim();
        BBVALINKDATA = pe.getString("BBVALINKDATA").trim();
        BBVAUSER = pe.getString("BBVAUSER").trim();
        BBVAPASS = pe.getString("BBVAPASS").trim();
        BANCOLOMBIALINK = pe.getString("BANCOLOMBIALINK").trim();
        BANCOLOMBIALINKLOGIN = pe.getString("BANCOLOMBIALINKLOGIN").trim();
        BANCOLOMBIALINKDATA = pe.getString("BANCOLOMBIALINKDATA").trim();
        
        SMTP_HOST_NAME = pe.getString("SMTP_HOST_NAME").trim();
        SMTP_HOST_NAME = pe.getString("SMTP_HOST_NAME").trim();
        SMTP_HOST_PORT = pe.getString("SMTP_HOST_PORT").trim();

        TIME_MINUTES = Integer.parseInt(pe.getString("TIME_MINUTES").trim()); 

        IVA = Double.parseDouble(pe.getString("IVA").trim());
        VALORRETIRO = Double.parseDouble(pe.getString("VALORRETIRO").trim());
        VALORFIJOCOMISIONCONVENIO = Double.parseDouble(pe.getString("VALORFIJOCOMISIONCONVENIO").trim());
        VALORVARIABLECOMISIONCONVENIO = Double.parseDouble(pe.getString("VALORVARIABLECOMISIONCONVENIO").trim());
        VALORFIJOCOMISIONUSUARIO = Double.parseDouble(pe.getString("VALORFIJOCOMISIONUSUARIO").trim());
        VALORVARIABLECOMISIONUSUARIO = Double.parseDouble(pe.getString("VALORVARIABLECOMISIONUSUARIO").trim());
        VALORMINIMOCOMISION = Double.parseDouble(pe.getString("VALORMINIMOCOMISION").trim());
   
        AVVILLASLINK = pe.getString("AVVILLASLINK").trim();
        CODIGOCONVENIOAVVILLAS = pe.getString("CODIGOCONVENIOAVVILLAS").trim();
        DOMAINAPROB = pe.getString("DOMAINAPROB").trim();
 
        VALORCOMISIONAVVILLAS = Double.parseDouble(pe.getString("VALORCOMISIONAVVILLAS"));

        codigoBBVA = pe.getString("codigoBBVA");
        codigoAVVILLAS = pe.getString("codigoAVVILLAS"); 
        CORREOAVVILLAS = pe.getString("CORREOAVVILLAS");
        CORREOBANCOLOMBIA = pe.getString("CORREOBANCOLOMBIA");
        CORREOBBVA = pe.getString("CORREOBBVA");

        NIVEL1 = Integer.parseInt(pe.getString("NIVEL1"));
        NIVEL2 = Integer.parseInt(pe.getString("NIVEL2"));
        NIVEL3 = Integer.parseInt(pe.getString("NIVEL3"));
        NIVEL4 = Integer.parseInt(pe.getString("NIVEL4"));
        NIVEL5 = Integer.parseInt(pe.getString("NIVEL5"));

        tiempoFrecuenciaDaemonPrivado = Integer.parseInt(pe.getString("tiempoFrecuenciaDaemonPrivado"));
        ValorLimiteComision = Integer.parseInt(pe.getString("ValorLimiteComision"));
        SALDOINICIAL = Integer.parseInt(pe.getString("SALDOINICIAL"));

        PUNTORED_ENDPOINT = pe.getString("PUNTORED_ENDPOINT");
        PUNTORED_ID_MAYORISTA = Integer.parseInt(pe.getString("PUNTORED_ID_MAYORISTA"));
        PUNTORED_ID_DISTRIBUIDOR = Integer.parseInt(pe.getString("PUNTORED_ID_DISTRIBUIDOR"));
        PUNTORED_ID_ALIADO = Integer.parseInt(pe.getString("PUNTORED_ID_ALIADO"));
        PUNTORED_ID_PUNTO_ATENCION = Integer.parseInt(pe.getString("PUNTORED_ID_PUNTO_ATENCION"));
        PUNTORED_TERMINAL = Integer.parseInt(pe.getString("PUNTORED_TERMINAL"));
        PUNTORED_CLAVE_CXR = Integer.parseInt(pe.getString("PUNTORED_CLAVE_CXR"));
        PUNTORED_USUARIO_HOST = pe.getString("PUNTORED_USUARIO_HOST");
        PUNTORED_CLAVE = pe.getString("PUNTORED_CLAVE");

    }

}
