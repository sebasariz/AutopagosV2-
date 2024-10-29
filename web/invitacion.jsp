<%-- 
    Document   : invitacion
    Created on : 20-ene-2017, 15:43:32
    Author     : root
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width,initial-scale=1">
        <meta name="description" content="Portal de gestión de Autopagos.">
        <meta name="author" content="Autopagos">

        <link rel="shortcut icon" href="img/images/auto.ico">

        <title>Invitación Autopagos APP | La forma más efectiva de valorar tu tiempo pagando tus facturas por internet.</title>

        <link href="assets/plugins/switchery/switchery.min.css" rel="stylesheet" />
        <link href="assets/plugins/jquery-circliful/css/jquery.circliful.css" rel="stylesheet" type="text/css" />

        <link href="assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <link href="assets/css/core.css" rel="stylesheet" type="text/css">
        <link href="assets/css/icons.css" rel="stylesheet" type="text/css">
        <link href="assets/css/components.css" rel="stylesheet" type="text/css">
        <link href="assets/css/pages.css" rel="stylesheet" type="text/css">

        <link href="assets/css/responsive.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="assets/fonts/css/material-design-iconic-font.css">

 

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
                        <div class="col-md-6 col-md-offset-3" style="text-align:center;">
                            <div class="card-box">
                                <img style="width:150px;" src="img/template/logo-banner-principal-2.png">
                                <h3 class="m-t-0" style=" margin-top:15px;">Bienvenido a Autopagos</h3>
                                <p class="text-mu m-b-30" style="font-size: 10px; color:#cccccc;">
                                    <i>¡La forma más efectiva de valorar tu tiempo pagando tus facturas por internet.!</i>
                                </p>
                                <p class="text-mu m-b-30" style="font-size: 12px; color:#2196F3;">
                                    Tu amigo <b><bean:write name="nombre"/></b>  te ha invitado a formar parte de Autopagos. Agrupa tus facturas y acumula recompensas que podrás usar como saldo en tus pagos invitando a tus amigos.
                                </p>

                                <div class="row">
                                    <div class="col-md-10 col-md-offset-1" style="background: url(img/template/mujer-iphone-pagos-agrupados.jpg); background-size: cover; padding:10px; background-position: right;">
                                        <div class="col-md-8 col-md-offset-4" style="margin-top: 45px;">
                                            <p style="color:#ffffff;font-size:20px;"><strong>TODAS TUS FACTURAS, UN SOLO PAGO.</strong></p>
                                            <p style="color:#dddddd;font-size:15px; text-align:justify;">Desde tu celular, ahora podrás agrupar y realizar el pago de TODAS tus facturas en un solo proceso desde cualquier cuenta bancaria.</p>
                                        </div>
                                        <div class="col-md-8 col-md-offset-4" style="background: rgba(0, 0, 0, 0.5);padding: 10px;border-radius: 5px; margin-top: 10px;">
                                            <p style="color:#ff9800;font-size:15px;">CODIGO DE INVITACIÓN:</p>
                                            <p style="font-size:25px; text-align:center; color:#AEEA00;"><bean:write name="codigo"/></p>
                                        </div>
                                        <div class="col-md-8 col-md-offset-4" style="margin-top: 10px;">
                                            <div class="col-sm-6"> <span style="color:#ffffff; font-size: 9px;">Descargala Ahora en:</span> <a href="https://play.google.com/store/apps/details?id=com.iammagis.autopagos&hl=es"><img src="img/template/logo-play-store.png" style="width:70%;"></a>
                                            </div>
                                            <div class="col-sm-6"> <span style="color:#ffffff; font-size: 9px;">Descargala Ahora en:</span> <a href="https://itunes.apple.com/co/app/autopagos/id1235055319?mt=8"><img src="img/template/app-store.png" style="width:70%;"></a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-10 col-md-offset-1" style="margin-top: 10px;">
                                        <img src="img/template/convenios-banner.png" width="100%">
                                    </div>
                                </div> 
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-md-offset-3"> 
                        <div class="card-box" style="min-height: 170px;">
                            <div class="row"> 
                                <div class="col-md-12" style="text-align: center;">
                                    <h4 style="color:#2196F3;">Siempre en contacto:</h4>
                                </div> 

                                <div class="col-xs-12 col-sm-12" style="text-align: center; margin-top: 15px;">
                                    <div class="col-xs-6 col-sm-6">
                                        <a href="https://www.instagram.com/autopagos/" ><img style="width:50px; " src="img/template/intagram-logo.png"></a>
                                        <p style="color: #d4145a; font-size: 12px;">@autopagos</p>
                                    </div>
                                    <div class="col-xs-6 col-sm-6">
                                        <a href="https://www.facebook.com/autopagos/"><img style="width:50px;" src="img/template/fb-logo.png"></a>
                                        <p style="color: #3b5998; font-size: 12px;">@autopagos</p>
                                    </div>
                                    <div class="col-xs-6 col-sm-6">
                                        <a href="https://twitter.com/autopagos"><img style="width:50px;" src="img/template/twt-logo.png"></a>
                                        <p style="color: #0084b4; font-size: 12px;">@autopagos</p>
                                    </div>
                                    <div class="col-xs-6 col-sm-6">
                                        <a href="https://www.youtube.com/channel/UCWxD9PjsEG2SPbSad-Zuw6w"><img style="width:50px;" src="img/template/youtube-logo.png"></a>
                                        <p style="color: #e52d27; font-size: 12px;">Autopagos</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>



                    <div style="width:350px; margin:0 auto;">
                        <div style="text-align:center; ">
                            <img style="width:170px;" src="img/template/autopagos.png">
                            <p style="font-size:8px; color:#666666;">AUTOPAGOS.CO SAS / Dir: Cll 32E #80a-57 Int 201 / Medellín - Colombia</p>
                        </div>
                    </div>    



                </div>
                <!-- end container -->
            </div>
            <!-- end content --> 
        </div> 

    </body>
</html>