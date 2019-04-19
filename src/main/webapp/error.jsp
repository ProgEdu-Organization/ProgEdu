<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="BIG5"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="BIG5">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<title>Error Pages</title>
<style>
body {
	
}

.box {
	position: absolute;
	left: 50%;
	top: 50%;
	margin-left: -400px;
	margin-top: -200px;
}
</style>
</head>
<body onload="timedCount()" bgcolor="#BDC3C7">
	<div class="box">
		<div style="float: left">
			<img src="./img/error.jpg" class="rounded" />
		</div>
		<div style="float: left; padding-left: 20px;">
			<div class="card" >
				<div class="card-header">
					<h3 color="red">服務竟然發生錯誤</h3>
				</div>
				<div class="card-body">
					<th>
					<tr>
						<p>別急，工程師努力維修中</p>
					</tr>
					<tr>
						<p>連絡電話 0800-000-000</p>
					</tr>
					<tr>
						<p>感謝你的使用，請您耐心等候</p>
					</tr>
					<th>
				</div>
				<button type="button" class="btn btn-primary">返回登入頁面</button>
			</div>
			<p id="time"></p>
		</div>
	</div>
	<script>
		var time = 8;
		var timeout;

		function timedCount() {
			$("#time").html("此頁面會自動轉頁" + time + "  稍後在試");
			time -= 1;
			timeout = setTimeout("timedCount()", 1000);
			if (time == -1) {
				clearTimeout(timeout);
				location.href="memberLogout.jsp";
			}
		}
	</script>
</body>
</html>