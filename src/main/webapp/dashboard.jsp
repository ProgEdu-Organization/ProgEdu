<%@ page language="java" contentType="text/html; charset=BIG5"
         pageEncoding="utf-8"%>
<%@ page import="fcu.selab.progedu.conn.Conn,fcu.selab.progedu.conn.HttpConnect" %>
<%@ page import="fcu.selab.progedu.jenkins.JenkinsApi, fcu.selab.progedu.conn.Language" %>
<%@ page import="fcu.selab.progedu.config.GitlabConfig" %>
<%@ page import="fcu.selab.progedu.config.JenkinsConfig" %>
<%@ page import="fcu.selab.progedu.db.UserDbManager, fcu.selab.progedu.db.ProjectDbManager" %>
<%@ page import="fcu.selab.progedu.data.User, fcu.selab.progedu.data.Project" %>
<%@ page import="org.gitlab.api.models.*" %>
<%@ page import="java.util.*, fcu.selab.progedu.conn.Dash" %>
<%@ page import="fcu.selab.progedu.jenkins.JobStatus" %>
<%@ page import="org.json.JSONArray, org.json.JSONException, org.json.JSONObject" %>

<%
    if(session.getAttribute("username") == null || session.getAttribute("username").toString().equals("")){
        response.sendRedirect("index.jsp");
    }
    session.putValue("page", "dashboard");
%>

<%@ include file="language.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <style type="text/css">
        /* Center the loader */
    #loadingBackground {
        position: absolute;
        top: 0;
        bottom: 0%;
        left: 0;
        right: 0%;
        background-color: rgba(0, 0, 0, 0.7);
        z-index: 9999;
        display: none;
        text-align: center;
        width: 100%;
        padding-top: 25px;
    }
    #loader {
        position: absolute;
        left: 50%;
        top: 50%;
        z-index: 9999;
        width: 150px;
        height: 150px;
        margin: -75px 0 0 -75px;
        border: 16px solid #f3f3f3;
        border-radius: 50%;
        border-top: 16px solid #3498db;
        width: 120px;
        height: 120px;
        -webkit-animation: spin 2s linear infinite;
        animation: spin 2s linear infinite;
    }

    @-webkit-keyframes spin {
        0% { -webkit-transform: rotate(0deg); }
        100% { -webkit-transform: rotate(360deg); }
    }

    @keyframes spin {
        0% { transform: rotate(0deg); }
        100% { transform: rotate(360deg); }
    }

    /* Add animation to "page content" */
    .animate-bottom {
        position: relative;
        -webkit-animation-name: animatebottom;
        -webkit-animation-duration: 1s;
        animation-name: animatebottom;
        animation-duration: 1s
    }

    @-webkit-keyframes animatebottom {
        from { bottom:-100px; opacity:0 }
        to { bottom:0px; opacity:1 }
    }

    @keyframes animatebottom {
        from{ bottom:-100px; opacity:0 }
        to{ bottom:0; opacity:1 }
    }
        body, html{
            height: 100%;
            overflow-x: hidden;
        }
        #allProject {
            margin: 10px 0px 0px 0px;
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
        .circle a {
            color: #fff;
            line-height: 30px;
        }
    </style>

    <link rel="shortcut icon" href="img/favicon.ico"/>
    <link rel="bookmark" href="img/favicon.ico"/>
    <title>ProgEdu4Web</title>
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="PUBLIC">
</head>
<body>
<%
    Conn conn = Conn.getInstance();

    HttpConnect httpConn = HttpConnect.getInstance();

    UserDbManager db = UserDbManager.getInstance();
    ProjectDbManager Pdb = ProjectDbManager.getInstance();

    // db users
    List<User> users = db.listAllUsers();

    // gitlab projects
    List<GitlabProject> gitProjects = new ArrayList<GitlabProject>();

    // db projects
    List<Project> dbProjects = Pdb.listAllProjects();

    // gitlab jenkins courseData
    GitlabConfig gitData = GitlabConfig.getInstance();
    JenkinsConfig jenkinsData = JenkinsConfig.getInstance();

    JenkinsApi jenkins = JenkinsApi.getInstance();
%>
<%@ include file="header.jsp" %>
<div id="loadingBackground" style="display: none">
    <div id="loader"></div>
</div>
<!-- -----sidebar----- -->
<div class="sidebar" style="width:200px;">
    <ul class="nav flex-column" style="padding-top: 20px;">
        <li class="nav-item">
            <font size="4"><a href="javascript:;" data-toggle="collapse" data-target="#overview" class="nav-link"><i class="fa fa-bars" aria-hidden="true"></i>&nbsp; <fmt:message key="dashboard_a_overview"/> <i class="fa fa-chevron-down" aria-hidden="true"></i></a></font>
            <ul id="overview" class="collapse" style="list-style: none;">
                <li class="nav-item"><font size="3"><a class="nav-link" href="#Student Projects"><i class="fa fa-table" aria-hidden="true"></i>&nbsp; <fmt:message key="dashboard_li_studentProjects"/></a></font></li>
                <li class="nav-item"><font size="3">
                    <a class="nav-link" href="dashboardChart.jsp">
                        <i class="fa fa-bar-chart" aria-hidden="true"></i>&nbsp;
                        <fmt:message key="dashboard_li_chart"/>
                    </a>
                </font></li>
            </ul>
        </li>
        <li class="nav-item">
            <font size="4"><a href="javascript:;" data-toggle="collapse" data-target="#student" class="nav-link"><i class="fa fa-bars" aria-hidden="true"></i>&nbsp; <fmt:message key="dashboard_a_student"/> <i class="fa fa-chevron-down" aria-hidden="true"></i></a></font>
            <ul id="student" class="collapse" style="list-style: none;">
                <%
                    for(User user : users){
                        String userName = user.getUserName();
                        String name = user.getName();
                        String href = "\"dashStuChoosed.jsp?studentId=" + user.getGitLabId() + "\"";
                %>
                <li class="nav-item"><font size="3"><a class="nav-link" href=<%=href %>><i class="fa fa-angle-right" aria-hidden="true"></i>&nbsp; <%=userName %>&nbsp; <%=name %></a></font></li>
                <%
                    }
                %>
            </ul>
        </li>
    </ul>
