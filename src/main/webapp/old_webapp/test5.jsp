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
.line {
	fill: none;
	stroke: steelblue;
	stroke-width: 2px;
}

.area {
	fill: lightsteelblue;
}

.circle {
	width: 20px;
	height: 20px;
	-moz-border-radius: 10px;
	-webkit-border-radius: 10px;
	border-radius: 10px;
}
</style>

</head>
<body style="padding-top: 56px;">

  <nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
    <a class="navbar-brand" href="#">ProgEdu</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse"
      data-target="#navbarsExampleDefault" aria-controls="navbarsExampleDefault"
      aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarsExampleDefault">
      <ul class="navbar-nav mr-auto">
        <li class="nav-item active"><a class="nav-link" href="#">Dashboard <span
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
          id="dropdown01" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Language</a>
          <div class="dropdown-menu" aria-labelledby="dropdown01">
            <a class="dropdown-item" href="#">Students</a> <a class="dropdown-item" href="#">Teams</a>
            <a class="dropdown-item" href="#">Assignments</a>
          </div></li>
        <li class="nav-item active"><a class="nav-link" href="#"> Sign Out <i
            class="fa fa-sign-out" aria-hidden="true"></i></a></li>
      </ul>
    </div>
  </nav>

  <table style="width: 100%; height: 100%;">
    <tr>
      <td style="width: 250px; background-color: #444; color: white;">
        <div style="height: 100%">
          <div style="margin: 15px 15px 15px 30px;">
            <i class="fa fa-bar-chart" aria-hidden="true"></i> Overview
          </div>
          <div style="margin: 15px">
            <i class="fa fa-minus-square-o" aria-hidden="true"></i> Assignments
          </div>
          <div style="margin: 15px 15px 15px 30px; color: burlywood">
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
              <i class="fa fa-pencil-square-o" aria-hidden="true"></i> OOP-HW1
            </h2>
          </div>
          <div style="margin: 25px 0px">
            <div class="form-group">
              <label for="exampleInputEmail1">Git Repository</label> <input type="text"
                class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp"
                placeholder="http://mselab.iecs.fcu.edu.tw:20080/D0437516/OOP-HW1.git"> <small
                id="emailHelp" class="form-text text-muted">Please clone this git repository
                to your local workspace. </small>
            </div>

            <hr>

            <div class="container" style="margin: 25px 0px;">
              <div class="row">
                <div class="col-3">
                  <h4 style="text-align: center">Code Analysis Result</h4>
                  <div style="margin: 10px; text-align: center">
                    <div style="padding: 5px;">
                      <h3
                        style="width: 90px; margin: 0 auto; padding: 20px; background-color: #00A600; color: white;">3</h3>
                    </div>
                  </div>
                </div>
                <div class="col-9">
                  <h4>Programming History</h4>
                  <table class="table table-hover" style="background-color: white">
                    <thead>
                      <tr>
                        <th>#</th>
                        <th>Status</th>
                        <th>Date</th>
                        <th>Comment</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <th scope="row">1</th>
                        <td><div class="circle" style="background-color: #CE0000;"></div></td>
                        <td>2017-03-11 10:23</td>
                        <td>1st submission</td>
                      </tr>
                      <tr>
                        <th scope="row">2</th>
                        <td><div class="circle" style="background-color: #6F00D2;"></div></td>
                        <td>2017-03-11 11:45</td>
                        <td>Fix compilation errors</td>
                      </tr>
                      <tr>
                        <th scope="row">3</th>
                        <td><div class="circle" style="background-color: #00A600;"></div></td>
                        <td>2017-03-12 09:07</td>
                        <td>Fix coding convention violations</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>

          <hr>

          <h4>Feedback Information (#2)</h4>
          <small>Coding Convention Failures: 17</small>

          <div class="alert alert-warning" role="alert" style="margin: 15px">
            <pre>
Temperature_Converter.java:3:14: error: Type name 'Temperature_Converter' must match pattern '^[A-Z][a-zA-Z0-9]*$'.
Temperature_Converter.java:5:3: error: Missing a Javadoc comment.
Temperature_Converter.java:5:16: error: Method name 'ConvertToFahrenheit' must match pattern '^[a-z][a-z0-9][a-zA-Z0-9_]*$'.
Temperature_Converter.java:5:42: error: Parameter name 'Celsius' must match pattern '^[a-z][a-z0-9][a-zA-Z0-9]*$'.
Temperature_Converter.java:6: error: 'method def lcurly' have incorrect indentation level 0, expected level should be 2.
Temperature_Converter.java:6:1: error: '{' at column 1 should be on the previous line.
Temperature_Converter.java:7: error: 'member def type' have incorrect indentation level 0, expected level should be 4.
Temperature_Converter.java:7: error: 'method def' child have incorrect indentation level 0, expected level should be 4.
Temperature_Converter.java:8: error: '32' have incorrect indentation level 0, expected level should be 4.
Temperature_Converter.java:8:3: error: WhitespaceAround: '+' is not preceded with whitespace.
Temperature_Converter.java:8:4: error: WhitespaceAround: '+' is not followed by whitespace.
Temperature_Converter.java:8:19: error: WhitespaceAround: '*' is not followed by whitespace.
Temperature_Converter.java:9: error: 'method def' child have incorrect indentation level 0, expected level should be 4.
Temperature_Converter.java:10: error: 'class def rcurly' have incorrect indentation level 1, expected level should be 0.
Temperature_Converter.java:10: error: 'method def rcurly' have incorrect indentation level 0, expected level should be 2.
Temperature_Converter.java:10:1: error: '}' at column 1 should be alone on a line.
Temperature_Converter.java:10:2: error: '}' at column 2 should be on a new line.            
            </pre>
          </div>


          <svg width="670" height="400"></svg>
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
  <script src="https://d3js.org/d3.v3.min.js"></script>

  <svg id="s">
  </svg>
  <script>
			var data = [ {
				x : 0,
				y : 1.89
			}, {
				x : 1,
				y : 2.77
			}, {
				x : 2,
				y : 0.86
			}, {
				x : 3,
				y : 3.45
			}, {
				x : 4,
				y : 4.13
			}, {
				x : 5,
				y : 3.59
			}, {
				x : 6,
				y : 2.33
			}, {
				x : 7,
				y : 3.79
			}, {
				x : 8,
				y : 2.61
			}, {
				x : 9,
				y : 2.15
			} ];

			var width = 240, height = 120;

			var s = d3.select('#s');

			s.attr({
				'width' : '300',
				'height' : '180'
			}).style({
				'border' : '1px dotted #aaa'
			});

			var scaleX = d3.scale.linear().range([ 0, width ]).domain([ 0, 9 ]);

			var scaleY = d3.scale.linear().range([ height, 0 ])
					.domain([ 0, 5 ]);

			var line = d3.svg.line().x(function(d) {
				return scaleX(d.x);
			}).y(function(d) {
				return scaleY(d.y);
			});

			var axisX = d3.svg.axis().scale(scaleX).orient("bottom").ticks(10);

			var axisY = d3.svg.axis().scale(scaleY).orient("left").ticks(10);

			s.append('path').attr({
				'd' : line(data),
				'stroke' : '#09c',
				'fill' : 'none',
				'transform' : 'translate(35,20)' //折線圖也要套用 translate
			});

			s.append('g').call(axisX).attr({
				'fill' : 'none',
				'stroke' : '#000',
				'transform' : 'translate(35,' + (height + 20) + ')'
			});

			s.append('g').call(axisY).attr({
				'fill' : 'none',
				'stroke' : '#000',
				'transform' : 'translate(35,20)'
			});
		</script>
</body>
</html>