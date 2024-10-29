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
import com.iammagis.autopagos.jpa.beans.Modulo;
import com.iammagis.autopagos.jpa.beans.SubModulo;
import com.iammagis.autopagos.jpa.beans.Usuario;
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
public class SubModuloJpaController implements Serializable {

    public SubModuloJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SubModulo subModulo) {
        if (subModulo.getUsuarioCollection() == null) {
            subModulo.setUsuarioCollection(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Modulo moduloIdmodulo = subModulo.getModuloIdmodulo();
            if (moduloIdmodulo != null) {
                moduloIdmodulo = em.getReference(moduloIdmodulo.getClass(), moduloIdmodulo.getIdmodulo());
                subModulo.setModuloIdmodulo(moduloIdmodulo);
            }
            Collection<Usuario> attachedUsuarioCollection = new ArrayList<Usuario>();
            for (Usuario usuarioCollectionUsuarioToAttach : subModulo.getUsuarioCollection()) {
                usuarioCollectionUsuarioToAttach = em.getReference(usuarioCollectionUsuarioToAttach.getClass(), usuarioCollectionUsuarioToAttach.getIdUsuario());
                attachedUsuarioCollection.add(usuarioCollectionUsuarioToAttach);
            }
            subModulo.setUsuarioCollection(attachedUsuarioCollection);
            em.persist(subModulo);
            if (moduloIdmodulo != null) {
                moduloIdmodulo.getSubModuloCollection().add(subModulo);
                moduloIdmodulo = em.merge(moduloIdmodulo);
            }
            for (Usuario usuarioCollectionUsuario : subModulo.getUsuarioCollection()) {
                usuarioCollectionUsuario.getSubModuloCollection().add(subModulo);
                usuarioCollectionUsuario = em.merge(usuarioCollectionUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SubModulo subModulo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SubModulo persistentSubModulo = em.find(SubModulo.class, subModulo.getIdsubModulo());
            Modulo moduloIdmoduloOld = persistentSubModulo.getModuloIdmodulo();
            Modulo moduloIdmoduloNew = subModulo.getModuloIdmodulo();
            Collection<Usuario> usuarioCollectionOld = persistentSubModulo.getUsuarioCollection();
            Collection<Usuario> usuarioCollectionNew = subModulo.getUsuarioCollection();
            if (moduloIdmoduloNew != null) {
                moduloIdmoduloNew = em.getReference(moduloIdmoduloNew.getClass(), moduloIdmoduloNew.getIdmodulo());
                subModulo.setModuloIdmodulo(moduloIdmoduloNew);
            }
            Collection<Usuario> attachedUsuarioCollectionNew = new ArrayList<Usuario>();
            for (Usuario usuarioCollectionNewUsuarioToAttach : usuarioCollectionNew) {
                usuarioCollectionNewUsuarioToAttach = em.getReference(usuarioCollectionNewUsuarioToAttach.getClass(), usuarioCollectionNewUsuarioToAttach.getIdUsuario());
                attachedUsuarioCollectionNew.add(usuarioCollectionNewUsuarioToAttach);
            }
            usuarioCollectionNew = attachedUsuarioCollectionNew;
            subModulo.setUsuarioCollection(usuarioCollectionNew);
            subModulo = em.merge(subModulo);
            if (moduloIdmoduloOld != null && !moduloIdmoduloOld.equals(moduloIdmoduloNew)) {
                moduloIdmoduloOld.getSubModuloCollection().remove(subModulo);
                moduloIdmoduloOld = em.merge(moduloIdmoduloOld);
            }
            if (moduloIdmoduloNew != null && !moduloIdmoduloNew.equals(moduloIdmoduloOld)) {
                moduloIdmoduloNew.getSubModuloCollection().add(subModulo);
                moduloIdmoduloNew = em.merge(moduloIdmoduloNew);
            }
            for (Usuario usuarioCollectionOldUsuario : usuarioCollectionOld) {
                if (!usuarioCollectionNew.contains(usuarioCollectionOldUsuario)) {
                    usuarioCollectionOldUsuario.getSubModuloCollection().remove(subModulo);
                    usuarioCollectionOldUsuario = em.merge(usuarioCollectionOldUsuario);
                }
            }
            for (Usuario usuarioCollectionNewUsuario : usuarioCollectionNew) {
                if (!usuarioCollectionOld.contains(usuarioCollectionNewUsuario)) {
                    usuarioCollectionNewUsuario.getSubModuloCollection().add(subModulo);
                    usuarioCollectionNewUsuario = em.merge(usuarioCollectionNewUsuario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = subModulo.getIdsubModulo();
                if (findSubModulo(id) == null) {
                    throw new NonexistentEntityException("The subModulo with id " + id + " no longer exists.");
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
            SubModulo subModulo;
            try {
                subModulo = em.getReference(SubModulo.class, id);
                subModulo.getIdsubModulo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subModulo with id " + id + " no longer exists.", enfe);
            }
            Modulo moduloIdmodulo = subModulo.getModuloIdmodulo();
            if (moduloIdmodulo != null) {
                moduloIdmodulo.getSubModuloCollection().remove(subModulo);
                moduloIdmodulo = em.merge(moduloIdmodulo);
            }
            Collection<Usuario> usuarioCollection = subModulo.getUsuarioCollection();
            for (Usuario usuarioCollectionUsuario : usuarioCollection) {
                usuarioCollectionUsuario.getSubModuloCollection().remove(subModulo);
                usuarioCollectionUsuario = em.merge(usuarioCollectionUsuario);
            }
            em.remove(subModulo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SubModulo> findSubModuloEntities() {
        return findSubModuloEntities(true, -1, -1);
    }

    public List<SubModulo> findSubModuloEntities(int maxResults, int firstResult) {
        return findSubModuloEntities(false, maxResults, firstResult);
    }

    private List<SubModulo> findSubModuloEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SubModulo.class));
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

    public SubModulo findSubModulo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SubModulo.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubModuloCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SubModulo> rt = cq.from(SubModulo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
