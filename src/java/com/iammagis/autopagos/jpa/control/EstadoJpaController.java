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
import com.iammagis.autopagos.jpa.beans.ComprobanterecaudoPSE;
import com.iammagis.autopagos.jpa.beans.Estado;
import java.util.ArrayList;
import java.util.Collection;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.control.exceptions.IllegalOrphanException;
import com.iammagis.autopagos.jpa.control.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Usuario
 */
public class EstadoJpaController implements Serializable {

    public EstadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estado estado) {
        if (estado.getComprobanterecaudoPSECollection() == null) {
            estado.setComprobanterecaudoPSECollection(new ArrayList<ComprobanterecaudoPSE>());
        }
        if (estado.getFacturaCollection() == null) {
            estado.setFacturaCollection(new ArrayList<Factura>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ComprobanterecaudoPSE> attachedComprobanterecaudoPSECollection = new ArrayList<ComprobanterecaudoPSE>();
            for (ComprobanterecaudoPSE comprobanterecaudoPSECollectionComprobanterecaudoPSEToAttach : estado.getComprobanterecaudoPSECollection()) {
                comprobanterecaudoPSECollectionComprobanterecaudoPSEToAttach = em.getReference(comprobanterecaudoPSECollectionComprobanterecaudoPSEToAttach.getClass(), comprobanterecaudoPSECollectionComprobanterecaudoPSEToAttach.getIdcomprobanteRecaudoPSE());
                attachedComprobanterecaudoPSECollection.add(comprobanterecaudoPSECollectionComprobanterecaudoPSEToAttach);
            }
            estado.setComprobanterecaudoPSECollection(attachedComprobanterecaudoPSECollection);
            Collection<Factura> attachedFacturaCollection = new ArrayList<Factura>();
            for (Factura facturaCollectionFacturaToAttach : estado.getFacturaCollection()) {
                facturaCollectionFacturaToAttach = em.getReference(facturaCollectionFacturaToAttach.getClass(), facturaCollectionFacturaToAttach.getIdfactura());
                attachedFacturaCollection.add(facturaCollectionFacturaToAttach);
            }
            estado.setFacturaCollection(attachedFacturaCollection);
            em.persist(estado);
            for (ComprobanterecaudoPSE comprobanterecaudoPSECollectionComprobanterecaudoPSE : estado.getComprobanterecaudoPSECollection()) {
                Estado oldEstadoIdestadoOfComprobanterecaudoPSECollectionComprobanterecaudoPSE = comprobanterecaudoPSECollectionComprobanterecaudoPSE.getEstadoIdestado();
                comprobanterecaudoPSECollectionComprobanterecaudoPSE.setEstadoIdestado(estado);
                comprobanterecaudoPSECollectionComprobanterecaudoPSE = em.merge(comprobanterecaudoPSECollectionComprobanterecaudoPSE);
                if (oldEstadoIdestadoOfComprobanterecaudoPSECollectionComprobanterecaudoPSE != null) {
                    oldEstadoIdestadoOfComprobanterecaudoPSECollectionComprobanterecaudoPSE.getComprobanterecaudoPSECollection().remove(comprobanterecaudoPSECollectionComprobanterecaudoPSE);
                    oldEstadoIdestadoOfComprobanterecaudoPSECollectionComprobanterecaudoPSE = em.merge(oldEstadoIdestadoOfComprobanterecaudoPSECollectionComprobanterecaudoPSE);
                }
            }
            for (Factura facturaCollectionFactura : estado.getFacturaCollection()) {
                Estado oldEstadoIdestadoOfFacturaCollectionFactura = facturaCollectionFactura.getEstadoIdestado();
                facturaCollectionFactura.setEstadoIdestado(estado);
                facturaCollectionFactura = em.merge(facturaCollectionFactura);
                if (oldEstadoIdestadoOfFacturaCollectionFactura != null) {
                    oldEstadoIdestadoOfFacturaCollectionFactura.getFacturaCollection().remove(facturaCollectionFactura);
                    oldEstadoIdestadoOfFacturaCollectionFactura = em.merge(oldEstadoIdestadoOfFacturaCollectionFactura);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estado estado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado persistentEstado = em.find(Estado.class, estado.getIdestado());
            Collection<ComprobanterecaudoPSE> comprobanterecaudoPSECollectionOld = persistentEstado.getComprobanterecaudoPSECollection();
            Collection<ComprobanterecaudoPSE> comprobanterecaudoPSECollectionNew = estado.getComprobanterecaudoPSECollection();
            Collection<Factura> facturaCollectionOld = persistentEstado.getFacturaCollection();
            Collection<Factura> facturaCollectionNew = estado.getFacturaCollection();
            List<String> illegalOrphanMessages = null;
            for (ComprobanterecaudoPSE comprobanterecaudoPSECollectionOldComprobanterecaudoPSE : comprobanterecaudoPSECollectionOld) {
                if (!comprobanterecaudoPSECollectionNew.contains(comprobanterecaudoPSECollectionOldComprobanterecaudoPSE)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ComprobanterecaudoPSE " + comprobanterecaudoPSECollectionOldComprobanterecaudoPSE + " since its estadoIdestado field is not nullable.");
                }
            }
            for (Factura facturaCollectionOldFactura : facturaCollectionOld) {
                if (!facturaCollectionNew.contains(facturaCollectionOldFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Factura " + facturaCollectionOldFactura + " since its estadoIdestado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<ComprobanterecaudoPSE> attachedComprobanterecaudoPSECollectionNew = new ArrayList<ComprobanterecaudoPSE>();
            for (ComprobanterecaudoPSE comprobanterecaudoPSECollectionNewComprobanterecaudoPSEToAttach : comprobanterecaudoPSECollectionNew) {
                comprobanterecaudoPSECollectionNewComprobanterecaudoPSEToAttach = em.getReference(comprobanterecaudoPSECollectionNewComprobanterecaudoPSEToAttach.getClass(), comprobanterecaudoPSECollectionNewComprobanterecaudoPSEToAttach.getIdcomprobanteRecaudoPSE());
                attachedComprobanterecaudoPSECollectionNew.add(comprobanterecaudoPSECollectionNewComprobanterecaudoPSEToAttach);
            }
            comprobanterecaudoPSECollectionNew = attachedComprobanterecaudoPSECollectionNew;
            estado.setComprobanterecaudoPSECollection(comprobanterecaudoPSECollectionNew);
            Collection<Factura> attachedFacturaCollectionNew = new ArrayList<Factura>();
            for (Factura facturaCollectionNewFacturaToAttach : facturaCollectionNew) {
                facturaCollectionNewFacturaToAttach = em.getReference(facturaCollectionNewFacturaToAttach.getClass(), facturaCollectionNewFacturaToAttach.getIdfactura());
                attachedFacturaCollectionNew.add(facturaCollectionNewFacturaToAttach);
            }
            facturaCollectionNew = attachedFacturaCollectionNew;
            estado.setFacturaCollection(facturaCollectionNew);
            estado = em.merge(estado);
            for (ComprobanterecaudoPSE comprobanterecaudoPSECollectionNewComprobanterecaudoPSE : comprobanterecaudoPSECollectionNew) {
                if (!comprobanterecaudoPSECollectionOld.contains(comprobanterecaudoPSECollectionNewComprobanterecaudoPSE)) {
                    Estado oldEstadoIdestadoOfComprobanterecaudoPSECollectionNewComprobanterecaudoPSE = comprobanterecaudoPSECollectionNewComprobanterecaudoPSE.getEstadoIdestado();
                    comprobanterecaudoPSECollectionNewComprobanterecaudoPSE.setEstadoIdestado(estado);
                    comprobanterecaudoPSECollectionNewComprobanterecaudoPSE = em.merge(comprobanterecaudoPSECollectionNewComprobanterecaudoPSE);
                    if (oldEstadoIdestadoOfComprobanterecaudoPSECollectionNewComprobanterecaudoPSE != null && !oldEstadoIdestadoOfComprobanterecaudoPSECollectionNewComprobanterecaudoPSE.equals(estado)) {
                        oldEstadoIdestadoOfComprobanterecaudoPSECollectionNewComprobanterecaudoPSE.getComprobanterecaudoPSECollection().remove(comprobanterecaudoPSECollectionNewComprobanterecaudoPSE);
                        oldEstadoIdestadoOfComprobanterecaudoPSECollectionNewComprobanterecaudoPSE = em.merge(oldEstadoIdestadoOfComprobanterecaudoPSECollectionNewComprobanterecaudoPSE);
                    }
                }
            }
            for (Factura facturaCollectionNewFactura : facturaCollectionNew) {
                if (!facturaCollectionOld.contains(facturaCollectionNewFactura)) {
                    Estado oldEstadoIdestadoOfFacturaCollectionNewFactura = facturaCollectionNewFactura.getEstadoIdestado();
                    facturaCollectionNewFactura.setEstadoIdestado(estado);
                    facturaCollectionNewFactura = em.merge(facturaCollectionNewFactura);
                    if (oldEstadoIdestadoOfFacturaCollectionNewFactura != null && !oldEstadoIdestadoOfFacturaCollectionNewFactura.equals(estado)) {
                        oldEstadoIdestadoOfFacturaCollectionNewFactura.getFacturaCollection().remove(facturaCollectionNewFactura);
                        oldEstadoIdestadoOfFacturaCollectionNewFactura = em.merge(oldEstadoIdestadoOfFacturaCollectionNewFactura);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estado.getIdestado();
                if (findEstado(id) == null) {
                    throw new NonexistentEntityException("The estado with id " + id + " no longer exists.");
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
            Estado estado;
            try {
                estado = em.getReference(Estado.class, id);
                estado.getIdestado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ComprobanterecaudoPSE> comprobanterecaudoPSECollectionOrphanCheck = estado.getComprobanterecaudoPSECollection();
            for (ComprobanterecaudoPSE comprobanterecaudoPSECollectionOrphanCheckComprobanterecaudoPSE : comprobanterecaudoPSECollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estado (" + estado + ") cannot be destroyed since the ComprobanterecaudoPSE " + comprobanterecaudoPSECollectionOrphanCheckComprobanterecaudoPSE + " in its comprobanterecaudoPSECollection field has a non-nullable estadoIdestado field.");
            }
            Collection<Factura> facturaCollectionOrphanCheck = estado.getFacturaCollection();
            for (Factura facturaCollectionOrphanCheckFactura : facturaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estado (" + estado + ") cannot be destroyed since the Factura " + facturaCollectionOrphanCheckFactura + " in its facturaCollection field has a non-nullable estadoIdestado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estado> findEstadoEntities() {
        return findEstadoEntities(true, -1, -1);
    }

    public List<Estado> findEstadoEntities(int maxResults, int firstResult) {
        return findEstadoEntities(false, maxResults, firstResult);
    }

    private List<Estado> findEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estado.class));
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

    public Estado findEstado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estado> rt = cq.from(Estado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
