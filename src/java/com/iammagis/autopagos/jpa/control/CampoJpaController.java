/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.control;

import com.iammagis.autopagos.jpa.beans.Campo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.iammagis.autopagos.jpa.beans.FacturaTemplate;
import com.iammagis.autopagos.jpa.control.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Usuario
 */
public class CampoJpaController implements Serializable {

    public CampoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Campo campo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FacturaTemplate facturaTemplateidfacturaTemplate = campo.getFacturaTemplateidfacturaTemplate();
            if (facturaTemplateidfacturaTemplate != null) {
                facturaTemplateidfacturaTemplate = em.getReference(facturaTemplateidfacturaTemplate.getClass(), facturaTemplateidfacturaTemplate.getIdfacturaTemplate());
                campo.setFacturaTemplateidfacturaTemplate(facturaTemplateidfacturaTemplate);
            }
            em.persist(campo);
            if (facturaTemplateidfacturaTemplate != null) {
                facturaTemplateidfacturaTemplate.getCampoCollection().add(campo);
                facturaTemplateidfacturaTemplate = em.merge(facturaTemplateidfacturaTemplate);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Campo campo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Campo persistentCampo = em.find(Campo.class, campo.getIdcampo());
            FacturaTemplate facturaTemplateidfacturaTemplateOld = persistentCampo.getFacturaTemplateidfacturaTemplate();
            FacturaTemplate facturaTemplateidfacturaTemplateNew = campo.getFacturaTemplateidfacturaTemplate();
            if (facturaTemplateidfacturaTemplateNew != null) {
                facturaTemplateidfacturaTemplateNew = em.getReference(facturaTemplateidfacturaTemplateNew.getClass(), facturaTemplateidfacturaTemplateNew.getIdfacturaTemplate());
                campo.setFacturaTemplateidfacturaTemplate(facturaTemplateidfacturaTemplateNew);
            }
            campo = em.merge(campo);
            if (facturaTemplateidfacturaTemplateOld != null && !facturaTemplateidfacturaTemplateOld.equals(facturaTemplateidfacturaTemplateNew)) {
                facturaTemplateidfacturaTemplateOld.getCampoCollection().remove(campo);
                facturaTemplateidfacturaTemplateOld = em.merge(facturaTemplateidfacturaTemplateOld);
            }
            if (facturaTemplateidfacturaTemplateNew != null && !facturaTemplateidfacturaTemplateNew.equals(facturaTemplateidfacturaTemplateOld)) {
                facturaTemplateidfacturaTemplateNew.getCampoCollection().add(campo);
                facturaTemplateidfacturaTemplateNew = em.merge(facturaTemplateidfacturaTemplateNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = campo.getIdcampo();
                if (findCampo(id) == null) {
                    throw new NonexistentEntityException("The campo with id " + id + " no longer exists.");
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
            Campo campo;
            try {
                campo = em.getReference(Campo.class, id);
                campo.getIdcampo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The campo with id " + id + " no longer exists.", enfe);
            }
            FacturaTemplate facturaTemplateidfacturaTemplate = campo.getFacturaTemplateidfacturaTemplate();
            if (facturaTemplateidfacturaTemplate != null) {
                facturaTemplateidfacturaTemplate.getCampoCollection().remove(campo);
                facturaTemplateidfacturaTemplate = em.merge(facturaTemplateidfacturaTemplate);
            }
            em.remove(campo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Campo> findCampoEntities() {
        return findCampoEntities(true, -1, -1);
    }

    public List<Campo> findCampoEntities(int maxResults, int firstResult) {
        return findCampoEntities(false, maxResults, firstResult);
    }

    private List<Campo> findCampoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Campo.class));
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

    public Campo findCampo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Campo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCampoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Campo> rt = cq.from(Campo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
