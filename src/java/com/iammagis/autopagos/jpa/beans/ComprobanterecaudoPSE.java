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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "comprobante_recaudo_PSE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComprobanterecaudoPSE.findAll", query = "SELECT c FROM ComprobanterecaudoPSE c"),
    @NamedQuery(name = "ComprobanterecaudoPSE.findByIdcomprobanteRecaudoPSE", query = "SELECT c FROM ComprobanterecaudoPSE c WHERE c.idcomprobanteRecaudoPSE = :idcomprobanteRecaudoPSE"),
    @NamedQuery(name = "ComprobanterecaudoPSE.findByFecha", query = "SELECT c FROM ComprobanterecaudoPSE c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "ComprobanterecaudoPSE.findByValorTotal", query = "SELECT c FROM ComprobanterecaudoPSE c WHERE c.valorTotal = :valorTotal"),
    @NamedQuery(name = "ComprobanterecaudoPSE.findByFechaRespuesta", query = "SELECT c FROM ComprobanterecaudoPSE c WHERE c.fechaRespuesta = :fechaRespuesta")})
public class ComprobanterecaudoPSE implements Serializable {

    @Column(name = "creditos")
    private Integer creditos;

    @Column(name = "intento")
    private Boolean intento;

    @Column(name = "id_avvillas")
    private Integer idAvvillas;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcomprobanteRecaudoPSE")
    private Integer idcomprobanteRecaudoPSE;
    @Column(name = "fecha")
    private BigInteger fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valorTotal")
    private Double valorTotal;
    @Lob
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fechaRespuesta")
    private BigInteger fechaRespuesta;
    @JoinColumn(name = "estado_idestado", referencedColumnName = "idestado")
    @ManyToOne(optional = false)
    private Estado estadoIdestado;
    @OneToMany(mappedBy = "comprobanteRecaudoPSEidcomprobanteRecaudoPSE")
    private Collection<Transaccion> transaccionCollection;

    public ComprobanterecaudoPSE() {
    }

    public ComprobanterecaudoPSE(Integer idcomprobanteRecaudoPSE) {
        this.idcomprobanteRecaudoPSE = idcomprobanteRecaudoPSE;
    }

    public Integer getIdcomprobanteRecaudoPSE() {
        return idcomprobanteRecaudoPSE;
    }

    public void setIdcomprobanteRecaudoPSE(Integer idcomprobanteRecaudoPSE) {
        this.idcomprobanteRecaudoPSE = idcomprobanteRecaudoPSE;
    }

    public BigInteger getFecha() {
        return fecha;
    }

    public void setFecha(BigInteger fecha) {
        this.fecha = fecha;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigInteger getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(BigInteger fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public Estado getEstadoIdestado() {
        return estadoIdestado;
    }

    public void setEstadoIdestado(Estado estadoIdestado) {
        this.estadoIdestado = estadoIdestado;
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
        hash += (idcomprobanteRecaudoPSE != null ? idcomprobanteRecaudoPSE.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComprobanterecaudoPSE)) {
            return false;
        }
        ComprobanterecaudoPSE other = (ComprobanterecaudoPSE) object;
        if ((this.idcomprobanteRecaudoPSE == null && other.idcomprobanteRecaudoPSE != null) || (this.idcomprobanteRecaudoPSE != null && !this.idcomprobanteRecaudoPSE.equals(other.idcomprobanteRecaudoPSE))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.ComprobanterecaudoPSE[ idcomprobanteRecaudoPSE=" + idcomprobanteRecaudoPSE + " ]";
    }

    public Integer getIdAvvillas() {
        return idAvvillas;
    }

    public void setIdAvvillas(Integer idAvvillas) {
        this.idAvvillas = idAvvillas;
    }

    public Boolean getIntento() {
        return intento;
    }

    public void setIntento(Boolean intento) {
        this.intento = intento;
    }

    public Integer getCreditos() {
        return creditos;
    }

    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }
    
}
