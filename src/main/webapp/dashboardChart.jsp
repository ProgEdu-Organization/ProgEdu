<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="utf-8"%>
<%@ page
	import="fcu.selab.progedu.conn.Language"%>
<%@ page
	import="fcu.selab.progedu.db.UserDbManager, fcu.selab.progedu.db.ProjectDbManager"%>
<%@ page
	import="fcu.selab.progedu.data.User, fcu.selab.progedu.data.Project"%>
<%@ page import="java.util.*"%>
<%@ page
	import="org.json.JSONArray, org.json.JSONException, org.json.JSONObject"%>

<%
	if (session.getAttribute("username") == null || session.getAttribute("username").toString().equals("")) {
		response.sendRedirect("index.jsp");
	}
	session.putValue("page", "dashboard");
%>

<%@ include file="language.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<style type="text/css">
		body, html{
			height: 100%;
			overflow-x: hidden;
		}
		
		.sidebar {
			height: 100%;
			background-color: #444;
			color: white; 
			margin: -1px;
			position: fixed; /* Set the navbar to fixed position */
   			top: 0;
   			margin-top: 50px;
   		 	overflow-y: scroll;
   		 	z-index: 100;
		}
		.sidebar a{
			color: white;
		}
		.sidebar a:hover{
			color: orange;
		}
		.sidebar button{
			color: white;
			background: none;
		}
		
		#main {
			height: 100%;
			margin-left: 200px;
			overflow-x: scroll;
			padding-top: 20px;
			width: auto;
		}
		
		#inline {
		    margin: 20px;
		}
		
		#inline p {
		    display: inline;
		}
		.ovol {
			border-radius: 5px;
			height: 50px;
            font-weight: bold;
            width: 120px;
            color: white;
            text-align: center;
		}
		.circle {
			border-radius: 30px;
			height: 30px;
            font-weight: bold;
            width: 30px;
            color: white;
            text-align: center;
		}
		.red {
			background: #e52424;
		}
		.blue {
			background: #5fa7e8;
		}
		.gray {
			background: #878787;
		}
		.orange {
			background: gold;
		}
		.green {
			background: #32CD32;
		}
		.gold{
			background: #FFD700;
		}
		.circle a {
			color: #fff;
			line-height: 30px;
		}
		.highcharts-container {
			text-align: center;
		}
		.axis path, .axis line {
		  fill: none;
		  stroke: #000;
		  shape-rendering: crispEdges;
		}
		
		.dot {
		  stroke: #000;
		}
		</style>

