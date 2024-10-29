<%-- 
    Document   : online
    Created on : 09-ago-2016, 14:16:23
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
                        <li class="active">Resumen Plataforma - Autoagos Online</li>
                    </ol>
                    <h4 class="page-title">Bienvenido !</h4>
                </div>
            </div>
        </div>


        <div class="row">
            <div class="col-sm-6 col-lg-4">
                <div class="widget-simple text-center card-box">
                    <h3 class="text-primary counter"><bean:write name="convenio" property="transacciones"/></h3>
                    <p class="text-muted">Transacciones</p>
                </div>
            </div>
            <div class="col-sm-6 col-lg-4">
                <div class="widget-simple text-center card-box">
                    <h3 class="text-primary counter" id="recaudo"><bean:write name="convenio" property="valorRecaudado"/></h3>
                    <p class="text-muted" >Recaudado</p>
                </div>
            </div>

            <div class="col-sm-6 col-lg-4">
                <div class="widget-simple text-center card-box">
                    <h3 class="text-success counter" id="disponible"><bean:write name="convenio" property="saldo"/></h3>
                    <p class="text-muted" >Saldo Disponible</p>
                </div>
            </div>
        </div> 



        <div class="row">
            <div class="col-lg-6 card-box">
                <p class="text-default" style="text-align:center;">Solicitar desembolso:</p>
                <div class="input-group">
                    <input type="text" id="valor" class="form-control" placeholder="Ingrese el saldo a solicitar">
                    <span class="input-group-btn">
                        <button type="button" class="btn waves-effect waves-light" style="background:#AEEA00; color:#ffffff;" onclick="pago();">Solicitar</button>
                    </span>
                </div>

            </div>
            <div class="col-sm-6 col-lg-6">
                <div class="widget-simple-chart text-right card-box">
                    <p class="text-muted" style="text-align:center;">Seleccione un rango de fechas:</p>
                    <div class="input-daterange input-group" id="date-range">
                        <span class="input-group-addon bg-primary b-0 text-white">Desde</span>
                        <input type="text" class="form-control" id="start" />
                        <span class="input-group-addon bg-primary b-0 text-white">Hasta</span>
                        <input type="text" class="form-control" id="end" />
                        <a class="input-group-addon bg-primary b-0 text-white" onclick="buscar()">Buscar</a>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-lg-12">
                <div class="portlet"><!-- /primary heading -->
                    <div class="portlet-heading">
                        <h3 class="portlet-title text-dark">
                            Recaudos generados
                        </h3>
                        <div class="clearfix"></div>
                    </div>
                    <div id="portlet1" class="panel-collapse collapse in">
                        <div class="portlet-body">
                            <div id="website-stats" style="height: 320px;" class="flot-chart"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>



        <div class="row">
            <div class="col-lg-12">
                <div class="card-box">
                    <h4 class="text-dark  header-title m-t-0">Movimientos Autopagos Online</h4>
                    <div style="margin-bottom:10px; text-align:right;">
                        <button onclick="createXLS()" type="button" class="btn btn-success waves-effect w-md waves-light m-b-5" style="-webkit-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);
                                -moz-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);
                                box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);"><i class="fa fa-file-excel-o"> </i> Excel</button>
                        <button onclick="createPDF()" type="button" class="btn btn-danger waves-effect w-md waves-light m-b-5" style="-webkit-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);
                                -moz-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);
                                box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);"><i class="fa fa-file-pdf-o"> </i> PDF</button>

                    </div>
                    <div id="gridbox" style="height:300px;"> 
                    </div> 
                </div> 
            </div>
            <!-- end col -8 --> 
        </div>
        <!-- end row -->  
    </div>
    <script type="text/javascript">
        var gridTransacciones = new dhtmlXGridObject('gridbox');
        gridTransacciones.attachHeader("#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter");
        gridTransacciones.setHeader("Fecha - Hora, Factura ,Email,Usuario,Estado, Valor,Tipo,ID Trx");//the headers of columns  
        gridTransacciones.setInitWidths("200,150,150,150,250,150,180,150");          //the widths of columns  
        gridTransacciones.setColAlign("center,center,center,center,center,center,center,center,center");       //the alignment of columns   
        gridTransacciones.setColTypes("ro,ed,ed,ed,ed,price,ed,ed");                //the types of columns  
        gridTransacciones.setColSorting("str,str,str,str,int,str,str,str,str");          //the sorting types   
        gridTransacciones.init();      //finishes initialization and renders the grid on the page

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
        function pago() {
            if (!$("#valor").val()) {
                $("#errorLabel").text("Ingrese el valor del retiro");
                $("#errorModal").modal("show");
                return;
            }
            $('#cargando-modal').modal('show');
            $.ajax({
                type: "POST",
                cache: false,
                data: {
                    valor: $("#valor").val(),
                    fechaInicial: fechaInicial,
                    fechaFinal: fechaFinal
                },
                url: 'SolicitudPago.ap',
                success: function (json) {
                    $('#cargando-modal').modal('hide');
                    if (json.error) {
                        $("#errorLabel").text(json.error);
                        $("#errorModal").modal("show");
                    } else {
                        //redireccionar a pagos pse
                        $("#recaudo").text(json.recaudo);
                        $("#disponible").text(json.disponible);

                        gridTransacciones.clearAll();
                        gridTransacciones.parse(json.grid, "json");

                        //inicializamos la grafica
                        var plabels = ["TRX ONLINE"];
                        var pcolors = ['#3bdfda'];
                        var borderColor = '#f5f5f5';
                        var bgColor = '#fff';
                        $.FlotChart.createPlotGraph("#website-stats", json.grafica.online, plabels, pcolors, borderColor, bgColor);

                        $("#infoLabel").text(json.msg);
                        $('#infoModal').modal("show");
                    }

                }
            });
        }
        function buscar() {
            $('#cargando-modal').modal('show');
            $.ajax({
                type: "POST",
                cache: false,
                url: 'SearchIndicatorsDataOnline.ap',
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
                        gridTransacciones.clearAll();
                        gridTransacciones.parse(json.grid, "json");

                        //inicializamos la grafica
                        var plabels = ["TRX ONLINE"];
                        var pcolors = ['#3bdfda'];
                        var borderColor = '#f5f5f5';
                        var bgColor = '#fff';
                        $.FlotChart.createPlotGraph("#website-stats", json.grafica.online, plabels, pcolors, borderColor, bgColor);
                    }
                }
            });
        }
        buscar();

        function createPDF() {
            gridTransacciones.toPDF('PdfGenerator.ap', 'gray');
        }
        function createXLS() {
            gridTransacciones.toPDF('XlsGenerator.ap', 'gray');
        }
    </script>
</html>
