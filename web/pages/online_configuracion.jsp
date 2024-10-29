<%-- 
    Document   : sipar_facturas
    Created on : 12-ago-2016, 15:10:01
    Author     : Usuario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>

    <div class="container">

        <!-- Page-Title -->
        <div class="row">
            <div class="col-sm-12">
                <div class="page-title-box">
                    <ol class="breadcrumb pull-right">
                        <li><a href="#">Autopagos</a></li>
                        <li class="active">Módulo de configuración - SIPAR</li>
                    </ol>
                    <h4 class="page-title">Configuración</h4>
                </div>
            </div>
        </div>


        <!--Comienza Tabla-->

        <div class="row">
            <div class="col-sm-12">
                <div class="card-box">
                    <div class="table-rep-plugin">
                        <div class="table-responsive" data-pattern="priority-columns">
                            <table id="tech-companies-1" class="table ">
                                <thead>
                                    <tr>
                                        <th>Estado de cuenta</th>
                                        <th>Token</th> 
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td><bean:write name="convenio" property="activoString"/></td>
                                        <td><bean:write name="convenio" property="codigo"/></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>

                    </div>

                </div>
            </div>
        </div>
        <!-- end row -->

        <!--Comienza Tabla-->

        <div class="row">
            <div class="col-sm-12">
                <div class="card-box">
                    <div class="table-rep-plugin">
                        <div class="table-responsive" data-pattern="priority-columns">
                            <table id="tech-companies-1" class="table ">
                                <thead>
                                    <tr>
                                        <th>Generador de botones de pago:</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody> 
                                    <tr>
                                        <td>Referencia de producto:</td>
                                        <td><input type="text" class="form-control" id="referencia" placeholder="Referencia de producto"></td>
                                    </tr>
                                    <tr>
                                        <td>Valor:</td>
                                        <td><input type="text" class="form-control" id="valor" placeholder="Valor"></td>
                                    </tr>
                                    <tr>
                                        <td><button type="button" onclick="generarBoton()" class="btn btn-warning waves-effect w-md waves-light m-b-5" style="-webkit-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);-moz-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);"><i class="fa fa-plus"> </i> Generar botón</button></td>
                                        <td><textarea id="textarea" class="form-control" rows="5">Copie y pegue el código generado</textarea></td>
                                    </tr>

                                    <tr >
                                        <td colspan="2" id="btn-generado" style="text-align: center;;">

                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>

                    </div>

                </div>
            </div>
        </div>
        <!-- end row -->



        <!--Comienza Tabla-->

        <div class="row">
            <div class="col-sm-12">
                <div class="card-box">
                    <div class="table-rep-plugin">
                        <div class="table-responsive" data-pattern="priority-columns">
                            <table id="tech-companies-1" class="table ">
                                <thead>
                                    <tr>
                                        <th>Información de la empresa:</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                <form id="saveFormConvenioDocs" method="post">
                                    <tr>
                                        <td colspan="2" style="text-align:center;"><img style="max-width: 650px;" class="logo" src="img/logoConvenios/<bean:write name="convenio" property="logo"/>"></td>
                                    </tr>
                                    <tr>
                                        <td>Cargar logo:</td>
                                        <td><input type="file" class="form-control"  name="logoFileForm"></td>
                                    </tr>
                                    <tr>
                                        <td><a id="ccio" target="_blank"  href="docs/<bean:write name="convenio" property="camaraComercio"/>">Certificado de Existencia y Representación:</a></td>
                                        <td><input type="file" class="form-control" name="logoFileFormCCio"></td>
                                    </tr>
                                    <tr>
                                        <td><a id="rut" target="_blank" href="docs/<bean:write name="convenio" property="rut"/>">Rut:</a></td>
                                        <td><input type="file" class="form-control" name="logoFileFormRut"></td>
                                    </tr>
                                    <tr>
                                        <td><a id="banco" target="_blank" href="docs/<bean:write name="convenio" property="certificacionBancaria"/>">Certificacion bancaria:</a></td>
                                        <td><input type="file" name="logoFileFormBanco" class="form-control"></td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td style="text-align:right;"><button type="submit" class="btn btn-warning waves-effect w-md waves-light m-b-5" style="-webkit-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);-moz-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);"><i class="fa fa-save"> </i> Guardar</button></td>
                                    </tr>
                                </form>
                                </tbody>
                            </table>
                        </div>

                    </div>

                </div>
            </div>
        </div>
        <!-- end row -->


        <div class="row">
            <div class="col-sm-12">
                <div class="card-box">
                    <div class="table-rep-plugin">
                        <div class="table-responsive" data-pattern="priority-columns">
                            <table id="tech-companies-1" class="table ">
                                <thead>
                                    <tr>
                                        <th>Información bancaria para depósitos e interconexion:</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                <form id="saveFormConvenio" method="post">
                                    <tr>
                                        <td>Direccion respuesta API</td>
                                        <td><input name="direccionRespuesta" value="<bean:write name="convenio" property="direccionRespuesta"/>" type="text" class="form-control"></td>
                                    </tr>
                                    <tr>
                                        <td>Banco:</td>
                                        <td><input name="banco" value="<bean:write name="convenio" property="banco"/>" type="text" class="form-control"></td>
                                    </tr>
                                    <tr>
                                        <td>Numero cuenta</td>
                                        <td><input name="numeroCuenta" value="<bean:write name="convenio" property="numeroCuenta"/>" type="text" class="form-control"></td>
                                    </tr>
                                    <tr>
                                        <td>Tipo de cuenta:</td>
                                        <td>
                                            <select name="idTipoCuenta" id="idTipo" class="form-control">
                                                <option value="0">Seleccione un tipo de cuenta</option>
                                                <logic:iterate name="tipoCuentas" id="tipoCuenta">
                                                    <option value="<bean:write name="tipoCuenta" property="idtipoCuenta"/>"><bean:write name="tipoCuenta" property="nombre"/></option>
                                                </logic:iterate>
                                            </select>
                                        </td>
                                    </tr> 
                                    <tr>
                                        <td>Titular de la cunta</td>
                                        <td><input name="titularCuenta" value="<bean:write name="convenio" property="titularCuenta"/>" type="text" class="form-control"></td>
                                    </tr>   
                                    <tr>
                                        <td></td>
                                        <td style="text-align:right;"><button type="submit" class="btn btn-warning waves-effect w-md waves-light m-b-5" style="-webkit-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);-moz-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);"><i class="fa fa-save"> </i> Guardar</button></td>
                                    </tr>
                                </form>
                                </tbody>
                            </table>
                        </div>

                    </div>

                </div>
            </div>
        </div>
        <!-- end row -->  
    </div>
    <!-- end container --> 
    <script type="text/javascript">
        $(document).ready(function () {
            $("#idTipo").val(<bean:write name="convenio" property="tipoCuenta"/>);
            $("#saveFormConvenio").submit(function (e) {
                e.preventDefault();
                var formData = new FormData(this);
                $('#cargando-modal').modal('show');
                $.ajax({
                    type: "POST",
                    cache: false,
                    data: formData,
                    url: 'SaveConvenioConfiguracion.ap',
                    contentType: false,
                    processData: false,
                    success: function (json) {
                        $("#loadingModal").modal('hide');
                        if (json.error != null) {
                            $("#errorLabel").text(json.error);
                            $('#errorModal').modal('show');
                        } else {
                            $('#cargando-modal').modal('hide');
                            $("#infoLabel").text("Los cambios se realizaron de forma satisfactoria.");
                            $("#infoModal").modal("show");
                        }
                    }
                });
            });

            $("#saveFormConvenioDocs").submit(function (e) {
                e.preventDefault();
                var formData = new FormData(this);
                $('#modal-cargando').addClass('md-show');
                $.ajax({
                    type: "POST",
                    cache: false,
                    data: formData,
                    url: 'SaveConvenioDocs.ap',
                    contentType: false,
                    processData: false,
                    success: function (json) {
                        $("#loadingModal").modal('hide');
                        if (json.error != null) {
                            $("#errorLabel").text(json.error);
                            $('#errorModal').modal('show');
                        } else {
                            $('#cargando-modal').modal('hide');
                            $("#infoLabel").text("Los cambios se realizaron de forma satisfactoria.");
                            $("#infoModal").modal("show");
                            $(".logo").attr("src", json.logo);
                            $("#ccio").attr("href", json.ccio);
                            $("#banco").attr("href", json.banco);
                            $("#rut").attr("href", json.rut);
                        }
                    }
                });
            });
        });
        var Base64={_keyStr:"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",encode:function(e){var t="";var n,r,i,s,o,u,a;var f=0;e=Base64._utf8_encode(e);while(f<e.length){n=e.charCodeAt(f++);r=e.charCodeAt(f++);i=e.charCodeAt(f++);s=n>>2;o=(n&3)<<4|r>>4;u=(r&15)<<2|i>>6;a=i&63;if(isNaN(r)){u=a=64}else if(isNaN(i)){a=64}t=t+this._keyStr.charAt(s)+this._keyStr.charAt(o)+this._keyStr.charAt(u)+this._keyStr.charAt(a)}return t},decode:function(e){var t="";var n,r,i;var s,o,u,a;var f=0;e=e.replace(/[^A-Za-z0-9\+\/\=]/g,"");while(f<e.length){s=this._keyStr.indexOf(e.charAt(f++));o=this._keyStr.indexOf(e.charAt(f++));u=this._keyStr.indexOf(e.charAt(f++));a=this._keyStr.indexOf(e.charAt(f++));n=s<<2|o>>4;r=(o&15)<<4|u>>2;i=(u&3)<<6|a;t=t+String.fromCharCode(n);if(u!=64){t=t+String.fromCharCode(r)}if(a!=64){t=t+String.fromCharCode(i)}}t=Base64._utf8_decode(t);return t},_utf8_encode:function(e){e=e.replace(/\r\n/g,"\n");var t="";for(var n=0;n<e.length;n++){var r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r)}else if(r>127&&r<2048){t+=String.fromCharCode(r>>6|192);t+=String.fromCharCode(r&63|128)}else{t+=String.fromCharCode(r>>12|224);t+=String.fromCharCode(r>>6&63|128);t+=String.fromCharCode(r&63|128)}}return t},_utf8_decode:function(e){var t="";var n=0;var r=c1=c2=0;while(n<e.length){r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r);n++}else if(r>191&&r<224){c2=e.charCodeAt(n+1);t+=String.fromCharCode((r&31)<<6|c2&63);n+=2}else{c2=e.charCodeAt(n+1);c3=e.charCodeAt(n+2);t+=String.fromCharCode((r&15)<<12|(c2&63)<<6|c3&63);n+=3}}return t}}


        function generarBoton() {
            var referencia = $("#referencia").val();
            var valor = $("#valor").val();
            var parameters = "token=<bean:write name="convenio" property="codigo"/>" 
                    + "&referencia=" + referencia
                    + "&valor=" + valor;
            //codificamos base 64
            
            var encodedString = Base64.encode(parameters);
            
            var server = "<bean:write name="server"/>LoadPagoUsuario.ap?"
                    + "enc="+encodedString;

            var sourceBtn = "<a style=\"background-color:#ff6c01;color:white;border-bottom:5px solid d65b07;padding:10px; border-radius:5px;text-decoration:none;font-weight:600;\" href=\"" + server + "\" target=\"_blank\">Pagar ahora</a>"
            var tr = $("#btn-generado");
            tr.empty().append(sourceBtn);

            var textarea = $("#textarea");
            textarea.text(sourceBtn);




        }
    </script>
</html>
