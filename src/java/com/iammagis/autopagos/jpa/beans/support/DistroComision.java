/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.UsuarioHasUsuarioReferidosAutored;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import com.iammagis.autopagos.jpa.control.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author root
 */
public class DistroComision {

    EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
    UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
    PropertiesAccess propertiesAccess = new PropertiesAccess();
    int comisionRepartida = 0;

    public int distribuirEscalaRedAutored(Usuario usuario) throws Exception {

        int nivel = 1;
        ArrayList<UsuarioHasUsuarioReferidosAutored> usuarioReferidosAutoreds = new ArrayList<>(usuario.getUsuarioHasUsuarioReferidosAutoredCollection());
//        System.out.println("usuarioReferente: " + usuarioReferidosAutoreds.size());
        setComision(usuarioReferidosAutoreds, nivel);
        return comisionRepartida;
    }

    private void setComision(ArrayList<UsuarioHasUsuarioReferidosAutored> usuarioHasUsuarioReferidosAutored, int nivel) throws NonexistentEntityException, Exception {

        for (UsuarioHasUsuarioReferidosAutored usuarioHasUsuarioReferidosAutored1 : usuarioHasUsuarioReferidosAutored) {
            Usuario usuario = usuarioHasUsuarioReferidosAutored1.getUsuario1();
//            System.out.println("nombre: " + usuario.getNombre() + " email: " + usuario.getEmail());
            if (nivel > 5) {
                break;
            } else if (nivel == 1) {
//                System.out.println("user: " + usuario.getEmail() + "nivel 1: " + usuario.getPuntos());
                usuario.setPuntos(usuario.getPuntos() + propertiesAccess.NIVEL1);
                comisionRepartida += propertiesAccess.NIVEL1;
            } else if (nivel == 2) {
//                System.out.println("user: " + usuario.getEmail() + "nivel 2: " + usuario.getPuntos());
                usuario.setPuntos(usuario.getPuntos() + propertiesAccess.NIVEL2);
                comisionRepartida += propertiesAccess.NIVEL2;
            } else if (nivel == 3) {
//                System.out.println("user: " + usuario.getEmail() + "nivel 3: " + usuario.getPuntos());
                usuario.setPuntos(usuario.getPuntos() + propertiesAccess.NIVEL3);
                comisionRepartida += propertiesAccess.NIVEL3;
            } else if (nivel == 4) {
//                System.out.println("user: " + usuario.getEmail() + "nivel 4: " + usuario.getPuntos());
                usuario.setPuntos(usuario.getPuntos() + propertiesAccess.NIVEL4);
                comisionRepartida += propertiesAccess.NIVEL4;
            } else if (nivel == 5) {
//                System.out.println("user: " + usuario.getEmail() + "nivel 5: " + usuario.getPuntos());
                usuario.setPuntos(usuario.getPuntos() + propertiesAccess.NIVEL5);
                comisionRepartida += propertiesAccess.NIVEL5;
            }
            usuario = usuarioJpaController.edit(usuario);
            ArrayList<UsuarioHasUsuarioReferidosAutored> usuarioReferidosAutoreds = new ArrayList<>(usuario.getUsuarioHasUsuarioReferidosAutoredCollection());
            nivel++;
            setComision(usuarioReferidosAutoreds, nivel);
        }

    }
 

}
