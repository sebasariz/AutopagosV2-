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
import com.iammagis.autopagos.jpa.beans.TipoUsuario;
import com.iammagis.autopagos.jpa.beans.Modulo;
import java.util.ArrayList;
import java.util.Collection;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.SubModulo;
import com.iammagis.autopagos.jpa.beans.Notificacion;
import com.iammagis.autopagos.jpa.beans.Dispositivo;
import com.iammagis.autopagos.jpa.beans.LogIngreso;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.control.exceptions.IllegalOrphanException;
import com.iammagis.autopagos.jpa.control.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

/**
 *
 * @author Usuario
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Usuario create(Usuario usuario) {
        if (usuario.getModuloCollection() == null) {
            usuario.setModuloCollection(new ArrayList<Modulo>());
        }
        if (usuario.getFacturaCollection() == null) {
            usuario.setFacturaCollection(new ArrayList<Factura>());
        }
        if (usuario.getSubModuloCollection() == null) {
            usuario.setSubModuloCollection(new ArrayList<SubModulo>());
        }
        if (usuario.getNotificacionCollection() == null) {
            usuario.setNotificacionCollection(new ArrayList<Notificacion>());
        }
        if (usuario.getDispositivoCollection() == null) {
            usuario.setDispositivoCollection(new ArrayList<Dispositivo>());
        }
        if (usuario.getLogIngresoCollection() == null) {
            usuario.setLogIngresoCollection(new ArrayList<LogIngreso>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoUsuario tipousuarioIDTipoUsuario = usuario.getTipousuarioIDTipoUsuario();
            if (tipousuarioIDTipoUsuario != null) {
                tipousuarioIDTipoUsuario = em.getReference(tipousuarioIDTipoUsuario.getClass(), tipousuarioIDTipoUsuario.getIDTipoUsuario());
                usuario.setTipousuarioIDTipoUsuario(tipousuarioIDTipoUsuario);
            }
            Collection<Modulo> attachedModuloCollection = new ArrayList<Modulo>();
            for (Modulo moduloCollectionModuloToAttach : usuario.getModuloCollection()) {
                moduloCollectionModuloToAttach = em.getReference(moduloCollectionModuloToAttach.getClass(), moduloCollectionModuloToAttach.getIdmodulo());
                attachedModuloCollection.add(moduloCollectionModuloToAttach);
            }
            usuario.setModuloCollection(attachedModuloCollection);
            Collection<Factura> attachedFacturaCollection = new ArrayList<Factura>();
            for (Factura facturaCollectionFacturaToAttach : usuario.getFacturaCollection()) {
                facturaCollectionFacturaToAttach = em.getReference(facturaCollectionFacturaToAttach.getClass(), facturaCollectionFacturaToAttach.getIdfactura());
                attachedFacturaCollection.add(facturaCollectionFacturaToAttach);
            }
            usuario.setFacturaCollection(attachedFacturaCollection);
            Collection<SubModulo> attachedSubModuloCollection = new ArrayList<SubModulo>();
            for (SubModulo subModuloCollectionSubModuloToAttach : usuario.getSubModuloCollection()) {
                subModuloCollectionSubModuloToAttach = em.getReference(subModuloCollectionSubModuloToAttach.getClass(), subModuloCollectionSubModuloToAttach.getIdsubModulo());
                attachedSubModuloCollection.add(subModuloCollectionSubModuloToAttach);
            }
            usuario.setSubModuloCollection(attachedSubModuloCollection);
            Collection<Notificacion> attachedNotificacionCollection = new ArrayList<Notificacion>();
            for (Notificacion notificacionCollectionNotificacionToAttach : usuario.getNotificacionCollection()) {
                notificacionCollectionNotificacionToAttach = em.getReference(notificacionCollectionNotificacionToAttach.getClass(), notificacionCollectionNotificacionToAttach.getIdnotificacion());
                attachedNotificacionCollection.add(notificacionCollectionNotificacionToAttach);
            }
            usuario.setNotificacionCollection(attachedNotificacionCollection);
            Collection<Dispositivo> attachedDispositivoCollection = new ArrayList<Dispositivo>();
            for (Dispositivo dispositivoCollectionDispositivoToAttach : usuario.getDispositivoCollection()) {
                dispositivoCollectionDispositivoToAttach = em.getReference(dispositivoCollectionDispositivoToAttach.getClass(), dispositivoCollectionDispositivoToAttach.getIddispositivo());
                attachedDispositivoCollection.add(dispositivoCollectionDispositivoToAttach);
            }
            usuario.setDispositivoCollection(attachedDispositivoCollection);
            Collection<LogIngreso> attachedLogIngresoCollection = new ArrayList<LogIngreso>();
            for (LogIngreso logIngresoCollectionLogIngresoToAttach : usuario.getLogIngresoCollection()) {
                logIngresoCollectionLogIngresoToAttach = em.getReference(logIngresoCollectionLogIngresoToAttach.getClass(), logIngresoCollectionLogIngresoToAttach.getIdlogIngreso());
                attachedLogIngresoCollection.add(logIngresoCollectionLogIngresoToAttach);
            }
            usuario.setLogIngresoCollection(attachedLogIngresoCollection);
            em.persist(usuario);
            if (tipousuarioIDTipoUsuario != null) {
                tipousuarioIDTipoUsuario.getUsuarioCollection().add(usuario);
                tipousuarioIDTipoUsuario = em.merge(tipousuarioIDTipoUsuario);
            }
            for (Modulo moduloCollectionModulo : usuario.getModuloCollection()) {
                moduloCollectionModulo.getUsuarioCollection().add(usuario);
                moduloCollectionModulo = em.merge(moduloCollectionModulo);
            }
            for (Factura facturaCollectionFactura : usuario.getFacturaCollection()) {
                facturaCollectionFactura.getUsuarioCollection().add(usuario);
                facturaCollectionFactura = em.merge(facturaCollectionFactura);
            }
            for (SubModulo subModuloCollectionSubModulo : usuario.getSubModuloCollection()) {
                subModuloCollectionSubModulo.getUsuarioCollection().add(usuario);
                subModuloCollectionSubModulo = em.merge(subModuloCollectionSubModulo);
            }
            for (Notificacion notificacionCollectionNotificacion : usuario.getNotificacionCollection()) {
                Usuario oldUsuarioidUsuarioOfNotificacionCollectionNotificacion = notificacionCollectionNotificacion.getUsuarioidUsuario();
                notificacionCollectionNotificacion.setUsuarioidUsuario(usuario);
                notificacionCollectionNotificacion = em.merge(notificacionCollectionNotificacion);
                if (oldUsuarioidUsuarioOfNotificacionCollectionNotificacion != null) {
                    oldUsuarioidUsuarioOfNotificacionCollectionNotificacion.getNotificacionCollection().remove(notificacionCollectionNotificacion);
                    oldUsuarioidUsuarioOfNotificacionCollectionNotificacion = em.merge(oldUsuarioidUsuarioOfNotificacionCollectionNotificacion);
                }
            }
            for (Dispositivo dispositivoCollectionDispositivo : usuario.getDispositivoCollection()) {
                Usuario oldUsuarioidUsuarioOfDispositivoCollectionDispositivo = dispositivoCollectionDispositivo.getUsuarioidUsuario();
                dispositivoCollectionDispositivo.setUsuarioidUsuario(usuario);
                dispositivoCollectionDispositivo = em.merge(dispositivoCollectionDispositivo);
                if (oldUsuarioidUsuarioOfDispositivoCollectionDispositivo != null) {
                    oldUsuarioidUsuarioOfDispositivoCollectionDispositivo.getDispositivoCollection().remove(dispositivoCollectionDispositivo);
                    oldUsuarioidUsuarioOfDispositivoCollectionDispositivo = em.merge(oldUsuarioidUsuarioOfDispositivoCollectionDispositivo);
                }
            }
            for (LogIngreso logIngresoCollectionLogIngreso : usuario.getLogIngresoCollection()) {
                Usuario oldUsuarioidUsuarioOfLogIngresoCollectionLogIngreso = logIngresoCollectionLogIngreso.getUsuarioidUsuario();
                logIngresoCollectionLogIngreso.setUsuarioidUsuario(usuario);
                logIngresoCollectionLogIngreso = em.merge(logIngresoCollectionLogIngreso);
                if (oldUsuarioidUsuarioOfLogIngresoCollectionLogIngreso != null) {
                    oldUsuarioidUsuarioOfLogIngresoCollectionLogIngreso.getLogIngresoCollection().remove(logIngresoCollectionLogIngreso);
                    oldUsuarioidUsuarioOfLogIngresoCollectionLogIngreso = em.merge(oldUsuarioidUsuarioOfLogIngresoCollectionLogIngreso);
                }
            }
            em.getTransaction().commit();
            return usuario;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Usuario edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            TipoUsuario tipousuarioIDTipoUsuarioOld = persistentUsuario.getTipousuarioIDTipoUsuario();
            TipoUsuario tipousuarioIDTipoUsuarioNew = usuario.getTipousuarioIDTipoUsuario();
            Collection<Modulo> moduloCollectionOld = persistentUsuario.getModuloCollection();
            Collection<Modulo> moduloCollectionNew = usuario.getModuloCollection();
            Collection<Factura> facturaCollectionOld = persistentUsuario.getFacturaCollection();
            Collection<Factura> facturaCollectionNew = usuario.getFacturaCollection();
            Collection<SubModulo> subModuloCollectionOld = persistentUsuario.getSubModuloCollection();
            Collection<SubModulo> subModuloCollectionNew = usuario.getSubModuloCollection();
            Collection<Notificacion> notificacionCollectionOld = persistentUsuario.getNotificacionCollection();
            Collection<Notificacion> notificacionCollectionNew = usuario.getNotificacionCollection();
            Collection<Dispositivo> dispositivoCollectionOld = persistentUsuario.getDispositivoCollection();
            Collection<Dispositivo> dispositivoCollectionNew = usuario.getDispositivoCollection();
            Collection<LogIngreso> logIngresoCollectionOld = persistentUsuario.getLogIngresoCollection();
            Collection<LogIngreso> logIngresoCollectionNew = usuario.getLogIngresoCollection();
            List<String> illegalOrphanMessages = null;
            for (Notificacion notificacionCollectionOldNotificacion : notificacionCollectionOld) {
                if (!notificacionCollectionNew.contains(notificacionCollectionOldNotificacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Notificacion " + notificacionCollectionOldNotificacion + " since its usuarioidUsuario field is not nullable.");
                }
            }
            for (Dispositivo dispositivoCollectionOldDispositivo : dispositivoCollectionOld) {
                if (!dispositivoCollectionNew.contains(dispositivoCollectionOldDispositivo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Dispositivo " + dispositivoCollectionOldDispositivo + " since its usuarioidUsuario field is not nullable.");
                }
            }
            for (LogIngreso logIngresoCollectionOldLogIngreso : logIngresoCollectionOld) {
                if (!logIngresoCollectionNew.contains(logIngresoCollectionOldLogIngreso)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain LogIngreso " + logIngresoCollectionOldLogIngreso + " since its usuarioidUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tipousuarioIDTipoUsuarioNew != null) {
                tipousuarioIDTipoUsuarioNew = em.getReference(tipousuarioIDTipoUsuarioNew.getClass(), tipousuarioIDTipoUsuarioNew.getIDTipoUsuario());
                usuario.setTipousuarioIDTipoUsuario(tipousuarioIDTipoUsuarioNew);
            }
            Collection<Modulo> attachedModuloCollectionNew = new ArrayList<Modulo>();
            for (Modulo moduloCollectionNewModuloToAttach : moduloCollectionNew) {
                moduloCollectionNewModuloToAttach = em.getReference(moduloCollectionNewModuloToAttach.getClass(), moduloCollectionNewModuloToAttach.getIdmodulo());
                attachedModuloCollectionNew.add(moduloCollectionNewModuloToAttach);
            }
            moduloCollectionNew = attachedModuloCollectionNew;
            usuario.setModuloCollection(moduloCollectionNew);
            Collection<Factura> attachedFacturaCollectionNew = new ArrayList<Factura>();
            for (Factura facturaCollectionNewFacturaToAttach : facturaCollectionNew) {
                facturaCollectionNewFacturaToAttach = em.getReference(facturaCollectionNewFacturaToAttach.getClass(), facturaCollectionNewFacturaToAttach.getIdfactura());
                attachedFacturaCollectionNew.add(facturaCollectionNewFacturaToAttach);
            }
            facturaCollectionNew = attachedFacturaCollectionNew;
            usuario.setFacturaCollection(facturaCollectionNew);
            Collection<SubModulo> attachedSubModuloCollectionNew = new ArrayList<SubModulo>();
            for (SubModulo subModuloCollectionNewSubModuloToAttach : subModuloCollectionNew) {
                subModuloCollectionNewSubModuloToAttach = em.getReference(subModuloCollectionNewSubModuloToAttach.getClass(), subModuloCollectionNewSubModuloToAttach.getIdsubModulo());
                attachedSubModuloCollectionNew.add(subModuloCollectionNewSubModuloToAttach);
            }
            subModuloCollectionNew = attachedSubModuloCollectionNew;
            usuario.setSubModuloCollection(subModuloCollectionNew);
            Collection<Notificacion> attachedNotificacionCollectionNew = new ArrayList<Notificacion>();
            for (Notificacion notificacionCollectionNewNotificacionToAttach : notificacionCollectionNew) {
                notificacionCollectionNewNotificacionToAttach = em.getReference(notificacionCollectionNewNotificacionToAttach.getClass(), notificacionCollectionNewNotificacionToAttach.getIdnotificacion());
                attachedNotificacionCollectionNew.add(notificacionCollectionNewNotificacionToAttach);
            }
            notificacionCollectionNew = attachedNotificacionCollectionNew;
            usuario.setNotificacionCollection(notificacionCollectionNew);
            Collection<Dispositivo> attachedDispositivoCollectionNew = new ArrayList<Dispositivo>();
            for (Dispositivo dispositivoCollectionNewDispositivoToAttach : dispositivoCollectionNew) {
                dispositivoCollectionNewDispositivoToAttach = em.getReference(dispositivoCollectionNewDispositivoToAttach.getClass(), dispositivoCollectionNewDispositivoToAttach.getIddispositivo());
                attachedDispositivoCollectionNew.add(dispositivoCollectionNewDispositivoToAttach);
            }
            dispositivoCollectionNew = attachedDispositivoCollectionNew;
            usuario.setDispositivoCollection(dispositivoCollectionNew);
            Collection<LogIngreso> attachedLogIngresoCollectionNew = new ArrayList<LogIngreso>();
            for (LogIngreso logIngresoCollectionNewLogIngresoToAttach : logIngresoCollectionNew) {
                logIngresoCollectionNewLogIngresoToAttach = em.getReference(logIngresoCollectionNewLogIngresoToAttach.getClass(), logIngresoCollectionNewLogIngresoToAttach.getIdlogIngreso());
                attachedLogIngresoCollectionNew.add(logIngresoCollectionNewLogIngresoToAttach);
            }
            logIngresoCollectionNew = attachedLogIngresoCollectionNew;
            usuario.setLogIngresoCollection(logIngresoCollectionNew);
            usuario = em.merge(usuario);
            if (tipousuarioIDTipoUsuarioOld != null && !tipousuarioIDTipoUsuarioOld.equals(tipousuarioIDTipoUsuarioNew)) {
                tipousuarioIDTipoUsuarioOld.getUsuarioCollection().remove(usuario);
                tipousuarioIDTipoUsuarioOld = em.merge(tipousuarioIDTipoUsuarioOld);
            }
            if (tipousuarioIDTipoUsuarioNew != null && !tipousuarioIDTipoUsuarioNew.equals(tipousuarioIDTipoUsuarioOld)) {
                tipousuarioIDTipoUsuarioNew.getUsuarioCollection().add(usuario);
                tipousuarioIDTipoUsuarioNew = em.merge(tipousuarioIDTipoUsuarioNew);
            }
            for (Modulo moduloCollectionOldModulo : moduloCollectionOld) {
                if (!moduloCollectionNew.contains(moduloCollectionOldModulo)) {
                    moduloCollectionOldModulo.getUsuarioCollection().remove(usuario);
                    moduloCollectionOldModulo = em.merge(moduloCollectionOldModulo);
                }
            }
            for (Modulo moduloCollectionNewModulo : moduloCollectionNew) {
                if (!moduloCollectionOld.contains(moduloCollectionNewModulo)) {
                    moduloCollectionNewModulo.getUsuarioCollection().add(usuario);
                    moduloCollectionNewModulo = em.merge(moduloCollectionNewModulo);
                }
            }
            for (Factura facturaCollectionOldFactura : facturaCollectionOld) {
                if (!facturaCollectionNew.contains(facturaCollectionOldFactura)) {
                    facturaCollectionOldFactura.getUsuarioCollection().remove(usuario);
                    facturaCollectionOldFactura = em.merge(facturaCollectionOldFactura);
                }
            }
            for (Factura facturaCollectionNewFactura : facturaCollectionNew) {
                if (!facturaCollectionOld.contains(facturaCollectionNewFactura)) {
                    facturaCollectionNewFactura.getUsuarioCollection().add(usuario);
                    facturaCollectionNewFactura = em.merge(facturaCollectionNewFactura);
                }
            }
            for (SubModulo subModuloCollectionOldSubModulo : subModuloCollectionOld) {
                if (!subModuloCollectionNew.contains(subModuloCollectionOldSubModulo)) {
                    subModuloCollectionOldSubModulo.getUsuarioCollection().remove(usuario);
                    subModuloCollectionOldSubModulo = em.merge(subModuloCollectionOldSubModulo);
                }
            }
            for (SubModulo subModuloCollectionNewSubModulo : subModuloCollectionNew) {
                if (!subModuloCollectionOld.contains(subModuloCollectionNewSubModulo)) {
                    subModuloCollectionNewSubModulo.getUsuarioCollection().add(usuario);
                    subModuloCollectionNewSubModulo = em.merge(subModuloCollectionNewSubModulo);
                }
            }
            for (Notificacion notificacionCollectionNewNotificacion : notificacionCollectionNew) {
                if (!notificacionCollectionOld.contains(notificacionCollectionNewNotificacion)) {
                    Usuario oldUsuarioidUsuarioOfNotificacionCollectionNewNotificacion = notificacionCollectionNewNotificacion.getUsuarioidUsuario();
                    notificacionCollectionNewNotificacion.setUsuarioidUsuario(usuario);
                    notificacionCollectionNewNotificacion = em.merge(notificacionCollectionNewNotificacion);
                    if (oldUsuarioidUsuarioOfNotificacionCollectionNewNotificacion != null && !oldUsuarioidUsuarioOfNotificacionCollectionNewNotificacion.equals(usuario)) {
                        oldUsuarioidUsuarioOfNotificacionCollectionNewNotificacion.getNotificacionCollection().remove(notificacionCollectionNewNotificacion);
                        oldUsuarioidUsuarioOfNotificacionCollectionNewNotificacion = em.merge(oldUsuarioidUsuarioOfNotificacionCollectionNewNotificacion);
                    }
                }
            }
            for (Dispositivo dispositivoCollectionNewDispositivo : dispositivoCollectionNew) {
                if (!dispositivoCollectionOld.contains(dispositivoCollectionNewDispositivo)) {
                    Usuario oldUsuarioidUsuarioOfDispositivoCollectionNewDispositivo = dispositivoCollectionNewDispositivo.getUsuarioidUsuario();
                    dispositivoCollectionNewDispositivo.setUsuarioidUsuario(usuario);
                    dispositivoCollectionNewDispositivo = em.merge(dispositivoCollectionNewDispositivo);
                    if (oldUsuarioidUsuarioOfDispositivoCollectionNewDispositivo != null && !oldUsuarioidUsuarioOfDispositivoCollectionNewDispositivo.equals(usuario)) {
                        oldUsuarioidUsuarioOfDispositivoCollectionNewDispositivo.getDispositivoCollection().remove(dispositivoCollectionNewDispositivo);
                        oldUsuarioidUsuarioOfDispositivoCollectionNewDispositivo = em.merge(oldUsuarioidUsuarioOfDispositivoCollectionNewDispositivo);
                    }
                }
            }
            for (LogIngreso logIngresoCollectionNewLogIngreso : logIngresoCollectionNew) {
                if (!logIngresoCollectionOld.contains(logIngresoCollectionNewLogIngreso)) {
                    Usuario oldUsuarioidUsuarioOfLogIngresoCollectionNewLogIngreso = logIngresoCollectionNewLogIngreso.getUsuarioidUsuario();
                    logIngresoCollectionNewLogIngreso.setUsuarioidUsuario(usuario);
                    logIngresoCollectionNewLogIngreso = em.merge(logIngresoCollectionNewLogIngreso);
                    if (oldUsuarioidUsuarioOfLogIngresoCollectionNewLogIngreso != null && !oldUsuarioidUsuarioOfLogIngresoCollectionNewLogIngreso.equals(usuario)) {
                        oldUsuarioidUsuarioOfLogIngresoCollectionNewLogIngreso.getLogIngresoCollection().remove(logIngresoCollectionNewLogIngreso);
                        oldUsuarioidUsuarioOfLogIngresoCollectionNewLogIngreso = em.merge(oldUsuarioidUsuarioOfLogIngresoCollectionNewLogIngreso);
                    }
                }
            }
            em.getTransaction().commit();
            return usuario;
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Notificacion> notificacionCollectionOrphanCheck = usuario.getNotificacionCollection();
            for (Notificacion notificacionCollectionOrphanCheckNotificacion : notificacionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Notificacion " + notificacionCollectionOrphanCheckNotificacion + " in its notificacionCollection field has a non-nullable usuarioidUsuario field.");
            }
            Collection<Dispositivo> dispositivoCollectionOrphanCheck = usuario.getDispositivoCollection();
            for (Dispositivo dispositivoCollectionOrphanCheckDispositivo : dispositivoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Dispositivo " + dispositivoCollectionOrphanCheckDispositivo + " in its dispositivoCollection field has a non-nullable usuarioidUsuario field.");
            }
            Collection<LogIngreso> logIngresoCollectionOrphanCheck = usuario.getLogIngresoCollection();
            for (LogIngreso logIngresoCollectionOrphanCheckLogIngreso : logIngresoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the LogIngreso " + logIngresoCollectionOrphanCheckLogIngreso + " in its logIngresoCollection field has a non-nullable usuarioidUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoUsuario tipousuarioIDTipoUsuario = usuario.getTipousuarioIDTipoUsuario();
            if (tipousuarioIDTipoUsuario != null) {
                tipousuarioIDTipoUsuario.getUsuarioCollection().remove(usuario);
                tipousuarioIDTipoUsuario = em.merge(tipousuarioIDTipoUsuario);
            }
            Collection<Modulo> moduloCollection = usuario.getModuloCollection();
            for (Modulo moduloCollectionModulo : moduloCollection) {
                moduloCollectionModulo.getUsuarioCollection().remove(usuario);
                moduloCollectionModulo = em.merge(moduloCollectionModulo);
            }
            Collection<Factura> facturaCollection = usuario.getFacturaCollection();
            for (Factura facturaCollectionFactura : facturaCollection) {
                facturaCollectionFactura.getUsuarioCollection().remove(usuario);
                facturaCollectionFactura = em.merge(facturaCollectionFactura);
            }
            Collection<SubModulo> subModuloCollection = usuario.getSubModuloCollection();
            for (SubModulo subModuloCollectionSubModulo : subModuloCollection) {
                subModuloCollectionSubModulo.getUsuarioCollection().remove(usuario);
                subModuloCollectionSubModulo = em.merge(subModuloCollectionSubModulo);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Usuario login(Usuario usuario) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Usuario.login");
        query.setParameter("email", usuario.getEmail());
        query.setParameter("pass", usuario.getPass());
        Usuario usuarioRetrun = null;
        try {
            usuarioRetrun = (Usuario) query.getSingleResult();
        } catch (NoResultException e) {
//            e.printStackTrace();
        }
        return usuarioRetrun;
    }

    public int countUsersAutored() {
        EntityManager em = getEntityManager();
        String queryString = "SELECT count(*) as suma FROM `usuario` WHERE `tipo_usuario_IDTipoUsuario`=4 ";
        Query query = em.createNativeQuery(queryString);
        int objects = 0;
        try {
            objects = Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;

    }

    public int countUsersSiperEInternos() {
        EntityManager em = getEntityManager();
        String queryString = "SELECT count(*) as suma FROM `usuario` WHERE `tipo_usuario_IDTipoUsuario`!=4 ";
        Query query = em.createNativeQuery(queryString);
        int objects = 0;
        try {
            objects = Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;

    }

    public Usuario emailExist(Usuario usuario) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Usuario.findByEmail");
        query.setParameter("email", usuario.getEmail());
        Usuario usuarioRetrun = null;
        try {
            ArrayList<Usuario> usuarios = new ArrayList<Usuario>(query.getResultList());
            if (!usuarios.isEmpty()) {
                usuarioRetrun = usuarios.get(usuarios.size() - 1);
            }
        } catch (NoResultException e) {
        }
        return usuarioRetrun;
    }

    public Usuario findByToken(String token) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Usuario.token");
        query.setParameter("sesionToken", token);
        Usuario usuarioRetrun = null;
        try {
            usuarioRetrun = (Usuario) query.getSingleResult();
        } catch (NoResultException e) {
        }
        return usuarioRetrun;
    }

    public void destoyUsersByConvenio(int idConvenio) {
        String queryString = "";
        queryString = "DELETE FROM `usuario` WHERE `convenios_idconvenios` =" + idConvenio;
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Query query = em.createNativeQuery(queryString);
        query.executeUpdate();
        tx.commit();
    }

    public ArrayList<Usuario> findByNameContains(String search) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `usuario` WHERE (`nombre` LIKE '%" + search + "%' OR `apellidos` LIKE '%" + search + "%' OR `email` LIKE '%" + search + "%') AND `tipo_usuario_IDTipoUsuario` = 4;";
        Query query = em.createNativeQuery(queryString, Usuario.class);
        ArrayList<Usuario> result = new ArrayList<>(query.getResultList());
        return result;
    }

    public Usuario findByTokenUserAndPass(String token, String user, String pass) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `usuario`,`convenios` WHERE `email` = '" + user + "' AND `pass` = '" + pass + "' AND `convenios_idconvenios` = `idconvenios` AND `codigo` = '" + token + "';";
//        System.out.println("queryString: "+queryString);
        Query query = em.createNativeQuery(queryString, Usuario.class);
        ArrayList<Usuario> result = new ArrayList<>(query.getResultList());
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    public ArrayList<Usuario> getUsuariosByTipoAndConvenio(int i, Integer idconvenios) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `usuario`,`convenios` WHERE `tipo_usuario_IDTipoUsuario` = " + i + " AND `tipo_usuario_IDTipoUsuario` = " + idconvenios + ";";
//        System.out.println("queryString: "+queryString);
        Query query = em.createNativeQuery(queryString, Usuario.class);
        ArrayList<Usuario> result = new ArrayList<>(query.getResultList());
        return result;
    }

}
