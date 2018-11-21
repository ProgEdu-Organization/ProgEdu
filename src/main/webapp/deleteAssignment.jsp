<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="utf-8"%>
<%@ page import="fcu.selab.progedu.db.ProjectDbManager, java.util.*, fcu.selab.progedu.data.Project" %>
<%@ page import="java.text.SimpleDateFormat" %>
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
		body{
			overflow-x: scroll;
		}
		#editModal{
			overflow-x: auto; !important
		}
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
		html, body{
			height: 100%;
		}
	</style>
	<script src="http://js.nicedit.com/nicEdit-latest.js" type="text/javascript"></script>
	<script type="text/javascript">bkLib.onDomLoaded(nicEditors.allTextAreas);</script>
	

	<link rel="shortcut icon" href="img/favicon.ico"/>
	<link rel="bookmark" href="img/favicon.ico"/>
	<title>ProgEdu</title>
</head>
<body>
	<%@ include file="header.jsp" %>
	<script>
	$(document).ready(function() {
		$("#deletetHw").submit(function(evt) {
			evt.preventDefault();
			var formData = new FormData($(this)[0]);
			$.ajax({
				url : 'webapi/project/delete',
				type : 'POST',
				data : formData,
				async : true,
				cache : false,
				contentType : false,
				enctype : 'multipart/form-data',
				processData : false,
				success : function(response) {
					alert("deleted!");
					top.location.href = "assignmentManagement.jsp";
				}, 
				error : function(response,A,B) {
					console.log(response,A,B);
					alert("failed!");
				}
			});
			return false;
		});
	});
	</script>
	<script>
	$(document).ready(function() {
		$("#editHw").submit(function(evt) {
			evt.preventDefault();
			var formData = new FormData($(this)[0]);
			$.ajax({
				url : 'webapi/project/edit',
				type : 'POST',
				data : formData,
				async : true,
				cache : false,
				contentType : false,
				enctype : 'multipart/form-data',
				processData : false,
				success : function(response) {
					alert("Edited!");
					top.location.href = "assignmentManagement.jsp";
				}, 
				error : function(response,A,B) {
					console.log(response,A,B);
					alert("failed!");
				}
			});
			return false;
		});
	});
	</script>
	<script type="text/javascript">
		function load() {
			document.getElementById("loadingBackground").style.display = "block";
			document.getElementById("loader").style.display = "block";
		}
	</script>
	<script type="text/javascript">
		function sendDeleteId(a) {
			var id = a.id;
			document.getElementById("Hw_Name").value = id;
		}
	</script>
	<script type="text/javascript">
		function sendEditId(a) {
			var id = a.id;
			document.getElementById("Edit_Hw_Name").value = id;
		}
	</script>
	<script>
	$(document).ready(function() {
		$("#newHw").submit(function(evt) {
			evt.preventDefault();
			var formData = new FormData($(this)[0]);
			$.ajax({
				url : 'webapi/project/create',
				type : 'POST',
				data : formData,
				async : true,
				cache : false,
				contentType : false,
				enctype : 'multipart/form-data',
				processData : false,
				success : function(response) {
					alert("uploaded!");
					top.location.href = "assignmentManagement.jsp";
				}, 
				error : function(response) {
					alert("failed!");
				}
			});
			return false;
		});
	});
	</script>
	<script>
		function changeBotton(target) {
			var type = target.value;
			if(type == 'Javac') {
				document.getElementById('java_download').style.display = '';
				document.getElementById('mvn_download').style.display = 'none';
			}else if (type == 'Maven'){
				document.getElementById('mvn_download').style.display = '';
				document.getElementById('java_download').style.display = 'none';
			}
		}
	</script>
	
	<%
		ProjectDbManager db = ProjectDbManager.getInstance();
		List<Project> projects = db.listAllProjects();
	%>
	
	<div id="loadingBackground" style="display: none">
		<div id="loader"></div>
	</div>
	
	<div class="container" style="margin-top: 30px; width: 1140px;">
		<button class="btn btn-default" data-toggle="modal" data-target="#newModal">
			<fmt:message key="teacherManageHW_h3_distributeHW"/>
		</button>
		<br>
		<br>
		<div class="card">
			<div class="card-header">
				<!-- <h4 ><strong><fmt:message key="deleteAssignment_h4"/></strong></h4> -->
				<h4 ><strong>作業列表</strong></h4>
			</div>
  			<div class="card-block" style="padding: 20px 20px 20px 20px;">
  				<table class="table table-striped">
  					<tr>
  						<th>作業名稱</th>
  						<th>建立日期</th>
  						<th>截止日期</th>
  						<th class="text-center">編輯</th>
  						<th class="text-center">刪除</th>
  					</tr>
  					<%
  					
					for(Project project : projects) {
						String name = project.getName().replace("T", " ");
						String deadline = project.getDeadline().replace("T", " ");
						String createTime = project.getCreateTime().replace("T", " ");
					%>
  					<tr>
  						<td><%=name %></td>
  						<td><%=createTime %></td>
  						<td><%=deadline %></td>
  						<td class="text-center">
  							<a id="<%=name %>" data-toggle="modal" data-target="#editModal" onclick="sendEditId(this)">
  								<i class="fa fa-lg fa-pencil-square-o" aria-hidden="true" style="line-height: 25px"></i>
  							</a>
  						</td>
  						<td class="text-center">
  							<a id="<%=name %>" data-toggle="modal" data-target="#deleteModal" onclick="sendDeleteId(this)">
  								<i class="fa fa-lg fa-times" aria-hidden="true" style="line-height: 25px; color: red;"></i>
  							</a>
  						</td>
  						</tr>
  					<%
  					}
  					%>
  				</table>
  			</div>
		</div>
	</div>
	
	
  <!-- Edit Modal -->
  <form id="editHw" style="margin-top: 10px;">
	<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModal" aria-hidden="true">
	  <div class="modal-dialog" role="document" style="max-width: 850px;">
	    <div class="modal-content" style="width: 850px">
	    
	      <div class="modal-header">
	        <h4 class="modal-title" id="editModal">編輯作業</h4>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      
	      <div class="modal-body" style="height: auto;">
	      	<input type="hidden" id="Edit_Hw_Name" name="Edit_Hw_Name"/>
		    <div class="form-group" style="width: fit-content">
				<label for="Hw_Deadline"><h4><i class="fa fa-minus" aria-hidden="true"></i>&nbsp; <fmt:message key="teacherManageHW_label_hwDeadline"/></h4></label>
				<input id="Hw_Deadline" type="datetime-local" class="form-control" name="Hw_Deadline"/>
			</div>
			<div class="form-group">
				<label for="Hw_README"><h4><i class="fa fa-minus" aria-hidden="true"></i>&nbsp; <fmt:message key="teacherManageHW_label_hwReadme"/></h4></label>
				<div class="form-group" style="width: fit-content">
					<textarea id="Hw_README" cols="100" rows="20" name="Hw_README" style="width: 823px; height: 200px;"></textarea>
				</div>
			</div>
		  </div>
		  
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">
				<fmt:message key="teacherManageGroup_button_close"/>
			</button>
			<button type="submit" class="btn btn-primary" onclick="load(); nicEditors.findEditor('Hw_README').saveContent();">
				<fmt:message key="teacherManageGroup_button_send"/>
			</button>
	      </div>
	      
	    </div>
	  </div>
	</div>
  </form>
  
  <!-- Delete Modal -->
  <form id="deletetHw" style="margin-top: 10px;">
	<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModal" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h4 class="modal-title" id="deleteModal">刪除作業</h4>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      
	      <div class="modal-body">
		      <div class="form-group">
		      	<h5>確定要刪除作業嗎？</h5>
		      	<input type="hidden" id="Hw_Name" name="Hw_Name"/>
		      </div>
		  </div>
		  
	      <div class="modal-footer">
	        <button type="button" class="btn btn-primary" data-dismiss="modal">
				<fmt:message key="teacherManageGroup_button_close"/>
			</button>
			<button type="submit" class="btn btn-secondary" onclick="load();">
				確定
			</button>
	      </div>
	      
	    </div>
	  </div>
	</div>
  </form>
  
  <!-- New Project Modal -->
  <form id="newHw" style="margin-top: 10px;">
	<div class="modal fade" id="newModal" tabindex="-1" role="dialog" aria-labelledby="newModal" aria-hidden="true" style="overflow-x: auto;">
		  <div class="modal-dialog" role="document"  style="max-width: 850px;">
		    <div class="modal-content" style="width: 850px">
		      <div class="modal-header">
		        <h4 class="modal-title" id="newModal"><strong><fmt:message key="teacherManageHW_h3_distributeHW"/></strong></h4>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      
		      <div class="modal-body">
				<div class="form-group">
					<label for="Hw_Name"><h4><i class="fa fa-minus" aria-hidden="true"></i>&nbsp; <fmt:message key="teacherManageHW_label_hwName"/></h4></label>
					<input id="Hw_Name" type="text" class="form-control" name="Hw_Name" required="required" placeholder="eg. OOP-HW1"/>
				</div>
				<%
					TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
					Date now = new Date();
					SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
					String datestring = sdFormat.format(now);
				%>
				<div class="form-group" style="width: fit-content">
					<label for="Hw_Deadline"><h4><i class="fa fa-minus" aria-hidden="true"></i>&nbsp; <fmt:message key="teacherManageHW_label_hwDeadline"/></h4></label>
					<input id="Hw_Deadline" type="datetime-local" class="form-control" name="Hw_Deadline" required="required" value=<%="\"" + datestring + "\""%>/>
				</div>				
				
				<div class="form-group">
					<label for="Hw_README"><h4><i class="fa fa-minus" aria-hidden="true"></i>&nbsp; <fmt:message key="teacherManageHW_label_hwReadme"/></h4></label>
					<textarea id="Hw_README" cols="100" rows="20" name="Hw_README" style="width: 823px; height: 200px;"></textarea>
				</div>
				
				<hr>
				
				<div>
					<ul class="nav nav-tabs" role="tablist">
						<li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#java" role="tab">Java</a></li>
						<li class="nav-item"><a class="nav-link" data-toggle="tab" href="#android" role="tab">Android</a></li>
						<li class="nav-item"><a class="nav-link" data-toggle="tab" href="#web" role="tab">Web</a></li>
					</ul>
					<!-- Tab panes -->
					<div class="tab-content" style="margin-top: 20px; margin-left: 20px; width: fit-content;">
						<div class="tab-pane active col-md-12" id="java" role="tabpanel">
							<div class="form-group" style="width: max-content; white-space: nowrap; display: list-item">
								<label for="fileRadio"><fmt:message key="teacherManageHW_label_zipradio"/>&nbsp; &nbsp; </label>
								<label class="radio-inline"><input type="radio" name="fileRadio" value="Javac" onclick="changeBotton(this)" checked>Javac&nbsp; &nbsp; </label>
								<label class="radio-inline"><input type="radio" name="fileRadio" value="Maven" onclick="changeBotton(this)">Maven</label>
								<br>
								<a href="JavacQuickStart.zip" class="btn btn-default" style="background-color:#F5F5F5; color: #292b2c; border-color: #ccc" id="java_download"><fmt:message key="teacherManageHW_a_downloadJavac"/></a>
								<a href="MvnQuickStart.zip" class="btn btn-default" style="background-color:#F5F5F5; color: #292b2c; border-color: #ccc; display: none;" id="mvn_download"><fmt:message key="teacherManageHW_a_downloadMaven"/></a>
							</div>
							<div class="form-group" style="width: max-content; display: list-item">
								<label for="file"><fmt:message key="teacherManageHW_label_uploadZip"/></label>
								<br>
								<input type="file" accept=".zip" name="file" size="50" width="48"/>
							</div>
						</div>
						<div class="tab-pane active col-md-12" id="android" role="tabpanel">
						</div>
						<div class="tab-pane active col-md-12" id="web" role="tabpanel">
						</div>
					</div>
			  </div>
			  
		      <div class="modal-footer">
		        <button type="button" class="btn btn-primary" data-dismiss="modal">
					<fmt:message key="teacherManageGroup_button_close"/>
				</button>
				<button type="submit" class="btn btn-secondary" onclick="load();nicEditors.findEditor('Hw_README').saveContent();">
					<fmt:message key="teacherManageHW_button_send"/>
				</button>
		      </div>
		      
		    </div>
		  </div>
		</div>
	</div>
  </form>
</body>
</html>