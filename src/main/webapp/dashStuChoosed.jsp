<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="utf-8"%>
<%@ page
	import="fcu.selab.progedu.conn.GitlabService,fcu.selab.progedu.conn.HttpConnect,fcu.selab.progedu.conn.StudentConn"%>
<%@ page
	import="fcu.selab.progedu.jenkins.JenkinsApi,fcu.selab.progedu.conn.Language"%>
<%@ page import="fcu.selab.progedu.config.CourseConfig"%>
<%@ page import="fcu.selab.progedu.config.GitlabConfig"%>
<%@ page import="fcu.selab.progedu.config.JenkinsConfig"%>
<%@ page
	import="fcu.selab.progedu.db.UserDbManager,fcu.selab.progedu.db.AssignmentDbManager"%>
<%@ page
	import="fcu.selab.progedu.data.User,fcu.selab.progedu.data.Assignment"%>
<%@ page import="org.gitlab.api.GitlabAPI"%>
<%@ page import="org.gitlab.api.models.*"%>
<%@ page import="java.util.*,fcu.selab.progedu.conn.Dash"%>
<%@ page import="fcu.selab.progedu.jenkins.JobStatus"%>

<%
  if(session.getAttribute("username") == null || session.getAttribute("username").toString().equals("")){
		response.sendRedirect("index.jsp");
	}
	session.putValue("page", "dashStuChoosed");
%>

<%@ include file="language.jsp"%>

<%
  String studentId = request.getParameter("studentId");
	if(null == studentId){
	  response.sendRedirect("index.jsp");
	}
%>

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
	position: fixed;
	/* Set the navbar to fixed position */
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
</style>

<link rel="shortcut icon" href="img/favicon.ico" />
<link rel="bookmark" href="img/favicon.ico" />
<title>ProgEdu</title>
</head>

