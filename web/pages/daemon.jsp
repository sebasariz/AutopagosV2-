<%-- 
    Document   : sipar
    Created on : 10-ago-2016, 14:06:55
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
                        <li class="active">DAEMON</li>
                    </ol>
                    <h4 class="page-title">Bienvenido !</h4>
                </div>
            </div>
        </div>


        <div class="row">
            <div class="col-sm-6 col-lg-6">
                <div class="widget-simple text-center card-box">
                    <select class="form-control" id="estado">
                        <option value="0">Inactivo</option>
                        <option value="1">Activo</option>
                    </select>
                    <p class="text-muted">Estado daemon Online</p>
                </div>
            </div>
            <div class="col-sm-6 col-lg-6">
                <div class="widget-simple text-center card-box"> 
                    <input type="text" class="form-control" id="fecha" />
                    <p class="text-muted">Hora facturacion</p>
                </div>
            </div> 
        </div>
        <div class="row">
            <div class="col-sm-6 col-lg-6">
                <div class="widget-simple text-center card-box">
                    <select class="form-control" id="estadoPrivados">
                        <option value="0">Inactivo</option>
                        <option value="1">Activo</option>
                    </select>
                    <p class="text-muted">Estado Daemon Sipar</p>
                </div>
            </div> 
            <div class="col-sm-6 col-lg-6">
                <div class="widget-simple text-center card-box">
                    <select class="form-control" id="estadoFacturacion" >
                        <option value="0">Inactivo</option>
                        <option value="1">Activo</option>
                    </select>
                    <p class="text-muted">Daemon facturacion</p>
                </div>
            </div>  
        </div>
        <div class="row">
            <div class="col-sm-6 col-lg-6">
                <div class="widget-simple text-center card-box">
                    <input type="text" id="user" class="form-control" value="<bean:write name="config" property="autopagosDaemonUsuario"/>">
                    <p class="text-muted">User online</p>
                </div>
            </div> 
            <div class="col-sm-6 col-lg-6">
                <div class="widget-simple text-center card-box">
                    <input type="text" id="pass" class="form-control" value="<bean:write name="config" property="autopagosDaemonPass"/>">
                    <p class="text-muted">Pass online</p>
                </div>
            </div>  
        </div>
        <div class="row">
            <div class="col-sm-12 col-lg-12">
                <div class="widget-simple text-center card-box">
                    Correo electronico soporte daemon
                    <input type="text" class="form-control" id="correo" value="<bean:write name="config" property="autopagosEmailSoporte"/>">
                </div>
                <div class="widget-simple text-center card-box"> 
                    <button type="button" class="btn form-control btn-success" onclick="guardar()">Guardar</button>
                </div>
            </div>             
        </div>
    </div>


    <div id="infoModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;text-align:left;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header"> 
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title">Informacion</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-12">
                            <p><label id="infoLabel"></label></p>
                        </div>

                    </div>
                </div> 
            </div> 
        </div><!-- /.modal --> 
    </div>
    <script type="text/javascript">

        if (<bean:write name="config" property="autopaogsDaemon"/>) {
            $("#estado").val(1);
        }
        if (<bean:write name="config" property="autopagosDaemonPrivadosActivo"/>) {
            $("#estadoPrivados").val(1);
        }
        if (<bean:write name="config" property="autopagosDaemonFacturacionActivo"/>) {
            $("#estadoFacturacion").val(1);
        }
        $("#fecha").val('<bean:write name="config" property="autopagosFechaString"/>');
        function guardar() {
            var estado = $("#estado").val();
            var fecha = $("#fecha").val();
            var estadoPrivados = $("#estadoPrivados").val();
            var estadoFacturacion = $("#estadoFacturacion").val();
            var correo = $("#correo").val();
            var user = $("#user").val();
            var pass = $("#pass").val(); 
            $.ajax({
                type: "POST",
                cache: false,
                url: 'GuardarConfigDaemin.ap',
                data: {
                    estado: estado,
                    fecha: fecha,
                    estadoPrivados: estadoPrivados,
                    estadoFacturacion: estadoFacturacion,
                    correo: correo,
                    user:user,
                    pass:pass
                },
                success: function (json) {
                    $('#cargando-modal').modal('hide');
                    if (json.error != null) {
                        $("#errorLabel").text(json.error);
                        $("#errorModal").modal("show");
                    } else {
                        $("#infoLabel").text("Los cambios se guardaron de forma exitosa.");
                        $("#infoModal").modal("show");
                    }
                }
            });

        }


    </script>
</html>
