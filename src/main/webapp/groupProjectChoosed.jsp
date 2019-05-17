<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="utf-8"%>
<%@ page import="fcu.selab.progedu.jenkins.JenkinsApi"%>
<%@ page import="fcu.selab.progedu.config.GitlabConfig"%>
<%@ page import="fcu.selab.progedu.config.JenkinsConfig"%>
<%@ page
	import="fcu.selab.progedu.db.UserDbManager, fcu.selab.progedu.db.ProjectDbManager"%>
<%@ page
	import="fcu.selab.progedu.data.User, fcu.selab.progedu.data.Project"%>
<%@ page
	import="fcu.selab.progedu.data.User, fcu.selab.progedu.data.Group"%>
<%@ page import="org.gitlab.api.models.*"%>
<%@ page import="java.util.*"%>
<%@ page import="fcu.selab.progedu.jenkins.JobStatus"%>
<%@ page
	import="org.json.JSONArray, org.json.JSONException, org.json.JSONObject"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="fcu.selab.progedu.conn.*"%>
<%@ page import="fcu.selab.progedu.status.*"%>
<%@ page import="fcu.selab.progedu.service.AssignmentTypeFactory"%>
<%@ page import="fcu.selab.progedu.service.AssignmentTypeSelector"%>

<%
  if (session.getAttribute("username") == null
      || session.getAttribute("username").toString().equals("")) {
    response.sendRedirect("index.jsp");
  }
  session.putValue("page", "dashboard");

  int groupId = Integer.parseInt(request.getParameter("groupId"));
%>

<%@ include file="language.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
html, body {
	height: 100%;
	overflow-x: hidden;
}

#allProject {
	margin: 10px 0px 0px 0px;
}

#inline p {
	display: inline;
}

#inline {
	margin: 20px;
}

#main {
	height: 100%;
	margin-left: 200px;
	overflow-x: scroll;
	padding-top: 20px;
	width: auto;
}

.sidebar {
	height: 100%;
	background-color: #444;
	color: white;
	margin: -1px;
	position: fixed; /* Set the navbar to fixed position */
	top: 0;
	padding-top: 50px;
	overflow-y: scroll;
	z-index: 100;
}

.sidebar a {
	color: white;
}

.sidebar a:hover {
	color: orange;
}

.sidebar button {
	color: white;
	background: none;
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

.gold {
	background: #FFD700;
}

.circle a {
	color: #fff;
	line-height: 30px;
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
#container{
		  width: 100%;
		  height: auto;
		  margin: 10px; 
		  background: #fff3cd;
		}
</style>

<link rel="shortcut icon" href="img/favicon.ico" />
<link rel="bookmark" href="img/favicon.ico" />
<title>ProgEdu</title>
</head>
<body>
	<%
	  Conn conn = Conn.getInstance();

	  UserDbManager db = UserDbManager.getInstance();
	  ProjectDbManager Pdb = ProjectDbManager.getInstance();
	  StudentDashChoosePro stuDashChoPro = new StudentDashChoosePro();

	  List<User> users = db.listAllUsers();
	  List<Project> dbProjects = Pdb.listAllProjects();

	  // gitlab jenkins course��Data
	  GitlabConfig gitData = GitlabConfig.getInstance();
	  JenkinsConfig jenkinsData = JenkinsConfig.getInstance();

	  JenkinsApi jenkins = JenkinsApi.getInstance();
	%>
	<%@ include file="header.jsp"%>
	<!-- -----sidebar----- -->
	<div class="sidebar" style="width: 200px">
		<ul class="nav flex-column" style="padding-top: 20px;">
			<li class="nav-item"><font size="4"><a
					href="javascript:;" data-toggle="collapse" data-target="#group"
					class="nav-link"><i class="fa fa-bars" aria-hidden="true"></i>&nbsp;
						<fmt:message key="dashboard_a_group" /> <i
						class="fa fa-chevron-down" aria-hidden="true"></i></a></font>
				<ul id="group" class="collapse show" style="list-style: none;">
					<%
					  /*初始化group*/
					  Group[] groups = { new Group(), new Group() };
					  groups[0].setGroupName("小白組");
					  groups[0].addContributor("小新");
					  groups[0].addContributor("佳和");
					  groups[0].addContributor("一拳");
					  groups[0].setGroupId(1);
					  groups[0].setMaster("佳和");
					  groups[1].setGroupName("小黑組");

					  Group group = null;
					  for (Group g : groups) {
					    String href = "\"dashStuChoosed.jsp?groupName=" + g.getGroupName() + "\"";
					    if (g.getGroupId() == groupId) {
					      group = g;
					    }
					%>
					<li class="nav-item"><font size="3"><a class="nav-link"
							href=<%=href%>><i class="fa fa-angle-right"
								aria-hidden="true"></i>&nbsp; <%=g.getGroupName()%></a></font></li>
					<%
					  }
					%>
				</ul></li>
		</ul>
	</div>
	<!-- -----sidebar----- -->
	<div class="container-fluid" id="main">
		<h1 style="margin-bottom: 20px;">
			隊伍<%=groupId%>_<%=group.getGroupName()%>
		</h1>
		<!-- ---------------------------- Project ------------------------------- -->
		<%
		  //ProjectDbManager pDb = ProjectDbManager.getInstance();
		  //Project project = pDb.getProjectByName(projectName);
		%>
		<div style="margin: 10px 10px 10px 10px;">
			<h2 style="white-space: nowrap">
				<i class="fa fa-pencil-square-o" aria-hidden="true"></i>&nbsp;
				<%=group.getGroupName()%></h2>
			<hr>
			<h5 style="font-weight: 700">組員</h5>
			<p><%=group.getContributor().toString()%></p>
			<h5 style="font-weight: 700">
				<fmt:message key="stuDashChooseProject_p_assignmentContent" />
			</h5>
			<p><%=group.getGroupName()%></p>
			<hr>
			<h5 style="font-weight: 700">
				<fmt:message key="stuDashChooseProject_p_deadline" />
			</h5>
			<hr>
		</div>
		<!-- ---------------------------- Student Project ------------------------------- -->
		<div class="card" style="padding: 0; width: fit-content">
			<h4 id="Student Projects" class="card-header">
				<i class="fa fa-table" aria-hidden="true"></i>&nbsp; Records
			</h4>
			<div class="card-block">
				<%@ include file="projectLight.jsp"%>
				<table class="table table-hover"
					style="margin-top: 20px; width: 100%; margin-bottom: 0px;">
					<thead>
						<tr>
							<th width="10%" class="text-center">Commit</th>
							<th width="10%">Light</th>
							<th width="15%">Date</th>
							<th width="10%">Author</th>
							<th>Commit Message</th>
						</tr>
					</thead>
					<tbody id="projectTbody">
						<tr id="1" class="">

							<th width="10%" class="text-center"><%=0%></th>
							<td width="10%"><p id="color1" class="circle INI"></p></td>
							<td width="15%" id="date1">2019-05-06 11:40</td>
							<td width="15%" id="author1">小白</td>
							<td id="message1">"Instructor&nbsp;Commit"</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<!-- ---------------------------- Student Project ------------------------------- -->
		<!-- iFrame -->
		<%
		  String console = "Instructor Commit";
		%>
		<h4>
			<a id="iFrameTitle" href="">Feedback Information (<%=1%>)
			</a>
		</h4>
		<div id="container">
			<pre style="overflow: auto;"><%=console%></pre>
		</div>
		<!-- iFrame -->
	</div>
</body>
</html>