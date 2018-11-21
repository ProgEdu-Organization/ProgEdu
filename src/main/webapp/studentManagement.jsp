<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="utf-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
	if(session.getAttribute("username") == null || session.getAttribute("username").toString().equals("")){
		response.sendRedirect("index.jsp");
	}
	session.putValue("page", "studentManagement");
%>

<%@ include file="language.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<style>
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
  			z-index: 1;
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
	</style>
	
	<link rel="shortcut icon" href="img/favicon.ico"/>
	<link rel="bookmark" href="img/favicon.ico"/>
	<title>ProgEdu</title>
</head>

<body  style="background-color:#F5F5F5;">
	
	<%@ include file="header.jsp" %>
	
<script>
	$(document).ready(function() {
		$("#addAllStudent").submit(function(evt) {
			evt.preventDefault();
			var formData = new FormData($(this)[0]);
			$.ajax({
				url : 'webapi/user/upload',
				type : 'POST',
				data : formData,
				async : true,
				cache : false,
				contentType : false,
				enctype : 'multipart/form-data',
				processData : false,
				success : function(response) {
					alert("uploaded!");
					top.location.href = "studentManagement.jsp";
				}, 
				error : function(a, b, c) {
				    console.log(a.status, b, c)
					alert("failed!");
                    location.reload();
				}
			});
			return false;
		});
	});
    $(document).ready(function() {
        $("#newStudent").submit(function(evt) {
            evt.preventDefault();
            var formData = new FormData($(this)[0]);
            $.ajax({
                url : 'webapi/user/new',
                type : 'POST',
                data : formData,
                async : true,
                cache : false,
                contentType : false,
                enctype : 'multipart/form-data',
                processData : false,
                success : function(response) {
                    alert("uploaded!");
                    top.location.href = "studentManagement.jsp";
                },
                error : function(a, b, c) {
                    console.log(a.status, b, c)
                    alert("failed!");
                    // location.reload();
                }
            });
            return false;
        });
    });
</script>
	
	<script type="text/javascript">
        function load() {
            document.getElementById("loader").style.display = "block";
            document.getElementById("loadingBackground").style.display = "block";
        }
	</script>
	<div id="loadingBackground" style="display: none">
		<div id="loader"></div>
	</div>
	
	<div>
		<div class="container-fluid" style="margin-top: 20px; width: 1140px;">
			<br>
			<div>
				<div class="card">
					<h4 class="card-header"><strong><fmt:message key="teacherManageStudent_h3_newAllStudent"/></strong></h4>
					
					<div class="col-md-3" style="padding-top: 20px;">
						<a href="StudentTemplate.csv" class="btn btn-default" style="background-color:#F5F5F5; color: #292b2c; border-color: #ccc">
							<fmt:message key="teacherManageStudent_a_downloadEnrollmentTemplate"/>
						</a>
					</div>
					<div class="card-block" style="padding: 20px 20px 20px 20px;">
						<div class="form-group">
							<form id="addAllStudent">
								<h5><i class="fa fa-file-excel-o" aria-hidden="true"></i>&nbsp; <fmt:message key="teacherManageStudent_h4_uploadStudent"/></h5>
								Select File to Upload:
								<input type="file" name="file" style="margin-left: 10px;">
								<br>
								<input type="submit" class="btn btn-light" style="border: gray solid 1px" value="Upload" onclick="load();" style="margin-top:10px;">
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div>
		<div class="container-fluid" style="margin-top: 20px; width: 1140px;">
			<br>
			<div>
				<div class="card">
					<h4 class="card-header"><strong><fmt:message key="teacherManageStudent_h3_newAStudent"/></strong></h4>
					<div class="card-block" style="padding: 20px 20px 20px 20px;">
						<div class="form-group">
							<form id="newStudent">
								<div class="form-group row" style="text-align: right">
									<label class="col-sm-2 col-form-label">Student's name</label>
									<div class="col-sm-3">
										<input type="text" class="form-control" name="studentName">
									</div>
								</div>
								<div class="form-group row" style="text-align: right">
									<label class="col-sm-2 col-form-label">Student's Id</label>
									<div class="col-sm-3">
										<input type="text" class="form-control" name="studentId">
									</div>
								</div>
								<div class="form-group row" style="text-align: right">
									<label class="col-sm-2 col-form-label">Student's email</label>
									<div class="col-sm-3">
										<input type="text" class="form-control" name="studentEmail">
									</div>
								</div>
								<div class="form-group">
									<input type="submit" class="btn btn-light" style="border: gray solid 1px;" value="Submit" onclick="load();" style="margin-top:10px;">
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>