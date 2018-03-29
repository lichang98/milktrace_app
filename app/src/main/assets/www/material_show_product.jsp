<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device_width, initial-scale=1">
<title>原料生产加工信息</title>
<link rel="stylesheet" href="js/jquery-ui.min.css">
<link rel="stylesheet" href="js/jquery.mobile-1.4.5.min.css">
<script type="text/javascript" src="js/jquery-1.12.4.min.js"></script>
<script src="js/jquery-ui.min.js"></script>
<script src="js/jquery.mobile-1.4.5.min.js"></script>
</head>
<body>
<div data-role="page" id="sale_page">
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
	<!-- 具体信息展示 -->
	<div data-role="collapsible" data-theme="a" data-content-theme="a">
		<h4>钙添加剂的生产</h4>
		<div class="ui-fieldcontain">
			<label>方解石原材料: </label>
			<img alt="方解石矿石" src="images/camine.png" width="60%">		
		</div>
		<br/><br/>
		<div class="ui-fieldcontain">
			<label>方解石化验: </label>
			<img alt="方解石化验室" src="images/cachemistry.png" width="60%">		
		</div>
		<br/><br/>
		<div class="ui-fieldcontain">
			<label>方解石化验检验</label>
			<img alt="方解石化验检验" src="images/cacheck.png" width="60%">
		</div>
		<br/><br/>
		<div class="ui-fieldcontain">
			<label>方解石生产车间</label>
			<img alt="方解石生产车间" src="images/caproduct.png" width="60%">
		</div>
	</div>
	
</div>


</body>
</html>

<!-- 

    <div data-role="collapsible" data-theme="b" data-content-theme="b">
    <h4>A</h4>
    <ul data-role="listview">
      <li><a href="#">Adele</a></li>
      <li><a href="#">Agnes</a></li>
    </ul>
  </div>
  
  <div data-role="main" class="ui-content">
  <form method="post" action="demoform.asp">
  <div class="ui-fieldcontain">
    <label for="name">全名:</label>
    <input type="text" name="text" id="name" placeholder="你的名字.." data-theme="b">
    <br><br>
  
    <label for="search">您需要搜索什么？</label>
    <input type="search" name="search" id="search" placeholder="搜索内容..">
    <br><br>
 
 
 -->