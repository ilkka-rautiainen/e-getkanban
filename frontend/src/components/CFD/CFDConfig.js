const cfdConfig = {
  legendConfig: {
    "equalWidths": false,
    "periodValueText": "latest: [[value.high]]",
    "position": "top",
    "valueAlign": "left",
    "valueWidth": 60
  },
  graphConfigTemplate: {
    "balloon":{
      "drop":true,
      "adjustBorderColor":false,
      "color":"#ffffff",
    },
    "balloonColor": "",
    "bullet": "round",
    "bulletBorderAlpha": 1,
    "bulletColor": "#FFFFFF",
    "bulletSize": 5,
    "hideBulletsCount": 50,
    "lineThickness": 2,
    "useLineColorForBulletBorder": true,
    "fillAlphas": 0.0,
    "lineAlpha": 0.8,
    "title": "",
    "valueField": "",
    "lineColor": ""
  },
  chartCursorConfig: {
    "cursorAlpha": 0
  },
  valueAxesConfig: [{
    "axisAlpha": 1,
    "integersOnly": true
  }],
  categoryAxisConfig: {
    "axisAlpha": 1,
    "startOnAxis": true
  }
};

export default cfdConfig;