</div>
<!-- -----sidebar----- -->
<div class="container-fluid" id="main" style="width: auto;">
    <h1 style="margin-bottom: 20px;"><fmt:message key="dashboard_a_overview"/></h1>
    <!-- ---------------------------- Student Project ------------------------------- -->
    <div class="card" style="width: fit-content;">
        <h4 id="Student Projects" class="card-header"><i class="fa fa-table" aria-hidden="true"></i>&nbsp; <fmt:message key="dashboard_li_studentProjects"/></h4>
        <div class="card-block">
            <!-- <div id="inline">
				<p class="ovol gray" style="padding: 5px 10px; margin-left: 5px;"><fmt:message key="dashboard_p_compileNotYet"/></p>
				<p class="ovol red" style="padding: 5px 10px; margin-left: 5px;"><fmt:message key="dashboard_p_compileFail"/></p>
				<p class="ovol orange" style="padding: 5px 10px; margin-left: 5px;"><fmt:message key="dashboard_p_checkstyleFail"/></p>
				<p class="ovol green" style="padding: 5px 10px;"><fmt:message key="dashboard_p_plagiarism"/></p>
				<p class="ovol gold" style="padding: 5px 10px;"><fmt:message key="dashboard_p_unitTestFail"/></p>
				<p class="ovol blue" style="padding: 5px 10px;"><fmt:message key="dashboard_p_compileSuccess"/></p>
			</div> -->
            <%@ include file="projectLight.jsp" %>
            <table class="table table-striped" style="margin-top: 20px; width: 100%">
                <thead>
                <tr>
                    <th style="font-weight: 900; font-size: 18px"><fmt:message key="dashboard_th_studentId"/></th>
                    <%
                        for(Project project : dbProjects){
                    %>
                    <th style="font-weight: 900; font-size: 18px"><%=project.getName() %></th>
                    <%
                        }
                    %>
                </tr>
                </thead>
                <tbody id="dashboard">

                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">
    var projectCount = <%=dbProjects.size()%>;
    var projects = [];
    <% for (int i=0; i<dbProjects.size(); i++) { %>
            projects.push("<%=dbProjects.get(i).getName()%>");
    <% } %>

    $.ajax({
        url : 'webapi/commits/all',
        type : 'GET',
        async : true,
        cache : true,
        contentType: 'application/json; charset=UTF-8',
        success : function(responseText) {
            var result = JSON.parse(responseText);
            setData(result.result);
        },
        error : function(responseText) {
            console.log("False!");
        }
    });

    function setData(result) {
        var content = '';
        for (i in result) {
            var student = result[i];
            var userName = student.userName;
            var gitlabId = student.gitlabId;
            var commits = student.commits;
            var name = student.name;

            content = '<tr id="allProject">';
            content += '<td width="10%" id="allProject"><a href="dashStuChoosed.jsp?studentId=' + gitlabId + '">' + userName + "  " + name + '</a></td>';

            if(commits.length > 0){
                for(j in projects) {
                    var thName = projects[j];

                    for(k in commits) {
                        var pName;
                        var hw = commits[k];
                        var hwName = hw.hw;
                        var commit = hw.commit + 1;
                        var color = 'circle ' + hw.color;
                        if(thName == hwName) {
                            pName = hwName;
                            break;
                        }
                        else {
                            pName = 'N/A';
                        }
                    }

                    if(pName == undefined || pName == 'N/A') {
                        content += '<td style="padding: 10px 0px 0px 30px;">';
                        content += '<p>' + pName + '</p></td>';
                    } else {
                        pName = hwName;
                        content += '<td style="padding: 10px 0px 0px 30px;">';
                        content += '<p id="' + userName + '_' + hwName + '" class="' + color + '">';
                        content += '<a id="' + userName + '_' + hwName + '_commit" href="dashProjectChoosed.jsp?userId=' + gitlabId + '&proName=' + hwName + '">';
                        content += commit;
                        content += '</a>';
                        content += '</p></td>';
                    }
                }
                content += '</tr>';
                $('#dashboard').append(content)
            } else {
                for (var i=0; i<projectCount; i++) {
                    content += '<td style="padding: 10px 0px 0px 30px;">';
                    content += '<p>N/A</p></td>';
                }
                content += '</tr>';
                $('#dashboard').append(content)
            }
        }
        document.getElementById('loadingBackground').style.display = 'none';
    }
</script>
</html>