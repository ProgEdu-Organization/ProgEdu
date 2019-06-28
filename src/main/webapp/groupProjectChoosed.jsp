<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="utf-8"%>
<%@ page import="fcu.selab.progedu.jenkins.JenkinsApi"%>
<%@ page import="fcu.selab.progedu.config.GitlabConfig"%>
<%@ page import="fcu.selab.progedu.config.JenkinsConfig"%>
<%@ page
	import="fcu.selab.progedu.db.UserDbManager,fcu.selab.progedu.db.AssignmentDbManager"%>
<%@ page
	import="fcu.selab.progedu.data.User,fcu.selab.progedu.data.Assignment"%>
<%@ page
	import="fcu.selab.progedu.data.User,fcu.selab.progedu.data.Group"%>
<%@ page import="org.gitlab.api.models.*"%>
<%@ page import="java.util.*"%>
<%@ page import="fcu.selab.progedu.jenkins.JobStatus"%>
<%@ page
	import="org.json.JSONArray,org.json.JSONException,org.json.JSONObject"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="fcu.selab.progedu.conn.*"%>
<%@ page import="fcu.selab.progedu.status.*"%>
<%@ page import="fcu.selab.progedu.service.IGroupProject"%>
<%@ page import="fcu.selab.progedu.service.AssignmentTypeEnum"%>
<%@ page import="fcu.selab.progedu.service.GroupProjectFactory"%>
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
			  AssignmentDbManager Pdb = AssignmentDbManager.getInstance();
			  StudentDashChoosePro stuDashChoPro = new StudentDashChoosePro();

			  List<User> users = db.listAllUsers();
			  List<Assignment> dbProjects = Pdb.listAllProjects();

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
					  groups[0].setGroupName("group1");
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
		<div class="col-9">
			<h4><fmt:message key="stuDashChooseProject_h4_programHistory"/></h4>
			<div style="margin: 15px 0px;">
				<%@ include file="projectLight.jsp" %>
			</div>
			
			<table class="table table-hover" style="background-color: white" id="projectList">
				<thead>
					<tr>
						<th width="5%" class="text-center">Commit</th>
						<th width="5%">Light</th>
						<th width="15%">Date</th>
						<th width="5%">Author</th>
						<th>Commit Message</th>
					</tr>
				</thead>
				
				<tbody id="projectTbody">
				<%
					String groupName = group.getGroupName();
					String projectName = groupName;
					List<Integer> buildNum = stuDashChoPro.getScmBuildCounts(groupName, projectName);
					int commit_count = buildNum.size();
					int i=1;
					int lastBuildMessageNum = 0;
					for(Integer num : buildNum){
					  	%>
					  	<script type="text/javascript">
							var groupName = <%="'" + groupName + "'"%>
							var projectName = <%="'" + projectName + "'"%>
							var num, status, date, committer, message
							$.ajax({
								url : 'webapi/jenkins/groupProjectBuildDetail',
								type : 'GET',
								data: {
									"num": <%=num%>,
									"projectName" : projectName,
									"groupName" : groupName
								}, 
								async : true,
								cache : true,
								contentType: 'application/json; charset=UTF-8',
								success : function(responseText) {
									
									var str = JSON.stringify(responseText);
									var obj = JSON.parse(str);
									
									num = obj.num;
									status = obj.status;
									date = obj.date;
									committer = obj.committer;
									message = obj.message;
									
									td_status = document.getElementById("status" + num);
									td_date = document.getElementById("date" + num);
									td_message = document.getElementById("message" + num);
									td_committer = document.getElementById("committer" + num);
									
									td_status.className=status;
									td_date.textContent = date;
									td_message.innerHTML = message;
									td_committer.textContent = committer;
									
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
					  	<tr id="<%=num %>" onClick="changeIframe(this)" class="<%=tableActive%>">
					  		<td class="text-center"><%=i %></td>
					  		<td width="5%" ><p id=<%="status" + num %>></p></td>
					  		<td width="5%" id=<%="date" + num %>></td>
					  		<td width="5%" id=<%="committer" + num %>></td>
					  		<td width="15%" id=<%="message" + num %>></td>
					  	</tr>
					  	
					  	<%
					  	i++;
					  	lastBuildMessageNum = num;
					}
				%>
				</tbody>
			</table>
		</div>
		<!-- ---------------------------- Student Project ------------------------------- -->
		<!-- iFrame -->
		<%
		String jobName = groupName + "_" + projectName;
		String jenkinsBuildNumUrl = jenkinsData.getJenkinsHostUrl() + "/job/" + jobName;
		String lastBuildUrl = jenkinsData.getJenkinsHostUrl() + "/job/" + jobName + "/" + lastBuildMessageNum + "/consoleText";
		String url = jenkinsData.getJenkinsHostUrl() + "/job/" + jobName + "/";
		String projectType = AssignmentTypeEnum.MAVEN.getTypeName();
		StudentDashChoosePro studentDashChoosePro = new StudentDashChoosePro();
		
		String detailConsoleText = jenkins.getConsoleText(lastBuildUrl);
		String buildApiJson = stuDashChoPro.getBuildApiJson(lastBuildMessageNum,groupName, projectName);
		String statusType = stuDashChoPro.getCommitStatus(lastBuildMessageNum,groupName, projectName, buildApiJson,projectType);
		//String statusType = "INI";
		
		IGroupProject groupProject = GroupProjectFactory.getGroupProjectType(projectType);
		Status status = groupProject.getStatus(statusType);
		
		
		String console = status.extractFailureMsg(detailConsoleText);
		%>
		<h4>
			<a id="iFrameTitle" href="<%=jenkinsBuildNumUrl %>">Feedback Information (#<%=lastBuildMessageNum%>)
			</a>
		</h4>
		<div id="container">
			<pre style="overflow: auto;"><%=console%></pre>
		</div>
		<!-- iFrame -->
		
		<!-- changeIframe -->
		<script type="text/javascript">
			function changeIframe(tr){
				var url = '<%=jenkinsBuildNumUrl%>' + '/' + tr.id + '/consoleText';
				$.ajax({
					url: 'webapi/jenkins/getFeedbackInfoForGroup',
					type: 'POST',
					data: url,
					success: function(res){
						$('#container pre').html(res);
					}
				})
				$('#iFrameTitle').html("Feedback Information (#" + tr.id + ")");
				$('#projectTbody tr').removeClass("tableActive");
				$('#'+tr.id).addClass("tableActive");
				//showJavaStyle(tr);
			}
		
		</script>
	<!-- changeIframe -->
	</div>
</body>
</html>