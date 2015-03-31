<%@ page pageEncoding="UTF-8" %>
<!-- 显示翻页功能-->
<!--
javascript:ORS.showOrder(要跳转到的页数)
count 总页数
page 当前页数
-->
<div class="usercenter_right_fenye">
    <%
        int count = 1;
        if (request.getAttribute("count") != null) {
            count = Integer.parseInt(request.getAttribute("count").toString());
        }
        //当前页数，从0开始
        int npage = 0;
        if (request.getAttribute("page") != null) {
            npage = Integer.parseInt(request.getAttribute("page").toString());
        }
        if (npage != 0) {
    %>
    <a href="javascript:ORS.showOrder(0)">首页</a>
    <a href="javascript:ORS.showOrder(<%=npage - 1%>)">上一页</a>
    <%} else {%>
    <span style="color: #cccccc; cursor:pointer;">上一页</span>
    <% }
        for (int i = 0; i < count; i++) {
            //计算显示页码，只显示当前页数的前后三个数，如果当前页数前面的页数小于三 则后面页数多显示  反之......
            int ePage = npage + 3;
            int sPage = npage - 3;
            if (sPage < 0) {
                ePage += Math.abs(sPage) - 1;
            }
            if (count - ePage < 0) {
                sPage -= Math.abs(count - ePage);
            }
            if (i > sPage && i < ePage) {
                if (i == npage) {
    %>
    &nbsp;&nbsp;<span style="color: #cccccc; cursor:pointer;"><%=i + 1%></span>
    <%} else {%>
    &nbsp;&nbsp;<a href="javascript:ORS.showOrder(<%=i%>)"><%=i + 1%></a>
    <%}
            }
        }%>
    <% if (npage >= count - 1) {%>
    <span style="color: #cccccc; cursor:pointer;">下一页</span>
    <%} else {%>
    <a href="javascript:ORS.showOrder(<%=npage + 1%>)">下一页</a>
    <a href="javascript:ORS.showOrder(<%=count - 1%>)">末页</a>  
    <%}%>
    &nbsp;&nbsp;共 <%=count%> 页 
</div>