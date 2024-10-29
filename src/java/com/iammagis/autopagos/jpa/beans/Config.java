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
import javax.persistence.Lob;
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
@Table(name = "config")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Config.findAll", query = "SELECT c FROM Config c"),
    @NamedQuery(name = "Config.findByIdconfig", query = "SELECT c FROM Config c WHERE c.idconfig = :idconfig"),
    @NamedQuery(name = "Config.findByAutoredNumeroTransacciones", query = "SELECT c FROM Config c WHERE c.autoredNumeroTransacciones = :autoredNumeroTransacciones"),
    @NamedQuery(name = "Config.findByAutoredNumeroFacturasPagadas", query = "SELECT c FROM Config c WHERE c.autoredNumeroFacturasPagadas = :autoredNumeroFacturasPagadas"),
    @NamedQuery(name = "Config.findByAutoredComisionActualAutopagos", query = "SELECT c FROM Config c WHERE c.autoredComisionActualAutopagos = :autoredComisionActualAutopagos"),
    @NamedQuery(name = "Config.findByAutoredComisionActualUsuariosAutored", query = "SELECT c FROM Config c WHERE c.autoredComisionActualUsuariosAutored = :autoredComisionActualUsuariosAutored"),
    @NamedQuery(name = "Config.findByAutoredComisionPagadaUsuariosAutored", query = "SELECT c FROM Config c WHERE c.autoredComisionPagadaUsuariosAutored = :autoredComisionPagadaUsuariosAutored"),
    @NamedQuery(name = "Config.findByAutoredComisionPorRedimirUsuariosAutored", query = "SELECT c FROM Config c WHERE c.autoredComisionPorRedimirUsuariosAutored = :autoredComisionPorRedimirUsuariosAutored"),
    @NamedQuery(name = "Config.findByAutoredNivelesHorizontales", query = "SELECT c FROM Config c WHERE c.autoredNivelesHorizontales = :autoredNivelesHorizontales"),
    @NamedQuery(name = "Config.findByAutoredNivelesVerticales", query = "SELECT c FROM Config c WHERE c.autoredNivelesVerticales = :autoredNivelesVerticales"),
    @NamedQuery(name = "Config.findByAutoredSaldoDisponible", query = "SELECT c FROM Config c WHERE c.autoredSaldoDisponible = :autoredSaldoDisponible"),
    @NamedQuery(name = "Config.findBySiparNumeroTransacciones", query = "SELECT c FROM Config c WHERE c.siparNumeroTransacciones = :siparNumeroTransacciones"),
    @NamedQuery(name = "Config.findBySiparNumeroFacturasCargadas", query = "SELECT c FROM Config c WHERE c.siparNumeroFacturasCargadas = :siparNumeroFacturasCargadas"),
    @NamedQuery(name = "Config.findBySiparNumeroFacturasPagadas", query = "SELECT c FROM Config c WHERE c.siparNumeroFacturasPagadas = :siparNumeroFacturasPagadas"),
    @NamedQuery(name = "Config.findBySiparValorTotalCargado", query = "SELECT c FROM Config c WHERE c.siparValorTotalCargado = :siparValorTotalCargado"),
    @NamedQuery(name = "Config.findBySiparValorTotalRecaudado", query = "SELECT c FROM Config c WHERE c.siparValorTotalRecaudado = :siparValorTotalRecaudado"),
    @NamedQuery(name = "Config.findBySiparValorTotalFacturadoAutopagos", query = "SELECT c FROM Config c WHERE c.siparValorTotalFacturadoAutopagos = :siparValorTotalFacturadoAutopagos"),
    @NamedQuery(name = "Config.findBySiparValorTotalRecaudadoAutopagos", query = "SELECT c FROM Config c WHERE c.siparValorTotalRecaudadoAutopagos = :siparValorTotalRecaudadoAutopagos"),
    @NamedQuery(name = "Config.findByOnlineCobrosRealizados", query = "SELECT c FROM Config c WHERE c.onlineCobrosRealizados = :onlineCobrosRealizados"),
    @NamedQuery(name = "Config.findByOnlineCobrosRecibidos", query = "SELECT c FROM Config c WHERE c.onlineCobrosRecibidos = :onlineCobrosRecibidos"),
    @NamedQuery(name = "Config.findByOnlineValorTotalRecibido", query = "SELECT c FROM Config c WHERE c.onlineValorTotalRecibido = :onlineValorTotalRecibido"),
    @NamedQuery(name = "Config.findByAutopagosDaemonFechaFacturacion", query = "SELECT c FROM Config c WHERE c.autopagosDaemonFechaFacturacion = :autopagosDaemonFechaFacturacion"),
    @NamedQuery(name = "Config.findByAutopagosDaemonFacturacionActivo", query = "SELECT c FROM Config c WHERE c.autopagosDaemonFacturacionActivo = :autopagosDaemonFacturacionActivo"),
    @NamedQuery(name = "Config.findByAutopagosDaemonPrivadosActivo", query = "SELECT c FROM Config c WHERE c.autopagosDaemonPrivadosActivo = :autopagosDaemonPrivadosActivo"),
    @NamedQuery(name = "Config.findByAutopagosEmailSoporte", query = "SELECT c FROM Config c WHERE c.autopagosEmailSoporte = :autopagosEmailSoporte"),
    @NamedQuery(name = "Config.findByAutopaogsDaemon", query = "SELECT c FROM Config c WHERE c.autopaogsDaemon = :autopaogsDaemon")})
