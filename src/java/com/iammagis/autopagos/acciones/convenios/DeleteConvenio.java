/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.convenios;

import com.iammagis.autopagos.jpa.beans.Campo;
import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.FacturaAutopagos;
import com.iammagis.autopagos.jpa.beans.FacturaTemplate;
import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
import com.iammagis.autopagos.jpa.control.CampoJpaController;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.FacturaAutopagosJpaController;
import com.iammagis.autopagos.jpa.control.FacturaJpaController;
import com.iammagis.autopagos.jpa.control.FacturaTemplateJpaController;
import com.iammagis.autopagos.jpa.control.LogIngresoJpaController;
import com.iammagis.autopagos.jpa.control.TransaccionJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class DeleteConvenio extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";

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
        
        HttpSession session = request.getSession();
        JSONObject jSONObject = new JSONObject();
        if (session != null) {
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            int idConvenio = Integer.parseInt(request.getParameter("idConvenio"));
            ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
            try {
                TransaccionJpaController transaccionJpaController = new TransaccionJpaController(manager);
                transaccionJpaController.destroyTransaccionesByConvenio(idConvenio);
                
                Convenios convenios = conveniosJpaController.findConvenios(idConvenio);
                ArrayList<Usuario> usuarios = new ArrayList<>(convenios.getUsuarioCollection());
                LogIngresoJpaController logIngresoJpaController = new LogIngresoJpaController(manager);
                UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
                for (Usuario usuario : usuarios) {
                    logIngresoJpaController.deleteLogsByUser(usuario.getIdUsuario());
                    usuarioJpaController.destroy(usuario.getIdUsuario());
                }
                
                FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
                ArrayList<Factura> facturas = new ArrayList<>(convenios.getFacturaCollection());
                for (Factura factura : facturas) {
                    ArrayList<Transaccion> transaccions = new ArrayList<>(factura.getTransaccionCollection());
                    for (Transaccion transaccion : transaccions) {
                        transaccionJpaController.destroy(transaccion.getIdtransaccion());
                    }
                    facturaJpaController.destroy(factura.getIdfactura());
                    
                }
                
                FacturaTemplateJpaController facturaTemplateJpaController = new FacturaTemplateJpaController(manager);
                CampoJpaController campoJpaController = new CampoJpaController(manager);
                ArrayList<FacturaTemplate> facturaTemplates = new ArrayList<>(convenios.getFacturaTemplateCollection());
                for (FacturaTemplate facturaTemplate : facturaTemplates) {
                    ArrayList<Campo> campos = new ArrayList<>(facturaTemplate.getCampoCollection());
                    for (Campo campo : campos) {
                        campoJpaController.destroy(campo.getIdcampo());
                    }
                    facturaTemplateJpaController.destroy(facturaTemplate.getIdfacturaTemplate());
                }
                
                FacturaAutopagosJpaController facturaAutopagosJpaController = new FacturaAutopagosJpaController(manager);
                ArrayList<FacturaAutopagos> facturaAutopagoses = new ArrayList<>(convenios.getFacturaAutopagosCollection());
                for (FacturaAutopagos facturaAutopagos : facturaAutopagoses) {
                    facturaAutopagos.setConveniosIdconvenios(null);
                    facturaAutopagos.setItems(facturaAutopagos.getItems() + " - Conenio eliminado: " + convenios.getNombre() + " NIT: " + convenios.getNit());
                    facturaAutopagosJpaController.edit(facturaAutopagos);
                }
                
                conveniosJpaController.destroy(idConvenio);
                ArrayList<Convenios> convenioses = new ArrayList<>(conveniosJpaController.findConveniosEntities());
                jSONObject = DataGrid.getConveniosData(convenioses);
            } catch (Exception e) {
                e.printStackTrace();
                MessageResources messages = MessageResources.getMessageResources("com.iammagis.autopagos.resources");
                String message = messages.getMessage(request.getLocale(), "erros.convenio.no.eliminado");
                jSONObject.put("error", message);
            }
            
        } else {
            MessageResources messages = MessageResources.getMessageResources("com.iammagis.autopagos.resources");
            String message = messages.getMessage(request.getLocale(), "erros.tiempoAgotado");
            jSONObject.put("error", message);
        }
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject);
        return null;
    }
}
