/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.convenios;

import com.iammagis.autopagos.jpa.beans.Canal;
import com.iammagis.autopagos.jpa.beans.Clase;
import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Plan;
import com.iammagis.autopagos.jpa.beans.TipoCuenta;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
import com.iammagis.autopagos.jpa.control.CanalJpaController;
import com.iammagis.autopagos.jpa.control.ClaseJpaController;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.PlanJpaController;
import com.iammagis.autopagos.jpa.control.TipoCuentaJpaController;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class LoadConvenioJson extends org.apache.struts.action.Action {

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
            int idConvenio = Integer.parseInt(request.getParameter("idConvenio"));
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
            Convenios convenios = conveniosJpaController.findConvenios(idConvenio);

            jSONObject.put("idConvenio", convenios.getIdconvenios());
            jSONObject.put("nombre", convenios.getNombre());
            jSONObject.put("codigo", convenios.getCodigo());
            jSONObject.put("nit", convenios.getNit());
            jSONObject.put("direccion", convenios.getDireccion());
            jSONObject.put("telefono", convenios.getTelefono());
            jSONObject.put("idClase", convenios.getClaseIdclase().getIdclase());
            jSONObject.put("numeroCuenta", convenios.getNumeroCuenta());
            jSONObject.put("clase", convenios.getClaseIdclase().getIdclase());
            jSONObject.put("banco", convenios.getBanco());
            jSONObject.put("titularCuenta", convenios.getTitularCuenta());
            if (convenios.getTipoCuentaidtipoCuenta() != null) {
                jSONObject.put("idTipoCuenta", convenios.getTipoCuentaidtipoCuenta().getIdtipoCuenta());
            }
            jSONObject.put("valorFijoConvenio", convenios.getValorFijoConvenio());
            jSONObject.put("valorVariableConvenio", convenios.getValorVariableConvenio());
            jSONObject.put("valorFijoUsuario", convenios.getValorFijoUsuario());
            jSONObject.put("valorVariableUsuario", convenios.getValorVariableUsuario());
            jSONObject.put("dirRespuesta", convenios.getDireccionRespuesta());
            jSONObject.put("logo", "img/logoConvenios/" + convenios.getLogo());
            jSONObject.put("camaraComercio", "docs/" + convenios.getCamaraComercio());
            jSONObject.put("rut", "docs/" + convenios.getRut());
            jSONObject.put("tipoConvenio", convenios.getTipoConvenio());
            jSONObject.put("codigoBarrasTercero", convenios.getCodigoBarrasTercero());
            if (convenios.getFechaCortePlanPost() != null) {
                jSONObject.put("fechaFacturacion", convenios.getFechaCortePlanPost().longValue());
            }
            jSONObject.put("usuarioCanal", convenios.getUsuarioCanalPlanPost());
            jSONObject.put("passCanal", convenios.getPassCanalPlanPost());
            jSONObject.put("codigoCanal", convenios.getCodigoCanalPlanPost());
            if (convenios.getCanalIdcanalPlanPost() != null) {
                jSONObject.put("idCanal", convenios.getCanalIdcanalPlanPost().getIdcanal());
            }
            if (convenios.getActivo() != null && convenios.getActivo()) {
                jSONObject.put("mora", "Inactivo");
            } else {
                jSONObject.put("mora", "Activo");
            }
            if (convenios.getValorVariable()) {
                jSONObject.put("valorVariable", 1);
            } else {
                jSONObject.put("valorVariable", 0);
            }

            jSONObject.put("bancoDoc", "docs/" + convenios.getCertificacionBancaria());
            if (convenios.getActivo()) {
                jSONObject.put("activo", 1);
            } else {
                jSONObject.put("activo", 0);
            }
            jSONObject.put("codigoProductoTercero", convenios.getCodigoProductoTercero());

            //AQUI VA EL ID DEL PLAN
            if (convenios.getPlanIdplan() != null) {
                jSONObject.put("idPlan", convenios.getPlanIdplan().getIdplan());
            }
            if (convenios.getValorFijoTercero() != null && convenios.getValorFijoTercero()) {
                jSONObject.put("valorFijo", 1);
            } else {
                jSONObject.put("valorFijo", 0);
            }
            jSONObject.put("textoGuia", convenios.getTextoGuiaTercero());
            jSONObject.put("comisionWinredFija", convenios.getComisionTerceroFija());
            jSONObject.put("comisionWinredVariable", convenios.getComisionTerceroVariable());

            jSONObject.put("camposObligatoriosSipar", convenios.getCamposObligatoriosSipar());
            jSONObject.put("campoIdentificadorSipar", convenios.getCampoIdentificadorSipar());
            jSONObject.put("bancoTercero", convenios.getBancoTercero());
            jSONObject.put("recargaTercero", convenios.getRecargaTercero());
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
