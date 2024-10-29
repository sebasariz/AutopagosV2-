/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.control;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.iammagis.autopagos.jpa.beans.FacturaTemplate;
import com.iammagis.autopagos.jpa.beans.Estado;
import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.Usuario;
import java.util.ArrayList;
import java.util.Collection;
import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.control.exceptions.IllegalOrphanException;
import com.iammagis.autopagos.jpa.control.exceptions.NonexistentEntityException;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 *
 * @author Usuario
 */
public class FacturaJpaController implements Serializable {

    public FacturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Factura create(Factura factura) {
        if (factura.getUsuarioCollection() == null) {
            factura.setUsuarioCollection(new ArrayList<Usuario>());
        }
        if (factura.getTransaccionCollection() == null) {
            factura.setTransaccionCollection(new ArrayList<Transaccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FacturaTemplate facturaTemplateidfacturaTemplate = factura.getFacturaTemplateidfacturaTemplate();
            if (facturaTemplateidfacturaTemplate != null) {
                facturaTemplateidfacturaTemplate = em.getReference(facturaTemplateidfacturaTemplate.getClass(), facturaTemplateidfacturaTemplate.getIdfacturaTemplate());
                factura.setFacturaTemplateidfacturaTemplate(facturaTemplateidfacturaTemplate);
            }
            Estado estadoIdestado = factura.getEstadoIdestado();
            if (estadoIdestado != null) {
                estadoIdestado = em.getReference(estadoIdestado.getClass(), estadoIdestado.getIdestado());
                factura.setEstadoIdestado(estadoIdestado);
            }
            Convenios conveniosIdconvenios = factura.getConveniosIdconvenios();
            if (conveniosIdconvenios != null) {
                conveniosIdconvenios = em.getReference(conveniosIdconvenios.getClass(), conveniosIdconvenios.getIdconvenios());
                factura.setConveniosIdconvenios(conveniosIdconvenios);
            }
            Collection<Usuario> attachedUsuarioCollection = new ArrayList<Usuario>();
            for (Usuario usuarioCollectionUsuarioToAttach : factura.getUsuarioCollection()) {
                usuarioCollectionUsuarioToAttach = em.getReference(usuarioCollectionUsuarioToAttach.getClass(), usuarioCollectionUsuarioToAttach.getIdUsuario());
                attachedUsuarioCollection.add(usuarioCollectionUsuarioToAttach);
            }
            factura.setUsuarioCollection(attachedUsuarioCollection);
            Collection<Transaccion> attachedTransaccionCollection = new ArrayList<Transaccion>();
            for (Transaccion transaccionCollectionTransaccionToAttach : factura.getTransaccionCollection()) {
                transaccionCollectionTransaccionToAttach = em.getReference(transaccionCollectionTransaccionToAttach.getClass(), transaccionCollectionTransaccionToAttach.getIdtransaccion());
                attachedTransaccionCollection.add(transaccionCollectionTransaccionToAttach);
            }
            factura.setTransaccionCollection(attachedTransaccionCollection);
            em.persist(factura);
            if (facturaTemplateidfacturaTemplate != null) {
                facturaTemplateidfacturaTemplate.getFacturaCollection().add(factura);
                facturaTemplateidfacturaTemplate = em.merge(facturaTemplateidfacturaTemplate);
            }
            if (estadoIdestado != null) {
                estadoIdestado.getFacturaCollection().add(factura);
                estadoIdestado = em.merge(estadoIdestado);
            }
            if (conveniosIdconvenios != null) {
                conveniosIdconvenios.getFacturaCollection().add(factura);
                conveniosIdconvenios = em.merge(conveniosIdconvenios);
            }
            for (Usuario usuarioCollectionUsuario : factura.getUsuarioCollection()) {
                usuarioCollectionUsuario.getFacturaCollection().add(factura);
                usuarioCollectionUsuario = em.merge(usuarioCollectionUsuario);
            }
            for (Transaccion transaccionCollectionTransaccion : factura.getTransaccionCollection()) {
                Factura oldFacturaIdfacturaOfTransaccionCollectionTransaccion = transaccionCollectionTransaccion.getFacturaIdfactura();
                transaccionCollectionTransaccion.setFacturaIdfactura(factura);
                transaccionCollectionTransaccion = em.merge(transaccionCollectionTransaccion);
                if (oldFacturaIdfacturaOfTransaccionCollectionTransaccion != null) {
                    oldFacturaIdfacturaOfTransaccionCollectionTransaccion.getTransaccionCollection().remove(transaccionCollectionTransaccion);
                    oldFacturaIdfacturaOfTransaccionCollectionTransaccion = em.merge(oldFacturaIdfacturaOfTransaccionCollectionTransaccion);
                }
            }
            em.getTransaction().commit();
            return factura;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Factura edit(Factura factura) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Factura persistentFactura = em.find(Factura.class, factura.getIdfactura());
            FacturaTemplate facturaTemplateidfacturaTemplateOld = persistentFactura.getFacturaTemplateidfacturaTemplate();
            FacturaTemplate facturaTemplateidfacturaTemplateNew = factura.getFacturaTemplateidfacturaTemplate();
            Estado estadoIdestadoOld = persistentFactura.getEstadoIdestado();
            Estado estadoIdestadoNew = factura.getEstadoIdestado();
            Convenios conveniosIdconveniosOld = persistentFactura.getConveniosIdconvenios();
            Convenios conveniosIdconveniosNew = factura.getConveniosIdconvenios();
            Collection<Usuario> usuarioCollectionOld = persistentFactura.getUsuarioCollection();
            Collection<Usuario> usuarioCollectionNew = factura.getUsuarioCollection();
            Collection<Transaccion> transaccionCollectionOld = persistentFactura.getTransaccionCollection();
            Collection<Transaccion> transaccionCollectionNew = factura.getTransaccionCollection();
            List<String> illegalOrphanMessages = null;
            for (Transaccion transaccionCollectionOldTransaccion : transaccionCollectionOld) {
                if (!transaccionCollectionNew.contains(transaccionCollectionOldTransaccion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Transaccion " + transaccionCollectionOldTransaccion + " since its facturaIdfactura field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (facturaTemplateidfacturaTemplateNew != null) {
                facturaTemplateidfacturaTemplateNew = em.getReference(facturaTemplateidfacturaTemplateNew.getClass(), facturaTemplateidfacturaTemplateNew.getIdfacturaTemplate());
                factura.setFacturaTemplateidfacturaTemplate(facturaTemplateidfacturaTemplateNew);
            }
            if (estadoIdestadoNew != null) {
                estadoIdestadoNew = em.getReference(estadoIdestadoNew.getClass(), estadoIdestadoNew.getIdestado());
                factura.setEstadoIdestado(estadoIdestadoNew);
            }
            if (conveniosIdconveniosNew != null) {
                conveniosIdconveniosNew = em.getReference(conveniosIdconveniosNew.getClass(), conveniosIdconveniosNew.getIdconvenios());
                factura.setConveniosIdconvenios(conveniosIdconveniosNew);
            }
            Collection<Usuario> attachedUsuarioCollectionNew = new ArrayList<Usuario>();
            for (Usuario usuarioCollectionNewUsuarioToAttach : usuarioCollectionNew) {
                usuarioCollectionNewUsuarioToAttach = em.getReference(usuarioCollectionNewUsuarioToAttach.getClass(), usuarioCollectionNewUsuarioToAttach.getIdUsuario());
                attachedUsuarioCollectionNew.add(usuarioCollectionNewUsuarioToAttach);
            }
            usuarioCollectionNew = attachedUsuarioCollectionNew;
            factura.setUsuarioCollection(usuarioCollectionNew);
            Collection<Transaccion> attachedTransaccionCollectionNew = new ArrayList<Transaccion>();
            for (Transaccion transaccionCollectionNewTransaccionToAttach : transaccionCollectionNew) {
                transaccionCollectionNewTransaccionToAttach = em.getReference(transaccionCollectionNewTransaccionToAttach.getClass(), transaccionCollectionNewTransaccionToAttach.getIdtransaccion());
                attachedTransaccionCollectionNew.add(transaccionCollectionNewTransaccionToAttach);
            }
            transaccionCollectionNew = attachedTransaccionCollectionNew;
            factura.setTransaccionCollection(transaccionCollectionNew);
            factura = em.merge(factura);
            if (facturaTemplateidfacturaTemplateOld != null && !facturaTemplateidfacturaTemplateOld.equals(facturaTemplateidfacturaTemplateNew)) {
                facturaTemplateidfacturaTemplateOld.getFacturaCollection().remove(factura);
                facturaTemplateidfacturaTemplateOld = em.merge(facturaTemplateidfacturaTemplateOld);
            }
            if (facturaTemplateidfacturaTemplateNew != null && !facturaTemplateidfacturaTemplateNew.equals(facturaTemplateidfacturaTemplateOld)) {
                facturaTemplateidfacturaTemplateNew.getFacturaCollection().add(factura);
                facturaTemplateidfacturaTemplateNew = em.merge(facturaTemplateidfacturaTemplateNew);
            }
            if (estadoIdestadoOld != null && !estadoIdestadoOld.equals(estadoIdestadoNew)) {
                estadoIdestadoOld.getFacturaCollection().remove(factura);
                estadoIdestadoOld = em.merge(estadoIdestadoOld);
            }
            if (estadoIdestadoNew != null && !estadoIdestadoNew.equals(estadoIdestadoOld)) {
                estadoIdestadoNew.getFacturaCollection().add(factura);
                estadoIdestadoNew = em.merge(estadoIdestadoNew);
            }
            if (conveniosIdconveniosOld != null && !conveniosIdconveniosOld.equals(conveniosIdconveniosNew)) {
                conveniosIdconveniosOld.getFacturaCollection().remove(factura);
                conveniosIdconveniosOld = em.merge(conveniosIdconveniosOld);
            }
            if (conveniosIdconveniosNew != null && !conveniosIdconveniosNew.equals(conveniosIdconveniosOld)) {
                conveniosIdconveniosNew.getFacturaCollection().add(factura);
                conveniosIdconveniosNew = em.merge(conveniosIdconveniosNew);
            }
            for (Usuario usuarioCollectionOldUsuario : usuarioCollectionOld) {
                if (!usuarioCollectionNew.contains(usuarioCollectionOldUsuario)) {
                    usuarioCollectionOldUsuario.getFacturaCollection().remove(factura);
                    usuarioCollectionOldUsuario = em.merge(usuarioCollectionOldUsuario);
                }
            }
            for (Usuario usuarioCollectionNewUsuario : usuarioCollectionNew) {
                if (!usuarioCollectionOld.contains(usuarioCollectionNewUsuario)) {
                    usuarioCollectionNewUsuario.getFacturaCollection().add(factura);
                    usuarioCollectionNewUsuario = em.merge(usuarioCollectionNewUsuario);
                }
            }
            for (Transaccion transaccionCollectionNewTransaccion : transaccionCollectionNew) {
                if (!transaccionCollectionOld.contains(transaccionCollectionNewTransaccion)) {
                    Factura oldFacturaIdfacturaOfTransaccionCollectionNewTransaccion = transaccionCollectionNewTransaccion.getFacturaIdfactura();
                    transaccionCollectionNewTransaccion.setFacturaIdfactura(factura);
                    transaccionCollectionNewTransaccion = em.merge(transaccionCollectionNewTransaccion);
                    if (oldFacturaIdfacturaOfTransaccionCollectionNewTransaccion != null && !oldFacturaIdfacturaOfTransaccionCollectionNewTransaccion.equals(factura)) {
                        oldFacturaIdfacturaOfTransaccionCollectionNewTransaccion.getTransaccionCollection().remove(transaccionCollectionNewTransaccion);
                        oldFacturaIdfacturaOfTransaccionCollectionNewTransaccion = em.merge(oldFacturaIdfacturaOfTransaccionCollectionNewTransaccion);
                    }
                }
            }
            em.getTransaction().commit();
            return factura;
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = factura.getIdfactura();
                if (findFactura(id) == null) {
                    throw new NonexistentEntityException("The factura with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Factura factura;
            try {
                factura = em.getReference(Factura.class, id);
                factura.getIdfactura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The factura with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Transaccion> transaccionCollectionOrphanCheck = factura.getTransaccionCollection();
            for (Transaccion transaccionCollectionOrphanCheckTransaccion : transaccionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Factura (" + factura + ") cannot be destroyed since the Transaccion " + transaccionCollectionOrphanCheckTransaccion + " in its transaccionCollection field has a non-nullable facturaIdfactura field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            FacturaTemplate facturaTemplateidfacturaTemplate = factura.getFacturaTemplateidfacturaTemplate();
            if (facturaTemplateidfacturaTemplate != null) {
                facturaTemplateidfacturaTemplate.getFacturaCollection().remove(factura);
                facturaTemplateidfacturaTemplate = em.merge(facturaTemplateidfacturaTemplate);
            }
            Estado estadoIdestado = factura.getEstadoIdestado();
            if (estadoIdestado != null) {
                estadoIdestado.getFacturaCollection().remove(factura);
                estadoIdestado = em.merge(estadoIdestado);
            }
            Convenios conveniosIdconvenios = factura.getConveniosIdconvenios();
            if (conveniosIdconvenios != null) {
                conveniosIdconvenios.getFacturaCollection().remove(factura);
                conveniosIdconvenios = em.merge(conveniosIdconvenios);
            }
            Collection<Usuario> usuarioCollection = factura.getUsuarioCollection();
            for (Usuario usuarioCollectionUsuario : usuarioCollection) {
                usuarioCollectionUsuario.getFacturaCollection().remove(factura);
                usuarioCollectionUsuario = em.merge(usuarioCollectionUsuario);
            }
            em.remove(factura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Factura> findFacturaEntities() {
        return findFacturaEntities(true, -1, -1);
    }

    public List<Factura> findFacturaEntities(int maxResults, int firstResult) {
        return findFacturaEntities(false, maxResults, firstResult);
    }

    private List<Factura> findFacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Factura.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Factura findFactura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Factura.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Factura> rt = cq.from(Factura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public ArrayList<Factura> getFacturasFromConvenio(int idConvenio) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `factura` WHERE `convenios_idconvenios` = " + idConvenio + "  ORDER BY idfactura DESC";
        Query query = em.createNativeQuery(queryString, Factura.class);
        ArrayList<Factura> facturas = new ArrayList<>(query.getResultList());
        return facturas;
    }
    

    public Factura getFacturaFromConvenioYReferencia(int idConvenio, String numero) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `factura` WHERE `referencia` = '" + numero + "' AND convenios_idconvenios = " + idConvenio + " AND (estado_idestado=1 OR estado_idestado=3) ORDER BY idfactura DESC";
//        System.out.println("queryString: " + queryString);
        Query query = em.createNativeQuery(queryString, Factura.class);
        ArrayList<Factura> facturas = new ArrayList<>(query.getResultList());
        if (!facturas.isEmpty()) {
            return facturas.get(0);
        } else {
            return null;
        }
    }

    public ArrayList<Factura> getFacturasFromConvenioNoPagadas(int idConvenio) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `factura` WHERE `convenios_idconvenios` = " + idConvenio + " AND (estado_idestado=1 OR estado_idestado=3 OR estado_idestado=4) ORDER BY idfactura DESC";
        Query query = em.createNativeQuery(queryString, Factura.class);
        ArrayList<Factura> facturas = new ArrayList<>(query.getResultList());
        return facturas;
    }

    public void destroyFacturasByConvenio(int idConvenio) {
        String queryString = "";
        queryString = "DELETE FROM `factura` WHERE `convenios_idconvenios` =" + idConvenio;
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Query query = em.createNativeQuery(queryString);
        query.executeUpdate();
        tx.commit();
    }

    public ArrayList<Factura> getfacturasSinPrimeraBusqueda(long fecha) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `factura` WHERE `codigo_fullcarga` IS NOT NULL AND `fechaEmision` IS NULL AND `fechaCreacion` <" + fecha;
        Query query = em.createNativeQuery(queryString, Factura.class);
        ArrayList<Factura> facturas = new ArrayList<>(query.getResultList());
        return facturas;
    }

    public ArrayList<Factura> getFacturasRenovacion(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.add(Calendar.DAY_OF_YEAR, -4);
//        System.out.println("cale: "+calendar.getTime()+" time: "+calendar.getTimeInMillis());
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `factura` WHERE `codigo_fullcarga` IS NOT NULL AND `fechaEmision` < " + timeInMillis;
//        System.out.println("queryString: "+queryString);
        Query query = em.createNativeQuery(queryString, Factura.class);
        ArrayList<Factura> facturas = new ArrayList<>(query.getResultList());
        return facturas;
    }

    public ArrayList<Factura> getFacturasAutored() {
        EntityManager em = getEntityManager();
        String queryString = "SELECT *  FROM `factura` WHERE `codigo_fullcarga` IS NOT NULL ORDER BY `idfactura` DESC";
        Query query = em.createNativeQuery(queryString, Factura.class);
        ArrayList<Factura> facturas = new ArrayList<>(query.getResultList());
        return facturas;
    }

}
