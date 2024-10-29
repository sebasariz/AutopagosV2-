/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "log_ingreso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LogIngreso.findAll", query = "SELECT l FROM LogIngreso l"),
    @NamedQuery(name = "LogIngreso.findByIdlogIngreso", query = "SELECT l FROM LogIngreso l WHERE l.idlogIngreso = :idlogIngreso"),
    @NamedQuery(name = "LogIngreso.findByFecha", query = "SELECT l FROM LogIngreso l WHERE l.fecha = :fecha")})
public class LogIngreso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idlogIngreso")
    private Integer idlogIngreso;
    @Column(name = "fecha")
    private BigInteger fecha;
    @JoinColumn(name = "usuario_idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario usuarioidUsuario;

    public LogIngreso() {
    }

    public LogIngreso(Integer idlogIngreso) {
        this.idlogIngreso = idlogIngreso;
    }

    public Integer getIdlogIngreso() {
        return idlogIngreso;
    }

    public void setIdlogIngreso(Integer idlogIngreso) {
        this.idlogIngreso = idlogIngreso;
    }

    public BigInteger getFecha() {
        return fecha;
    }

    public void setFecha(BigInteger fecha) {
        this.fecha = fecha;
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
        hash += (idlogIngreso != null ? idlogIngreso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LogIngreso)) {
            return false;
        }
        LogIngreso other = (LogIngreso) object;
        if ((this.idlogIngreso == null && other.idlogIngreso != null) || (this.idlogIngreso != null && !this.idlogIngreso.equals(other.idlogIngreso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.LogIngreso[ idlogIngreso=" + idlogIngreso + " ]";
    }
    
}