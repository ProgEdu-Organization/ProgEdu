<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="utf-8"%>
<%@ page
	import="fcu.selab.progedu.conn.GitlabService,fcu.selab.progedu.conn.StudentConn,fcu.selab.progedu.conn.HttpConnect"%>
<%@ page
	import="fcu.selab.progedu.config.GitlabConfig,fcu.selab.progedu.config.CourseConfig"%>
<%@ page
	import="fcu.selab.progedu.config.GitlabConfig,fcu.selab.progedu.config.JenkinsConfig"%>
<%@ page import="fcu.selab.progedu.jenkins.JenkinsApi"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.gitlab.api.GitlabAPI"%>
<%@ page import="org.gitlab.api.models.*"%>
<%@ page import="java.util.*"%>
<<<<<<< HEAD
<%@ page
	import="org.json.JSONArray,org.json.JSONException,org.json.JSONObject"%>
<%@ page
	import="fcu.selab.progedu.db.UserDbManager,fcu.selab.progedu.db.AssignmentDbManager,fcu.selab.progedu.db.ScreenshotRecordDbManager"%>
<%@ page
	import="fcu.selab.progedu.data.User,fcu.selab.progedu.data.Assignment"%>
<%@ page
	import="fcu.selab.progedu.jenkins.JobStatus,java.text.SimpleDateFormat"%>
<%@ page import="fcu.selab.progedu.conn.StudentDash"%>
<%@ page import="fcu.selab.progedu.conn.StudentDashChoosePro"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="fcu.selab.progedu.conn.*"%>
<%@ page import="fcu.selab.progedu.status.*"%>
<%@ page import="fcu.selab.progedu.project.AssignmentFactory"%>
<%@ page import="fcu.selab.progedu.service.AssignmentTypeSelector"%>
=======
<%@ page import="org.json.JSONArray,org.json.JSONException,org.json.JSONObject" %>
<%@ page import="fcu.selab.progedu.db.UserDbManager,fcu.selab.progedu.db.ProjectDbManager,fcu.selab.progedu.db.ScreenshotRecordDbManager" %>
<%@ page import="fcu.selab.progedu.data.User,fcu.selab.progedu.data.Project" %>
<%@ page import="fcu.selab.progedu.jenkins.JobStatus,java.text.SimpleDateFormat" %>
<%@ page import="fcu.selab.progedu.conn.StudentDash" %>
<%@ page import="fcu.selab.progedu.conn.StudentDashChoosePro" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="fcu.selab.progedu.conn.*" %>
<%@ page import="fcu.selab.progedu.status.*" %>
<%@ page import="fcu.selab.progedu.project.AssignmentFactory" %>
<%@ page import="fcu.selab.progedu.service.AssignmentTypeSelector" %>
>>>>>>> Github/master
<%@ include file="language.jsp"%>
<%@ include file="studentHeader.jsp"%>

<%
  String private_token = null;
	if(null != session.getAttribute("private_token") && !"".equals(session.getAttribute("private_token")) ){
	  private_token = session.getAttribute("private_token").toString();
	}else{
	  response.sendRedirect("index.jsp");
	}
	session.putValue("page", "studentDashboardChooseProject");
	
	// Set the student private_token
	
	Cookie[] cookies = request.getCookies();
	Cookie cookie = null;
	if(cookies != null){
	  for(Cookie c : cookies){
	    if(c.getName().equals("private_token")){
	      cookie = c;
	      break;
	    }
	  }
	}else {
		response.sendRedirect("index.jsp");
	}
	if(cookie != null){
	  private_token = cookie.getValue();
	}else {
		response.sendRedirect("index.jsp");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<<<<<<< HEAD
<head>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
<style type="text/css">
body {
	overflow-x: hidden;
}

p {
	white-space: nowrap;
}

#mainTable {
	width: 100%;
	height: 100%;
}

