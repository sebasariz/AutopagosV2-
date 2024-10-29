/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "factura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Factura.findAll", query = "SELECT f FROM Factura f"),
    @NamedQuery(name = "Factura.findByIdfactura", query = "SELECT f FROM Factura f WHERE f.idfactura = :idfactura"),
    @NamedQuery(name = "Factura.findByValor", query = "SELECT f FROM Factura f WHERE f.valor = :valor"),
    @NamedQuery(name = "Factura.findByFechaCreacion", query = "SELECT f FROM Factura f WHERE f.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Factura.findByFechaEmision", query = "SELECT f FROM Factura f WHERE f.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "Factura.findByFechaVencimiento", query = "SELECT f FROM Factura f WHERE f.fechaVencimiento = :fechaVencimiento"),
    @NamedQuery(name = "Factura.findByValorComision", query = "SELECT f FROM Factura f WHERE f.valorComision = :valorComision")})
public class Factura extends ActionForm implements Serializable {

    @Lob
    @Column(name = "codigo_fullcarga")
    private String codigoFullcarga;
    @Column(name = "valor_pagado")
    private Double valorPagado;
    @Column(name = "visto")
    private Boolean visto;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idfactura")
    private Integer idfactura;
    @Lob
    @Column(name = "referencia")
    private String referencia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private Double valor;
    @Lob
    @Column(name = "email")
    private String email;
    @Column(name = "fechaCreacion")
    private BigInteger fechaCreacion;
    @Column(name = "fechaEmision")
    private BigInteger fechaEmision;
    @Column(name = "fechaVencimiento")
    private BigInteger fechaVencimiento;
    @Column(name = "valorComision")
    private Double valorComision;
    @Lob
    @Column(name = "nombre")
    private String nombre;
    @JoinTable(name = "factura_has_usuario", joinColumns = {
        @JoinColumn(name = "factura_idfactura", referencedColumnName = "idfactura")}, inverseJoinColumns = {
        @JoinColumn(name = "usuario_idUsuario", referencedColumnName = "idUsuario")})
    @ManyToMany
    private Collection<Usuario> usuarioCollection;
    @JoinColumn(name = "facturaTemplate_idfacturaTemplate", referencedColumnName = "idfacturaTemplate")
    @ManyToOne
    private FacturaTemplate facturaTemplateidfacturaTemplate;
    @JoinColumn(name = "estado_idestado", referencedColumnName = "idestado")
    @ManyToOne(optional = false)
    private Estado estadoIdestado;
    @JoinColumn(name = "convenios_idconvenios", referencedColumnName = "idconvenios")
    @ManyToOne(optional = false)
    private Convenios conveniosIdconvenios;
    @OneToMany(mappedBy = "facturaIdfactura")
    private Collection<Transaccion> transaccionCollection;
    @Transient
    FormFile fileFacturas;
    @Transient
    int plantilla;
    @Transient
    ArrayList<Campo> campos;
    @Transient
    int idBanco; 
    

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }
    
    
    public ArrayList<Campo> getCampos() {
        return campos;
    }

    public void setCampos(ArrayList<Campo> campos) {
        this.campos = campos;
    }
    
    

    public int getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(int plantilla) {
        this.plantilla = plantilla;
    }
    
    

    public FormFile getFileFacturas() {
        return fileFacturas;
    }

    public void setFileFacturas(FormFile fileFacturas) {
        this.fileFacturas = fileFacturas;
    }
    
    
    
    public Factura() {
    }

    public Factura(Integer idfactura) {
        this.idfactura = idfactura;
    }

    public Integer getIdfactura() {
        return idfactura;
    }

    public void setIdfactura(Integer idfactura) {
        this.idfactura = idfactura;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigInteger getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(BigInteger fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigInteger getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(BigInteger fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public BigInteger getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(BigInteger fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Double getValorComision() {
        return valorComision;
    }

    public void setValorComision(Double valorComision) {
        this.valorComision = valorComision;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    public FacturaTemplate getFacturaTemplateidfacturaTemplate() {
        return facturaTemplateidfacturaTemplate;
    }

    public void setFacturaTemplateidfacturaTemplate(FacturaTemplate facturaTemplateidfacturaTemplate) {
        this.facturaTemplateidfacturaTemplate = facturaTemplateidfacturaTemplate;
    }

    public Estado getEstadoIdestado() {
        return estadoIdestado;
    }

    public void setEstadoIdestado(Estado estadoIdestado) {
        this.estadoIdestado = estadoIdestado;
    }

    public Convenios getConveniosIdconvenios() {
        return conveniosIdconvenios;
    }

    public void setConveniosIdconvenios(Convenios conveniosIdconvenios) {
        this.conveniosIdconvenios = conveniosIdconvenios;
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
        hash += (idfactura != null ? idfactura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        if ((this.idfactura == null && other.idfactura != null) || (this.idfactura != null && !this.idfactura.equals(other.idfactura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.Factura[ idfactura=" + idfactura + " ]";
    }

    public Boolean getVisto() {
        return visto;
    }

    public void setVisto(Boolean visto) {
        this.visto = visto;
    }

    public Double getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(Double valorPagado) {
        this.valorPagado = valorPagado;
    }

    public String getCodigoFullcarga() {
        return codigoFullcarga;
    }

    public void setCodigoFullcarga(String codigoFullcarga) {
        this.codigoFullcarga = codigoFullcarga;
    }
    
}
