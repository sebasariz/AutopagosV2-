<%-- 
    Document   : esquema_autored
    Created on : 12-ago-2016, 13:53:50
    Author     : Usuario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <script src="assets/css/bootstrap-treeview.css"></script>  
    <script src="assets/js/bootstrap-treeview.js"></script>
    <div class="container"> 
        <!-- Page-Title -->
        <div class="row">
            <div class="col-sm-12">
                <div class="page-title-box">
                    <ol class="breadcrumb pull-right">
                        <li><a href="#">Autopagos</a></li>
                        <li class="active">Esquema Autored</li>
                    </ol>
                    <h4 class="page-title">Bienvenido !</h4>
                </div>
            </div>
        </div> 

        <div class="row">
            <div class="col-lg-12">
                <div class="input-group card-box">
                    <input type="text" id="text" name="example-input1-group2" class="form-control" placeholder="Buscar Miembro Autored">
                    <span class="input-group-btn">
                        <button type="button" class="btn waves-effect waves-light btn-primary" onclick="search()">Buscar usuario</button>
                    </span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="card-box">
                    <h4 class="text-dark  header-title m-t-0">ARBOL AUTORED</h4>
                    <p class="text-muted m-b-25 font-13"> 
                    <div id="tree"></div>

                </div>
            </div>
            <!-- end col -8 -->
        </div>  
    </div>
    <script type="text/javascript">
 
        $.ajax({
            type: "POST",
            cache: false,
            url: 'SearchEsquemaAutored.ap', 
            success: function (json) {
                $('#cargando-modal').modal('hide');
                if (json.error != null) {
                    $("#errorLabel").text(json.error);
                    $("#errorModal").modal("show");
                } else {  
                    $('#tree').treeview({data: json.data});
                }
            }
        });
        
        function search(){
            $.ajax({
            type: "POST",
            cache: false,
            url: 'SearchEsquemaTextAutored.ap', 
            data:{text:$("#text").val()},
            success: function (json) {
                $('#cargando-modal').modal('hide');
                if (json.error != null) {
                    $("#errorLabel").text(json.error);
                    $("#errorModal").modal("show");
                } else {  
                    $('#tree').treeview({data: json.data});
                }
            }
        });
        }
    </script>
</html>
