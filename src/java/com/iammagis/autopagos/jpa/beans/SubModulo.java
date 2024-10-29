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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "subModulo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SubModulo.findAll", query = "SELECT s FROM SubModulo s"),
    @NamedQuery(name = "SubModulo.findByIdsubModulo", query = "SELECT s FROM SubModulo s WHERE s.idsubModulo = :idsubModulo"),
    @NamedQuery(name = "SubModulo.findByNombre", query = "SELECT s FROM SubModulo s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SubModulo.findByAccion", query = "SELECT s FROM SubModulo s WHERE s.accion = :accion")})
public class SubModulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idsubModulo")
    private Integer idsubModulo;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "accion")
    private String accion;
    @JoinTable(name = "usuario_has_subModulo", joinColumns = {
        @JoinColumn(name = "subModulo_idsubModulo", referencedColumnName = "idsubModulo")}, inverseJoinColumns = {
        @JoinColumn(name = "usuario_idUsuario", referencedColumnName = "idUsuario")})
    @ManyToMany
    private Collection<Usuario> usuarioCollection;
    @JoinColumn(name = "modulo_idmodulo", referencedColumnName = "idmodulo")
    @ManyToOne(optional = false)
    private Modulo moduloIdmodulo;

    public SubModulo() {
    }

    public SubModulo(Integer idsubModulo) {
        this.idsubModulo = idsubModulo;
    }

    public Integer getIdsubModulo() {
        return idsubModulo;
    }

    public void setIdsubModulo(Integer idsubModulo) {
        this.idsubModulo = idsubModulo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    @XmlTransient
    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    public Modulo getModuloIdmodulo() {
        return moduloIdmodulo;
    }

    public void setModuloIdmodulo(Modulo moduloIdmodulo) {
        this.moduloIdmodulo = moduloIdmodulo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idsubModulo != null ? idsubModulo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubModulo)) {
            return false;
        }
        SubModulo other = (SubModulo) object;
        if ((this.idsubModulo == null && other.idsubModulo != null) || (this.idsubModulo != null && !this.idsubModulo.equals(other.idsubModulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.SubModulo[ idsubModulo=" + idsubModulo + " ]";
    }
    
}