#sidebar {
	height: 100%;
	background-color: #444;
	color: white;
	margin: -1px;
	position: fixed; /* Set the navbar to fixed position */
	top: 0;
	z-index: 100;
}

.nav-link {
	color: white;
}

.nav-link:hover {
	color: #33CCFF;
}

#overview {
	color: white;
}

#overview:hover {
	color: #33CCFF;
}

#copyTarget {
	border: 1px solid gray;
	background-color: white;
	border-radius: 5px;
	padding: 5px 0px 5px 10px;
	width: 300px;
	color: gray;
}

#inline {
	width: 100%;
	display: inline;
}

.bigcircle2 {
	border-radius: 10px;
	height: 80px;
	width: 80px;
	color: white;
	text-align: center;
	margin: 0 auto;
}

.bigcircle2 a {
	line-height: 40px;
}

#inline p {
	display: inline;
}

#main {
	background-color: #f5f5f5;
	height: 100%;
	margin-left: 200px;
	overflow-x: scroll;
	padding-top: 20px;
	width: auto;
}

.ovol {
	border-radius: 50px;
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

.bigcircle {
	border-radius: 10px;
	height: 80px;
	font-weight: bold;
	width: 80px;
	color: white;
	text-align: center;
	position: absolute;
	top: 100%;
	left: 45%;
	margin-right: -50%;
	transform: translate(-50%, -50%)
}

.CPF {
	background: #e52424;
}

.BS {
	background: #5fa7e8;
}

.INI {
	background: #878787;
}

.CSF {
	background: gold;
}

.UTF {
	background: #32CD32;
}

#goToJenkins {
	float: right;
	background-color: white;
	color: #1079c9;
	border: 1px solid #1079c9;
	margin-bottom: 10px;
}

.center-justified {
	text-align: justify;
	-moz-text-align-last: center;
	text-align-last: center;
}

.tableActive {
	background-color: #ddd;
}

#container {
	width: 100%;
	height: auto;
	margin: 10px;
	background: #fff3cd;
}

#reference {
	text-align: left;
	display: none;
}
</style>
<script type="text/javascript">
=======
	<head>
		<script src="https://code.highcharts.com/highcharts.js"></script>
		<script src="https://code.highcharts.com/modules/exporting.js"></script>
		<style type="text/css">
			body{
				overflow-x: hidden;
			}
			p {
				white-space: nowrap;
			}
			#mainTable {
				width: 100%;
				height: 100%;
			}
			#sidebar {
				height: 100%;
				background-color: #444;
				color: white; 
				margin: -1px;
				position: fixed; /* Set the navbar to fixed position */
    			top: 0;
    			z-index: 100;
			}
			.nav-link {
				color: white; 
			}
			.nav-link:hover{
				color: #33CCFF;
			}
			#overview {
			color: white;
			}
			#overview:hover{
				color: #33CCFF;
			}
			#copyTarget {
				border: 1px solid gray; 
				background-color: white; 
				border-radius: 5px; 
				padding: 5px 0px 5px 10px; 
				width: 300px;
				color: gray;
			}
			#inline {
				width: 100%;
				display: inline;
			}
			.bigcircle2 {
				border-radius: 10px;
				height: 80px;
	            width: 80px;
	            color: white;
	            text-align: center;
	            margin: 0 auto;
			}
			.bigcircle2 a{
				line-height: 40px;
			}
			
			#inline p {
				display: inline;
			}
			#main {
				background-color: #f5f5f5;
				height: 100%;
				margin-left: 200px;
				overflow-x: scroll;
				padding-top: 20px;
				width: auto;
			}
			.ovol {
				border-radius: 50px;
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
			.bigcircle {
				border-radius: 10px;
				height: 80px;
	            font-weight: bold;
	            width: 80px;
	            color: white;
	            text-align: center;
			    position: absolute;
	    		top: 100%;
	    		left: 45%;
	    		margin-right: -50%;
	    		transform: translate(-50%, -50%)
			}
			.CPF {
				background: #e52424;
			}
			.BS {
				background: #5fa7e8;
			}
			.INI {
				background: #878787;
			}
			.CSF {
				background: gold;
			}
			.UTF {
				background: #32CD32;
			}
			#goToJenkins{
				float: right;
				background-color: white;
				color: #1079c9;
				border: 1px solid #1079c9;
				margin-bottom: 10px;
			}
			.center-justified {
	    		text-align: justify;
	    		-moz-text-align-last: center;
	    		text-align-last: center;
			}
			.tableActive {
				background-color: #ddd;
			}
			#container{
			  width: 100%;
			  height: auto;
			  margin: 10px; 
			  background: #fff3cd;
			}
			#reference{
				text-align: left;
				display: none;
			}
			.screenshotSlides {
	display: none
}

