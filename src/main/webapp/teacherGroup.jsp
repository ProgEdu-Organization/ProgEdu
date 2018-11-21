<%@ page import="fcu.selab.progedu.conn.Conn, fcu.selab.progedu.conn.HttpConnect, fcu.selab.progedu.db.GroupDbManager"%>
<%@ page import="fcu.selab.progedu.conn.Language,fcu.selab.progedu.config.GitlabConfig, fcu.selab.progedu.data.Group" %>
<%@ page import="fcu.selab.progedu.jenkins.JenkinsApi, fcu.selab.progedu.conn.Language" %>
<%@ page import="fcu.selab.progedu.config.GitlabConfig" %>
<%@ page import="fcu.selab.progedu.config.JenkinsConfig" %>
<%@ page import="fcu.selab.progedu.db.UserDbManager, fcu.selab.progedu.db.ProjectDbManager" %>
<%@ page import="fcu.selab.progedu.data.User, fcu.selab.progedu.data.Project" %>   
<%@ page import="org.gitlab.api.GitlabAPI" %>
<%@ page import="org.gitlab.api.models.*" %>
<%@ page import="java.util.*, java.io.BufferedReader, java.io.InputStreamReader, java.io.IOException" %>
<%@ page import="org.json.JSONArray, org.json.JSONException, org.json.JSONObject" %>
<%@ page import="org.apache.http.client.HttpClient, org.apache.http.client.methods.HttpGet" %>
<%@ page import="org.apache.http.HttpEntity, org.apache.http.HttpResponse, org.apache.http.impl.client.DefaultHttpClient" %>


<%
	if(session.getAttribute("username") == null || session.getAttribute("username").toString().equals("")){
		response.sendRedirect("index.jsp");
	}
	session.putValue("page", "teacherGroup");
%>

<%@ include file="language.jsp" %>

<%
	Conn conn = Conn.getInstance();
	GitlabConfig gitData = GitlabConfig.getInstance();
	
	List<GitlabGroup> groups = conn.getGroups();
	Collections.reverse(groups);
	
	GroupDbManager gdb = GroupDbManager.getInstance();
	List<Group> dbGroups = gdb.listGroups();
	//String groupUrl = "/groups/";
		

	String groupId = request.getParameter("id"); // Get group id
	if(null == groupId && !groups.isEmpty()){
	  groupId = String.valueOf(groups.get(0).getId());
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><%@ page language="java" contentType="text/html; charset=BIG5"	pageEncoding="utf-8"%>
<html>
<head>
	<!-- <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css"> -->
	<style type="text/css">
		#inline li {
		    display: inline;
		}
	    .table th, .table td { 
	        border-top: none !important;
	        border-left: none !important;
	    }
		body, html, .row, #navHeight{
			height:100%;
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
		#groupMamber {
			padding: 5px !important;
		}
		#groupNameLink {
			color: #464a4c;
		}
		#groupMamber a{
			color: #464a4c;
			border-bottom: 1px solid #464a4c;
		}
	</style>
	
	<link rel="shortcut icon" href="img/favicon.ico"/>
	<link rel="bookmark" href="img/favicon.ico"/>
	<title>ProgEdu</title>
