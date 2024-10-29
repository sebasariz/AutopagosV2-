Document   : facturas
Created on : 10-ago-2016, 14:00:11
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
                        <li class="active">Gestión de usuarios</li>
                    </ol>
                    <h4 class="page-title">Bienvenido !</h4>
                </div>
            </div>
        </div> 


        <div class="row">
            <div class="col-sm-12 col-lg-6">
                <div class="widget-simple text-center card-box">
                    <h3 class="text-success"><bean:write name="facturasemitidas"/></h3>
                    <p class="text-muted">Total de Facturas Emitidas</p>
                </div>
            </div>

            <div class="col-sm-12 col-lg-6">
                <div class="widget-simple text-center card-box">
                    <h3 class="text-muted"><bean:write name="facturaspagadas"/></h3>
                    <p class="text-muted">Total Facturas Pagadas</p>
                </div>
            </div>
            <!-- end row -->



            <div class="row">
                <div class="col-lg-12">
                    <div class="card-box">
                        <h4 class="text-dark  header-title m-t-0">Facturas</h4>
                        <div style="margin-bottom:10px; text-align:right;">
                            <button type="button" data-toggle="modal" data-target="#crear-factura-libre-modal" class="btn btn-primary waves-effect w-md waves-light m-b-5" style="-webkit-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);
                                    -moz-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);
                                    box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);"><i class="fa fa-plus"> </i> Crear factura libre</button>
                            <button type="button" data-toggle="modal" data-target="#crear-factura-plan-modal" class="btn btn-primary waves-effect w-md waves-light m-b-5" style="-webkit-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);
                                    -moz-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);
                                    box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);"><i class="fa fa-edit"> </i> Crear factura plan</button>
                            <button type="button" data-toggle="modal"  data-target="#anular-factura-modal" class="btn btn-warning waves-effect w-md waves-light m-b-5" style="-webkit-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);
                                    -moz-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);
                                    box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);"><i class="fa fa-exclamation"> </i> Anular</button>
                            <button type="button" data-toggle="modal"  data-target="#pagar-factura-modal" class="btn waves-effect w-md waves-light m-b-5" style="-webkit-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);
                                    -moz-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);
                                    box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);background:#2ecc71;color:#ffffff;"><i class="fa fa-check"> </i> Marcar pagada</button>
                            <button type="button" data-toggle="modal"  data-target="#masivo-factura-modal" class="btn btn-danger waves-effect w-md waves-light m-b-5" style="-webkit-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);
                                    -moz-box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);
                                    box-shadow: 0px 2px 10px 0px rgba(97,97,97,0.5);"><i class="fa fa-bell-o"> </i> Recordatorio masivo</button>

                        </div>
                        <div id="gridbox" style="height:350px;"></div> 

                    </div>
                </div>     

            </div>
            <!-- end container -->
        </div>  
    </div>

    <div id="crear-factura-libre-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;text-align:left;">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="createFactura">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title">Crear factura libre</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" id="container">
                            <div class="col-md-12">
                                <div class="widget-simple text-center"> 
                                    Convenio:
                                    <select class="form-control" id="idconvenio" name="idconvenio">
                                        <option value="0">Seleccione un convenio</option>
                                        <logic:iterate name="convenios" id="convenio">
                                            <option value="<bean:write name="convenio" property="idconvenios"/>"><bean:write name="convenio" property="nombre"/></option>
                                        </logic:iterate>
                                    </select>
                                    <input type="hidden" id="items" name="items">
                                </div>
                            </div>
                            <div class="col-md-12">
                                <div class="widget-simple text-center" style="padding-top: 10px;"> 
                                    <button type="button" class="btn form-control btn-success" onclick="agregar()"><i class="fa fa-plus"></i> Agregar</button>
                                </div>
                            </div>
                            <div class="col-md-12" style="margin-top: 20px;"> 
                                <ul class="list-inline" style="width: 100%;">
                                    <li style="width: 60%;">Descripción</li>
                                    <li style="width: 15%;">Valor</li>
                                    <li style="width: 20%;"></li>
                                </ul>
                            </div>
                            <div class="div col-md-12" style="margin-top: 20px;" id="1"> 
                                <ul class="list-inline" style="width: 100%;">
                                    <li style="width: 60%;"><input class="desc form-control" id="desc1" placeholder="Descripción"></li>
                                    <li style="width: 15%;"><input class="val form-control" id="val1" onchange="sumatoria()" placeholder="Valor"></li>
                                    <li style="width: 20%;"> <button class="btn btn-danger form-control" onclick="destroy(1)">Eliminar</button></li>
                                </ul>
                            </div>

                        </div>
                    </div>
                    <div class="modal-footer">
                        <ul class="list-inline" style="width: 100%;">
                            <li style="width: 60%;"></li>
                            <li style="width: 15%;">Subtotal:</li>
                            <li style="width: 20%;" id="subtotal">000</li>
                        </ul>
                        <ul class="list-inline" style="width: 100%;">
                            <li style="width: 60%;"></li>
                            <li style="width: 15%;">IVA:</li>
                            <li style="width: 20%;" id="iva">0.00</li>
                        </ul>
                        <ul class="list-inline" style="width: 100%;">
                            <li style="width: 60%;"></li>
                            <li style="width: 15%;">Total:</li>
                            <li style="width: 20%;" id="total">0.00</li>
                        </ul>
                    </div>
                    <div class="modal-footer">
                        <button type="button"  class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                        <button type="submit"  class="btn btn-success waves-effect waves-light" >Crear factura</button>
                    </div>
                </form>
            </div>
        </div><!-- /.modal -->
    </div>

    <div id="crear-factura-plan-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;text-align:left;">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="createPlanFactura">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title">Crear factura plan</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="widget-simple text-center"> 
                                    Convenio:
                                    <select class="form-control" id="idconvenioPlan" name="idconvenio">
                                        <option value="0">Seleccione un convenio</option>
                                        <logic:iterate name="convenios" id="convenio">
                                            <option value="<bean:write name="convenio" property="idconvenios"/>"><bean:write name="convenio" property="nombre"/></option>
                                        </logic:iterate>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="row" style="padding-top: 15px;">
                            <div class="input-daterange input-group">
                                <span class="input-group-addon bg-primary b-0 text-white">Desde</span>
                                <input type="text" class="form-control" id="start" />
                                <span class="input-group-addon bg-primary b-0 text-white">Hasta</span>
                                <input type="text" class="form-control" id="end" />
                                <input type="hidden" id="fechaInicial" name="fechaInicial">
                                <input type="hidden" id="fechaFinal" name="fechaFinal">
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button"  class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                        <button type="submit"  class="btn btn-success waves-effect waves-light" >Crear factura</button>
                    </div>
                </form>
            </div>
        </div><!-- /.modal -->
    </div>


    <div id="anular-factura-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;text-align:left;">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="anularFactura">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title">Anular factura</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" id="container">
                            <label>Desea Anular la factura seleccionada ?</label>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button"  class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                        <button type="submit"  class="btn btn-success waves-effect waves-light" >Anular factura</button>
                    </div>
                </form>
            </div>
        </div><!-- /.modal -->
    </div>

    <div id="pagar-factura-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;text-align:left;">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="pagarFactura">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title">Pagar factura</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" id="container">
                            <label>Desea pagar la factura seleccionada ?</label>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button"  class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                        <button type="submit"  class="btn btn-success waves-effect waves-light" >Pagar factura</button>
                    </div>
                </form>
            </div>
        </div><!-- /.modal -->
    </div>

    <div id="masivo-factura-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;text-align:left;">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="recordarFactura">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title">Enviar recordatorio</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" id="container">
                            <label>Desea enviar el recordatorio a todas las facturas pendientes de pago?</label>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button"  class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                        <button type="submit"  class="btn btn-success waves-effect waves-light" >Recordar facturas</button>
                    </div>
                </form>
            </div>
        </div><!-- /.modal -->
    </div>
    <script type="text/javascript">
        Number.prototype.format = function (n, x) {
            var re = '\\d(?=(\\d{' + (x || 3) + '})+' + (n > 0 ? '\\.' : '$') + ')';
            return this.toFixed(Math.max(0, ~~n)).replace(new RegExp(re, 'g'), '$&,');
        };

        var facturasgrid = new dhtmlXGridObject('gridbox');

        //the path to images required by grid 
        facturasgrid.setImagePath(".dhtmlx/codebase/imgs/");
        facturasgrid.attachHeader("#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter");
        facturasgrid.setHeader("ID Factura,Fecha Emisión,Cliente,Valor servicio,IVA, Total,Estado,Ver Factura ");//the headers of columns  
        facturasgrid.setColAlign("left,left,center,center,center,center,center,center");       //the alignment of columns   
        facturasgrid.setColTypes("ro,ed,ed,price,price,price,ro,link");                //the types of columns  
        facturasgrid.setColSorting("int,str,str,int,int,int,str,str");          //the sorting types   
        facturasgrid.attachEvent("onRowSelect", onSelectfactura);
        facturasgrid.init();      //finishes initialization and renders the grid on the page
        var json = <%=request.getAttribute("facturas")%>;
        facturasgrid.clearAll();
        facturasgrid.parse(json, "json");
        var idFactura;
        function onSelectfactura(id) {
            idFactura = id;
        }
        var cont = 2;
        function agregar() {
            var html = "<div class=\"div col-md-12\" style=\"margin-top: 20px;\" id=\"" + cont + "\"> "
                    + "    <ul class=\"list-inline\" style=\"width: 100%;\">"
                    + "        <li style=\"width: 60%;\"><input class=\"desc form-control\" id=\"desc" + cont + "\" placeholder=\"Descripción\"></li>"
                    + "        <li style=\"width: 15%;\"><input class=\"val form-control\" id=\"val" + cont + "\" onchange=\"sumatoria()\" placeholder=\"Valor\"></li>"
                    + "        <li style=\"width: 20%;\"> <button class=\"btn btn-danger form-control\" onclick=\"destroy(" + cont + ")\">Eliminar</button></li>"
                    + "    </ul>"
                    + "</div>";

            $("#container").append(html);
            cont++;
        }

        function destroy(id) {
            $("#" + id).remove();
        }
        var porcentajeIva =<%=request.getAttribute("iva")%>;
        function sumatoria() {
            var subtotalValue = 0;
            for (var i = 1; i < cont; i++) {
                var valor = parseFloat($("#val" + i).val());
                subtotalValue += valor;
            }
            $("#subtotal").text(subtotalValue.format(2));
            var iva = subtotalValue * porcentajeIva;
            $("#iva").text(iva.format(2));
            var total = iva + subtotalValue;
            $("#total").text(total.format(2));
        }

        var longDate = new Date().getTime();
        var fechaInicial = longDate - 2629746000;
        var fechaFinal = longDate;

        $(document).ready(function () {
            $("#createFactura").submit(function (e) {
                e.preventDefault();
                if ($("#idconvenio").val() == 0) {
                    $("#errorLabel").text("Seleccione un convenio");
                    $("#errorModal").modal("show");
                    return false;
                }
                var array = new Array();
                $(".div").each(function (index, element) {
                    var descripcion = $(element).find(".desc").val();
                    var valor = $(element).find(".val").val();
                    if (descripcion == '' || valor == '') {
                        $("#errorLabel").text("Ingrese la descripción y el valor en cada uno de los items.");
                        $("#errorModal").modal("show");
                        return false;
                    }
                    array.push({descripcion: descripcion, valor: parseFloat(valor)});
                });
                $("#items").val(JSON.stringify(array));
                var formData = new FormData(this);
                $('#cargando-modal').modal('show');
                $.ajax({
                    type: "POST",
                    cache: false,
                    url: 'CreateFacturaAutopagos.ap',
                    data: formData,
                    contentType: false,
                    processData: false,
                    success: function (json) {
                        $('#cargando-modal').modal('hide');
                        if (json.error != null) {
                            $("#errorLabel").text(json.error);
                            $("#errorModal").modal("show");
                        } else {
                            $("#crear-factura-libre-modal").modal('hide');
                            facturasgrid.clearAll();
                            facturasgrid.parse(json, "json");
                        }
                    }
                });
            });

            $("#createPlanFactura").submit(function (e) {
                e.preventDefault();
                if ($("#idconvenioPlan").val() == 0) {
                    $("#errorLabel").text("Seleccione un convenio");
                    $("#errorModal").modal("show");
                    return false;
                }
                $("#fechaFinal").val(fechaFinal);
                $("#fechaInicial").val(fechaInicial);
                var formData = new FormData(this);
                $('#cargando-modal').modal('show');
                $.ajax({
                    type: "POST",
                    cache: false,
                    url: 'CreateFacturaPlanAutopagos.ap',
                    data: formData,
                    contentType: false,
                    processData: false,
                    success: function (json) {
                        $('#cargando-modal').modal('hide');
                        if (json.error != null) {
                            $("#errorLabel").text(json.error);
                            $("#errorModal").modal("show");
                        } else {
                            $("#crear-factura-plan-modal").modal('hide');
                            facturasgrid.clearAll();
                            facturasgrid.parse(json, "json");
                        }
                    }
                });
            });

            $("#anularFactura").submit(function (e) {
                e.preventDefault();
                if (!idFactura) {
                    $("#errorLabel").text("Seleccione una factura");
                    $("#errorModal").modal("show");
                    return false;
                }
                $('#cargando-modal').modal('show');
                $.ajax({
                    type: "POST",
                    cache: false,
                    url: 'AnularFacturaAutopagos.ap',
                    data: {id: idFactura},
                    success: function (json) {
                        $('#cargando-modal').modal('hide');
                        if (json.error != null) {
                            $("#errorLabel").text(json.error);
                            $("#errorModal").modal("show");
                        } else {
                            $("#anular-factura-modal").modal('hide');
                            facturasgrid.clearAll();
                            facturasgrid.parse(json, "json");
                        }
                    }
                });
            });


            $("#pagarFactura").submit(function (e) {
                e.preventDefault();
                if (!idFactura) {
                    $("#errorLabel").text("Seleccione una factura");
                    $("#errorModal").modal("show");
                    return false;
                }
                $('#cargando-modal').modal('show');
                $.ajax({
                    type: "POST",
                    cache: false,
                    url: 'PagarFacturaAutopagos.ap',
                    data: {id: idFactura},
                    success: function (json) {
                        $('#cargando-modal').modal('hide');
                        if (json.error != null) {
                            $("#errorLabel").text(json.error);
                            $("#errorModal").modal("show");
                        } else {
                            $("#pagar-factura-modal").modal('hide');
                            facturasgrid.clearAll();
                            facturasgrid.parse(json, "json");
                        }
                    }
                });
            });

            $("#recordarFactura").submit(function (e) {
                e.preventDefault();
                $('#cargando-modal').modal('show');
                $.ajax({
                    type: "POST",
                    cache: false,
                    url: 'RecordarFacturaAutopagos.ap',
                    data: {id: idFactura},
                    success: function (json) {
                        $('#cargando-modal').modal('hide');
                        if (json.error != null) {
                            $("#errorLabel").text(json.error);
                            $("#errorModal").modal("show");
                        } else {
                            $("#masivo-factura-modal").modal('hide');
                            $("#infoLabel").text("Los correos se enviaron de forma correcta");
                            $("#infoModal").modal("show");
                        }
                    }
                });
            });

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
    </script>
</html>
