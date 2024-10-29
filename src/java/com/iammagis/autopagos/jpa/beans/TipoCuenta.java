/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "tipo_cuenta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoCuenta.findAll", query = "SELECT t FROM TipoCuenta t"),
    @NamedQuery(name = "TipoCuenta.findByIdtipoCuenta", query = "SELECT t FROM TipoCuenta t WHERE t.idtipoCuenta = :idtipoCuenta"),
    @NamedQuery(name = "TipoCuenta.findByNombre", query = "SELECT t FROM TipoCuenta t WHERE t.nombre = :nombre")})
public class TipoCuenta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idtipoCuenta")
    private Integer idtipoCuenta;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "tipoCuentaidtipoCuenta")
    private Collection<Convenios> conveniosCollection;

    public TipoCuenta() {
    }

    public TipoCuenta(Integer idtipoCuenta) {
        this.idtipoCuenta = idtipoCuenta;
    }

    public Integer getIdtipoCuenta() {
        return idtipoCuenta;
    }

    public void setIdtipoCuenta(Integer idtipoCuenta) {
        this.idtipoCuenta = idtipoCuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<Convenios> getConveniosCollection() {
        return conveniosCollection;
    }

    public void setConveniosCollection(Collection<Convenios> conveniosCollection) {
        this.conveniosCollection = conveniosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipoCuenta != null ? idtipoCuenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoCuenta)) {
            return false;
        }
        TipoCuenta other = (TipoCuenta) object;
        if ((this.idtipoCuenta == null && other.idtipoCuenta != null) || (this.idtipoCuenta != null && !this.idtipoCuenta.equals(other.idtipoCuenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.TipoCuenta[ idtipoCuenta=" + idtipoCuenta + " ]";
    }
    
}
