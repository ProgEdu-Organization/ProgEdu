<%@ page language="java"%>
<%@ page import="fcu.selab.progedu.config.CourseConfig"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<%@ include file="language.jsp"%>

<c:url value="index.jsp" var="displayLan">
  <c:param name="Language" value="tw" />
</c:url>

<!DOCTYPE html>
<html>
<head>
<title>ProgEdu</title>
<meta charset="UTF-8">

<link rel="shortcut icon" href="img/favicon.ico" />
<link rel="bookmark" href="img/favicon.ico" />

<link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet"
  id="bootstrap-css">
<link rel="stylesheet"
  href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">  
  
<style type="text/css">
body {
	font-family: Microsoft JhengHei;
	background: url('./img/login-bg.jpg') no-repeat center
		center fixed;
	-webkit-background-size: cover;
	-moz-background-size: cover;
	-o-background-size: cover;
	background-size: cover;
	padding-top: 180px;
}

.login-form {
	margin-top: 60px;
}

form[role=login] {
	color: #5d5d5d;
	background: #f2f2f2;
	padding: 26px;
	border-radius: 10px;
	-moz-border-radius: 10px;
	-webkit-border-radius: 10px;
}

form[role=login] img {
	display: block;
	margin: 0 auto;
	margin-bottom: 35px;
}

form[role=login] input, form[role=login] button {
	font-size: 18px;
	margin: 16px 0;
}

form[role=login]>div {
	text-align: center;
}

#course-name {
  text-align: center;
}
</style>
<script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>


<script type="text/javascript">
  var Msg ='<%=session.getAttribute("enterError")%>';
  if (Msg != "null") {
    function alertName() {
      alert("Your username or password is incorrect!");
    }
  }
</script>
</head>

<body>
  <div class="container">
    <div class="row" id="pwd-container">
      <div class="col-md-4"></div>
      <div class="col-md-4">
        <section class="login-form">
        <form method="post" method="post" action="AfterEnter" role="login">
          <h4 id="course-name"><%=CourseConfig.getInstance().getCourseFullName()%></h4>  
          <input type="hidden" name="grant_type" value="password">
          <input type="text" name="username" placeholder="user name" required autofocus class="form-control input-lg"/> 
          <input type="password" name="password" class="form-control input-lg" id="password" placeholder="password" required />
          <button type="submit" name="go" class="btn btn-lg btn-primary btn-block">Sign in</button>
        </form>
        </section>
      </div>
      <div class="col-md-4"></div>
    </div>
  </div>
</body>
</html>