img {
	vertical-align: middle;
}

/* Slideshow container */
.slideshow-container {
	max-width: 1000px;
	position: relative;
	margin: auto;
}

/* Next & previous buttons */
.prev, .next {
	cursor: pointer;
	position: absolute;
	top: 50%;
	width: auto;
	padding: 16px;
	margin-top: -22px;
	color: white;
	font-weight: bold;
	font-size: 18px;
	transition: 0.6s ease;
	border-radius: 0 3px 3px 0;
	user-select: none;
}

/* Position the "next button" to the right */
.next {
	right: 0;
	border-radius: 3px 0 0 3px;
}

/* On hover, add a grey background color */
.prev:hover, .next:hover {
	background-color: #f1f1f1;
	color: black;
}
		</style>
		<script type="text/javascript">
>>>>>>> Github/master
				function handleClick(cb, divId){
					var o=document.getElementById(divId);
					if(cb.checked){
						o.style.display='';
					}else{
						o.style.display='none';
					}
				}
		</script>

<link rel="shortcut icon" href="img/favicon.ico" />
<link rel="bookmark" href="img/favicon.ico" />
<title>ProgEdu</title>
</head>
<body onload="init()">
	<%
	  //To display the under html code (about some if-else)
						StudentDash stuDash = new StudentDash(private_token);
						StudentDashChoosePro stuDashChoPro = new StudentDashChoosePro();
						
						// Get the user's Gitlab project
						List<GitlabProject> stuProjects = stuDash.getStuProject();
					
						String strProjectId = request.getParameter("projectId");
						
						int projectId = -1;
						if(null == strProjectId || "".equals(strProjectId)){
							  
						}else {
							projectId = Integer.parseInt(strProjectId);
						}
						
						GitlabProject choosedProject = stuDashChoPro.getChoosedProject(stuProjects, projectId);
						
						JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
						GitlabService conn = GitlabService.getInstance();
						
						StudentConn sConn = new StudentConn(private_token);
						GitlabUser user = sConn.getUser();
						List<GitlabProject> projects = sConn.getProject();
						Collections.reverse(projects);
						
						UserDbManager db = UserDbManager.getInstance();
						AssignmentDbManager Pdb = AssignmentDbManager.getInstance();
						List<User> users = db.listAllUsers();
						List<Assignment> dbProjects = Pdb.listAllAssignments();
						
						// gitlab jenkins course��Data
						GitlabConfig gitData = GitlabConfig.getInstance();
						
						JenkinsApi jenkins = JenkinsApi.getInstance();
						
						GitlabUser choosedUser = conn.getUserById(userId);
						Collections.reverse(projects);
	%>
	<!-- -----sidebar----- -->
		<div id="sidebar">
			<ul class="nav flex-column" style="padding-top: 20px;">
			  <li class="nav-item" style="margin: 10px 0px 0px 15px; color: burlywood;">
			    <font size="4"><a href="studentDashboard.jsp" id="overview"><i class="fa fa-bar-chart" aria-hidden="true"></i>&nbsp; <fmt:message key="stuDashboard_li_overview"/></a></font>
			  </li>
			  <li class="nav-item" style="margin: 10px 0px 0px 15px;">
			    <font size="4"><a><i class="fa fa-minus-square-o" aria-hidden="true"> &nbsp;<fmt:message key="stuDashboard_li_assignments"/></i></a></font>
			  </li>
			  <%
			    for(GitlabProject stuProject : stuProjects){
			  				  	  String href = "\"studentDashboardChooseProject.jsp?projectId=" + stuProject.getId() + "\"";
			  %>
				  	  	<li class="nav-item" style="margin:0px 0px 0px 30px">
						  <font size="3"><a class="nav-link" href=<%=href%>><i class="fa fa-pencil-square-o" aria-hidden="true"></i>&nbsp; <%=stuProject.getName()%></a></font>
						</li>
				  	  <%
				  	    }
				  	  %>
			</ul>
		</div>
		<!-- -----sidebar----- -->
	<!-- -----main----- -->
		<div class="container-fluid" id="main">
			<%
			  String projectName = choosedProject.getName();
										String projectUrl = stuDashChoPro.getChoosedProjectUrl(choosedProject);
										List<String> jobColors = stuDash.getMainTableJobColor(stuProjects);
										List<String> jobCommitCounts = stuDash.getMainTableJobCommitCount(stuProjects);
										AssignmentDbManager pDb = AssignmentDbManager.getInstance();
										Assignment project = pDb.getAssignmentByName(projectName);
			%>
			<div style="margin: 10px 10px 10px 10px;">
				<h2 style="white-space: nowrap"><i class="fa fa-pencil-square-o" aria-hidden="true"></i>&nbsp; <%=projectName%></h2>
				<br>
				<h5 style="font-weight: 700"><fmt:message key="stuDashChooseProject_p_gitRepo"/></h5>
				<div id="inline" style="white-space: nowrap; margin-left: 0px;">
					<p id="copyTarget" style="padding-right: 10px;"><%=projectUrl%></p>
					<button id="copyButton" class="btn btn-dark" onclick = "copyToClipboard(document.getElementById('copyTarget'))"><i class="fa fa-clipboard" aria-hidden="true"></i></button>
				</div>
				<p><fmt:message key="stuDashChooseProject_p_cloneUrl"/></p>
				<hr>
				<h5 style="font-weight: 700"><fmt:message key="stuDashChooseProject_p_assignmentContent"/></h5>
				<p><%=project.getDescription()%></p>
				<hr>
				<h5 style="font-weight: 700"><fmt:message key="stuDashChooseProject_p_deadline"/></h5>
				<p><%=project.getDeadline()%></p>
			</div>
