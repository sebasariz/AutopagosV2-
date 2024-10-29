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
@Table(name = "convenios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Convenios.findAll", query = "SELECT c FROM Convenios c"),
    @NamedQuery(name = "Convenios.findByIdconvenios", query = "SELECT c FROM Convenios c WHERE c.idconvenios = :idconvenios"),
    @NamedQuery(name = "Convenios.findByNombre", query = "SELECT c FROM Convenios c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Convenios.findByCodigo", query = "SELECT c FROM Convenios c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "Convenios.findByCodigoBarrasTercero", query = "SELECT c FROM Convenios c WHERE c.codigoBarrasTercero = :codigoBarrasTercero"),
    @NamedQuery(name = "Convenios.findByNit", query = "SELECT c FROM Convenios c WHERE c.nit = :nit"),
    @NamedQuery(name = "Convenios.findByDireccion", query = "SELECT c FROM Convenios c WHERE c.direccion = :direccion"),
    @NamedQuery(name = "Convenios.findByTelefono", query = "SELECT c FROM Convenios c WHERE c.telefono = :telefono"),
    @NamedQuery(name = "Convenios.findByNumeroCuenta", query = "SELECT c FROM Convenios c WHERE c.numeroCuenta = :numeroCuenta"),
    @NamedQuery(name = "Convenios.findByBanco", query = "SELECT c FROM Convenios c WHERE c.banco = :banco"),
    @NamedQuery(name = "Convenios.findByFechaCreacion", query = "SELECT c FROM Convenios c WHERE c.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Convenios.findByValorFijoConvenio", query = "SELECT c FROM Convenios c WHERE c.valorFijoConvenio = :valorFijoConvenio"),
    @NamedQuery(name = "Convenios.findByValorVariableConvenio", query = "SELECT c FROM Convenios c WHERE c.valorVariableConvenio = :valorVariableConvenio"),
    @NamedQuery(name = "Convenios.findByValorFijoUsuario", query = "SELECT c FROM Convenios c WHERE c.valorFijoUsuario = :valorFijoUsuario"),
    @NamedQuery(name = "Convenios.findByValorVariableUsuario", query = "SELECT c FROM Convenios c WHERE c.valorVariableUsuario = :valorVariableUsuario"),
    @NamedQuery(name = "Convenios.findByActivo", query = "SELECT c FROM Convenios c WHERE c.activo = :activo"),
    @NamedQuery(name = "Convenios.findByTipoConvenio", query = "SELECT c FROM Convenios c WHERE c.tipoConvenio = :tipoConvenio"),
    @NamedQuery(name = "Convenios.findByCodigoProductoTercero", query = "SELECT c FROM Convenios c WHERE c.codigoProductoTercero = :codigoProductoTercero"),
    @NamedQuery(name = "Convenios.findByValorFijoTercero", query = "SELECT c FROM Convenios c WHERE c.valorFijoTercero = :valorFijoTercero"),
    @NamedQuery(name = "Convenios.findByComisionTerceroFija", query = "SELECT c FROM Convenios c WHERE c.comisionTerceroFija = :comisionTerceroFija"),
    @NamedQuery(name = "Convenios.findByComisionTerceroVariable", query = "SELECT c FROM Convenios c WHERE c.comisionTerceroVariable = :comisionTerceroVariable"),
    @NamedQuery(name = "Convenios.findByFechaCortePlanPost", query = "SELECT c FROM Convenios c WHERE c.fechaCortePlanPost = :fechaCortePlanPost"),
    @NamedQuery(name = "Convenios.findByNumeroTransaccionesPlanPost", query = "SELECT c FROM Convenios c WHERE c.numeroTransaccionesPlanPost = :numeroTransaccionesPlanPost"),
    @NamedQuery(name = "Convenios.findByUsuarioCanalPlanPost", query = "SELECT c FROM Convenios c WHERE c.usuarioCanalPlanPost = :usuarioCanalPlanPost"),
    @NamedQuery(name = "Convenios.findByPassCanalPlanPost", query = "SELECT c FROM Convenios c WHERE c.passCanalPlanPost = :passCanalPlanPost"),
    @NamedQuery(name = "Convenios.findByCodigoCanalPlanPost", query = "SELECT c FROM Convenios c WHERE c.codigoCanalPlanPost = :codigoCanalPlanPost"),
    @NamedQuery(name = "Convenios.findByTransacciones", query = "SELECT c FROM Convenios c WHERE c.transacciones = :transacciones"),
    @NamedQuery(name = "Convenios.findByValorRecaudado", query = "SELECT c FROM Convenios c WHERE c.valorRecaudado = :valorRecaudado"),
    @NamedQuery(name = "Convenios.findByValorComisiones", query = "SELECT c FROM Convenios c WHERE c.valorComisiones = :valorComisiones")})
