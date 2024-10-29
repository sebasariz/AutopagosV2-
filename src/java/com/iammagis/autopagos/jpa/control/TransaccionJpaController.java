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
import com.iammagis.autopagos.jpa.beans.TipoTransaccion;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.ComprobanterecaudoPSE;
import com.iammagis.autopagos.jpa.beans.FacturaAutopagos;
import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.control.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 *
 * @author Usuario
 */
public class TransaccionJpaController implements Serializable {

    public TransaccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Transaccion transaccion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoTransaccion tipoTransaccionidtipoTransaccion = transaccion.getTipoTransaccionidtipoTransaccion();
            if (tipoTransaccionidtipoTransaccion != null) {
                tipoTransaccionidtipoTransaccion = em.getReference(tipoTransaccionidtipoTransaccion.getClass(), tipoTransaccionidtipoTransaccion.getIdtipoTransaccion());
                transaccion.setTipoTransaccionidtipoTransaccion(tipoTransaccionidtipoTransaccion);
            }
            Factura facturaIdfactura = transaccion.getFacturaIdfactura();
            if (facturaIdfactura != null) {
                facturaIdfactura = em.getReference(facturaIdfactura.getClass(), facturaIdfactura.getIdfactura());
                transaccion.setFacturaIdfactura(facturaIdfactura);
            }
            ComprobanterecaudoPSE comprobanteRecaudoPSEidcomprobanteRecaudoPSE = transaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE();
            if (comprobanteRecaudoPSEidcomprobanteRecaudoPSE != null) {
                comprobanteRecaudoPSEidcomprobanteRecaudoPSE = em.getReference(comprobanteRecaudoPSEidcomprobanteRecaudoPSE.getClass(), comprobanteRecaudoPSEidcomprobanteRecaudoPSE.getIdcomprobanteRecaudoPSE());
                transaccion.setComprobanteRecaudoPSEidcomprobanteRecaudoPSE(comprobanteRecaudoPSEidcomprobanteRecaudoPSE);
            }
            FacturaAutopagos facturaAutopagosIdfacturaAutopagos = transaccion.getFacturaAutopagosIdfacturaAutopagos();
            if (facturaAutopagosIdfacturaAutopagos != null) {
                facturaAutopagosIdfacturaAutopagos = em.getReference(facturaAutopagosIdfacturaAutopagos.getClass(), facturaAutopagosIdfacturaAutopagos.getIdfacturaAutopagos());
                transaccion.setFacturaAutopagosIdfacturaAutopagos(facturaAutopagosIdfacturaAutopagos);
            }
            em.persist(transaccion);
            if (tipoTransaccionidtipoTransaccion != null) {
                tipoTransaccionidtipoTransaccion.getTransaccionCollection().add(transaccion);
                tipoTransaccionidtipoTransaccion = em.merge(tipoTransaccionidtipoTransaccion);
            }
            if (facturaIdfactura != null) {
                facturaIdfactura.getTransaccionCollection().add(transaccion);
                facturaIdfactura = em.merge(facturaIdfactura);
            }
            if (comprobanteRecaudoPSEidcomprobanteRecaudoPSE != null) {
                comprobanteRecaudoPSEidcomprobanteRecaudoPSE.getTransaccionCollection().add(transaccion);
                comprobanteRecaudoPSEidcomprobanteRecaudoPSE = em.merge(comprobanteRecaudoPSEidcomprobanteRecaudoPSE);
            }
            if (facturaAutopagosIdfacturaAutopagos != null) {
                facturaAutopagosIdfacturaAutopagos.getTransaccionCollection().add(transaccion);
                facturaAutopagosIdfacturaAutopagos = em.merge(facturaAutopagosIdfacturaAutopagos);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Transaccion transaccion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaccion persistentTransaccion = em.find(Transaccion.class, transaccion.getIdtransaccion());
            TipoTransaccion tipoTransaccionidtipoTransaccionOld = persistentTransaccion.getTipoTransaccionidtipoTransaccion();
            TipoTransaccion tipoTransaccionidtipoTransaccionNew = transaccion.getTipoTransaccionidtipoTransaccion();
            Factura facturaIdfacturaOld = persistentTransaccion.getFacturaIdfactura();
            Factura facturaIdfacturaNew = transaccion.getFacturaIdfactura();
            ComprobanterecaudoPSE comprobanteRecaudoPSEidcomprobanteRecaudoPSEOld = persistentTransaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE();
            ComprobanterecaudoPSE comprobanteRecaudoPSEidcomprobanteRecaudoPSENew = transaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE();
            FacturaAutopagos facturaAutopagosIdfacturaAutopagosOld = persistentTransaccion.getFacturaAutopagosIdfacturaAutopagos();
            FacturaAutopagos facturaAutopagosIdfacturaAutopagosNew = transaccion.getFacturaAutopagosIdfacturaAutopagos();
            if (tipoTransaccionidtipoTransaccionNew != null) {
                tipoTransaccionidtipoTransaccionNew = em.getReference(tipoTransaccionidtipoTransaccionNew.getClass(), tipoTransaccionidtipoTransaccionNew.getIdtipoTransaccion());
                transaccion.setTipoTransaccionidtipoTransaccion(tipoTransaccionidtipoTransaccionNew);
            }
            if (facturaIdfacturaNew != null) {
                facturaIdfacturaNew = em.getReference(facturaIdfacturaNew.getClass(), facturaIdfacturaNew.getIdfactura());
                transaccion.setFacturaIdfactura(facturaIdfacturaNew);
            }
            if (comprobanteRecaudoPSEidcomprobanteRecaudoPSENew != null) {
                comprobanteRecaudoPSEidcomprobanteRecaudoPSENew = em.getReference(comprobanteRecaudoPSEidcomprobanteRecaudoPSENew.getClass(), comprobanteRecaudoPSEidcomprobanteRecaudoPSENew.getIdcomprobanteRecaudoPSE());
                transaccion.setComprobanteRecaudoPSEidcomprobanteRecaudoPSE(comprobanteRecaudoPSEidcomprobanteRecaudoPSENew);
            }
            if (facturaAutopagosIdfacturaAutopagosNew != null) {
                facturaAutopagosIdfacturaAutopagosNew = em.getReference(facturaAutopagosIdfacturaAutopagosNew.getClass(), facturaAutopagosIdfacturaAutopagosNew.getIdfacturaAutopagos());
                transaccion.setFacturaAutopagosIdfacturaAutopagos(facturaAutopagosIdfacturaAutopagosNew);
            }
            transaccion = em.merge(transaccion);
            if (tipoTransaccionidtipoTransaccionOld != null && !tipoTransaccionidtipoTransaccionOld.equals(tipoTransaccionidtipoTransaccionNew)) {
                tipoTransaccionidtipoTransaccionOld.getTransaccionCollection().remove(transaccion);
                tipoTransaccionidtipoTransaccionOld = em.merge(tipoTransaccionidtipoTransaccionOld);
            }
            if (tipoTransaccionidtipoTransaccionNew != null && !tipoTransaccionidtipoTransaccionNew.equals(tipoTransaccionidtipoTransaccionOld)) {
                tipoTransaccionidtipoTransaccionNew.getTransaccionCollection().add(transaccion);
                tipoTransaccionidtipoTransaccionNew = em.merge(tipoTransaccionidtipoTransaccionNew);
            }
            if (facturaIdfacturaOld != null && !facturaIdfacturaOld.equals(facturaIdfacturaNew)) {
                facturaIdfacturaOld.getTransaccionCollection().remove(transaccion);
                facturaIdfacturaOld = em.merge(facturaIdfacturaOld);
            }
            if (facturaIdfacturaNew != null && !facturaIdfacturaNew.equals(facturaIdfacturaOld)) {
                facturaIdfacturaNew.getTransaccionCollection().add(transaccion);
                facturaIdfacturaNew = em.merge(facturaIdfacturaNew);
            }
            if (comprobanteRecaudoPSEidcomprobanteRecaudoPSEOld != null && !comprobanteRecaudoPSEidcomprobanteRecaudoPSEOld.equals(comprobanteRecaudoPSEidcomprobanteRecaudoPSENew)) {
                comprobanteRecaudoPSEidcomprobanteRecaudoPSEOld.getTransaccionCollection().remove(transaccion);
                comprobanteRecaudoPSEidcomprobanteRecaudoPSEOld = em.merge(comprobanteRecaudoPSEidcomprobanteRecaudoPSEOld);
            }
            if (comprobanteRecaudoPSEidcomprobanteRecaudoPSENew != null && !comprobanteRecaudoPSEidcomprobanteRecaudoPSENew.equals(comprobanteRecaudoPSEidcomprobanteRecaudoPSEOld)) {
                comprobanteRecaudoPSEidcomprobanteRecaudoPSENew.getTransaccionCollection().add(transaccion);
                comprobanteRecaudoPSEidcomprobanteRecaudoPSENew = em.merge(comprobanteRecaudoPSEidcomprobanteRecaudoPSENew);
            }
            if (facturaAutopagosIdfacturaAutopagosOld != null && !facturaAutopagosIdfacturaAutopagosOld.equals(facturaAutopagosIdfacturaAutopagosNew)) {
                facturaAutopagosIdfacturaAutopagosOld.getTransaccionCollection().remove(transaccion);
                facturaAutopagosIdfacturaAutopagosOld = em.merge(facturaAutopagosIdfacturaAutopagosOld);
            }
            if (facturaAutopagosIdfacturaAutopagosNew != null && !facturaAutopagosIdfacturaAutopagosNew.equals(facturaAutopagosIdfacturaAutopagosOld)) {
                facturaAutopagosIdfacturaAutopagosNew.getTransaccionCollection().add(transaccion);
                facturaAutopagosIdfacturaAutopagosNew = em.merge(facturaAutopagosIdfacturaAutopagosNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = transaccion.getIdtransaccion();
                if (findTransaccion(id) == null) {
                    throw new NonexistentEntityException("The transaccion with id " + id + " no longer exists.");
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
            Transaccion transaccion;
            try {
                transaccion = em.getReference(Transaccion.class, id);
                transaccion.getIdtransaccion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transaccion with id " + id + " no longer exists.", enfe);
            }
            TipoTransaccion tipoTransaccionidtipoTransaccion = transaccion.getTipoTransaccionidtipoTransaccion();
            if (tipoTransaccionidtipoTransaccion != null) {
                tipoTransaccionidtipoTransaccion.getTransaccionCollection().remove(transaccion);
                tipoTransaccionidtipoTransaccion = em.merge(tipoTransaccionidtipoTransaccion);
            }
            Factura facturaIdfactura = transaccion.getFacturaIdfactura();
            if (facturaIdfactura != null) {
                facturaIdfactura.getTransaccionCollection().remove(transaccion);
                facturaIdfactura = em.merge(facturaIdfactura);
            }
            ComprobanterecaudoPSE comprobanteRecaudoPSEidcomprobanteRecaudoPSE = transaccion.getComprobanteRecaudoPSEidcomprobanteRecaudoPSE();
            if (comprobanteRecaudoPSEidcomprobanteRecaudoPSE != null) {
                comprobanteRecaudoPSEidcomprobanteRecaudoPSE.getTransaccionCollection().remove(transaccion);
                comprobanteRecaudoPSEidcomprobanteRecaudoPSE = em.merge(comprobanteRecaudoPSEidcomprobanteRecaudoPSE);
            }
            FacturaAutopagos facturaAutopagosIdfacturaAutopagos = transaccion.getFacturaAutopagosIdfacturaAutopagos();
            if (facturaAutopagosIdfacturaAutopagos != null) {
                facturaAutopagosIdfacturaAutopagos.getTransaccionCollection().remove(transaccion);
                facturaAutopagosIdfacturaAutopagos = em.merge(facturaAutopagosIdfacturaAutopagos);
            }
            em.remove(transaccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Transaccion> findTransaccionEntities() {
        return findTransaccionEntities(true, -1, -1);
    }

    public List<Transaccion> findTransaccionEntities(int maxResults, int firstResult) {
        return findTransaccionEntities(false, maxResults, firstResult);
    }

    private List<Transaccion> findTransaccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Transaccion.class));
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

    public Transaccion findTransaccion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Transaccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransaccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Transaccion> rt = cq.from(Transaccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public ArrayList<Transaccion> getTransacciones(long fechaInicial, long fechaFinal) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `transaccion` WHERE `fecha`<" + fechaFinal + " AND `fecha`>" + fechaInicial + " ORDER BY idtransaccion DESC";
        Query query = em.createNativeQuery(queryString, Transaccion.class);
        ArrayList<Transaccion> result = new ArrayList<>(query.getResultList());
        return result;
    }

    public ArrayList<Transaccion> getTransaccionesOnlineByConvenio(long fechaInicial, long fechaFinal, int idConvenio) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `transaccion`,`convenios`,`factura`,`factura_autopagos` WHERE  `idconvenios`= " + idConvenio + "  AND `fecha`<" + fechaFinal + " AND `fecha`>" + fechaInicial + " AND (`factura`.`convenios_idconvenios` = `idconvenios` AND `idfactura` = `factura_idfactura`) OR (`factura_autopagos`.`convenios_idconvenios` = `idconvenios` AND `idfactura_autopagos`=`factura_autopagos_idfactura_autopagos`) ORDER BY idtransaccion DESC";
        Query query = em.createNativeQuery(queryString, Transaccion.class);
        ArrayList<Transaccion> result = new ArrayList<>(query.getResultList());
        return result;
    }

    public ArrayList<Transaccion> getTransaccionesOnline(long fechaInicial, long fechaFinal) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `transaccion`,`convenios`,`factura` WHERE `convenios`.`tipo_convenio` = 2 AND `factura`.`convenios_idconvenios` = `idconvenios` AND `idfactura` = `factura_idfactura` AND `fecha`<" + fechaFinal + " AND `fecha`>" + fechaInicial + " ORDER BY idtransaccion DESC";
        Query query = em.createNativeQuery(queryString, Transaccion.class);
        ArrayList<Transaccion> result = new ArrayList<>(query.getResultList());
        return result;
    }

    public ArrayList<Transaccion> getTransaccionesSipar(long fechaInicial, long fechaFinal) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `transaccion`,`convenios` WHERE `convenios`.`tipo_convenio` = 1 AND `transaccion`.`convenios_idconvenios` = `idconvenios` AND `fecha`<" + fechaFinal + " AND `fecha`>" + fechaInicial + " ORDER BY idtransaccion DESC";
        Query query = em.createNativeQuery(queryString, Transaccion.class);
        ArrayList<Transaccion> result = new ArrayList<>(query.getResultList());
        return result;
    }

