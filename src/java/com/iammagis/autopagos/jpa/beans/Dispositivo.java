/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans;
  
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "dispositivo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dispositivo.findAll", query = "SELECT d FROM Dispositivo d"),
    @NamedQuery(name = "Dispositivo.findByIddispositivo", query = "SELECT d FROM Dispositivo d WHERE d.iddispositivo = :iddispositivo")})
public class Dispositivo implements Serializable {

    @Column(name = "tipo")
    private Integer tipo;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iddispositivo")
    private Integer iddispositivo;
    @Lob
    @Column(name = "token")
    private String token;
    @JoinColumn(name = "usuario_idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario usuarioidUsuario;

    public Dispositivo() {
    }

    public Dispositivo(Integer iddispositivo) {
        this.iddispositivo = iddispositivo;
    }

    public Integer getIddispositivo() {
        return iddispositivo;
    }

    public void setIddispositivo(Integer iddispositivo) {
        this.iddispositivo = iddispositivo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getUsuarioidUsuario() {
        return usuarioidUsuario;
    }

    public void setUsuarioidUsuario(Usuario usuarioidUsuario) {
        this.usuarioidUsuario = usuarioidUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iddispositivo != null ? iddispositivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dispositivo)) {
            return false;
        }
        Dispositivo other = (Dispositivo) object;
        if ((this.iddispositivo == null && other.iddispositivo != null) || (this.iddispositivo != null && !this.iddispositivo.equals(other.iddispositivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.Dispositivo[ iddispositivo=" + iddispositivo + " ]";
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
    
}
