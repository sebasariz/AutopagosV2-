/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.movil;

import com.iammagis.autopagos.jpa.beans.Dispositivo;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.control.DispositivoJpaController;
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
 * @author root
 */
public class PushNotification extends org.apache.struts.action.Action {

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

        request.setCharacterEncoding("UTF-8");
        String sessionToken = request.getParameter("sessionToken");
        String notification = request.getParameter("notification");
        int tipo=1;
        //tipo 1 Android 2 IOS
        if(request.getParameter("tipo")!=null){
            tipo = Integer.parseInt(request.getParameter("tipo"));
        }

        JSONObject jSONObject = new JSONObject();
        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
        Usuario usuario = usuarioJpaController.findByToken(sessionToken);
        DispositivoJpaController dispositivoJpaController = new DispositivoJpaController(manager);
        if (usuario != null) {
            ArrayList<Dispositivo> dispositivos = new ArrayList<>(usuario.getDispositivoCollection());
            boolean existe = false;
            for (Dispositivo dispositivo : dispositivos) {
                if (dispositivo.getToken().equals(notification)) {
                    //el dispositivo existe 
                    existe = true;
                }
            }
            if (!existe) {
                Dispositivo dispositivo = new Dispositivo();
                dispositivo.setUsuarioidUsuario(usuario);
                dispositivo.setToken(notification);
                dispositivo.setTipo(tipo);
                dispositivoJpaController.create(dispositivo);
            } 
            jSONObject.put("estado", "ok");
        } else {
            jSONObject.put("estado", "error");
        }
        //enviar los asignados que tenga en el momento 
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject);
        return null;
    }
}
