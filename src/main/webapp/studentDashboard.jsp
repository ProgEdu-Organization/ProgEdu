<%@ page language="java" contentType="text/html; charset=BIG5" pageEncoding="utf-8"%>
<%@ page import="fcu.selab.progedu.conn.Conn, fcu.selab.progedu.conn.StudentConn, fcu.selab.progedu.conn.HttpConnect"%>
<%@ page import="fcu.selab.progedu.config.GitlabConfig,fcu.selab.progedu.config.CourseConfig"%>
<%@ page import="fcu.selab.progedu.config.JenkinsConfig" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.gitlab.api.GitlabAPI"%>
<%@ page import="org.gitlab.api.models.*"%>
<%@ page import="java.util.*"%>
<%@ page import="fcu.selab.progedu.jenkins.JobStatus" %>
<%@ page import="fcu.selab.progedu.jenkins.JenkinsApi" %>
<%@ page import="org.json.JSONArray, org.json.JSONException, org.json.JSONObject" %>
<%@ page import="fcu.selab.progedu.db.UserDbManager, fcu.selab.progedu.db.ProjectDbManager" %>
<%@ page import="fcu.selab.progedu.data.User, fcu.selab.progedu.data.Project" %>  
<%@ page import="fcu.selab.progedu.conn.StudentDash" %> 
<%@ page import="fcu.selab.progedu.conn.Language" %>
<%@ page import="javax.servlet.http.Cookie" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="language.jsp"%>
<%@ include file="studentHeader.jsp"%>

<%
	session.putValue("page", "studentDashboard");
	// Set the student private_token
	String private_token = null;
	
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
	
	/*Language language = new Language();
	String lan = request.getParameter("language");
	String basename = null;
	if(null != lan && !"".equals(lan)){
	  basename = language.getBaseName(lan);
	}
	System.out.println("lan : " + lan);
	System.out.println("basename : " + basename);*/
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<style type="text/css">
		html, body {
			height: 100%;
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
			width: 200px;
			position: fixed;
			top: 0;
			left: 0;
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
		#main {
			height: 100%;
			margin-left: 200px;
			overflow-x: scroll;
			padding-top: 20px;
			width: auto;
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
		#goToJenkins{
			float: right;
			background-color: white;
			color: #1079c9;
			border: 1px solid #1079c9;
		}
		#gotop {
		    display: none;
		    position: fixed;
		    right: 20px;
		    bottom: 20px;    
		    padding: 10px 15px;    
		    font-size: 20px;
		    background: #777;
		    color: white;
		    cursor: pointer;
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
	</style>

	<link rel="shortcut icon" href="img/favicon.ico"/>
	<link rel="bookmark" href="img/favicon.ico"/>
<title>ProgEdu</title>
</head>
<body>
	<%
		// Get the user in Gitlab
		StudentConn sConn = new StudentConn(private_token);
		GitlabUser user = sConn.getUser();
		
		Cookie cookieUserId = new Cookie("userId", String.valueOf(user.getId()));
    	response.addCookie(cookieUserId);
		
		// To display the under html code (about some if-else)
		StudentDash stuDash = new StudentDash(private_token);
		
		// Get the user's Gitlab project
  		List<GitlabProject> stuProjects = stuDash.getStuProject();
	%>
		<!-- -----sidebar----- -->
		<div id="sidebar">
			<ul class="nav flex-column" style="padding-top: 20px;">
			  <li class="nav-item" style="margin: 10px 0px 0px 15px; color: white;">
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
						  <font size="3"><a class="nav-link" href=<%=href %>><i class="fa fa-pencil-square-o" aria-hidden="true"></i>&nbsp; <%=stuProject.getName() %></a></font>
						</li>
				  	  <%
				  	}
			  %>
			</ul>
		</div>
		<!-- -----sidebar----- -->
		<!-- -----main----- -->
		<div class="container-fluid" id="main">
           	<div class="card" style="width: fit-content;">
	        	<h2 class="card-header">
              		<i class="fa fa-bar-chart" aria-hidden="true"></i> <fmt:message key="stuDashboard_h2_overviewOfAssignments"/>
            	</h2>
            	<div class="card-block">
					<%@ include file="projectLight.jsp" %>
					<!-- -----table----- -->
					<table class="table table-striped" style="margin-top: 20px; margin-bottom: 0px; width: max-content;">
						<thead>
							<tr>
								<th><fmt:message key="stuDashboard_th_studentId"/></th>
								<%
									for(GitlabProject stuProject : stuProjects){
										%>
											<th><%=stuProject.getName() %></th>
										<%
									}
								%>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td><%=user.getUsername() %></td>
								<%
									List<JobStatus> jobStatus = stuDash.getJobStatusList(stuProjects, user);
									int i = 0;
									for(GitlabProject stuProject : stuProjects){
									  	int commitCount = stuDash.getScmCommit(user.getUsername(), stuProject);
									  	String href = "\"studentDashboardChooseProject.jsp?projectId=" + stuProject.getId() + "\"";
									  	%>
									      <script type="text/javascript">
											var userName = <%="'" + user.getUsername() + "'"%>
											var proName = <%="'" + stuProject.getName() + "'"%>
											$.ajax({
												url : 'webapi/commits/result',
												type : 'GET',
												data: {
													"proName" : proName,
													"userName" : userName
												}, 
												async : true,
												cache : true,
												contentType: 'application/json; charset=UTF-8',
												success : function(responseText) {
													var result = responseText.split(",");
													if(result.length >= 3) {
														var d = document.getElementById(result[0]);
														d.className = result[1];
														var a = document.getElementById(result[0] + "_commit");
														a.textContent = result[2];
													}
												}, 
												error : function(responseText) {
													console.log("False!");
												}
											});
										</script>
										<%
									  	%>
									  		<td><p class="" id=<%= user.getUsername() + "_" + stuProject.getName()%>>
									  			<a href=<%=href %> id=<%= user.getUsername() + "_" + stuProject.getName() + "_commit"%>><%=commitCount %></a>
									  		</p></td>
									  	<%
									  	i++;
									}
								%>
							</tr>
						</tbody>
					</table>
					<!-- -----table----- -->
				</div>
			</div>
		</div>
		<!-- -----main----- -->
	<div id="gotop"><i class="fa fa-chevron-up" aria-hidden="true"></i></div>
</body>
</html>