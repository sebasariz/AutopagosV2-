/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.control;

import com.iammagis.autopagos.jpa.beans.Clase;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.iammagis.autopagos.jpa.beans.Convenios;
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
public class ClaseJpaController implements Serializable {

    public ClaseJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clase clase) {
        if (clase.getConveniosCollection() == null) {
            clase.setConveniosCollection(new ArrayList<Convenios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Convenios> attachedConveniosCollection = new ArrayList<Convenios>();
            for (Convenios conveniosCollectionConveniosToAttach : clase.getConveniosCollection()) {
                conveniosCollectionConveniosToAttach = em.getReference(conveniosCollectionConveniosToAttach.getClass(), conveniosCollectionConveniosToAttach.getIdconvenios());
                attachedConveniosCollection.add(conveniosCollectionConveniosToAttach);
            }
            clase.setConveniosCollection(attachedConveniosCollection);
            em.persist(clase);
            for (Convenios conveniosCollectionConvenios : clase.getConveniosCollection()) {
                Clase oldClaseIdclaseOfConveniosCollectionConvenios = conveniosCollectionConvenios.getClaseIdclase();
                conveniosCollectionConvenios.setClaseIdclase(clase);
                conveniosCollectionConvenios = em.merge(conveniosCollectionConvenios);
                if (oldClaseIdclaseOfConveniosCollectionConvenios != null) {
                    oldClaseIdclaseOfConveniosCollectionConvenios.getConveniosCollection().remove(conveniosCollectionConvenios);
                    oldClaseIdclaseOfConveniosCollectionConvenios = em.merge(oldClaseIdclaseOfConveniosCollectionConvenios);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clase clase) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clase persistentClase = em.find(Clase.class, clase.getIdclase());
            Collection<Convenios> conveniosCollectionOld = persistentClase.getConveniosCollection();
            Collection<Convenios> conveniosCollectionNew = clase.getConveniosCollection();
            List<String> illegalOrphanMessages = null;
            for (Convenios conveniosCollectionOldConvenios : conveniosCollectionOld) {
                if (!conveniosCollectionNew.contains(conveniosCollectionOldConvenios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Convenios " + conveniosCollectionOldConvenios + " since its claseIdclase field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Convenios> attachedConveniosCollectionNew = new ArrayList<Convenios>();
            for (Convenios conveniosCollectionNewConveniosToAttach : conveniosCollectionNew) {
                conveniosCollectionNewConveniosToAttach = em.getReference(conveniosCollectionNewConveniosToAttach.getClass(), conveniosCollectionNewConveniosToAttach.getIdconvenios());
                attachedConveniosCollectionNew.add(conveniosCollectionNewConveniosToAttach);
            }
            conveniosCollectionNew = attachedConveniosCollectionNew;
            clase.setConveniosCollection(conveniosCollectionNew);
            clase = em.merge(clase);
            for (Convenios conveniosCollectionNewConvenios : conveniosCollectionNew) {
                if (!conveniosCollectionOld.contains(conveniosCollectionNewConvenios)) {
                    Clase oldClaseIdclaseOfConveniosCollectionNewConvenios = conveniosCollectionNewConvenios.getClaseIdclase();
                    conveniosCollectionNewConvenios.setClaseIdclase(clase);
                    conveniosCollectionNewConvenios = em.merge(conveniosCollectionNewConvenios);
                    if (oldClaseIdclaseOfConveniosCollectionNewConvenios != null && !oldClaseIdclaseOfConveniosCollectionNewConvenios.equals(clase)) {
                        oldClaseIdclaseOfConveniosCollectionNewConvenios.getConveniosCollection().remove(conveniosCollectionNewConvenios);
                        oldClaseIdclaseOfConveniosCollectionNewConvenios = em.merge(oldClaseIdclaseOfConveniosCollectionNewConvenios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clase.getIdclase();
                if (findClase(id) == null) {
                    throw new NonexistentEntityException("The clase with id " + id + " no longer exists.");
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
            Clase clase;
            try {
                clase = em.getReference(Clase.class, id);
                clase.getIdclase();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clase with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Convenios> conveniosCollectionOrphanCheck = clase.getConveniosCollection();
            for (Convenios conveniosCollectionOrphanCheckConvenios : conveniosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clase (" + clase + ") cannot be destroyed since the Convenios " + conveniosCollectionOrphanCheckConvenios + " in its conveniosCollection field has a non-nullable claseIdclase field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(clase);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clase> findClaseEntities() {
        return findClaseEntities(true, -1, -1);
    }

    public List<Clase> findClaseEntities(int maxResults, int firstResult) {
        return findClaseEntities(false, maxResults, firstResult);
    }

    private List<Clase> findClaseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clase.class));
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

    public Clase findClase(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clase.class, id);
        } finally {
            em.close();
        }
    }

    public int getClaseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clase> rt = cq.from(Clase.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