</head>
<body>
	<%@ include file="header.jsp" %>

       <div class="sidebar" style="width:200px;">
         <ul class="nav nav-pills flex-column" style="margin-top: 20px;">
           <%
           	for(GitlabGroup group : groups){
           	  String href = "\"teacherGroup.jsp?id=" + group.getId() + "\"";
           	  %>
           	  	<li class="nav-item"><font size="3"><a class="nav-link" href=<%=href %>><%=group.getName() %></a></font></li>
           	  <%
           	}
           %>
         </ul>
       </div>
      	<%
      		GitlabGroup groupChoosed = new GitlabGroup();
      		for(GitlabGroup group : groups){
      			  int id = Integer.valueOf(groupId);
      		  if(id == group.getId()){
      		    groupChoosed = group;
      		    break;
      		  }
      		}
      		String groupUrl = gitData.getGitlabHostUrl() + "/groups/" + groupChoosed.getName();
      	%>
      	<div class="container-fluid" id="main" style="width: auto;">
       	<h2><a id="groupNameLink" href="#" onclick="window.open('<%=groupUrl %>')"><%=groupChoosed.getName() %></a></h2>
       		<table class="table" style="margin-top: 20px;">
	        	<thead class="thead-default">
	        		<tr>
	        			<th width="20%"><font size="3"><fmt:message key="teacherGroup_th_project"/></font></th>
					    <th width="20%"><font size="3"><fmt:message key="teacherGroup_th_student"/></font></th>
	        		</tr>
	        	</thead>
	        	<tbody>
	        		<%
	        			List<GitlabProject> projects = conn.getGroupProject(groupChoosed);
	        			Collections.reverse(projects);
	        			List<GitlabGroupMember> groupMembers = conn.getGroupMembers(groupChoosed);
	        			Collections.reverse(groupMembers);
	        		%>
	        		<tr>
	        			<td>
	        				<table>
	        					<tr>
	        						<%
	        						List<JSONObject> jsons = new ArrayList<JSONObject>();
	        							for(GitlabProject project : projects){
	        							  String projectUrl = gitData.getGitlabHostUrl() + "/" + groupChoosed.getName() + "/" + project.getName();
	        							  String commitsUrl = gitData.getGitlabHostUrl() + "projects/" + project.getId() + "/repository/commits?private_token=" + gitData.getGitlabApiToken();
	        							  JSONObject json = new JSONObject();
	        							  JSONArray array = new JSONArray();
	        							  	%>
	        							  		<td id="groupMamber" width="20%"><p><a href="#" onclick="window.open('<%=projectUrl %>')"><%=project.getName() %></a></p></td>
	        							  	<%
	        							  json.put("projectName", project.getName());
	        							  List<GitlabCommit> commits = conn.getProjectCommits(project.getId());
	        							  for(GitlabGroupMember member : groupMembers) {
	        								if(member.getName().equals("Administrator")) {
			        						  continue;
			        						}
	        								int count=0;
	        								JSONObject memberJson = new JSONObject();
	        								  for(GitlabCommit commit : commits) {
	        									if(member.getUsername().equals(commit.getAuthorName())) {
	        									  count++;
	        									}
	        								  }
	        								  memberJson.put("authorName", member.getName());
	        								  memberJson.put("count", count);
	        								  array.put(memberJson);
	        							  }
	        							  json.put("member", array);
	        							  jsons.add(json);
	        							}
	       							%>
	       						</tr>
	       					</table>
	       				</td>
	     				<td>
	        				<table id="noborder">
	        					<%
	        					for(GitlabGroupMember member : groupMembers){
	        					  String userName = member.getUsername();
	        					  String personal_url = gitData.getGitlabHostUrl() + "/u/" + userName;
	        						if(member.getName().equals("Administrator")) {
        								continue;
        							}
	        						if(member.getAccessLevel().toString().equals("Master")) {
	        							%>
	        								<tr><td id="groupMamber"><h5><a href="#" onclick="window.open('<%=personal_url %>')"><i class="fa fa-flag" aria-hidden="true"></i>&nbsp; <%=member.getName() %></a></h5></td></tr>
	       								<%
	       							}else{
	       							  	continue;
	       							}
	       						}
	       						for(GitlabGroupMember member : groupMembers){
	       							String userName = member.getUsername();
	        					  	String personal_url = gitData.getGitlabHostUrl() + "/u/" + userName;
	       							if(member.getName().equals("Administrator")) {
       									continue;
       								}
        							if(member.getAccessLevel().toString().equals("Developer")) {
        								%>
			        					  	<tr><td id="groupMamber"><h6><a href="#" onclick="window.open('<%=personal_url %>')"><i class="fa fa-user" aria-hidden="true"></i>&nbsp; <%=member.getName() %></a></h6></td></tr>
			        					<%
	        						}
	        					}
	       					%>
	       				</table>
	       			</td>
				</tr>
        	</tbody>
        </table>
        <!-- Nav tabs -->
        <div class="card">
        	<div class="card-header">
        		<h4 id="Statistics Chart"><i class="fa fa-bar-chart" aria-hidden="true"></i>&nbsp; <fmt:message key="dashboard_li_chart"/></h4>
        	</div>
        	
        	<div class="card-block">
        		<ul class="nav nav-tabs" role="tablist">
				  <li class="nav-item">
				    <a class="nav-link active" data-toggle="tab" href="#chart1" role="tab">Chart1</a>
				  </li>
				</ul>
        		<!-- Tab panes -->
				<div class="tab-content text-center" style="margin-top: 10px">
				  <div class="tab-pane active" id="chart1" role="tabpanel">
				  	<div id="chart1Demo" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
				  	</div>
				</div>
        	</div>
        </div>
      	</div>
</body>

<!-- set Highchart colors -->
<script>
Highcharts.setOptions({
	 colors: ['#5fa7e8', '#e52424', '#FF5809', '#878787']
	})
</script>
<script type="text/javascript">
<%
List<String> names = new ArrayList<String>();
List<Integer> values = new ArrayList<Integer>();
for(JSONObject jsonObject : jsons){
	JSONArray members = jsonObject.getJSONArray("member");
	for (int i=0; i<members.length(); i++) {
		String name = members.getJSONObject(i).getString("authorName");
		int count = members.getJSONObject(i).getInt("count");
		names.add(name);
		values.add(count);
	}
}
String x = "var x=[";
int i = 0;
for(String name : names) {
	x += "'" + name + "'";
	if(i != names.size()-1) {
		x += ",";
	}
	i++;
}
x += "];";
out.println(x);
int j=0;
String s = "var s = [{ name: '上傳次數', data:[";
for(Integer c : values) {
	s += c;
	if(j != names.size()-1) {
		s += ",";
	}
	j++;
}
s += "]}]";
out.println(s);
%>
Highcharts.chart('chart1Demo', {
    chart: {
        type: 'column'
    },
    title: {
        text: '組員上傳次數累計'
    },
    subtitle: {
        text: ''
    },
    xAxis: { 
    	categories: x,
        crosshair: true
    },
    yAxis: {
        min: 0,
        title: {
            text: '次數'
        }
    },
    tooltip: {
        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
            '<td style="padding:0"><b>{point.y}</b></td></tr>',
        footerFormat: '</table>',
        shared: true,
        useHTML: true
    },
    plotOptions: {
        column: {
            pointPadding: 0.2,
            borderWidth: 0
        }
    },
    series: s
});
</script>
</html>