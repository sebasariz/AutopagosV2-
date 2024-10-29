<%-- 
    Document   : conveinos
    Created on : 10-ago-2016, 11:48:35
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
                        <li class="active">Gestión de convenios</li>
                    </ol>
                    <h4 class="page-title">Bienvenido !</h4>
                </div>
            </div>
        </div>  

        <div class="row">
            <div class="col-lg-12">
                <div class="card-box">
                    <h4 class="text-dark  header-title m-t-0">Convenios</h4>
                    <div style="margin-bottom:10px; text-align:right;">
                        <button type="button" data-toggle="modal" data-target="#crear-convenio-modal" class="btn btn-primary waves-effect w-md waves-light m-b-5" style="margin-bottom:10px;-webkit-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);-moz-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);"><i class="fa fa-plus"> </i> Crear convenio</button>
                        <button type="button" data-toggle="modal" onclick="loadConvenio()" class="btn btn-default waves-effect w-md waves-light m-b-5" style="margin-bottom:10px;-webkit-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);-moz-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);"><i class="fa fa-edit"> </i> Editar convenio</button>
                        <button type="button" data-toggle="modal" data-target="#eliminar-convenio-modal" class="btn btn-danger waves-effect w-md waves-light m-b-5" style="margin-bottom:10px;-webkit-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);-moz-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);"><i class="fa fa-minus"> </i> Eliminar convenio</button>
                        <div id="grid" style="height:400px;"></div>
                        <!--MODAL CREATE--> 
                        <div id="crear-convenio-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;text-align:left;">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                        <h4 class="modal-title">Crear convenio</h4>
                                    </div>
                                    <div class="modal-body">
                                        <form id="CreateConvenio">
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">Nombre convenio</label>
                                                        <input class="form-control" type="text" name="nombre" placeholder="Nombre" required="true">
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-2" class="control-label">Generar token API</label>
                                                        <input class="form-control" type="text" name="codigo" id="codigo" placeholder="Codigo para API click para autogenerar" onclick="guid(this);" required="true">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">Texto guía de pago</label>
                                                        <input class="form-control" type="text" name="textoGuiaTercero" id="textoGuia" placeholder="Texto guia para pago (numero referencia - cedula)">
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">NIT</label>
                                                        <input class="form-control" type="text" name="nit" placeholder="Nit" required="true">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">Dirección</label>
                                                        <input class="form-control" type="text" name="direccion" placeholder="Dirección" required="true">
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-2" class="control-label">Teléfono</label>
                                                        <input class="form-control" type="text" name="telefono" placeholder="Telefono" required="true">
                                                    </div>
                                                </div>
                                            </div> 




                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">Valor fijo usuario</label>
                                                        <input class="form-control" type="text" name="valorFijoUsuario" placeholder="Valor fijo usuario" required="true">
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-2" class="control-label">Valor variable usuario</label>
                                                        <input class="form-control" type="text" name="valorVariableUsuario" placeholder="Valor variable usuario (%)" required="true">
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">Direccion de respuesta</label>
                                                        <input class="form-control" type="text" name="direccionRespuesta" placeholder="direccion de respuesta servicio pago" >
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">Logo</label>
                                                        <input class="form-control" type="file" name="logoFileForm" placeholder="Logo" required="true">
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row"> 
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">RUT</label>
                                                        <input class="form-control" type="file" name="logoFileFormRut" placeholder="Rut" >
                                                    </div>
                                                </div> 
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-2" class="control-label">Camara de comercio</label>
                                                        <input class="form-control" type="file" name="logoFileFormCCio" placeholder="Camara de comercio" >
                                                    </div>
                                                </div>
                                            </div>



                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">Categoria</label>
                                                        <select class="form-control"required="true" name="idClase">
                                                            <logic:iterate id="clase" name="clases">
                                                                <option value="<bean:write name="clase" property="idclase"/>"><bean:write name="clase" property="nombre"/></option>
                                                            </logic:iterate>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-2" class="control-label">Estado convenio</label>
                                                        <select class="form-control" name="activo">
                                                            <option value="1">Activo</option>
                                                            <option value="0">Inactivo</option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">   
                                                <div class="col-md-12">
                                                    <div class="form-group">
                                                        <label for="field-2" class="control-label">Valor de pago variable</label>
                                                        <select name="valorVariable" class="form-control">
                                                            <option value="0">No</option>
                                                            <option value="1">Si</option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="col-md-12">
                                                    <div class="form-group">
                                                        <label for="field-2" class="control-label">Tipo de convenio</label>
                                                        <select class="form-control" name="tipoConvenio" id="tipoConvenio" placeholder="Tipo de convenio" required="true" onchange="changneTipoConvenio(this.value)">
                                                            <option value="1">SIPAR</option>
                                                            <option value="2">Autopagos Online</option>
                                                            <option value="3">Corresponsalía</option>
                                                            <option value="4">Aliado</option>
                                                        </select>
                                                    </div>
                                                </div> 
                                            </div>


                                            <div class="row">
                                                <div class="col-md-12" style="text-align:center">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">Parámetros del convenio</label>
                                                    </div>
                                                </div>    
                                            </div>

                                            <div id="sipar" style="display: block;"> 
                                                <div class="row"> 
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-3" class="control-label">Fecha de corte</label>
                                                            <div class="input-group">
                                                                <input type="text" class="form-control"  placeholder="mm/dd/yyyy" id="dpStart">
                                                                <span class="input-group-addon bg-primary b-0 text-white"><i class="fa fa-calendar"></i></span>
                                                            </div> <!-- input-group -->
                                                            <input type="hidden" name="fechaCortePlanPost" id="fechaFacturacionPost">
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-1" class="control-label">Seleccione el plan</label>
                                                            <select class="form-control"required="true" name="idPlan">
                                                                <logic:iterate id="plan" name="planes">
                                                                    <option value="<bean:write name="plan" property="idplan"/>"><bean:write name="plan" property="nombre"/></option>
                                                                </logic:iterate>
                                                            </select>
                                                        </div>
                                                    </div> 

                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Canal transaccional</label>
                                                            <select class="form-control"required="true" name="idCanal">
                                                                <logic:iterate id="canal" name="canales">
                                                                    <option value="<bean:write name="canal" property="idcanal"/>"><bean:write name="canal" property="nombre"/></option>
                                                                </logic:iterate>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">ID Canal</label>
                                                            <input class="form-control" type="text" name="codigoCanalPlanPost" id="codigoCanal" placeholder="Ingrese el codigo del canal">
                                                        </div>
                                                    </div>

                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Usuario canal</label>
                                                            <input class="form-control" type="text" name="usuarioCanalPlanPost" id="usuarioCanal" placeholder="Usuario canal transaccional">
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Clave canal</label>
                                                            <input class="form-control" type="text" name="passCanalPlanPost" id="passCanal" placeholder="Clave canal transaccional">
                                                        </div>
                                                    </div> 

                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Campos obligatorios</label>
                                                            <input class="form-control" type="text" name="camposObligatoriosSipar" placeholder="Campos obligatorios">
                                                        </div>
                                                    </div> 
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Campo identificador</label>
                                                            <input class="form-control" type="text" name="campoIdentificadorSipar" placeholder="Campo identificador">
                                                        </div>
                                                    </div>  
                                                </div> 
                                            </div>
                                            <div id="online" style="display: none;">

                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-1" class="control-label">Numero de cuenta</label>
                                                            <input class="form-control" type="text" name="numeroCuenta" placeholder="Numero cuenta" >
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Titular de cuenta</label>
                                                            <input class="form-control" type="text" name="titularCuenta" placeholder="Titular en la cuenta" >
                                                        </div>
                                                    </div>
                                                </div> 
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-1" class="control-label">Banco</label>
                                                            <input class="form-control" type="text" name="banco" placeholder="Banco" >
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Tipo de cuenta</label>
                                                            <select class="form-control"required="true" name="idTipoCuenta">
                                                                <logic:iterate id="tipoCuenta" name="tipoCuentas">
                                                                    <option value="<bean:write name="tipoCuenta" property="idtipoCuenta"/>"><bean:write name="tipoCuenta" property="nombre"/></option>
                                                                </logic:iterate>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Certificado bancario</label>
                                                            <input class="form-control" type="file" name="logoFileFormBanco" placeholder="Banco" >
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-1" class="control-label">Valor fijo convenio</label>
                                                            <input class="form-control" type="text" name="valorFijoConvenio" placeholder="Valor fijo convenio" >
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Valor variable convenio</label>
                                                            <input class="form-control" type="text" name="valorVariableConvenio" placeholder="Valor variable convenio (%)" >
                                                        </div>
                                                    </div> 
                                                </div>
                                            </div>
                                            <div id="corresponsal" style="display: none;">

                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Código de producto tercero</label>
                                                            <input class="form-control" type="text" name="codigoProductoTercero" id="codigoProductoTercero" placeholder="Codigo winred">
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Tipo de comisión</label>
                                                            <select class="form-control" name="valorFijoTercero" id="valorFijo">
                                                                <option value="1">Valor fijo</option>
                                                                <option value="0">Valor variable</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Valor comision fijo</label>
                                                            <input class="form-control" type="text" name="comisionTerceroFija" id="comisionTerceroFija" placeholder="comision fija">
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Valor comisión variable </label>
                                                            <input class="form-control" type="text" name="comisionTerceroVariable" id="comisionTerceroVariable" placeholder="comision variable">
                                                        </div>
                                                    </div>
                                                </div> 
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Codigo de barras</label>
                                                            <input class="form-control" type="text" name="codigoBarrasTercero" id="codigoBarrasTercero" placeholder="codigo de barras">
                                                        </div>
                                                    </div> 
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Banco</label>
                                                            <select name="bancoTercero" class="form-control">
                                                                <option value="1">Avvillas</option>
                                                                <option value="2">BBVA</option>
                                                            </select>
                                                        </div>
                                                    </div> 
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Recarga</label>
                                                            <select name="recargaTercero" class="form-control">
                                                                <option value="0">No - Factura fija</option>
                                                                <option value="1">SI - Factura variable</option>
                                                            </select>
                                                        </div>
                                                    </div> 
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                                                <button type="submit" class="btn btn-primary waves-effect waves-light">Crear Usuario</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div><!-- /.modal -->
                        </div>
                        <!--MODAL EDIT--> 
                        <div id="editar-convenio-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;text-align:left;">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                        <h4 class="modal-title">Editar convenio</h4>
                                    </div>
                                    <div class="modal-body">
                                        <form id="EditConvenio">
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">Nombre convenio</label>
                                                        <input class="form-control" type="text" name="nombre" id="nombreEdit" placeholder="Nombre" required="true">
                                                        <input type="hidden" name="idconvenios" id="idConvenioEdit">
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-2" class="control-label">Generar token API</label>
                                                        <input class="form-control" type="text" name="codigo" id="codigoEdit" placeholder="Codigo para API click para autogenerar" onclick="guid(this);" required="true">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">Texto guía de pago</label>
                                                        <input class="form-control" type="text" name="textoGuiaTercero" id="textoGuiaEdit" placeholder="Texto guia para pago (numero referencia - cedula)">
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">NIT</label>
                                                        <input class="form-control" type="text" name="nit" id="nitEdit" placeholder="Nit" required="true">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">Dirección</label>
                                                        <input class="form-control" type="text" name="direccion" id="direccionEdit" placeholder="Dirección" required="true">
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-2" class="control-label">Teléfono</label>
                                                        <input class="form-control" type="text" name="telefono" id="telefonoEdit" placeholder="Telefono" required="true">
                                                    </div>
                                                </div>
                                            </div>  
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">Valor fijo usuario</label>
                                                        <input class="form-control" type="text" name="valorFijoUsuario" id="valorFijoUsuarioEdit" placeholder="Valor fijo usuario" required="true">
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-2" class="control-label">Valor variable usuario</label>
                                                        <input class="form-control" type="text" name="valorVariableUsuario" id="valorVariableUsuarioEdit" placeholder="Valor variable usuario (%)" required="true">
                                                    </div>
                                                </div>
                                            </div> 
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">Direccion de respuesta</label>
                                                        <input class="form-control" type="text" name="direccionRespuesta" id="direccionRespuestaEdit" placeholder="direccion de respuesta servicio pago" >
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label"><a id="logoAEdit">Logo</a></label>
                                                        <input class="form-control" type="file" name="logoFileForm" placeholder="Logo" >
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row"> 
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label"><a id="rutAEdit">RUT</a></label>
                                                        <input class="form-control" type="file" name="logoFileFormRut" placeholder="Rut" >
                                                    </div>
                                                </div> 
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-2" class="control-label"><a id="ccioAEdit">Camara de comercio</a></label>
                                                        <input class="form-control" type="file" name="logoFileFormCCio" placeholder="Camara de comercio" >
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">Categoria</label>
                                                        <select class="form-control"required="true" name="idClase" id="idClaseEdit">
                                                            <logic:iterate id="clase" name="clases">
                                                                <option value="<bean:write name="clase" property="idclase"/>"><bean:write name="clase" property="nombre"/></option>
                                                            </logic:iterate>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label for="field-2" class="control-label">Estado convenio</label>
                                                        <select class="form-control" name="activo" id="activoEdit">
                                                            <option value="1">Activo</option>
                                                            <option value="0">Inactivo</option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">   
                                                <div class="col-md-12">
                                                    <div class="form-group">
                                                        <label for="field-2" class="control-label">Valor de pago variable</label>
                                                        <select name="valorVariable" id="valorVariable" class="form-control">
                                                            <option value="0">No</option>
                                                            <option value="1">Si</option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <div class="form-group">
                                                        <label for="field-2" class="control-label">Tipo de convenio</label>
                                                        <select class="form-control" name="tipoConvenio" id="tipoConvenioEdit" placeholder="Tipo de convenio" required="true" onchange="changneTipoConvenioEdit(this.value)">
                                                            <option value="1">SIPAR</option>
                                                            <option value="2">Autopagos Online</option>
                                                            <option value="3">Corresponsalía</option>
                                                            <option value="4">Aliado</option>
                                                        </select>
                                                    </div>
                                                </div> 
                                            </div>
                                            <div class="row">
                                                <div class="col-md-12" style="text-align:center">
                                                    <div class="form-group">
                                                        <label for="field-1" class="control-label">Parámetros del convenio</label>
                                                    </div>
                                                </div>    
                                            </div> 
                                            <div id="siparEdit" style="display: block;"> 
                                                <div class="row"> 
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-3" class="control-label">Fecha de corte</label>
                                                            <div class="input-group">
                                                                <input type="text" class="form-control"  placeholder="mm/dd/yyyy" id="dpStartEdit">
                                                                <span class="input-group-addon bg-primary b-0 text-white"><i class="fa fa-calendar"></i></span>
                                                            </div> <!-- input-group -->
                                                            <input type="hidden" name="fechaCortePlanPost" id="fechaFacturacionPostEdit">
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-1" class="control-label">Seleccione el plan</label>
                                                            <select class="form-control"required="true" name="idPlan" id="idPlanEdit">
                                                                <logic:iterate id="plan" name="planes">
                                                                    <option value="<bean:write name="plan" property="idplan"/>"><bean:write name="plan" property="nombre"/></option>
                                                                </logic:iterate>
                                                            </select>
                                                        </div>
                                                    </div>  
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Canal transaccional</label>
                                                            <select class="form-control"required="true" name="idCanal" id="idCanalEdit">
                                                                <logic:iterate id="canal" name="canales">
                                                                    <option value="<bean:write name="canal" property="idcanal"/>"><bean:write name="canal" property="nombre"/></option>
                                                                </logic:iterate>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">ID Canal</label>
                                                            <input class="form-control" type="text" name="codigoCanalPlanPost" id="codigoCanalEdit"  placeholder="Ingrese el codigo del canal">
                                                        </div>
                                                    </div> 
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Usuario canal</label>
                                                            <input class="form-control" type="text" name="usuarioCanalPlanPost" id="usuarioCanalEdit" placeholder="Usuario canal transaccional">
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Clave canal</label>
                                                            <input class="form-control" type="text" name="passCanalPlanPost" id="passCanalEdit" placeholder="Clave canal transaccional">
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Campos obligatorios</label>
                                                            <input class="form-control" type="text" name="camposObligatoriosSipar" id="camposObligatoriosSipar" placeholder="Campos obligatorios">
                                                        </div>
                                                    </div> 
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Campo identificador</label>
                                                            <input class="form-control" type="text" name="campoIdentificadorSipar" id="campoIdentificadorSipar" placeholder="Campo identificador">
                                                        </div>
                                                    </div> 
                                                    <div class="col-md-12">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Estado pago:</label>
                                                            <label id="moraEdit"></label>
                                                        </div>
                                                    </div>
                                                </div> 
                                            </div>
                                            <div id="onlineEdit" style="display: none;">

                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-1" class="control-label">Numero de cuenta</label>
                                                            <input class="form-control" type="text" name="numeroCuenta" id="numeroCuentaEdit" placeholder="Numero cuenta" >
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Titular de cuenta</label>
                                                            <input class="form-control" type="text" name="titularCuenta" id="titularCuentaEdit" placeholder="Titular en la cuenta" >
                                                        </div>
                                                    </div>
                                                </div> 
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-1" class="control-label">Banco</label>
                                                            <input class="form-control" type="text" name="banco" placeholder="Banco" id="bancoEdit">
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Tipo de cuenta</label>
                                                            <select class="form-control"required="true" name="idTipoCuenta" id="idTipoCuentaEdit">
                                                                <logic:iterate id="tipoCuenta" name="tipoCuentas">
                                                                    <option value="<bean:write name="tipoCuenta" property="idtipoCuenta"/>"><bean:write name="tipoCuenta" property="nombre"/></option>
                                                                </logic:iterate>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label"><a id="bancoAEdit">Certificado bancario</a></label>
                                                            <input class="form-control" type="file" name="logoFileFormBanco" placeholder="Banco" >
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-1" class="control-label">Valor fijo convenio</label>
                                                            <input class="form-control" type="text" name="valorFijoConvenio" id="valorfijoConvenioEdit" placeholder="Valor fijo convenio" >
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Valor variable convenio</label>
                                                            <input class="form-control" type="text" name="valorVariableConvenio" id="valorVariableConvenioEdit" placeholder="Valor variable convenio (%)" >
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div id="corresponsalEdit" style="display: none;">
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Código de producto tercero</label>
                                                            <input class="form-control" type="text" name="codigoProductoTercero" id="codigoProductoTerceroEdit" placeholder="Codigo winred">
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Tipo de comisión</label>
                                                            <select class="form-control" name="valorFijoTercero" id="valorFijoEdit">
                                                                <option value="1">Valor fijo</option>
                                                                <option value="0">Valor variable</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Valor comision fijo</label>
                                                            <input class="form-control" type="text" name="comisionTerceroFija" id="comisionTerceroFijaEdit" placeholder="comision fija">
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Valor comisión variable </label>
                                                            <input class="form-control" type="text" name="comisionTerceroVariable" id="comisionTerceroVariableEdit" placeholder="comision variable">
                                                        </div>
                                                    </div>
                                                </div> 
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Codigo de barras</label>
                                                            <input class="form-control" type="text" name="codigoBarrasTercero" id="codigoBarrasTerceroEdit" placeholder="codigo de barras">
                                                        </div>
                                                    </div> 
                                                </div> 
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Banco</label>
                                                            <select name="bancoTercero" id="bancoTerceroEdit" class="form-control">
                                                                <option value="1">Avvillas</option>
                                                                <option value="2">BBVA</option>
                                                            </select>
                                                        </div>
                                                    </div> 
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="form-group">
                                                            <label for="field-2" class="control-label">Recarga</label>
                                                            <select name="recargaTercero" id="recargaTerceroEdit" class="form-control">
                                                                <option value="0">No - Factura fija</option>
                                                                <option value="1">SI - Factura variable</option>
                                                            </select>
                                                        </div>
                                                    </div> 
                                                </div>
                                            </div> 
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                                                <button type="submit" class="btn btn-primary waves-effect waves-light">Editar Usuario</button>
                                            </div>
                                        </form>


                                    </div>
                                </div>
                            </div>
                        </div><!-- /.modal -->
                        <!--MODAL DELETE--> 
                        <div id="eliminar-convenio-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;text-align:left;">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                        <h4 class="modal-title">Eliminar convenio</h4>
                                    </div>
                                    <div class="modal-body">
                                        <div class="row">
                                            <div class="col-md-12">
                                                <p>Esta seguro que desea eliminar el convenio seleccionado?</p>
                                            </div>

                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">Cancelar</button>
                                        <button type="button" onclick="deleteConvenio()" class="btn btn-danger waves-effect waves-light">Eliminar convenio</button>
                                    </div>
                                </div>

                            </div><!-- /.modal -->

                        </div>

                    </div>
                </div>
            </div>    
            <!-- end container -->
        </div>

        <script type="text/javascript">
            $("#fechaFacturacionPost").val(new Date().getTime());
            $('#dpStart').datepicker({
                autoclose: true
            }).on('changeDate', function (e) {
                $("#fechaFacturacionPost").val(e.date.getTime());
            });
            $('#dpStart').datepicker('update', new Date());
            var height = $(window).height();
            height = height - 300;
            $("#grid").height(height);

            var gridConvenios = new dhtmlXGridObject('grid');
            gridConvenios.setImagePath("codebase/imgs/");
            gridConvenios.setHeader("NIT,Logo,Nombre convenio,Tipo convenio");
            gridConvenios.attachHeader("#text_filter,#text_filter,#text_filter,#text_filter");
            gridConvenios.setColAlign("center,center,center,center");
            gridConvenios.setColTypes("ro,img,ro,ro");
            gridConvenios.setColSorting("str,str,str,str");
            gridConvenios.attachEvent("onRowSelect", onSelectCovnenio);
            gridConvenios.init();
            var grid = <%=request.getAttribute("convenios")%>;
            gridConvenios.clearAll();
            gridConvenios.parse(grid, "json");
            var idConvenio;
            function onSelectCovnenio(id) {
                idConvenio = id;
            }

            $(document).ready(function () {
                $("#CreateConvenio").submit(function (e) {
                    e.preventDefault();
                    var formData = new FormData(this);
                    $('#cargando-modal').modal('show');
                    $.ajax({
                        type: "POST",
                        cache: false,
                        url: 'CreateConvenio.ap',
                        data: formData,
                        contentType: false,
                        processData: false,
                        success: function (json) {
                            $('#cargando-modal').modal('hide');
                            if (json.error != null) {
                                $("#errorLabel").text(json.error);
                                $("#errorModal").modal("show");
                            } else {
                                $('#crear-convenio-modal').modal('hide');
                                gridConvenios.clearAll();
                                gridConvenios.parse(json, "json");
                            }
                        }
                    });
                });

                $("#EditConvenio").submit(function (e) {
                    e.preventDefault();
                    var formData = new FormData(this);
                    $('#cargando-modal').modal('show');
                    $.ajax({
                        type: "POST",
                        cache: false,
                        url: 'EditConvenio.ap',
                        data: formData,
                        contentType: false,
                        processData: false,
                        success: function (json) {
                            $('#cargando-modal').modal('hide');
                            if (json.error != null) {
                                $("#errorLabel").text(json.error);
                                $("#errorModal").modal("show");
                            } else {
                                $('#editar-convenio-modal').modal('hide');
                                gridConvenios.clearAll();
                                gridConvenios.parse(json, "json");
                            }
                        }
                    });
                });
            });


            function deleteConvenio() {
                if (idConvenio == null) {
                    $("#errorLabel").empty().append("Seleccione un convenio");
                    $("#errorModal").addClass('md-show');
                    return null;
                }
                $('#cargando-modal').modal('show');
                $.ajax({
                    type: "POST",
                    cache: false,
                    data: {idConvenio: idConvenio},
                    url: 'DeleteConvenio.ap',
                    success: function (json) {
                        $('#cargando-modal').modal('hide');
                        if (json.error != null) {
                            $("#errorLabel").text(json.error);
                            $("#errorModal").modal("show");
                        } else {
                            $('#eliminar-convenio-modal').modal('hide');
                            gridConvenios.clearAll();
                            gridConvenios.parse(json, "json");
                        }
                    }
                });
            }

            function loadConvenio() {
                if (idConvenio == null) {
                    $("#errorLabel").empty().append("Seleccione un convenio");
                    $("#errorModal").modal('show');
                    return null;
                }
//                $('#cargando-modal').modal('show');
                $.ajax({
                    type: "POST",
                    cache: false,
                    data: {idConvenio: idConvenio},
                    url: 'LoadConvenioJson.ap',
                    success: function (json) {
//                        $('#cargando-modal').modal('hide');
                        $("#idConvenioEdit").val(json.idConvenio);
                        $("#nombreEdit").val(json.nombre);
                        $("#codigoEdit").val(json.codigo);
                        $("#textoGuiaEdit").val(json.textoGuia);
                        $("#nitEdit").val(json.nit);
                        $("#direccionEdit").val(json.direccion);
                        $("#telefonoEdit").val(json.telefono);
                        $("#valorFijoUsuarioEdit").val(json.valorFijoUsuario);
                        $("#valorVariableUsuarioEdit").val(json.valorVariableUsuario);
                        $("#direccionRespuestaEdit").val(json.dirRespuesta);
                        $("#logoAEdit").attr("href", json.logo);
                        $("#rutAEdit").attr("href", json.rut);
                        $("#ccioAEdit").attr("href", json.camaraComercio);
                        $("#idClaseEdit").val(json.clase);
                        $("#activoEdit").val(json.activo);
                        $("#valorVariable").val(json.valorVariable);
                        changneTipoConvenioEdit(json.tipoConvenio)
                        $("#tipoConvenioEdit").val(json.tipoConvenio);
//                        SIPAR 
                        $("#fechaFacturacionPostEdit").val(json.fechaFacturacion);
                        $('#dpStartEdit').datepicker().on('changeDate', function (e) {
                            $("#fechaFacturacionPostEdit").val(e.date.getTime());
                        });
                        $('#dpStartEdit').datepicker('update', new Date(json.fechaFacturacion));
                        $("#idPlanEdit").val(json.idPlan);
                        $("#idCanalEdit").val(json.idCanal);
                        $("#codigoCanalEdit").val(json.codigoCanal);
                        $("#usuarioCanalEdit").val(json.usuarioCanal);
                        $("#passCanalEdit").val(json.passCanal);
//                        ONLINE
                        $("#numeroCuentaEdit").val(json.numeroCuenta);
                        $("#bancoEdit").val(json.banco);
                        $("#titularCuentaEdit").val(json.titularCuenta);
                        $("#idTipoCuentaEdit").val(json.idTipoCuenta);
                        $("#bancoAEdit").attr("href", json.bancoDoc);
                        $("#valorFijoConvenioEdit").val(json.valorFijoConvenio);
                        $("#valorVariableConvenioEdit").val(json.valorVariableConvenio);
//                        CORRESPONSALIA
                        $("#codigoProductoTerceroEdit").val(json.codigoProductoTercero);
                        $("#valorFijoEdit").val(json.valorFijo);
                        $("#comisionTerceroFijaEdit").val(json.comisionWinredFija);
                        $("#comisionTerceroVariableEdit").val(json.comisionWinredVariable);
                        $("#moraEdit").text(json.mora);

                        $("#camposObligatoriosSipar").val(json.camposObligatoriosSipar);
                        $("#campoIdentificadorSipar").val(json.campoIdentificadorSipar);
                        $("#codigoBarrasTerceroEdit").val(json.codigoBarrasTercero);
                        $("#bancoTerceroEdit").val(json.bancoTercero);
                        $("#recargaTerceroEdit").val(json.recargaTercero);
                        $('#editar-convenio-modal').modal('show');
                    }
                });
            }

            function guid() {
                function s4() {
                    return Math.floor((1 + Math.random()) * 0x10000)
                            .toString(16)
                            .substring(1);
                }
                var uuid = s4() + s4() + '-' + s4() + '-' + s4() + '-' +
                        s4() + '-' + s4() + s4() + s4();
                $("#codigo").val(uuid);
            }

            function changneTipoConvenio(id) {
                if (id == 1) {
                    //sipar
                    $("#sipar").show();
                    $("#online").hide();
                    $("#corresponsal").hide();

                } else if (id == 2) {
                    //online
                    $("#sipar").hide();
                    $("#online").show();
                    $("#corresponsal").hide();

                } else if (id == 3) {
                    //corresponsal
                    $("#sipar").hide();
                    $("#online").hide();
                    $("#corresponsal").show();
                } else if (id == 4) {
                    //aliado
                    $("#sipar").hide();
                    $("#online").hide();
                    $("#corresponsal").hide();
                } 
            }
            function changneTipoConvenioEdit(id) {
                if (id == 1) {
                    //sipar
                    $("#siparEdit").show();
                    $("#onlineEdit").hide();
                    $("#corresponsalEdit").hide();

                } else if (id == 2) {
                    //online
                    $("#siparEdit").hide();
                    $("#onlineEdit").show();
                    $("#corresponsalEdit").hide();

                } else if (id == 3) {
                    //corresponsal
                    $("#siparEdit").hide();
                    $("#onlineEdit").hide();
                    $("#corresponsalEdit").show();
                } else if (id == 4) {
                    //aliado
                    $("#siparEdit").hide();
                    $("#onlineEdit").hide();
                    $("#corresponsalEdit").hide();
                }
            }
        </script>
</html>
