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
import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class LoadGestionConvenios extends org.apache.struts.action.Action {

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
        ActionErrors errores = new ActionErrors();
        if (session != null) {
            String content = "/pages/convenios.jsp";
            session.setAttribute("contenido", content);

            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            //cargamos las clases
            ClaseJpaController claseJpaController = new ClaseJpaController(manager);
            ArrayList<Clase> clases = new ArrayList<>(claseJpaController.findClaseEntities());
            request.setAttribute("clases", clases);
            //tipos de cuenta
            TipoCuentaJpaController tipoCuentaJpaController = new TipoCuentaJpaController(manager);
            ArrayList<TipoCuenta> tipoCuentas = new ArrayList<>(tipoCuentaJpaController.findTipoCuentaEntities());
            request.setAttribute("tipoCuentas", tipoCuentas);
            //planes
            PlanJpaController planJpaController = new PlanJpaController(manager);
            ArrayList<Plan> plans = new ArrayList<>(planJpaController.findPlanEntities());
            request.setAttribute("planes", plans);
            //canales
            CanalJpaController canalJpaController = new CanalJpaController(manager);
            ArrayList<Canal> canales = new ArrayList<>(canalJpaController.findCanalEntities());
            request.setAttribute("canales", canales);
            
            ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
            ArrayList<Convenios> convenioses = new ArrayList<>(conveniosJpaController.findConveniosEntities());
            JSONObject jSONObject = DataGrid.getConveniosData(convenioses);
            request.setAttribute("convenios", jSONObject);
            
        } else {
            errores.add("register", new ActionMessage("erros.tiempoAgotado"));
            saveErrors(request, errores);
            return mapping.findForward("inicio");
        }

        return mapping.findForward(SUCCESS);
    }
}
