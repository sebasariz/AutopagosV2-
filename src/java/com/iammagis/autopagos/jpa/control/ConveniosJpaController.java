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
import com.iammagis.autopagos.jpa.beans.TipoCuenta;
import com.iammagis.autopagos.jpa.beans.Clase;
import com.iammagis.autopagos.jpa.beans.Canal;
import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Factura;
import java.util.ArrayList;
import java.util.Collection;
import com.iammagis.autopagos.jpa.beans.FacturaAutopagos;
import com.iammagis.autopagos.jpa.beans.FacturaTemplate;
import com.iammagis.autopagos.jpa.control.exceptions.IllegalOrphanException;
import com.iammagis.autopagos.jpa.control.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author Usuario
 */
public class ConveniosJpaController implements Serializable {

    public ConveniosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Convenios convenios) {
        if (convenios.getFacturaCollection() == null) {
            convenios.setFacturaCollection(new ArrayList<Factura>());
        }
        if (convenios.getFacturaAutopagosCollection() == null) {
            convenios.setFacturaAutopagosCollection(new ArrayList<FacturaAutopagos>());
        }
        if (convenios.getFacturaTemplateCollection() == null) {
            convenios.setFacturaTemplateCollection(new ArrayList<FacturaTemplate>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoCuenta tipoCuentaidtipoCuenta = convenios.getTipoCuentaidtipoCuenta();
            if (tipoCuentaidtipoCuenta != null) {
                tipoCuentaidtipoCuenta = em.getReference(tipoCuentaidtipoCuenta.getClass(), tipoCuentaidtipoCuenta.getIdtipoCuenta());
                convenios.setTipoCuentaidtipoCuenta(tipoCuentaidtipoCuenta);
            }
            Clase claseIdclase = convenios.getClaseIdclase();
            if (claseIdclase != null) {
                claseIdclase = em.getReference(claseIdclase.getClass(), claseIdclase.getIdclase());
                convenios.setClaseIdclase(claseIdclase);
            }
            Canal canalIdcanalPlanPost = convenios.getCanalIdcanalPlanPost();
            if (canalIdcanalPlanPost != null) {
                canalIdcanalPlanPost = em.getReference(canalIdcanalPlanPost.getClass(), canalIdcanalPlanPost.getIdcanal());
                convenios.setCanalIdcanalPlanPost(canalIdcanalPlanPost);
            }
            Collection<Factura> attachedFacturaCollection = new ArrayList<Factura>();
            for (Factura facturaCollectionFacturaToAttach : convenios.getFacturaCollection()) {
                facturaCollectionFacturaToAttach = em.getReference(facturaCollectionFacturaToAttach.getClass(), facturaCollectionFacturaToAttach.getIdfactura());
                attachedFacturaCollection.add(facturaCollectionFacturaToAttach);
            }
            convenios.setFacturaCollection(attachedFacturaCollection);
            Collection<FacturaAutopagos> attachedFacturaAutopagosCollection = new ArrayList<FacturaAutopagos>();
            for (FacturaAutopagos facturaAutopagosCollectionFacturaAutopagosToAttach : convenios.getFacturaAutopagosCollection()) {
                facturaAutopagosCollectionFacturaAutopagosToAttach = em.getReference(facturaAutopagosCollectionFacturaAutopagosToAttach.getClass(), facturaAutopagosCollectionFacturaAutopagosToAttach.getIdfacturaAutopagos());
                attachedFacturaAutopagosCollection.add(facturaAutopagosCollectionFacturaAutopagosToAttach);
            }
            convenios.setFacturaAutopagosCollection(attachedFacturaAutopagosCollection);
            Collection<FacturaTemplate> attachedFacturaTemplateCollection = new ArrayList<FacturaTemplate>();
            for (FacturaTemplate facturaTemplateCollectionFacturaTemplateToAttach : convenios.getFacturaTemplateCollection()) {
                facturaTemplateCollectionFacturaTemplateToAttach = em.getReference(facturaTemplateCollectionFacturaTemplateToAttach.getClass(), facturaTemplateCollectionFacturaTemplateToAttach.getIdfacturaTemplate());
                attachedFacturaTemplateCollection.add(facturaTemplateCollectionFacturaTemplateToAttach);
            }
            convenios.setFacturaTemplateCollection(attachedFacturaTemplateCollection);
            em.persist(convenios);
            if (tipoCuentaidtipoCuenta != null) {
                tipoCuentaidtipoCuenta.getConveniosCollection().add(convenios);
                tipoCuentaidtipoCuenta = em.merge(tipoCuentaidtipoCuenta);
            }
            if (claseIdclase != null) {
                claseIdclase.getConveniosCollection().add(convenios);
                claseIdclase = em.merge(claseIdclase);
            }
            if (canalIdcanalPlanPost != null) {
                canalIdcanalPlanPost.getConveniosCollection().add(convenios);
                canalIdcanalPlanPost = em.merge(canalIdcanalPlanPost);
            }
            for (Factura facturaCollectionFactura : convenios.getFacturaCollection()) {
                Convenios oldConveniosIdconveniosOfFacturaCollectionFactura = facturaCollectionFactura.getConveniosIdconvenios();
                facturaCollectionFactura.setConveniosIdconvenios(convenios);
                facturaCollectionFactura = em.merge(facturaCollectionFactura);
                if (oldConveniosIdconveniosOfFacturaCollectionFactura != null) {
                    oldConveniosIdconveniosOfFacturaCollectionFactura.getFacturaCollection().remove(facturaCollectionFactura);
                    oldConveniosIdconveniosOfFacturaCollectionFactura = em.merge(oldConveniosIdconveniosOfFacturaCollectionFactura);
                }
            }
            for (FacturaAutopagos facturaAutopagosCollectionFacturaAutopagos : convenios.getFacturaAutopagosCollection()) {
                Convenios oldConveniosIdconveniosOfFacturaAutopagosCollectionFacturaAutopagos = facturaAutopagosCollectionFacturaAutopagos.getConveniosIdconvenios();
                facturaAutopagosCollectionFacturaAutopagos.setConveniosIdconvenios(convenios);
                facturaAutopagosCollectionFacturaAutopagos = em.merge(facturaAutopagosCollectionFacturaAutopagos);
                if (oldConveniosIdconveniosOfFacturaAutopagosCollectionFacturaAutopagos != null) {
                    oldConveniosIdconveniosOfFacturaAutopagosCollectionFacturaAutopagos.getFacturaAutopagosCollection().remove(facturaAutopagosCollectionFacturaAutopagos);
                    oldConveniosIdconveniosOfFacturaAutopagosCollectionFacturaAutopagos = em.merge(oldConveniosIdconveniosOfFacturaAutopagosCollectionFacturaAutopagos);
                }
            }
            for (FacturaTemplate facturaTemplateCollectionFacturaTemplate : convenios.getFacturaTemplateCollection()) {
                Convenios oldConveniosIdconveniosOfFacturaTemplateCollectionFacturaTemplate = facturaTemplateCollectionFacturaTemplate.getConveniosIdconvenios();
                facturaTemplateCollectionFacturaTemplate.setConveniosIdconvenios(convenios);
                facturaTemplateCollectionFacturaTemplate = em.merge(facturaTemplateCollectionFacturaTemplate);
                if (oldConveniosIdconveniosOfFacturaTemplateCollectionFacturaTemplate != null) {
                    oldConveniosIdconveniosOfFacturaTemplateCollectionFacturaTemplate.getFacturaTemplateCollection().remove(facturaTemplateCollectionFacturaTemplate);
                    oldConveniosIdconveniosOfFacturaTemplateCollectionFacturaTemplate = em.merge(oldConveniosIdconveniosOfFacturaTemplateCollectionFacturaTemplate);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Convenios convenios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Convenios persistentConvenios = em.find(Convenios.class, convenios.getIdconvenios());
            TipoCuenta tipoCuentaidtipoCuentaOld = persistentConvenios.getTipoCuentaidtipoCuenta();
            TipoCuenta tipoCuentaidtipoCuentaNew = convenios.getTipoCuentaidtipoCuenta();
            Clase claseIdclaseOld = persistentConvenios.getClaseIdclase();
            Clase claseIdclaseNew = convenios.getClaseIdclase();
            Canal canalIdcanalPlanPostOld = persistentConvenios.getCanalIdcanalPlanPost();
            Canal canalIdcanalPlanPostNew = convenios.getCanalIdcanalPlanPost();
            Collection<Factura> facturaCollectionOld = persistentConvenios.getFacturaCollection();
            Collection<Factura> facturaCollectionNew = convenios.getFacturaCollection();
            Collection<FacturaAutopagos> facturaAutopagosCollectionOld = persistentConvenios.getFacturaAutopagosCollection();
            Collection<FacturaAutopagos> facturaAutopagosCollectionNew = convenios.getFacturaAutopagosCollection();
            Collection<FacturaTemplate> facturaTemplateCollectionOld = persistentConvenios.getFacturaTemplateCollection();
            Collection<FacturaTemplate> facturaTemplateCollectionNew = convenios.getFacturaTemplateCollection();
            List<String> illegalOrphanMessages = null;
            for (Factura facturaCollectionOldFactura : facturaCollectionOld) {
                if (!facturaCollectionNew.contains(facturaCollectionOldFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Factura " + facturaCollectionOldFactura + " since its conveniosIdconvenios field is not nullable.");
                }
            }
            for (FacturaAutopagos facturaAutopagosCollectionOldFacturaAutopagos : facturaAutopagosCollectionOld) {
                if (!facturaAutopagosCollectionNew.contains(facturaAutopagosCollectionOldFacturaAutopagos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FacturaAutopagos " + facturaAutopagosCollectionOldFacturaAutopagos + " since its conveniosIdconvenios field is not nullable.");
                }
            }
            for (FacturaTemplate facturaTemplateCollectionOldFacturaTemplate : facturaTemplateCollectionOld) {
                if (!facturaTemplateCollectionNew.contains(facturaTemplateCollectionOldFacturaTemplate)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FacturaTemplate " + facturaTemplateCollectionOldFacturaTemplate + " since its conveniosIdconvenios field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tipoCuentaidtipoCuentaNew != null) {
                tipoCuentaidtipoCuentaNew = em.getReference(tipoCuentaidtipoCuentaNew.getClass(), tipoCuentaidtipoCuentaNew.getIdtipoCuenta());
                convenios.setTipoCuentaidtipoCuenta(tipoCuentaidtipoCuentaNew);
            }
            if (claseIdclaseNew != null) {
                claseIdclaseNew = em.getReference(claseIdclaseNew.getClass(), claseIdclaseNew.getIdclase());
                convenios.setClaseIdclase(claseIdclaseNew);
            }
            if (canalIdcanalPlanPostNew != null) {
                canalIdcanalPlanPostNew = em.getReference(canalIdcanalPlanPostNew.getClass(), canalIdcanalPlanPostNew.getIdcanal());
                convenios.setCanalIdcanalPlanPost(canalIdcanalPlanPostNew);
            }
            Collection<Factura> attachedFacturaCollectionNew = new ArrayList<Factura>();
            for (Factura facturaCollectionNewFacturaToAttach : facturaCollectionNew) {
                facturaCollectionNewFacturaToAttach = em.getReference(facturaCollectionNewFacturaToAttach.getClass(), facturaCollectionNewFacturaToAttach.getIdfactura());
                attachedFacturaCollectionNew.add(facturaCollectionNewFacturaToAttach);
            }
            facturaCollectionNew = attachedFacturaCollectionNew;
            convenios.setFacturaCollection(facturaCollectionNew);
            Collection<FacturaAutopagos> attachedFacturaAutopagosCollectionNew = new ArrayList<FacturaAutopagos>();
            for (FacturaAutopagos facturaAutopagosCollectionNewFacturaAutopagosToAttach : facturaAutopagosCollectionNew) {
                facturaAutopagosCollectionNewFacturaAutopagosToAttach = em.getReference(facturaAutopagosCollectionNewFacturaAutopagosToAttach.getClass(), facturaAutopagosCollectionNewFacturaAutopagosToAttach.getIdfacturaAutopagos());
                attachedFacturaAutopagosCollectionNew.add(facturaAutopagosCollectionNewFacturaAutopagosToAttach);
            }
            facturaAutopagosCollectionNew = attachedFacturaAutopagosCollectionNew;
            convenios.setFacturaAutopagosCollection(facturaAutopagosCollectionNew);
            Collection<FacturaTemplate> attachedFacturaTemplateCollectionNew = new ArrayList<FacturaTemplate>();
            for (FacturaTemplate facturaTemplateCollectionNewFacturaTemplateToAttach : facturaTemplateCollectionNew) {
                facturaTemplateCollectionNewFacturaTemplateToAttach = em.getReference(facturaTemplateCollectionNewFacturaTemplateToAttach.getClass(), facturaTemplateCollectionNewFacturaTemplateToAttach.getIdfacturaTemplate());
                attachedFacturaTemplateCollectionNew.add(facturaTemplateCollectionNewFacturaTemplateToAttach);
            }
            facturaTemplateCollectionNew = attachedFacturaTemplateCollectionNew;
            convenios.setFacturaTemplateCollection(facturaTemplateCollectionNew);
            convenios = em.merge(convenios);
            if (tipoCuentaidtipoCuentaOld != null && !tipoCuentaidtipoCuentaOld.equals(tipoCuentaidtipoCuentaNew)) {
                tipoCuentaidtipoCuentaOld.getConveniosCollection().remove(convenios);
                tipoCuentaidtipoCuentaOld = em.merge(tipoCuentaidtipoCuentaOld);
            }
            if (tipoCuentaidtipoCuentaNew != null && !tipoCuentaidtipoCuentaNew.equals(tipoCuentaidtipoCuentaOld)) {
                tipoCuentaidtipoCuentaNew.getConveniosCollection().add(convenios);
                tipoCuentaidtipoCuentaNew = em.merge(tipoCuentaidtipoCuentaNew);
            }
            if (claseIdclaseOld != null && !claseIdclaseOld.equals(claseIdclaseNew)) {
                claseIdclaseOld.getConveniosCollection().remove(convenios);
                claseIdclaseOld = em.merge(claseIdclaseOld);
            }
            if (claseIdclaseNew != null && !claseIdclaseNew.equals(claseIdclaseOld)) {
                claseIdclaseNew.getConveniosCollection().add(convenios);
                claseIdclaseNew = em.merge(claseIdclaseNew);
            }
            if (canalIdcanalPlanPostOld != null && !canalIdcanalPlanPostOld.equals(canalIdcanalPlanPostNew)) {
                canalIdcanalPlanPostOld.getConveniosCollection().remove(convenios);
                canalIdcanalPlanPostOld = em.merge(canalIdcanalPlanPostOld);
            }
            if (canalIdcanalPlanPostNew != null && !canalIdcanalPlanPostNew.equals(canalIdcanalPlanPostOld)) {
                canalIdcanalPlanPostNew.getConveniosCollection().add(convenios);
                canalIdcanalPlanPostNew = em.merge(canalIdcanalPlanPostNew);
            }
            for (Factura facturaCollectionNewFactura : facturaCollectionNew) {
                if (!facturaCollectionOld.contains(facturaCollectionNewFactura)) {
                    Convenios oldConveniosIdconveniosOfFacturaCollectionNewFactura = facturaCollectionNewFactura.getConveniosIdconvenios();
                    facturaCollectionNewFactura.setConveniosIdconvenios(convenios);
                    facturaCollectionNewFactura = em.merge(facturaCollectionNewFactura);
                    if (oldConveniosIdconveniosOfFacturaCollectionNewFactura != null && !oldConveniosIdconveniosOfFacturaCollectionNewFactura.equals(convenios)) {
                        oldConveniosIdconveniosOfFacturaCollectionNewFactura.getFacturaCollection().remove(facturaCollectionNewFactura);
                        oldConveniosIdconveniosOfFacturaCollectionNewFactura = em.merge(oldConveniosIdconveniosOfFacturaCollectionNewFactura);
                    }
                }
            }
            for (FacturaAutopagos facturaAutopagosCollectionNewFacturaAutopagos : facturaAutopagosCollectionNew) {
                if (!facturaAutopagosCollectionOld.contains(facturaAutopagosCollectionNewFacturaAutopagos)) {
                    Convenios oldConveniosIdconveniosOfFacturaAutopagosCollectionNewFacturaAutopagos = facturaAutopagosCollectionNewFacturaAutopagos.getConveniosIdconvenios();
                    facturaAutopagosCollectionNewFacturaAutopagos.setConveniosIdconvenios(convenios);
                    facturaAutopagosCollectionNewFacturaAutopagos = em.merge(facturaAutopagosCollectionNewFacturaAutopagos);
                    if (oldConveniosIdconveniosOfFacturaAutopagosCollectionNewFacturaAutopagos != null && !oldConveniosIdconveniosOfFacturaAutopagosCollectionNewFacturaAutopagos.equals(convenios)) {
                        oldConveniosIdconveniosOfFacturaAutopagosCollectionNewFacturaAutopagos.getFacturaAutopagosCollection().remove(facturaAutopagosCollectionNewFacturaAutopagos);
                        oldConveniosIdconveniosOfFacturaAutopagosCollectionNewFacturaAutopagos = em.merge(oldConveniosIdconveniosOfFacturaAutopagosCollectionNewFacturaAutopagos);
                    }
                }
            }
            for (FacturaTemplate facturaTemplateCollectionNewFacturaTemplate : facturaTemplateCollectionNew) {
                if (!facturaTemplateCollectionOld.contains(facturaTemplateCollectionNewFacturaTemplate)) {
                    Convenios oldConveniosIdconveniosOfFacturaTemplateCollectionNewFacturaTemplate = facturaTemplateCollectionNewFacturaTemplate.getConveniosIdconvenios();
                    facturaTemplateCollectionNewFacturaTemplate.setConveniosIdconvenios(convenios);
                    facturaTemplateCollectionNewFacturaTemplate = em.merge(facturaTemplateCollectionNewFacturaTemplate);
                    if (oldConveniosIdconveniosOfFacturaTemplateCollectionNewFacturaTemplate != null && !oldConveniosIdconveniosOfFacturaTemplateCollectionNewFacturaTemplate.equals(convenios)) {
                        oldConveniosIdconveniosOfFacturaTemplateCollectionNewFacturaTemplate.getFacturaTemplateCollection().remove(facturaTemplateCollectionNewFacturaTemplate);
                        oldConveniosIdconveniosOfFacturaTemplateCollectionNewFacturaTemplate = em.merge(oldConveniosIdconveniosOfFacturaTemplateCollectionNewFacturaTemplate);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = convenios.getIdconvenios();
                if (findConvenios(id) == null) {
                    throw new NonexistentEntityException("The convenios with id " + id + " no longer exists.");
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
            Convenios convenios;
            try {
                convenios = em.getReference(Convenios.class, id);
                convenios.getIdconvenios();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The convenios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Factura> facturaCollectionOrphanCheck = convenios.getFacturaCollection();
            for (Factura facturaCollectionOrphanCheckFactura : facturaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Convenios (" + convenios + ") cannot be destroyed since the Factura " + facturaCollectionOrphanCheckFactura + " in its facturaCollection field has a non-nullable conveniosIdconvenios field.");
            }
            Collection<FacturaAutopagos> facturaAutopagosCollectionOrphanCheck = convenios.getFacturaAutopagosCollection();
            for (FacturaAutopagos facturaAutopagosCollectionOrphanCheckFacturaAutopagos : facturaAutopagosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Convenios (" + convenios + ") cannot be destroyed since the FacturaAutopagos " + facturaAutopagosCollectionOrphanCheckFacturaAutopagos + " in its facturaAutopagosCollection field has a non-nullable conveniosIdconvenios field.");
            }
            Collection<FacturaTemplate> facturaTemplateCollectionOrphanCheck = convenios.getFacturaTemplateCollection();
            for (FacturaTemplate facturaTemplateCollectionOrphanCheckFacturaTemplate : facturaTemplateCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Convenios (" + convenios + ") cannot be destroyed since the FacturaTemplate " + facturaTemplateCollectionOrphanCheckFacturaTemplate + " in its facturaTemplateCollection field has a non-nullable conveniosIdconvenios field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoCuenta tipoCuentaidtipoCuenta = convenios.getTipoCuentaidtipoCuenta();
            if (tipoCuentaidtipoCuenta != null) {
                tipoCuentaidtipoCuenta.getConveniosCollection().remove(convenios);
                tipoCuentaidtipoCuenta = em.merge(tipoCuentaidtipoCuenta);
            }
            Clase claseIdclase = convenios.getClaseIdclase();
            if (claseIdclase != null) {
                claseIdclase.getConveniosCollection().remove(convenios);
                claseIdclase = em.merge(claseIdclase);
            }
            Canal canalIdcanalPlanPost = convenios.getCanalIdcanalPlanPost();
            if (canalIdcanalPlanPost != null) {
                canalIdcanalPlanPost.getConveniosCollection().remove(convenios);
                canalIdcanalPlanPost = em.merge(canalIdcanalPlanPost);
            }
            em.remove(convenios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Convenios> findConveniosEntities() {
        return findConveniosEntities(true, -1, -1);
    }

    public List<Convenios> findConveniosEntities(int maxResults, int firstResult) {
        return findConveniosEntities(false, maxResults, firstResult);
    }

    private List<Convenios> findConveniosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Convenios.class));
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

    public Convenios findConvenios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Convenios.class, id);
        } finally {
            em.close();
        }
    }

    public int getConveniosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Convenios> rt = cq.from(Convenios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public int countConveniosSIPAR() {
        EntityManager em = getEntityManager();
        String queryString = "SELECT count(*) as suma FROM `convenios` WHERE `tipo_convenio`= 1 ";
        Query query = em.createNativeQuery(queryString);
        int objects = 0;
        try {
            objects = Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
        }
        return objects;

    }

    public int countConveniosONLINE() {
        EntityManager em = getEntityManager();
        String queryString = "SELECT count(*) as suma FROM `convenios` WHERE `tipo_convenio`= 2 ";
        Query query = em.createNativeQuery(queryString);
        int objects = 0;
        try {
            objects = Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
        }
        return objects;

    }

    public int countConveniosCORRESPONSALIA() {
        EntityManager em = getEntityManager();
        String queryString = "SELECT count(*) as suma FROM `convenios` WHERE `tipo_convenio`= 3 ";
        Query query = em.createNativeQuery(queryString);
        int objects = 0;
        try {
            objects = Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
        }
        return objects;

    }

    public ArrayList<Convenios> getConveniosByTipo(int i) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `convenios` WHERE `tipo_convenio`= " + i;
        Query query = em.createNativeQuery(queryString, Convenios.class);
        ArrayList<Convenios> result = new ArrayList<>(query.getResultList());
        return result;

    }

    public Convenios findByToken(String token) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Convenios.findByCodigo", Convenios.class);
        query.setParameter("codigo", token);
        Convenios convenios = null;
        try {
            convenios = (Convenios) query.getSingleResult();
        } catch (NoResultException e) {
        }
        return convenios;
    }

    public Convenios findByCodigoBarras(String codigoBarrasTercero) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Convenios.findByCodigoBarrasTercero", Convenios.class);
        query.setParameter("codigoBarrasTercero", codigoBarrasTercero);
        Convenios convenios = null;
        try {
            convenios = (Convenios) query.getSingleResult();
        } catch (NoResultException e) {
        }
        return convenios;
    }

    public ArrayList<Convenios> getConveniosRecarga() {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `convenios` WHERE `recarga_tercero`= 1";
        Query query = em.createNativeQuery(queryString, Convenios.class);
        ArrayList<Convenios> result = new ArrayList<>(query.getResultList());
        return result;
    }

}
