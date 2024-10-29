/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.control;

import com.iammagis.autopagos.jpa.beans.Codigo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.UsuarioHasUsuarioReferidosAutored;
import com.iammagis.autopagos.jpa.control.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Usuario
 */
public class CodigoJpaController implements Serializable {

    public CodigoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Codigo codigo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuarioidUsuario = codigo.getUsuarioidUsuario();
            if (usuarioidUsuario != null) {
                usuarioidUsuario = em.getReference(usuarioidUsuario.getClass(), usuarioidUsuario.getIdUsuario());
                codigo.setUsuarioidUsuario(usuarioidUsuario);
            }
            em.persist(codigo);
            if (usuarioidUsuario != null) {
                usuarioidUsuario.getCodigoCollection().add(codigo);
                usuarioidUsuario = em.merge(usuarioidUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Codigo codigo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Codigo persistentCodigo = em.find(Codigo.class, codigo.getIdcodigo());
            Usuario usuarioidUsuarioOld = persistentCodigo.getUsuarioidUsuario();
            Usuario usuarioidUsuarioNew = codigo.getUsuarioidUsuario();
            if (usuarioidUsuarioNew != null) {
                usuarioidUsuarioNew = em.getReference(usuarioidUsuarioNew.getClass(), usuarioidUsuarioNew.getIdUsuario());
                codigo.setUsuarioidUsuario(usuarioidUsuarioNew);
            }
            codigo = em.merge(codigo);
            if (usuarioidUsuarioOld != null && !usuarioidUsuarioOld.equals(usuarioidUsuarioNew)) {
                usuarioidUsuarioOld.getCodigoCollection().remove(codigo);
                usuarioidUsuarioOld = em.merge(usuarioidUsuarioOld);
            }
            if (usuarioidUsuarioNew != null && !usuarioidUsuarioNew.equals(usuarioidUsuarioOld)) {
                usuarioidUsuarioNew.getCodigoCollection().add(codigo);
                usuarioidUsuarioNew = em.merge(usuarioidUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = codigo.getIdcodigo();
                if (findCodigo(id) == null) {
                    throw new NonexistentEntityException("The codigo with id " + id + " no longer exists.");
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
            Codigo codigo;
            try {
                codigo = em.getReference(Codigo.class, id);
                codigo.getIdcodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The codigo with id " + id + " no longer exists.", enfe);
            }
            Usuario usuarioidUsuario = codigo.getUsuarioidUsuario();
            if (usuarioidUsuario != null) {
                usuarioidUsuario.getCodigoCollection().remove(codigo);
                usuarioidUsuario = em.merge(usuarioidUsuario);
            }
            em.remove(codigo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Codigo> findCodigoEntities() {
        return findCodigoEntities(true, -1, -1);
    }

    public List<Codigo> findCodigoEntities(int maxResults, int firstResult) {
        return findCodigoEntities(false, maxResults, firstResult);
    }

    private List<Codigo> findCodigoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Codigo.class));
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

    public Codigo findCodigo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Codigo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCodigoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Codigo> rt = cq.from(Codigo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Codigo getCodigoByString(String codigoString) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `codigo` WHERE `codigo` = '" + codigoString + "' AND usado = 0;";
        Query query = em.createNativeQuery(queryString, Codigo.class);
        Codigo objects = null;
        try {
            objects = (Codigo) query.getSingleResult();
        } catch (Exception e) {
        }
        return objects;
    }

    public Codigo getCodigoByUsuario(Long idUsuario) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `codigo` WHERE `usuario_idUsuario` = '" + idUsuario + "' AND usado = 0;";
        Query query = em.createNativeQuery(queryString, Codigo.class);
        Codigo objects = null;
        try {
            ArrayList<Codigo> codigos = new ArrayList<>(query.getResultList());
            if (!codigos.isEmpty()) {
                objects = codigos.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;
    }

}
