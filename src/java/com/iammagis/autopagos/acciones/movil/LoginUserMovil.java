/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.movil;

import com.iammagis.autopagos.jpa.beans.Codigo;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.TipoUsuario;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.UsuarioHasUsuarioReferidosAutored;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.CodigoJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioHasUsuarioReferidosAutoredJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.UUID;
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
public class LoginUserMovil extends org.apache.struts.action.Action {

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
        String email = request.getParameter("email");
        String nombre = request.getParameter("nombre");
        String img = request.getParameter("img");
        JSONObject jSONObject = new JSONObject();
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario = usuarioJpaController.emailExist(usuario);
        if (usuario != null && usuario.getTipousuarioIDTipoUsuario().getIDTipoUsuario() == 4) {
            //el usuario ya ha sido creado ya no se requieren mas datos 
            usuario.setFoto(img);
            usuario.setNombre(nombre.split(" ")[0]);
            usuario.setApellidos(nombre.split(" ")[1]);
            usuario.setEmail(email);
            usuario = usuarioJpaController.edit(usuario);
            jSONObject.put("codigo", false);
            jSONObject.put("token", usuario.getSesionToken());
            jSONObject.put("inscritas", usuario.getFacturaCollection().size());
            jSONObject.put("pendientes", usuario.getFacturaCollection().size());
            jSONObject.put("puntos", usuario.getPuntos());
            jSONObject.put("miembros", usuario.getUsuarioHasUsuarioReferidosAutoredCollection1().size());

        } else if (request.getParameter("codigo") != null) {
            String codigoString = request.getParameter("codigo");

            //buscar el codigo y crear el usaurio con el nombre y los datos
            CodigoJpaController codigoJpaController = new CodigoJpaController(manager);
            Codigo codigo = codigoJpaController.getCodigoByString(codigoString);
            if (codigoString.equals("NO")) {
                codigo = codigoJpaController.findCodigo(23);
                Usuario usuarioReferente = codigo.getUsuarioidUsuario();
                usuario = new Usuario();
                usuario.setPass(codigoString);
                usuario.setNombre(nombre.split(" ")[0]);
                if (nombre.split(" ").length > 1) {
                    usuario.setApellidos(nombre.split(" ")[1]);
                } else {
                    usuario.setApellidos("");
                }
                usuario.setFoto(img);
                usuario.setPuntos(propertiesAccess.SALDOINICIAL);
                usuario.setEmail(email);
                usuario.setFacturaCollection(new ArrayList<Factura>());
                usuario.setActivoAutored(Boolean.TRUE);
                usuario.setFechaRegistro(BigInteger.valueOf(System.currentTimeMillis()));
                usuario.setTipousuarioIDTipoUsuario(new TipoUsuario(4));
                usuario.setSesionToken(UUID.randomUUID().toString());
                usuario = usuarioJpaController.create(usuario);

                UsuarioHasUsuarioReferidosAutoredJpaController usuarioHasUsuarioReferidosAutoredJpaController = new UsuarioHasUsuarioReferidosAutoredJpaController(manager);
                UsuarioHasUsuarioReferidosAutored usuarioHasUsuarioReferidosAutored = new UsuarioHasUsuarioReferidosAutored();
                usuarioHasUsuarioReferidosAutored.setUsuario1(usuarioReferente);
                usuarioHasUsuarioReferidosAutored.setUsuario(usuario);
                usuarioHasUsuarioReferidosAutored.setPuntos(0);
                usuarioHasUsuarioReferidosAutored.setCodigoReferido(codigoString);
                usuarioHasUsuarioReferidosAutoredJpaController.create(usuarioHasUsuarioReferidosAutored);
                jSONObject.put("token", usuario.getSesionToken());
                jSONObject.put("inscritas", usuario.getFacturaCollection().size());
                jSONObject.put("pendientes", usuario.getFacturaCollection().size());
                jSONObject.put("puntos", usuario.getPuntos());
                jSONObject.put("miembros", usuario.getUsuarioHasUsuarioReferidosAutoredCollection1().size());
                jSONObject.put("codigo", false);

            } else if (codigo != null && !codigo.getUsado()) {
                codigo.setUsado(Boolean.TRUE);
                codigoJpaController.edit(codigo);

                Usuario usuarioReferente = codigo.getUsuarioidUsuario();
                usuario = new Usuario();
                usuario.setPass(codigoString);
                usuario.setNombre(nombre.split(" ")[0]);
                if (nombre.split(" ").length > 1) {
                    usuario.setApellidos(nombre.split(" ")[1]);
                } else {
                    usuario.setApellidos("");
                }
                usuario.setFoto(img);
                usuario.setPuntos(propertiesAccess.SALDOINICIAL);
                usuario.setEmail(email);
                usuario.setFacturaCollection(new ArrayList<Factura>());
                usuario.setActivoAutored(Boolean.TRUE);
                usuario.setFechaRegistro(BigInteger.valueOf(System.currentTimeMillis()));
                usuario.setTipousuarioIDTipoUsuario(new TipoUsuario(4));
                usuario.setSesionToken(UUID.randomUUID().toString());
                usuario = usuarioJpaController.create(usuario);

                UsuarioHasUsuarioReferidosAutoredJpaController usuarioHasUsuarioReferidosAutoredJpaController = new UsuarioHasUsuarioReferidosAutoredJpaController(manager);
                UsuarioHasUsuarioReferidosAutored usuarioHasUsuarioReferidosAutored = new UsuarioHasUsuarioReferidosAutored();
                usuarioHasUsuarioReferidosAutored.setUsuario1(usuarioReferente);
                usuarioHasUsuarioReferidosAutored.setUsuario(usuario);
                usuarioHasUsuarioReferidosAutored.setPuntos(0);
                usuarioHasUsuarioReferidosAutored.setCodigoReferido(codigoString);
                usuarioHasUsuarioReferidosAutoredJpaController.create(usuarioHasUsuarioReferidosAutored);
                jSONObject.put("token", usuario.getSesionToken());
                jSONObject.put("inscritas", usuario.getFacturaCollection().size());
                jSONObject.put("pendientes", usuario.getFacturaCollection().size());
                jSONObject.put("puntos", usuario.getPuntos());
                jSONObject.put("miembros", usuario.getUsuarioHasUsuarioReferidosAutoredCollection1().size());
                jSONObject.put("codigo", false);
            } else {
                jSONObject.put("codigo", true);
                jSONObject.put("error", "El codigo de invitacion ingresado no es valido.");
            }
        } else {
            jSONObject.put("codigo", true);
            jSONObject.put("error", "El codigo de invitacion ingresado no es valido.");
        }
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jSONObject);
        return null;
    }
}