public class Convenios extends ActionForm implements Serializable {

    @Column(name = "recarga_tercero")
    private Integer recargaTercero;

    @OneToMany(mappedBy = "conveniosIdconvenios")
    private Collection<Transaccion> transaccionCollection;

    @Column(name = "banco_tercero")
    private Integer bancoTercero;
    @Column(name = "codigo_barras_tercero")
    private String codigoBarrasTercero;
    @Column(name = "canal_referenciado")
    private Boolean canalReferenciado;
    @Column(name = "valor_variable")
    private Boolean valorVariable;
    @Lob
    @Column(name = "campos_obligatorios_sipar")
    private String camposObligatoriosSipar;
    @Lob
    @Column(name = "campo_identificador_sipar")
    private String campoIdentificadorSipar;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idconvenios")
    private Integer idconvenios;
    @Column(name = "nombre")
    private String nombre;
    @Lob
    @Column(name = "logo")
    private String logo;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "nit")
    private String nit;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "numeroCuenta")
    private String numeroCuenta;
    @Lob
    @Column(name = "titularCuenta")
    private String titularCuenta;
    @Column(name = "banco")
    private String banco;
    @Column(name = "fechaCreacion")
    private BigInteger fechaCreacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valorFijoConvenio")
    private Double valorFijoConvenio;
    @Column(name = "valorVariableConvenio")
    private Double valorVariableConvenio;
    @Column(name = "valorFijoUsuario")
    private Double valorFijoUsuario;
    @Column(name = "valorVariableUsuario")
    private Double valorVariableUsuario;
    @Lob
    @Column(name = "direccionRespuesta")
    private String direccionRespuesta;
    @Lob
    @Column(name = "camaraComercio")
    private String camaraComercio;
    @Lob
    @Column(name = "rut")
    private String rut;
    @Lob
    @Column(name = "certificacionBancaria")
    private String certificacionBancaria;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "tipo_convenio")
    private Integer tipoConvenio;
    @Column(name = "codigoProductoTercero")
    private Integer codigoProductoTercero;
    @Column(name = "valorFijoTercero")
    private Boolean valorFijoTercero;
    @Lob
    @Column(name = "textoGuiaTercero")
    private String textoGuiaTercero;
    @Column(name = "comisionTerceroFija")
    private Double comisionTerceroFija;
    @Column(name = "comisionTerceroVariable")
    private Double comisionTerceroVariable;
    @Column(name = "fecha_corte_plan_post")
    private BigInteger fechaCortePlanPost;
    @Column(name = "numero_transacciones_plan_post")
    private Integer numeroTransaccionesPlanPost;
    @Column(name = "usuario_canal_plan_post")
    private String usuarioCanalPlanPost;
    @Column(name = "pass_canal_plan_post")
    private String passCanalPlanPost;
    @Column(name = "codigo_canal_plan_post")
    private String codigoCanalPlanPost;
    @Column(name = "transacciones")
    private Integer transacciones;
    @Column(name = "valor_recaudado")
    private Double valorRecaudado;
    @Column(name = "valor_comisiones")
    private Double valorComisiones;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conveniosIdconvenios")
    private Collection<Factura> facturaCollection;
    @OneToMany(mappedBy = "conveniosIdconvenios")
    private Collection<Usuario> usuarioCollection;
    @JoinColumn(name = "tipoCuenta_idtipoCuenta", referencedColumnName = "idtipoCuenta")
    @ManyToOne
    private TipoCuenta tipoCuentaidtipoCuenta;
    @JoinColumn(name = "plan_idplan", referencedColumnName = "idplan")
    @ManyToOne
    private Plan planIdplan;
    @JoinColumn(name = "clase_idclase", referencedColumnName = "idclase")
    @ManyToOne(optional = false)
    private Clase claseIdclase;
    @JoinColumn(name = "canal_idcanal_plan_post", referencedColumnName = "idcanal")
    @ManyToOne
    private Canal canalIdcanalPlanPost;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conveniosIdconvenios")
    private Collection<FacturaAutopagos> facturaAutopagosCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conveniosIdconvenios")
    private Collection<FacturaTemplate> facturaTemplateCollection;
    @Transient
    int idClase;
    @Transient
    int idTipoCuenta;
    @Transient
    int idCanal;
    @Transient
    FormFile logoFileForm;
    @Transient
    FormFile logoFileFormCCio;
    @Transient
    FormFile logoFileFormRut;
    @Transient
    FormFile logoFileFormBanco;
    @Transient
    int idPlan;
    @Transient
    String activoString;
    @Transient
    int tipoCuenta;
    @Transient
    double saldo;

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    
    
    public int getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(int tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
    
    
    public String getActivoString() {
        return activoString;
    }

    public void setActivoString(String activoString) {
        this.activoString = activoString;
    }
    
    
    
    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public int getIdTipoCuenta() {
        return idTipoCuenta;
    }

    public void setIdTipoCuenta(int idTipoCuenta) {
        this.idTipoCuenta = idTipoCuenta;
    }

    public int getIdCanal() {
        return idCanal;
    }

    public void setIdCanal(int idCanal) {
        this.idCanal = idCanal;
    }

    public FormFile getLogoFileForm() {
        return logoFileForm;
    }

    public void setLogoFileForm(FormFile logoFileForm) {
        this.logoFileForm = logoFileForm;
    }

    public FormFile getLogoFileFormCCio() {
        return logoFileFormCCio;
    }

    public void setLogoFileFormCCio(FormFile logoFileFormCCio) {
        this.logoFileFormCCio = logoFileFormCCio;
    }

    public FormFile getLogoFileFormRut() {
        return logoFileFormRut;
    }

    public void setLogoFileFormRut(FormFile logoFileFormRut) {
        this.logoFileFormRut = logoFileFormRut;
    }

    public FormFile getLogoFileFormBanco() {
        return logoFileFormBanco;
    }

    public void setLogoFileFormBanco(FormFile logoFileFormBanco) {
        this.logoFileFormBanco = logoFileFormBanco;
    }

    public int getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(int idPlan) {
        this.idPlan = idPlan;
    }
    
    
    public Convenios() {
    }

    public Convenios(Integer idconvenios) {
        this.idconvenios = idconvenios;
    }

    public Integer getIdconvenios() {
        return idconvenios;
    }

    public void setIdconvenios(Integer idconvenios) {
        this.idconvenios = idconvenios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTitularCuenta() {
        return titularCuenta;
    }

    public void setTitularCuenta(String titularCuenta) {
        this.titularCuenta = titularCuenta;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public BigInteger getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(BigInteger fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Double getValorFijoConvenio() {
        return valorFijoConvenio;
    }

    public void setValorFijoConvenio(Double valorFijoConvenio) {
        this.valorFijoConvenio = valorFijoConvenio;
    }

    public Double getValorVariableConvenio() {
        return valorVariableConvenio;
    }

    public void setValorVariableConvenio(Double valorVariableConvenio) {
        this.valorVariableConvenio = valorVariableConvenio;
    }

    public Double getValorFijoUsuario() {
        return valorFijoUsuario;
    }

    public void setValorFijoUsuario(Double valorFijoUsuario) {
        this.valorFijoUsuario = valorFijoUsuario;
    }

    public Double getValorVariableUsuario() {
        return valorVariableUsuario;
    }

    public void setValorVariableUsuario(Double valorVariableUsuario) {
        this.valorVariableUsuario = valorVariableUsuario;
    }

    public String getDireccionRespuesta() {
        return direccionRespuesta;
    }

    public void setDireccionRespuesta(String direccionRespuesta) {
        this.direccionRespuesta = direccionRespuesta;
    }

    public String getCamaraComercio() {
        return camaraComercio;
    }

    public void setCamaraComercio(String camaraComercio) {
        this.camaraComercio = camaraComercio;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getCertificacionBancaria() {
        return certificacionBancaria;
    }

    public void setCertificacionBancaria(String certificacionBancaria) {
        this.certificacionBancaria = certificacionBancaria;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getTipoConvenio() {
        return tipoConvenio;
    }

    public void setTipoConvenio(Integer tipoConvenio) {
        this.tipoConvenio = tipoConvenio;
    }

    public Integer getCodigoProductoTercero() {
        return codigoProductoTercero;
    }

    public void setCodigoProductoTercero(Integer codigoProductoTercero) {
        this.codigoProductoTercero = codigoProductoTercero;
    }

    public Boolean getValorFijoTercero() {
        return valorFijoTercero;
    }

    public void setValorFijoTercero(Boolean valorFijoTercero) {
        this.valorFijoTercero = valorFijoTercero;
    }

    public String getTextoGuiaTercero() {
        return textoGuiaTercero;
    }

    public void setTextoGuiaTercero(String textoGuiaTercero) {
        this.textoGuiaTercero = textoGuiaTercero;
    }

    public Double getComisionTerceroFija() {
        return comisionTerceroFija;
    }

    public void setComisionTerceroFija(Double comisionTerceroFija) {
        this.comisionTerceroFija = comisionTerceroFija;
    }

    public Double getComisionTerceroVariable() {
        return comisionTerceroVariable;
    }

    public void setComisionTerceroVariable(Double comisionTerceroVariable) {
        this.comisionTerceroVariable = comisionTerceroVariable;
    }

    public BigInteger getFechaCortePlanPost() {
        return fechaCortePlanPost;
    }

    public void setFechaCortePlanPost(BigInteger fechaCortePlanPost) {
        this.fechaCortePlanPost = fechaCortePlanPost;
    }

    public Integer getNumeroTransaccionesPlanPost() {
        return numeroTransaccionesPlanPost;
    }

    public void setNumeroTransaccionesPlanPost(Integer numeroTransaccionesPlanPost) {
        this.numeroTransaccionesPlanPost = numeroTransaccionesPlanPost;
    }

    public String getUsuarioCanalPlanPost() {
        return usuarioCanalPlanPost;
    }

    public void setUsuarioCanalPlanPost(String usuarioCanalPlanPost) {
        this.usuarioCanalPlanPost = usuarioCanalPlanPost;
    }

    public String getPassCanalPlanPost() {
        return passCanalPlanPost;
    }

    public void setPassCanalPlanPost(String passCanalPlanPost) {
        this.passCanalPlanPost = passCanalPlanPost;
    }

    public String getCodigoCanalPlanPost() {
        return codigoCanalPlanPost;
    }

    public void setCodigoCanalPlanPost(String codigoCanalPlanPost) {
        this.codigoCanalPlanPost = codigoCanalPlanPost;
    }

    public Integer getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(Integer transacciones) {
        this.transacciones = transacciones;
    }

    public Double getValorRecaudado() {
        return valorRecaudado;
    }

    public void setValorRecaudado(Double valorRecaudado) {
        this.valorRecaudado = valorRecaudado;
    }

    public Double getValorComisiones() {
        return valorComisiones;
    }

    public void setValorComisiones(Double valorComisiones) {
        this.valorComisiones = valorComisiones;
    }

    @XmlTransient
    public Collection<Factura> getFacturaCollection() {
        return facturaCollection;
    }

    public void setFacturaCollection(Collection<Factura> facturaCollection) {
        this.facturaCollection = facturaCollection;
    }

    @XmlTransient
    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    public TipoCuenta getTipoCuentaidtipoCuenta() {
        return tipoCuentaidtipoCuenta;
    }

    public void setTipoCuentaidtipoCuenta(TipoCuenta tipoCuentaidtipoCuenta) {
        this.tipoCuentaidtipoCuenta = tipoCuentaidtipoCuenta;
    }

    public Plan getPlanIdplan() {
        return planIdplan;
    }

    public void setPlanIdplan(Plan planIdplan) {
        this.planIdplan = planIdplan;
    }

    public Clase getClaseIdclase() {
        return claseIdclase;
    }

    public void setClaseIdclase(Clase claseIdclase) {
        this.claseIdclase = claseIdclase;
    }

    public Canal getCanalIdcanalPlanPost() {
        return canalIdcanalPlanPost;
    }

    public void setCanalIdcanalPlanPost(Canal canalIdcanalPlanPost) {
        this.canalIdcanalPlanPost = canalIdcanalPlanPost;
    }

    @XmlTransient
    public Collection<FacturaAutopagos> getFacturaAutopagosCollection() {
        return facturaAutopagosCollection;
    }

    public void setFacturaAutopagosCollection(Collection<FacturaAutopagos> facturaAutopagosCollection) {
        this.facturaAutopagosCollection = facturaAutopagosCollection;
    }

    @XmlTransient
    public Collection<FacturaTemplate> getFacturaTemplateCollection() {
        return facturaTemplateCollection;
    }

    public void setFacturaTemplateCollection(Collection<FacturaTemplate> facturaTemplateCollection) {
        this.facturaTemplateCollection = facturaTemplateCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idconvenios != null ? idconvenios.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Convenios)) {
            return false;
        }
        Convenios other = (Convenios) object;
        if ((this.idconvenios == null && other.idconvenios != null) || (this.idconvenios != null && !this.idconvenios.equals(other.idconvenios))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.Convenios[ idconvenios=" + idconvenios + " ]";
    }

    public String getCamposObligatoriosSipar() {
        return camposObligatoriosSipar;
    }

    public void setCamposObligatoriosSipar(String camposObligatoriosSipar) {
        this.camposObligatoriosSipar = camposObligatoriosSipar;
    }

    public String getCampoIdentificadorSipar() {
        return campoIdentificadorSipar;
    }

    public void setCampoIdentificadorSipar(String campoIdentificadorSipar) {
        this.campoIdentificadorSipar = campoIdentificadorSipar;
    }

    public Boolean getValorVariable() {
        return valorVariable;
    }

    public void setValorVariable(Boolean valorVariable) {
        this.valorVariable = valorVariable;
    }

    public Boolean getCanalReferenciado() {
        return canalReferenciado;
    }

    public void setCanalReferenciado(Boolean canalReferenciado) {
        this.canalReferenciado = canalReferenciado;
    }

    public String getCodigoBarrasTercero() {
        return codigoBarrasTercero;
    }

    public void setCodigoBarrasTercero(String codigoBarrasTercero) {
        this.codigoBarrasTercero = codigoBarrasTercero;
    }

    public Integer getBancoTercero() {
        return bancoTercero;
    }

    public void setBancoTercero(Integer bancoTercero) {
        this.bancoTercero = bancoTercero;
    }

    @XmlTransient
    public Collection<Transaccion> getTransaccionCollection() {
        return transaccionCollection;
    }

    public void setTransaccionCollection(Collection<Transaccion> transaccionCollection) {
        this.transaccionCollection = transaccionCollection;
    }

    public Integer getRecargaTercero() {
        return recargaTercero;
    }

    public void setRecargaTercero(Integer recargaTercero) {
        this.recargaTercero = recargaTercero;
    }
    
}
