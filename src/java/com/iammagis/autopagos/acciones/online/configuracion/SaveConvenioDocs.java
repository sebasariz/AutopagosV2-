/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.online.configuracion;

import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
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
public class SaveConvenioDocs extends org.apache.struts.action.Action {

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
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {

            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            Convenios conveniosAnterior = usuario.getConveniosIdconvenios();
            ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
            conveniosAnterior = conveniosJpaController.findConvenios(conveniosAnterior.getIdconvenios());
            Convenios convenio = (Convenios) form;
            //subimos le logo
            if (convenio.getLogoFileForm() != null) {
                FormFile logo = convenio.getLogoFileForm();
                File foto = null;
                try {
                    // get file from the bean
                    String fname = logo.getFileName();
                    if (fname.length() == 0) {
                        System.out.println("sin archivo");
                    } else {
                        fname = fname.replace(" ", "");
                        fname = System.currentTimeMillis() + fname;
                        // save file in the app server 
                        foto = new File(getServlet().getServletContext().getRealPath("") + "/img/logoConvenios/" + fname);
                        FileOutputStream fos = new FileOutputStream(foto);
                        fos.write(logo.getFileData());
                        fos.close();
                        conveniosAnterior.setLogo(fname);
                        jSONObject.put("logo", "img/logoConvenios/" + fname);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.print("ERROR-------" + e);
                }
            }
            if (convenio.getLogoFileFormCCio() != null) {
                FormFile ccio = convenio.getLogoFileFormCCio();
                File ccioFile = null;
                try {
                    // get file from the bean
                    String fname = ccio.getFileName();
                    if (fname.length() == 0) {
                        System.out.println("sin archivo");
                    } else {
                        fname = fname.replace(" ", "");
                        fname = System.currentTimeMillis() + fname;
                        // save file in the app server 
                        ccioFile = new File(getServlet().getServletContext().getRealPath("") + "/docs/" + fname);
                        FileOutputStream fos = new FileOutputStream(ccioFile);
                        fos.write(ccio.getFileData());
                        fos.close();
                        conveniosAnterior.setCamaraComercio(fname);
                        jSONObject.put("ccio", "docs/" + fname);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.print("ERROR-------" + e);
                }
            }
            if (convenio.getLogoFileFormRut() != null) {
                FormFile rut = convenio.getLogoFileFormRut();
                File rutFile = null;
                try {
                    // get file from the bean
                    String fname = rut.getFileName();
                    if (fname.length() == 0) {
                        System.out.println("sin archivo");
                    } else {
                        fname = fname.replace(" ", "");
                        fname = System.currentTimeMillis() + fname;
                        // save file in the app server 
                        rutFile = new File(getServlet().getServletContext().getRealPath("") + "/docs/" + fname);
                        FileOutputStream fos = new FileOutputStream(rutFile);
                        fos.write(rut.getFileData());
                        fos.close();
                        conveniosAnterior.setRut(fname);
                        jSONObject.put("rut", "docs/" + fname);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.print("ERROR-------" + e);
                }
            }
            if (convenio.getLogoFileFormBanco() != null) {
                FormFile banco = convenio.getLogoFileFormBanco();
                File bancoFile = null;
                try {
                    // get file from the bean
                    String fname = banco.getFileName();
                    if (fname.length() == 0) {
                        System.out.println("sin archivo");
                    } else {
                        fname = fname.replace(" ", "");
                        fname = System.currentTimeMillis() + fname;
                        // save file in the app server 
                        bancoFile = new File(getServlet().getServletContext().getRealPath("") + "/docs/" + fname);
                        FileOutputStream fos = new FileOutputStream(bancoFile);
                        fos.write(banco.getFileData());
                        fos.close();
                        conveniosAnterior.setCertificacionBancaria(fname);
                        jSONObject.put("banco", "docs/" + fname);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.print("ERROR-------" + e);
                }
            }

            conveniosJpaController.edit(conveniosAnterior);
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
