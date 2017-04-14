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

  get cardClass() {
    let columnCardsClass = "card";
    return this.state.isVisible ? columnCardsClass : columnCardsClass + " hidden";
  }

  render() {
    const isAdvancedGame = this.gameDifficulty === constants.GAME_DIFFICULTY_ADVANCED;
    const isEasyGame = this.gameDifficulty === constants.GAME_DIFFICULTY_EASY;
    return (
      <Paper className={this.cardClass} zDepth={3} style={this.props.style} onClick={!isEasyGame && this.props.onClick}>
      <div className="card-title">
        <span className="order-number">S{this.card.orderNumber}</span>
        { isAdvancedGame &&
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
        { isAdvancedGame &&
          <CardInfoItem title="Subscribers" value={this.card.subscribers}/>
        }
      </div>
      </Paper>
    )
  };
}

const mapStateToProps = (state, ownProps) => {
  const gameDifficulty = state.game.difficultyLevel ? state.game.difficultyLevel : "NORMAL";
  return {
    card: state.cards[ownProps.id],
    gameDifficulty: gameDifficulty
  }
};

export default connect(
  mapStateToProps
)(Card);
