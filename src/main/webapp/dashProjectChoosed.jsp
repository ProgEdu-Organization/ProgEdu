<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="utf-8"%>
<%@ page import="fcu.selab.progedu.jenkins.JenkinsApi" %>
<%@ page import="fcu.selab.progedu.config.GitlabConfig" %>
<%@ page import="fcu.selab.progedu.config.JenkinsConfig" %>
<%@ page import="fcu.selab.progedu.db.UserDbManager, fcu.selab.progedu.db.ProjectDbManager" %>
<%@ page import="fcu.selab.progedu.data.User, fcu.selab.progedu.data.Project" %>
<%@ page import="org.gitlab.api.models.*" %>
<%@ page import="java.util.*" %>
<%@ page import="fcu.selab.progedu.jenkins.JobStatus" %>
<%@ page import="org.json.JSONArray, org.json.JSONException, org.json.JSONObject" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="fcu.selab.progedu.conn.*" %>
<%@ page import="fcu.selab.progedu.status.*" %>

<%
	if(session.getAttribute("username") == null || session.getAttribute("username").toString().equals("")){
		response.sendRedirect("index.jsp");
	}
	session.putValue("page", "dashboard");
	
	int userId = Integer.parseInt(request.getParameter("userId"));
	String projectName = request.getParameter("proName");
%>

