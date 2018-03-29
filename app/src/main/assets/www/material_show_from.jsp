<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device_width, initial-scale=1">
<title>原料来源信息</title>
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
	<div data-role="main" class="ui-content">
		<form action="">
			<div class="ui-fieldcontain" >
				<label for="material_from_place" >原料产地: </label>
				<input type="text" name="material_from_place" value="江苏省南京市江宁区将军大道139号" readonly="readonly">
				<br/><br/>
			</div>
			<div class="ui-fieldcontain">
				<label for="material_description">原料描述(主要原料): </label>
				<textarea cols="40" rows=8 name="material_description" readonly="readonly">牛乳，俗称牛奶，是最古老的天然饮料之一。顾名思义，牛乳是从雌性乳牛身上所挤出来的。在不同国家，牛乳也分有不同的等级，目前最普遍的是全脂、高钙低脂及脱脂牛乳。美国将牛乳按照脂肪含量分为五类，分别是接近无脂（skim）、半低脂（1/2 percent low fat）、低脂（1 percent low fat）、减脂（2 percent reduced fat）与全脂（whole），而不管哪个国家，只要是标准流程生产，不添加任何添加物的鲜乳喝起来都是非常清淡。目前市面上牛乳的添加物也相当多，如高钙低脂牛乳，就强调其中增添了钙质</textarea>
			</div>
			<br/><br/>
			<div class="ui-fieldcontain">
				<label for="material_subdescription">原料描述(辅料): </label>
				<textarea cols="40" rows="8" name="material_subdescription" readonly="readonly">嗜热链球菌（拉丁学名Streptococcus thermophilus）为革兰氏阳性细菌，同型发酵（homofermentative）兼性厌氧，属于草绿色链球菌。对细胞色素、氧化酶及过氧化氢酶试验阴性，甲型溶血性试验阳性。不运动，无内生孢子。 该菌是一种乳酸菌，在酸奶中可以发现其存在。用于与保加利亚乳杆菌一起发酵生产酸奶。嗜热链球菌可以为保加利亚乳杆菌提供生成嘌呤所需的叶酸与甲酸。</textarea>
			</div>
			<br/><br/>
			<div class="ui-fieldcontain">
				<label for="material_shelflife">产品保质期: </label>
				<input type="text" name="material_shelflife" value="3个月" readonly="readonly">
			</div>
			<br/><br/>
			<div class="ui-fieldcontain">
				<label> 原奶生产图片: </label>
				<img alt="乳制品原料生产" src="images/material_milk.png">
			</div>
			<br/><br/>
			<div class="ui-fieldcontain">
				<label>原料流向: </label>
				<div data-role="collapsible" data-theme="a" data-content-theme="a">
					<h4>江苏省</h4>
					<ul data-role="listview">
						<li>
							<div data-role="collapsible" data-theme="a" data-content-theme="a">
								<h4>南京市卫岗乳业</h4>
								<ul data-role="listview">
									<li> 
										<div class="ui-fieldcontain">
											<label for="material_out1_list1">原料交易数量: </label>
											<textarea name="material_out1_list1" rows="1" cols="1" readonly="readonly">1000 KG</textarea>
										</div>
									</li>
									<li> 
										<div class="ui-fieldcontain">
											<label for="material_out1_list2">交易时间: </label>
											<textarea name="material_out1_list2" rows="1" cols="1" readonly="readonly">2017年12月13日</textarea>
										</div>
									</li>
								</ul>
							</div>
						</li>
						<li>
							<div data-role="collapsible" data-theme="a" data-content-theme="a">
								<h4>徐州市维维乳业</h4>
								<ul data-role="listview">
									<li> 
										<div class="ui-fieldcontain">
											<label for="material_out2_list1">原料交易数量: </label>
											<textarea name="material_out2_list1" rows="1" cols="1" readonly="readonly">1200 KG</textarea>									
										</div>
									</li>
									<li> 
										<div class="ui-fieldcontain">
											<label for="material_out2_list2">交易时间: </label>
											<textarea rows="1" cols="1" name="material_out2_list2" readonly="readonly">2017年12月19日</textarea>										
										</div>
									</li>
								</ul>
							</div>
							 
						</li>
					</ul>
				</div>
				<div data-role="collapsible" data-theme="a" data-content-theme="a">
					<h4>安徽省</h4>
					<ul data-role="listview">
						<li>
							<div data-role="collapsible" data-theme="a" data-content-theme="a">
								<h4>现代牧业</h4>
								<ul data-role="listview">
									<li>
										<div class="ui-fieldcontain">
											<label>原料交易数量: </label>
											<textarea rows="1" cols="1" readonly="readonly">1300 KG</textarea>
										</div>
									</li>
									<li>
										<div class="ui-fieldcontain">
											<label>交易时间: </label>
											<textarea cols="1" rows="1" readonly="readonly">2017年12月22日</textarea>
										</div>
									</li>
								</ul>
							</div>
						</li>
					</ul>		
				</div>
			</div>
			<br/><br/>
			<div data-role="ui-fieldcontain">
				<label>原料企业该批次产品交易负责人: </label>
				<input type="text" value="xxx" readonly="readonly" />
				<label>联系电话: </label>
				<input type="tel" value="1882919921" readonly="readonly">			
			</div>
			<br/>
			<div data-role="ui-fieldcontain">
				<label>乳制品生产企业交易负责人: </label>
				<input type="text" value="xxx" readonly="readonly" />
				<label>联系电话: </label>
				<input type="tel" value="1526618821" readonly="readonly" />			
			</div>
		</form>
	</div>
	
</div>


</body>
</html>





