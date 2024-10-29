/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import com.iammagis.autopagos.jpa.beans.Modulo;
import com.iammagis.autopagos.jpa.beans.SubModulo;
import com.iammagis.autopagos.jpa.beans.Usuario;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class DynamicMenu {

    public static String getMenu(Usuario usuario) {

        String menu = "";

        ArrayList<Modulo> modulos = new ArrayList<>(usuario.getModuloCollection());
        ArrayList<SubModulo> subModulos = new ArrayList<>(usuario.getSubModuloCollection()); 
        for (Modulo modulo : modulos) {
            menu += "<li class=\"has_sub\">"
                    + "<a href=\"javascript:void(0);\" class=\"waves-effect waves-primary\"><i class=\"" + modulo.getIcon() + "\"></i> <span> " + modulo.getNombre() + " </span>"
                    + "<span class=\"menu-arrow\"></span></a>"
                    + "<ul class=\"list-unstyled\">";
            for (SubModulo subModulo : subModulos) {
                if (modulo.getIdmodulo().intValue() == subModulo.getModuloIdmodulo().getIdmodulo().intValue()) {
                    menu += "<li><a href=\"" + subModulo.getAccion() + ".ap\">" + subModulo.getNombre() + "</a></li>"; 
                }
            }
            menu += "</ul>"
                    + "</li>";
        }

        return menu;
    }

}
