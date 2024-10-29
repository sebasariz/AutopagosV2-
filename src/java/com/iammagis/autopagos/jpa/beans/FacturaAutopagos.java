/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.struts.action.ActionForm;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "factura_autopagos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacturaAutopagos.findAll", query = "SELECT f FROM FacturaAutopagos f"),
    @NamedQuery(name = "FacturaAutopagos.findByIdfacturaAutopagos", query = "SELECT f FROM FacturaAutopagos f WHERE f.idfacturaAutopagos = :idfacturaAutopagos"),
    @NamedQuery(name = "FacturaAutopagos.findByFechaEmision", query = "SELECT f FROM FacturaAutopagos f WHERE f.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "FacturaAutopagos.findByValor", query = "SELECT f FROM FacturaAutopagos f WHERE f.valor = :valor"),
    @NamedQuery(name = "FacturaAutopagos.findByIva", query = "SELECT f FROM FacturaAutopagos f WHERE f.iva = :iva"),
    @NamedQuery(name = "FacturaAutopagos.findByNumeroTransaccionesConvenio", query = "SELECT f FROM FacturaAutopagos f WHERE f.numeroTransaccionesConvenio = :numeroTransaccionesConvenio")})
public class FacturaAutopagos extends ActionForm implements Serializable {
    @Lob
    @Column(name = "items")
    private String items;
    @Column(name = "tipo")
    private Integer tipo;
    @JoinColumn(name = "plan_idplan", referencedColumnName = "idplan")
    @ManyToOne
    private Plan planIdplan;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idfactura_autopagos")
    private Integer idfacturaAutopagos;
    @Column(name = "fecha_emision")
    private BigInteger fechaEmision;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private Double valor;
    @Column(name = "iva")
    private Double iva;
    @Lob
    @Column(name = "transacciones")
    private String transacciones;
    @Column(name = "numero_transacciones_convenio")
    private Integer numeroTransaccionesConvenio;
    @OneToMany(mappedBy = "facturaAutopagosIdfacturaAutopagos")
    private Collection<Transaccion> transaccionCollection;
    @JoinColumn(name = "estado_idestado", referencedColumnName = "idestado")
    @ManyToOne(optional = false)
    private Estado estadoIdestado;
    @JoinColumn(name = "convenios_idconvenios", referencedColumnName = "idconvenios")
    @ManyToOne(optional = false)
    private Convenios conveniosIdconvenios;
    @Transient
    int idconvenio;
    @Transient
    long fechaInicial;
    @Transient
    long fechaFinal;
    @Transient
    int idBanco;

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }
    
    
    public long getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(long fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public long getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(long fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
    
    

    public int getIdconvenio() {
        return idconvenio;
    }

    public void setIdconvenio(int idconvenio) {
        this.idconvenio = idconvenio;
    }
    
    
    public FacturaAutopagos() {
    }

    public FacturaAutopagos(Integer idfacturaAutopagos) {
        this.idfacturaAutopagos = idfacturaAutopagos;
    }

    public Integer getIdfacturaAutopagos() {
        return idfacturaAutopagos;
    }

    public void setIdfacturaAutopagos(Integer idfacturaAutopagos) {
        this.idfacturaAutopagos = idfacturaAutopagos;
    }

    public BigInteger getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(BigInteger fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public String getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(String transacciones) {
        this.transacciones = transacciones;
    }

    public Integer getNumeroTransaccionesConvenio() {
        return numeroTransaccionesConvenio;
    }

    public void setNumeroTransaccionesConvenio(Integer numeroTransaccionesConvenio) {
        this.numeroTransaccionesConvenio = numeroTransaccionesConvenio;
    }

    @XmlTransient
    public Collection<Transaccion> getTransaccionCollection() {
        return transaccionCollection;
    }

    public void setTransaccionCollection(Collection<Transaccion> transaccionCollection) {
        this.transaccionCollection = transaccionCollection;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idfacturaAutopagos != null ? idfacturaAutopagos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturaAutopagos)) {
            return false;
        }
        FacturaAutopagos other = (FacturaAutopagos) object;
        if ((this.idfacturaAutopagos == null && other.idfacturaAutopagos != null) || (this.idfacturaAutopagos != null && !this.idfacturaAutopagos.equals(other.idfacturaAutopagos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.FacturaAutopagos[ idfacturaAutopagos=" + idfacturaAutopagos + " ]";
    }

    public Plan getPlanIdplan() {
        return planIdplan;
    }

    public void setPlanIdplan(Plan planIdplan) {
        this.planIdplan = planIdplan;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
    
}
