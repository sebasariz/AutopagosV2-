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
                            <li class="active">Autopagos Autored</li>
                        </ol>
                        <h4 class="page-title">Bienvenido !</h4>
                    </div>
                </div>
            </div> 

            <div class="row">
                <div class="col-lg-12">
                    <div class="card-box">
                        <h4 class="text-dark  header-title m-t-0">Facturas autored</h4>
                        <div class="row">
                            <div style="margin-bottom:10px; text-align:right;" class="col-lg-12">
                                <button type="button" onclick="createXLS()" class="btn btn-success waves-effect w-md waves-light m-b-5" style="margin-bottom:10px;-webkit-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);-moz-box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);box-shadow: 0px 2px 5px 0px rgba(97,97,97,0.5);"><i class="fa fa-plus"> </i> XLS</button>
                            </div>
                        </div>
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
        var facturas_autopagos = new dhtmlXGridObject('gridbox');
        facturas_autopagos.attachHeader("#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter");
        facturas_autopagos.setHeader("Referencia,Fecha de registro,Estado,Valor,Convenio,Email,Nombre usuario"); //the headers of columns  
        facturas_autopagos.setColAlign("center,center,center,center,center,center,center"); //the alignment of columns   
        facturas_autopagos.setColTypes("ro,ed,ed,price,ro,ro,ro"); //the types of columns  
        facturas_autopagos.setColSorting("int,str,str,int,str,str,str"); //the sorting types   
        facturas_autopagos.init(); //finishes initialization and renders the grid on the page
        var grid = <%=request.getAttribute("grid")%>;
        facturas_autopagos.clearAll();
        facturas_autopagos.parse(grid, "json");

        function createXLS() {
            //mygridResult.toExcel('XlsGenerator.jsn', 'gray'); 
            facturas_autopagos.toExcel("https://dhtmlxgrid.appspot.com/export/excel", 'gray');
        }


    </script>
</html>
