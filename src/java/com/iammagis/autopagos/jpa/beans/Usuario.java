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

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByIdUsuario", query = "SELECT u FROM Usuario u WHERE u.idUsuario = :idUsuario"),
    @NamedQuery(name = "Usuario.findByPass", query = "SELECT u FROM Usuario u WHERE u.pass = :pass"),
    @NamedQuery(name = "Usuario.findByNombre", query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "Usuario.findByApellidos", query = "SELECT u FROM Usuario u WHERE u.apellidos = :apellidos"),
    @NamedQuery(name = "Usuario.findByNumeroDeDocumento", query = "SELECT u FROM Usuario u WHERE u.numeroDeDocumento = :numeroDeDocumento"),
    @NamedQuery(name = "Usuario.findByEmail", query = "SELECT u FROM Usuario u WHERE u.email = :email"),
    @NamedQuery(name = "Usuario.token", query = "SELECT u FROM Usuario u WHERE u.sesionToken = :sesionToken"),
    @NamedQuery(name = "Usuario.login", query = "SELECT u FROM Usuario u WHERE u.email = :email AND u.pass = :pass"),
    @NamedQuery(name = "Usuario.findByCelular", query = "SELECT u FROM Usuario u WHERE u.celular = :celular"),
    @NamedQuery(name = "Usuario.findByFechaRegistro", query = "SELECT u FROM Usuario u WHERE u.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "Usuario.findByActivoAutored", query = "SELECT u FROM Usuario u WHERE u.activoAutored = :activoAutored")})
public class Usuario extends ActionForm implements Serializable {
    @Column(name = "puntos")
    private Integer puntos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioidUsuario")
    private Collection<Codigo> codigoCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUsuario")
    private Long idUsuario;
    @Column(name = "pass")
    private String pass;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellidos")
    private String apellidos;
    @Column(name = "numeroDeDocumento")
    private String numeroDeDocumento;
    @Column(name = "email")
    private String email;
    @Column(name = "celular")
    private String celular;
    @Lob
    @Column(name = "foto")
    private String foto;
    @Column(name = "fechaRegistro")
    private BigInteger fechaRegistro;
    @Lob
    @Column(name = "sesionToken")
    private String sesionToken;
    @Column(name = "activo_autored")
    private Boolean activoAutored;
    @ManyToMany(mappedBy = "usuarioCollection")
    private Collection<Factura> facturaCollection;
    @ManyToMany(mappedBy = "usuarioCollection")
    private Collection<Modulo> moduloCollection;
    @ManyToMany(mappedBy = "usuarioCollection")
    private Collection<SubModulo> subModuloCollection;
    @OneToMany(mappedBy = "usuarioidUsuario")
    private Collection<Transaccion> transaccionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private Collection<UsuarioHasUsuarioReferidosAutored> usuarioHasUsuarioReferidosAutoredCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario1")
    private Collection<UsuarioHasUsuarioReferidosAutored> usuarioHasUsuarioReferidosAutoredCollection1;
    @JoinColumn(name = "tipo_usuario_IDTipoUsuario", referencedColumnName = "IDTipoUsuario")
    @ManyToOne(optional = false)
    private TipoUsuario tipousuarioIDTipoUsuario;
    @JoinColumn(name = "convenios_idconvenios", referencedColumnName = "idconvenios")
    @ManyToOne
    private Convenios conveniosIdconvenios;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioidUsuario")
    private Collection<Notificacion> notificacionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioidUsuario")
    private Collection<Dispositivo> dispositivoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioidUsuario")
    private Collection<LogIngreso> logIngresoCollection;
    @Transient
    int idTipoUsuario;
    @Transient
    int idConvenio;
    @Transient
    String tipoUsuarioString;

    public String getTipoUsuarioString() {
        return tipoUsuarioString;
    }

    public void setTipoUsuarioString(String tipoUsuarioString) {
        this.tipoUsuarioString = tipoUsuarioString;
    }
    
    public int getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(int idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public int getIdConvenio() {
        return idConvenio;
    }

    public void setIdConvenio(int idConvenio) {
        this.idConvenio = idConvenio;
    }
    
    
    
    public Usuario() {
    }

    public Usuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNumeroDeDocumento() {
        return numeroDeDocumento;
    }

    public void setNumeroDeDocumento(String numeroDeDocumento) {
        this.numeroDeDocumento = numeroDeDocumento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public BigInteger getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(BigInteger fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getSesionToken() {
        return sesionToken;
    }

    public void setSesionToken(String sesionToken) {
        this.sesionToken = sesionToken;
    }

    public Boolean getActivoAutored() {
        return activoAutored;
    }

    public void setActivoAutored(Boolean activoAutored) {
        this.activoAutored = activoAutored;
    }

    @XmlTransient
    public Collection<Factura> getFacturaCollection() {
        return facturaCollection;
    }

    public void setFacturaCollection(Collection<Factura> facturaCollection) {
        this.facturaCollection = facturaCollection;
    }

    @XmlTransient
    public Collection<Modulo> getModuloCollection() {
        return moduloCollection;
    }

    public void setModuloCollection(Collection<Modulo> moduloCollection) {
        this.moduloCollection = moduloCollection;
    }

    @XmlTransient
    public Collection<SubModulo> getSubModuloCollection() {
        return subModuloCollection;
    }

    public void setSubModuloCollection(Collection<SubModulo> subModuloCollection) {
        this.subModuloCollection = subModuloCollection;
    }

    @XmlTransient
    public Collection<Transaccion> getTransaccionCollection() {
        return transaccionCollection;
    }

    public void setTransaccionCollection(Collection<Transaccion> transaccionCollection) {
        this.transaccionCollection = transaccionCollection;
    }

    @XmlTransient
    public Collection<UsuarioHasUsuarioReferidosAutored> getUsuarioHasUsuarioReferidosAutoredCollection() {
        return usuarioHasUsuarioReferidosAutoredCollection;
    }

    public void setUsuarioHasUsuarioReferidosAutoredCollection(Collection<UsuarioHasUsuarioReferidosAutored> usuarioHasUsuarioReferidosAutoredCollection) {
        this.usuarioHasUsuarioReferidosAutoredCollection = usuarioHasUsuarioReferidosAutoredCollection;
    }

    @XmlTransient
    public Collection<UsuarioHasUsuarioReferidosAutored> getUsuarioHasUsuarioReferidosAutoredCollection1() {
        return usuarioHasUsuarioReferidosAutoredCollection1;
    }

    public void setUsuarioHasUsuarioReferidosAutoredCollection1(Collection<UsuarioHasUsuarioReferidosAutored> usuarioHasUsuarioReferidosAutoredCollection1) {
        this.usuarioHasUsuarioReferidosAutoredCollection1 = usuarioHasUsuarioReferidosAutoredCollection1;
    }

    public TipoUsuario getTipousuarioIDTipoUsuario() {
        return tipousuarioIDTipoUsuario;
    }

    public void setTipousuarioIDTipoUsuario(TipoUsuario tipousuarioIDTipoUsuario) {
        this.tipousuarioIDTipoUsuario = tipousuarioIDTipoUsuario;
    }

    public Convenios getConveniosIdconvenios() {
        return conveniosIdconvenios;
    }

    public void setConveniosIdconvenios(Convenios conveniosIdconvenios) {
        this.conveniosIdconvenios = conveniosIdconvenios;
    }

    @XmlTransient
    public Collection<Notificacion> getNotificacionCollection() {
        return notificacionCollection;
    }

    public void setNotificacionCollection(Collection<Notificacion> notificacionCollection) {
        this.notificacionCollection = notificacionCollection;
    }

    @XmlTransient
    public Collection<Dispositivo> getDispositivoCollection() {
        return dispositivoCollection;
    }

    public void setDispositivoCollection(Collection<Dispositivo> dispositivoCollection) {
        this.dispositivoCollection = dispositivoCollection;
    }

    @XmlTransient
    public Collection<LogIngreso> getLogIngresoCollection() {
        return logIngresoCollection;
    }

    public void setLogIngresoCollection(Collection<LogIngreso> logIngresoCollection) {
        this.logIngresoCollection = logIngresoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.Usuario[ idUsuario=" + idUsuario + " ]";
    }

    @XmlTransient
    public Collection<Codigo> getCodigoCollection() {
        return codigoCollection;
    }

    public void setCodigoCollection(Collection<Codigo> codigoCollection) {
        this.codigoCollection = codigoCollection;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }
    
}
