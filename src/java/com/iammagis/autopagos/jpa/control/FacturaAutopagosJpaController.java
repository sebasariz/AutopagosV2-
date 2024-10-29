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
import com.iammagis.autopagos.jpa.beans.FacturaAutopagos;
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
public class FacturaAutopagosJpaController implements Serializable {

    public FacturaAutopagosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public FacturaAutopagos create(FacturaAutopagos facturaAutopagos) {
        if (facturaAutopagos.getTransaccionCollection() == null) {
            facturaAutopagos.setTransaccionCollection(new ArrayList<Transaccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Convenios conveniosIdconvenios = facturaAutopagos.getConveniosIdconvenios();
            if (conveniosIdconvenios != null) {
                conveniosIdconvenios = em.getReference(conveniosIdconvenios.getClass(), conveniosIdconvenios.getIdconvenios());
                facturaAutopagos.setConveniosIdconvenios(conveniosIdconvenios);
            }
            Collection<Transaccion> attachedTransaccionCollection = new ArrayList<Transaccion>();
            for (Transaccion transaccionCollectionTransaccionToAttach : facturaAutopagos.getTransaccionCollection()) {
                transaccionCollectionTransaccionToAttach = em.getReference(transaccionCollectionTransaccionToAttach.getClass(), transaccionCollectionTransaccionToAttach.getIdtransaccion());
                attachedTransaccionCollection.add(transaccionCollectionTransaccionToAttach);
            }
            facturaAutopagos.setTransaccionCollection(attachedTransaccionCollection);
            em.persist(facturaAutopagos);
            if (conveniosIdconvenios != null) {
                conveniosIdconvenios.getFacturaAutopagosCollection().add(facturaAutopagos);
                conveniosIdconvenios = em.merge(conveniosIdconvenios);
            }
            for (Transaccion transaccionCollectionTransaccion : facturaAutopagos.getTransaccionCollection()) {
                FacturaAutopagos oldFacturaAutopagosIdfacturaAutopagosOfTransaccionCollectionTransaccion = transaccionCollectionTransaccion.getFacturaAutopagosIdfacturaAutopagos();
                transaccionCollectionTransaccion.setFacturaAutopagosIdfacturaAutopagos(facturaAutopagos);
                transaccionCollectionTransaccion = em.merge(transaccionCollectionTransaccion);
                if (oldFacturaAutopagosIdfacturaAutopagosOfTransaccionCollectionTransaccion != null) {
                    oldFacturaAutopagosIdfacturaAutopagosOfTransaccionCollectionTransaccion.getTransaccionCollection().remove(transaccionCollectionTransaccion);
                    oldFacturaAutopagosIdfacturaAutopagosOfTransaccionCollectionTransaccion = em.merge(oldFacturaAutopagosIdfacturaAutopagosOfTransaccionCollectionTransaccion);
                }
            }
            em.getTransaction().commit();
            return facturaAutopagos;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public FacturaAutopagos edit(FacturaAutopagos facturaAutopagos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FacturaAutopagos persistentFacturaAutopagos = em.find(FacturaAutopagos.class, facturaAutopagos.getIdfacturaAutopagos());
            Convenios conveniosIdconveniosOld = persistentFacturaAutopagos.getConveniosIdconvenios();
            Convenios conveniosIdconveniosNew = facturaAutopagos.getConveniosIdconvenios();
            Collection<Transaccion> transaccionCollectionOld = persistentFacturaAutopagos.getTransaccionCollection();
            Collection<Transaccion> transaccionCollectionNew = facturaAutopagos.getTransaccionCollection();
            if (conveniosIdconveniosNew != null) {
                conveniosIdconveniosNew = em.getReference(conveniosIdconveniosNew.getClass(), conveniosIdconveniosNew.getIdconvenios());
                facturaAutopagos.setConveniosIdconvenios(conveniosIdconveniosNew);
            }
            Collection<Transaccion> attachedTransaccionCollectionNew = new ArrayList<Transaccion>();
            for (Transaccion transaccionCollectionNewTransaccionToAttach : transaccionCollectionNew) {
                transaccionCollectionNewTransaccionToAttach = em.getReference(transaccionCollectionNewTransaccionToAttach.getClass(), transaccionCollectionNewTransaccionToAttach.getIdtransaccion());
                attachedTransaccionCollectionNew.add(transaccionCollectionNewTransaccionToAttach);
            }
            transaccionCollectionNew = attachedTransaccionCollectionNew;
            facturaAutopagos.setTransaccionCollection(transaccionCollectionNew);
            facturaAutopagos = em.merge(facturaAutopagos);
            if (conveniosIdconveniosOld != null && !conveniosIdconveniosOld.equals(conveniosIdconveniosNew)) {
                conveniosIdconveniosOld.getFacturaAutopagosCollection().remove(facturaAutopagos);
                conveniosIdconveniosOld = em.merge(conveniosIdconveniosOld);
            }
            if (conveniosIdconveniosNew != null && !conveniosIdconveniosNew.equals(conveniosIdconveniosOld)) {
                conveniosIdconveniosNew.getFacturaAutopagosCollection().add(facturaAutopagos);
                conveniosIdconveniosNew = em.merge(conveniosIdconveniosNew);
            }
            for (Transaccion transaccionCollectionOldTransaccion : transaccionCollectionOld) {
                if (!transaccionCollectionNew.contains(transaccionCollectionOldTransaccion)) {
                    transaccionCollectionOldTransaccion.setFacturaAutopagosIdfacturaAutopagos(null);
                    transaccionCollectionOldTransaccion = em.merge(transaccionCollectionOldTransaccion);
                }
            }
            for (Transaccion transaccionCollectionNewTransaccion : transaccionCollectionNew) {
                if (!transaccionCollectionOld.contains(transaccionCollectionNewTransaccion)) {
                    FacturaAutopagos oldFacturaAutopagosIdfacturaAutopagosOfTransaccionCollectionNewTransaccion = transaccionCollectionNewTransaccion.getFacturaAutopagosIdfacturaAutopagos();
                    transaccionCollectionNewTransaccion.setFacturaAutopagosIdfacturaAutopagos(facturaAutopagos);
                    transaccionCollectionNewTransaccion = em.merge(transaccionCollectionNewTransaccion);
                    if (oldFacturaAutopagosIdfacturaAutopagosOfTransaccionCollectionNewTransaccion != null && !oldFacturaAutopagosIdfacturaAutopagosOfTransaccionCollectionNewTransaccion.equals(facturaAutopagos)) {
                        oldFacturaAutopagosIdfacturaAutopagosOfTransaccionCollectionNewTransaccion.getTransaccionCollection().remove(transaccionCollectionNewTransaccion);
                        oldFacturaAutopagosIdfacturaAutopagosOfTransaccionCollectionNewTransaccion = em.merge(oldFacturaAutopagosIdfacturaAutopagosOfTransaccionCollectionNewTransaccion);
                    }
                }
            }
            em.getTransaction().commit();
            return facturaAutopagos;
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = facturaAutopagos.getIdfacturaAutopagos();
                if (findFacturaAutopagos(id) == null) {
                    throw new NonexistentEntityException("The facturaAutopagos with id " + id + " no longer exists.");
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
            FacturaAutopagos facturaAutopagos;
            try {
                facturaAutopagos = em.getReference(FacturaAutopagos.class, id);
                facturaAutopagos.getIdfacturaAutopagos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facturaAutopagos with id " + id + " no longer exists.", enfe);
            }
            Convenios conveniosIdconvenios = facturaAutopagos.getConveniosIdconvenios();
            if (conveniosIdconvenios != null) {
                conveniosIdconvenios.getFacturaAutopagosCollection().remove(facturaAutopagos);
                conveniosIdconvenios = em.merge(conveniosIdconvenios);
            }
            Collection<Transaccion> transaccionCollection = facturaAutopagos.getTransaccionCollection();
            for (Transaccion transaccionCollectionTransaccion : transaccionCollection) {
                transaccionCollectionTransaccion.setFacturaAutopagosIdfacturaAutopagos(null);
                transaccionCollectionTransaccion = em.merge(transaccionCollectionTransaccion);
            }
            em.remove(facturaAutopagos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FacturaAutopagos> findFacturaAutopagosEntities() {
        return findFacturaAutopagosEntities(true, -1, -1);
    }

    public List<FacturaAutopagos> findFacturaAutopagosEntities(int maxResults, int firstResult) {
        return findFacturaAutopagosEntities(false, maxResults, firstResult);
    }

    private List<FacturaAutopagos> findFacturaAutopagosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FacturaAutopagos.class));
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

    public FacturaAutopagos findFacturaAutopagos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FacturaAutopagos.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaAutopagosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FacturaAutopagos> rt = cq.from(FacturaAutopagos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public int getFacturasPagadas() {
        EntityManager em = getEntityManager();
        String queryString = "SELECT count(*) as suma FROM `factura_autopagos` WHERE `estado_idestado`=2 ";
        Query query = em.createNativeQuery(queryString);
        int objects = 0;
        try {
            objects = Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;
    }

    public double getTotalFacturado() {
        EntityManager em = getEntityManager();
        String queryString = "SELECT SUM(valor) as suma FROM `factura_autopagos` WHERE 1";
        Query query = em.createNativeQuery(queryString);
        double objects = 0;
        try {
            objects = Double.parseDouble(query.getSingleResult().toString());
        } catch (Exception e) {
        }
        return objects;
    }

    public double getTotalIVA() {
        EntityManager em = getEntityManager();
        String queryString = "SELECT SUM(iva) as suma FROM `factura_autopagos` WHERE 1";
        Query query = em.createNativeQuery(queryString);
        double objects = 0;
        try {
            objects = Double.parseDouble(query.getSingleResult().toString());
        } catch (Exception e) {
        }
        return objects;
    }

    public ArrayList<FacturaAutopagos> getFacturasPendientes() {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `factura_autopagos` WHERE `estado_idestado`=1 ";
        Query query = em.createNativeQuery(queryString, FacturaAutopagos.class);
        ArrayList<FacturaAutopagos> result = new ArrayList<>(query.getResultList());
        return result;
    }

}
