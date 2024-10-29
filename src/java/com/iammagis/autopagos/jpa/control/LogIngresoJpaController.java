/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.control;

import com.iammagis.autopagos.jpa.beans.LogIngreso;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.control.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 *
 * @author Usuario
 */
public class LogIngresoJpaController implements Serializable {

    public LogIngresoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LogIngreso logIngreso) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuarioidUsuario = logIngreso.getUsuarioidUsuario();
            if (usuarioidUsuario != null) {
                usuarioidUsuario = em.getReference(usuarioidUsuario.getClass(), usuarioidUsuario.getIdUsuario());
                logIngreso.setUsuarioidUsuario(usuarioidUsuario);
            }
            em.persist(logIngreso);
            if (usuarioidUsuario != null) {
                usuarioidUsuario.getLogIngresoCollection().add(logIngreso);
                usuarioidUsuario = em.merge(usuarioidUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LogIngreso logIngreso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LogIngreso persistentLogIngreso = em.find(LogIngreso.class, logIngreso.getIdlogIngreso());
            Usuario usuarioidUsuarioOld = persistentLogIngreso.getUsuarioidUsuario();
            Usuario usuarioidUsuarioNew = logIngreso.getUsuarioidUsuario();
            if (usuarioidUsuarioNew != null) {
                usuarioidUsuarioNew = em.getReference(usuarioidUsuarioNew.getClass(), usuarioidUsuarioNew.getIdUsuario());
                logIngreso.setUsuarioidUsuario(usuarioidUsuarioNew);
            }
            logIngreso = em.merge(logIngreso);
            if (usuarioidUsuarioOld != null && !usuarioidUsuarioOld.equals(usuarioidUsuarioNew)) {
                usuarioidUsuarioOld.getLogIngresoCollection().remove(logIngreso);
                usuarioidUsuarioOld = em.merge(usuarioidUsuarioOld);
            }
            if (usuarioidUsuarioNew != null && !usuarioidUsuarioNew.equals(usuarioidUsuarioOld)) {
                usuarioidUsuarioNew.getLogIngresoCollection().add(logIngreso);
                usuarioidUsuarioNew = em.merge(usuarioidUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = logIngreso.getIdlogIngreso();
                if (findLogIngreso(id) == null) {
                    throw new NonexistentEntityException("The logIngreso with id " + id + " no longer exists.");
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
            LogIngreso logIngreso;
            try {
                logIngreso = em.getReference(LogIngreso.class, id);
                logIngreso.getIdlogIngreso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The logIngreso with id " + id + " no longer exists.", enfe);
            }
            Usuario usuarioidUsuario = logIngreso.getUsuarioidUsuario();
            if (usuarioidUsuario != null) {
                usuarioidUsuario.getLogIngresoCollection().remove(logIngreso);
                usuarioidUsuario = em.merge(usuarioidUsuario);
            }
            em.remove(logIngreso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LogIngreso> findLogIngresoEntities() {
        return findLogIngresoEntities(true, -1, -1);
    }

    public List<LogIngreso> findLogIngresoEntities(int maxResults, int firstResult) {
        return findLogIngresoEntities(false, maxResults, firstResult);
    }

    private List<LogIngreso> findLogIngresoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LogIngreso.class));
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

    public LogIngreso findLogIngreso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LogIngreso.class, id);
        } finally {
            em.close();
        }
    }

    public int getLogIngresoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LogIngreso> rt = cq.from(LogIngreso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void deleteLogsByUser(Long idUsuario) {
        String queryString = "";
        queryString = "DELETE FROM `log_ingreso` WHERE `usuario_idUsuario` =" + idUsuario;
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Query query = em.createNativeQuery(queryString);
        query.executeUpdate();
        tx.commit();
    }
    
}
