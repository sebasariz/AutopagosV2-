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
import com.iammagis.autopagos.jpa.beans.Campo;
import java.util.ArrayList;
import java.util.Collection;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.FacturaTemplate;
import com.iammagis.autopagos.jpa.control.exceptions.IllegalOrphanException;
import com.iammagis.autopagos.jpa.control.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Usuario
 */
public class FacturaTemplateJpaController implements Serializable {

    public FacturaTemplateJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public FacturaTemplate create(FacturaTemplate facturaTemplate) {
        if (facturaTemplate.getCampoCollection() == null) {
            facturaTemplate.setCampoCollection(new ArrayList<Campo>());
        }
        if (facturaTemplate.getFacturaCollection() == null) {
            facturaTemplate.setFacturaCollection(new ArrayList<Factura>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Convenios conveniosIdconvenios = facturaTemplate.getConveniosIdconvenios();
            if (conveniosIdconvenios != null) {
                conveniosIdconvenios = em.getReference(conveniosIdconvenios.getClass(), conveniosIdconvenios.getIdconvenios());
                facturaTemplate.setConveniosIdconvenios(conveniosIdconvenios);
            }
            Collection<Campo> attachedCampoCollection = new ArrayList<Campo>();
            for (Campo campoCollectionCampoToAttach : facturaTemplate.getCampoCollection()) {
                campoCollectionCampoToAttach = em.getReference(campoCollectionCampoToAttach.getClass(), campoCollectionCampoToAttach.getIdcampo());
                attachedCampoCollection.add(campoCollectionCampoToAttach);
            }
            facturaTemplate.setCampoCollection(attachedCampoCollection);
            Collection<Factura> attachedFacturaCollection = new ArrayList<Factura>();
            for (Factura facturaCollectionFacturaToAttach : facturaTemplate.getFacturaCollection()) {
                facturaCollectionFacturaToAttach = em.getReference(facturaCollectionFacturaToAttach.getClass(), facturaCollectionFacturaToAttach.getIdfactura());
                attachedFacturaCollection.add(facturaCollectionFacturaToAttach);
            }
            facturaTemplate.setFacturaCollection(attachedFacturaCollection);
            em.persist(facturaTemplate);
            if (conveniosIdconvenios != null) {
                conveniosIdconvenios.getFacturaTemplateCollection().add(facturaTemplate);
                conveniosIdconvenios = em.merge(conveniosIdconvenios);
            }
            for (Campo campoCollectionCampo : facturaTemplate.getCampoCollection()) {
                FacturaTemplate oldFacturaTemplateidfacturaTemplateOfCampoCollectionCampo = campoCollectionCampo.getFacturaTemplateidfacturaTemplate();
                campoCollectionCampo.setFacturaTemplateidfacturaTemplate(facturaTemplate);
                campoCollectionCampo = em.merge(campoCollectionCampo);
                if (oldFacturaTemplateidfacturaTemplateOfCampoCollectionCampo != null) {
                    oldFacturaTemplateidfacturaTemplateOfCampoCollectionCampo.getCampoCollection().remove(campoCollectionCampo);
                    oldFacturaTemplateidfacturaTemplateOfCampoCollectionCampo = em.merge(oldFacturaTemplateidfacturaTemplateOfCampoCollectionCampo);
                }
            }
            for (Factura facturaCollectionFactura : facturaTemplate.getFacturaCollection()) {
                FacturaTemplate oldFacturaTemplateidfacturaTemplateOfFacturaCollectionFactura = facturaCollectionFactura.getFacturaTemplateidfacturaTemplate();
                facturaCollectionFactura.setFacturaTemplateidfacturaTemplate(facturaTemplate);
                facturaCollectionFactura = em.merge(facturaCollectionFactura);
                if (oldFacturaTemplateidfacturaTemplateOfFacturaCollectionFactura != null) {
                    oldFacturaTemplateidfacturaTemplateOfFacturaCollectionFactura.getFacturaCollection().remove(facturaCollectionFactura);
                    oldFacturaTemplateidfacturaTemplateOfFacturaCollectionFactura = em.merge(oldFacturaTemplateidfacturaTemplateOfFacturaCollectionFactura);
                }
            }
            em.getTransaction().commit();
            return facturaTemplate;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public FacturaTemplate edit(FacturaTemplate facturaTemplate) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FacturaTemplate persistentFacturaTemplate = em.find(FacturaTemplate.class, facturaTemplate.getIdfacturaTemplate());
            Convenios conveniosIdconveniosOld = persistentFacturaTemplate.getConveniosIdconvenios();
            Convenios conveniosIdconveniosNew = facturaTemplate.getConveniosIdconvenios();
            Collection<Campo> campoCollectionOld = persistentFacturaTemplate.getCampoCollection();
            Collection<Campo> campoCollectionNew = facturaTemplate.getCampoCollection();
            Collection<Factura> facturaCollectionOld = persistentFacturaTemplate.getFacturaCollection();
            Collection<Factura> facturaCollectionNew = facturaTemplate.getFacturaCollection();
            List<String> illegalOrphanMessages = null;
            for (Campo campoCollectionOldCampo : campoCollectionOld) {
                if (!campoCollectionNew.contains(campoCollectionOldCampo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Campo " + campoCollectionOldCampo + " since its facturaTemplateidfacturaTemplate field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (conveniosIdconveniosNew != null) {
                conveniosIdconveniosNew = em.getReference(conveniosIdconveniosNew.getClass(), conveniosIdconveniosNew.getIdconvenios());
                facturaTemplate.setConveniosIdconvenios(conveniosIdconveniosNew);
            }
            Collection<Campo> attachedCampoCollectionNew = new ArrayList<Campo>();
            for (Campo campoCollectionNewCampoToAttach : campoCollectionNew) {
                campoCollectionNewCampoToAttach = em.getReference(campoCollectionNewCampoToAttach.getClass(), campoCollectionNewCampoToAttach.getIdcampo());
                attachedCampoCollectionNew.add(campoCollectionNewCampoToAttach);
            }
            campoCollectionNew = attachedCampoCollectionNew;
            facturaTemplate.setCampoCollection(campoCollectionNew);
            Collection<Factura> attachedFacturaCollectionNew = new ArrayList<Factura>();
            for (Factura facturaCollectionNewFacturaToAttach : facturaCollectionNew) {
                facturaCollectionNewFacturaToAttach = em.getReference(facturaCollectionNewFacturaToAttach.getClass(), facturaCollectionNewFacturaToAttach.getIdfactura());
                attachedFacturaCollectionNew.add(facturaCollectionNewFacturaToAttach);
            }
            facturaCollectionNew = attachedFacturaCollectionNew;
            facturaTemplate.setFacturaCollection(facturaCollectionNew);
            facturaTemplate = em.merge(facturaTemplate);
            if (conveniosIdconveniosOld != null && !conveniosIdconveniosOld.equals(conveniosIdconveniosNew)) {
                conveniosIdconveniosOld.getFacturaTemplateCollection().remove(facturaTemplate);
                conveniosIdconveniosOld = em.merge(conveniosIdconveniosOld);
            }
            if (conveniosIdconveniosNew != null && !conveniosIdconveniosNew.equals(conveniosIdconveniosOld)) {
                conveniosIdconveniosNew.getFacturaTemplateCollection().add(facturaTemplate);
                conveniosIdconveniosNew = em.merge(conveniosIdconveniosNew);
            }
            for (Campo campoCollectionNewCampo : campoCollectionNew) {
                if (!campoCollectionOld.contains(campoCollectionNewCampo)) {
                    FacturaTemplate oldFacturaTemplateidfacturaTemplateOfCampoCollectionNewCampo = campoCollectionNewCampo.getFacturaTemplateidfacturaTemplate();
                    campoCollectionNewCampo.setFacturaTemplateidfacturaTemplate(facturaTemplate);
                    campoCollectionNewCampo = em.merge(campoCollectionNewCampo);
                    if (oldFacturaTemplateidfacturaTemplateOfCampoCollectionNewCampo != null && !oldFacturaTemplateidfacturaTemplateOfCampoCollectionNewCampo.equals(facturaTemplate)) {
                        oldFacturaTemplateidfacturaTemplateOfCampoCollectionNewCampo.getCampoCollection().remove(campoCollectionNewCampo);
                        oldFacturaTemplateidfacturaTemplateOfCampoCollectionNewCampo = em.merge(oldFacturaTemplateidfacturaTemplateOfCampoCollectionNewCampo);
                    }
                }
            }
            for (Factura facturaCollectionOldFactura : facturaCollectionOld) {
                if (!facturaCollectionNew.contains(facturaCollectionOldFactura)) {
                    facturaCollectionOldFactura.setFacturaTemplateidfacturaTemplate(null);
                    facturaCollectionOldFactura = em.merge(facturaCollectionOldFactura);
                }
            }
            for (Factura facturaCollectionNewFactura : facturaCollectionNew) {
                if (!facturaCollectionOld.contains(facturaCollectionNewFactura)) {
                    FacturaTemplate oldFacturaTemplateidfacturaTemplateOfFacturaCollectionNewFactura = facturaCollectionNewFactura.getFacturaTemplateidfacturaTemplate();
                    facturaCollectionNewFactura.setFacturaTemplateidfacturaTemplate(facturaTemplate);
                    facturaCollectionNewFactura = em.merge(facturaCollectionNewFactura);
                    if (oldFacturaTemplateidfacturaTemplateOfFacturaCollectionNewFactura != null && !oldFacturaTemplateidfacturaTemplateOfFacturaCollectionNewFactura.equals(facturaTemplate)) {
                        oldFacturaTemplateidfacturaTemplateOfFacturaCollectionNewFactura.getFacturaCollection().remove(facturaCollectionNewFactura);
                        oldFacturaTemplateidfacturaTemplateOfFacturaCollectionNewFactura = em.merge(oldFacturaTemplateidfacturaTemplateOfFacturaCollectionNewFactura);
                    }
                }
            }
            em.getTransaction().commit();
            return facturaTemplate;
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = facturaTemplate.getIdfacturaTemplate();
                if (findFacturaTemplate(id) == null) {
                    throw new NonexistentEntityException("The facturaTemplate with id " + id + " no longer exists.");
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
            FacturaTemplate facturaTemplate;
            try {
                facturaTemplate = em.getReference(FacturaTemplate.class, id);
                facturaTemplate.getIdfacturaTemplate();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facturaTemplate with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Campo> campoCollectionOrphanCheck = facturaTemplate.getCampoCollection();
            for (Campo campoCollectionOrphanCheckCampo : campoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This FacturaTemplate (" + facturaTemplate + ") cannot be destroyed since the Campo " + campoCollectionOrphanCheckCampo + " in its campoCollection field has a non-nullable facturaTemplateidfacturaTemplate field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Convenios conveniosIdconvenios = facturaTemplate.getConveniosIdconvenios();
            if (conveniosIdconvenios != null) {
                conveniosIdconvenios.getFacturaTemplateCollection().remove(facturaTemplate);
                conveniosIdconvenios = em.merge(conveniosIdconvenios);
            }
            Collection<Factura> facturaCollection = facturaTemplate.getFacturaCollection();
            for (Factura facturaCollectionFactura : facturaCollection) {
                facturaCollectionFactura.setFacturaTemplateidfacturaTemplate(null);
                facturaCollectionFactura = em.merge(facturaCollectionFactura);
            }
            em.remove(facturaTemplate);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FacturaTemplate> findFacturaTemplateEntities() {
        return findFacturaTemplateEntities(true, -1, -1);
    }

    public List<FacturaTemplate> findFacturaTemplateEntities(int maxResults, int firstResult) {
        return findFacturaTemplateEntities(false, maxResults, firstResult);
    }

    private List<FacturaTemplate> findFacturaTemplateEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FacturaTemplate.class));
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

    public FacturaTemplate findFacturaTemplate(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FacturaTemplate.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaTemplateCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FacturaTemplate> rt = cq.from(FacturaTemplate.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