public class Config implements Serializable {
    @Lob
    @Column(name = "autopagos_daemon_usuario")
    private String autopagosDaemonUsuario;
    @Lob
    @Column(name = "autopagos_daemon_pass")
    private String autopagosDaemonPass;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idconfig")
    private Integer idconfig;
    @Column(name = "autored_numero_transacciones")
    private Integer autoredNumeroTransacciones;
    @Column(name = "autored_numero_facturas_pagadas")
    private Integer autoredNumeroFacturasPagadas;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "autored_comision_actual_autopagos")
    private Double autoredComisionActualAutopagos;
    @Column(name = "autored_comision_actual_usuarios_autored")
    private Double autoredComisionActualUsuariosAutored;
    @Column(name = "autored_comision_pagada_usuarios_autored")
    private Double autoredComisionPagadaUsuariosAutored;
    @Column(name = "autored_comision_por_redimir_usuarios_autored")
    private Double autoredComisionPorRedimirUsuariosAutored;
    @Column(name = "autored_niveles_horizontales")
    private Integer autoredNivelesHorizontales;
    @Column(name = "autored_niveles_verticales")
    private Integer autoredNivelesVerticales;
    @Column(name = "autored_saldo_disponible")
    private Double autoredSaldoDisponible;
    @Column(name = "sipar_numero_transacciones")
    private Integer siparNumeroTransacciones;
    @Column(name = "sipar_numero_facturas_cargadas")
    private Integer siparNumeroFacturasCargadas;
    @Column(name = "sipar_numero_facturas_pagadas")
    private Integer siparNumeroFacturasPagadas;
    @Column(name = "sipar_valor_total_cargado")
    private Double siparValorTotalCargado;
    @Column(name = "sipar_valor_total_recaudado")
    private Double siparValorTotalRecaudado;
    @Column(name = "sipar_valor_total_facturado_autopagos")
    private Double siparValorTotalFacturadoAutopagos;
    @Column(name = "sipar_valor_total_recaudado_autopagos")
    private Double siparValorTotalRecaudadoAutopagos;
    @Column(name = "online_cobros_realizados")
    private Integer onlineCobrosRealizados;
    @Column(name = "online_cobros_recibidos")
    private Integer onlineCobrosRecibidos;
    @Column(name = "online_valor_total_recibido")
    private Double onlineValorTotalRecibido;
    @Column(name = "autopagos_daemon_fecha_facturacion")
    private BigInteger autopagosDaemonFechaFacturacion;
    @Column(name = "autopagos_daemon_facturacion_activo")
    private Boolean autopagosDaemonFacturacionActivo;
    @Column(name = "autopagos_daemon_privados_activo")
    private Boolean autopagosDaemonPrivadosActivo;
    @Column(name = "autopagos_email_soporte")
    private String autopagosEmailSoporte;
    @Column(name = "autopaogs_daemon")
    private Boolean autopaogsDaemon;
    @Transient
    String autopagosFechaString;
    @Transient
    int siparNumeroConvenios;

    public int getSiparNumeroConvenios() {
        return siparNumeroConvenios;
    }

    public void setSiparNumeroConvenios(int siparNumeroConvenios) {
        this.siparNumeroConvenios = siparNumeroConvenios;
    }
    
    
    public String getAutopagosFechaString() {
        return autopagosFechaString;
    }

    public void setAutopagosFechaString(String autopagosFechaString) {
        this.autopagosFechaString = autopagosFechaString;
    }
    
    
    
    public Config() {
    }

    public Config(Integer idconfig) {
        this.idconfig = idconfig;
    }

    public Integer getIdconfig() {
        return idconfig;
    }

    public void setIdconfig(Integer idconfig) {
        this.idconfig = idconfig;
    }

    public Integer getAutoredNumeroTransacciones() {
        return autoredNumeroTransacciones;
    }

    public void setAutoredNumeroTransacciones(Integer autoredNumeroTransacciones) {
        this.autoredNumeroTransacciones = autoredNumeroTransacciones;
    }

    public Integer getAutoredNumeroFacturasPagadas() {
        return autoredNumeroFacturasPagadas;
    }

    public void setAutoredNumeroFacturasPagadas(Integer autoredNumeroFacturasPagadas) {
        this.autoredNumeroFacturasPagadas = autoredNumeroFacturasPagadas;
    }

    public Double getAutoredComisionActualAutopagos() {
        return autoredComisionActualAutopagos;
    }

    public void setAutoredComisionActualAutopagos(Double autoredComisionActualAutopagos) {
        this.autoredComisionActualAutopagos = autoredComisionActualAutopagos;
    }

    public Double getAutoredComisionActualUsuariosAutored() {
        return autoredComisionActualUsuariosAutored;
    }

    public void setAutoredComisionActualUsuariosAutored(Double autoredComisionActualUsuariosAutored) {
        this.autoredComisionActualUsuariosAutored = autoredComisionActualUsuariosAutored;
    }

    public Double getAutoredComisionPagadaUsuariosAutored() {
        return autoredComisionPagadaUsuariosAutored;
    }

    public void setAutoredComisionPagadaUsuariosAutored(Double autoredComisionPagadaUsuariosAutored) {
        this.autoredComisionPagadaUsuariosAutored = autoredComisionPagadaUsuariosAutored;
    }

    public Double getAutoredComisionPorRedimirUsuariosAutored() {
        return autoredComisionPorRedimirUsuariosAutored;
    }

    public void setAutoredComisionPorRedimirUsuariosAutored(Double autoredComisionPorRedimirUsuariosAutored) {
        this.autoredComisionPorRedimirUsuariosAutored = autoredComisionPorRedimirUsuariosAutored;
    }

    public Integer getAutoredNivelesHorizontales() {
        return autoredNivelesHorizontales;
    }

    public void setAutoredNivelesHorizontales(Integer autoredNivelesHorizontales) {
        this.autoredNivelesHorizontales = autoredNivelesHorizontales;
    }

    public Integer getAutoredNivelesVerticales() {
        return autoredNivelesVerticales;
    }

    public void setAutoredNivelesVerticales(Integer autoredNivelesVerticales) {
        this.autoredNivelesVerticales = autoredNivelesVerticales;
    }

    public Double getAutoredSaldoDisponible() {
        return autoredSaldoDisponible;
    }

    public void setAutoredSaldoDisponible(Double autoredSaldoDisponible) {
        this.autoredSaldoDisponible = autoredSaldoDisponible;
    }

    public Integer getSiparNumeroTransacciones() {
        return siparNumeroTransacciones;
    }

    public void setSiparNumeroTransacciones(Integer siparNumeroTransacciones) {
        this.siparNumeroTransacciones = siparNumeroTransacciones;
    }

    public Integer getSiparNumeroFacturasCargadas() {
        return siparNumeroFacturasCargadas;
    }

    public void setSiparNumeroFacturasCargadas(Integer siparNumeroFacturasCargadas) {
        this.siparNumeroFacturasCargadas = siparNumeroFacturasCargadas;
    }

    public Integer getSiparNumeroFacturasPagadas() {
        return siparNumeroFacturasPagadas;
    }

    public void setSiparNumeroFacturasPagadas(Integer siparNumeroFacturasPagadas) {
        this.siparNumeroFacturasPagadas = siparNumeroFacturasPagadas;
    }

    public Double getSiparValorTotalCargado() {
        return siparValorTotalCargado;
    }

    public void setSiparValorTotalCargado(Double siparValorTotalCargado) {
        this.siparValorTotalCargado = siparValorTotalCargado;
    }

    public Double getSiparValorTotalRecaudado() {
        return siparValorTotalRecaudado;
    }

    public void setSiparValorTotalRecaudado(Double siparValorTotalRecaudado) {
        this.siparValorTotalRecaudado = siparValorTotalRecaudado;
    }

    public Double getSiparValorTotalFacturadoAutopagos() {
        return siparValorTotalFacturadoAutopagos;
    }

    public void setSiparValorTotalFacturadoAutopagos(Double siparValorTotalFacturadoAutopagos) {
        this.siparValorTotalFacturadoAutopagos = siparValorTotalFacturadoAutopagos;
    }

    public Double getSiparValorTotalRecaudadoAutopagos() {
        return siparValorTotalRecaudadoAutopagos;
    }

    public void setSiparValorTotalRecaudadoAutopagos(Double siparValorTotalRecaudadoAutopagos) {
        this.siparValorTotalRecaudadoAutopagos = siparValorTotalRecaudadoAutopagos;
    }

    public Integer getOnlineCobrosRealizados() {
        return onlineCobrosRealizados;
    }

    public void setOnlineCobrosRealizados(Integer onlineCobrosRealizados) {
        this.onlineCobrosRealizados = onlineCobrosRealizados;
    }

    public Integer getOnlineCobrosRecibidos() {
        return onlineCobrosRecibidos;
    }

    public void setOnlineCobrosRecibidos(Integer onlineCobrosRecibidos) {
        this.onlineCobrosRecibidos = onlineCobrosRecibidos;
    }

    public Double getOnlineValorTotalRecibido() {
        return onlineValorTotalRecibido;
    }

    public void setOnlineValorTotalRecibido(Double onlineValorTotalRecibido) {
        this.onlineValorTotalRecibido = onlineValorTotalRecibido;
    }

    public BigInteger getAutopagosDaemonFechaFacturacion() {
        return autopagosDaemonFechaFacturacion;
    }

    public void setAutopagosDaemonFechaFacturacion(BigInteger autopagosDaemonFechaFacturacion) {
        this.autopagosDaemonFechaFacturacion = autopagosDaemonFechaFacturacion;
    }

    public Boolean getAutopagosDaemonFacturacionActivo() {
        return autopagosDaemonFacturacionActivo;
    }

    public void setAutopagosDaemonFacturacionActivo(Boolean autopagosDaemonFacturacionActivo) {
        this.autopagosDaemonFacturacionActivo = autopagosDaemonFacturacionActivo;
    }

    public Boolean getAutopagosDaemonPrivadosActivo() {
        return autopagosDaemonPrivadosActivo;
    }

    public void setAutopagosDaemonPrivadosActivo(Boolean autopagosDaemonPrivadosActivo) {
        this.autopagosDaemonPrivadosActivo = autopagosDaemonPrivadosActivo;
    }

    public String getAutopagosEmailSoporte() {
        return autopagosEmailSoporte;
    }

    public void setAutopagosEmailSoporte(String autopagosEmailSoporte) {
        this.autopagosEmailSoporte = autopagosEmailSoporte;
    }

    public Boolean getAutopaogsDaemon() {
        return autopaogsDaemon;
    }

    public void setAutopaogsDaemon(Boolean autopaogsDaemon) {
        this.autopaogsDaemon = autopaogsDaemon;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idconfig != null ? idconfig.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Config)) {
            return false;
        }
        Config other = (Config) object;
        if ((this.idconfig == null && other.idconfig != null) || (this.idconfig != null && !this.idconfig.equals(other.idconfig))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.Config[ idconfig=" + idconfig + " ]";
    }

    public String getAutopagosDaemonUsuario() {
        return autopagosDaemonUsuario;
    }

    public void setAutopagosDaemonUsuario(String autopagosDaemonUsuario) {
        this.autopagosDaemonUsuario = autopagosDaemonUsuario;
    }

    public String getAutopagosDaemonPass() {
        return autopagosDaemonPass;
    }

    public void setAutopagosDaemonPass(String autopagosDaemonPass) {
        this.autopagosDaemonPass = autopagosDaemonPass;
    }
    
}
