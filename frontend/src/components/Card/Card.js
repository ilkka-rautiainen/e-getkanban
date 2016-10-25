import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import Paper from 'material-ui/Paper';
import CardPhasePoints from "../CardPhasePoints/CardPhasePoints"
import CardInfoItem from "../CardInfoItem/CardInfoItem";

class Card extends React.Component {
  static propTypes = {
    card: PropTypes.object.isRequired
  };

  get style() {
    return {
        height: 100,
        width: '80%',
        margin: 20,
        textAlign: 'center',
        display: 'inline-block'
    }
  }

  get calculateLeadTime() {
    if (this.dayDeployed != null && this.dayStarted != null)
      return this.dayDeployed - this.dayStarted;
  }

  constructor({ card }) {
    super();
    this.orderNumber = card.orderNumber;
    this.financialValue = card.financialValue;
    this.cardPhasePoints = card.cardPhasePoints;
    this.dayDeployed = card.dayDeployed;
    this.dayStarted = card.dayStarted;
    this.subscribers = card.subscribers;
  };

  render() {
    return <div className="card">
      <Paper style={this.style} zDepth={3} >
        <div className="card-title">
          <span className="order-number">S{this.orderNumber}</span>
          <span className="value">${this.financialValue}</span>
        </div>
        <CardPhasePoints cardPhasePointIds={this.cardPhasePoints} />
        <div className="card-info">
          <CardInfoItem title="Day Deployed" value={this.dayDeployed}/>
          <CardInfoItem title="Day Started" value={this.dayStarted}/>
          <CardInfoItem title="Lead Time" value={this.calculateLeadTime}/>
          <CardInfoItem title="Subscribers" value={this.subscribers}/>
        </div>
      </Paper>
    </div>
  };
}

const mapStateToProps = (state, ownProps) => {
  return {
    card: state.cards[ownProps.id]
  }
};

export default connect(
  mapStateToProps
)(Card);
