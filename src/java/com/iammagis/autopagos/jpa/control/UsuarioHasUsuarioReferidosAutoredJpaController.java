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
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.UsuarioHasUsuarioReferidosAutored;
import com.iammagis.autopagos.jpa.beans.UsuarioHasUsuarioReferidosAutoredPK;
import com.iammagis.autopagos.jpa.control.exceptions.NonexistentEntityException;
import com.iammagis.autopagos.jpa.control.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Usuario
 */
public class UsuarioHasUsuarioReferidosAutoredJpaController implements Serializable {

    public UsuarioHasUsuarioReferidosAutoredJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuarioHasUsuarioReferidosAutored usuarioHasUsuarioReferidosAutored) throws PreexistingEntityException, Exception {
        if (usuarioHasUsuarioReferidosAutored.getUsuarioHasUsuarioReferidosAutoredPK() == null) {
            usuarioHasUsuarioReferidosAutored.setUsuarioHasUsuarioReferidosAutoredPK(new UsuarioHasUsuarioReferidosAutoredPK());
        }
        usuarioHasUsuarioReferidosAutored.getUsuarioHasUsuarioReferidosAutoredPK().setUsuarioidUsuarioreferido(usuarioHasUsuarioReferidosAutored.getUsuario().getIdUsuario());
        usuarioHasUsuarioReferidosAutored.getUsuarioHasUsuarioReferidosAutoredPK().setUsuarioidUsuarioreferente(usuarioHasUsuarioReferidosAutored.getUsuario1().getIdUsuario());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario = usuarioHasUsuarioReferidosAutored.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getIdUsuario());
                usuarioHasUsuarioReferidosAutored.setUsuario(usuario);
            }
            Usuario usuario1 = usuarioHasUsuarioReferidosAutored.getUsuario1();
            if (usuario1 != null) {
                usuario1 = em.getReference(usuario1.getClass(), usuario1.getIdUsuario());
                usuarioHasUsuarioReferidosAutored.setUsuario1(usuario1);
            }
            em.persist(usuarioHasUsuarioReferidosAutored);
            if (usuario != null) {
                usuario.getUsuarioHasUsuarioReferidosAutoredCollection().add(usuarioHasUsuarioReferidosAutored);
                usuario = em.merge(usuario);
            }
            if (usuario1 != null) {
                usuario1.getUsuarioHasUsuarioReferidosAutoredCollection().add(usuarioHasUsuarioReferidosAutored);
                usuario1 = em.merge(usuario1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarioHasUsuarioReferidosAutored(usuarioHasUsuarioReferidosAutored.getUsuarioHasUsuarioReferidosAutoredPK()) != null) {
                throw new PreexistingEntityException("UsuarioHasUsuarioReferidosAutored " + usuarioHasUsuarioReferidosAutored + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuarioHasUsuarioReferidosAutored usuarioHasUsuarioReferidosAutored) throws NonexistentEntityException, Exception {
        usuarioHasUsuarioReferidosAutored.getUsuarioHasUsuarioReferidosAutoredPK().setUsuarioidUsuarioreferido(usuarioHasUsuarioReferidosAutored.getUsuario().getIdUsuario());
        usuarioHasUsuarioReferidosAutored.getUsuarioHasUsuarioReferidosAutoredPK().setUsuarioidUsuarioreferente(usuarioHasUsuarioReferidosAutored.getUsuario1().getIdUsuario());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioHasUsuarioReferidosAutored persistentUsuarioHasUsuarioReferidosAutored = em.find(UsuarioHasUsuarioReferidosAutored.class, usuarioHasUsuarioReferidosAutored.getUsuarioHasUsuarioReferidosAutoredPK());
            Usuario usuarioOld = persistentUsuarioHasUsuarioReferidosAutored.getUsuario();
            Usuario usuarioNew = usuarioHasUsuarioReferidosAutored.getUsuario();
            Usuario usuario1Old = persistentUsuarioHasUsuarioReferidosAutored.getUsuario1();
            Usuario usuario1New = usuarioHasUsuarioReferidosAutored.getUsuario1();
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getIdUsuario());
                usuarioHasUsuarioReferidosAutored.setUsuario(usuarioNew);
            }
            if (usuario1New != null) {
                usuario1New = em.getReference(usuario1New.getClass(), usuario1New.getIdUsuario());
                usuarioHasUsuarioReferidosAutored.setUsuario1(usuario1New);
            }
            usuarioHasUsuarioReferidosAutored = em.merge(usuarioHasUsuarioReferidosAutored);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getUsuarioHasUsuarioReferidosAutoredCollection().remove(usuarioHasUsuarioReferidosAutored);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getUsuarioHasUsuarioReferidosAutoredCollection().add(usuarioHasUsuarioReferidosAutored);
                usuarioNew = em.merge(usuarioNew);
            }
            if (usuario1Old != null && !usuario1Old.equals(usuario1New)) {
                usuario1Old.getUsuarioHasUsuarioReferidosAutoredCollection().remove(usuarioHasUsuarioReferidosAutored);
                usuario1Old = em.merge(usuario1Old);
            }
            if (usuario1New != null && !usuario1New.equals(usuario1Old)) {
                usuario1New.getUsuarioHasUsuarioReferidosAutoredCollection().add(usuarioHasUsuarioReferidosAutored);
                usuario1New = em.merge(usuario1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                UsuarioHasUsuarioReferidosAutoredPK id = usuarioHasUsuarioReferidosAutored.getUsuarioHasUsuarioReferidosAutoredPK();
                if (findUsuarioHasUsuarioReferidosAutored(id) == null) {
                    throw new NonexistentEntityException("The usuarioHasUsuarioReferidosAutored with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(UsuarioHasUsuarioReferidosAutoredPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioHasUsuarioReferidosAutored usuarioHasUsuarioReferidosAutored;
            try {
                usuarioHasUsuarioReferidosAutored = em.getReference(UsuarioHasUsuarioReferidosAutored.class, id);
                usuarioHasUsuarioReferidosAutored.getUsuarioHasUsuarioReferidosAutoredPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioHasUsuarioReferidosAutored with id " + id + " no longer exists.", enfe);
            }
            Usuario usuario = usuarioHasUsuarioReferidosAutored.getUsuario();
            if (usuario != null) {
                usuario.getUsuarioHasUsuarioReferidosAutoredCollection().remove(usuarioHasUsuarioReferidosAutored);
                usuario = em.merge(usuario);
            }
            Usuario usuario1 = usuarioHasUsuarioReferidosAutored.getUsuario1();
            if (usuario1 != null) {
                usuario1.getUsuarioHasUsuarioReferidosAutoredCollection().remove(usuarioHasUsuarioReferidosAutored);
                usuario1 = em.merge(usuario1);
            }
            em.remove(usuarioHasUsuarioReferidosAutored);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuarioHasUsuarioReferidosAutored> findUsuarioHasUsuarioReferidosAutoredEntities() {
        return findUsuarioHasUsuarioReferidosAutoredEntities(true, -1, -1);
    }

    public List<UsuarioHasUsuarioReferidosAutored> findUsuarioHasUsuarioReferidosAutoredEntities(int maxResults, int firstResult) {
        return findUsuarioHasUsuarioReferidosAutoredEntities(false, maxResults, firstResult);
    }

    private List<UsuarioHasUsuarioReferidosAutored> findUsuarioHasUsuarioReferidosAutoredEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioHasUsuarioReferidosAutored.class));
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

    public UsuarioHasUsuarioReferidosAutored findUsuarioHasUsuarioReferidosAutored(UsuarioHasUsuarioReferidosAutoredPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioHasUsuarioReferidosAutored.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioHasUsuarioReferidosAutoredCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioHasUsuarioReferidosAutored> rt = cq.from(UsuarioHasUsuarioReferidosAutored.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public UsuarioHasUsuarioReferidosAutored findByToken(String codigo) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `usuario_has_usuario_referidos_autored` WHERE `codigo_referido` = '"+codigo+"';";
        Query query = em.createNativeQuery(queryString,UsuarioHasUsuarioReferidosAutored.class);
        UsuarioHasUsuarioReferidosAutored objects = null;
        try {
            objects = (UsuarioHasUsuarioReferidosAutored)query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;
    }
    
}