<link rel="shortcut icon" href="img/favicon.ico" />
<link rel="bookmark" href="img/favicon.ico" />
<script src="//d3js.org/d3.v3.min.js"></script>
<title>ProgEdu</title>
</head>
<body>
	<%
		UserDbManager db = UserDbManager.getInstance();
		ProjectDbManager Pdb = ProjectDbManager.getInstance();

		// db users
		List<User> users = db.listAllUsers();

		// db projects
		List<Project> dbProjects = Pdb.listAllProjects();
		List<String> pNames = Pdb.listAllProjectNames();

	%>
	<%@ include file="header.jsp"%>
	<!-- -----sidebar----- -->
	<div class="sidebar" style="width:200px">
		<ul class="nav flex-column" style="padding-top: 20px;">
			<li class="nav-item">
				<font size="4">
					<a href="javascript:;" data-toggle="collapse" data-target="#overview" class="nav-link">
						<i class="fa fa-bars" aria-hidden="true"></i>&nbsp;
							<fmt:message key="dashboard_a_overview" />
							<i class="fa fa-chevron-down" aria-hidden="true"></i>
					</a>
				</font>
				<ul id="overview" class="collapse" style="list-style: none;">
					<li class="nav-item"><font size="3">
						<a class="nav-link" href="#Student Projects">
							<i class="fa fa-table" aria-hidden="true"></i>&nbsp; 
							<fmt:message key="dashboard_li_studentProjects" />
						</a>
					</font></li>
					<li class="nav-item"><font size="3">
							<button type="button" class="btn btn-default" data-toggle="modal" data-target="#exampleModal">
								<i class="fa fa-bar-chart" aria-hidden="true"></i>&nbsp;
								<fmt:message key="dashboard_li_chart" />
							</button>
					</font></li>
				</ul></li>
			<li class="nav-item">
				<font size="4">
					<a href="javascript:;" data-toggle="collapse" data-target="#student" class="nav-link">
						<i class="fa fa-bars" aria-hidden="true"></i>&nbsp;
							<fmt:message key="dashboard_a_student" />
							<i class="fa fa-chevron-down" aria-hidden="true">
						</i>
					</a>
				</font>
				<ul id="student" class="collapse" style="list-style: none;">
					<%
						for (User user : users) {
							String userName = user.getUserName();
							String name = user.getName();
							String href = "\"dashStuChoosed.jsp?studentId=" + user.getGitLabId() + "\"";
					%>
					<li class="nav-item">
						<font size="3">
							<a class="nav-link" href=<%=href%>>
								<i class="fa fa-angle-right" aria-hidden="true"></i>&nbsp; <%=userName%>&nbsp; <%=name%>
							</a>
						</font>
					</li>
					<%
						}
					%>
				</ul></li>
		</ul>
		</div>
	<!-- -----sidebar----- -->
	<div class="container-fluid" id="main">
		<h1 style="margin-top: 30px; margin-bottom: 20px;">
			<fmt:message key="dashboard_a_overview" />
		</h1>
		<!-- ---------------------------- Student Project ------------------------------- -->
		<div class="card" style="width: max-content;">
			<h4 id="Student Projects" class="card-header">
				<i class="fa fa-table" aria-hidden="true"></i>&nbsp;
				<fmt:message key="dashboard_li_chart" />
			</h4>
			<div class="card-block">
				<ul class="nav nav-tabs" role="tablist">
					<li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#chart1" role="tab">Compile Result</a></li>
					<li class="nav-item"><a class="nav-link" data-toggle="tab" href="#chart2" role="tab">Commit Count and Build Result</a></li>
					<li class="nav-item"><a class="nav-link" data-toggle="tab" href="#chart3" role="tab">Commit Record</a></li>
				</ul>
				<!-- Tab panes -->
				<div class="tab-content text-center" style="margin-top: 10px">
					<div class="tab-pane active col-md-12" id="chart1" role="tabpanel">
						<div class="col-md-12" id="chart1Demo"
							style="width: 1200px; height: 400px; margin: 0 auto"></div>
					</div>
					<div class="tab-pane col-md-12" id="chart2" role="tabpanel">
						<div class="col-md-12" id="chart2Demo"
							style="min-width: 310px; max-width: 1200px; height: 400px; margin: 0 auto"></div>
					</div>
					<div class="tab-pane col-md-12" id="chart3" role="tabpanel" style="width: 1200px">
					<%
						for(String pName : pNames) {
							String id = "canvas_" + pName;
							%>
							<canvas id=<%=id %> class="col-md-12"></canvas>
							<hr>
							<%
						}
					%>
					</div>
				</div>
			</div>
			<script>
				var chart1Array = [];
                var projectNames = [];
				<%
					for(String pname : pNames) {
					  String name = "'" + pname + "'";
				%>
						projectNames.push(<%=name%>);
				<%
					}
				%>
			</script>
			<%
				String[] colors = {"S", "CPF", "CSF", "CTF", "NB"};
				for (String color : colors) {
			%>
					<script type="text/javascript">
					var color = <%="'" + color + "'"%>;
					$.ajax({
						url : 'webapi/commits/color',
						type : 'GET',
						data: {
							"color" : color
						}, 
						async : false,
						cache : true,
						contentType: 'application/json; charset=UTF-8',
						success : function(responseText) {
							var str = JSON.stringify(responseText);
							var obj = JSON.parse(str);
							chart1Array.push(obj);
						}, 
						error : function(responseText,A,B) {
							console.log(responseText,A,B);
						}
					});
					</script>
			<%
				}
			%>
			<script>
				var chart2Array = [];
				$.ajax({
					url : 'webapi/commits/count',
					type : 'GET',
					async : false,
					cache : true,
					contentType: 'application/json; charset=UTF-8',
					success : function(responseText) {
						var str = JSON.stringify(responseText);
						var obj = JSON.parse(str);
						chart2Array.push(obj);
					}, 
					error : function(responseText,A,B) {
						console.log(responseText,A,B);
					}
				});
			</script>
			<%
				String[] status = {"success", "checkStyleError", "compileFailure", "testFailure"};
				for (String s : status) {
			%>
					<script type="text/javascript">
					var s = <%="'" + s + "'"%>;
					$.ajax({
						url : 'webapi/commits/state/',
						type : 'GET',
						data: {
							"state" : s
						}, 
						async : false,
						cache : true,
						contentType: 'application/json; charset=UTF-8',
						success : function(responseText) {
							var str = JSON.stringify(responseText);
							var obj = JSON.parse(str);
							chart2Array.push(obj);
						}, 
						error : function(responseText,A,B) {
							console.log(responseText,A,B);
						}
					});
					</script>
			<%
				}
			%>
					<script>
					var chart3Array = []
					</script>
					<%
					for(String pName : pNames) {
					%>
					<script type="text/javascript">
						var pName = <%="'" + pName + "'"%>;
						$.ajax({
							url : 'webapi/commits/record/records',
							type : 'GET',
							data: {
								"hw" : pName
							}, 
							async : false,
							cache : true,
							contentType: 'application/json; charset=UTF-8',
							success : function(responseText) {
								var str = JSON.stringify(responseText);
								var chart3;
								chart3 = JSON.parse(str);
								chart3Array.push(chart3);
							}, 
							error : function(responseText,A,B) {
								console.log(responseText,A,B);
							}
						 });
					</script>
					<%
					}
					%>
		</div>
	</div>
