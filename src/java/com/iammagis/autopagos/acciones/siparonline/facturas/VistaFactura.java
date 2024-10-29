/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.siparonline.facturas;

import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.control.ConveniosJpaController;
import com.iammagis.autopagos.jpa.control.FacturaJpaController;
import java.io.IOException;
import java.io.OutputStream;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class VistaFactura extends HttpServlet {

    private final byte[] singlePixelTransparentShimBytes = {71, 73, 70, 56, 57, 97, 1, 0, 1, 0, -128, 0, 0, -1, -1, -1, 0, 0, 0, 33, -7, 4, 1, 0, 0, 0, 0, 44, 0, 0, 0, 0, 1, 0, 1, 0, 0, 2, 2, 68, 1, 0, 59};

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            if (request.getParameter("id") == null && request.getParameter("token") == null) {
                return;
            }

            String token = request.getParameter("token");
            int idFactura = Integer.parseInt(request.getParameter("id"));
            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            ConveniosJpaController conveniosJpaController = new ConveniosJpaController(manager);
            Convenios convenios = conveniosJpaController.findByToken(token);
            if (convenios != null) {
                FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
                Factura factura = facturaJpaController.findFactura(idFactura);
                if (factura != null && convenios != null
                        && (factura.getConveniosIdconvenios().getIdconvenios().intValue() == convenios.getIdconvenios().intValue())) {
                    //es el mismo 
                    factura.setVisto(Boolean.TRUE);
                    facturaJpaController.edit(factura);
                }

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setContentType("image/gif");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(singlePixelTransparentShimBytes);
        outputStream.flush();

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Autopagos V2";
    }// </editor-fold>

}
