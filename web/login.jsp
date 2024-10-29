<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width,initial-scale=1">
        <meta name="description" content="Portal de gestión de Autopagos.">
        <meta name="author" content="Coderthemes"> 
        <link rel="shortcut icon" href="assets/images/auto.ico"> 
        <title><bean:message key="autopagos.title"/></title> 
        <link href="assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <link href="assets/css/core.css" rel="stylesheet" type="text/css">
        <link href="assets/css/icons.css" rel="stylesheet" type="text/css">
        <link href="assets/css/components.css" rel="stylesheet" type="text/css">
        <link href="assets/css/pages.css" rel="stylesheet" type="text/css">
        <link href="assets/css/menu.css" rel="stylesheet" type="text/css">
        <link href="assets/css/responsive.css" rel="stylesheet" type="text/css"> 
        <script src="assets/js/modernizr.min.js"></script> 
        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
        <![endif]--> 
    </head>
    <body>  
        <div class="wrapper-page"> 
            <div class="text-center">
                <img src="assets/images/logo-login.png">
            </div>

            <form role="form" action="Login.ap" name="Usuario" method="post" >
                <div class="form-group">
                    <div class="col-xs-12">
                        <label style="color: red;"><html:errors property="register" /></label>
                    </div>
                </div> 
                <div class="form-group">
                    <div class="col-xs-12">
                        <input class="form-control" type="text" required="" name="email" placeholder="<bean:message key="autopagos.usuario"/>">
                        <i class="fa fa-user form-control-feedback l-h-34"></i>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-xs-12">
                        <input class="form-control" type="password" required="" name="pass" placeholder="<bean:message key="autopagos.clave"/>">
                        <i class="fa fa-key form-control-feedback l-h-34"></i>
                    </div>
                </div> 
                <div class="form-group text-right m-t-40" >
                    <div class="col-xs-12" style="margin-top: 10px;">
                        <button class="btn btn-primary btn-custom w-md waves-effect waves-light" type="submit"> <bean:message key="autopagos.login"/>
                        </button>
                    </div>
                </div>

                        <div class="form-group m-t-30" style="padding-top: 90px;">
                    <div class="col-sm-12">
                        <a href="OlvidoClave.ap" class="text-muted"><i class="fa fa-lock m-r-5"></i> <bean:message key="autopagos.olvido.contrasena"/></a>
                    </div>

                </div>
            </form>
        </div> 

        <!-- Main  -->
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

        <!-- Custom main Js -->
        <script src="assets/js/jquery.core.js"></script>
        <script src="assets/js/jquery.app.js"></script>

    </body>
</html>