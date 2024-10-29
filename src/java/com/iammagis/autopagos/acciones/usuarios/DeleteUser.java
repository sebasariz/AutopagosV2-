/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.usuarios;

import com.iammagis.autopagos.jpa.beans.LogIngreso;
import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
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
public class DeleteUser extends org.apache.struts.action.Action {

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
            //aqui incluimos todo el codigo de la creacion de usauario
            long idUsuario = Long.parseLong(request.getParameter("idUsuario"));
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            //validamos la existencia del correo electronico
            UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
            Usuario usuarioEdit = usuarioJpaController.findUsuario(idUsuario);

            ArrayList<LogIngreso> logIngresos = new ArrayList<>(usuarioEdit.getLogIngresoCollection());
            LogIngresoJpaController logIngresoJpaController = new LogIngresoJpaController(manager);
            for (LogIngreso logIngreso : logIngresos) {
                logIngresoJpaController.destroy(logIngreso.getIdlogIngreso());
            }
            TransaccionJpaController transaccionJpaController = new TransaccionJpaController(manager);
            ArrayList<Transaccion> transaccions = new ArrayList<>(usuarioEdit.getTransaccionCollection());
            for(Transaccion transaccion:transaccions){
                transaccionJpaController.destroy(transaccion.getIdtransaccion());
            }
            usuarioJpaController.destroy(idUsuario);

            //retornamos la lista de usuarios 
            ArrayList<Usuario> usuarios = new ArrayList<>(usuarioJpaController.findUsuarioEntities());
            jSONObject = DataGrid.getUserData(usuarios);
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
