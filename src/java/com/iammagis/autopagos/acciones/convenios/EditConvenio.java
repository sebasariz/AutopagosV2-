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
public class EditConvenio extends org.apache.struts.action.Action {

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
            Convenios convenio = (Convenios) form;
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
            Convenios conveniosAnterior = conveniosJpaController.findConvenios(convenio.getIdconvenios());

            ClaseJpaController claseJpaController = new ClaseJpaController(manager);
            Clase clase = claseJpaController.findClase(convenio.getIdClase());
            conveniosAnterior.setClaseIdclase(clase);
            //buscando la clase
            TipoCuentaJpaController tipoCuentaJpaController = new TipoCuentaJpaController(manager);
            TipoCuenta tipoCuenta = tipoCuentaJpaController.findTipoCuenta(convenio.getIdTipoCuenta());
            conveniosAnterior.setTipoCuentaidtipoCuenta(tipoCuenta);

            //setiamos los campos anteriores
            conveniosAnterior.setNombre(convenio.getNombre());
            conveniosAnterior.setCodigo(convenio.getCodigo());
            conveniosAnterior.setTextoGuiaTercero(convenio.getTextoGuiaTercero());
            conveniosAnterior.setNit(convenio.getNit());
            conveniosAnterior.setDireccion(convenio.getDireccion());
            conveniosAnterior.setTelefono(convenio.getTelefono());
            conveniosAnterior.setValorFijoUsuario(convenio.getValorFijoUsuario());
            conveniosAnterior.setValorVariableUsuario(convenio.getValorVariableUsuario());
            conveniosAnterior.setBancoTercero(convenio.getBancoTercero());
            conveniosAnterior.setDireccionRespuesta(convenio.getDireccionRespuesta());
            conveniosAnterior.setActivo(convenio.getActivo());
            conveniosAnterior.setValorVariable(convenio.getValorVariable());
            conveniosAnterior.setTipoConvenio(convenio.getTipoConvenio());
            conveniosAnterior.setCampoIdentificadorSipar(convenio.getCampoIdentificadorSipar());
            conveniosAnterior.setCamposObligatoriosSipar(convenio.getCamposObligatoriosSipar());
            //sipar
            conveniosAnterior.setFechaCortePlanPost(convenio.getFechaCortePlanPost());
            PlanJpaController planJpaController = new PlanJpaController(manager);
            Plan planes = planJpaController.findPlan(convenio.getIdPlan());
            conveniosAnterior.setPlanIdplan(planes);
            CanalJpaController canalJpaController = new CanalJpaController(manager);
            Canal canal = canalJpaController.findCanal(convenio.getIdCanal());
            conveniosAnterior.setCanalIdcanalPlanPost(canal);
            conveniosAnterior.setCodigoCanalPlanPost(convenio.getCodigoCanalPlanPost());
            conveniosAnterior.setUsuarioCanalPlanPost(convenio.getUsuarioCanalPlanPost());
            conveniosAnterior.setPassCanalPlanPost(convenio.getPassCanalPlanPost());
            //onlie
            conveniosAnterior.setNumeroCuenta(convenio.getNumeroCuenta());
            conveniosAnterior.setTitularCuenta(convenio.getTitularCuenta());
            conveniosAnterior.setBanco(convenio.getBanco());
            conveniosAnterior.setValorFijoConvenio(convenio.getValorFijoConvenio());
            conveniosAnterior.setValorVariableConvenio(convenio.getValorVariableConvenio());
            //corresponsalia
            conveniosAnterior.setCodigoProductoTercero(convenio.getCodigoProductoTercero());
            conveniosAnterior.setComisionTerceroVariable(convenio.getComisionTerceroVariable());
            conveniosAnterior.setComisionTerceroFija(convenio.getComisionTerceroFija());
            conveniosAnterior.setComisionTerceroVariable(convenio.getComisionTerceroVariable());
            conveniosAnterior.setCodigoBarrasTercero(convenio.getCodigoBarrasTercero());
            conveniosAnterior.setRecargaTercero(convenio.getRecargaTercero());
            //subimos le logo
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
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print("ERROR-------" + e);
            }
            FormFile ccio = convenio.getLogoFileFormCCio();
            File ccioFile = null;
            if (ccio != null) {
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.print("ERROR-------" + e);
                }
            }
            FormFile rut = convenio.getLogoFileFormRut();
            File rutFile = null;
            if (rut != null) {
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.print("ERROR-------" + e);
                }
            }
            FormFile banco = convenio.getLogoFileFormBanco();
            File bancoFile = null;
            if (banco != null) {
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.print("ERROR-------" + e);
                }
            }

            conveniosJpaController.edit(conveniosAnterior);

            //cargamos la lista de los convenios
            ArrayList<Convenios> convenioses = new ArrayList<>(conveniosJpaController.findConveniosEntities());
            jSONObject = DataGrid.getConveniosData(convenioses);
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
