<%-- 
    Document   : root
    Created on : 09-ago-2016, 14:15:33
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
                        <li class="active">Tablero Principal</li>
                    </ol>
                    <h4 class="page-title">Bienvenido !</h4>
                </div>
            </div>
        </div>


        <div class="row">
            <div class="col-lg-12">
                <p class="text-muted" style="text-align:center;font-size:20px;"><strong>INDICADORES GLOBALES:</strong></p>
                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-success"><bean:write name="usuarios"/></h3>
                        <p class="text-muted">Usuarios</p>
                    </div>
                </div>

                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-primary"><bean:write name="sipar"/></h3>
                        <p class="text-muted">Convenios SIPAR</p>
                    </div>
                </div>

                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-warning"><bean:write name="corresponsalia"/></h3>
                        <p class="text-muted">Convenios Autored</p>
                    </div>
                </div>

                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-success"><bean:write name="online"/></h3>
                        <p class="text-muted">Convenios online</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-lg-12">
                <div class="col-sm-6 col-lg-6">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-success"><bean:write name="totalFacturado"/></h3>
                        <p class="text-muted">Total Facturado</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-6">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-default"><bean:write name="totalIva"/></h3>
                        <p class="text-muted">IVA Facturado</p>
                    </div>
                </div>
            </div>
        </div>


        <div class="row">
            <div class="col-lg-12">
                <p class="text-muted" style="text-align:center; font-size:20px;"><strong>INDICADORES AUTORED:</strong></p>
                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-success"><bean:write name="config" property="autoredNumeroTransacciones"/></h3>
                        <p class="text-muted">Operaciones Trx Autored</p>
                    </div>
                </div>

                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-primary"><bean:write name="config" property="autoredNumeroFacturasPagadas"/></h3>
                        <p class="text-muted">Facturas pagadas</p>
                    </div>
                </div>

                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-warning"><bean:write name="config" property="autoredComisionActualAutopagos"/></h3>
                        <p class="text-muted">Comisión Actual Autopagos</p>
                    </div>
                </div>

                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-success"><bean:write name="config" property="autoredComisionActualUsuariosAutored"/></h3>
                        <p class="text-muted">Comisión Actual Autored</p>
                    </div>
                </div>
            </div>    
            <div class="col-lg-12">
                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-default"><bean:write name="config" property="autoredComisionPagadaUsuariosAutored"/></h3>
                        <p class="text-muted">Comisión pagada</p>
                    </div>
                </div>

                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-default"><bean:write name="config" property="autoredComisionPorRedimirUsuariosAutored"/></h3>
                        <p class="text-muted">Comisión por redimir</p>
                    </div>
                </div>

                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-default"><bean:write name="config" property="autoredNivelesHorizontales"/></h3>
                        <p class="text-muted">Niveles horizontales</p>
                    </div>
                </div>

                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-default"><bean:write name="config" property="autoredNivelesVerticales"/></h3>
                        <p class="text-muted">Niveles Verticales</p>
                    </div>
                </div>
            </div>
            <div class="col-lg-12"> 
                <div class="col-sm-12 col-lg-12">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-danger"><bean:write name="config" property="autoredSaldoDisponible"/></h3>
                        <p class="text-muted">Saldo disponible</p>
                    </div>
                </div>
            </div>
        </div>




        <div class="row">
            <div class="col-lg-12">
                <p class="text-muted" style="text-align:center; font-size:20px;"><strong>INDICADORES SIPAR:</strong></p>
                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-success"><bean:write name="config" property="siparNumeroTransacciones"/></h3>
                        <p class="text-muted">Operaciones Trx SIPAR</p>
                    </div>
                </div>

                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-primary"><bean:write name="config" property="siparNumeroFacturasCargadas"/></h3>
                        <p class="text-muted">Facturas cargadas</p>
                    </div>
                </div>

                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-warning"><bean:write name="config" property="siparNumeroFacturasPagadas"/></h3>
                        <p class="text-muted">Facturas pagadas</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-success"><bean:write name="config" property="siparValorTotalCargado"/></h3>
                        <p class="text-muted">Monto total cargado</p>
                    </div>
                </div>

            </div>
        </div>

        <div class="row">
            <div class="col-lg-12">
                <div class="col-sm-6 col-lg-4">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-primary"><bean:write name="config" property="siparValorTotalRecaudado"/></h3>
                        <p class="text-muted">Monto total recaudado</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-4">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-default"><bean:write name="config" property="siparValorTotalFacturadoAutopagos"/></h3>
                        <p class="text-muted">Total facturado autopagos</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-4">
                    <div class="widget-simple text-center card-box">
                        <h3 class="text-success"><bean:write name="config" property="siparValorTotalRecaudadoAutopagos"/></h3>
                        <p class="text-muted">Total recaudado autopagos<p>
                    </div>
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
                <div class="portlet"><!-- /primary heading -->
                    <div class="portlet-heading">
                        <h3 class="portlet-title text-dark">
                            Trx Autored - Trx Sipar -Trx Online
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
                    <h4 class="text-dark  header-title m-t-0">Últimos Movimientos</h4>
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
        gridTransacciones.setImagePath(".dhtmlx/codebase/imgs/");
        gridTransacciones.attachHeader("#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter");
        gridTransacciones.setHeader("ID Trx,Fecha - Hora, Factura ,Usuario,Estado, Valor,Tipo,Comprobante PSE");//the headers of columns  
        gridTransacciones.setInitWidths("300,200,350,250,150,100,150,250");          //the widths of columns  
        gridTransacciones.setColAlign("right,center,left,center,center,center,center,center,center");       //the alignment of columns   
        gridTransacciones.setColTypes("ro,ed,ed,ed,ed,price,ed,ed");                //the types of columns  
        gridTransacciones.setColSorting("int,str,str,int,str,str,str,str,str");          //the sorting types   
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

        function buscar() {
            $('#cargando-modal').modal('show');
            $.ajax({
                type: "POST",
                cache: false,
                url: 'SearchIndicatorsData.ap',
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
                        var plabels = ["TRX AUTORED", "TRX SIPAR", "TRX ONLINE"];
                        var pcolors = ['#00b19d', '#3bafda', '#3bdfda'];
                        var borderColor = '#f5f5f5';
                        var bgColor = '#fff'; 
                        $.FlotChart.createPlotGraphMulti("#website-stats", json.grafica.autored,json.grafica.sipar, json.grafica.online,  plabels, pcolors, borderColor, bgColor);
                    }
                }
            });
        }
        buscar();
    </script>
</html>
