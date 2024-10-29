<%-- 
    Document   : usuarios
    Created on : 10-ago-2016, 10:44:38
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
                    <h3 class="text-success counter"><bean:write name="usuariosAutored"/></h3>
                    <p class="text-muted">Total de Usuarios Registrados autored</p>
                </div>
            </div> 
            <div class="col-sm-12 col-lg-6">
                <div class="widget-simple text-center card-box">
                    <h3 class="text-muted counter"><bean:write name="usuariosSipar"/></h3>
                    <p class="text-muted">Total de Usuarios Registrados convenios e internos</p>
                </div>
            </div>
        </div>
        <!-- end row --> 
        <div class="row">
            <div class="col-lg-12">
                <div class="card-box">
                    <h4 class="text-dark  header-title m-t-0">Listado de Usuarios</h4>
                    <div class="row">
                        <div style="margin-bottom:10px; text-align:left;" class="col-lg-6">
                            <button type="button" onclick="createXLS()" class="btn btn-primary waves-effect w-md waves-light m-b-5" style="margin-bottom:10px;-webkit-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);-moz-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);"><i class="fa fa-plus"> </i> XLS</button>
                        </div>
                        <div style="margin-bottom:10px; text-align:right;" class="col-lg-6">
                            <button type="button" data-toggle="modal" data-target="#crear-usuario-modal" class="btn btn-primary waves-effect w-md waves-light m-b-5" style="margin-bottom:10px;-webkit-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);-moz-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);"><i class="fa fa-plus"> </i> Crear usuario</button>
                            <button type="button" data-toggle="modal" onclick="loadUser()" class="btn btn-default waves-effect w-md waves-light m-b-5" style="margin-bottom:10px;-webkit-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);-moz-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);"><i class="fa fa-edit"> </i> Editar usuario</button>
                            <button type="button" data-toggle="modal" data-target="#eliminar-usuario-modal" class="btn btn-danger waves-effect w-md waves-light m-b-5" style="margin-bottom:10px;-webkit-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);-moz-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);"><i class="fa fa-minus"> </i> Eliminar usuario</button>
                        </div>
                    </div>
                    <div id="gridbox" style="height:400px;"/>
                    
                </div>
            </div>  
        </div>
    </div>
    <!--MODAL CREATE--> 
    <div id="crear-usuario-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;text-align:left;">
        <div class="modal-dialog">
            <form id="CreateUsuario">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title">Crear usuario</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label">Nombre completo</label>
                                    <input type="text" name="nombre" class="form-control" required placeholder="Nombre">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label">Apellidos</label>
                                    <input type="text" name="apellidos" class="form-control" required placeholder="Apellidos">
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label">Telefono</label>
                                    <input type="text" name="celular" class="form-control" required placeholder="Telefono">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label">Numero documento</label>
                                    <input type="text" name="numeroDeDocumento" class="form-control" required placeholder="Numero documento">
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label">Email</label>
                                    <input type="email" name="email" class="form-control" required placeholder="Email">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label">Clave Autopagos</label>
                                    <input type="password" name="pass" class="form-control" required placeholder="Clave Autopagos">
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label">Tipo de usuario</label>
                                    <select class="form-control" name="idTipoUsuario" id="idTipoUsuario"  required="true" onchange="convenioEnable(this.value)">
                                        <logic:iterate id="tipousuario" name="tipousuarios">
                                            <option value="<bean:write name="tipousuario" property="IDTipoUsuario"/>"><bean:write name="tipousuario" property="nombre"/></option>
                                        </logic:iterate>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label">Convenio Asociado</label>
                                    <select class="form-control" name="idConvenio" id="idConvenio" disabled > 
                                    </select>
                                </div>
                            </div>
                        </div> 
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-primary waves-effect waves-light">Crear Usuario</button>
                    </div>
                </div>
            </form>
        </div>
    </div><!-- /.modal -->
    <!--MODAL EDIT--> 
    <div id="editar-usuario-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;text-align:left;">
        <div class="modal-dialog">
            <form id="EditUsuario">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title">Editar usuario</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label">Nombre completo</label>
                                    <input type="text" name="nombre" class="form-control" id="nombre" required placeholder="Nombre">
                                    <input type="hidden" name="idUsuario" id="id">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label">Apellidos</label>
                                    <input type="text" name="apellidos" class="form-control" id="apellidos" required placeholder="Apellidos">
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label">Telefono</label>
                                    <input type="text" name="celular" class="form-control" id="celular" required placeholder="Telefono">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label">Numero documento</label>
                                    <input type="text" name="numeroDeDocumento" class="form-control" id="numeroDeDocumento" required placeholder="Numero documento">
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label">Email</label>
                                    <input type="email" name="email" class="form-control" disabled id="email" required placeholder="Email">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label">Clave Autopagos</label>
                                    <input type="password" name="pass" class="form-control" id="pass" required placeholder="Clave Autopagos">
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label">Tipo de usuario</label>
                                    <select class="form-control" name="idTipoUsuario" id="idTipoUsuarioEdit"  required="true" onchange="convenioEnableEdit(this.value)">
                                        <logic:iterate id="tipousuario" name="tipousuarios">
                                            <option value="<bean:write name="tipousuario" property="IDTipoUsuario"/>"><bean:write name="tipousuario" property="nombre"/></option>
                                        </logic:iterate>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label">Convenio Asociado</label>
                                    <select class="form-control" name="idConvenio" id="idConvenioEdit" disabled > 
                                    </select>
                                </div>
                            </div>
                        </div> 
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-inverse waves-effect waves-light">Editar Usuario</button>
                    </div>
                </div>
            </form>
        </div>
    </div> 
    <!--MODAL DELETE--> 
    <div id="eliminar-usuario-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;text-align:left;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title">Eliminar usuario</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-12">
                            <p>Esta seguro que desea eliminar el usuario seleccionado?</p>
                        </div>

                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button"  class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                    <button type="button" onclick="deleteUser()" class="btn btn-danger waves-effect waves-light">Eliminar usuario</button>
                </div>
            </div>

        </div><!-- /.modal -->

    </div>
    <!-- end container -->
    <script type="text/javascript">
        var gridUsuarios = new dhtmlXGridObject('gridbox');
        gridUsuarios.setImagePath(".dhtmlx/codebase/imgs/");
        gridUsuarios.attachHeader("#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter");
        gridUsuarios.setHeader("ID Usuario,Nombre,Apellidos,Fecha Inscripción,Email,Celular, Convenio");//the headers of columns  
        gridUsuarios.setInitWidths("100,150,150,170,150,150,150");          //the widths of columns  
        gridUsuarios.setColAlign("left,center,center,center,center,center,center");       //the alignment of columns   
        gridUsuarios.setColTypes("ro,ro,ro,ro,ro,ro,ro");                //the types of columns  
        gridUsuarios.setColSorting("str,str,str,str,str,str,str");          //the sorting types   
        gridUsuarios.init();      //finishes initialization and renders the grid on the page
        gridUsuarios.attachEvent("onRowSelect", onSelectUsuario);
        var grid = <%=request.getAttribute("usuarios")%>;
        gridUsuarios.clearAll();
        gridUsuarios.parse(grid, "json");
        //evento prevencion de crear usuario
        var idUsuario;
        function onSelectUsuario(id) {
            idUsuario = id;
        }
        function convenioEnable(id) {
            //activamos e inactivamos el select del convenio de acuerdo al tipo de usuario
            alert("id: "+id) 
            if (id == 2 || id == 3|| id == 5) {
                //activado 
                //aqui vamos con el llamado de los convenios 
                $.ajax({
                    type: "POST",
                    cache: false,
                    data: {idTipoUsuario: id},
                    url: 'LoadConveniosFromTipoUsuario.ap',
                    success: function (json) {
                        $('#cargando-modal').modal('hide');
                        if (json.error != null) {
                            $("#errorLabel").text(json.error);
                            $("#errorModal").modal("show");
                        } else {
                            $("#idConvenio").removeAttr("disabled");
                            var select = json.select;
                            for (var i = 0; i < select.length; i++) {
                                var option = select[i];
                                $("#idConvenio").append($('<option>', {
                                    value: option.id,
                                    text: option.nombre
                                }));
                            }

                        }
                    }
                });
            } else {
                //inactivo
                $("#idConvenio").attr("disabled", "disabled");
            }

        }
        function convenioEnableEdit(id) {
            //activamos e inactivamos el select del convenio de acuerdo al tipo de usuario
            $("#idConvenioEdit").empty();
            if (id == 2 || id == 3|| id == 5) {
                //activado 
                //aqui vamos con el llamado de los convenios 
                $.ajax({
                    type: "POST",
                    cache: false,
                    data: {idTipoUsuario: id},
                    url: 'LoadConveniosFromTipoUsuario.ap',
                    success: function (json) {
                        $('#cargando-modal').modal('hide');
                        if (json.error != null) {
                            $("#errorLabel").text(json.error);
                            $("#errorModal").modal("show");
                        } else {
                            $("#idConvenioEdit").removeAttr("disabled");
                            var select = json.select;
                            for (var i = 0; i < select.length; i++) {
                                var option = select[i];
                                $("#idConvenioEdit").append($('<option>', {
                                    value: option.id,
                                    text: option.nombre
                                }));
                            }
                            $("#idConvenioEdit").val(convenio);

                        }
                    }
                });
            } else {
                //inactivo
                $("#idConvenioEdit").attr("disabled", "disabled");
            }

        }
        function deleteUser() {
            if (idUsuario == null) {
                $("#errorLabel").empty().append("Seleccione un usuario");
                $("#errorModal").modal('show');
                return null;
            }
            $('#cargando-modal').modal('show');
            $.ajax({
                type: "POST",
                cache: false,
                data: {idUsuario: idUsuario},
                url: 'DeleteUser.ap',
                success: function (json) {
                    $('#cargando-modal').modal('hide');
                    if (json.error != null) {
                        $("#errorLabel").text(json.error);
                        $("#errorModal").modal("show");
                    } else {
                        $('#eliminar-usuario-modal').modal('hide');
                        gridUsuarios.clearAll();
                        gridUsuarios.parse(json, "json");
                    }
                }
            });
        }
        var convenio = 0;
        function loadUser() {
            if (idUsuario == null) {
                $("#errorLabel").empty().append("Seleccione un usuario");
                $("#errorModal").modal('show');
                return null;
            }
            $('#cargando-modal').modal('show');
            $.ajax({
                type: "POST",
                cache: false,
                data: {idUsuario: idUsuario},
                url: 'LoadUsuarioJson.ap',
                success: function (json) {
                    $('#cargando-modal').modal('hide');
                    $("#id").val(json.id);
                    $("#nombre").val(json.nombre);
                    $("#apellidos").val(json.apellidos);
                    $("#email").val(json.email);
                    $("#celular").val(json.celular);
                    $("#pass").val(json.pass);
                    $("#numeroDeDocumento").val(json.numeroDeDocumento);
                    $("#idTipoUsuarioEdit").val(json.idTipoUsuario);
                    convenioEnableEdit(json.idTipoUsuario);
                    $("#idConvenioEdit").val(json.convenio);
                    convenio = json.convenio;
                    $('#editar-usuario-modal').modal('show');
                }
            });
        }
        $(document).ready(function () {
            $("#CreateUsuario").submit(function (e) {
                e.preventDefault();
                var formData = new FormData(this);
                $('#cargando-modal').modal('show');
                $.ajax({
                    type: "POST",
                    cache: false,
                    url: 'CreateUsuario.ap',
                    data: formData,
                    contentType: false,
                    processData: false,
                    success: function (json) {
                        $('#cargando-modal').modal('hide');
                        if (json.error != null) {
                            $("#errorLabel").text(json.error);
                            $("#errorModal").modal("show");
                        } else {
                            $("#crear-usuario-modal").modal('hide');
                            gridUsuarios.clearAll();
                            gridUsuarios.parse(json, "json");
                        }
                    }
                });
            });
            $("#EditUsuario").submit(function (e) {
                e.preventDefault();
                var formData = new FormData(this);
                $('#cargando-modal').modal('show');
                $.ajax({
                    type: "POST",
                    cache: false,
                    url: 'EditarUsuario.ap',
                    data: formData,
                    contentType: false,
                    processData: false,
                    success: function (json) {
                        $('#cargando-modal').modal('hide');
                        if (json.error != null) {
                            $("#errorLabel").text(json.error);
                            $("#errorModal").modal("show");
                        } else {
                            $("#editar-usuario-modal").modal('hide');
                            gridUsuarios.clearAll();
                            gridUsuarios.parse(json, "json");
                        }
                    }
                });
            });
        });

         function createXLS() {
            //mygridResult.toExcel('XlsGenerator.jsn', 'gray'); 
            gridUsuarios.toExcel("https://dhtmlxgrid.appspot.com/export/excel", 'gray');
        }

    </script>
</div>
</html>
