/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.control;

import com.iammagis.autopagos.jpa.beans.TipoTransaccion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.control.exceptions.IllegalOrphanException;
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
public class TipoTransaccionJpaController implements Serializable {

    public TipoTransaccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoTransaccion tipoTransaccion) {
        if (tipoTransaccion.getTransaccionCollection() == null) {
            tipoTransaccion.setTransaccionCollection(new ArrayList<Transaccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Transaccion> attachedTransaccionCollection = new ArrayList<Transaccion>();
            for (Transaccion transaccionCollectionTransaccionToAttach : tipoTransaccion.getTransaccionCollection()) {
                transaccionCollectionTransaccionToAttach = em.getReference(transaccionCollectionTransaccionToAttach.getClass(), transaccionCollectionTransaccionToAttach.getIdtransaccion());
                attachedTransaccionCollection.add(transaccionCollectionTransaccionToAttach);
            }
            tipoTransaccion.setTransaccionCollection(attachedTransaccionCollection);
            em.persist(tipoTransaccion);
            for (Transaccion transaccionCollectionTransaccion : tipoTransaccion.getTransaccionCollection()) {
                TipoTransaccion oldTipoTransaccionidtipoTransaccionOfTransaccionCollectionTransaccion = transaccionCollectionTransaccion.getTipoTransaccionidtipoTransaccion();
                transaccionCollectionTransaccion.setTipoTransaccionidtipoTransaccion(tipoTransaccion);
                transaccionCollectionTransaccion = em.merge(transaccionCollectionTransaccion);
                if (oldTipoTransaccionidtipoTransaccionOfTransaccionCollectionTransaccion != null) {
                    oldTipoTransaccionidtipoTransaccionOfTransaccionCollectionTransaccion.getTransaccionCollection().remove(transaccionCollectionTransaccion);
                    oldTipoTransaccionidtipoTransaccionOfTransaccionCollectionTransaccion = em.merge(oldTipoTransaccionidtipoTransaccionOfTransaccionCollectionTransaccion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoTransaccion tipoTransaccion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoTransaccion persistentTipoTransaccion = em.find(TipoTransaccion.class, tipoTransaccion.getIdtipoTransaccion());
            Collection<Transaccion> transaccionCollectionOld = persistentTipoTransaccion.getTransaccionCollection();
            Collection<Transaccion> transaccionCollectionNew = tipoTransaccion.getTransaccionCollection();
            List<String> illegalOrphanMessages = null;
            for (Transaccion transaccionCollectionOldTransaccion : transaccionCollectionOld) {
                if (!transaccionCollectionNew.contains(transaccionCollectionOldTransaccion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Transaccion " + transaccionCollectionOldTransaccion + " since its tipoTransaccionidtipoTransaccion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Transaccion> attachedTransaccionCollectionNew = new ArrayList<Transaccion>();
            for (Transaccion transaccionCollectionNewTransaccionToAttach : transaccionCollectionNew) {
                transaccionCollectionNewTransaccionToAttach = em.getReference(transaccionCollectionNewTransaccionToAttach.getClass(), transaccionCollectionNewTransaccionToAttach.getIdtransaccion());
                attachedTransaccionCollectionNew.add(transaccionCollectionNewTransaccionToAttach);
            }
            transaccionCollectionNew = attachedTransaccionCollectionNew;
            tipoTransaccion.setTransaccionCollection(transaccionCollectionNew);
            tipoTransaccion = em.merge(tipoTransaccion);
            for (Transaccion transaccionCollectionNewTransaccion : transaccionCollectionNew) {
                if (!transaccionCollectionOld.contains(transaccionCollectionNewTransaccion)) {
                    TipoTransaccion oldTipoTransaccionidtipoTransaccionOfTransaccionCollectionNewTransaccion = transaccionCollectionNewTransaccion.getTipoTransaccionidtipoTransaccion();
                    transaccionCollectionNewTransaccion.setTipoTransaccionidtipoTransaccion(tipoTransaccion);
                    transaccionCollectionNewTransaccion = em.merge(transaccionCollectionNewTransaccion);
                    if (oldTipoTransaccionidtipoTransaccionOfTransaccionCollectionNewTransaccion != null && !oldTipoTransaccionidtipoTransaccionOfTransaccionCollectionNewTransaccion.equals(tipoTransaccion)) {
                        oldTipoTransaccionidtipoTransaccionOfTransaccionCollectionNewTransaccion.getTransaccionCollection().remove(transaccionCollectionNewTransaccion);
                        oldTipoTransaccionidtipoTransaccionOfTransaccionCollectionNewTransaccion = em.merge(oldTipoTransaccionidtipoTransaccionOfTransaccionCollectionNewTransaccion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoTransaccion.getIdtipoTransaccion();
                if (findTipoTransaccion(id) == null) {
                    throw new NonexistentEntityException("The tipoTransaccion with id " + id + " no longer exists.");
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
            TipoTransaccion tipoTransaccion;
            try {
                tipoTransaccion = em.getReference(TipoTransaccion.class, id);
                tipoTransaccion.getIdtipoTransaccion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoTransaccion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Transaccion> transaccionCollectionOrphanCheck = tipoTransaccion.getTransaccionCollection();
            for (Transaccion transaccionCollectionOrphanCheckTransaccion : transaccionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoTransaccion (" + tipoTransaccion + ") cannot be destroyed since the Transaccion " + transaccionCollectionOrphanCheckTransaccion + " in its transaccionCollection field has a non-nullable tipoTransaccionidtipoTransaccion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoTransaccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoTransaccion> findTipoTransaccionEntities() {
        return findTipoTransaccionEntities(true, -1, -1);
    }

    public List<TipoTransaccion> findTipoTransaccionEntities(int maxResults, int firstResult) {
        return findTipoTransaccionEntities(false, maxResults, firstResult);
    }

    private List<TipoTransaccion> findTipoTransaccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoTransaccion.class));
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

    public TipoTransaccion findTipoTransaccion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoTransaccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoTransaccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoTransaccion> rt = cq.from(TipoTransaccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
