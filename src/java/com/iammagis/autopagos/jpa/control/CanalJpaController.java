/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.control;

import com.iammagis.autopagos.jpa.beans.Canal;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.iammagis.autopagos.jpa.beans.Convenios;
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
public class CanalJpaController implements Serializable {

    public CanalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Canal canal) {
        if (canal.getConveniosCollection() == null) {
            canal.setConveniosCollection(new ArrayList<Convenios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Convenios> attachedConveniosCollection = new ArrayList<Convenios>();
            for (Convenios conveniosCollectionConveniosToAttach : canal.getConveniosCollection()) {
                conveniosCollectionConveniosToAttach = em.getReference(conveniosCollectionConveniosToAttach.getClass(), conveniosCollectionConveniosToAttach.getIdconvenios());
                attachedConveniosCollection.add(conveniosCollectionConveniosToAttach);
            }
            canal.setConveniosCollection(attachedConveniosCollection);
            em.persist(canal);
            for (Convenios conveniosCollectionConvenios : canal.getConveniosCollection()) {
                Canal oldCanalIdcanalPlanPostOfConveniosCollectionConvenios = conveniosCollectionConvenios.getCanalIdcanalPlanPost();
                conveniosCollectionConvenios.setCanalIdcanalPlanPost(canal);
                conveniosCollectionConvenios = em.merge(conveniosCollectionConvenios);
                if (oldCanalIdcanalPlanPostOfConveniosCollectionConvenios != null) {
                    oldCanalIdcanalPlanPostOfConveniosCollectionConvenios.getConveniosCollection().remove(conveniosCollectionConvenios);
                    oldCanalIdcanalPlanPostOfConveniosCollectionConvenios = em.merge(oldCanalIdcanalPlanPostOfConveniosCollectionConvenios);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Canal canal) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Canal persistentCanal = em.find(Canal.class, canal.getIdcanal());
            Collection<Convenios> conveniosCollectionOld = persistentCanal.getConveniosCollection();
            Collection<Convenios> conveniosCollectionNew = canal.getConveniosCollection();
            Collection<Convenios> attachedConveniosCollectionNew = new ArrayList<Convenios>();
            for (Convenios conveniosCollectionNewConveniosToAttach : conveniosCollectionNew) {
                conveniosCollectionNewConveniosToAttach = em.getReference(conveniosCollectionNewConveniosToAttach.getClass(), conveniosCollectionNewConveniosToAttach.getIdconvenios());
                attachedConveniosCollectionNew.add(conveniosCollectionNewConveniosToAttach);
            }
            conveniosCollectionNew = attachedConveniosCollectionNew;
            canal.setConveniosCollection(conveniosCollectionNew);
            canal = em.merge(canal);
            for (Convenios conveniosCollectionOldConvenios : conveniosCollectionOld) {
                if (!conveniosCollectionNew.contains(conveniosCollectionOldConvenios)) {
                    conveniosCollectionOldConvenios.setCanalIdcanalPlanPost(null);
                    conveniosCollectionOldConvenios = em.merge(conveniosCollectionOldConvenios);
                }
            }
            for (Convenios conveniosCollectionNewConvenios : conveniosCollectionNew) {
                if (!conveniosCollectionOld.contains(conveniosCollectionNewConvenios)) {
                    Canal oldCanalIdcanalPlanPostOfConveniosCollectionNewConvenios = conveniosCollectionNewConvenios.getCanalIdcanalPlanPost();
                    conveniosCollectionNewConvenios.setCanalIdcanalPlanPost(canal);
                    conveniosCollectionNewConvenios = em.merge(conveniosCollectionNewConvenios);
                    if (oldCanalIdcanalPlanPostOfConveniosCollectionNewConvenios != null && !oldCanalIdcanalPlanPostOfConveniosCollectionNewConvenios.equals(canal)) {
                        oldCanalIdcanalPlanPostOfConveniosCollectionNewConvenios.getConveniosCollection().remove(conveniosCollectionNewConvenios);
                        oldCanalIdcanalPlanPostOfConveniosCollectionNewConvenios = em.merge(oldCanalIdcanalPlanPostOfConveniosCollectionNewConvenios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = canal.getIdcanal();
                if (findCanal(id) == null) {
                    throw new NonexistentEntityException("The canal with id " + id + " no longer exists.");
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
            Canal canal;
            try {
                canal = em.getReference(Canal.class, id);
                canal.getIdcanal();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The canal with id " + id + " no longer exists.", enfe);
            }
            Collection<Convenios> conveniosCollection = canal.getConveniosCollection();
            for (Convenios conveniosCollectionConvenios : conveniosCollection) {
                conveniosCollectionConvenios.setCanalIdcanalPlanPost(null);
                conveniosCollectionConvenios = em.merge(conveniosCollectionConvenios);
            }
            em.remove(canal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Canal> findCanalEntities() {
        return findCanalEntities(true, -1, -1);
    }

    public List<Canal> findCanalEntities(int maxResults, int firstResult) {
        return findCanalEntities(false, maxResults, firstResult);
    }

    private List<Canal> findCanalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Canal.class));
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

    public Canal findCanal(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Canal.class, id);
        } finally {
            em.close();
        }
    }

    public int getCanalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Canal> rt = cq.from(Canal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