<%@ include file="language.jsp" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<style type="text/css">
		body, html {
			height: 100%;
			overflow-x: hidden;
		}
		#inline p {
		    display: inline;
		}
		#inline{
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
			position: absolute; /* Set the navbar to fixed position */
   			top: 0;
   			padding-top: 50px;
   		 	overflow-x:hidden;
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
		.ovol {
			border-radius: 50px;
			height: 50px;
            font-weight: bold;
            width: 120px;
            color: white;
            text-align: center;
		}
		html, body, .row, #navHeight {
			height: 100%;
		}
		#pProject a{
			width:1px;
			height:1px;
		}
		.tableActive {
			background-color: #ddd;
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
        .CPF {
            background: #e52424;
        }
        .S {
            background: #5fa7e8;
        }
        .NB {
            background: #878787;
        }
        .CSF {
            background: gold;
        }
        .CTF {
            background: #32CD32;
        }
        #container{
		  width: 100%;
		  height: 500px;
		  margin: 10px; 
		  background: #fff3cd;
		}
	</style>
	
	<link rel="shortcut icon" href="img/favicon.ico"/>
	<link rel="bookmark" href="img/favicon.ico"/>
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
		
		GitlabUser choosedUser = conn.getUserById(userId);
		List<GitlabProject> projects = conn.getProject(choosedUser);
		Collections.reverse(projects);
	%>
	<%@ include file="header.jsp" %>
		<!-- -----sidebar----- -->
		<div class="sidebar" style="width:200px">
			<ul class="nav flex-column" style="padding-top: 20px;">
          			<li class="nav-item">
       				<font size="4"><a href="javascript:;" data-toggle="collapse" data-target="#overview" class="nav-link"><i class="fa fa-bars" aria-hidden="true"></i>&nbsp; <%=choosedUser.getUsername() %> <i class="fa fa-chevron-down" aria-hidden="true"></i></a></font>
          				<ul id="overview" class="collapse" style="list-style: none;">
         			          <%
	 			           	for(GitlabProject project : projects){
	    			        	  for(Project dbProject : dbProjects){
	            	   			  if(project.getName().equals(dbProject.getName())){
	            	      			String href = "dashProjectChoosed.jsp?userId=" + choosedUser.getId() + "&proName=" + project.getName();
	            	      %>
	            	      				<li class="nav-item"><font size="3"><a class="nav-link" href=<%=href %>><i class="fa fa-angle-right" aria-hidden="true"></i>&nbsp; <%=project.getName() %></a></font></li>
	            	      <%
	            	    			}
	            	  			}
	            			}
	            		%>
           			</ul>
       			</li>
          			<li class="nav-item">
              			<font size="4"><a href="javascript:;" data-toggle="collapse" data-target="#student" class="nav-link"><i class="fa fa-bars" aria-hidden="true"></i>&nbsp; <fmt:message key="dashboard_a_student"/> <i class="fa fa-chevron-down" aria-hidden="true"></i></a></font>
              			<ul id="student" class="collapse show" style="list-style: none;">
                  			<%
            				for(User user : users){
            					String style = "";
	            	  			String userName = user.getUserName();
	            	  			String href = "\"dashStuChoosed.jsp?studentId=" + user.getGitLabId() + "\"";
	            	  			if(choosedUser.getUsername().equals(user.getUserName())) {
	            	 				style = "color: burlywood;";
	            	 			}
            	  		%>
            	  			<li class="nav-item"><font size="3"><a style="<%=style%>" class="nav-link" href=<%=href %>><i class="fa fa-angle-right" aria-hidden="true"></i>&nbsp; <%=userName %></a></font></li>
            	 		 <%
            				}
            			%>
              			</ul>
          			</li>
        			</ul>
		<!-- -----sidebar----- -->
	</div>
	<div class="container-fluid" id="main">
       	<h1 style="margin-bottom: 20px;"> <%=choosedUser.getUsername() %>_ <%=projectName %> </h1>
        <!-- ---------------------------- Project ------------------------------- -->
		<%
			ProjectDbManager pDb = ProjectDbManager.getInstance();
			Project project = pDb.getProjectByName(projectName);
		%>
		<div style="margin: 10px 10px 10px 10px;">
			<h2 style="white-space: nowrap"><i class="fa fa-pencil-square-o" aria-hidden="true"></i>&nbsp; <%=projectName%></h2>
			<hr>
			<h5 style="font-weight: 700"><fmt:message key="stuDashChooseProject_p_assignmentContent"/></h5>
			<p><%=project.getDescription() %></p>
			<hr>
			<h5 style="font-weight: 700"><fmt:message key="stuDashChooseProject_p_deadline"/></h5>
			<p><%=project.getDeadline() %></p>
			<hr>
		</div>
        <div class="card" style="padding:0; width: fit-content">
        	<h4 id="Student Projects" class="card-header"><i class="fa fa-table" aria-hidden="true"></i>&nbsp; Records</h4>
        	<div class="card-block">
        		<%@ include file="projectLight.jsp" %>
				<table class="table table-hover" style="margin-top: 20px; width: 100%; margin-bottom: 0px;">
					<thead>
						<tr>
							<th width="10%" class="text-center">Commit</th>
							<th width="10%">Light</th>
							<th width="15%">Date</th>
							<th>Commit Message</th>
						</tr>
					</thead>
					<tbody id="projectTbody">
						<%
							List<Integer> buildNum = stuDashChoPro.getScmBuildCounts(choosedUser.getUsername(), projectName);
							int commit_count = buildNum.size();
							int i=1;
							int lastBuildMessageNum = 0;
							for(Integer num : buildNum){
							  	%>
							  	<script type="text/javascript">
									var userName = <%="'" + choosedUser.getUsername() + "'"%>
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
											
											console.log(color)
											
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
							  	<tr id="<%=num %>" onClick="changeIframe(this)" class="<%=tableActive %>">
							  		<th width="10%" class="text-center"><%=i %></th>
							  		<td width="10%"><p id=<%="color" + num %>></p></td>
							  		<td width="15%" id=<%="date" + num %>></td>
							  		<td id=<%="message" + num %>></td>
							  	</tr>
							  	<%
							  	i++;
							  	lastBuildMessageNum = num;
							}
						%>
					</tbody>
				</table>
        	</div>
        </div>
        <!-- ---------------------------- Student Project ------------------------------- -->
        	<hr>
		
       		<!-- iFrame -->
			<%
				StudentDashChoosePro studentDashChoosePro = new StudentDashChoosePro();
				String color = studentDashChoosePro.getLastColor(choosedUser.getUsername(),projectName);
				Status status = StatusFactory.getStatus(color);
				int num = lastBuildMessageNum;
				String jobName = choosedUser.getUsername() + "_" + projectName;
				String jenkinsBuildNumUrl = jenkinsData.getJenkinsHostUrl() + "/job/" + jobName;
				String lastBuildUrl = jenkinsBuildNumUrl + "/" +  num + "/consoleText";
				String detailConsoleText = jenkins.getConsoleText(lastBuildUrl);
				String console = status.getConsole(detailConsoleText);
			%>
			<h4><a id="iFrameTitle" href="<%=jenkinsBuildNumUrl%>">Feedback Information (#<%=num %>)</a></h4>
			<!--  <div style="margin:10px;">
				<iframe src="<%=lastBuildUrl %>" width="100%" height="500px" style="background: #fff3cd;" id="jenkinsOutput">
			  		<p>Your browser does not support iframes.</p>
				</iframe>
			</div>-->
			<div id="container">
				<pre> <%=console%></pre>
			</div>
			<!-- iFrame -->
       </div>
<!-- ------------------------ main -------------------------------------- -->
	</body><script type="text/javascript">
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
			var u = '<%=jenkinsBuildNumUrl%>' + '/' + tr.id + '/consoleText';
			$('#jenkinsOutput').attr('src',u);
			document.getElementById("iFrameTitle").innerHTML = "Feedback Information (#" + tr.id + ")";
			$('#projectTbody tr').removeClass("tableActive");
			document.getElementById(tr.id).className = "tableActive";
		}
	</script>
</html>
