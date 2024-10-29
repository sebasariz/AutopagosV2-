/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.control;

import com.iammagis.autopagos.jpa.beans.ComprobanterecaudoPSE;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.iammagis.autopagos.jpa.beans.Estado;
import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.control.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Usuario
 */
public class ComprobanterecaudoPSEJpaController implements Serializable {

    public ComprobanterecaudoPSEJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public ComprobanterecaudoPSE create(ComprobanterecaudoPSE comprobanterecaudoPSE) {
        if (comprobanterecaudoPSE.getTransaccionCollection() == null) {
            comprobanterecaudoPSE.setTransaccionCollection(new ArrayList<Transaccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado estadoIdestado = comprobanterecaudoPSE.getEstadoIdestado();
            if (estadoIdestado != null) {
                estadoIdestado = em.getReference(estadoIdestado.getClass(), estadoIdestado.getIdestado());
                comprobanterecaudoPSE.setEstadoIdestado(estadoIdestado);
            }
            Collection<Transaccion> attachedTransaccionCollection = new ArrayList<Transaccion>();
            for (Transaccion transaccionCollectionTransaccionToAttach : comprobanterecaudoPSE.getTransaccionCollection()) {
                transaccionCollectionTransaccionToAttach = em.getReference(transaccionCollectionTransaccionToAttach.getClass(), transaccionCollectionTransaccionToAttach.getIdtransaccion());
                attachedTransaccionCollection.add(transaccionCollectionTransaccionToAttach);
            }
            comprobanterecaudoPSE.setTransaccionCollection(attachedTransaccionCollection);
            em.persist(comprobanterecaudoPSE);
            if (estadoIdestado != null) {
                estadoIdestado.getComprobanterecaudoPSECollection().add(comprobanterecaudoPSE);
                estadoIdestado = em.merge(estadoIdestado);
            }
            for (Transaccion transaccionCollectionTransaccion : comprobanterecaudoPSE.getTransaccionCollection()) {
                ComprobanterecaudoPSE oldComprobanteRecaudoPSEidcomprobanteRecaudoPSEOfTransaccionCollectionTransaccion = transaccionCollectionTransaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE();
                transaccionCollectionTransaccion.setComprobanteRecaudoPSEidcomprobanteRecaudoPSE(comprobanterecaudoPSE);
                transaccionCollectionTransaccion = em.merge(transaccionCollectionTransaccion);
                if (oldComprobanteRecaudoPSEidcomprobanteRecaudoPSEOfTransaccionCollectionTransaccion != null) {
                    oldComprobanteRecaudoPSEidcomprobanteRecaudoPSEOfTransaccionCollectionTransaccion.getTransaccionCollection().remove(transaccionCollectionTransaccion);
                    oldComprobanteRecaudoPSEidcomprobanteRecaudoPSEOfTransaccionCollectionTransaccion = em.merge(oldComprobanteRecaudoPSEidcomprobanteRecaudoPSEOfTransaccionCollectionTransaccion);
                }
            }
            em.getTransaction().commit();
            return comprobanterecaudoPSE;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public ComprobanterecaudoPSE edit(ComprobanterecaudoPSE comprobanterecaudoPSE) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ComprobanterecaudoPSE persistentComprobanterecaudoPSE = em.find(ComprobanterecaudoPSE.class, comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE());
            Estado estadoIdestadoOld = persistentComprobanterecaudoPSE.getEstadoIdestado();
            Estado estadoIdestadoNew = comprobanterecaudoPSE.getEstadoIdestado();
            Collection<Transaccion> transaccionCollectionOld = persistentComprobanterecaudoPSE.getTransaccionCollection();
            Collection<Transaccion> transaccionCollectionNew = comprobanterecaudoPSE.getTransaccionCollection();
            if (estadoIdestadoNew != null) {
                estadoIdestadoNew = em.getReference(estadoIdestadoNew.getClass(), estadoIdestadoNew.getIdestado());
                comprobanterecaudoPSE.setEstadoIdestado(estadoIdestadoNew);
            }
            Collection<Transaccion> attachedTransaccionCollectionNew = new ArrayList<Transaccion>();
            for (Transaccion transaccionCollectionNewTransaccionToAttach : transaccionCollectionNew) {
                transaccionCollectionNewTransaccionToAttach = em.getReference(transaccionCollectionNewTransaccionToAttach.getClass(), transaccionCollectionNewTransaccionToAttach.getIdtransaccion());
                attachedTransaccionCollectionNew.add(transaccionCollectionNewTransaccionToAttach);
            }
            transaccionCollectionNew = attachedTransaccionCollectionNew;
            comprobanterecaudoPSE.setTransaccionCollection(transaccionCollectionNew);
            comprobanterecaudoPSE = em.merge(comprobanterecaudoPSE);
            if (estadoIdestadoOld != null && !estadoIdestadoOld.equals(estadoIdestadoNew)) {
                estadoIdestadoOld.getComprobanterecaudoPSECollection().remove(comprobanterecaudoPSE);
                estadoIdestadoOld = em.merge(estadoIdestadoOld);
            }
            if (estadoIdestadoNew != null && !estadoIdestadoNew.equals(estadoIdestadoOld)) {
                estadoIdestadoNew.getComprobanterecaudoPSECollection().add(comprobanterecaudoPSE);
                estadoIdestadoNew = em.merge(estadoIdestadoNew);
            }
            for (Transaccion transaccionCollectionOldTransaccion : transaccionCollectionOld) {
                if (!transaccionCollectionNew.contains(transaccionCollectionOldTransaccion)) {
                    transaccionCollectionOldTransaccion.setComprobanteRecaudoPSEidcomprobanteRecaudoPSE(null);
                    transaccionCollectionOldTransaccion = em.merge(transaccionCollectionOldTransaccion);
                }
            }
            for (Transaccion transaccionCollectionNewTransaccion : transaccionCollectionNew) {
                if (!transaccionCollectionOld.contains(transaccionCollectionNewTransaccion)) {
                    ComprobanterecaudoPSE oldComprobanteRecaudoPSEidcomprobanteRecaudoPSEOfTransaccionCollectionNewTransaccion = transaccionCollectionNewTransaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE();
                    transaccionCollectionNewTransaccion.setComprobanteRecaudoPSEidcomprobanteRecaudoPSE(comprobanterecaudoPSE);
                    transaccionCollectionNewTransaccion = em.merge(transaccionCollectionNewTransaccion);
                    if (oldComprobanteRecaudoPSEidcomprobanteRecaudoPSEOfTransaccionCollectionNewTransaccion != null && !oldComprobanteRecaudoPSEidcomprobanteRecaudoPSEOfTransaccionCollectionNewTransaccion.equals(comprobanterecaudoPSE)) {
                        oldComprobanteRecaudoPSEidcomprobanteRecaudoPSEOfTransaccionCollectionNewTransaccion.getTransaccionCollection().remove(transaccionCollectionNewTransaccion);
                        oldComprobanteRecaudoPSEidcomprobanteRecaudoPSEOfTransaccionCollectionNewTransaccion = em.merge(oldComprobanteRecaudoPSEidcomprobanteRecaudoPSEOfTransaccionCollectionNewTransaccion);
                    }
                }
            }
            em.getTransaction().commit();
            return comprobanterecaudoPSE;
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE();
                if (findComprobanterecaudoPSE(id) == null) {
                    throw new NonexistentEntityException("The comprobanterecaudoPSE with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ComprobanterecaudoPSE comprobanterecaudoPSE;
            try {
                comprobanterecaudoPSE = em.getReference(ComprobanterecaudoPSE.class, id);
                comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comprobanterecaudoPSE with id " + id + " no longer exists.", enfe);
            }
            Estado estadoIdestado = comprobanterecaudoPSE.getEstadoIdestado();
            if (estadoIdestado != null) {
                estadoIdestado.getComprobanterecaudoPSECollection().remove(comprobanterecaudoPSE);
                estadoIdestado = em.merge(estadoIdestado);
            }
            Collection<Transaccion> transaccionCollection = comprobanterecaudoPSE.getTransaccionCollection();
            for (Transaccion transaccionCollectionTransaccion : transaccionCollection) {
                transaccionCollectionTransaccion.setComprobanteRecaudoPSEidcomprobanteRecaudoPSE(null);
                transaccionCollectionTransaccion = em.merge(transaccionCollectionTransaccion);
            }
            em.remove(comprobanterecaudoPSE);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ComprobanterecaudoPSE> findComprobanterecaudoPSEEntities() {
        return findComprobanterecaudoPSEEntities(true, -1, -1);
    }

    public List<ComprobanterecaudoPSE> findComprobanterecaudoPSEEntities(int maxResults, int firstResult) {
        return findComprobanterecaudoPSEEntities(false, maxResults, firstResult);
    }

    private List<ComprobanterecaudoPSE> findComprobanterecaudoPSEEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ComprobanterecaudoPSE.class));
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

    public ComprobanterecaudoPSE findComprobanterecaudoPSE(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ComprobanterecaudoPSE.class, id);
        } finally {
            em.close();
        }
    }

    public int getComprobanterecaudoPSECount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ComprobanterecaudoPSE> rt = cq.from(ComprobanterecaudoPSE.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public ArrayList<ComprobanterecaudoPSE> getComprobantesPendientesOnline() {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `comprobante_recaudo_PSE`,`transaccion`,`factura`,`convenios` WHERE `comprobante_recaudo_PSE`.`estado_idestado` = 4 AND `idcomprobanteRecaudoPSE` = `comprobanteRecaudoPSE_idcomprobanteRecaudoPSE` AND `factura_idfactura` = `idfactura` AND `factura`.`convenios_idconvenios` = `idconvenios` AND `tipo_convenio` = 2 AND `tipoTransaccion_idtipoTransaccion` = 6";
        Query query = em.createNativeQuery(queryString, ComprobanterecaudoPSE.class);
        ArrayList<ComprobanterecaudoPSE> result = new ArrayList<ComprobanterecaudoPSE>(query.getResultList());
        return result;
    }

    public ArrayList<ComprobanterecaudoPSE> getComprobantesPendientesSipar() {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `comprobante_recaudo_PSE`,`transaccion`,`factura`,`convenios` WHERE `comprobante_recaudo_PSE`.`estado_idestado` = 4 AND `idcomprobanteRecaudoPSE` = `comprobanteRecaudoPSE_idcomprobanteRecaudoPSE` AND `factura_idfactura` = `idfactura` AND `factura`.`convenios_idconvenios` = `idconvenios` AND `tipo_convenio` = 1 AND `tipoTransaccion_idtipoTransaccion` = 6";
        Query query = em.createNativeQuery(queryString, ComprobanterecaudoPSE.class);
        ArrayList<ComprobanterecaudoPSE> result = new ArrayList<ComprobanterecaudoPSE>(query.getResultList());
        return result;
    }

    public ArrayList<ComprobanterecaudoPSE> getComprobantesAutopagosPendintes() {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `comprobante_recaudo_PSE`,`transaccion` WHERE `comprobante_recaudo_PSE`.`estado_idestado` = 4 AND `idcomprobanteRecaudoPSE` = `transaccion`.`comprobanteRecaudoPSE_idcomprobanteRecaudoPSE` AND `factura_autopagos_idfactura_autopagos` IS NOT NULL";
        Query query = em.createNativeQuery(queryString, ComprobanterecaudoPSE.class);
        ArrayList<ComprobanterecaudoPSE> result = new ArrayList<ComprobanterecaudoPSE>(query.getResultList());
        return result;
    }

    public ComprobanterecaudoPSE findByIdAvvillas(String numeroTransaccion) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `comprobante_recaudo_PSE` WHERE `id_avvillas` = " + numeroTransaccion;
        Query query = em.createNativeQuery(queryString, ComprobanterecaudoPSE.class);
        ComprobanterecaudoPSE result = (ComprobanterecaudoPSE) query.getSingleResult();
        return result;
    }

    

}
