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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "factura_template")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacturaTemplate.findAll", query = "SELECT f FROM FacturaTemplate f"),
    @NamedQuery(name = "FacturaTemplate.findByIdfacturaTemplate", query = "SELECT f FROM FacturaTemplate f WHERE f.idfacturaTemplate = :idfacturaTemplate")})
public class FacturaTemplate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idfacturaTemplate")
    private Integer idfacturaTemplate;
    @Lob
    @Column(name = "html")
    private String html;
    @Lob
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "facturaTemplateidfacturaTemplate")
    private Collection<Factura> facturaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facturaTemplateidfacturaTemplate")
    private Collection<Campo> campoCollection;
    @JoinColumn(name = "convenios_idconvenios", referencedColumnName = "idconvenios")
    @ManyToOne(optional = false)
    private Convenios conveniosIdconvenios;

    public FacturaTemplate() {
    }

    public FacturaTemplate(Integer idfacturaTemplate) {
        this.idfacturaTemplate = idfacturaTemplate;
    }

    public Integer getIdfacturaTemplate() {
        return idfacturaTemplate;
    }

    public void setIdfacturaTemplate(Integer idfacturaTemplate) {
        this.idfacturaTemplate = idfacturaTemplate;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<Factura> getFacturaCollection() {
        return facturaCollection;
    }

    public void setFacturaCollection(Collection<Factura> facturaCollection) {
        this.facturaCollection = facturaCollection;
    }

    @XmlTransient
    public Collection<Campo> getCampoCollection() {
        return campoCollection;
    }

    public void setCampoCollection(Collection<Campo> campoCollection) {
        this.campoCollection = campoCollection;
    }

    public Convenios getConveniosIdconvenios() {
        return conveniosIdconvenios;
    }

    public void setConveniosIdconvenios(Convenios conveniosIdconvenios) {
        this.conveniosIdconvenios = conveniosIdconvenios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idfacturaTemplate != null ? idfacturaTemplate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturaTemplate)) {
            return false;
        }
        FacturaTemplate other = (FacturaTemplate) object;
        if ((this.idfacturaTemplate == null && other.idfacturaTemplate != null) || (this.idfacturaTemplate != null && !this.idfacturaTemplate.equals(other.idfacturaTemplate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.FacturaTemplate[ idfacturaTemplate=" + idfacturaTemplate + " ]";
    }
    
}
