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
@Table(name = "canal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Canal.findAll", query = "SELECT c FROM Canal c"),
    @NamedQuery(name = "Canal.findByIdcanal", query = "SELECT c FROM Canal c WHERE c.idcanal = :idcanal"),
    @NamedQuery(name = "Canal.findByNombre", query = "SELECT c FROM Canal c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Canal.findByRutaIndex", query = "SELECT c FROM Canal c WHERE c.rutaIndex = :rutaIndex"),
    @NamedQuery(name = "Canal.findByRutaLogin", query = "SELECT c FROM Canal c WHERE c.rutaLogin = :rutaLogin"),
    @NamedQuery(name = "Canal.findByRutaformulario", query = "SELECT c FROM Canal c WHERE c.rutaformulario = :rutaformulario"),
    @NamedQuery(name = "Canal.findByDiasActualizarClave", query = "SELECT c FROM Canal c WHERE c.diasActualizarClave = :diasActualizarClave")})
public class Canal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcanal")
    private Integer idcanal;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "rutaIndex")
    private String rutaIndex;
    @Column(name = "rutaLogin")
    private String rutaLogin;
    @Column(name = "rutaformulario")
    private String rutaformulario;
    @Column(name = "dias_actualizar_clave")
    private Integer diasActualizarClave;
    @OneToMany(mappedBy = "canalIdcanalPlanPost")
    private Collection<Convenios> conveniosCollection;

    public Canal() {
    }

    public Canal(Integer idcanal) {
        this.idcanal = idcanal;
    }

    public Integer getIdcanal() {
        return idcanal;
    }

    public void setIdcanal(Integer idcanal) {
        this.idcanal = idcanal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRutaIndex() {
        return rutaIndex;
    }

    public void setRutaIndex(String rutaIndex) {
        this.rutaIndex = rutaIndex;
    }

    public String getRutaLogin() {
        return rutaLogin;
    }

    public void setRutaLogin(String rutaLogin) {
        this.rutaLogin = rutaLogin;
    }

    public String getRutaformulario() {
        return rutaformulario;
    }

    public void setRutaformulario(String rutaformulario) {
        this.rutaformulario = rutaformulario;
    }

    public Integer getDiasActualizarClave() {
        return diasActualizarClave;
    }

    public void setDiasActualizarClave(Integer diasActualizarClave) {
        this.diasActualizarClave = diasActualizarClave;
    }

    @XmlTransient
    public Collection<Convenios> getConveniosCollection() {
        return conveniosCollection;
    }

    public void setConveniosCollection(Collection<Convenios> conveniosCollection) {
        this.conveniosCollection = conveniosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcanal != null ? idcanal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Canal)) {
            return false;
        }
        Canal other = (Canal) object;
        if ((this.idcanal == null && other.idcanal != null) || (this.idcanal != null && !this.idcanal.equals(other.idcanal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.Canal[ idcanal=" + idcanal + " ]";
    }
    
}