<<<<<<< HEAD
	<hr>

	<div class="container" style="margin: 25px 0px;">
		<div class="row">
			<div class="col-3">
				<h4 style="text-align: center;">
					<fmt:message key="stuDashChooseProject_h4_codeAnalysisResult" />
				</h4>
				<%
				  =======
							
							<hr>
							
							<div class="container" style="margin: 25px 0px;">
								<div class="row">
									<div class="col-2">
										<h4 style="text-align: center;"><fmt:message key="stuDashChooseProject_h4_codeAnalysisResult"/></h4>
										<%
				>>>>>>> Github/master
											String lastBuildColor = stuDashChoPro.getLastColor(user.getUsername(), projectName);
											lastBuildColor = "bigcircle2 " + lastBuildColor;
											List<Integer> buildNum = stuDashChoPro.getScmBuildCounts(user.getUsername(), projectName);
											int lastBuildNum = buildNum.size();
											if(lastBuildNum==1){
											  lastBuildColor = "bigcircle2 gray";
											}
				%>
				<div style="text-align: center">
					<div style="padding: 5px;">
						<h3 class="<%=lastBuildColor%>"
							style="width: 90px; margin: 0 auto; padding: 20px; color: white;">
							<a><%=lastBuildNum%></a>
						</h3>
					</div>
