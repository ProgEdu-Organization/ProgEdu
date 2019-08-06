<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="utf-8"%>
<%@ page import="fcu.selab.progedu.db.ProjectDbManager, java.util.*, fcu.selab.progedu.data.Project" %>
<%@ page import="java.text.SimpleDateFormat, fcu.selab.progedu.service.ProjectService" %>
<%
	if(session.getAttribute("username") == null || session.getAttribute("username").toString().equals("")){
		response.sendRedirect("index.jsp");
	}
	session.putValue("page", "assignmentManagement");

	ProjectService projectService = new ProjectService();
	String courseName = projectService.getCourseName();
%>

<%@ include file="language.jsp" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<style type="text/css">
		body{
			overflow-x: scroll;
			height: fit-content;
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
		body{
			height: 100%;
		}
	</style>
	<script src="http://js.nicedit.com/nicEdit-latest.js" type="text/javascript"></script>
	<script type="text/javascript">bkLib.onDomLoaded(nicEditors.allTextAreas);</script>
	

	<link rel="shortcut icon" href="img/favicon.ico"/>
	<link rel="bookmark" href="img/favicon.ico"/>
	<title>ProgEdu</title>
</head>
<body id="body">
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
					location.reload();
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
					location.reload();
				}
			});
			return false;
		});
	});
	</script>
	<script type="text/javascript">
		function load(target) {
		    var name = target.name;
		    switch (name) {
				case 'new_btn':
				    var hw_name = document.getElementById('Hw_Name').value;
				    console.log(hw_name)
				    if(hw_name !== '' && hw_name !== undefined){
				    	$('#loadingBackground')[0].style.height = $(document).height() + 'px'; 
                        document.getElementById("loader").style.display = "block";
                        document.getElementById("loadingBackground").style.display = "block";
					}
				    break;
				case 'delete_btn':
                    var hw_name = document.getElementById('del_Hw_Name').value;
                    if(hw_name !== '' && hw_name !== undefined){
                    	$('#loadingBackground')[0].style.height = $(document).height() + 'px'; 
                        document.getElementById("loader").style.display = "block";
                        document.getElementById("loadingBackground").style.display = "block";
                    }
				    break;
				default:
					$('#loadingBackground')[0].style.height = $(document).height() + 'px'; 
                    document.getElementById("loader").style.display = "block";
                    document.getElementById("loadingBackground").style.display = "block";
                    break;
			}
		}
	</script>
	<script type="text/javascript">
		function sendDeleteId(a) {
			var id = a.id;
			document.getElementById("del_Hw_Name").value = id;
		}
	</script>
	<script type="text/javascript">
		function sendEditId(a) {
			var id = a.id;
			document.getElementById("Edit_Hw_Name").value = id;

			var deadline = document.getElementById(id + '_deadline').innerHTML;
			deadline = deadline.replace(' ', 'T');
            document.getElementById("edit_Hw_Deadline").value = deadline;

            var readMe = document.getElementById(id + '_readMe').innerHTML;
			nicEditors.findEditor('edit_Hw_README').setContent(readMe);
			
			$('#editHw_card')[0].style.display = 'block';
			$('#edited_name')[0].innerHTML = '<fmt:message key="teacherManageHW_hw_edit_modal_title"/> (' + id + ')';
		}
	</script>
	<script>
	$(document).ready(function() {
		$("#newHw").submit(function(evt) {
			evt.preventDefault();
			var formData = new FormData(this);
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
                    location.reload();
				}, 
				error : function(xhr, ajaxOptions, thrownError) {
					//alert("failed!");
                    console.log(xhr.status, ajaxOptions, thrownError);
					//location.reload();
					
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
				document.getElementById('mvn_no_checkstyle_download').style.display = 'none';
			}else if (type == 'Maven'){
				document.getElementById('mvn_download').style.display = '';
				document.getElementById('mvn_no_checkstyle_download').style.display = '';
				document.getElementById('java_download').style.display = 'none';
			}
		}
		function changeDisplay(target) {
			var name = target.name;
			switch (name) {
				case 'hw_list':
                    document.getElementById('hw_list').style.display = 'block';
                    document.getElementById('new_hw').style.display = 'none';
                    break;
				case 'distributeHW':
                    document.getElementById('hw_list').style.display = 'none';
                    document.getElementById('editHw_card').style.display = 'none';
                    document.getElementById('new_hw').style.display = 'block';
                    break;
				default:
				    break;

			}
        }
		
		function changeTab(target){
			var name = target.name;
			if(name == "JavaTab") {
				$("[name='fileRadio']")[1].checked = false
				$("[name='fileRadio']")[2].checked = false
			} else if(name == "AndroidTab") {
				$("[name='fileRadio']")[0].checked = false
				$("[name='fileRadio']")[1].checked = false
				$("[name='fileRadio']")[2].checked = false
			} else if(name == "WebTab") {
				$("[name='fileRadio']")[0].checked = false
				$("[name='fileRadio']")[1].checked = false
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
	
	<div class="container" style="margin-top: 30px; height: fit-content">
		<button class="btn btn-outline-secondary" name="hw_list" onclick="changeDisplay(this)">
			<fmt:message key="teacherManageHW_hw_list"/>
		</button>
		<button class="btn btn-outline-secondary" name="distributeHW" onclick="changeDisplay(this)">
			<fmt:message key="teacherManageHW_h3_distributeHW"/>
		</button>
		<div class="card" id="hw_list" style="margin-top: 20px;">
			<div class="card-header">
				<h4 ><strong><fmt:message key="teacherManageHW_hw_list"/></strong></h4>
			</div>
  			<div class="card-block" style="padding: 20px 20px 20px 20px;">
  				<table class="table table-striped">
  					<tr>
  						<th><fmt:message key="teacherManageHW_hw_name"/></th>
  						<th><fmt:message key="teacherManageHW_hw_createTime"/></th>
  						<th><fmt:message key="teacherManageHW_hw_deadline"/></th>
  						<th class="text-center"><fmt:message key="teacherManageHW_hw_edit"/></th>
  						<th class="text-center"><fmt:message key="teacherManageHW_hw_delete"/></th>
  					</tr>
  					<%
  					
					for(Project project : projects) {
					  String name = project.getName();
					  String deadline = project.getDeadline().replace("T", " ");
					  String createTime = project.getCreateTime();
					  String readMe = project.getDescription();
					  if(null == createTime) {
					    createTime = "";
					  }
					%>
  					<tr>
  						<td id=<%=name + "_name"%>><%=name %></td>
  						<td id=<%=name + "_createTime"%>><%=createTime %></td>
  						<td id=<%=name + "_deadline"%>><%=deadline %></td>
  						<td style="display: none;" id=<%=name + "_readMe"%>><%=readMe %></td>
  						<td class="text-center">
  							<a id="<%=name %>" onclick="sendEditId(this)">
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
		
	<!--New homework-->
		<div class="card" id="new_hw" style="margin-top: 20px; display: none">
			<div class="card-header">
				<h4><strong><fmt:message key="teacherManageHW_hw_edit_modal_title"/></strong></h4>
			</div>
			<div class="card-block" style="padding: 20px 20px 20px 20px;">
				<form id="newHw" style="margin-top: 10px;">
					<div class="form-group col-md-3" style="padding-left: 0px; max-width:100%">
						
						<label for="Hw_Name"><h4><i class="fa fa-minus" aria-hidden="true"></i>&nbsp; <fmt:message key="teacherManageHW_label_hwName"/></h4></label>
						<div name="Hw_limit">
						<input id="Hw_Name" type="text" class="form-control" name="Hw_Name" maxlength="10"   required="required" placeholder="eg. <%=courseName%>-HW1" required style="display:inline-block;max-width:fit-content"/>
                     	<span style="color:gray"><fmt:message key="teacherManageHW_hw_name_lenth_limit"/></span>	
                     	</div>			
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

					<form>
						<ul class="nav nav-tabs" role="tablist">
							<li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#java" role="tab" onclick="changeTab(this)" name="JavaTab">Java</a></li>
							<li class="nav-item"><a class="nav-link" data-toggle="tab" href="#android" role="tab" onclick="changeTab(this)" name="AndroidTab">Android</a></li>
							<li class="nav-item"><a class="nav-link" data-toggle="tab" href="#web" role="tab" onclick="changeTab(this)" name="WebTab">Web</a></li>
						</ul>
						<!-- Tab panes -->
						<div class="tab-content" style="margin-top: 20px; margin-left: 20px; width: fit-content;">
						
							<!-- java panes -->
							<div class="tab-pane active col-md-12" id="java" role="tabpanel">
								<div class="form-group" style="width: max-content; white-space: nowrap; display: list-item">
									<label for="fileRadio"><fmt:message key="teacherManageHW_label_zipradio"/>&nbsp; &nbsp; </label>
									<label class="radio-inline"><input type="radio" name="fileRadio" value="Javac" onclick="changeBotton(this)" checked>&nbsp; Javac&nbsp; &nbsp; </label>
									<label class="radio-inline"><input type="radio" name="fileRadio" value="Maven" onclick="changeBotton(this)">&nbsp; Maven</label>
									<br>
									<a href="JavacQuickStart.zip" class="btn btn-default" style="background-color:#F5F5F5; color: #292b2c; border-color: #ccc" id="java_download"><fmt:message key="teacherManageHW_a_downloadJavac"/></a>
									<a href="MvnQuickStart.zip" class="btn btn-default" style="background-color:#F5F5F5; color: #292b2c; border-color: #ccc; display: none;" id="mvn_download"><fmt:message key="teacherManageHW_a_downloadMaven"/></a>
									<a href="MvnQuickStartWithoutCheckstyle.zip" class="btn btn-default" style="background-color:#F5F5F5; color: #292b2c; border-color: #ccc; display: none;" id="mvn_no_checkstyle_download"><fmt:message key="teacherManageHW_a_downloadMavenWithoutCheckstyle"/></a>
								</div>
							</div>
						
							<!-- android panes -->
							<div class="tab-pane col-md-12" id="android" role="tabpanel">
							</div>
						
							<!-- web panes -->
							<div class="tab-pane col-md-12" id="web" role="tabpanel">
								<div class="form-group" style="width: max-content; white-space: nowrap; display: list-item">
									<div style = "display:none">
									<label for="fileRadio"><fmt:message key="teacherManageHW_label_zipradio"/>&nbsp; &nbsp; </label>						
									<label class="radio-inline"><input type="radio" name="fileRadio" value="Web" onclick="changeBotton(this)" checked>&nbsp; Web&nbsp; &nbsp; </label>
									</div>
									<a href="WebQuickStart.zip" class="btn btn-default" style="background-color:#F5F5F5; color: #292b2c; border-color: #ccc" id="java_download">
									<fmt:message key="teacherManageHW_a_downloadWeb"/></a>
								</div>
							</div>
							
							<div class="form-group" style="width: max-content; display: list-item; margin-left: 14px;">
								<label for="file"><fmt:message key="teacherManageHW_label_uploadZip"/></label>
								<br>
								<input type="file" accept=".zip" name="file" size="50" width="48"/>
							</div>
						</div>
					
						<button type="submit" class="btn btn-info" name="new_btn" onclick="load(this);nicEditors.findEditor('Hw_README').saveContent();">
							<fmt:message key="teacherManageHW_button_send"/>
						</button>
					</form>				
				</form>
			</div>
		</div>
		  <!-- Edit Modal -->
	  <div class="card" id="editHw_card" style="margin-top: 20px; display: none;">
	  	<div class="card-header">
	  		<h4 id="edited_name"><strong><fmt:message key="teacherManageHW_hw_edit_modal_title"/></strong></h4>
	  	</div>
		      
	    <div class="card-block"style="padding: 20px 20px 20px 20px;">
		    <form id="editHw" style="margin-top: 10px;">
		    	<input type="hidden" id="Edit_Hw_Name" name="Edit_Hw_Name"/>
		    	<div class="form-group" style="width: fit-content">
				<label for="Hw_Deadline"><h4><i class="fa fa-minus" aria-hidden="true"></i>&nbsp; <fmt:message key="teacherManageHW_label_hwDeadline"/></h4></label>
				<input id="edit_Hw_Deadline" type="datetime-local" class="form-control" name="Hw_Deadline"/>
				</div>
				<div class="form-group">
					<label for="Hw_README"><h4><i class="fa fa-minus" aria-hidden="true"></i>&nbsp; <fmt:message key="teacherManageHW_label_hwReadme"/></h4></label>
					<div class="form-group" style="width: fit-content">
						<textarea id="edit_Hw_README" cols="100" rows="20" name="Hw_README" style="width: 823px; height: 200px;"></textarea>
					</div>
				</div>
				<div class="form-group" style="width: fit-content">
					<label for="Hw_TestCase"><h4><i class="fa fa-minus" aria-hidden="true"></i>&nbsp; <fmt:message key="teacherManageHW_label_hwTestCase"/></h4></label>
					<br>
					<input id="edit_Hw_TestCase" type="file" accept=".zip" name="Hw_TestCase" size="50" width="48"/>
				</div>
				<button type="submit" class="btn btn-primary" name="edit_btn" onclick="load(this); nicEditors.findEditor('edit_Hw_README').saveContent();">
					<fmt:message key="teacherManageGroup_button_send"/>
				</button>
	  		</form>
	 	</div>
	  </div>
	</div>
	
  <!-- Delete Modal -->
  <form id="deletetHw" style="margin-top: 10px;">
	<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModal" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h4 class="modal-title" id="deleteModal"><fmt:message key="teacherManageHW_hw_delete_modal_title"/></h4>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      
	      <div class="modal-body">
		      <div class="form-group">
		      	<h5><fmt:message key="teacherManageHW_hw_delete_modal_content"/></h5>
		      	<input type="hidden" id="del_Hw_Name" name="del_Hw_Name"/>
		      </div>
		  </div>
		  
	      <div class="modal-footer">
	        <button type="button" class="btn btn-primary" data-dismiss="modal">
				<fmt:message key="teacherManageGroup_button_close"/>
			</button>
			<button type="submit" class="btn btn-secondary" onclick="load(this);">
				<fmt:message key="teacherManageHW_hw_delete_modal_botton"/>
			</button>
	      </div>
	      
	    </div>
	  </div>
	</div>
  </form>
</body>
</html>