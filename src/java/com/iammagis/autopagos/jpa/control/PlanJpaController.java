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
import com.iammagis.autopagos.jpa.beans.Plan;
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
public class PlanJpaController implements Serializable {

    public PlanJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Plan plan) {
        if (plan.getConveniosCollection() == null) {
            plan.setConveniosCollection(new ArrayList<Convenios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Convenios> attachedConveniosCollection = new ArrayList<Convenios>();
            for (Convenios conveniosCollectionConveniosToAttach : plan.getConveniosCollection()) {
                conveniosCollectionConveniosToAttach = em.getReference(conveniosCollectionConveniosToAttach.getClass(), conveniosCollectionConveniosToAttach.getIdconvenios());
                attachedConveniosCollection.add(conveniosCollectionConveniosToAttach);
            }
            plan.setConveniosCollection(attachedConveniosCollection);
            em.persist(plan);
            for (Convenios conveniosCollectionConvenios : plan.getConveniosCollection()) {
                Plan oldPlanIdplanOfConveniosCollectionConvenios = conveniosCollectionConvenios.getPlanIdplan();
                conveniosCollectionConvenios.setPlanIdplan(plan);
                conveniosCollectionConvenios = em.merge(conveniosCollectionConvenios);
                if (oldPlanIdplanOfConveniosCollectionConvenios != null) {
                    oldPlanIdplanOfConveniosCollectionConvenios.getConveniosCollection().remove(conveniosCollectionConvenios);
                    oldPlanIdplanOfConveniosCollectionConvenios = em.merge(oldPlanIdplanOfConveniosCollectionConvenios);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Plan plan) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Plan persistentPlan = em.find(Plan.class, plan.getIdplan());
            Collection<Convenios> conveniosCollectionOld = persistentPlan.getConveniosCollection();
            Collection<Convenios> conveniosCollectionNew = plan.getConveniosCollection();
            Collection<Convenios> attachedConveniosCollectionNew = new ArrayList<Convenios>();
            for (Convenios conveniosCollectionNewConveniosToAttach : conveniosCollectionNew) {
                conveniosCollectionNewConveniosToAttach = em.getReference(conveniosCollectionNewConveniosToAttach.getClass(), conveniosCollectionNewConveniosToAttach.getIdconvenios());
                attachedConveniosCollectionNew.add(conveniosCollectionNewConveniosToAttach);
            }
            conveniosCollectionNew = attachedConveniosCollectionNew;
            plan.setConveniosCollection(conveniosCollectionNew);
            plan = em.merge(plan);
            for (Convenios conveniosCollectionOldConvenios : conveniosCollectionOld) {
                if (!conveniosCollectionNew.contains(conveniosCollectionOldConvenios)) {
                    conveniosCollectionOldConvenios.setPlanIdplan(null);
                    conveniosCollectionOldConvenios = em.merge(conveniosCollectionOldConvenios);
                }
            }
            for (Convenios conveniosCollectionNewConvenios : conveniosCollectionNew) {
                if (!conveniosCollectionOld.contains(conveniosCollectionNewConvenios)) {
                    Plan oldPlanIdplanOfConveniosCollectionNewConvenios = conveniosCollectionNewConvenios.getPlanIdplan();
                    conveniosCollectionNewConvenios.setPlanIdplan(plan);
                    conveniosCollectionNewConvenios = em.merge(conveniosCollectionNewConvenios);
                    if (oldPlanIdplanOfConveniosCollectionNewConvenios != null && !oldPlanIdplanOfConveniosCollectionNewConvenios.equals(plan)) {
                        oldPlanIdplanOfConveniosCollectionNewConvenios.getConveniosCollection().remove(conveniosCollectionNewConvenios);
                        oldPlanIdplanOfConveniosCollectionNewConvenios = em.merge(oldPlanIdplanOfConveniosCollectionNewConvenios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = plan.getIdplan();
                if (findPlan(id) == null) {
                    throw new NonexistentEntityException("The plan with id " + id + " no longer exists.");
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
            Plan plan;
            try {
                plan = em.getReference(Plan.class, id);
                plan.getIdplan();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The plan with id " + id + " no longer exists.", enfe);
            }
            Collection<Convenios> conveniosCollection = plan.getConveniosCollection();
            for (Convenios conveniosCollectionConvenios : conveniosCollection) {
                conveniosCollectionConvenios.setPlanIdplan(null);
                conveniosCollectionConvenios = em.merge(conveniosCollectionConvenios);
            }
            em.remove(plan);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Plan> findPlanEntities() {
        return findPlanEntities(true, -1, -1);
    }

    public List<Plan> findPlanEntities(int maxResults, int firstResult) {
        return findPlanEntities(false, maxResults, firstResult);
    }

    private List<Plan> findPlanEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Plan.class));
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

    public Plan findPlan(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Plan.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlanCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Plan> rt = cq.from(Plan.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
