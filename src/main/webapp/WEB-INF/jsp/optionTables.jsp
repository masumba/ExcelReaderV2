<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<div class="form-group">
<select class="form-control" name="clmnName" id="tbllist">
    <option value="">Select A Table</option>
<c:forEach var="list" items="${dbTblLists}">
    <option value="${dbTblListsName}.${list.TABLE_NAME}">${list.TABLE_NAME}</option>
 </c:forEach>
 </select>
 </div>

  <div id="show_products"></div>



 <script>

  $(document).ready(function(){


       $('#tbllist').change(function(){
            var clmnName = $(this).val();
            $.ajax({
                 url:"http://localhost:8080/columns",
                 method:"POST",
                 data:{clmnName:clmnName},
                 success:function(data){
                      $('#show_products').html(data);
                 }
            });
       });


  });

 </script>