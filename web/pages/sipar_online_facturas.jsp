<%-- 
    Document   : sipar_facturas
    Created on : 12-ago-2016, 15:10:01
    Author     : Usuario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<html>

    <div class="container">
        <!-- Page-Title -->
        <div class="row">
            <div class="col-sm-12">
                <div class="page-title-box">
                    <ol class="breadcrumb pull-right">
                        <li><a href="#">Autopagos</a></li>
                        <li class="active">Módulo facturas - SIPAR</li>
                    </ol>
                    <h4 class="page-title">Resumen de tus facturas !</h4>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="card-box"> 
                    <div class="col-lg-6" style="text-align:left;">
                        <button type="button"  class="btn btn-primary waves-effect w-md waves-light m-b-5 buttonAuto" data-toggle="modal" data-target="#crear-factura-modal"><i class="fa fa-plus"> </i> Crear Factura</button>
                        <button type="button" class="btn btn-danger waves-effect w-md waves-light m-b-5 buttonAuto" data-toggle="modal" data-target="#eliminar-factura-modal" ><i class="fa fa-minus"> </i> Eliminar Factura</button>
                        <button id="plantillas_cargar" type="button" class="btn btn-warning waves-effect w-md waves-light m-b-5 buttonAuto" data-toggle="modal" data-target="#carga-plantilla-factura-modal" ><i class="fa fa-upload"> </i> Carga Plantilla</button>
                        <a type="button" class="btn btn-default waves-effect w-md waves-light m-b-5 buttonAuto" href="docs/plantilla.xlsx"><i class="fa fa-cloud-download"> </i> Plantilla básica</a>
                        <button type="button" class="btn btn-default waves-effect w-md waves-light m-b-5 buttonAuto" data-toggle="modal" data-target="#carga-pantilla-basica-modal" style="background:#2ecc71; color:white;"><i class="fa fa-upload"> </i> Carga básica</button>
                        <button type="button" class="btn waves-effect btn-danger w-md waves-light m-b-5 buttonAuto" data-toggle="modal" data-target="#recordatorios-masivos-modal"><i class="fa fa-bell-o"> </i> Recordatorio</button>
                    </div>
                    <div class="col-lg-6" style="text-align:right;">
                        <button onclick="createXLS()" type="button" class="btn btn-success waves-effect w-md waves-light m-b-5 buttonAuto"><i class="fa fa-file-excel-o"> </i> Excel</button>
                        <button onclick="createPDF()" type="button" class="btn btn-danger waves-effect w-md waves-light m-b-5 buttonAuto"><i class="fa fa-file-pdf-o"> </i> PDF</button>
                    </div> 
                    <div class="row">
                        <div class="col-lg-12" id="gridbox" style="height:350px;"> 
                        </div>    
                    </div> 
                </div> 
            </div>
            <!-- end col -8 --> 
        </div>
        <!-- end row --> 
    </div>
    <!-- end container --> 
    <!--MODAL --> 
    <div id="crear-factura-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title">Crear Factura</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="field-1" class="control-label">Referencia</label>
                                <input type="text" class="form-control" id="numero" placeholder="Referencia">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="field-2" class="control-label">Email</label>
                                <input type="email" class="form-control" id="email" placeholder="Email">
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="field-3" class="control-label">Fecha de Emisión</label> 
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="mm/dd/yyyy" id="datepicker-emision">
                                    <span class="input-group-addon bg-primary b-0 text-white"><i class="fa fa-calendar"></i></span>
                                </div> <!-- input-group -->

                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="field-3" class="control-label">Fecha de Vencimiento</label>
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="mm/dd/yyyy" id="datepicker-vencimiento">
                                    <span class="input-group-addon bg-primary b-0 text-white"><i class="fa fa-calendar"></i></span>
                                </div><!-- input-group -->

                            </div>
                        </div>
                    </div> 
                    <div class="row">
                        <div class="col-md-6 col-md-offset-6">
                            <div class="form-group">
                                <label for="field-2" class="control-label">Valor</label>
                                <input type="text" class="form-control" id="valor" placeholder="Valor">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary waves-effect waves-light" onclick="inscribirFactura()">Crear Factura</button>
                </div>
            </div>
        </div>
    </div><!-- /.modal -->
    <!--MODAL --> 
    <div id="eliminar-factura-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title">Eliminar Facturas</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-12">
                            <p>Esta seguro que desea eliminar las facturas seleccionadas?</p>
                        </div>

                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                    <button type="button" onclick="eliminarFactura()" class="btn btn-danger waves-effect waves-light">Eliminar Facturas</button>
                </div>
            </div>
        </div>
    </div><!-- /.modal -->
    <!--MODAL --> 
    <div id="carga-plantilla-factura-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="subirArchivoPlantilla">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title">Cargar plantilla de facturas</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="form-group">
                                    <label for="field-1" class="control-label">Seleccione plantilla</label>
                                    <select class="form-control" name="plantilla" required>
                                        <logic:iterate name="facturaTemplates" id="plantilla">
                                            <option value="<bean:write name="plantilla" property="idfacturaTemplate"/>"><bean:write name="plantilla" property="nombre"/></option>
                                        </logic:iterate>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <div class="form-group">
                                    <label for="field-3" class="control-label">Seleccione el archivo</label>
                                    <input type="file" name="fileFacturas" class="form-control" required>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-warning waves-effect waves-light">Cargar Facturas</button>
                    </div>
                </form>
            </div>
        </div>
    </div><!-- /.modal -->
    <!--MODAL --> 
    <div id="carga-pantilla-basica-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="subirArchivo">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title">Cargar planilla básica de factura</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="form-group">
                                    <label for="field-3" class="control-label">Seleccione el archivo</label>
                                    <input type="file" name="fileFacturas" class="form-control" required>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-inverse waves-effect waves-light">Cargar Planilla</button>
                    </div>
                </form>
            </div>
        </div>
    </div><!-- /.modal -->
    <!--MODAL RECORDATORIO--> 
    <div id="recordatorios-masivos-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title">Enviar recordatorio masivo</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-12">
                            <p>Esta seguro que desea enviar un recordatorio masivo?</p>
                        </div>

                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                    <button type="button" onclick="sendAllRemember()" class="btn btn-danger waves-effect waves-light">Enviar recordatorio</button>
                </div>
            </div>
        </div>
    </div><!-- /.modal -->
    <script type="text/javascript">
        var ids;
        var old_row_gen = 0;
        var row_gen = 0;
        var longDate = new Date().getTime();
        var emision = longDate;
        var vencimiento = longDate;
        $("#datepicker-emision").datepicker({
            autoclose: true
        }).on('changeDate', function (e) {
            emision = e.date.getTime();
        });
        $("#datepicker-vencimiento").datepicker({
            autoclose: true
        }).on('changeDate', function (e) {
            vencimiento = e.date.getTime();
        });

        //delete plantillas_cargar
        if (<bean:write name="convenio" property="tipoConvenio"/> == 2) {
            $("#plantillas_cargar").remove();
        }
        
        var gridFacturas = new dhtmlXGridObject('gridbox');
        gridFacturas.setImagePath(".dhtmlx/codebase/imgs/");
        gridFacturas.attachHeader("#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,,,");
        gridFacturas.setHeader("Referencia,Fecha - Hora,Estado,Visto,Valor,Valor pagado,Fecha vencimiento,Email, Recordar,Pagar");//the headers of columns   
        gridFacturas.setColAlign("center,center,center,center,center,center,center,center,center,center,center");       //the alignment of columns   
        gridFacturas.setColTypes("ro,ed,ed,ed,ed,ed,ed,link,link,link");                //the types of columns  
        gridFacturas.setColSorting("int,str,str,int,str,str,str,str,str,str,str");          //the sorting types   
        gridFacturas.init();      //finishes initialization and renders the grid on the page
        gridFacturas.enableMultiselect(true);
        gridFacturas.attachEvent("onBeforeSelect", function (row, old_row) {
            //alert("row: " + row + " old_row:" + old_row);
            if (old_row == 0 || old_row_gen != old_row) {
                ids = new Array();
                old_row_gen = old_row;
                if (old_row_gen != 0) {
                    ids.push(old_row_gen);
                }
            }
            if (row_gen != row) {
                ids.push(row);
                row_gen = row;
            }
            return true;
        });
        gridFacturas.attachEvent("onRowSelect", onselect);
        var data = <%=request.getAttribute("tabla")%>
        gridFacturas.clearAll();
        gridFacturas.parse(data, "json");

        $("#subirArchivoPlantilla").submit(function (e) {
            e.preventDefault();
            var formData = new FormData(this);
            $('#cargando-modal').modal('show');
            $.ajax({
                type: "POST",
                cache: false,
                data: formData,
                url: 'UpdateFacturasTemplateForm.ap',
                contentType: false,
                processData: false,
                success: function (json) {
                    $("#cargando-modal").modal('hide');
                    if (json.error != null) {
                        $("#errorLabel").text(json.error);
                        $("#errorModal").modal("show");
                    } else {
                        $('#carga-plantilla-factura-modal').modal('hide');
                        gridFacturas.clearAll();
                        gridFacturas.parse(json.tabla, "json");
                    }
                }
            });
        });
        $("#subirArchivo").submit(function (e) {
            e.preventDefault();
            var formData = new FormData(this);
            $('#cargando-modal').modal('show');
            $.ajax({
                type: "POST",
                cache: false,
                data: formData,
                url: 'UpdateFacturasForm.ap',
                contentType: false,
                processData: false,
                success: function (json) {
                    $('#cargando-modal').modal('hide');
                    if (json.error != null) {
                        $("#errorLabel").text(json.error);
                        $("#errorModal").modal("show");
                    } else {
                        $('#carga-pantilla-basica-modal').modal('hide');
                        gridFacturas.clearAll();
                        gridFacturas.parse(json.tabla, "json");
                    }
                }
            });
        });

        var idFactura;
        function onselect(id) {
            idFactura = id;
        } 
        function createPDF() {
            gridFacturas.toPDF('PdfGenerator.ap', 'gray');
        }
        function createXLS() {
            gridFacturas.toPDF('XlsGenerator.ap', 'gray');
        }
        function inscribirFactura() {
            if ($("#datepicker-emision").val() == '') {
                $("#errorLabel").text("Seleccione una fecha de emisión");
                $('#errorModal').modal('show');
                return;
            }
            if ($("#datepicker-vencimiento").val() == '') {
                $("#errorLabel").text("Seleccione una fecha de vencimiento");
                $('#errorModal').modal('show');
                return;
            }
            $('#cargando-modal').modal('show');
            $.ajax({
                type: "POST",
                cache: false,
                data: {
                    numero: $("#numero").val(),
                    valor: $("#valor").val(),
                    fechaEmision: emision,
                    fechaVencimiento: vencimiento,
                    email: $("#email").val()
                },
                url: 'InscribirFacturaConvenio.ap',
                success: function (json) {
                    $('#cargando-modal').modal('hide');
                    if (json.error) {
                        $("#errorLabel").text(json.error);
                        $('#errorModal').modal('show');
                    } else {
                        $('#crear-factura-modal').modal('hide');
                        gridFacturas.clearAll();
                        gridFacturas.parse(json.tabla, "json");
                    }
                }
            });
        }
        function eliminarFactura() {
            if (idFactura == null) {
                $("#errorLabel").text("Seleccione una factura");
                $("#errorModal").modal('show');
                return;
            }
            $('#cargando-modal').modal('show');
            $.ajax({
                type: "POST",
                cache: false,
                data: {
                    ids: JSON.stringify(ids)
                },
                url: 'DeleteFacturaFromConvenio.ap',
                success: function (json) {
                    $('#cargando-modal').modal('hide');
                    if (json.error) {
                        $("#errorLabel").text(json.error);
                        $("#errorModal").modal('show');
                    } else {
                        $('#eliminar-factura-modal').modal('hide');
                        gridFacturas.clearAll();
                        gridFacturas.parse(json.tabla, "json");
                    }
                }
            });
        }
        function sendAllRemember() {
            $('#cargando-modal').modal('show');
            $.ajax({
                type: "POST",
                cache: false,
                url: 'SendInvoiceRemember.ap',
                success: function (json) {
                    $('#cargando-modal').modal('hide');
                    if (json.error) {
                        $("#errorLabel").text(json.error);
                        $("#errorModal").modal('show');
                    } else {
                        $("#recordatorios-masivos-modal").modal("hide");
                        $("#infoLabel").text("Notificaciónes enviadas correctamente.");
                        $('#infoModal').modal("show");
                    }
                }
            });
        }
        function sendRemember(id) {
            $('#cargando-modal').modal('show');
            $.ajax({
                type: "POST",
                cache: false,
                data: {id: id},
                url: 'RememberInv.ap',
                success: function (json) {
                    $('#cargando-modal').modal('hide');
                    if (json.error) {
                        $("#errorLabel").text(json.error);
                        $("#errorModal").modal('show');
                    } else {
                        $("#infoLabel").text("Notiicaciónes enviadas correctamente.");
                        $('#infoModal').modal('show');
                    }
                }
            });
        }
        function sendPagar(id) {
            $('#cargando-modal').modal('show');
            $.ajax({
                type: "POST",
                cache: false,
                data: {id: id},
                url: 'PagarInv.ap',
                success: function (json) {
                    $('#cargando-modal').modal('hide');
                    if (json.error) {
                        $("#errorLabel").text(json.error);
                        $("#errorModal").modal('show');
                    } else {
                        $("#infoLabel").text("Pago realizado correctamente.");
                        $('#infoModal').modal('show');
                        gridFacturas.clearAll();
                        gridFacturas.parse(json.tabla, "json");
                    }
                }
            });
        }


    </script>
</html>
