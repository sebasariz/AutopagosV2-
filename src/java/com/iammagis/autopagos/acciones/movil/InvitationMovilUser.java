/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.movil;

import com.iammagis.autopagos.jpa.beans.Codigo;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.CorreoZoho;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.CodigoJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class InvitationMovilUser extends org.apache.struts.action.Action {

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

        JSONObject jSONObject = new JSONObject();
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
        Usuario usuario = usuarioJpaController.findByToken(token);
        Usuario usuarioExistente = usuarioJpaController.emailExist(usuario);
        if (usuarioExistente != null) {
            //buscamos el codigo del email 
            //creamos el codigo de referencia

            if (request.getParameter("email") != null) {
                String email = request.getParameter("email");
                //validar si el correo electornico no se encuentra activo
                Usuario usuarioInvitado = new Usuario();
                usuarioInvitado.setEmail(email);
                usuarioInvitado = usuarioJpaController.emailExist(usuarioInvitado);
                if (usuarioInvitado == null || usuarioInvitado.getTipousuarioIDTipoUsuario().getIDTipoUsuario() != 4) {
                    //miramos si ahi codigos generados libres
                    CodigoJpaController codigoJpaController = new CodigoJpaController(manager);
                    Codigo codigo = codigoJpaController.getCodigoByUsuario(usuarioExistente.getIdUsuario());
                    if (codigo == null) {
                        codigo = new Codigo();
                        String codigoString = UUID.randomUUID().toString().substring(0, 6);
                        codigo.setCodigo(codigoString);
                        codigo.setUsado(Boolean.FALSE);
                        codigo.setUsuarioidUsuario(usuarioExistente);
                        codigoJpaController.create(codigo);
                    }

                    ArrayList<String> emails = new ArrayList<>();
                    emails.add(email);
                    String template = CorreoZoho.getTemplate(CorreoZoho.invitaiconTemplate);
                    template = template.replace("$server", propertiesAccess.SERVER);
                    template = template.replace("$codInv", codigo.getCodigo());
                    template = template.replace("$nomUsuario", usuarioExistente.getNombre() + " " + usuarioExistente.getApellidos());
                    new CorreoZoho("Invitacion autored", template, emails).start();
//                System.out.println("enviando correo: " + email);
                } else {
//                System.out.println("no enviado");
                    jSONObject.put("error", "El usaurio ya es parte de autored.");
                }
            } else if (request.getParameter("multiple") != null) {
                String multiple = request.getParameter("multiple");
                JSONArray jSONArray = new JSONArray(multiple);
                for (int i = 0; i < jSONArray.length(); i++) {
                    String email = jSONArray.getString(i);
                    Usuario usuarioInvitado = new Usuario();
                    usuarioInvitado.setEmail(email);
                    usuarioInvitado = usuarioJpaController.emailExist(usuarioInvitado);
                    if (usuarioInvitado == null || usuarioInvitado.getTipousuarioIDTipoUsuario().getIDTipoUsuario() != 4) {
                        //miramos si ahi codigos generados libres
                        CodigoJpaController codigoJpaController = new CodigoJpaController(manager);
                        Codigo codigo = codigoJpaController.getCodigoByUsuario(usuarioExistente.getIdUsuario());
                        if (codigo == null) {
                            codigo = new Codigo();
                            String codigoString = UUID.randomUUID().toString().substring(0, 6);
                            codigo.setCodigo(codigoString);
                            codigo.setUsado(Boolean.FALSE);
                            codigo.setUsuarioidUsuario(usuarioExistente);
                            codigoJpaController.create(codigo);
                        }

                        ArrayList<String> emails = new ArrayList<>();
                        emails.add(email);
                        String template = CorreoZoho.getTemplate(CorreoZoho.invitaiconTemplate);
                        template = template.replace("$server", propertiesAccess.SERVER);
                        template = template.replace("$codInv", codigo.getCodigo());
                        template = template.replace("$nomUsuario", usuarioExistente.getNombre() + " " + usuarioExistente.getApellidos());
                        new CorreoZoho("Invitacion autored", template, emails).start();
//                System.out.println("enviando correo: " + email);
                    } else {
//                System.out.println("no enviado");
                        jSONObject.put("error", "El usaurio ya es parte de autored.");
                    }
                }
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