</body>
<!-- set Highchart colors -->
<script>
Highcharts.setOptions({
	 colors: ['#5fa7e8', '#e52424', 'gold', '#32CD32', '#878787']
	})
</script>

<script src="./chart/chart1.js"></script>

<!-- set Highchart colors -->
<script>
Highcharts.setOptions({
	 colors: ['#878787', '#5fa7e8', 'gold',  '#e52424', '#32CD32']
	})
</script>

<script src="./chart/chart2.js"></script>

<script src = "https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.bundle.js"></script>
<script src = "https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.bundle.min.js"></script>
<script src = "https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.js"></script>
<script src = "https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.min.js"></script>
<script src="./dist/utils.js"></script>
<script src="./dist/moment.min.js"></script>
<script src="./dist/moment-with-locales.min.js"></script>
<script>
var i;
var bubbleChartDataArray = []
for(i in chart3Array) {
	var chart3 = chart3Array[i];
	var DEFAULT_DATASET_SIZE = 7;
	var addedCount = 0;
	var color = Chart.helpers.color;
	var bubbleChartData = {
	    animation: {
	        duration: 10000
	    },
	    datasets: [{
	        label: chart3.title + ' (' + chart3.deadline + ')',
	        backgroundColor: color(window.chartColors.red).alpha(0.5).rgbString(),
	        borderColor: window.chartColors.red,
	        borderWidth: 1,
	        data: chart3.records
	    }]
	};
	bubbleChartDataArray.push(bubbleChartData);
}
window.onload = function() {
	for(i in chart3Array) {
		var chart3 = chart3Array[i];
	    var ctx = document.getElementById("canvas_" + chart3.title).getContext("2d");
	    var text = 'Commit Record';
	    text.fontsize(100);
	    window.myChart = new Chart(ctx, {
	    	responsive: true,
	        type: 'bubble',
	        data: bubbleChartDataArray[i],
	        options: {
	            responsive: true,
	            title:{
	                display:true,
	                text:text,
	                fontSize: 18,
	                fontStyle: "normal",
	            },
	            tooltips: {
	            	enabled: true,
	                mode: 'single',
	                callbacks: {
	                  label: function(tooltipItem, data) {
	                    var label = data.labels[tooltipItem.index];
	                    var datasetLabel = data.datasets[0].data[tooltipItem.index];
	                    var date = new Date(datasetLabel.t);
	                    return date.toLocaleString() + ', count: ' + datasetLabel.r;
	                  }
	                }
	            },
	            scales: {
	                xAxes: [{
	                	ticks: {
	                		callback: function(value, index, values) {
	                			var now = new Date(value).toDateString();
	                            return now;
	                        },
		                	maxRotation: 60,
		                    minRotation: 60
	                	}
	                }],
	                yAxes: [{
	                    ticks: {
	                        // Include a dollar sign in the ticks
	                        callback: function(value, index, values) {
	                            return value + ':00';
	                        },
	                        max: 24,
	                        min: 0
	                    }
	                }]
	            }
	        }
	    });
	};
}
</script>
</html>