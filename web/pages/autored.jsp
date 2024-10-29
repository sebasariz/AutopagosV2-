<%-- 
    Document   : autored
    Created on : 10-ago-2016, 14:08:53
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
                        <li class="active">Resumen Autored</li>
                    </ol>
                    <h4 class="page-title">Bienvenido !</h4>
                </div>
            </div>
        </div>



        <div class="row">
            <div class="col-sm-12 col-lg-6">
                <div class="widget-simple text-center card-box">
                    <h3 class="text-success"><bean:write name="config" property="autoredNumeroFacturasPagadas"/></h3>
                    <p class="text-muted">Total Facturas Pagadas</p>
                </div>
            </div>

            <div class="col-sm-12 col-lg-6">
                <div class="widget-simple text-center card-box">
                    <h3 class="text-primary"><bean:write name="config" property="autoredNumeroTransacciones"/></h3>
                    <p class="text-muted">Total de Operaciones</p>
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
                        <a href="javascript:void(0)" class="input-group-addon bg-success b-0 text-white" onclick="createXLS()">XLS</a>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-lg-12">
                <div class="card-box">
                    <h4 class="text-dark  header-title m-t-0">Trx Autored</h4>
                    <div id="gridbox" style="height:400px;"> 
                    </div> 
                </div> 
            </div> 
        </div> 
    </div>
    <script type="text/javascript">
        var gridautored = new dhtmlXGridObject('gridbox');
        gridautored.setImagePath(".dhtmlx/codebase/imgs/");
        gridautored.attachHeader("#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter");
        gridautored.setHeader("ID Trx,Fecha - Hora, Usuario, Referencia, Convenio, Estado, Valor,Tipo transaccion,Transaccion PSE");//the headers of columns  
        gridautored.setInitWidths("100,200,280,100,100,100,100,150,100");
        gridautored.setColAlign("center,center,center,center,center,center,center,center,center");       //the alignment of columns   
        gridautored.setColTypes("ro,ed,ed,ed,ed,ed,ed,ed,ed");                //the types of columns  
        gridautored.setColSorting("int,str,str,int,str,str,str,str,str");          //the sorting types   
        gridautored.init();      //finishes initialization and renders the grid on the page

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
        function createXLS() {
            //mygridResult.toExcel('XlsGenerator.jsn', 'gray'); 
            gridautored.toExcel("https://dhtmlxgrid.appspot.com/export/excel", 'gray');
        }
        function buscar() {
            $('#cargando-modal').modal('show');
            $.ajax({
                type: "POST",
                cache: false,
                url: 'SearchAutoredTableTRX.ap',
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
                        gridautored.clearAll();
                        gridautored.parse(json, "json");
                    }
                }
            });
        }
        buscar();
    </script>
</html>
