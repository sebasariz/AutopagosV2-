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
import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.TipoCuenta;
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
public class TipoCuentaJpaController implements Serializable {

    public TipoCuentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoCuenta tipoCuenta) {
        if (tipoCuenta.getConveniosCollection() == null) {
            tipoCuenta.setConveniosCollection(new ArrayList<Convenios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Convenios> attachedConveniosCollection = new ArrayList<Convenios>();
            for (Convenios conveniosCollectionConveniosToAttach : tipoCuenta.getConveniosCollection()) {
                conveniosCollectionConveniosToAttach = em.getReference(conveniosCollectionConveniosToAttach.getClass(), conveniosCollectionConveniosToAttach.getIdconvenios());
                attachedConveniosCollection.add(conveniosCollectionConveniosToAttach);
            }
            tipoCuenta.setConveniosCollection(attachedConveniosCollection);
            em.persist(tipoCuenta);
            for (Convenios conveniosCollectionConvenios : tipoCuenta.getConveniosCollection()) {
                TipoCuenta oldTipoCuentaidtipoCuentaOfConveniosCollectionConvenios = conveniosCollectionConvenios.getTipoCuentaidtipoCuenta();
                conveniosCollectionConvenios.setTipoCuentaidtipoCuenta(tipoCuenta);
                conveniosCollectionConvenios = em.merge(conveniosCollectionConvenios);
                if (oldTipoCuentaidtipoCuentaOfConveniosCollectionConvenios != null) {
                    oldTipoCuentaidtipoCuentaOfConveniosCollectionConvenios.getConveniosCollection().remove(conveniosCollectionConvenios);
                    oldTipoCuentaidtipoCuentaOfConveniosCollectionConvenios = em.merge(oldTipoCuentaidtipoCuentaOfConveniosCollectionConvenios);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoCuenta tipoCuenta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoCuenta persistentTipoCuenta = em.find(TipoCuenta.class, tipoCuenta.getIdtipoCuenta());
            Collection<Convenios> conveniosCollectionOld = persistentTipoCuenta.getConveniosCollection();
            Collection<Convenios> conveniosCollectionNew = tipoCuenta.getConveniosCollection();
            Collection<Convenios> attachedConveniosCollectionNew = new ArrayList<Convenios>();
            for (Convenios conveniosCollectionNewConveniosToAttach : conveniosCollectionNew) {
                conveniosCollectionNewConveniosToAttach = em.getReference(conveniosCollectionNewConveniosToAttach.getClass(), conveniosCollectionNewConveniosToAttach.getIdconvenios());
                attachedConveniosCollectionNew.add(conveniosCollectionNewConveniosToAttach);
            }
            conveniosCollectionNew = attachedConveniosCollectionNew;
            tipoCuenta.setConveniosCollection(conveniosCollectionNew);
            tipoCuenta = em.merge(tipoCuenta);
            for (Convenios conveniosCollectionOldConvenios : conveniosCollectionOld) {
                if (!conveniosCollectionNew.contains(conveniosCollectionOldConvenios)) {
                    conveniosCollectionOldConvenios.setTipoCuentaidtipoCuenta(null);
                    conveniosCollectionOldConvenios = em.merge(conveniosCollectionOldConvenios);
                }
            }
            for (Convenios conveniosCollectionNewConvenios : conveniosCollectionNew) {
                if (!conveniosCollectionOld.contains(conveniosCollectionNewConvenios)) {
                    TipoCuenta oldTipoCuentaidtipoCuentaOfConveniosCollectionNewConvenios = conveniosCollectionNewConvenios.getTipoCuentaidtipoCuenta();
                    conveniosCollectionNewConvenios.setTipoCuentaidtipoCuenta(tipoCuenta);
                    conveniosCollectionNewConvenios = em.merge(conveniosCollectionNewConvenios);
                    if (oldTipoCuentaidtipoCuentaOfConveniosCollectionNewConvenios != null && !oldTipoCuentaidtipoCuentaOfConveniosCollectionNewConvenios.equals(tipoCuenta)) {
                        oldTipoCuentaidtipoCuentaOfConveniosCollectionNewConvenios.getConveniosCollection().remove(conveniosCollectionNewConvenios);
                        oldTipoCuentaidtipoCuentaOfConveniosCollectionNewConvenios = em.merge(oldTipoCuentaidtipoCuentaOfConveniosCollectionNewConvenios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoCuenta.getIdtipoCuenta();
                if (findTipoCuenta(id) == null) {
                    throw new NonexistentEntityException("The tipoCuenta with id " + id + " no longer exists.");
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
            TipoCuenta tipoCuenta;
            try {
                tipoCuenta = em.getReference(TipoCuenta.class, id);
                tipoCuenta.getIdtipoCuenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoCuenta with id " + id + " no longer exists.", enfe);
            }
            Collection<Convenios> conveniosCollection = tipoCuenta.getConveniosCollection();
            for (Convenios conveniosCollectionConvenios : conveniosCollection) {
                conveniosCollectionConvenios.setTipoCuentaidtipoCuenta(null);
                conveniosCollectionConvenios = em.merge(conveniosCollectionConvenios);
            }
            em.remove(tipoCuenta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoCuenta> findTipoCuentaEntities() {
        return findTipoCuentaEntities(true, -1, -1);
    }

    public List<TipoCuenta> findTipoCuentaEntities(int maxResults, int firstResult) {
        return findTipoCuentaEntities(false, maxResults, firstResult);
    }

    private List<TipoCuenta> findTipoCuentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoCuenta.class));
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

    public TipoCuenta findTipoCuenta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoCuenta.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoCuentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoCuenta> rt = cq.from(TipoCuenta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
