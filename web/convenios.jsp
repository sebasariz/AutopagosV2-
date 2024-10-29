<%-- 
    Document   : conveinos
    Created on : 10-ago-2016, 11:48:35
    Author     : Usuario
--%>

<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width,initial-scale=1">
        <meta name="description" content="Portal de gestión de Autopagos.">
        <meta name="author" content="Autopagos">

        <link rel="shortcut icon" href="assets/images/auto.ico">

        <title>Portal Autopagos - Sistema integrado de pagos y administarción de recaudos</title>

        <link href="assets/plugins/switchery/switchery.min.css" rel="stylesheet" />
        <link href="assets/plugins/jquery-circliful/css/jquery.circliful.css" rel="stylesheet" type="text/css" />

        <link href="assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <link href="assets/css/core.css" rel="stylesheet" type="text/css">
        <link href="assets/css/icons.css" rel="stylesheet" type="text/css">
        <link href="assets/css/components.css" rel="stylesheet" type="text/css">
        <link href="assets/css/pages.css" rel="stylesheet" type="text/css">
        <link href="assets/css/menu.css" rel="stylesheet" type="text/css">
        <link href="assets/css/responsive.css" rel="stylesheet" type="text/css">
        <link href="assets/css/timepicker.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="assets/fonts/css/material-design-iconic-font.css">



        <script src="assets/js/modernizr.min.js"></script>


        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
        <![endif]-->

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
        <script src="assets/pages/jquery.flot.init.js"></script>
        <!--Date Picker-->
        <script src="assets/plugins/moment/moment.js"></script>
        <script src="assets/plugins/timepicker/bootstrap-timepicker.min.js"></script>
        <script src="assets/plugins/mjolnic-bootstrap-colorpicker/dist/js/bootstrap-colorpicker.min.js"></script>
        <script src="assets/plugins/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
        <script src="assets/plugins/bootstrap-daterangepicker/daterangepicker.js"></script>

        <!--DHTMLX-->
        <link rel="stylesheet" type="text/css" href="codebase/dhtmlxgrid.css">
        <script type="text/javascript" src="codebase/dhtmlxgrid.js"></script>


    </head> 
    <body class="fixed-left">

        <!-- Begin page -->
        <div id="wrapper">

            <!-- Top Bar Start -->
            <div class="topbar">

                <!-- LOGO -->
                <div class="topbar-left">
                    <div class="text-center">
                        <a href="javascript:void(0)" onclick="window.location = 'LoadInicio.ap';" class="logo"><img src="assets/images/small/logo-icon.png"> <span>autopagos</span> </a>
                    </div>
                </div>

                <!-- Navbar -->
                <div class="navbar navbar-default" role="navigation">
                    <div class="container">
                        <div class="">
                            <div class="pull-left">
                                <button class="button-menu-mobile open-left waves-effect">

                                </button>
                                <span class="clearfix"></span>
                            </div>


                        </div>
                        <!--/.nav-collapse -->
                    </div>
                </div>
            </div>

            <div class="container"> 
                <!-- Page-Title -->
                <div class="row">
                    <div class="col-sm-12">
                        <div class="page-title-box">
                            <ol class="breadcrumb pull-right"> 
                            </ol>
                            <h4 class="page-title">Bienvenido !</h4>
                        </div>
                    </div>
                </div>  

                <div class="row">
                    <div class="col-lg-12">
                        <div class="card-box">
                            <h1 class="text-dark  header-title m-t-0" style="padding-top: 30px; padding-bottom: 15px; text-align: center;"><span style="color:#2196f3;">Listado de convenios de pago.</span></h1>
                            <div style="margin-bottom:10px; text-align:right;">
                                <div id="grid" style="height:650px;"></div> 
                            </div>
                        </div>
                    </div>    
                    <!-- end container -->
                </div>
            </div>
            <script>
                var gridConvenios = new dhtmlXGridObject('grid');
                gridConvenios.setImagePath("codebase/imgs/");
                gridConvenios.setHeader("Logo,Nombre convenio,Texto guía");
                gridConvenios.attachHeader(",#text_filter,#text_filter");
                gridConvenios.setColAlign("center,center,center");
                gridConvenios.setColTypes("img,ro,ro");
                gridConvenios.setColSorting("str,str,str"); 
                gridConvenios.init();
                var grid = <%=request.getAttribute("convenios")%>;
                gridConvenios.clearAll();
                gridConvenios.parse(grid, "json");
            </script>
    </body>
</html>