    public ArrayList<Transaccion> getTransaccionesAutored(long fechaInicial, long fechaFinal) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `transaccion`,`convenios`,`factura` WHERE `convenios`.`tipo_convenio` = 3 AND `factura`.`convenios_idconvenios` = `idconvenios` AND `idfactura` = `factura_idfactura` AND `fecha`<" + fechaFinal + " AND `fecha`>" + fechaInicial + " ORDER BY idtransaccion DESC";
        Query query = em.createNativeQuery(queryString, Transaccion.class);
        ArrayList<Transaccion> result = new ArrayList<>(query.getResultList());
        return result;
    }

    public ArrayList<Transaccion> getTransaccionesSiparByConvenioFacturacion(long fechaInicial, long fechaFinal, Integer idconvenios) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `transaccion`,`convenios`,`factura` WHERE `convenios`.`tipo_convenio` = 1  AND `transaccion`.`estado_idestado` = 2 AND  `transaccion`.`convenios_idconvenios` = `idconvenios` AND `idconvenios`=" + idconvenios + " AND `idfactura` = `factura_idfactura` AND `fecha`<" + fechaFinal + " AND `fecha`>" + fechaInicial + " ORDER BY idtransaccion DESC";
//        System.out.println("queryString: " + queryString);
        Query query = em.createNativeQuery(queryString, Transaccion.class);
        ArrayList<Transaccion> result = new ArrayList<>(query.getResultList());
        return result;
    }

    public ArrayList<Transaccion> getTransaccionesSiparByConvenio(long fechaInicial, long fechaFinal, Integer idconvenios) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `transaccion`,`convenios` WHERE `convenios`.`tipo_convenio` = 1  AND  `transaccion`.`convenios_idconvenios` = "+idconvenios+" AND `fecha`<" + fechaFinal + " AND `fecha`>" + fechaInicial + " ORDER BY idtransaccion DESC";
        Query query = em.createNativeQuery(queryString, Transaccion.class);
        ArrayList<Transaccion> result = new ArrayList<>(query.getResultList());
        return result;
    }

    public ArrayList<Transaccion> getTransaccionesPagosOnline() {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `transaccion` WHERE `tipoTransaccion_idtipoTransaccion` = 5 AND `estado_idestado` = 1 ORDER BY idtransaccion DESC";
        Query query = em.createNativeQuery(queryString, Transaccion.class);
        ArrayList<Transaccion> result = new ArrayList<>(query.getResultList());
        return result;
    }

    public void destroyTransaccionesByConvenio(int idConvenio) {
        String queryString = "";
        queryString = "DELETE FROM `transaccion` WHERE `convenios_idconvenios` =" + idConvenio;
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Query query = em.createNativeQuery(queryString);
        query.executeUpdate();
        tx.commit();
    }

    public ArrayList<Transaccion> get10ComprobantesUsuarios(Long idUsuario) {
        EntityManager em = getEntityManager();
        String queryString = "SELECT * FROM `transaccion` WHERE `tipoTransaccion_idtipoTransaccion` = 6 AND `estado_idestado` = 2 AND `usuario_idUsuario` = " + idUsuario + "  ORDER BY `idtransaccion` DESC LIMIT 10";
        Query query = em.createNativeQuery(queryString, Transaccion.class);
        ArrayList<Transaccion> result = new ArrayList<Transaccion>(query.getResultList());
        return result;
    }
}
