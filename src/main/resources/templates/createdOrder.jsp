<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/4/18
  Time: 10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>微信支付页面</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/qrcode/excanvas.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/qrcode/qrcode.js"></script>
    <script>
        $.post("${pageContext.request.contextPath}/wechat/createOrder.do",{point:1,amount:1},function (d) {
            if(d.success){
                $('#pay_erweima').html("").qrcode({
                    render: 'canvas',
                    text: utf16to8(d.code_url),
                    height: 125,
                    width: 125,
                    //logo图片地址
                    src: '${pageContext.request.contextPath}/resources/logo/pc/erweima_logo.png'
                });
            }else {
                alert(d.msg);
            }
        })

    </script>
</head>
<body>
<div id="pay_erweima"></div>
</body>
</html>
