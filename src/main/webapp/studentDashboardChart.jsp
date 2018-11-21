<%@ page language="java" contentType="text/html; charset=BIG5" pageEncoding="utf-8"%>
<%@ page import="fcu.selab.progedu.conn.Conn, fcu.selab.progedu.conn.StudentConn, fcu.selab.progedu.conn.HttpConnect"%>
<%@ page import="fcu.selab.progedu.config.GitlabConfig,fcu.selab.progedu.config.CourseConfig"%>
<%@ page import="fcu.selab.progedu.config.JenkinsConfig" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.gitlab.api.GitlabAPI"%>
<%@ page import="org.gitlab.api.models.*"%>
<%@ page import="java.util.*"%>
<%@ page import="fcu.selab.progedu.jenkins.JobStatus" %>
<%@ page import="fcu.selab.progedu.jenkins.JenkinsApi" %>
<%@ page import="org.json.JSONArray, org.json.JSONException, org.json.JSONObject" %>
<%@ page import="fcu.selab.progedu.db.UserDbManager, fcu.selab.progedu.db.ProjectDbManager" %>
<%@ page import="fcu.selab.progedu.data.User, fcu.selab.progedu.data.Project" %>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
	<link rel="shortcut icon" href="img/favicon.ico"/>
	<link rel="bookmark" href="img/favicon.ico"/>
</head>
<body>
<%
List<JSONObject> jsons = new ArrayList<JSONObject>();
%>
<div class="card" style="margin-top: 30px">
		        <h4 id="Student Projects" class="card-header">
		        	<i class="fa fa-table" aria-hidden="true"></i>&nbsp; 
		        		<fmt:message key="stuDashboard_card_statisticChart"/>
		        </h4>
		        <div class="card-block">
		        		<ul class="nav nav-tabs" role="tablist">
						  <li class="nav-item">
						    <a class="nav-link active" data-toggle="tab" href="#chart1" role="tab">Chart1</a>
						  </li>
						  <li class="nav-item">
						    <a class="nav-link" data-toggle="tab" href="#chart2" role="tab">Chart2</a>
						  </li>
						</ul>
		        		<!-- Tab panes -->
						<div class="tab-content text-center" style="margin-top: 10px">
						  <div class="tab-pane active" id="chart1" role="tabpanel">
						  	<div id="chart1Demo" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
						  </div>
						  <div class="tab-pane" id="chart2" role="tabpanel">
						  	<div id="chart2Demo" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
						  </div>
						</div>
		        	</div>
			</div>
</body>
<!-- set Highchart colors -->
<script>
Highcharts.setOptions({
	 colors: ['#5fa7e8', '#e52424', '#FF5809', '#878787']
	})
</script>
<!-- chart1 -->
<script>
<%
List<String> names = new ArrayList<String>();
List<Integer> blues = new ArrayList<Integer>();
List<Integer> reds = new ArrayList<Integer>();
List<Integer> oranges = new ArrayList<Integer>();
List<Integer> grays = new ArrayList<Integer>();

for(JSONObject json : jsons) {
	if(!names.contains(json.get("name"))) {
		names.add(json.get("name").toString());
		blues.add(Integer.parseInt(json.get("blueCount").toString()));
		reds.add(Integer.parseInt(json.get("redCount").toString()));
		oranges.add(Integer.parseInt(json.get("orangeCount").toString()));
		grays.add(Integer.parseInt(json.get("grayCount").toString()));
	}else {
		int index = names.indexOf(json.get("name"));
		int blue = blues.get(index) + Integer.parseInt(json.get("blueCount").toString());
		int red = reds.get(index) + Integer.parseInt(json.get("redCount").toString());
		int orange = oranges.get(index) + Integer.parseInt(json.get("orangeCount").toString());
		int gray = grays.get(index) + Integer.parseInt(json.get("grayCount").toString());
		
		blues.set(index, blue);
		reds.set(index, red);
		oranges.set(index, orange);
		grays.set(index, gray);
	}
}

String x = "var x=[";
int i = 0;
for(String name : names) {
	x += "'" + name + "'";
	if(i != names.size()-1) {
		x += ",";
	}
	i++;
}
x += "];";
out.println(x);

int j=0;
String s = "var s = [{ name: '建置成功', data:[";
for(int blue : blues) {
	s += blue;
	if(j != blues.size()-1) {
		s += ", ";
	}
	j++;
}
s += "]}, { name: '編譯失敗', data:[";
j = 0;
for(int red : reds) {
	s += red;
	if(j != reds.size()-1) {
		s += ", ";
	}
	j++;
}
s += "]}, { name: '未通過程式規範', data:[";
j = 0;
for(int orange : oranges) {
	s += orange;
	if(j != oranges.size()-1) {
		s += ", ";
	}
	j++;
}
s += "]}, { name: '未建置', data:[";
j = 0;
for(int gray : grays) {
	s += gray;
	if(j != grays.size()-1) {
		s += ", ";
	}
	j++;
}
s += "]}]";
out.println(s);
%>
Highcharts.chart('chart1Demo', {
    chart: {
        type: 'column'
    },
    title: {
        text: '各作業建置結果統計表'
    },
    xAxis: {
        categories: x
    },
    yAxis: {
        min: 0,
        title: {
            text: '個數'
        },
        stackLabels: {
            enabled: true,
            style: {
                fontWeight: 'bold',
                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
            }
        }
    },
    legend: {
        align: 'right',
        x: -30,
        verticalAlign: 'top',
        y: 25,
        floating: true,
        backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
        borderColor: '#CCC',
        borderWidth: 1,
        shadow: false
    },
    tooltip: {
        headerFormat: '<b>{point.x}</b><br/>',
        pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
    },
    plotOptions: {
        column: {
            stacking: 'normal',
            dataLabels: {
                enabled: true,
                color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
            }
        }
    },
    series: s
});
</script>
<!-- chart2 -->
<script>
<%

j=0;
int blueTotal = 0;
int redTotal = 0;
int orangeTotal = 0;
int grayTotal = 0;

s = "var s = [{name: 'Brands', colorByPoint: true, data: [{ name: '建置成功', y:";
for(int blue : blues) {
	blueTotal += blue;
}
s += blueTotal;
s += "}, { name: '編譯失敗', y:";
j = 0;
for(int red : reds) {
	redTotal += red;
}
s += redTotal;
s += "}, { name: '未通過程式規範', y:";
j = 0;
for(int orange : oranges) {
	orangeTotal += orange;
}
s += orangeTotal;
s += "}, { name: '未建置', y:";
j = 0;
for(int gray : grays) {
	grayTotal += gray;
}
s += grayTotal;
s += "}]}]";
out.println(s);
%>
$(document).ready(function () {
// Build the chart
    Highcharts.chart('chart2Demo', {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
        title: {
            text: '所有作業建置結果統計'
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.y:.0f}</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: false
                },
                showInLegend: true
            }
        },
        series: s
    });
});
</script>
<script type="text/javascript">
$(function(){
    $("#gotop").click(function(){
        jQuery("html,body").animate({
            scrollTop:0
        },1000);
    });
    $(window).scroll(function() {
        if ( $(this).scrollTop() > 300){
            $('#gotop').fadeIn("fast");
        } else {
            $('#gotop').stop().fadeOut("fast");
        }
    });
});
</script>
</html>