<body>
	
	<%
		  GitlabService conn = GitlabService.getInstance();
			
				UserDbManager db = UserDbManager.getInstance();
				AssignmentDbManager Pdb = AssignmentDbManager.getInstance();
				
				// Get all db users
				List<User> users = db.listAllUsers();
				
				// Get all db projects
				List<Assignment> dbProjects = Pdb.listAllAssignments();
				
				// gitlab jenkins course data
				GitlabConfig gitData = GitlabConfig.getInstance();
				JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
				CourseConfig courseData = CourseConfig.getInstance();
				
				JenkinsApi jenkins = JenkinsApi.getInstance();
				
				// Get the choosed user
				User choosedUser = new User();
		     	for(User user : users){
		     		if(studentId.equals(String.valueOf(user.getGitLabId()))){
		     			choosedUser = user;
		     		    break;
		     		}
		     	}
		%>
	<%@ include file="header.jsp"%>
	<!-- -----sidebar----- -->
	<div class="sidebar" style="width: 200px">
		<ul class="nav flex-column">
			<li class="nav-item"><font size="4"><a
					style="color: white;" href="javascript:;" data-toggle="collapse"
					data-target="#projects" class="nav-link"><i class="fa fa-bars"
						aria-hidden="true"></i>&nbsp; <%=choosedUser.getStufentId()%> <i
						class="fa fa-chevron-down" aria-hidden="true"></i></a></font>
				<ul id="projects" class="collapse" style="list-style: none;">
					<%
					  List<GitlabProject> projects = conn.getProject(choosedUser);
										    			  Collections.reverse(projects);
										    			  
										    			  for(GitlabProject project : projects){
										    			      for(Assignment dbProject : dbProjects){
										    			         if(project.getName().equals(dbProject.getName())){
										    			            String href = "dashProjectChoosed.jsp?userId=" + choosedUser.getGitLabId() + "&proName=" + project.getName();
					%>
					<li class="nav-item"><font size="3"><a
							style="color: white;" class="nav-link" href=<%=href%>><i
								class="fa fa-angle-right" aria-hidden="true"></i>&nbsp; <%=project.getName()%></a>
					</font></li>
					<%
					  }
															            	      					            	      					            	      					            	  }
															            	      					            	      					            	      					            	}
					%>
				</ul></li>
			<li class="nav-item"><font size="4"><a
					style="color: white;" href="javascript:;" data-toggle="collapse"
					data-target="#student" class="nav-link"><i class="fa fa-bars"
						aria-hidden="true"></i>&nbsp; <fmt:message
							key="dashboard_a_student" /> <i class="fa fa-chevron-down"
						aria-hidden="true"></i></a></font>
				<ul id="student" class="collapse show" style="list-style: none;">
					<%
					  for(User user : users){
											        			            String style = "";
												  			          	    String userName = user.getStufentId();
												  			          	    String name = user.getName();
												            	 			String href = "\"dashStuChoosed.jsp?studentId=" + user.getGitLabId() + "\"";
												            	 			if(choosedUser.getStufentId().equals(user.getStufentId())) {
												            	 				style = "color: burlywood;";
												            	 			}
					%>
					<li class="nav-item"><font size="3"> <a
							style="<%=style%>" class="nav-link active" href=<%=href%>> <i
								class="fa fa-angle-right" aria-hidden="true"></i>&nbsp; <%=userName%>&nbsp;
								<%=name%>
						</a>
					</font></li>
					<%
					  }
					%>
				</ul></li>
		</ul>
	</div>
	<!-- -----sidebar----- -->
	<%
	  String private_token = choosedUser.getGitLabToken();
		   	       	StudentConn sConn = new StudentConn(private_token); 	
		   	       	List<GitlabProject> gitProjects = sConn.getProject();
		   	       	int pro_total_commits = 0;
	%>
	<div class="container-fluid" id="main">
		<h2><%=choosedUser.getStufentId()%></h2>
		<div class="card" style="width: fit-content">
			<div class="card-header">
				<h4 id="Statistics Chart">
					<i class="fa fa-table" aria-hidden="true"></i>&nbsp; 作業
				</h4>
			</div>
			<div class="card-block">
				<%@ include file="projectLight.jsp"%>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>作業</th>
							<%
							  for(Assignment dbProject : dbProjects){
							%>
							<th style="font-weight: 900; font-size: 18px"><%=dbProject.getName()%></th>
							<%
							  }
							%>
						</tr>
					</thead>
					<tbody>
						<tr id="allProject" style="width: 100%">
							<th>Commits</th>
							<%
							  for(Assignment dbProject : dbProjects){
																															  
																															  int commit_count = 0;
																															  String projectJenkinsUrl = "";
																															  
																															  for(GitlabProject gitProject : gitProjects){
																															    if(dbProject.getName().equals(gitProject.getName())){
																																  projectJenkinsUrl = "dashProjectChoosed.jsp?userId=" + choosedUser.getGitLabId() + "&proName=" + gitProject.getName();
																															      Dash dash = new Dash(choosedUser);
																															      commit_count = dash.getProjectCommitCount(gitProject);
							%>
							<script type="text/javascript">
								var userName = <
									%= "'" + choosedUser.getStudentId()() + "'" % >
									var proName = <
										%= "'" + gitProject.getName() + "'" % >
										$
										.ajax({
											url: 'webapi/commits/result',
											type: 'GET',
											data: {
												"proName": proName,
												"userName": userName
											},
											async: true,
											cache: true,
											contentType: 'application/json; charset=UTF-8',
											success: function (responseText) {
												var result = responseText
													.split(",");
												if (result.length >= 3) {
													var d = document
														.getElementById(result[0]);
													d.className = result[1];
													var a = document
														.getElementById(result[0] +
															"_commit");
													a.textContent = result[2];
												}
											},
											error: function (responseText) {
												console.log("False!");
											}
										});
							</script>
							<%
							  }else{
																									continue;
																								}
							%>
							<td>
								<p class=""
									id=<%=choosedUser.getStufentId() + "_" + dbProject.getName()%>>
									<a href="#" onclick="window.open('<%=projectJenkinsUrl%>')"
										id=<%=choosedUser.getStufentId() + "_" + dbProject.getName() + "_commit"%>><%=commit_count %></a>
								</p>
							</td>
							<%
									  }
									}
								%>

						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>

</html>