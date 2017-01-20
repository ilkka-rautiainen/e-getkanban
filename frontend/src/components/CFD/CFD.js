import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import AmCharts from 'amcharts3-react';
import _ from 'lodash';
import IconButton from 'material-ui/IconButton';
import ExpandMore from 'material-ui/svg-icons/navigation/expand-more';
import ExpandLess from 'material-ui/svg-icons/navigation/expand-less';
import cfdConfig from './CFDConfig';
import cfdGraphConfigTemplate from './CFDGraphConfigTemplate';
import './CFD.scss';

class CFD extends React.Component {
  static propTypes = {
    dataProvider: PropTypes.arrayOf(PropTypes.object).isRequired,
    graphConfigValues: PropTypes.arrayOf(PropTypes.object).isRequired
  };

  constructor({ graphConfigValues }) {
    super();
    this.state = {isVisible: false};
    let graphConfigs = [];
    graphConfigValues.forEach(function(config) {
      let graphConfig = _.clone(cfdGraphConfigTemplate, true);
      graphConfig["balloonColor"] = config.color;
      graphConfig["lineColor"] = config.color;
      graphConfig["valueField"] = config.valueField;
      graphConfig["title"] = config.title;
      graphConfigs.push(graphConfig);
    });
    this.graphConfigs = graphConfigs;

    this.handleClick = this.handleClick.bind(this);
  }

  handleClick() {
    this.setState(prevState => ({
      isVisible: !prevState.isVisible
    }));
  }

  get buttonStyles() {
    return {
      width: 60,
      height: 60,
      padding: 0,
      color: '#fff',
      display: 'block',
      margin: '0 auto'
    }
  }

  get containerClass() {
    return this.state.isVisible ? "cfd visible" : "cfd hidden";
  }

  render() {
    return (
      <div className={this.containerClass}>
        <IconButton iconStyle={this.buttonStyles} style={this.buttonStyles} onClick={this.handleClick}>
          {!this.state.isVisible ? <ExpandLess /> : <ExpandMore />}
        </IconButton>
        <AmCharts
          type="serial"
          theme="light"
          legend={cfdConfig.legendConfig}
          graphs={this.graphConfigs}
          chartCursor={cfdConfig.chartCursorConfig}
          dataProvider={this.props.dataProvider}
          valueAxes={cfdConfig.valueAxesConfig}
          categoryField="day"
          categoryAxis={cfdConfig.categoryAxisConfig}
        />
      </div>
    )
  }
}

const mapStateToProps = (state) => {
  return {
    dataProvider: state.cfdData,
    graphConfigValues: state.cfdConfig
  }
};

export default connect(
  mapStateToProps
)(CFD);



