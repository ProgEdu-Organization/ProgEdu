<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JavaStyle</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

<style type="text/css">
.div_left {
position:fixed;
z-index:20;
}
</style>

</head>
<body>
<div class="container-fluid">
<div class="row">
	<div class="col-2 div_left">
		<div id="list-example" class="list-group">
		  <a class="list-group-item list-group-item-action" href="#list-item-1">Formatting</a>
		  <a class="list-group-item list-group-item-action" href="#list-item-2">Naming</a>
		  <a class="list-group-item list-group-item-action" href="#list-item-3">Programming Practices</a>
		  <a class="list-group-item list-group-item-action" href="#list-item-4">Javadoc</a>
		</div>
		<p>此篇參考<a title="莫希爾手札" href="https://blog.mosil.biz/2014/05/java-style-guide/">莫希爾(Mosil) 手札</a></p>
		<p>只用來教學使用，不會有任何商業用途</p>
	</div>
	<div class="col-2">
	</div>
	<div class="col-10">
		<div data-spy="scroll" data-target="#list-example" data-offset="0" class="scrollspy-example">
		  <h2 id="list-item-1"></h2>
		  <pre><%@ include file="./formatting.jsp" %></pre>
		  <h2 id="list-item-2"></h2>
		  <pre><%@ include file="./naming.jsp" %></pre>
		  <h2 id="list-item-3"></h2>
		  <pre><%@ include file="./programmingPractices.jsp" %></pre>
		  <h2 id="list-item-4"></h2>
		  <pre><%@ include file="./jacadoc.jsp" %></pre>
		</div>
	</div>
</div>
</div>
<script>
$(document).ready(function(){
	$(".list-group-item").click(function(){
		$(this).addClass('active').siblings().removeClass('active');
	});
});
</script>
</body>
</html>