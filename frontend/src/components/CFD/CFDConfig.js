const cfd = {
  legendConfig: {
    "equalWidths": false,
    "periodValueText": "total: [[value.high]]",
    "position": "top",
    "valueAlign": "left",
    "valueWidth": 60
  },
  graphConfig: {
    "balloon":{
      "drop":true,
      "adjustBorderColor":false,
      "color":"#ffffff",
    },
    "balloonColor": "BALLOON_COLOR",
    "bullet": "round",
    "bulletBorderAlpha": 1,
    "bulletColor": "#FFFFFF",
    "bulletSize": 5,
    "hideBulletsCount": 50,
    "lineThickness": 2,
    "useLineColorForBulletBorder": true,
    "fillAlphas": 0.0,
    "lineAlpha": 0.8,
    "title": "TITLE_TEXT",
    "valueField": "VALUE_FIELD",
    "lineColor": "LINE_COLOR"
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

export default cfd;
