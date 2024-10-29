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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "campo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Campo.findAll", query = "SELECT c FROM Campo c"),
    @NamedQuery(name = "Campo.findByIdcampo", query = "SELECT c FROM Campo c WHERE c.idcampo = :idcampo")})
public class Campo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcampo")
    private Integer idcampo;
    @Lob
    @Column(name = "nombre")
    private String nombre;
    @Lob
    @Column(name = "referencia")
    private String referencia;
    @Lob
    @Column(name = "xls")
    private String xls;
    @JoinColumn(name = "facturaTemplate_idfacturaTemplate", referencedColumnName = "idfacturaTemplate")
    @ManyToOne(optional = false)
    private FacturaTemplate facturaTemplateidfacturaTemplate;
    @Transient
    String valor;

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    

    public Campo() {
    }

    public Campo(Integer idcampo) {
        this.idcampo = idcampo;
    }

    public Integer getIdcampo() {
        return idcampo;
    }

    public void setIdcampo(Integer idcampo) {
        this.idcampo = idcampo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getXls() {
        return xls;
    }

    public void setXls(String xls) {
        this.xls = xls;
    }

    public FacturaTemplate getFacturaTemplateidfacturaTemplate() {
        return facturaTemplateidfacturaTemplate;
    }

    public void setFacturaTemplateidfacturaTemplate(FacturaTemplate facturaTemplateidfacturaTemplate) {
        this.facturaTemplateidfacturaTemplate = facturaTemplateidfacturaTemplate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcampo != null ? idcampo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Campo)) {
            return false;
        }
        Campo other = (Campo) object;
        if ((this.idcampo == null && other.idcampo != null) || (this.idcampo != null && !this.idcampo.equals(other.idcampo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.Campo[ idcampo=" + idcampo + " ]";
    }
    
}
