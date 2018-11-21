Highcharts.chart('chart1Demo', {
    chart: {
        type: 'column'
    },
    title: {
        text: 'Compile Result'
    },
    subtitle: {
        text: ''
    },
    xAxis: {
        categories: projectNames,
        crosshair: true
    },
    yAxis: {
        min: 0,
        title: {
            text: 'count'
        }
    },
    tooltip: {
        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
            '<td style="padding:0"><b>{point.y}</b></td></tr>',
        footerFormat: '</table>',
        shared: true,
        useHTML: true
    },
    plotOptions: {
        column: {
            pointPadding: 0.2,
            borderWidth: 0
        }
    },
    series: chart1Array
});