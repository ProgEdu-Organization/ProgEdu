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
					<h3 color="red">�A�ȳ��M�o�Ϳ��~</h3>
				</div>
				<div class="card-body">
					<th>
					<tr>
						<p>�O��A�u�{�v�V�O���פ�</p>
					</tr>
					<tr>
						<p>�s���q�� 0800-000-000</p>
					</tr>
					<tr>
						<p>�P�§A���ϥΡA�бz�@�ߵ���</p>
					</tr>
					<th>
				</div>
				<div style="display:inline-block;">
					<button id="goBack" type="button" class="btn btn-primary" style="width:49%">��^�W�@��</button>
					<button id="logout" type="button" class="btn btn-primary" style="width:49%">�n�J</button>
				</div>
			</div>
			<p id="time"></p>
		</div>
	</div>
	<script>
		var time = 7;
		var timeout;

		function timedCount() {
			$("#time").html("�������|�۰��୶" + time + "  �y��b��");
			time -= 1;
			timeout = setTimeout("timedCount()", 1000);
			if (time == -1) {
				clearTimeout(timeout);
				window.location.replace("memberLogout.jsp")
			}
		}
		
		$("#goBack").click(function(){
			history.back();
		});
		
		$("#logout").click(function(){
			window.location.replace("memberLogout.jsp") ;
		});
	</script>
</body>
</html>