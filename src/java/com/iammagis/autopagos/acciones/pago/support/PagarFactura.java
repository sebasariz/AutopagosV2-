/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.pago.support;

import com.iammagis.autopagos.jpa.beans.Banco;
import com.iammagis.autopagos.jpa.beans.ComprobanterecaudoPSE;
import com.iammagis.autopagos.jpa.beans.Config;
import com.iammagis.autopagos.jpa.beans.Convenios;
import com.iammagis.autopagos.jpa.beans.Estado;
import com.iammagis.autopagos.jpa.beans.Factura;
import com.iammagis.autopagos.jpa.beans.FacturaAutopagos;
import com.iammagis.autopagos.jpa.beans.TipoTransaccion;
import com.iammagis.autopagos.jpa.beans.Transaccion;
import com.iammagis.autopagos.jpa.beans.Usuario;
import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import com.iammagis.autopagos.jpa.control.BancoJpaController;
import com.iammagis.autopagos.jpa.control.ComprobanterecaudoPSEJpaController;
import com.iammagis.autopagos.jpa.control.ConfigJpaController;
import com.iammagis.autopagos.jpa.control.FacturaAutopagosJpaController;
import com.iammagis.autopagos.jpa.control.FacturaJpaController;
import com.iammagis.autopagos.jpa.control.TransaccionJpaController;
import com.iammagis.autopagos.jpa.control.UsuarioJpaController;
import com.iammagis.autopagos.jpa.control.exceptions.NonexistentEntityException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.UUID;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class PagarFactura {

    static EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
    static PropertiesAccess propertiesAccess = new PropertiesAccess();

    public static String pagarPSESipar(ComprobanterecaudoPSE comprobanterecaudoPSE, Banco banco) throws IOException, JSONException, Exception {
        //buscamos si el susuario registrado con dicho email existe en autoapagos
        ArrayList<Transaccion> transaccions = new ArrayList<>(comprobanterecaudoPSE.getTransaccionCollection());
        if (!transaccions.isEmpty()) {
            FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
            Convenios convenios = transaccions.get(0).getFacturaIdfactura().getConveniosIdconvenios();
            String retorno = "";
            Factura factura;
            System.out.println("convenios.getCanalIdcanalPlanPost(): " + convenios.getCanalIdcanalPlanPost());
            if (null != convenios.getCanalIdcanalPlanPost().getIdcanal()) {
                switch (convenios.getCanalIdcanalPlanPost().getIdcanal()) {
                    case 1:
                        System.out.println("por bbva");
                        factura = new ArrayList<>(comprobanterecaudoPSE.getTransaccionCollection()).get(0).getFacturaIdfactura();
                        factura.setEstadoIdestado(new Estado(4));
                        facturaJpaController.edit(factura);
                        HttpAutomatizationBBVARequestPaymentIndependiente httpAutomatizationBBVARequestPayment = new HttpAutomatizationBBVARequestPaymentIndependiente();
                        httpAutomatizationBBVARequestPayment.init(convenios);
                        httpAutomatizationBBVARequestPayment.getInicialForm(comprobanterecaudoPSE);
                        retorno = httpAutomatizationBBVARequestPayment.secongCall(banco.getCodigo(), "0");
                        //cambiar el id del convenio para que recaude directamente
                        break;
                    case 2:
                        System.out.println("por bancolombia convenio");
                        //si el canal es de bncolombia
                        //analizamos si es referenciada o no
                        System.out.println("pago sin referencia");
                        factura = new ArrayList<>(comprobanterecaudoPSE.getTransaccionCollection()).get(0).getFacturaIdfactura();
                        factura.setEstadoIdestado(new Estado(4));
                        facturaJpaController.edit(factura);
                        HttpAutomatizationBancolombiaRequestPayment httpAutomatization = new HttpAutomatizationBancolombiaRequestPayment();
                        httpAutomatization.init(convenios);
                        httpAutomatization.getInicialForm(comprobanterecaudoPSE, convenios);
                        retorno = httpAutomatization.secongCall(banco.getCodigo(), "0");

                        break;
                    case 3:
                        //aqui va el canal de avvillas
                        System.out.println("por avvillas");
                        retorno = PagarFactura.pagarPSEAutopagos(comprobanterecaudoPSE, banco, convenios);
                        JSONObject jSONObjectRetorno = new JSONObject(retorno);
                        ComprobanterecaudoPSEJpaController comprobanterecaudoPSEJpaController = new ComprobanterecaudoPSEJpaController(manager);
                        comprobanterecaudoPSE.setIdAvvillas(jSONObjectRetorno.getInt("pagoId"));
                        comprobanterecaudoPSE.setEstadoIdestado(new Estado(4));
                        comprobanterecaudoPSEJpaController.edit(comprobanterecaudoPSE);
                        if (!jSONObjectRetorno.getBoolean("ok")) {
                            System.out.println("no podemos procesar la transaccion");
                        } else {
                            factura = new ArrayList<>(comprobanterecaudoPSE.getTransaccionCollection()).get(0).getFacturaIdfactura();
                            factura.setEstadoIdestado(new Estado(4));
                            facturaJpaController.edit(factura);
                            retorno = jSONObjectRetorno.getString("dato");
                        }
                        break;
                    default:
                        break;
                }
            }
            return retorno;
        }
        return null;
    }

    public static String pagarPSEOnline(ComprobanterecaudoPSE comprobanterecaudoPSE, Banco banco) throws IOException, JSONException, Exception {

        String retorno = PagarFactura.pagarPSEAutopagos(comprobanterecaudoPSE, banco, null);
        JSONObject jSONObjectRetorno = new JSONObject(retorno);
        ComprobanterecaudoPSEJpaController comprobanterecaudoPSEJpaController = new ComprobanterecaudoPSEJpaController(manager);
        comprobanterecaudoPSE.setIdAvvillas(jSONObjectRetorno.getInt("pagoId"));
        comprobanterecaudoPSE.setEstadoIdestado(new Estado(4));
        comprobanterecaudoPSEJpaController.edit(comprobanterecaudoPSE);
        if (!jSONObjectRetorno.getBoolean("ok")) {
            System.out.println("no podemos procesar la transaccion");
        } else {
            FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
            Factura factura = new ArrayList<>(comprobanterecaudoPSE.getTransaccionCollection()).get(0).getFacturaIdfactura();
            factura.setEstadoIdestado(new Estado(4));
            facturaJpaController.edit(factura);
            retorno = jSONObjectRetorno.getString("dato");
        }
        return retorno;

    }

    public static ComprobanterecaudoPSE generarTransaccionInicial(Factura factura) throws NonexistentEntityException, Exception {
        //si la factura ya existe en el sistema se busca, si la factura no existe se crea
        //transaccion si tiene comision de usuario
        double valorPagado = factura.getValorPagado();

        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(manager);
        Usuario usuario = new Usuario();
        String[] emails = factura.getEmail().split(",");
        ArrayList<Usuario> usuarios = new ArrayList<>();
        for (String email : emails) {
            if (email != null) {
                usuario = new Usuario();
                usuario.setEmail(email);
                usuario = usuarioJpaController.emailExist(usuario);
                if (usuario != null) {
                    usuarios.add(usuario);
                }
            }
        }
        long fecha = System.currentTimeMillis();
        FacturaJpaController facturaJpaController = new FacturaJpaController(manager);
        if (factura.getIdfactura() != null && factura.getIdfactura() != 0) {
            System.out.println("============== creando porque factura existe: " + factura.getIdfactura());
            //la factura ya existe 
            factura = facturaJpaController.findFactura(factura.getIdfactura());
            factura.setValorPagado(valorPagado);
        } else {
            //creamos una nueva 
            System.out.println("============== creando porque factura no existe");
            factura.setFechaCreacion(BigInteger.valueOf(fecha));
            factura.setUsuarioCollection(usuarios);
            factura.setEstadoIdestado(new Estado(4));
            factura.setFechaEmision(BigInteger.valueOf(fecha));
            factura.setFechaVencimiento(BigInteger.valueOf(fecha));
            factura.setValorPagado(valorPagado);
            factura = facturaJpaController.create(factura);
        }
        if (factura != null) {
            System.out.println("entando por factura no nula: " + factura.getIdfactura());
            Convenios convenios = factura.getConveniosIdconvenios();
            ArrayList<Transaccion> transaccions = new ArrayList<Transaccion>();
            double valorComisionUsuario = 0;

            if (convenios.getValorFijoUsuario() != null && convenios.getValorFijoUsuario() != 0) {
                valorComisionUsuario += convenios.getValorFijoUsuario();
            }
            if (convenios.getValorVariableUsuario() != null && convenios.getValorVariableUsuario() != 0) {
                valorComisionUsuario += convenios.getValorVariableUsuario() * factura.getValorPagado() / 100;
            }

            if (valorComisionUsuario > 0) {
                //miramos si el convenio es de tipo sipar y generamos comision de usuario si es mayor a cero sin iva
                //generamos la transaccion de comisiond e usuario
                Transaccion transaccionComisionUsuario = new Transaccion();
                transaccionComisionUsuario.setFecha(BigInteger.valueOf(fecha));
                transaccionComisionUsuario.setEstadoIdestado(new Estado(4));
                transaccionComisionUsuario.setFacturaIdfactura(factura);
                transaccionComisionUsuario.setIdTransaccion(UUID.randomUUID().toString());
                transaccionComisionUsuario.setTipoTransaccionidtipoTransaccion(new TipoTransaccion(3));
                transaccionComisionUsuario.setUsuarioidUsuario(usuario);
                transaccionComisionUsuario.setValor(valorComisionUsuario * -1);
                transaccionComisionUsuario.setEmail(factura.getEmail());
                transaccionComisionUsuario.setReferencia(factura.getReferencia());
                transaccionComisionUsuario.setConveniosIdconvenios(factura.getConveniosIdconvenios());
                transaccions.add(transaccionComisionUsuario);
                if (convenios.getTipoConvenio() == 2) {
                    //es online generamos el iva de la transaccion de usuario
                    Transaccion transaccionIvaComisionUsuario = new Transaccion();
                    transaccionIvaComisionUsuario.setFecha(BigInteger.valueOf(fecha));
                    transaccionIvaComisionUsuario.setEstadoIdestado(new Estado(4));
                    transaccionIvaComisionUsuario.setFacturaIdfactura(factura);
                    transaccionIvaComisionUsuario.setIdTransaccion(UUID.randomUUID().toString());
                    transaccionIvaComisionUsuario.setTipoTransaccionidtipoTransaccion(new TipoTransaccion(4));
                    transaccionIvaComisionUsuario.setUsuarioidUsuario(usuario);
                    transaccionIvaComisionUsuario.setValor(valorComisionUsuario * propertiesAccess.IVA * -1);
                    transaccionIvaComisionUsuario.setEmail(factura.getEmail());
                    transaccionIvaComisionUsuario.setReferencia(factura.getReferencia());
                    transaccionIvaComisionUsuario.setConveniosIdconvenios(factura.getConveniosIdconvenios());
                    transaccions.add(transaccionIvaComisionUsuario);
                }
            }

            //ahora la transaccion de la comision del convenio, solo si es online
            if (convenios.getTipoConvenio() == 2) {
                double valorComisionConvenio = 0;
                if (convenios.getValorFijoConvenio() != null && convenios.getValorFijoConvenio() != 0) {
                    valorComisionConvenio += convenios.getValorFijoConvenio();
                }
                if (convenios.getValorVariableConvenio() != null && convenios.getValorVariableConvenio() != 0) {
                    valorComisionConvenio += convenios.getValorVariableConvenio() * factura.getValorPagado() / 100;
                }

                if (valorComisionConvenio > 0) {
                    Transaccion transaccionComisionConvenio = new Transaccion();
                    transaccionComisionConvenio.setFecha(BigInteger.valueOf(fecha));
                    transaccionComisionConvenio.setEstadoIdestado(new Estado(4));
                    transaccionComisionConvenio.setFacturaIdfactura(factura);
                    transaccionComisionConvenio.setIdTransaccion(UUID.randomUUID().toString());
                    transaccionComisionConvenio.setTipoTransaccionidtipoTransaccion(new TipoTransaccion(1));
                    transaccionComisionConvenio.setUsuarioidUsuario(usuario);
                    transaccionComisionConvenio.setValor(valorComisionConvenio * -1);
                    transaccionComisionConvenio.setEmail(factura.getEmail());
                    transaccionComisionConvenio.setReferencia(factura.getReferencia());
                    transaccionComisionConvenio.setConveniosIdconvenios(factura.getConveniosIdconvenios());
                    transaccions.add(transaccionComisionConvenio);

                    Transaccion transaccionIvaComisionConvenio = new Transaccion();
                    transaccionIvaComisionConvenio.setFecha(BigInteger.valueOf(fecha));
                    transaccionIvaComisionConvenio.setEstadoIdestado(new Estado(4));
                    transaccionIvaComisionConvenio.setFacturaIdfactura(factura);
                    transaccionIvaComisionConvenio.setIdTransaccion(UUID.randomUUID().toString());
                    transaccionIvaComisionConvenio.setTipoTransaccionidtipoTransaccion(new TipoTransaccion(2));
                    transaccionIvaComisionConvenio.setUsuarioidUsuario(usuario);
                    transaccionIvaComisionConvenio.setValor(valorComisionConvenio * propertiesAccess.IVA * -1);
                    transaccionIvaComisionConvenio.setEmail(factura.getEmail());
                    transaccionIvaComisionConvenio.setReferencia(factura.getReferencia());
                    transaccionIvaComisionConvenio.setConveniosIdconvenios(factura.getConveniosIdconvenios());
                    transaccions.add(transaccionIvaComisionConvenio);
                }
            }

            //listas las comisiones y transacciones falta la transaccion del pago de la lfactura
            Transaccion transaccionPago = new Transaccion();
            transaccionPago.setFecha(BigInteger.valueOf(fecha));
            transaccionPago.setEstadoIdestado(new Estado(4));
            transaccionPago.setFacturaIdfactura(factura);
            transaccionPago.setIdTransaccion(UUID.randomUUID().toString());
            transaccionPago.setTipoTransaccionidtipoTransaccion(new TipoTransaccion(6));
            transaccionPago.setUsuarioidUsuario(usuario);
            transaccionPago.setValor(factura.getValorPagado());
            transaccionPago.setEmail(factura.getEmail());
            transaccionPago.setReferencia(factura.getReferencia());
            transaccionPago.setConveniosIdconvenios(factura.getConveniosIdconvenios());
            transaccions.add(transaccionPago);

            ComprobanterecaudoPSE comprobanterecaudoPSE = new ComprobanterecaudoPSE();
            comprobanterecaudoPSE.setDescripcion("Factura: " + convenios.getNombre() + " Ref: " + factura.getReferencia());
            comprobanterecaudoPSE.setEstadoIdestado(new Estado(4));
            comprobanterecaudoPSE.setFecha(BigInteger.valueOf(System.currentTimeMillis()));
            comprobanterecaudoPSE.setValorTotal(factura.getValorPagado() + valorComisionUsuario);

            ComprobanterecaudoPSEJpaController comprobanterecaudoPSEJpaController = new ComprobanterecaudoPSEJpaController(manager);
            comprobanterecaudoPSE = comprobanterecaudoPSEJpaController.create(comprobanterecaudoPSE);
            TransaccionJpaController transaccionJpaController = new TransaccionJpaController(manager);
            for (Transaccion transaccion : transaccions) {
                transaccion.setComprobanteRecaudoPSEidcomprobanteRecaudoPSE(comprobanterecaudoPSE);
                transaccionJpaController.create(transaccion);
            }
            comprobanterecaudoPSE = comprobanterecaudoPSEJpaController.findComprobanterecaudoPSE(comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE());
            System.out.println("comprobanterecaudoPSE: " + comprobanterecaudoPSE);
            factura = facturaJpaController.findFactura(factura.getIdfactura());
            factura.setEstadoIdestado(new Estado(4));
            facturaJpaController.edit(factura);
            //sout  aumentamos las transacciones realizadas por cada uno
            ConfigJpaController configJpaController = new ConfigJpaController(manager);
            Config config = configJpaController.findConfig(1);
            config.setSiparNumeroTransacciones(config.getSiparNumeroTransacciones() + 1);
            configJpaController.edit(config);

            return comprobanterecaudoPSE;
        } else {
            return null;
        }

    }

    public static ComprobanterecaudoPSE generarTransaccionInicialAutopagos(FacturaAutopagos factura) throws NonexistentEntityException, Exception {
        //si la factura ya existe en el sistema se busca, si la factura no existe se crea
        //transaccion si tiene comision de usuario
        String email = "";
        ArrayList<Usuario> usuarios = new ArrayList<>(factura.getConveniosIdconvenios().getUsuarioCollection());
        for (Usuario usuario : usuarios) {
            email += usuario.getEmail() + ",";
        }
        email = email.substring(0, email.length() - 1);

        long fecha = System.currentTimeMillis();
        FacturaAutopagosJpaController facturaJpaController = new FacturaAutopagosJpaController(manager);

        if (factura.getIdfacturaAutopagos() != null && factura.getIdfacturaAutopagos() != 0) {
            //la factura ya existe 
            factura = facturaJpaController.findFacturaAutopagos(factura.getIdfacturaAutopagos());
        }
        if (factura != null) {
            Convenios convenios = factura.getConveniosIdconvenios();
            ArrayList<Transaccion> transaccions = new ArrayList<Transaccion>();

            //listas las comisiones y transacciones falta la transaccion del pago de la lfactura
            Transaccion transaccionPago = new Transaccion();
            transaccionPago.setFecha(BigInteger.valueOf(fecha));
            transaccionPago.setEstadoIdestado(new Estado(4));
            transaccionPago.setFacturaAutopagosIdfacturaAutopagos(factura);
            transaccionPago.setIdTransaccion(UUID.randomUUID().toString());
            transaccionPago.setTipoTransaccionidtipoTransaccion(new TipoTransaccion(6));
            transaccionPago.setValor(factura.getValor());
            transaccionPago.setEmail(email);
            transaccionPago.setReferencia(factura.getIdfacturaAutopagos() + "");
            transaccionPago.setConveniosIdconvenios(factura.getConveniosIdconvenios());
            transaccions.add(transaccionPago);

            ComprobanterecaudoPSE comprobanterecaudoPSE = new ComprobanterecaudoPSE();
            comprobanterecaudoPSE.setDescripcion("Factura SIPAR: " + convenios.getNombre() + " Ref: " + factura.getIdfacturaAutopagos());
            comprobanterecaudoPSE.setEstadoIdestado(new Estado(4));
            comprobanterecaudoPSE.setFecha(BigInteger.valueOf(System.currentTimeMillis()));
            comprobanterecaudoPSE.setValorTotal(factura.getValor());

            factura.setEstadoIdestado(new Estado(4));
            facturaJpaController.edit(factura);

            ComprobanterecaudoPSEJpaController comprobanterecaudoPSEJpaController = new ComprobanterecaudoPSEJpaController(manager);
            comprobanterecaudoPSE = comprobanterecaudoPSEJpaController.create(comprobanterecaudoPSE);
            TransaccionJpaController transaccionJpaController = new TransaccionJpaController(manager);
            for (Transaccion transaccion : transaccions) {
                transaccion.setComprobanteRecaudoPSEidcomprobanteRecaudoPSE(comprobanterecaudoPSE);
                transaccionJpaController.create(transaccion);
            }
            comprobanterecaudoPSE = comprobanterecaudoPSEJpaController.findComprobanterecaudoPSE(comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE());

            return comprobanterecaudoPSE;
        } else {
            return null;
        }

    }

    public static String pagarPSEAutopagos(ComprobanterecaudoPSE comprobanterecaudoPSE, Banco banco, Convenios convenios) throws IOException, JSONException, JSONException {
        String url = propertiesAccess.AVVILLASLINK;
        String correo = propertiesAccess.CORREOAVVILLAS;
        HttpAutomatizationAVVILLASRequest httpAutomatizationAVVILLASRequest = new HttpAutomatizationAVVILLASRequest();
        String retorno = "";
        if (convenios != null) {
            retorno = httpAutomatizationAVVILLASRequest.getTokenPagePSECargaRecarga(url, propertiesAccess.CODIGOCLIENTE, comprobanterecaudoPSE.getDescripcion(), banco.getCodigo(), correo, comprobanterecaudoPSE.getValorTotal() + "", convenios.getCodigoCanalPlanPost(), comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE());
        } else {
            retorno = httpAutomatizationAVVILLASRequest.getTokenPagePSECargaRecarga(url, propertiesAccess.CODIGOCLIENTE, comprobanterecaudoPSE.getDescripcion(), banco.getCodigo(), correo, comprobanterecaudoPSE.getValorTotal() + "", propertiesAccess.CODIGOCONVENIOAVVILLAS, comprobanterecaudoPSE.getIdcomprobanteRecaudoPSE());
        }
        return retorno;
    }

    public static void main(String[] argts) throws IOException, JSONException, Exception {

        EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
        ComprobanterecaudoPSEJpaController comprobanterecaudoPSEJpaController = new ComprobanterecaudoPSEJpaController(manager);
        ComprobanterecaudoPSE comprobanterecaudoPSE = comprobanterecaudoPSEJpaController.findComprobanterecaudoPSE(7594);
        BancoJpaController bancoJpaController = new BancoJpaController(manager);
        Banco banco = bancoJpaController.findBanco(14);
        System.out.println("comprobanterecaudoPSE: " + comprobanterecaudoPSE.getValorTotal());
//        JSONObject jSONObjectRetorno = new JSONObject(retorno);
        String retorno = PagarFactura.pagarPSESipar(comprobanterecaudoPSE, banco);
        System.out.println("retorno: " + retorno);
    }

}

