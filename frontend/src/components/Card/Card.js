import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import Paper from 'material-ui/Paper';
import CardPhasePoints from "../CardPhasePoints/CardPhasePoints"
import CardInfoItem from "../CardInfoItem/CardInfoItem";
import constants from "../../constants";
import "./Card.scss";

class Card extends React.Component {
  static propTypes = {
    card: PropTypes.shape({
      id: PropTypes.string.isRequired,
      cardPhasePoints: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired,
      financialValue: PropTypes.string.isRequired,
      gameOptionChangesWhenDeployed: PropTypes.string,
      subscribesWhenDeployed: PropTypes.string,
      description: PropTypes.string,
      outcome: PropTypes.string,
      dayStarted: PropTypes.number,
      dayDeployed: PropTypes.number,
      leadTimeInDays: PropTypes.number,
      subscribers: PropTypes.number,
      orderNumber: PropTypes.number.isRequired
    }).isRequired
  };

  get cardInfoClass() {
    let baseClassName = "card-info ";
    return baseClassName + this.gameDifficulty.toString().toLowerCase();
  }

  constructor({ card, gameDifficulty }) {
    super();
    this.card = card;
    this.gameDifficulty = gameDifficulty;
    this.state = {isVisible: true};
  };

  componentWillMount() {
    const that = this;
    if (this.props.waitForCardRender) {
      this.setState({
        isVisible: false
      });
      setTimeout(function() {
        that.show();
      }, 2000);
    }
  }

  show() {
    this.setState({
      isVisible: true
    });
  }

  get cardClass() {
    let columnCardsClass = "card";
    return this.state.isVisible ? columnCardsClass : columnCardsClass + " hidden";
  }

  render() {
    return <Paper className={this.cardClass} zDepth={3} >
      <div className="card-title">
        <span className="order-number">S{this.card.orderNumber}</span>
        { this.gameDifficulty !== "NORMAL" &&
          <span className="value">${this.card.financialValue}</span>
        }
      </div>
      <CardPhasePoints cardPhasePointIds={this.card.cardPhasePoints} />
      <div className={this.cardInfoClass}>
        <CardInfoItem title="Day Deployed" value={this.card.dayDeployed}/>
        <span className="special-char">&minus;</span>
        <CardInfoItem title="Day Started" value={this.card.dayStarted}/>
        <span className="special-char">=</span>
        <CardInfoItem givenClass="lead-time" title="Lead Time" value={this.card.leadTimeInDays}/>
        { this.gameDifficulty !== constants.GAME_DIFFICULTY_NORMAL &&
          <CardInfoItem title="Subscribers" value={this.card.subscribers}/>
        }
      </div>
      </Paper>
  };
}

const mapStateToProps = (state, ownProps) => {
  const gameDifficulty = state.game.difficultyLevel ? state.game.difficultyLevel : "NORMAL";
  const waitForCardRender = state.game.currentDay >= 2;
  return {
    card: state.cards[ownProps.id],
    gameDifficulty: gameDifficulty,
    waitForCardRender: waitForCardRender
  }
};

export default connect(
  mapStateToProps
)(Card);
