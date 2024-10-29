/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.sesion;

import com.iammagis.autopagos.jpa.beans.LogIngreso;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.DataGrid;
import com.iammagis.autopagos.jpa.beans.support.DynamicMenu;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.LogIngresoJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import java.math.BigInteger;
import java.text.DecimalFormat;
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

/**
 *
 * @author Usuario
 */
public class Login extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";

    DecimalFormat decimalFormat = new DecimalFormat("##0.00");
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
        if (session != null) {
            Usuario usuario = (Usuario) form;
            ActionErrors errores = new ActionErrors();
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
            Usuario usuarioLoged = usuarioJpaController.login(usuario);
            if (usuarioLoged != null) {

                if (usuarioLoged.getConveniosIdconvenios() != null && (!usuarioLoged.getConveniosIdconvenios().getActivo())) {
                    //el convenio se enceuntra inactivo
                    errores.add("register", new ActionMessage("erros.convenio.inactivo"));
                    saveErrors(request, errores);
                    return mapping.findForward("error");
                }
                //registro log de ingreso
                LogIngreso logIngreso = new LogIngreso();
                logIngreso.setFecha(BigInteger.valueOf(System.currentTimeMillis()));
                logIngreso.setUsuarioidUsuario(usuarioLoged);
                LogIngresoJpaController logIngresoJpaController = new LogIngresoJpaController(manager);
                logIngresoJpaController.create(logIngreso);
                //armando menu dinamico
                session.setAttribute("menuHtml", DynamicMenu.getMenu(usuarioLoged));

                //informacion estandar
                usuarioLoged.setTipoUsuarioString(usuarioLoged.getTipousuarioIDTipoUsuario().getNombre());
                request.getSession().setAttribute("usuario", usuarioLoged);
                //usuario existente obtenemos la informacion basada en los ultimaos 30 das
                String content = "";
                if (usuarioLoged.getTipousuarioIDTipoUsuario().getIDTipoUsuario() == 1) {
                    //tipo usuario root  
//                    FullCargaAutopagosServices.getSaldo();
                    //setiamos el contenido
                    content = "contenedor/root.jsp";
                    //cargamos los indicadores globbales 
                    DataGrid.getGlobalIndicators(request);

                } else if (usuarioLoged.getTipousuarioIDTipoUsuario().getIDTipoUsuario() == 2) {
                    //Sipar
                    content = "contenedor/sipar.jsp";
                    //cargamos los datos del convenio
                    request.setAttribute("convenio", usuarioLoged.getConveniosIdconvenios());

                } else if (usuarioLoged.getTipousuarioIDTipoUsuario().getIDTipoUsuario() == 3) {
                    //Online
                    content = "contenedor/online.jsp";
                    //set valor saldo
                    double saldo = 0;
                    if (usuarioLoged.getConveniosIdconvenios() != null) {
                        saldo = usuarioLoged.getConveniosIdconvenios().getValorRecaudado() - usuarioLoged.getConveniosIdconvenios().getValorComisiones();
                        usuarioLoged.getConveniosIdconvenios().setSaldo(saldo);
                    }

                    //cargamos los datos del convenio
                    request.setAttribute("convenio", usuarioLoged.getConveniosIdconvenios());

                } else if (usuarioLoged.getTipousuarioIDTipoUsuario().getIDTipoUsuario() == 4) {
                    //Autored no puede logearse al sistema
                    errores.add("register", new ActionMessage("erros.noUsuAndPass"));
                    saveErrors(request, errores);
                    return mapping.findForward("error");
                }
                //setiamos el contenido del jsp
                session.setAttribute("contenido", content);
            } else {
                //no logeado error de sesion
                errores.add("register", new ActionMessage("erros.noUsuAndPass"));
                saveErrors(request, errores);
                return mapping.findForward("error");
            }
        }
        return mapping.findForward(SUCCESS);
    }
}
