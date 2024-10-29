<%-- 
    Document   : recaudo
    Created on : 24/02/2015, 09:29:01 PM
    Author     : sebasariz
--%> 
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width,initial-scale=1">
        <meta name="description" content="Portal de gestión de Autopagos.">
        <meta name="author" content="Autopagos">

        <link rel="shortcut icon" href="assets/images/auto.ico">

        <title>Procesamiento de pago</title>

        <link href="assets/plugins/switchery/switchery.min.css" rel="stylesheet" />
        <link href="assets/plugins/jquery-circliful/css/jquery.circliful.css" rel="stylesheet" type="text/css" />

        <link href="assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <link href="assets/css/core.css" rel="stylesheet" type="text/css">
        <link href="assets/css/icons.css" rel="stylesheet" type="text/css">
        <link href="assets/css/components.css" rel="stylesheet" type="text/css">
        <link href="assets/css/pages.css" rel="stylesheet" type="text/css">

        <link href="assets/css/responsive.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="assets/fonts/css/material-design-iconic-font.css">


        <!--DHTMLX-->
        <link rel="stylesheet" type="text/css" href="dhtmlx/codebase/dhtmlxgrid.css">
        <script type="text/javascript" src="dhtmlx/codebase/dhtmlxgrid.js"></script>

        <script src="assets/js/modernizr.min.js"></script>


        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
        <![endif]-->


    </head>


    <body class="fixed-left">


        <!-- Left Sidebar End --> 



        <!-- ============================================================== -->
        <!-- Start right Content here -->
        <!-- ============================================================== -->                      
        <div class="content-page">
            <!-- Start content -->
            <div class="content">
                <div class="container">

                    <div class="row">
                        <div style=" width:350px; margin:50px auto; text-align:center;">
                            <div class="card-box">
                                <img style="width:80px;" src="img/logoConvenios/<bean:write name="convenio" property="logo"/>">
                                <h4 class="m-t-0 header-title" style="color:#2196F3; margin-top:15px;"><b><bean:write name="convenio" property="nombre"/></b></h4>
                                <p class="text-muted m-b-30 font-13">
                                    Por favor verifica los datos de la transacción y selecciona tu banco.
                                </p>

                                <div class="row">
                                    <div class="col-md-12">
                                        <form role="form" action="Pago.ap" method="POST" name="Factura" onsubmit="return validarBanco()">
                                            <div class="form-group">
                                                <div class="input-group">
                                                    <div id="error" style="color: red;"></div>
                                                </div>
                                                <label class="control-label"  style="font-size:13px; font-weight:100; margin:10px 0;">Referencia:</label>
                                                <div class="input-group">
                                                    <span class="input-group-addon"><i class="fa fa-barcode"></i></span>
                                                    <input type="text" name="nombre" readonly class="form-control" value="<bean:write name="referencia"/>" placeholder="Referencia">
                                                    <input type="hidden" name="email" value="<bean:write name="email"/>">
                                                    <input type="hidden" name="idfactura" value="<bean:write name="idfactura"/>"> 
                                                </div>
                                                <label class="control-label"  style="font-size:13px; font-weight:100; margin:10px 0;">Valor Factura:</label>
                                                <div class="input-group">
                                                    <span class="input-group-addon"><i class="fa fa-money"></i></span>
                                                    <input type="text"  class="form-control" readonly name="valorPagado" onchange="sum()" id="valor" placeholder="Valor" value="<bean:write name="valor"/>">
                                                </div>
                                                <label class="control-label" style="font-size:13px; font-weight:100; margin:10px 0;">Costo Transacción:</label>
                                                <div class="input-group">
                                                    <span class="input-group-addon"><i class="fa fa-money"></i></span>
                                                    <input type="text"  class="form-control" readonly name="comision" id="comision" placeholder="Costo transaccion" value="<bean:write name="comision"/>">

                                                </div>
                                                <label class="control-label"  style="font-size:13px; font-weight:100; margin:10px 0;">Total a pagar:</label>
                                                <div class="input-group">
                                                    <span class="input-group-addon"><i class="fa fa-money"></i></span>
                                                    <input type="text"  class="form-control" readonly name="total" id="total" placeholder="Totel" value="<bean:write name="total"/>">
                                                </div>
                                                <div class="form-group">
                                                    <label class="control-label"  style="font-size:13px; font-weight:100; margin:10px 0;">Selecciona un banco:</label>
                                                    <select name="idBanco" id="idBanco" class="form-control" style="width: 100%;">
                                                        <option value="0">Seleccione un banco</option>
                                                        <logic:iterate id="banco" name="bancos">
                                                            <option value="<bean:write name="banco" property="idBanco"/>"><bean:write name="banco" property="nombre"/></option>
                                                        </logic:iterate>
                                                    </select>
                                                </div>

                                            </div> <!-- form-group -->
                                            <div class="col-lg-12">
                                                <button class="btn btn-warning btn-custom w-md waves-effect waves-light" type="submit">Realizar pago</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="card-box" style="width:350px; margin:0 auto;">
                            <div style="text-align:center; ">
                                <img style="width:170px;" src="img/template/autopagos.png">
                                <p style="font-size:8px; color:#666666;">AUTOPAGOS.CO SAS / Dir: Cll 32E #80a-57 Int 201 / Medellín - Colombia</p>
                            </div>
                        </div>    
                    </div>


                </div>
                <!-- end container -->
            </div>
            <!-- end content -->



        </div>
        <!-- ============================================================== -->
        <!-- End Right content here -->
        <!-- ============================================================== -->



        <script>
            var resizefunc = [];
        </script>


        <!-- Plugins  -->
        <script src="assets/js/jquery.min.js"></script>
        <script src="assets/js/bootstrap.min.js"></script>
        <script src="assets/js/detect.js"></script>
        <script src="assets/js/fastclick.js"></script>
        <script src="assets/js/jquery.slimscroll.js"></script>
        <script src="assets/js/jquery.blockUI.js"></script>
        <script src="assets/js/waves.js"></script>
        <script src="assets/js/wow.min.js"></script>
        <script src="assets/js/jquery.nicescroll.js"></script>
        <script src="assets/js/jquery.scrollTo.min.js"></script>
        <script src="assets/plugins/switchery/switchery.min.js"></script>

        <!-- Counter Up  -->
        <script src="assets/plugins/waypoints/lib/jquery.waypoints.js"></script>
        <script src="assets/plugins/counterup/jquery.counterup.min.js"></script>

        <!-- circliful Chart -->
        <script src="assets/plugins/jquery-circliful/js/jquery.circliful.min.js"></script>
        <script src="assets/plugins/jquery-sparkline/jquery.sparkline.min.js"></script>

        <!-- skycons -->
        <script src="assets/plugins/skyicons/skycons.min.js" type="text/javascript"></script>

        <!-- Page js  -->
        <script src="assets/pages/jquery.dashboard.js"></script>

        <!--Charts-->
        <script src="assets/plugins/flot-chart/jquery.flot.js"></script>
        <script src="assets/plugins/flot-chart/jquery.flot.time.js"></script>
        <script src="assets/plugins/flot-chart/jquery.flot.tooltip.min.js"></script>
        <script src="assets/plugins/flot-chart/jquery.flot.resize.js"></script>
        <script src="assets/plugins/flot-chart/jquery.flot.pie.js"></script>
        <script src="assets/plugins/flot-chart/jquery.flot.selection.js"></script>
        <script src="assets/plugins/flot-chart/jquery.flot.stack.js"></script>
        <script src="assets/plugins/flot-chart/jquery.flot.crosshair.js"></script>
        <script src="assets/pages/jquery.flot.init.js"></script>

        <!--Date Picker-->
        <script src="assets/plugins/moment/moment.js"></script>
        <script src="assets/plugins/timepicker/bootstrap-timepicker.min.js"></script>
        <script src="assets/plugins/mjolnic-bootstrap-colorpicker/dist/js/bootstrap-colorpicker.min.js"></script>
        <script src="assets/plugins/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
        <script src="assets/plugins/bootstrap-daterangepicker/daterangepicker.js"></script>

        <!-- Custom main Js -->
        <script src="assets/js/jquery.core.js"></script>
        <script src="assets/js/jquery.app.js"></script>

        <!--Morris Chart-->
        <script src="assets/plugins/morris/morris.min.js"></script>
        <script src="assets/plugins/raphael/raphael-min.js"></script>
        <script src="assets/pages/morris.init.js"></script>



        <script type="text/javascript">

            function validarBanco() { 
                if (Number(<bean:write name="valor"/>) > Number($("#valor").val())) {
                    $("#error").empty().html("El valor no puede ser inferior a: <bean:write name="valor"/>");
                    return false;
                }
                if ($("#idBanco").val() == 0) {
                    $("#error").empty().html("Seleccione un banco");
                    return false;
                }

                return true;

            }

            function sum() {
                var valor = $("#valor").val();
                var comision = Number(<bean:write name="comfija"/>) + Number(<bean:write name="comvar"/> * valor);
                $("#comision").val(comision);
                var total = Number(valor) + Number(comision);
                $("#total").val(total);
            }


            if (<bean:write name="variable"/>) {
                $("#valor").removeAttr("readonly");
            }

        </script>

    </body>
</html>



