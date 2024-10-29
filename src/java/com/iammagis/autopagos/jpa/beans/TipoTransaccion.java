/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "tipo_transaccion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoTransaccion.findAll", query = "SELECT t FROM TipoTransaccion t"),
    @NamedQuery(name = "TipoTransaccion.findByIdtipoTransaccion", query = "SELECT t FROM TipoTransaccion t WHERE t.idtipoTransaccion = :idtipoTransaccion"),
    @NamedQuery(name = "TipoTransaccion.findByNombre", query = "SELECT t FROM TipoTransaccion t WHERE t.nombre = :nombre")})
public class TipoTransaccion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idtipoTransaccion")
    private Integer idtipoTransaccion;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoTransaccionidtipoTransaccion")
    private Collection<Transaccion> transaccionCollection;

    public TipoTransaccion() {
    }

    public TipoTransaccion(Integer idtipoTransaccion) {
        this.idtipoTransaccion = idtipoTransaccion;
    }

    public Integer getIdtipoTransaccion() {
        return idtipoTransaccion;
    }

    public void setIdtipoTransaccion(Integer idtipoTransaccion) {
        this.idtipoTransaccion = idtipoTransaccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<Transaccion> getTransaccionCollection() {
        return transaccionCollection;
    }

    public void setTransaccionCollection(Collection<Transaccion> transaccionCollection) {
        this.transaccionCollection = transaccionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipoTransaccion != null ? idtipoTransaccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoTransaccion)) {
            return false;
        }
        TipoTransaccion other = (TipoTransaccion) object;
        if ((this.idtipoTransaccion == null && other.idtipoTransaccion != null) || (this.idtipoTransaccion != null && !this.idtipoTransaccion.equals(other.idtipoTransaccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.TipoTransaccion[ idtipoTransaccion=" + idtipoTransaccion + " ]";
    }
    
}
