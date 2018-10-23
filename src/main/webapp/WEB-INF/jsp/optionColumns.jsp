<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>


<div class="form-group">
<c:forEach var="list" items="${tblClmnLists}">

<c:set var = "salary" scope = "session" value = "${list.Key}"/>
<c:if test = "${salary != 'PRI'}">

    <div class="row" id="option_row_${list.Field}">

        <div class="col-lg-5 col-md-5 col-sm-5">
            <div class="form-group">
                <div class="input-group">
                    <div class="input-group-addon"><span>Excel.</span></div>
                    <!--<input class="form-control" name="ExcelValue" type="text">-->
                    <select class="form-control"  name="ExcelValue">
                        <c:forEach var="excel_List" items="${excelList}">
                            <option>${excel_List}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>
        <div class="col-lg-2 col-md-2 col-sm-2">
            <input type="checkbox" name="my-checkbox" id="my-checkbox${list.Field}" checked class="switch"><span>Use Data From This Feild</span>
            <input type="hidden" name="entryState" value="off" id="entryState${list.Field}">
        </div>
        <div class="col-lg-5 col-md-5 col-sm-5">
            <div class="form-group">
                <div class="input-group">
                    <div class="input-group-addon"><span>XML</span></div>
                    <input class="form-control" name="columnName" value="${list.Field}" readonly type="text">
                </div>
            </div>
        </div>
    </div>

    <script type="text/javascript">
        $('#my-checkbox${list.Field}').change(function(){
           if(this.checked)
               $('#entryState${list.Field}').val('on');
          else
               $('#entryState${list.Field}').val('off');
               var child = document.getElementById("option_row_${list.Field}");
               child.parentNode.removeChild(child);
       });

    </script>

</c:if>

 </c:forEach>
</div>

 <script type="text/javascript">

   $('#my-checkbox').change(function(){
      if(this.checked)
           $('#entryState').val('on');
      else
           $('#entryState').val('off');
   });
 </script>