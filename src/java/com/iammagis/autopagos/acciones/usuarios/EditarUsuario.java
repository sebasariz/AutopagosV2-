/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.usuarios;

import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Modulo;
import com.iammagis.autopagos.jpa.beans.SubModulo;
import com.iammagis.autopagos.jpa.beans.TipoUsuario;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.CorreoZoho;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.TipoUsuarioJpaController;
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
public class EditarUsuario extends org.apache.struts.action.Action {

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
        HttpSession session = request.getSession();
        JSONObject jSONObject = new JSONObject();
        if (session != null) {
            //aqui incluimos todo el codigo de la creacion de usauario
            Usuario usuario = (Usuario) form;
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            //validamos la existencia del correo electronico
            UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
            Usuario usuarioEdit = usuarioJpaController.findUsuario(usuario.getIdUsuario());

            //setiamos el tipo de usuario
            TipoUsuarioJpaController tipoUsuarioJpaController = new TipoUsuarioJpaController(manager);
            TipoUsuario tipoUsuario = tipoUsuarioJpaController.findTipoUsuario(usuario.getIdTipoUsuario());
            usuarioEdit.setTipousuarioIDTipoUsuario(tipoUsuario);
            //setiamos el convenio si lo requiere
            if (tipoUsuario.getIDTipoUsuario() == 2 || tipoUsuario.getIDTipoUsuario() == 3 || tipoUsuario.getIDTipoUsuario() == 5) {
                ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
                Convenios convenios = conveniosJpaController.findConvenios(usuario.getIdConvenio());
                usuarioEdit.setConveniosIdconvenios(convenios);
            }
            usuarioEdit.setNombre(usuario.getNombre());
            usuarioEdit.setApellidos(usuario.getApellidos());
            usuarioEdit.setCelular(usuarioEdit.getCelular());
            usuarioEdit.setNumeroDeDocumento(usuario.getNumeroDeDocumento());
            usuarioEdit.setPass(usuario.getPass());

            //setiamos los modulos y submodulos por cada uno de los roles
            ArrayList<Modulo> modulos = new ArrayList<>();
            ArrayList<SubModulo> subModulos = new ArrayList<>();
            if (tipoUsuario.getIDTipoUsuario() == 1) {
                //root
                modulos.add(new Modulo(1));
                modulos.add(new Modulo(2));
                modulos.add(new Modulo(3));
                modulos.add(new Modulo(4));
                //agregamos los submodulos

                subModulos.add(new SubModulo(1));
                subModulos.add(new SubModulo(2));
                subModulos.add(new SubModulo(3));
                subModulos.add(new SubModulo(4));
                subModulos.add(new SubModulo(5));
                subModulos.add(new SubModulo(6));
                subModulos.add(new SubModulo(7));
                subModulos.add(new SubModulo(8));
                subModulos.add(new SubModulo(15));
                subModulos.add(new SubModulo(17));
            } else if (tipoUsuario.getIDTipoUsuario() == 2) {
                //sipar
                modulos.add(new Modulo(3));
                subModulos.add(new SubModulo(9));
                subModulos.add(new SubModulo(10));
                subModulos.add(new SubModulo(13));
                subModulos.add(new SubModulo(18));
            } else if (tipoUsuario.getIDTipoUsuario() == 3) {
                //online
                modulos.add(new Modulo(2));
                subModulos.add(new SubModulo(11));
                subModulos.add(new SubModulo(12));
                subModulos.add(new SubModulo(16));
            } else if (tipoUsuario.getIDTipoUsuario() == 4) {

            }
            usuarioEdit.setModuloCollection(modulos);
            usuarioEdit.setSubModuloCollection(subModulos);
            usuarioEdit = usuarioJpaController.edit(usuarioEdit);

            //Falta enviar el correo electronico pues no existen plantillas
            ArrayList<String> strings = new ArrayList<>();
            strings.add(usuarioEdit.getEmail());
            String template = CorreoZoho.getTemplate(CorreoZoho.modificadoUsuarioTemplate);
            template = template.replace("$server", propertiesAccess.SERVER);
            template = template.replace("$nomUser", usuario.getNombre() + " " + usuario.getApellidos());
            template = template.replace("$usuario", usuario.getEmail());
            template = template.replace("$password", usuario.getPass());
            CorreoZoho correoZoho = new CorreoZoho("Se han modificado tus datos AUTOPAGOS.CO", template, strings);
            correoZoho.run();

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
