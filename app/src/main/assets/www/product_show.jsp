<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device_width, initial-scale=1">
<title>产品生产信息</title>
<link rel="stylesheet" href="js/jquery-ui.min.css">
<link rel="stylesheet" href="js/jquery.mobile-1.4.5.min.css">
<script type="text/javascript" src="js/jquery-1.12.4.min.js"></script>
<script src="js/jquery-ui.min.js"></script>
<script src="js/jquery.mobile-1.4.5.min.js"></script>
</head>
<body>
<div data-role="page" id="product_page">
	<div data_role="head">
		<h1>乳制品全过程信息展示</h1>
		<div data-role="navbar">
			<ul>
				<li> <a href="material_show.jsp">原料</a> </li>
				<li> <a href="product_show.jsp">生产</a> </li>
				<li> <a href="transport_show.jsp">运输</a> </li>
				<li> <a href="sale_show.jsp">销售</a> </li>
			</ul>
		</div>
	</div>
	<!-- 具体内容展示 -->
	<div data-role="main" class="ui-content">
		<div data-role="collapsible" data-theme="b" data-content-theme="b">
			<h4>产品生产加工信息</h4>
			<ul data-role="listview">
				<li> <a href="product_material_in.jsp">产品原料进场检验信息</a> </li>
				<li> <a href="product_info.jsp">产品生产加工信息</a> </li>
			</ul>
		</div>
		<div data-role="collapsible" data-theme="b" data-conent-theme="b">
			<h4>产品出场信息</h4>
			<ul data-role="listview">
				<li> <a href="product_check_out.jsp">产品出场检验信息</a> </li>			
			</ul>
		</div>
	</div>
	
</div>


</body>
</html>