<<<<<<< HEAD
				</div>
			</div>

			<div class="col-9">
				<h4>
					<fmt:message key="stuDashChooseProject_h4_programHistory" />
				</h4>
				<div style="margin: 15px 0px;">
					<%@ include file="projectLight.jsp"%>
				</div>

				<table class="table table-hover" style="background-color: white"
					id="projectList">
					<thead>
						<tr>
							<th>#</th>
							<th><fmt:message key="stuDashChooseProject_th_status" /></th>
							<th><fmt:message key="stuDashChooseProject_th_date" /></th>
							<th><fmt:message key="stuDashChooseProject_th_comment" /></th>
						</tr>
					</thead>
					<tbody id="projectTbody">
						<%
						  =======
											
											<div class="col-8">
												<h4><fmt:message key="stuDashChooseProject_h4_programHistory"/></h4>
												<div style="margin: 15px 0px;">
													<%@ include file="projectLight.jsp"
						%>
						</div>
						
						<table class="table table-hover" style="background-color: white" id="projectList">
							<thead>
								<tr>
			                    	<th>#</th>
			                    	<th><fmt:message key="stuDashChooseProject_th_status"/></th>
			                    	<th><fmt:message key="stuDashChooseProject_th_date"/></th>
			                    	<th><fmt:message key="stuDashChooseProject_th_comment"/></th>
			                    </tr>
							</thead>
							<tbody id="projectTbody">
							<%
							  >>>>>>> Github/master
															int commit_count = buildNum.size();
															int i=1;
															int lastBuildMessageNum = 0;
															for(Integer num : buildNum){
							%>
						<script type="text/javascript">
										var userName = <%="'" + user.getUsername() + "'"%>
										var proName = <%="'" + projectName + "'"%>
										var date, color, message, num
										$.ajax({
											url : 'webapi/jenkins/buildDetail',
											type : 'GET',
											data: {
												"num": <%=num%>,
												"proName" : proName,
												"userName" : userName
											}, 
											async : true,
											cache : true,
											contentType: 'application/json; charset=UTF-8',
											success : function(responseText) {
												var str = JSON.stringify(responseText);
												var obj = JSON.parse(str);
												date = obj.date;
												color = obj.color;
												message = obj.message;
												num = obj.num;
												
												td_color = document.getElementById("color" + num);
												td_date = document.getElementById("date" + num);
												td_message = document.getElementById("message" + num);
												
												td_color.className=color;
												td_date.textContent = date;
												td_message.innerHTML = message;
											}, 
											error : function(responseText) {
												console.log("False!");
											}
										});
									</script>
						<%
						  String tableActive = "";
																if(num == commit_count){
																  tableActive = "tableActive";
																}
						%>
						<tr id="<%=num%>" onClick="changeIframe(this)"
							class="<%=tableActive%>">
							<td><%=i%></td>
							<td><p class="" id=<%="color" + num%>></p></td>
							<td id=<%="date" + num%>>></td>
							<td id=<%="message" + num%>></td>
						</tr>
						<%
						  i++;
														  	lastBuildMessageNum = num;
														}
						%>
<<<<<<< HEAD
					</tbody>
				</table>
