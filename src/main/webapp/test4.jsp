<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>ProgEdu</title>
<!-- 
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
	<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
	<link href="font-awesome/progedu.css" rel="stylesheet" type="text/css">
	<script src="https://code.highcharts.com/highcharts.js"></script>
	<script src="https://code.highcharts.com/modules/data.js"></script>
	<script src="https://code.highcharts.com/modules/exporting.js"></script>
    -->

<link rel="stylesheet"
  href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
  integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M"
  crossorigin="anonymous">
<link rel="stylesheet"
  href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">

<style>
.axis .domain {
	display: none;
}
</style>

</head>
<body style="padding-top: 56px;">

  <nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
    <a class="navbar-brand" href="studentDashboard.jsp">ProgEdu</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse"
      data-target="#navbarsExampleDefault" aria-controls="navbarsExampleDefault"
      aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarsExampleDefault">
      <ul class="navbar-nav mr-auto">
        <li class="nav-item active"><a class="nav-link" href="studentDashboard.jsp"><fmt:message key="top_navbar_dashboard"/> <span
            class="sr-only">(current)</span></a></li>
        <li class="nav-item dropdown"><a class="nav-link dropdown-toggle" href=""
          id="dropdown01" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Setting</a>
          <div class="dropdown-menu" aria-labelledby="dropdown01">
            <a class="dropdown-item" href="#">Students</a> <a class="dropdown-item" href="#">Teams</a>
            <a class="dropdown-item" href="#">Assignments</a>
          </div></li>
      </ul>
      <ul class="navbar-nav navbar-toggler-right">
        <li class="nav-item dropdown active"><a class="nav-link dropdown-toggle" href=""
          id="dropdown01" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><fmt:message key="top_navbar_language"/></a>
          <div class="dropdown-menu" aria-labelledby="dropdown01">
            <a class="dropdown-item" href="ChooseLanguage?language=zh"><fmt:message key="top_navbar_lanChinese"/></a>
          	<a class="dropdown-item" href="ChooseLanguage?language=en"><fmt:message key="top_navbar_lanEnglish"/></a>
          </div></li>
        <li class="nav-item active"><a class="nav-link" href="memberLogOut.jsp"> <fmt:message key="top_navbar_signOut"/> <i
            class="fa fa-sign-out" aria-hidden="true"></i></a></li>
      </ul>
    </div>
  </nav>

  <table style="width: 100%; height: 100%;">
    <tr>
      <td style="width: 250px; background-color: #444; color: white;">
        <div style="height: 100%">
          <div style="margin: 15px 15px 15px 30px; color: burlywood">
            <i class="fa fa-bar-chart" aria-hidden="true"></i> Overview
          </div>
          <div style="margin: 15px">
            <i class="fa fa-minus-square-o" aria-hidden="true"></i> Assignments
          </div>
          <div style="margin: 15px 15px 15px 30px">
            <i class="fa fa-pencil-square-o" aria-hidden="true"></i> OOP-HW1
          </div>
          <div style="margin: 15px 15px 15px 30px">
            <i class="fa fa-pencil-square-o" aria-hidden="true"></i> OOP-HW2
          </div>
          <div style="margin: 15px 15px 15px 30px">
            <i class="fa fa-pencil-square-o" aria-hidden="true"></i> OOP-HW3
          </div>
          <div style="margin: 15px 15px 15px 30px">
            <i class="fa fa-pencil-square-o" aria-hidden="true"></i> OOP-HW4
          </div>
          <div style="margin: 15px 15px 15px 30px">
            <i class="fa fa-pencil-square-o" aria-hidden="true"></i> OOP-HW5
          </div>
          
        </div>
      </td>

      <td style="background-color: #f5f5f5">
        <div class="container-fluid" style="height: 100%">
          <div style="margin: 20px 0px">
            <h2>
              <i class="fa fa-bar-chart" aria-hidden="true"></i> Overview of Assignments
            </h2>
          </div>
          <div style="margin: 25px 0px">
            <span
              style="margin: 3px; padding: 5px; background-color: #9D9D9D; color: white; border-radius: 5px;">Not
              Submit</span> <span
              style="margin: 3px; padding: 5px; background-color: #CE0000; color: white; border-radius: 5px;">Compilation
              Failure</span> <span
              style="margin: 3px; padding: 5px; background-color: #FF5809; color: white; border-radius: 5px;">Plagiarism</span>
            <span
              style="margin: 3px; padding: 5px; background-color: #984B4B; color: white; border-radius: 5px;">Unit
              Test Failure</span> <span
              style="margin: 3px; padding: 5px; background-color: #6F00D2; color: white; border-radius: 5px;">Coding
              Convention Failure</span> <span
              style="margin: 3px; padding: 5px; background-color: #00A600; color: white; border-radius: 5px;">Success</span>
          </div>

          <table class="table table-striped">
            <thead>
              <tr>
                <th>Student Id</th>
                <th>OOP-HW1</th>
                <th>OOP-HW2</th>
                <th>OOP-HW3</th>
                <th>OOP-HW4</th>
                <th>OOP-HW5</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <th scope="row">D0437516</th>
                <td><span
                  style="margin: 3px; padding: 8px; background-color: #00A600; color: white; border-radius: 5px;">3</span></td>
                <td><span
                  style="margin: 3px; padding: 8px; background-color: #00A600; color: white; border-radius: 5px;">8</span></td>
                <td><span
                  style="margin: 3px; padding: 8px; background-color: #00A600; color: white; border-radius: 5px;">9</span></td>
                <td><span
                  style="margin: 3px; padding: 8px; background-color: #6F00D2; color: white; border-radius: 5px;">12</span></td>
                <td><span
                  style="margin: 3px; padding: 8px; background-color: #9D9D9D; color: white; border-radius: 5px;">0</span></td>
              </tr>
              
            </tbody>
          </table>
          <!--  <svg width="670" height="400"></svg> -->
        </div>
      </td>
    </tr>
  </table>

  <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
    integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
    crossorigin="anonymous"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"
    integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4"
    crossorigin="anonymous"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"
    integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1"
    crossorigin="anonymous"></script>
  <script src="https://d3js.org/d3.v4.min.js"></script>
  
  <script>
      var svg = d3.select("svg"), margin = {
        top : 20,
        right : 180,
        bottom : 30,
        left : 40
      }, width = +svg.attr("width") - margin.left - margin.right, height = +svg
          .attr("height")
          - margin.top - margin.bottom, g = svg.append("g").attr(
          "transform",
          "translate(" + margin.left + "," + margin.top + ")");

      var x0 = d3.scaleBand().rangeRound([ 0, width ]).paddingInner(0.1);

      var x1 = d3.scaleBand().padding(0.05);

      var y = d3.scaleLinear().rangeRound([ height, 0 ]);

      var z = d3.scaleOrdinal().range(
          [ "#98abc5", "#6b486b", "#d0743c"]);

      d3
          .csv("data4.csv", function(d, i, columns) {
            for (var i = 1, n = columns.length; i < n; ++i)
              d[columns[i]] = +d[columns[i]];
            return d;
          },
              function(error, data) {
                if (error)
                  throw error;

                var keys = data.columns.slice(1);

                x0.domain(data.map(function(d) {
                  return d.State;
                }));
                x1.domain(keys).rangeRound(
                    [ 0, x0.bandwidth() ]);
                y.domain([ 0, d3.max(data, function(d) {
                  return d3.max(keys, function(key) {
                    return d[key];
                  });
                }) ]).nice();

                g.append("g").selectAll("g").data(data).enter()
                    .append("g").attr(
                        "transform",
                        function(d) {
                          return "translate("
                              + x0(d.State)
                              + ",0)";
                        }).selectAll("rect").data(
                        function(d) {
                          return keys.map(function(
                              key) {
                            return {
                              key : key,
                              value : d[key]
                            };
                          });
                        }).enter().append("rect").attr(
                        "x", function(d) {
                          return x1(d.key);
                        }).attr("y", function(d) {
                      return y(d.value);
                    }).attr("width", x1.bandwidth()).attr(
                        "height", function(d) {
                          return height - y(d.value);
                        }).attr("fill", function(d) {
                      return z(d.key);
                    });

                g.append("g").attr("class", "axis").attr(
                    "transform",
                    "translate(0," + height + ")").call(
                    d3.axisBottom(x0));

                g.append("g").attr("class", "axis").call(
                    d3.axisLeft(y).ticks(null, "s"))
                    .append("text").attr("x", 2).attr("y",
                        y(y.ticks().pop()) + 0.5).attr(
                        "dy", "0.32em").attr("fill",
                        "#000").attr("font-weight",
                        "bold").attr("text-anchor",
                        "start").text("Submissions");

                var legend = g.append("g").attr("font-family",
                    "sans-serif").attr("font-size", 14)
                    .attr("text-anchor", "end").selectAll(
                        "g").data(
                        keys.slice().reverse()).enter()
                    .append("g").attr(
                        "transform",
                        function(d, i) {
                          return "translate(0," + i
                              * 20 + ")";
                        });

                legend.append("rect").attr("x", width + 155)
                    .attr("width", 19).attr("height", 19)
                    .attr("fill", z);

                legend.append("text").attr("x", width + 150)
                    .attr("y", 9.5).attr("dy", "0.32em")
                    .text(function(d) {
                      return d;
                    });
              });
    </script>
</body>
</html>