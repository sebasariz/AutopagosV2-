/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.movil;

import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.FacturaJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class InscribirMovilFactura extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    PropertiesAccess propertiesAccess = new PropertiesAccess();

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        request.setCharacterEncoding("UTF-8");
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        String token = request.getParameter("token");
        String referencia = request.getParameter("referencia");
        String nombre = request.getParameter("nombre");
        int idCodigoConvenio = Integer.parseInt(request.getParameter("idConvenio"));

        JSONObject jSONObject = new JSONObject();
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
        Usuario usuario = usuarioJpaController.findByToken(token);
        System.out.println("inicio por aqui");
        if (usuario != null) {
            //consultar existencia de factura con fullcarga

            ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
            Convenios convenios = conveniosJpaController.findConvenios(idCodigoConvenio);
            FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
            //consultamos la factura
            System.out.println("entrando por covenio: " + convenios.getTipoConvenio());
            if (convenios.getTipoConvenio() == 2) {
                System.out.println("entrando por convenio tipo 2");
                Factura factura = facturaJpaController.getFacturaFromConvenioYReferencia(idCodigoConvenio, referencia);
                if (factura != null) {
                    ArrayList<Usuario> usuarios = new ArrayList<>(factura.getUsuarioCollection());
                    usuarios.add(usuario);
                    factura.setNombre(nombre);
                    factura.setUsuarioCollection(usuarios);
                    facturaJpaController.edit(factura);
                    jSONObject.put("estado", "Inscripcion correcta");
                } else {
                    jSONObject.put("estado", "error");
                    jSONObject.put("msg", "La factura no se encuentra disponible.");
                }
            } else if (convenios.getTipoConvenio() == 3) {
                //JSONObject jSONObjectRespuesta = FullCargaAutopagosServices.getEstado(convenios, referencia);
                /*if (jSONObjectRespuesta.getBoolean("estado") && jSONObjectRespuesta.getString("codigo").equals("00")) {
                    //esta disponible para ser asociada
                    String codigoFull = jSONObjectRespuesta.getString("codfullcarga");
                    double valor = jSONObjectRespuesta.getDouble("valor");
                    //aqui ya sigue la creacion normal 
                    //si factura retorna disponible se asocia, de lo contrario factura no se encuentra disponible

                    //buscamso la factura por si ya existe
                    Factura factura = facturaJpaController.getFacturaFromConvenioYReferencia(idCodigoConvenio, referencia);
                    if (factura != null) {
                        ArrayList<Usuario> usuarios = new ArrayList<>(factura.getUsuarioCollection());
                        usuarios.add(usuario);

                        factura.setNombre(nombre);
                        factura.setUsuarioCollection(usuarios);
                        factura.setValor(valor);
                        factura.setEmail(usuario.getEmail());
                        factura.setCodigoFullcarga(codigoFull);
                        facturaJpaController.edit(factura);
                    } else {
                        ArrayList<Usuario> usuarios = new ArrayList<>();
                        usuarios.add(usuario);
                        factura = new Factura();
                        factura.setFechaCreacion(BigInteger.valueOf(System.currentTimeMillis()));
                        factura.setReferencia(referencia);
                        factura.setNombre(nombre);
                        factura.setValor(valor);
                        factura.setCodigoFullcarga(codigoFull);
                        factura.setEmail(usuario.getEmail());
                        factura.setEstadoIdestado(new Estado(1));
                        factura.setConveniosIdconvenios(convenios);
                        factura.setFechaEmision(BigInteger.ZERO);
                        factura.setUsuarioCollection(usuarios);
                        facturaJpaController.create(factura);
                    }
                    jSONObject.put("estado", "Inscripcion correcta");

                } else {
                    System.out.println("la factura no esta disponible");
                    jSONObject.put("estado", "error");
                    jSONObject.put("msg", "La factura no se encuentra disponible.");
                }
*/
            }

        } else {
            jSONObject.put("error", "Error de session");
        }
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject);
        return null;
    }
}