=======
							</tbody>
						</table>
					</div>
			<!-----------------------------------------  Screenshot  ----------------------------------------->
			<div class="col-2">
			<div class="card" id="Screenshots-area"
				style="margin-left: 100px; width: fit-content; float: left;">
				<h4 id="Screenshots" class="card-header">
					<div>
						<i class="fa fa-table" aria-hidden="true"></i> Screenshot
						<div id='screenshotName' style="text-align: right; float: right;">No
							the Screenshot</div>
					</div>
				</h4>
				<div class="card-block"">
					<div class="slideshow-container">
						<%
						  ScreenshotRecordDbManager sd = ScreenshotRecordDbManager.getInstance();
															//why the userId need to -1, because this userId is the gitlabId
															List<String> pngUrls = null;
															for (User u : users) {
																if (u.getGitLabId() == userId) {
																	pngUrls = sd.getScreenshots(u.getId(), projectName);
																	break;
																}
															}
						%>

						<a class="prev" onclick="plusSlides(-1, 0)">&#10094;</a> <a
							class="next" onclick="plusSlides(1, 0)">&#10095;</a>
					</div>
				</div>
			</div>
			<script>
			var pngUrls = new Array();
			
			<%boolean showScreenshot = project.getType().equals("Web");
			System.out.println(project.getType());
			if (showScreenshot) {
				for (int count = 0; count < pngUrls.size(); count++) {%>
			pngUrls[<%=count%>] = "<%=pngUrls.get(count)%>";
			<%} ;
			} ;%>
			var showScreenshot = <%=showScreenshot%>;
			if(showScreenshot){
				for(var url in pngUrls){
					var pngUrl = "<%=jenkinsData.getJenkinsHostUrl()%>" + pngUrls[url];
					$screenshotSlides = $("<div class='screenshotSlides'>"
							+"<a href='"+ pngUrl + "'>"
								+"<img src='"+  pngUrl +"' style='height: 480px' /"
								+"</a></div>");

					$('.slideshow-container').append($screenshotSlides)
				}
				var slideIndex = [1];
				var slideId = ["screenshotSlides"]
				showSlides(1, 0);
			}else{
				$("#Screenshots-area").css('display',"none");
			}

				
				
				function plusSlides(n, no) {
				  showSlides(slideIndex[no] += n, no);
				}
				
				function showSlides(n, no) {
				  var i;
				  var x = document.getElementsByClassName(slideId[no]);
				  if (n > x.length) {slideIndex[no] = 1}    
				  if (n < 1) {slideIndex[no] = x.length}
				  for (i = 0; i < x.length; i++) {
				     x[i].style.display = "none";  
				  }
				  x[slideIndex[no]-1].style.display = "block";
				  //find the the screenshot location
				  $screenshotSlides = $(".screenshotSlides").filter(function () {
					    return $(this).css("display") == "block";
					});
				  var nowScreenshotUrl = $screenshotSlides.children("a").attr('href');
				  var urlSplit = nowScreenshotUrl.split('/');
				  var screenshotName = urlSplit[urlSplit.length - 1].split('.')[0]+'.html';
				  $('#screenshotName').html(screenshotName);
				}
			</script>
		</div>
		</div>
		<!-----------------------------------------  Screenshot  ----------------------------------------->
				</div>
>>>>>>> Github/master
			</div>
		</div>
	</div>

	<hr>

	<!-- iFrame -->
	<%
	  int num = lastBuildMessageNum;
					String jobName = user.getUsername() + "_" + projectName;
					String jenkinsBuildNumUrl = jenkinsData.getJenkinsHostUrl() + "/job/" + jobName;
					String lastBuildUrl = jenkinsData.getJenkinsHostUrl() + "/job/" + jobName + "/" + num + "/consoleText";
					String url = jenkinsData.getJenkinsHostUrl() + "/job/" + jobName + "/";
				
					StudentDashChoosePro studentDashChoosePro = new StudentDashChoosePro();
					String color = studentDashChoosePro.getLastColor(choosedUser.getUsername(),projectName);
					int assignmentType = project.getType();
					String  assignmentTypeName = Pdb.getAssignmentTypeName(assignmentType);
					
					AssignmentTypeSelector assignmentTypeSelector = 
				        AssignmentFactory.getAssignmentType(assignmentTypeName);
					Status status = assignmentTypeSelector.getStatus(color);
					
					String detailConsoleText = jenkins.getConsoleText(lastBuildUrl);
					String console = status.extractFailureMsg(detailConsoleText);
	%>
