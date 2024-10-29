/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Usuario
 */
@Embeddable
public class UsuarioHasUsuarioReferidosAutoredPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "usuario_idUsuario_referente")
    private long usuarioidUsuarioreferente;
    @Basic(optional = false)
    @Column(name = "usuario_idUsuario_referido")
    private long usuarioidUsuarioreferido;

    public UsuarioHasUsuarioReferidosAutoredPK() {
    }

    public UsuarioHasUsuarioReferidosAutoredPK(long usuarioidUsuarioreferente, long usuarioidUsuarioreferido) {
        this.usuarioidUsuarioreferente = usuarioidUsuarioreferente;
        this.usuarioidUsuarioreferido = usuarioidUsuarioreferido;
    }

    public long getUsuarioidUsuarioreferente() {
        return usuarioidUsuarioreferente;
    }

    public void setUsuarioidUsuarioreferente(long usuarioidUsuarioreferente) {
        this.usuarioidUsuarioreferente = usuarioidUsuarioreferente;
    }

    public long getUsuarioidUsuarioreferido() {
        return usuarioidUsuarioreferido;
    }

    public void setUsuarioidUsuarioreferido(long usuarioidUsuarioreferido) {
        this.usuarioidUsuarioreferido = usuarioidUsuarioreferido;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) usuarioidUsuarioreferente;
        hash += (int) usuarioidUsuarioreferido;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioHasUsuarioReferidosAutoredPK)) {
            return false;
        }
        UsuarioHasUsuarioReferidosAutoredPK other = (UsuarioHasUsuarioReferidosAutoredPK) object;
        if (this.usuarioidUsuarioreferente != other.usuarioidUsuarioreferente) {
            return false;
        }
        if (this.usuarioidUsuarioreferido != other.usuarioidUsuarioreferido) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.UsuarioHasUsuarioReferidosAutoredPK[ usuarioidUsuarioreferente=" + usuarioidUsuarioreferente + ", usuarioidUsuarioreferido=" + usuarioidUsuarioreferido + " ]";
    }
    
}
