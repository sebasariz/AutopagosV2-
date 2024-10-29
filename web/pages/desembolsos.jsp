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
                <div class="col-lg-12">
                    <div class="card-box">
                        <h4 class="text-dark  header-title m-t-0">Desembolsos pendientes</h4>
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

    <div id="confirm" class="modal fade" tabindex="-1" role="dialog" style="display: none;text-align:left;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    Seguro el pago fue realizado?
                </div>
                <div class="modal-footer">
                    <button type="button" data-dismiss="modal" class="btn">Cancel</button>
                    <button type="button" data-dismiss="modal" class="btn btn-primary" onclick="changeState()">Eliminar</button>
                </div>
            </div>
        </div>
    </div>
    <script type="text/javascript">
        var desembolsos = new dhtmlXGridObject('gridbox');
        desembolsos.attachHeader("#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter");
        desembolsos.setHeader("Fecha - Hora, Valor, Convenio, Cuenta N, Tipo cuenta, Banco,Titular"); //the headers of columns  
        desembolsos.setInitWidths("100,200,150,100,100,100,150");
        desembolsos.setColAlign("center,center,center,center,center,center,center"); //the alignment of columns   
        desembolsos.setColTypes("ro,price,ed,ed,ed,ed,ed"); //the types of columns  
        desembolsos.setColSorting("int,str,str,int,str,str,str"); //the sorting types   
        desembolsos.attachEvent("onRowSelect", onselect);
        desembolsos.init(); //finishes initialization and renders the grid on the page

        $(document).ready(function () {
            $('#cargando-modal').modal('show');
            $.ajax({
                type: "POST",
                cache: false,
                url: 'LoadDesembolsosOnline.ap',
                success: function (json) {
                    $('#cargando-modal').modal('hide');
                    if (json.error != null) {
                        $("#errorLabel").text(json.error);
                        $("#errorModal").modal("show");
                    } else {
                        desembolsos.clearAll();
                        desembolsos.parse(json, "json");
                    }
                }
            });
        });
        var idDesembolso;
        function onselect(id) {
            idDesembolso = id;
            $("#confirm").modal("show");
        }
        function changeState() {
            $.ajax({
                type: "POST",
                cache: false,
                url: 'PagoDesembolso.ap',
                data: {
                    idTransaccion: idDesembolso
                },
                success: function (json) {
                    $('#cargando-modal').modal('hide');
                    if (json.error != null) {
                        $("#errorLabel").text(json.error);
                        $("#errorModal").modal("show");
                    } else {
                        desembolsos.clearAll();
                        desembolsos.parse(json, "json");
                    }
                }
            });
        }


    </script>
</html>