<<<<<<< HEAD
	<div>
		<h4>
			<a id="iFrameTitle" href="<%=jenkinsBuildNumUrl%>">Feedback
				Information (#<%=num %>)
			</a>
		</h4>
		<h6 id="reference">
			<a href="https://blog.mosil.biz/2014/05/java-style-guide/"
				target="_blank">java-style-guide</a>
		</h6>
		<div>
=======
			<div style="margin-left:200px">
				<h4><a id="iFrameTitle" href="<%=jenkinsBuildNumUrl%>">Feedback Information (#<%=num %>)</a></h4>
				<h6 id="reference"><a href="https://blog.mosil.biz/2014/05/java-style-guide/" target="_blank">java-style-guide</a></h6>
			<div>
>>>>>>> Github/master
			<div id="container">
				<pre style="overflow: auto;"><%=console%></pre>
			</div>
			<!-- iFrame -->
		</div>
		<!-- -----main----- -->
		<script type="text/javascript">
		function copyToClipboard(elem) {
			  // create hidden text element, if it doesn't already exist
		    var targetId = "_hiddenCopyText_";
		    var isInput = elem.tagName === "INPUT" || elem.tagName === "TEXTAREA";
		    var origSelectionStart, origSelectionEnd;
		    if (isInput) {
		        // can just use the original source element for the selection and copy
		        target = elem;
		        origSelectionStart = elem.selectionStart;
		        origSelectionEnd = elem.selectionEnd;
		    } else {
		        // must use a temporary form element for the selection and copy
		        target = document.getElementById(targetId);
		        if (!target) {
		            var target = document.createElement("textarea");
		            target.style.position = "absolute";
		            target.style.left = "-9999px";
		            target.style.top = "0";
		            target.id = targetId;
		            document.body.appendChild(target);
		        }
		        target.textContent = elem.textContent;
		    }
		    // select the content
		    var currentFocus = document.activeElement;
		    target.focus();
		    target.setSelectionRange(0, target.value.length);
		    
		    // copy the selection
		    var succeed;
		    try {
		    	  succeed = document.execCommand("copy");
		    } catch(e) {
		        succeed = false;
		    }
		    // restore original focus
		    if (currentFocus && typeof currentFocus.focus === "function") {
		        currentFocus.focus();
		    }
		    
		    if (isInput) {
		        // restore prior selection
		        elem.setSelectionRange(origSelectionStart, origSelectionEnd);
		    } else {
		        // clear temporary content
		        target.textContent = "";
		    }
		    return succeed;
		}
	</script>
		<script type="text/javascript">
		function changeIframe(tr){
			var url = '<%=jenkinsBuildNumUrl%>' + '/' + tr.id + '/consoleText';
			$.ajax({
				url: 'webapi/jenkins/getFeedbackInfo',
				type: 'POST',
				data: url,
				success: function(res){
					$('#container pre').html(res);
				}
			})
			$('#iFrameTitle').html("Feedback Information (#" + tr.id + ")");
			$('#projectTbody tr').removeClass("tableActive");
			$('#'+tr.id).addClass("tableActive");
			showJavaStyle(tr);
		}
		//show javaStyle Reference
		function showJavaStyle(tr){
			var errorStatus = ['CSF'];
			var projectType = "<%=assignmentTypeName%>
			";
				if (projectType == "Maven") {
					var $className = $(tr).children('td').find('p').attr(
							'class');
					$className = $className.replace('circle ', '');
					for ( var s in errorStatus) {
						if (errorStatus[s] == $className) {
							$('#reference').show();
							break;
						} else {
							$('#reference').hide();
						}
					}
				}

			}
			function init() {
				setTimeout(function() {
					var errorStatus = [ 'CSF' ];
					var num =
		<%=num%>
			;
					var tr = $("tr[id='" + num + "']");
					showJavaStyle(tr);
				}, 1000);
			};
		</script>
</body>
</html>