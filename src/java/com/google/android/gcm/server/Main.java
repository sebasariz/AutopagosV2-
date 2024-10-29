/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.android.gcm.server;

import java.io.IOException; 
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author sebasariz
 */
public class Main {
    
    
    
    public static void main(String[] args){  
        ResourceBundle pe = ResourceBundle.getBundle("com/iammagis/resources/server");
        String keyGoogle = pe.getString("keyGoogleUsuario").trim() ;
        System.out.println("keyGoogle: "+keyGoogle);
        Sender sender = new Sender(keyGoogle);
        
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("mensaje", "holitaaa");
        } catch (JSONException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Message.Builder builder = new Message.Builder();
        builder.addData("message", "Me golie tu linea"); 
        builder.addData("json", jSONObject.toString()); 
        
        Message message = builder.build();
        try {
            System.out.println("enviando");
            sender.sendNoRetry(message, "APA91bGczwzsNsR9uOOEeRUrFCtMkd3ws_V-nphC-Swe4LV1qc2Iba7Icum325mYSU2-mySMSb0p6UO0EjykFqIB0JwMRL4RyrWqj8K-ScVwrd71spfCYma96N0xdooxs3MPVAVrEJtVHLjim4VtDVNunRtO1TLxqA");
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        
    }
}
