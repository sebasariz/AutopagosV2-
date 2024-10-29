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
@Table(name = "plan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Plan.findAll", query = "SELECT p FROM Plan p"),
    @NamedQuery(name = "Plan.findByIdplan", query = "SELECT p FROM Plan p WHERE p.idplan = :idplan"),
    @NamedQuery(name = "Plan.findByCargoBasico", query = "SELECT p FROM Plan p WHERE p.cargoBasico = :cargoBasico"),
    @NamedQuery(name = "Plan.findByNumeroTransacciones", query = "SELECT p FROM Plan p WHERE p.numeroTransacciones = :numeroTransacciones"),
    @NamedQuery(name = "Plan.findByValorExtraTransaccion", query = "SELECT p FROM Plan p WHERE p.valorExtraTransaccion = :valorExtraTransaccion"),
    @NamedQuery(name = "Plan.findByNombre", query = "SELECT p FROM Plan p WHERE p.nombre = :nombre")})
public class Plan implements Serializable {
    @OneToMany(mappedBy = "planIdplan")
    private Collection<FacturaAutopagos> facturaAutopagosCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idplan")
    private Integer idplan;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cargo_basico")
    private Double cargoBasico;
    @Column(name = "numero_transacciones")
    private Integer numeroTransacciones;
    @Column(name = "valor_extra_transaccion")
    private Double valorExtraTransaccion;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "planIdplan")
    private Collection<Convenios> conveniosCollection;

    public Plan() {
    }

    public Plan(Integer idplan) {
        this.idplan = idplan;
    }

    public Integer getIdplan() {
        return idplan;
    }

    public void setIdplan(Integer idplan) {
        this.idplan = idplan;
    }

    public Double getCargoBasico() {
        return cargoBasico;
    }

    public void setCargoBasico(Double cargoBasico) {
        this.cargoBasico = cargoBasico;
    }

    public Integer getNumeroTransacciones() {
        return numeroTransacciones;
    }

    public void setNumeroTransacciones(Integer numeroTransacciones) {
        this.numeroTransacciones = numeroTransacciones;
    }

    public Double getValorExtraTransaccion() {
        return valorExtraTransaccion;
    }

    public void setValorExtraTransaccion(Double valorExtraTransaccion) {
        this.valorExtraTransaccion = valorExtraTransaccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        hash += (idplan != null ? idplan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Plan)) {
            return false;
        }
        Plan other = (Plan) object;
        if ((this.idplan == null && other.idplan != null) || (this.idplan != null && !this.idplan.equals(other.idplan))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.iammagis.autopagos.jpa.beans.Plan[ idplan=" + idplan + " ]";
    }

    @XmlTransient
    public Collection<FacturaAutopagos> getFacturaAutopagosCollection() {
        return facturaAutopagosCollection;
    }

    public void setFacturaAutopagosCollection(Collection<FacturaAutopagos> facturaAutopagosCollection) {
        this.facturaAutopagosCollection = facturaAutopagosCollection;
    }
    
}
