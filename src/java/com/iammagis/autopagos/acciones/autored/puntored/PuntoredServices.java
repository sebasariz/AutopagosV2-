/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.autored.puntored;

import com.iammagis.autopagos.jpa.beans.support.PropertiesAccess;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author sebasariz
 */
public class PuntoredServices {

    static PropertiesAccess propertiesAccess = new PropertiesAccess();

    static String servicio_token = "serviciosIntegracionHost/api/host/token";
    static String servicio_consultas = "serviciosIntegracionHost/api/host/restful/consultas";

    public static JSONObject getToken() throws JSONException {
        JSONObject jSONObjectRequestToken = new JSONObject();
        jSONObjectRequestToken.put("ip", "192.168.1.110");
        jSONObjectRequestToken.put("usuarioHost", propertiesAccess.PUNTORED_USUARIO_HOST);
        jSONObjectRequestToken.put("claveHost", propertiesAccess.PUNTORED_CLAVE);
        jSONObjectRequestToken.put("comercio", propertiesAccess.PUNTORED_ID_ALIADO);
        jSONObjectRequestToken.put("cliente", propertiesAccess.PUNTORED_TERMINAL);

        String url = propertiesAccess.PUNTORED_ENDPOINT + servicio_token;
        String response = send(jSONObjectRequestToken, url);

        System.out.println("response: " + response);
        return null;
    }

    public static JSONObject getSaldo() {
        String proceso = "04001";
        return null;
    }

    public static JSONObject getEstadoTransaccion() {
        String proceso = "04002";
        return null;
    }

    public static JSONObject getConvenioActivos() {
        String proceso = "0X001";
        return null;
    }

    public static JSONObject ConsultaReferenciaConvenio() {
        String proceso = "0X001";
        return null;
    }

    public static String send(JSONObject jSONObject, String url) throws JSONException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("content-type", "application/json; charset=utf-8");
        try {
            // Add your data
            System.out.println("JSON: " + jSONObject);
            StringEntity entity = new StringEntity(jSONObject.toString());
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity httpEntity = response.getEntity();
            String responseString = EntityUtils.toString(httpEntity, "UTF-8");
            System.out.println("response: " + response.getStatusLine().getStatusCode());
            System.out.println("responseString: " + responseString);
            responseString = responseString.replace("\"", "");
            return responseString;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            jSONObject = new JSONObject();
            jSONObject.put("error", e.getMessage());
            return jSONObject.toString();
        }

    }

    public static void main(String[] args) {
        try {
            getToken();
        } catch (JSONException ex) {
            Logger.getLogger(PuntoredServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
