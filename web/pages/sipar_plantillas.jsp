<%-- 
    Document   : sipar_facturas
    Created on : 12-ago-2016, 15:10:01
    Author     : Usuario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
                    <h4 class="page-title">Tus plantillas !</h4>
                </div>
            </div>
        </div>


        <div class="row">
            <div class="col-lg-12">
                <div class="card-box">
                    <div class="col-lg-6" style="text-align:left;margin-bottom:10px;">
                    </div>
                    <div class="col-lg-6" style="text-align:right;margin-bottom:10px;">
                        <button type="button" class="btn btn-primary waves-effect w-md waves-light m-b-5" data-toggle="modal" data-target="#crear-plantilla-modal" style="-webkit-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);-moz-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);"><i class="fa fa-plus"> </i> Crear</button>
                        <button type="button" onclick="loadPlantilla()" class="btn btn-default waves-effect w-md waves-light m-b-5" style="-webkit-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);-moz-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);"><i class="fa fa-edit"> </i> Editar</button>
                        <button type="button" data-toggle="modal" data-target="#eliminar-plantilla-modal" class="btn btn-danger waves-effect w-md waves-light m-b-5" style="-webkit-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);-moz-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);"><i class="fa fa-minus"> </i> Eliminar</button>
                    </div>
                    <div class="row">
                        <div class="col-lg-12" id="gridbox" style="height:400px;"> 
                        </div>                                        
                    </div> 
                </div>  
            </div> 
        </div>

    </div>
    <!--MODAL --> 
    <div id="crear-plantilla-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;text-align:left;">
        <div class="modal-dialog">
            <div class="modal-content">
                <form name="facturaTemplate" id="createFacturaTemplate" enctype="multipart/form-data" method="post" accept-charset="utf-8">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title">Crear plantilla -Copia de factura a PDF-</h4>
                    </div>
                    <div class="modal-body">

                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="field-1" class="control-label">Nombre de la plantilla</label>
                                    <input type="text" class="form-control" id="nombre" placeholder="Nombre de la plantilla">
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-12">
                                <div class="form-group no-margin">
                                    <label for="field-7" class="control-label">Pega el código <code>HTML</code></label>
                                    <textarea class="form-control autogrow" id="html" placeholder="Pega el código HTML" style="overflow: hidden; word-wrap: break-word; resize: horizontal; height: 250px;"></textarea>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group no-margin">
                                    <label for="field-7" class="control-label">Relación de variables con archivo XLS</label>
                                </div>
                            </div>
                            <div class="col-mg-6" style="text-align:right;">
                                <div class="form-group no-margin">
                                    <button type="button" class="btn btn-warning waves-effect" onclick="addCampo()"><i class="fa fa-plus"></i>Agregar</button>
                                </div>
                            </div>
                        </div>
                        <div class="row" >
                            <div class="col-md-5" style="text-align:left;">
                                <div class="form-group no-margin">
                                    <label for="field-7" class="control-label">Nombre variable <code>$variable</code></label>
                                </div>
                            </div>
                            <div class="col-md-5" style="text-align:left;">
                                <div class="form-group no-margin">
                                    <label for="field-7" class="control-label">Columna XLS <code>col A = 0</code></label>
                                </div>
                            </div>
                            <div class="col-md-2" style="text-align:center;">
                                <div class="form-group no-margin">
                                    <label for="field-7" class="control-label"><i class="fa fa-trash-o"></i></label>
                                </div>
                            </div>
                        </div>

                        <div class="row" id="campos">

                        </div>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-primary waves-effect waves-light">Confirmar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!-- /.modal -->
    <div class="modal fade" id="modal-cargar" >
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title">Plantilla-</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="modal-body" id="modal-div-plantilla">  
                        </div> 
                    </div>
                </div>
                <!--<button class="md-close close">&times;</button>-->
                <div class="modal-footer">
                    <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button> 
                </div> 
            </div>
        </div>
    </div>
    <!--MODAL --> 
    <div id="editar-plantilla-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;text-align:left;">
        <div class="modal-dialog">
            <div class="modal-content">
                <form name="facturaTemplate" id="editFacturaTemplate" enctype="multipart/form-data" method="post" accept-charset="utf-8">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title">Editar plantilla -Copia de factura a PDF-</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="field-1" class="control-label">Nombre de la plantilla</label>
                                    <input type="text" class="form-control" id="nombreEdit" placeholder="Nombre de la plantilla">
                                    <input type="hidden" name="idfacturaTemplate" id="id">
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-12">
                                <div class="form-group no-margin">
                                    <label for="field-7" class="control-label">Pega el código <code>HTML</code></label>
                                    <textarea class="form-control autogrow" id="htmlEdit" placeholder="Pega el código HTML" style="overflow: hidden; word-wrap: break-word; resize: horizontal; height: 250px;"></textarea>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group no-margin">
                                    <label for="field-7" class="control-label">Relación de variables con archivo XLS</label>
                                </div>
                            </div>
                            <div class="col-mg-6" style="text-align:right;">
                                <div class="form-group no-margin">
                                    <button type="button" onclick="addCampoEdit()" class="btn btn-warning waves-effect" ><i class="fa fa-plus"></i>Agregar</button>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-5" style="text-align:left;">
                                <div class="form-group no-margin">
                                    <label for="field-7" class="control-label">Nombre variable <code>$variable</code></label>
                                </div>
                            </div>
                            <div class="col-md-5" style="text-align:left;">
                                <div class="form-group no-margin">
                                    <label for="field-7" class="control-label">Columna XLS <code>col A = 0</code></label>
                                </div>
                            </div>
                            <div class="col-md-2" style="text-align:center;">
                                <div class="form-group no-margin">
                                    <label for="field-7" class="control-label"><i class="fa fa-trash-o"></i></label>
                                </div>
                            </div>
                        </div>

                        <div class="row" id="camposEdit">

                        </div>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-primary waves-effect waves-light">Confirmar</button>
                    </div>
                </form>
            </div>
        </div>
    </div><!-- /.modal -->    



    <!--MODAL --> 
    <div id="eliminar-plantilla-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;text-align:left;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title">Eliminar Plantilla</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-12">
                            <p>Esta seguro que desea eliminar la plantilla seleccionada?</p>
                        </div> 
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                    <button type="button" onclick="eliminarPlantilla()" class="btn btn-danger waves-effect waves-light" data-dismiss="modal">Eliminar Plantilla</button>
                </div>
            </div>
        </div>
    </div><!-- /.modal --> 

    <!-- end col -8 -->

    <script type="text/javascript">
        var gridPlantillas = new dhtmlXGridObject('gridbox');
        //the path to images required by grid  
        gridPlantillas.attachHeader("#text_filter");
        gridPlantillas.setHeader("Nombre,Vista previa");//the headers of columns  
        //mygrid.setInitWidths("100,250,150,100");          //the widths of columns  
        gridPlantillas.setColAlign("center,center");       //the alignment of columns   
        gridPlantillas.setColTypes("ro,link");                //the types of columns  
        gridPlantillas.setColSorting("str,str");          //the sorting types   
        gridPlantillas.attachEvent("onRowSelect", onselect);
        gridPlantillas.init();      //finishes initialization and renders the grid on the page
        var data1 = <%=request.getAttribute("tabla")%>;
        gridPlantillas.clearAll();
        gridPlantillas.parse(data1, "json");
        var idPlantilla;
        function onselect(id) {
            idPlantilla = id;
        }

        var cont = 0;
        function addCampo() {
            var html = "<div class=\"row\" id=\"variable" + cont + "\">" +
                    "    <div class=\"col-md-5\" style=\"text-align:center;\">" +
                    "        <div class=\"form-group no-margin\">" +
                    "            <input type=\"text\" class=\"campo form-control\" id=\"" + cont + "\" placeholder=\"$nombreVariable\">" +
                    "        </div>" +
                    "    </div>" +
                    "    <div class=\"col-md-5\" style=\"text-align:center;\">" +
                    "        <div class=\"form-group no-margin\">" +
                    "            <input type=\"text\" class=\"form-control\" id=\"xls" + cont + "\" placeholder=\"Columna XLS\">" +
                    "        </div>" +
                    "    </div>" +
                    "    <div class=\"col-md-2\" style=\"text-align:center;\">" +
                    "        <div class=\"form-group no-margin\">" +
                    "            <button type=\"button\" class=\"btn btn-danger waves-effect\" onclick=\"deleteVariable('variable" + cont + "')\"><i class=\"fa fa-trash-o\"></i></button>" +
                    "        </div>" +
                    "    </div>" +
                    "</div>";
            $("#campos").append(html);
            cont++;
        }
        function addCampoEdit() {
            var campos = $("#camposEdit");
            var html = "<div class=\"row\" id=\"variable" + cont + "\">" +
                    "    <div class=\"col-md-5\" style=\"text-align:center;\">" +
                    "        <div class=\"form-group no-margin\">" +
                    "            <input type=\"text\" class=\"campoEdit form-control\" id=\"" + cont + "\" placeholder=\"$nombreVariable\">" +
                    "        </div>" +
                    "    </div>" +
                    "    <div class=\"col-md-5\" style=\"text-align:center;\">" +
                    "        <div class=\"form-group no-margin\">" +
                    "            <input type=\"text\" class=\"form-control\" id=\"xlsEdit" + cont + "\" placeholder=\"Columna XLS\">" +
                    "        </div>" +
                    "    </div>" +
                    "    <div class=\"col-md-2\" style=\"text-align:center;\">" +
                    "        <div class=\"form-group no-margin\">" +
                    "            <button type=\"button\" class=\"btn btn-danger waves-effect\" onclick=\"deleteVariable('variable" + cont + "')\"><i class=\"fa fa-trash-o\"></i></button>" +
                    "        </div>" +
                    "    </div>" +
                    "</div>";
            cont++;
            campos.append(html);
        }
        function deleteVariable(id) {
            $("#" + id).remove();
        }
        function preview(id) {
            $.ajax({
                type: "POST",
                cache: false,
                data: {
                    id: id
                },
                url: 'PreviewPlantilla.ap',
                success: function (json) {
                    if (json.error) {
                        $("#errorLabel").text(json.error);
                        $("#errorModal").modal("show");
                    } else {
                        $("#modal-div-plantilla").empty().append(json.html);
                        $("#modal-cargar").modal('show');
                    }
                }
            });
        }
        function loadPlantilla() {
            if (idPlantilla == null) {
                $("#errorLabel").text("Seleccione una plantilla.");
                $('#modal-error').addClass('md-show');
                return;
            }
            $.ajax({
                type: "POST",
                cache: false,
                data: {
                    id: idPlantilla
                },
                url: 'LoadPlantillaJson.ap',
                success: function (json) {
                    if (json.error) {
                        $("#errorLabel").text(json.error);
                        $("#errorModal").modal("show");
                    } else {
                        $("#nombreEdit").val(json.nombre);
                        $("#htmlEdit").val(json.html);
                        $("#id").val(json.id);
                        var array = json.campos;

                        var campos = "";
                        var cont1 = 0;
                        for (var i = 0; i < array.length; i++) {

                            campos += "<div class=\"row\" id=\"variable" + cont1 + "\">" +
                                    "    <div class=\"col-md-5\" style=\"text-align:center;\">" +
                                    "        <div class=\"form-group no-margin\">" +
                                    "            <input type=\"text\" class=\"campoEdit form-control\" id=\"" + cont1 + "\" value=\"" + array[i].referencia + "\" placeholder=\"$nombreVariable\">" +
                                    "        </div>" +
                                    "    </div>" +
                                    "    <div class=\"col-md-5\" style=\"text-align:center;\">" +
                                    "        <div class=\"form-group no-margin\">" +
                                    "            <input type=\"text\" class=\"xlsEdit form-control\" id=\"xlsEdit" + cont1 + "\" value=\"" + array[i].xls + "\" placeholder=\"Columna XLS\">" +
                                    "        </div>" +
                                    "    </div>" +
                                    "    <div class=\"col-md-2\" style=\"text-align:center;\">" +
                                    "        <div class=\"form-group no-margin\">" +
                                    "            <button type=\"button\" class=\"btn btn-danger waves-effect\" onclick=\"deleteVariable('variable" + cont1 + "')\"><i class=\"fa fa-trash-o\"></i></button>" +
                                    "        </div>" +
                                    "    </div>" +
                                    "</div>";
                            cont1++;
                        }
                        cont=cont1;
                        $("#camposEdit").empty().append(campos);
                        $('#editar-plantilla-modal').modal('show');


                    }
                }
            });
        }
        function eliminarPlantilla() {
            if (idPlantilla == null) {
                $("#errorLabel").text("Seleccione una plantilla");
                $("#errorModal").modal("show");
                return;
            }
            $('#cargando-modal').modal('show');
            $.ajax({
                type: "POST",
                cache: false,
                data: {
                    id: idPlantilla
                },
                url: 'DeletePlantilla.ap',
                success: function (json) {
                    $('#cargando-modal').modal('hide');
                    if (json.error) {
                        $("#errorLabel").text(json.error);
                        $("#errorModal").modal("show");
                    } else {
                        gridPlantillas.clearAll();
                        gridPlantillas.parse(json.tabla, "json");
                    }
                }
            });
        }

        $("#createFacturaTemplate").submit(function (e) {
            e.preventDefault();
            var array = new Array();
            $(".campo").each(function (index) {
                array.push({variable: $(this).val(), xls: $("#xls" + this.id).val()});
            });
            $.ajax({
                type: "POST",
                cache: false,
                url: 'CreatePlantillaFactura.ap',
                data: {
                    variables: JSON.stringify(array),
                    nombre: $("#nombre").val(),
                    html: $("#html").val()
                },
                success: function (json) {
                    if (json.error != null) {
                        $("#errorLabel").text(json.error);
                        $("#errorModal").modal("show");
                    } else {
                        $('#crear-plantilla-modal').modal("hide");
                        gridPlantillas.clearAll();
                        gridPlantillas.parse(json.tabla, "json");
                    }
                }
            });
        });
        $("#editFacturaTemplate").submit(function (e) {
            e.preventDefault();
            var array = new Array();
            $(".campoEdit").each(function (index) {
                array.push({variable: $(this).val(), xls: $("#xlsEdit" + this.id).val()});
            });   
            $.ajax({
                type: "POST",
                cache: false,
                url: 'EditPlantillaFactura.ap',
                data: {
                    variables: JSON.stringify(array),
                    nombre: $("#nombreEdit").val(),
                    id: $("#id").val(),
                    html: $("#htmlEdit").val()

                },
                success: function (json) {
                    if (json.error != null) {
                        $("#errorLabel").text(json.error);
                        $("#errorModal").modal("show");
                    } else {
                        $("#editar-plantilla-modal").modal("hide");
                        gridPlantillas.clearAll();
                        gridPlantillas.parse(json.tabla, "json");
                    }
                }
            });
        });
    </script>
</html>
