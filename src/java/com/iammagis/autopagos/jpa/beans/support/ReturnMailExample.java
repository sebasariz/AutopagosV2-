/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import com.iammagis.autopagos.jpa.beans.ComprobanterecaudoPSE;
import com.iammagis.autopagos.jpa.control.ComprobanterecaudoPSEJpaController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author sebastianarizmendy
 */
public class ReturnMailExample {

    private static final PropertiesAccess propertiesAccess = new PropertiesAccess();

    public static void main(String[] args) throws JSONException, Exception {
        String recipient = "avvillas@correo.appropiate.app";
//        String dataReceive = "         Wed, 18 Sep 2019 11:20:39 -0500 (COT)\n"
//                + "DKIM-Signature: v=1; a=rsa-sha256; d=bancoavvillas.com.co; s=bavvillas; c=relaxed/simple;\n"
//                + "	q=dns/txt; i=@bancoavvillas.com.co; t=1568821500; x=1663429500;\n"
//                + "	h=From:Sender:Reply-To:Subject:Date:Message-ID:To:Cc:MIME-Version:Content-Type:\n"
//                + "	Content-Transfer-Encoding:Content-ID:Content-Description:Resent-Date:Resent-From:\n"
//                + "	Resent-Sender:Resent-To:Resent-Cc:Resent-Message-ID:In-Reply-To:References:List-Id:\n"
//                + "	List-Help:List-Unsubscribe:List-Subscribe:List-Post:List-Owner:List-Archive;\n"
//                + "	bh=H6vnNYEUn/wJjEbyc4hiMPpkXd8fYwMSI4ciN72mBN0=;\n"
//                + "	b=Mfo80GQhkrw3uTrIIT6xxzAA5U09DoQ6ytY1cVz6tLZNaBaAXiQghc7sdGaeGZ4/\n"
//                + "	W//D2X69QiTWFCCiAX2ecrvgxSsP8kqaH3frz7/oMTfJFHDvO5HY6wPZ/pUEEBEm\n"
//                + "	5AsIatalL+E/ZCG/ioLzdER6voCkqW7zUDBdwGSqWi0=;\n"
//                + "X-AuditID: c0a80113-0ffff70000002814-d9-5d8250fc01ec\n"
//                + "Received: from ATHMBXCONT.ath.net ( [10.129.0.51])\n"
//                + "	(using TLS with cipher ECDHE-RSA-AES128-SHA256 (128/128 bits))\n"
//                + "	(Client did not present a certificate)\n"
//                + "	by mail2.ath.com.co (Symantec Mail Security) with SMTP id 3B.5B.10260.CF0528D5; Wed, 18 Sep 2019 10:45:00 -0500 (-05)\n"
//                + "Received: from avveoffprd01 (10.129.61.127) by athmbxcont.ath.net\n"
//                + " (10.129.0.51) with Microsoft SMTP Server id 14.3.439.0; Wed, 18 Sep 2019\n"
//                + " 11:21:20 -0500\n"
//                + "Date: Wed, 18 Sep 2019 11:21:20 -0500\n"
//                + "From: <OficinaInternet@bancoavvillas.com.co>\n"
//                + "To: <avvillas@autopagos.com>\n"
//                + "Message-ID: <-63943228.8751568823680893.JavaMail.root@avveoffprd01>\n"
//                + "Subject: Tu pago ha sido rechazado.\n"
//                + "MIME-Version: 1.0\n"
//                + "Content-Type: text/html; charset=\"UTF-8\"\n"
//                + "Content-Transfer-Encoding: quoted-printable\n"
//                + "Return-Path: OficinaInternet@bancoavvillas.com.co\n"
//                + "X-CFilter-Loop: Reflected\n"
//                + "X-Brightmail-Tracker: H4sIAAAAAAAAA+NgFmphluLIzCtJLcpLzFFi42Lh6mER0f0T0BRrsGi5ssWZ/XPZHRg9Fp27\n"
//                + "	yxjAGMVlk5Kak1mWWqRvl8CVsWh1N2vBDpGKJz/nsTQwvhHoYuTgkBAwkdj6zqyLkYtDSGAK\n"
//                + "	k8S3KcdZIZy5jBLr369l7GLk5GARUJX48OYdmM0moCOxfcFOFhBbREBGYuWts+wgg3gFnCWe\n"
//                + "	vWQCCQsLKEt8+LEBzOYVEJQ4OfMJWDmzgIbEgjv7GCFsbYllC18zg9hCAoYSTde/g8UlBCQl\n"
//                + "	Dq64wTKBkXcWkvZZSNpnIWlfwMi8ilGkODkxLy+1yMBQL7EkQy85PxeINzECw+bACkbhHYwz\n"
//                + "	d7kdYhTgYFTi4Z1v2xQrxJpYVlyZe4hRgoNZSYR3jilQiDclsbIqtSg/vqg0J7X4EKM0B4uS\n"
//                + "	OO857YpYIYH0xJLU7NTUgtQimCwTB6dUA6PVljPXefrzi/MfLMwpesG+5Kzr8bn6R3+LfV/1\n"
//                + "	2PB729zeq+2qWktLl30+Irk+piak+ezn0Pj5gmlFs75MObPXp09VON4rd963HSc/rZ+6ktvi\n"
//                + "	PlO/3WTLsKdHb0os9PwyW8XknCLnuWD1QNbyQz/YMm59v9d2/Xiz5ek0A0ML0cbuQ7ItG5iU\n"
//                + "	WIozEg21mIuKEwH0FFKhFwIAAA==\n"
//                + "\n"
//                + "<html>\n"
//                + "<head>\n"
//                + "<meta http-equiv=3D\"Content-Type\" content=3D\"text/html; charset=3Dutf-8\" />\n"
//                + "</head>\n"
//                + "<body>\n"
//                + "<div style=3D\"border: 1px #183F6C solid;padding: 3px;font-size:12px\">\n"
//                + "<p>La transacci=C3=B3n <strong>n=C3=BAmero <span>8397619</span></strong>=20\n"
//                + "con la <strong>entidad financiera <span style=3D\"color:#6E6E6E\">BANCOLOMBIA=\n"
//                + "</span></strong>,=20\n"
//                + "de la fecha 18-sep-2019 11:21:20 AM fue:=20\n"
//                + "<strong><span style=3D\"color: red\">RECHAZADA</span></strong>.</p>\n"
//                + "\n"
//                + "<table width=3D\"100%\" cellpadding=3D\"5px\" style=3D\"font-size:12px\">\n"
//                + "    <tr>\n"
//                + "        <th width=3D\"50%\" colspan=3D\"2\" scope=3D\"col\" style=3D\"text-align:l=\n"
//                + "eft;background-color:#f5fbff;padding:5px;color:#0059AB\">Informaci=C3=B3n de=\n"
//                + "l proveedor del servicio:</th>\n"
//                + "        <th width=3D\"50%\" colspan=3D\"2\" scope=3D\"col\" style=3D\"text-align:l=\n"
//                + "eft;background-color:#f5fbff;padding:5px;color:#0059AB;font-size:12px\">Info=\n"
//                + "rmaci=C3=B3n del cliente que efectu=C3=B3 el pago:</th>\n"
//                + "    </tr>\n"
//                + "    <tr>\n"
//                + "        <td colspan=3D\"2\">\n"
//                + "        =09<span style=3D\"color:#0059AB\">Raz=C3=B3n social:</span><br />Ban=\n"
//                + "co Comercial AV Villas\n"
//                + "        </td>\n"
//                + "        <td colspan=3D\"2\">\n"
//                + "        =09<span style=3D\"color:#0059AB\">IP:</span><br />192.168.2.86\n"
//                + "        </td>\n"
//                + "    </tr>\n"
//                + "    <tr>\n"
//                + "        <td colspan=3D\"4\">\n"
//                + "        =09<span style=3D\"color:#0059AB\">NIT:</span><br />860035827\n"
//                + "        </td>\n"
//                + "    </tr>\n"
//                + "    <tr>\n"
//                + "        <th colspan=3D\"4\" style=3D\"text-align:left;background-color:#f5fbff=\n"
//                + ";padding:5px;color:#0059AB\">Informaci=C3=B3n del pago:</th>\n"
//                + "    </tr>\n"
//                + "    <tr>\n"
//                + "    =09<td><strong>N=C2=BA Pago:</strong></td>\n"
//                + "    =09<td>8397619</td>\n"
//                + "    =09<td><strong>N=C2=BA Transacci=C3=B3n:</strong></td>\n"
//                + "    =09<td>496674900</td>\n"
//                + "    </tr>\n"
//                + "    <tr>\n"
//                + "    =09<td><strong>Medio de Pago:</strong></td>\n"
//                + "    =09<td>Pago con PSE</td>\n"
//                + "    =09<td><strong>Descripci=C3=B3n:</strong></td>\n"
//                + "    =09<td>Factura: Autopagos APP Ref: ref</td>\n"
//                + "    </tr>\n"
//                + "</table>\n"
//                + "\n"
//                + "<p style=3D\"text-align:center;color:#0059AB;font-weight:bold\">Convenio: AUT=\n"
//                + "OPAGOS - CLL 32 E No.80 A - 57 INT 201</p>\n"
//                + "<table width=3D\"100%\" style=3D\"font-size:12px\">\n"
//                + "    <tr>\n"
//                + "        <th scope=3D\"col\" style=3D\"text-align:left;background-color:#f5fbff=\n"
//                + ";padding:5px;color:#0059AB\">REFERENCIA:</th>\n"
//                + "        <th scope=3D\"col\" style=3D\"text-align:right;background-color:#f5fbf=\n"
//                + "f;padding:5px;color:#0059AB\">Valor:</th>\n"
//                + "    </tr>\n"
//                + "    <tr>\n"
//                + "    =09<td style=3D\"vertical-align:top\">6206</td>\n"
//                + "    =09<td style=3D\"vertical-align:top;text-align:right\">$2,333</td>\n"
//                + "    </tr>\n"
//                + "</table>\n"
//                + "\n"
//                + "\n"
//                + "</div><br /><br />Cordialmente,<br /><br />Centro de Pagos Virtual</font></=\n"
//                + "body></html>";
        String dataReceive = "         Wed, 18 Sep 2019 10:52:36 -0500 (COT)\n"
                + "DKIM-Signature: v=1; a=rsa-sha256; d=bancoavvillas.com.co; s=bavvillas; c=relaxed/simple;\n"
                + "	q=dns/txt; i=@bancoavvillas.com.co; t=1568821833; x=1663429833;\n"
                + "	h=From:Sender:Reply-To:Subject:Date:Message-ID:To:Cc:MIME-Version:Content-Type:\n"
                + "	Content-Transfer-Encoding:Content-ID:Content-Description:Resent-Date:Resent-From:\n"
                + "	Resent-Sender:Resent-To:Resent-Cc:Resent-Message-ID:In-Reply-To:References:List-Id:\n"
                + "	List-Help:List-Unsubscribe:List-Subscribe:List-Post:List-Owner:List-Archive;\n"
                + "	bh=YQ3k8WFj7o6Vhgan8ZGNDxOf3PZapiMfT+S+bU/zzkw=;\n"
                + "	b=EYyjBcGSNnxLfNEe6BTMbCH8thT1FPZ2wuHkrfsyN1MUMcumUTAKouANu/PGEpnb\n"
                + "	KmHp5UkLAudgxJbJmKN+8mf8rZO+zoTCH0futvLm1SwCL0DPc/RtjOGP3faXJDUg\n"
                + "	eIpMX5bQAciUlL3eIymZoG62S7XKGoeb2y/qkBw9FsI=;\n"
                + "X-AuditID: c0a80114-f11ff70000009b3a-a8-5d825248eafd\n"
                + "Received: from ATHMBX03.ath.net ( [10.129.0.215])\n"
                + "	(using TLS with cipher ECDHE-RSA-AES128-SHA256 (128/128 bits))\n"
                + "	(Client did not present a certificate)\n"
                + "	by mail.ath.com.co (Symantec Mail Security) with SMTP id FC.56.39738.842528D5; Wed, 18 Sep 2019 10:50:32 -0500 (-05)\n"
                + "Received: from avveoffprd01 (10.129.61.127) by athmbx03.ath.net (10.129.0.215)\n"
                + " with Microsoft SMTP Server id 14.3.439.0; Wed, 18 Sep 2019 10:50:32 -0500\n"
                + "Date: Wed, 18 Sep 2019 10:50:32 -0500\n"
                + "From: <OficinaInternet@bancoavvillas.com.co>\n"
                + "To: <avvillas@correo.appropiate.app>\n"
                + "Message-ID: <-1410989024.7401568821832531.JavaMail.root@avveoffprd01>\n"
                + "Subject: Tu pago ha sido realizado exitosamente.\n"
                + "MIME-Version: 1.0\n"
                + "Content-Type: text/html; charset=\"UTF-8\"\n"
                + "Content-Transfer-Encoding: quoted-printable\n"
                + "Return-Path: OficinaInternet@bancoavvillas.com.co\n"
                + "X-CFilter-Loop: Reflected\n"
                + "X-Brightmail-Tracker: H4sIAAAAAAAAA+NgFmphluLIzCtJLcpLzFFi42Lh6mER0fUMaoo16JnIbDHhXzO7A6PH1KaX\n"
                + "	rAGMUVw2Kak5mWWpRfp2CVwZ17tOMBXsF6mY1DOfpYHxs0AXIweHhICJxO+ToV2MXBxCApOY\n"
                + "	JBZMmMrWxcgJ5MxilFh2oQLEZhFQlZjy6g07iM0moCOxfcFOFhBbREBF4tXNG2A2r4CrxPcp\n"
                + "	WxhBbGEBA4kbl68zQ8QFJU7OfAJWwyygIbHgzj5GCFtbYtnC18wQuwwlmq5/B4tLCEhKHFxx\n"
                + "	g2UCI+8sJO2zkLTPQtK+gJF5FaNIcXJiXl5qkYGRXmJJhl5yfi4Qb2IEhs2BFYwiOxjvbnM/\n"
                + "	xCjAwajEwxvo2hQrxJpYVlyZe4hRgoNZSYR3jilQiDclsbIqtSg/vqg0J7X4EKM0B4uSOK+b\n"
                + "	SUWskEB6YklqdmpqQWoRTJaJg1OqgdHadNK737Wxum6LKyUW2oo8fO/yrqA3Wltvonnp1UN3\n"
                + "	9/KpVX1Z9uhx9NLM8BmrvI0mbM3ukuKw/lqR9K9e8F9hxvQb964vSfZh/zG/XFJoicKKZTqp\n"
                + "	p98/r9VwNktu7S1v2yC4pqNufVui8eezqYVf163nNPmU7eH1+0j83uWarx7F253ij1ViKc5I\n"
                + "	NNRiLipOBAB2phGPFwIAAA==\n"
                + "\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta http-equiv=3D\"Content-Type\" content=3D\"text/html; charset=3Dutf-8\" />\n"
                + "</head>\n"
                + "<body>\n"
                + "<div style=3D\"border: 1px #183F6C solid;padding: 3px;font-size:12px\">\n"
                + "<p>La transacci=C3=B3n <strong>n=C3=BAmero <span>8397278</span></strong>=20\n"
                + "con la <strong>entidad financiera <span style=3D\"color:#6E6E6E\">BANCO DAVIV=\n"
                + "IENDA</span></strong>,=20\n"
                + "de la fecha 18-sep-2019 10:50:32 AM fue:=20\n"
                + "<strong><span style=3D\"color:green\">APROBADA</span></strong>.</p>\n"
                + "\n"
                + "<table width=3D\"100%\" cellpadding=3D\"5px\" style=3D\"font-size:12px\">\n"
                + "    <tr>\n"
                + "        <th width=3D\"50%\" colspan=3D\"2\" scope=3D\"col\" style=3D\"text-align:l=\n"
                + "eft;background-color:#f5fbff;padding:5px;color:#0059AB\">Informaci=C3=B3n de=\n"
                + "l proveedor del servicio:</th>\n"
                + "        <th width=3D\"50%\" colspan=3D\"2\" scope=3D\"col\" style=3D\"text-align:l=\n"
                + "eft;background-color:#f5fbff;padding:5px;color:#0059AB;font-size:12px\">Info=\n"
                + "rmaci=C3=B3n del cliente que efectu=C3=B3 el pago:</th>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "        <td colspan=3D\"2\">\n"
                + "        =09<span style=3D\"color:#0059AB\">Raz=C3=B3n social:</span><br />Ban=\n"
                + "co Comercial AV Villas\n"
                + "        </td>\n"
                + "        <td colspan=3D\"2\">\n"
                + "        =09<span style=3D\"color:#0059AB\">IP:</span><br />192.168.2.86\n"
                + "        </td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "        <td colspan=3D\"4\">\n"
                + "        =09<span style=3D\"color:#0059AB\">NIT:</span><br />860035827\n"
                + "        </td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "        <th colspan=3D\"4\" style=3D\"text-align:left;background-color:#f5fbff=\n"
                + ";padding:5px;color:#0059AB\">Informaci=C3=B3n del pago:</th>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "    =09<td><strong>N=C2=BA Pago:</strong></td>\n"
                + "    =09<td>8397278</td>\n"
                + "    =09<td><strong>N=C2=BA Transacci=C3=B3n:</strong></td>\n"
                + "    =09<td>496653028</td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "    =09<td><strong>Medio de Pago:</strong></td>\n"
                + "    =09<td>Pago con PSE</td>\n"
                + "    =09<td><strong>Descripci=C3=B3n:</strong></td>\n"
                + "    =09<td>Factura: Autopagos APP Ref: prueba4</td>\n"
                + "    </tr>\n"
                + "</table>\n"
                + "\n"
                + "<p style=3D\"text-align:center;color:#0059AB;font-weight:bold\">Convenio: AUT=\n"
                + "OPAGOS - CLL 32 E No.80 A - 57 INT 201</p>\n"
                + "<table width=3D\"100%\" style=3D\"font-size:12px\">\n"
                + "    <tr>\n"
                + "        <th scope=3D\"col\" style=3D\"text-align:left;background-color:#f5fbff=\n"
                + ";padding:5px;color:#0059AB\">REFERENCIA:</th>\n"
                + "        <th scope=3D\"col\" style=3D\"text-align:right;background-color:#f5fbf=\n"
                + "f;padding:5px;color:#0059AB\">Valor:</th>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "    =09<td style=3D\"vertical-align:top\">5737</td>\n"
                + "    =09<td style=3D\"vertical-align:top;text-align:right\">$100</td>\n"
                + "    </tr>\n"
                + "</table>\n"
                + "\n"
                + "\n"
                + "</div><br /><br />Cordialmente,<br /><br />Centro de Pagos Virtual</font></=\n"
                + "body></html>";

        dataReceive = dataReceive.replaceAll(" ", "");
        if (recipient.equals(propertiesAccess.CORREOAVVILLAS)) {
            System.out.println("dataReceive: " + dataReceive);
            System.out.println("entre por avvillas");
            System.out.println("dataReceive.split(\"<strong>n=C3=BAmero<span>\").length: " + dataReceive.split("<strong>n=C3=BAmero<span>").length);
            System.out.println("is: " + dataReceive != null);
            if (dataReceive != null && dataReceive.split("<strong>n=C3=BAmero<span>").length > 1) {
                System.out.println("numero");
                String numeroTransaccion = dataReceive.split("<strong>n=C3=BAmero<span>")[1].split("</span></strong>")[0];
                System.out.println("numeroTransaccion: " + numeroTransaccion);

                if (!numeroTransaccion.equals("")) {
                    //aqui vamos a organizar la accion al recibir el mail
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("estado", 1);
                    if (dataReceive.toUpperCase().contains("RECHAZADA")) {
                        System.out.println("rechazada");
                        jSONObject.put("estado", 3);
                    } else if (dataReceive.toUpperCase().contains("APROBADA")) {
                        System.out.println("aprobada");
                        jSONObject.put("estado", 2);
                    } else {
                        System.out.println("no encontre nada =======");
                    }
//                    System.out.println("jSONObject: " + jSONObject);
                    //cambiamos el estado de la transaccion
                    EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
                    ComprobanterecaudoPSEJpaController comprobanterecaudoPSEJpaController = new ComprobanterecaudoPSEJpaController(manager);
                    ComprobanterecaudoPSE comprobanterecaudoPSE = comprobanterecaudoPSEJpaController.findByIdAvvillas(numeroTransaccion);
                    if (comprobanterecaudoPSE != null && (comprobanterecaudoPSE.getEstadoIdestado().getIdestado() == 4
                            || comprobanterecaudoPSE.getEstadoIdestado().getIdestado() == 3)) {
                        //puedo seguir trabajando y cambiar lso estados
                        ChangeComprobantePSE.cambioEstadoAutored(comprobanterecaudoPSE, jSONObject);
                        //cambio de estado de facturas de acuerdo al estado
                    }
                }
            }
        } else if (recipient.equals(propertiesAccess.CORREOBANCOLOMBIA)) {
            dataReceive = dataReceive.replaceAll(" ", "").replaceAll("	", "").replaceAll("	", "").replace("=\n", "");
//                System.out.println("dataReceive: " + dataReceive);

            String numeroTransaccion = dataReceive.split("Concepto:</td>=0D=0A<tdclass=3D\"font\">=0D=0A")[1]
                    .split("</td>=0D=0A")[0].replaceAll("[^\\d.]", "");
            if (numeroTransaccion.contains("_")) {
                numeroTransaccion = numeroTransaccion.split("_")[0];
            }
            System.out.println("idReferencia: " + numeroTransaccion);
            JSONObject jSONObject = new JSONObject();
            if (dataReceive.contains("echazada")) {
                System.out.println("rechazada");
                jSONObject.put("estado", 3);
            } else if (dataReceive.contains("probada")) {
                System.out.println("aprobada");
                jSONObject.put("estado", 2);
            }
//                    System.out.println("jSONObject: " + jSONObject);
            //cambiamos el estado de la transaccion

            EntityManagerFactory manager = Persistence.createEntityManagerFactory("AutopagosV2PU");
            ComprobanterecaudoPSEJpaController comprobanterecaudoPSEJpaController = new ComprobanterecaudoPSEJpaController(manager);
            ComprobanterecaudoPSE comprobanterecaudoPSE = comprobanterecaudoPSEJpaController.findComprobanterecaudoPSE(Integer.parseInt(numeroTransaccion));
            if (comprobanterecaudoPSE != null) {
                //puedo seguir trabajando y cambiar lso estados
                ChangeComprobantePSE.cambioEstado(comprobanterecaudoPSE, jSONObject.getInt("estado"),numeroTransaccion);
                //cambio de estado de facturas de acuerdo al estado
            }

        } else if (recipient.equals(propertiesAccess.CORREOBBVA)) {

        }
    }

}
