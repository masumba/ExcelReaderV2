<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MPS</title>
    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/jquery.bootgrid.css">
    <link rel="stylesheet" href="assets/css/styles.css">
    <link href="assets/css/bootstrap-switch.css" rel="stylesheet">

</head>

<body>


<!---->
<hr/>

<div class="container">${msg}</div>

<div class="container" id="submission-form">

<c:choose>
    <c:when test="${not empty excelList}">
<!--*-->
            <form method="POST" action="/save">
                <div class="form-group">
                    <select class="form-control" name="tblName" id="dblist">
                        <option value="">Select A Database</option>
                    </select>
                </div>

                    <div id="show_product"></div>


                <div class="form-group">
                    <button type="Submit" class="btn btn-default btn-block" type="button">Save</button>
                    <button type="Reset" class="btn btn-default btn-block" type="button">Reset</button>
                </div>
            </form>
<!--*-->
    </c:when>
    <c:otherwise>
<!--**-->

<form action="/saveFile" method="POST" enctype="multipart/form-data">
        <div class="form-group"><input name="excelFile" type="file" class="form-control" accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel" /></div>
        <!---->
        <c:if test="${not empty excelList}">

        <div class="form-group">
        <select class="form-control">
            <c:forEach var="list" items="${excelList}">
                <option>${list}</option>
            </c:forEach>
        </select>
        </div>

        </c:if>
        <!---->
        <div class="form-group"><button class="btn btn-default btn-block" type="submit">Submit</button></div>
    </form>

<!--**-->
    </c:otherwise>
</c:choose>
</div>




<!--<form action="/save" method="POST">
    <div class="form-group"><button class="btn btn-default btn-block" type="submit">Submit</button></div>
</form>-->


<!---->

    <script src="assets/js/jquery.min.js"></script>
    <script src="assets/bootstrap/js/bootstrap.min.js"></script>
    <script src="assets/js/jquery.bootgrid.js"></script>
    <script src="assets/js/Bootgridlauncher.js"></script>
    <script src="assets/js/jquery.2.2.0.min.js"></script>
    <script src="assets/js/bootstrap-switch.js"></script>

<c:choose>
    <c:when test="${not empty excelList}">

<!--#-->
<script>
 $(document).ready(function(){
      $('#dblist').change(function(){
           var tblName = $(this).val();
           $.ajax({
                url:"http://localhost:8080/tables",
                method:"POST",
                data:{tblName:tblName},
                success:function(data){
                     $('#show_product').html(data);
                }
           });
      });

 });

/**/
function load_unseen_notification(dblist = ''){
  $.ajax({
    url:"http://localhost:8080/databases",
    method:"POST",
    data:{dblist:dblist},
    success:function(data){
      $('#dblist').html(data);
    }
  })
}

load_unseen_notification();


setInterval(function(){
  load_unseen_notification();
}, 300000);

      /**/

 </script>
<!--#-->

    </c:when>
    <c:otherwise>

<!--#-->
<!--#-->

    </c:otherwise>
</c:choose>

</body>
</html>