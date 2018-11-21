<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="utf-8"%>
 	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	 
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css" integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
	<script src="https://code.jquery.com/jquery-3.2.1.js" integrity="sha256-DZAnKJ/6XZ9si04Hgrsxu/8s717jcIzLy3oi35EouyE=" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4" crossorigin="anonymous"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js" integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1" crossorigin="anonymous"></script>
	<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
	<link href="font-awesome/progedu.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet"
	  href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
	 
	<style>
		body{
			padding-top: 45px;
			font-family: Microsoft JhengHei;
		}
		.container{
			padding-bottom: 30px;
		}
		button{
			font-family: -apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif;
		}
	</style>
	
	<%
		int userId = 0;
		Cookie[] cookiesUserId = request.getCookies();
		Cookie cooUserId = null;
		if(cookiesUserId != null){
		  for(Cookie c : cookiesUserId){
		    if(c.getName().equals("userId")){
		      cooUserId = c;
		      break;
		    }
		  }
		}
		if(cooUserId != null){
		  userId = Integer.valueOf(cooUserId.getValue());
		}
	%>
	
	<script>
	$(document).ready(function() {
		$("form").submit(function(evt) {
			evt.preventDefault();
			var formData = new FormData($(this)[0]);
			var oldPwd = document.getElementById("oldPwd").value;
			var newPwd = document.getElementById("newPwd").value;
			var checkPwd = document.getElementById("checkPwd").value;
			console.log(oldPwd);
			console.log(newPwd);
			console.log(checkPwd);
			if(oldPwd===newPwd){
				alert("Do not use old passwords!");
			}
			else if(newPwd!==checkPwd) {
				alert("The password does not match!");
			} else if(newPwd.length < 8) {
                alert("The password is too short (minimum is 8 characters)");
            }
            else
            {
				$.ajax({
					url : 'webapi/user/changePwd',
					type : 'POST',
					data : formData,
					async : true,
					cache : false,
					contentType : false,
					enctype : 'multipart/form-data',
					processData : false,
					success : function(response) {
						alert("Updated!");
						top.location.href = "index.jsp";
					},
					error : function(response) {
						alert("Failed! Please try again.");
					}
				});
			}
			return false;
		});
	});
	</script>
	
	<nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
    <a class="navbar-brand" href="studentDashboard.jsp">ProgEdu</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse"
      data-target="#navbarsExampleDefault" aria-controls="navbarsExampleDefault"
      aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarsExampleDefault">
      <ul class="navbar-nav mr-auto">
        <li class="nav-item active"><a class="nav-link" href="studentDashboard.jsp"><fmt:message key="top_navbar_dashboard"/></a>
        </li>
      </ul>
      <ul class="navbar-nav navbar-toggler-right">
        <li class="nav-item dropdown active"><a class="nav-link dropdown-toggle" href=""
          id="language" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><i class="fa fa-language" aria-hidden="true"></i>&nbsp;<fmt:message key="top_navbar_language"/></a>
          <div class="dropdown-menu" aria-labelledby="language">
            <a class="dropdown-item" href="studentDashboard.jsp?lang=zh"><i class="fa fa-globe" aria-hidden="true"></i>&nbsp;<fmt:message key="top_navbar_lanChinese"/></a>
          	<a class="dropdown-item" href="studentDashboard.jsp?lang=en"><i class="fa fa-globe" aria-hidden="true"></i>&nbsp;<fmt:message key="top_navbar_lanEnglish"/></a>
          </div>
        </li>
        <li class="nav-item active">
        	<a class="nav-link" data-toggle="modal" data-target="#exampleModal"><i class="fa fa-key" aria-hidden="true"></i> <fmt:message key="top_navbar_changePassword"/></a>
        </li>

        <li class="nav-item active">
        	<a class="nav-link" href="memberLogOut.jsp"><i class="fa fa-sign-out" aria-hidden="true"></i> <fmt:message key="top_navbar_signOut"/> </a>
        </li>
      </ul>
    </div>
  </nav>
  <form id="changePwd" name="upload" style="margin-top: 10px;">
  <!-- Modal -->
	<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	    
	      <div class="modal-header">
	        <h4 class="modal-title" id="exampleModalLabel">修改密碼</h4>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      
	      <div class="modal-body">
		      <div class="form-group">
				  <h5>目前密碼</h5>
				  	<input type="password" class="form-control" id="oldPwd" name="oldPwd" placeholder="Current password" required>
				  <hr>
				  <h5>新密碼</h5>
				  <input type="password" class="form-control" id="newPwd" name="newPwd" placeholder="New password" required>
				  <hr>
				  <h5>確認新密碼</h5>
				  <input type="password" class="form-control" id="checkPwd" name="checkPwd" placeholder="Password confirmation" required>
				  <input type="hidden" id="userId" name="userId" value="<%=userId%>">
		      </div>
		  </div>
		  
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">
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