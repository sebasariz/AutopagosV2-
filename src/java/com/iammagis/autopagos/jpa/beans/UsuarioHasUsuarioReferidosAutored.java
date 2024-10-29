/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "usuario_has_usuario_referidos_autored")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioHasUsuarioReferidosAutored.findAll", query = "SELECT u FROM UsuarioHasUsuarioReferidosAutored u"),
    @NamedQuery(name = "UsuarioHasUsuarioReferidosAutored.findByUsuarioidUsuarioreferente", query = "SELECT u FROM UsuarioHasUsuarioReferidosAutored u WHERE u.usuarioHasUsuarioReferidosAutoredPK.usuarioidUsuarioreferente = :usuarioidUsuarioreferente"),
    @NamedQuery(name = "UsuarioHasUsuarioReferidosAutored.findByUsuarioidUsuarioreferido", query = "SELECT u FROM UsuarioHasUsuarioReferidosAutored u WHERE u.usuarioHasUsuarioReferidosAutoredPK.usuarioidUsuarioreferido = :usuarioidUsuarioreferido"),
    @NamedQuery(name = "UsuarioHasUsuarioReferidosAutored.findByCodigoReferido", query = "SELECT u FROM UsuarioHasUsuarioReferidosAutored u WHERE u.codigoReferido = :codigoReferido")})
public class UsuarioHasUsuarioReferidosAutored implements Serializable {
    @Column(name = "puntos")
    private Integer puntos;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsuarioHasUsuarioReferidosAutoredPK usuarioHasUsuarioReferidosAutoredPK;
    @Column(name = "codigo_referido")
    private String codigoReferido;
    @JoinColumn(name = "usuario_idUsuario_referido", referencedColumnName = "idUsuario", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuario usuario;
    @JoinColumn(name = "usuario_idUsuario_referente", referencedColumnName = "idUsuario", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuario usuario1;

    public UsuarioHasUsuarioReferidosAutored() {
    }

    public UsuarioHasUsuarioReferidosAutored(UsuarioHasUsuarioReferidosAutoredPK usuarioHasUsuarioReferidosAutoredPK) {
        this.usuarioHasUsuarioReferidosAutoredPK = usuarioHasUsuarioReferidosAutoredPK;
    }

    public UsuarioHasUsuarioReferidosAutored(long usuarioidUsuarioreferente, long usuarioidUsuarioreferido) {
        this.usuarioHasUsuarioReferidosAutoredPK = new UsuarioHasUsuarioReferidosAutoredPK(usuarioidUsuarioreferente, usuarioidUsuarioreferido);
    }

    public UsuarioHasUsuarioReferidosAutoredPK getUsuarioHasUsuarioReferidosAutoredPK() {
        return usuarioHasUsuarioReferidosAutoredPK;
    }

    public void setUsuarioHasUsuarioReferidosAutoredPK(UsuarioHasUsuarioReferidosAutoredPK usuarioHasUsuarioReferidosAutoredPK) {
        this.usuarioHasUsuarioReferidosAutoredPK = usuarioHasUsuarioReferidosAutoredPK;
    }

    public String getCodigoReferido() {
        return codigoReferido;
    }

    public void setCodigoReferido(String codigoReferido) {
        this.codigoReferido = codigoReferido;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(Usuario usuario1) {
        this.usuario1 = usuario1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuarioHasUsuarioReferidosAutoredPK != null ? usuarioHasUsuarioReferidosAutoredPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioHasUsuarioReferidosAutored)) {
            return false;
        }
        UsuarioHasUsuarioReferidosAutored other = (UsuarioHasUsuarioReferidosAutored) object;
        if ((this.usuarioHasUsuarioReferidosAutoredPK == null && other.usuarioHasUsuarioReferidosAutoredPK != null) || (this.usuarioHasUsuarioReferidosAutoredPK != null && !this.usuarioHasUsuarioReferidosAutoredPK.equals(other.usuarioHasUsuarioReferidosAutoredPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.UsuarioHasUsuarioReferidosAutored[ usuarioHasUsuarioReferidosAutoredPK=" + usuarioHasUsuarioReferidosAutoredPK + " ]";
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }
    
}
