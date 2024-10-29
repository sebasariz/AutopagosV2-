<%-- 
    Document   : online
    Created on : 10-ago-2016, 14:02:32
    Author     : Usuario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
    <div class="content">
        <div class="container">

            <!-- Page-Title -->
            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">Autopagos</a></li>
                            <li class="active">Autopagos Online</li>
                        </ol>
                        <h4 class="page-title">Bienvenido !</h4>
                    </div>
                </div>
            </div>
 

            <div class="row">
                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-primary counter"><bean:write name="conveniosOnline"/></h3>
                        <p class="text-muted">Convenios</p>
                    </div>
                </div>

                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-warning counter"><bean:write name="config" property="onlineCobrosRealizados"/></h3>
                        <p class="text-muted">Cobros realizados</p>
                    </div>
                </div>

                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-info counter"><bean:write name="config" property="onlineCobrosRecibidos"/></h3>
                        <p class="text-muted">Cobros recibidos</p>
                    </div>
                </div>

                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-success counter"><bean:write name="config" property="onlineValorTotalRecibido"/></h3>
                        <p class="text-muted">Valor total recibido</p>
                    </div>
                </div>
            </div>   

            <div class="row">
                <div class="col-sm-12 col-lg-12">
                    <div class="widget-simple-chart text-right card-box">
                        <p class="text-muted" style="text-align:center;">Seleccione un rango de fechas:</p>
                        <div class="input-daterange input-group">
                            <span class="input-group-addon bg-primary b-0 text-white">Desde</span>
                            <input type="text" class="form-control" id="start" />
                            <span class="input-group-addon bg-primary b-0 text-white">Hasta</span>
                            <input type="text" class="form-control" id="end" />
                            <a href="javascript:void(0)" class="input-group-addon bg-primary b-0 text-white" onclick="buscar()">Buscar</a>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-12">
                    <div class="card-box">
                        <h4 class="text-dark  header-title m-t-0">Últimos Movimientos Autopagos Online</h4>
                        <div id="gridbox" style="height:400px;"> 
                        </div> 
                    </div> 
                </div>
                <!-- end col -8 --> 
            </div>
            <!-- end row --> 
        </div>
        <!-- end container -->
    </div>
    <script type="text/javascript">
        var movimientosonline = new dhtmlXGridObject('gridbox'); 
        movimientosonline.setImagePath(".dhtmlx/codebase/imgs/");
        movimientosonline.attachHeader("#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter");
        movimientosonline.setHeader("ID Trx,Fecha - Hora, Usuario, Referencia, Convenio, Estado, Valor,Tipo transaccion,Transaccion PSE");//the headers of columns  
        movimientosonline.setInitWidths("100,200,280,100,100,100,100,150,100");  
        movimientosonline.setColAlign("center,center,center,center,center,center,center,center,center");       //the alignment of columns   
        movimientosonline.setColTypes("ro,ed,ed,ed,ed,ed,ed,ed,ed");                //the types of columns  
        movimientosonline.setColSorting("int,str,str,int,str,str,str,str,str");          //the sorting types   
        movimientosonline.init();      //finishes initialization and renders the grid on the page

        var longDate = new Date().getTime();
        var fechaInicial = longDate - 2629746000;
        var fechaFinal = longDate;
        $(document).ready(function () {
            $("#start").datepicker({
                autoclose: true
            }).on('changeDate', function (e) {
                fechaInicial = e.date.getTime();
            });
            $('#start').datepicker('update', new Date(fechaInicial));
            $("#end").datepicker({
                autoclose: true
            }).on('changeDate', function (e) {
                fechaFinal = e.date.getTime();
            });
            $('#end').datepicker('update', new Date(fechaFinal));
        });
        
        function buscar(){
            $('#cargando-modal').modal('show');
            $.ajax({
                type: "POST",
                cache: false,
                url: 'SearchOnlineTableTRX.ap',
                data: {
                    fechaInicial: fechaInicial,
                    fechaFinal: fechaFinal
                },
                success: function (json) {
                    $('#cargando-modal').modal('hide');
                    if (json.error != null) {
                        $("#errorLabel").text(json.error);
                        $("#errorModal").modal("show");
                    } else {
                        movimientosonline.clearAll();
                        movimientosonline.parse(json, "json");
                    }
                }
            });
        }
        buscar();
    </script>
</html>
