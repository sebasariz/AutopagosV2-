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
@Table(name = "transaccion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transaccion.findAll", query = "SELECT t FROM Transaccion t"),
    @NamedQuery(name = "Transaccion.findByIdtransaccion", query = "SELECT t FROM Transaccion t WHERE t.idtransaccion = :idtransaccion"),
    @NamedQuery(name = "Transaccion.findByFecha", query = "SELECT t FROM Transaccion t WHERE t.fecha = :fecha"),
    @NamedQuery(name = "Transaccion.findByValor", query = "SELECT t FROM Transaccion t WHERE t.valor = :valor")})
public class Transaccion implements Serializable {

    @Lob
    @Column(name = "codigofullcarga")
    private String codigofullcarga;

    @JoinColumn(name = "convenios_idconvenios", referencedColumnName = "idconvenios")
    @ManyToOne
    private Convenios conveniosIdconvenios;
    @Lob
    @Column(name = "referencia")
    private String referencia;
    @Lob
    @Column(name = "email")
    private String email;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idtransaccion")
    private Integer idtransaccion;
    @Lob
    @Column(name = "id_transaccion")
    private String idTransaccion;
    @Column(name = "fecha")
    private BigInteger fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private Double valor;
    @JoinColumn(name = "usuario_idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne
    private Usuario usuarioidUsuario;
    @JoinColumn(name = "tipoTransaccion_idtipoTransaccion", referencedColumnName = "idtipoTransaccion")
    @ManyToOne(optional = false)
    private TipoTransaccion tipoTransaccionidtipoTransaccion;
    @JoinColumn(name = "factura_autopagos_idfactura_autopagos", referencedColumnName = "idfactura_autopagos")
    @ManyToOne
    private FacturaAutopagos facturaAutopagosIdfacturaAutopagos;
    @JoinColumn(name = "factura_idfactura", referencedColumnName = "idfactura")
    @ManyToOne
    private Factura facturaIdfactura;
    @JoinColumn(name = "estado_idestado", referencedColumnName = "idestado")
    @ManyToOne(optional = false)
    private Estado estadoIdestado;
    @JoinColumn(name = "comprobanteRecaudoPSE_idcomprobanteRecaudoPSE", referencedColumnName = "idcomprobanteRecaudoPSE")
    @ManyToOne
    private ComprobanterecaudoPSE comprobanteRecaudoPSEidcomprobanteRecaudoPSE;

    public Transaccion() {
    }

    public Transaccion(Integer idtransaccion) {
        this.idtransaccion = idtransaccion;
    }

    public Integer getIdtransaccion() {
        return idtransaccion;
    }

    public void setIdtransaccion(Integer idtransaccion) {
        this.idtransaccion = idtransaccion;
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public BigInteger getFecha() {
        return fecha;
    }

    public void setFecha(BigInteger fecha) {
        this.fecha = fecha;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Usuario getUsuarioidUsuario() {
        return usuarioidUsuario;
    }

    public void setUsuarioidUsuario(Usuario usuarioidUsuario) {
        this.usuarioidUsuario = usuarioidUsuario;
    }

    public TipoTransaccion getTipoTransaccionidtipoTransaccion() {
        return tipoTransaccionidtipoTransaccion;
    }

    public void setTipoTransaccionidtipoTransaccion(TipoTransaccion tipoTransaccionidtipoTransaccion) {
        this.tipoTransaccionidtipoTransaccion = tipoTransaccionidtipoTransaccion;
    }

    public FacturaAutopagos getFacturaAutopagosIdfacturaAutopagos() {
        return facturaAutopagosIdfacturaAutopagos;
    }

    public void setFacturaAutopagosIdfacturaAutopagos(FacturaAutopagos facturaAutopagosIdfacturaAutopagos) {
        this.facturaAutopagosIdfacturaAutopagos = facturaAutopagosIdfacturaAutopagos;
    }

    public Factura getFacturaIdfactura() {
        return facturaIdfactura;
    }

    public void setFacturaIdfactura(Factura facturaIdfactura) {
        this.facturaIdfactura = facturaIdfactura;
    }

    public Estado getEstadoIdestado() {
        return estadoIdestado;
    }

    public void setEstadoIdestado(Estado estadoIdestado) {
        this.estadoIdestado = estadoIdestado;
    }

    public ComprobanterecaudoPSE getComprobanteRecaudoPSEidcomprobanteRecaudoPSE() {
        return comprobanteRecaudoPSEidcomprobanteRecaudoPSE;
    }

    public void setComprobanteRecaudoPSEidcomprobanteRecaudoPSE(ComprobanterecaudoPSE comprobanteRecaudoPSEidcomprobanteRecaudoPSE) {
        this.comprobanteRecaudoPSEidcomprobanteRecaudoPSE = comprobanteRecaudoPSEidcomprobanteRecaudoPSE;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtransaccion != null ? idtransaccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaccion)) {
            return false;
        }
        Transaccion other = (Transaccion) object;
        if ((this.idtransaccion == null && other.idtransaccion != null) || (this.idtransaccion != null && !this.idtransaccion.equals(other.idtransaccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.Transaccion[ idtransaccion=" + idtransaccion + " ]";
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Convenios getConveniosIdconvenios() {
        return conveniosIdconvenios;
    }

    public void setConveniosIdconvenios(Convenios conveniosIdconvenios) {
        this.conveniosIdconvenios = conveniosIdconvenios;
    }

    public String getCodigofullcarga() {
        return codigofullcarga;
    }

    public void setCodigofullcarga(String codigofullcarga) {
        this.codigofullcarga = codigofullcarga;
    }
    
}
