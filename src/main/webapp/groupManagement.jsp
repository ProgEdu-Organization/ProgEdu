<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="utf-8"%>
<%@ page import="fcu.selab.progedu.conn.Conn,fcu.selab.progedu.conn.Language" %>
<%@ page import="fcu.selab.progedu.db.UserDbManager, fcu.selab.progedu.data.User" %>
<%@ page import="fcu.selab.progedu.service.UserService" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.Locale" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
	if(session.getAttribute("username") == null || session.getAttribute("username").toString().equals("")){
		response.sendRedirect("index.jsp");
	}
	session.putValue("page", "groupManagement");
	
	UserDbManager uDB = UserDbManager.getInstance();
	List<User> users = uDB.listAllUsers();
%>

<%@ include file="language.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="shortcut icon" href="img/favicon.ico"/>
	<link rel="bookmark" href="img/favicon.ico"/>
<title>ProgEdu</title>
</head>

<body style="background-color:#F5F5F5;">
	
	<%@ include file="header.jsp" %>
<script>
	$(document).ready(function() {
		$('#upload').submit(function(evt) {
			evt.preventDefault();
			var formData = new FormData($(this)[0]);
			$.ajax({
				url : 'webapi/group/upload',
				type : 'POST',
				data : formData,
				async : false,
				cache : false,
				contentType : false,
				enctype : 'multipart/form-data',
				processData : false,
				success : function(response) {
					alert("Uploaded!");
					top.location.href = "groupManagement.jsp";
				}, 
				error : function(response) {
					alert("Failed! Check out whether there is the group with the same name on GitLab.");
				}
			});
			return false;
		});
	});
</script>
<script>
	$(document).ready(function() {
		$('#addMember').submit(function(evt) {
			evt.preventDefault();
			var formData = $(this).serialize();
			$.ajax({
				url : 'webapi/group/add',
				type : 'POST',
				data : formData,
				async : false,
				cache : false,
				contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
				processData : false,
				success : function(response) {
					alert("Added!");
					top.location.href = "groupManagement.jsp";
				}, 
				error : function(response) {
					alert("Failed!");
				}
			});
			return false;
		});
	});
</script>
	<div class="container" style="margin-top: 30px; width: 1140px;">
		<div class="card">
			<div class="card-header">
				<h4 ><strong><fmt:message key="teacherManageGroup_h3_newGroup"/></strong></h4>
			</div>
  			<div class="card-block" style="padding: 20px 20px 20px 20px;">
    			 <a href="webapi/group/export" class="btn btn-default" style="background-color:#F5F5F5; color: #292b2c; border-color: #ccc">
    			 	<i class="fa fa-download" aria-hidden="true"></i>
    			 	<fmt:message key="teacherManageGroup_a_exportStudent"/>
    			 </a>
    			 <form id="upload" name="upload" style="margin-top: 10px;">
					<button type="button" class="btn btn-default" style="background-color:#F5F5F5; color: #292b2c; border-color: #ccc" data-toggle="modal" data-target="#exampleModal" data-whatever="@mdo">
						<i class="fa fa-file-excel-o" aria-hidden="true"></i>
						<fmt:message key="teacherManageGroup_button_importStudent"/>
					</button>
					<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal">
										<span aria-hidden="true">&times;</span>
										<span class="sr-only">Close</span>
									</button>
									<h4 class="modal-title" id="exampleModalLabel"><fmt:message key="teacherManageGroup_h4_importStudent"/></h4>
								</div>

								<div class="modal-body">
									<div class="form-group">
										<h4><fmt:message key="teacherManageGroup_h4_uploadFile"/></h4>
										<input type="file" name="file" size="50" />
									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default" data-dismiss="modal">
										<fmt:message key="teacherManageGroup_button_close"/>
									</button>
									<button type="submit" class="btn btn-primary">
										<fmt:message key="teacherManageGroup_button_send"/>
									</button>
								</div>
							</div>
						</div>
					</div>
				</form>
  			</div>
		</div>
		<div class="card" style="margin-top: 30px">
			<div class="card-header">
				<h4 ><strong><fmt:message key="teacherManageGroup_addMember"/></strong></h4>
			</div>
  			<div class="card-block" style="padding: 20px 20px 20px 20px;">
    			<form id="addMember" name="select">
					<table>
						<tr>
							<td>
								<label for="groupName" style="font-size: 20px;"><fmt:message key="teacherManageGroup_groupName"/>: </label>
								<input type="text" id="groupName" name="groupName"/>
							</td>
						</tr>
						<tr class="col-md-12">
							<td>
								<select style="width: 100%; padding-top: 5px; padding-bottom: 5px; margin-top: 10px;" name="select1" id="select1" multiple size="<%=users.size()%>">
									<%
										for(User user : users){
									%>
											<option id="member" value="<%=user.getUserName()%>"><%=user.getUserName() %>-<%=user.getName() %></option>
									<%
										}
									%>
								</select>
							</td>
							<td>
								<input type="button" id="gt" name="gt" value="&gt;&gt;">
								<br>
								<input type="button" id="lt" name="lt" value="&lt;&lt;">
							</td>
							<td>
								<select style="width: 300px; padding-top: 5px; padding-bottom: 5px" name="select2" id="select2" multiple size="<%=users.size()+1%>">
								</select>
							</td>
						</tr>
						<tr>
							<td>
								<button type="submit" class="btn btn-default" style="margin-top: 10px"><fmt:message key="teacherManageHW_button_send"/></button>
							</td>
						</tr>
					</table>
				</form>
  			</div>
		</div>
	</div>
<script>
		$('#gt').click(function(e){
			$('#select1 option:selected').each(function(index){
				$(this).remove();
				$('#select2').append("<option selected id="+$(this).val()+" value="+$(this).val()+">"+$(this).text()+"</option>");
			})
	
		});
		$('#lt').click(function(e){
			$('#select2 option:selected').each(function(index){
				$(this).remove();
				$('#select1').append("<option id="+$(this).val()+" value="+$(this).val()+">" + $(this).text() + "</option>");
			})
	
		});
</script>
</body>
</html>