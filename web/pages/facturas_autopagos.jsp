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
                        <h4 class="text-dark  header-title m-t-0">Facturas autopagos</h4>
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
        facturas_autopagos.setHeader("Numero,Fecha - Hora,Convenio,Valor,Iva,Total,Estado,Detalle,Pagar"); //the headers of columns  
        facturas_autopagos.setColAlign("center,center,center,center,center,center,center,center,center"); //the alignment of columns   
        facturas_autopagos.setColTypes("ro,ed,ed,price,price,price,ro,link,link"); //the types of columns  
        facturas_autopagos.setColSorting("int,str,str,int,str,str,str,str,str"); //the sorting types   
        facturas_autopagos.init(); //finishes initialization and renders the grid on the page
        var grid = <%=request.getAttribute("grid")%>;
        facturas_autopagos.clearAll();
        facturas_autopagos.parse(grid, "json");

        


    </script>
</html>
