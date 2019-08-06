<%@ page language="java" contentType="text/html; charset=BIG5" pageEncoding="utf-8"%>
<%@ page import="fcu.selab.progedu.conn.Conn, fcu.selab.progedu.conn.StudentConn, fcu.selab.progedu.conn.HttpConnect"%>
<%@ page import="fcu.selab.progedu.config.GitlabConfig,fcu.selab.progedu.config.CourseConfig"%>
<%@ page import="fcu.selab.progedu.config.GitlabConfig,fcu.selab.progedu.config.JenkinsConfig"%>
<%@ page import="fcu.selab.progedu.jenkins.JenkinsApi"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.gitlab.api.GitlabAPI"%>
<%@ page import="org.gitlab.api.models.*"%>
<%@ page import="java.util.*"%>
<%@ page import="org.json.JSONArray, org.json.JSONException, org.json.JSONObject" %>
<%@ page import="fcu.selab.progedu.db.UserDbManager, fcu.selab.progedu.db.ProjectDbManager" %>
<%@ page import="fcu.selab.progedu.data.User, fcu.selab.progedu.data.Project" %>
<%@ page import="fcu.selab.progedu.jenkins.JobStatus, java.text.SimpleDateFormat" %>

<%
	String private_token = null;
	if(null != session.getAttribute("private_token") && !"".equals(session.getAttribute("private_token")) ){
	  private_token = session.getAttribute("private_token").toString();
	}else{
	  response.sendRedirect("index.jsp");
	}
	session.putValue("page", "sutdentDashboardChooseProject");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>

	<link rel="shortcut icon" href="img/favicon.ico"/>
	<link rel="bookmark" href="img/favicon.ico"/>
</head>
<body>
<%
		String projectIdSession = request.getParameter("projectId");
		int projectId = -1;
		if(null == projectIdSession || "".equals(projectIdSession)){
			  
		}else {
			projectId = Integer.parseInt(projectIdSession);
		}
		GitlabConfig gitData = GitlabConfig.getInstance();
		CourseConfig courseData = CourseConfig.getInstance();
		JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
		Conn conn = Conn.getInstance();
		JenkinsApi jenkins = JenkinsApi.getInstance();
		
		StudentConn sConn = new StudentConn(private_token);
		GitlabUser user = sConn.getUser();
		List<GitlabProject> projects = sConn.getProject();
		Collections.reverse(projects);
		List<GitlabCommit> commits;

		int pro_total_commits = 0;
	%>
<div class="card" style="margin-top: 30px">
		        <h4 id="Student Projects" class="card-header">
		        	<i class="fa fa-table" aria-hidden="true"></i>&nbsp; 
		        		<fmt:message key="stuDashboard_card_statisticChart"/>
		        </h4>
		        <div class="card-block">
					<div id="chart1Demo" style="min-width: 310px; height: 350px; max-width: 525px; margin: 0 auto"></div>
				</div>
			</div>
			<%
		String projectName = "";
		String projectUrl = "";
		if(projectId != -1){
			GitlabProject project = sConn.getProjectById(projectId);
			projectName = project.getName();
			projectUrl = project.getHttpUrl();
		}
		GitlabProject choosedProject = new GitlabProject();
		for(GitlabProject project : projects){
		  if(projectName.equals(project.getName())){
		    choosedProject = project;
		  }
		}
		int commit_count = conn.getAllCommitsCounts(choosedProject.getId());
		commits = conn.getAllCommits(choosedProject.getId());
		Collections.reverse(commits);
		%>
</body>
<!-- set Highchart colors -->
<script>
Highcharts.setOptions({
	 colors: ['#5fa7e8', '#878787']
	})
</script>
<!-- chart1 -->
<script>
<%
UserDbManager db = UserDbManager.getInstance();
ProjectDbManager pDb = ProjectDbManager.getInstance();
List<User> users = db.listAllUsers();
List<GitlabProject> gitProjects = conn.getAllProjects();
List<Project> dbProjects = pDb.listAllProjects();

List<JSONObject> jsons = new ArrayList<JSONObject>();
String jobName = null;
String jobUrl = null;

for(User eachuser : users){
	String userName = eachuser.getUserName();
	Collections.reverse(gitProjects);
	//for(Project dbProject : dbProjects){
		JobStatus jobStatus = new JobStatus();
		commit_count = 0;
		for(GitlabProject gitProject : gitProjects){
			String fullName = eachuser.getName() + " / " + projectName;
			if(fullName.equals(gitProject.getNameWithNamespace())){
				JSONObject json = new JSONObject();
				json.put("name", projectName);
				int notCommitCount = 0;
				int commitCount = 0;
							
				commit_count = conn.getAllCommitsCounts(gitProject.getId());
				//---Jenkins---
				jobName = eachuser.getUserName() + "_" + gitProject.getName();
				jobStatus.setName(jobName);
				jobUrl = jenkinsData.getJenkinsHostUrl() + "/job/" + jobName + "/api/json";
				jobStatus.setUrl(jobUrl);
				
				// Get job status
				jobStatus.setJobApiJson();
				
				String color = null;
				if(null != jobStatus.getJobApiJson()){
					color = jenkins.getJobJsonColor(jobStatus.getJobApiJson());
				}
					
				if(commit_count == 1){
					notCommitCount++;
				} else {
					if(color!=null){
					  	if(color.equals("CPF") || color.equals("S") || color.equals("CSF") || color.equals("CTF")) {
					  		commitCount++;
					  	}
					}else{
						notCommitCount++;
					}
				}

				json.put("commitCount", commitCount);
				json.put("notCommitCount", notCommitCount);
				jsons.add(json);
				//-------------
				break;
			}
		}
	//}
}

List<String> names = new ArrayList<String>();
List<Integer> allCommits = new ArrayList<Integer>();
List<Integer> allNotCommits = new ArrayList<Integer>();

for(JSONObject json : jsons) {
	if(json.get("name").equals(projectName)) {
		if(!names.contains(json.get("name"))) {
			names.add(json.get("name").toString());
			allCommits.add(Integer.parseInt(json.get("commitCount").toString()));
			allNotCommits.add(Integer.parseInt(json.get("notCommitCount").toString()));
		}else {
			int index = names.indexOf(json.get("name"));
			int allCommit = allCommits.get(index) + Integer.parseInt(json.get("commitCount").toString());
			int allNotCommit = allNotCommits.get(index) + Integer.parseInt(json.get("notCommitCount").toString());
			
			allCommits.set(index, allCommit);
			allNotCommits.set(index, allNotCommit);
		}
	}
}
int j=0;
int commitTotal = 0;
int notCommitTotal = 0;

String s = "var s = [{name: 'Brands', colorByPoint: true, data: [{ name: '已繳交', y:";
for(int allCommit : allCommits) {
	commitTotal += allCommit;
}
s += commitTotal;
s += "}, { name: '未繳交', y:";
j = 0;
for(int allNotCommit : allNotCommits) {
	notCommitTotal += allNotCommit;
}
s += notCommitTotal;
s += "}]}]";
out.println(s);
%>
Highcharts.chart('chart1Demo', {
    chart: {
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false,
        type: 'pie'
    },
    title: {
        text: ''
    },
    tooltip: {
        pointFormat: '{series.name}: <b>{point.y}</b>'
    },
    plotOptions: {
        pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
                enabled: true,
                format: '<b>{point.name}</b>: {point.y}',
                style: {
                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                }
            }
        }
    },
    series: s
});
</script>
</html>