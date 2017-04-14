import React, { PropTypes } from 'react';
import constants from '../../constants';
import "./Die.scss";

class Die extends React.Component {

  static propTypes = {
    castNumbers: PropTypes.object.isRequired,
    visibilityClass: PropTypes.string.isRequired,
    dieStyle: PropTypes.object.isRequired,
    diePosition: PropTypes.object,
    selectedClass: PropTypes.string
  };

  constructor({dieStyle}) {
    super();
    this.dieStyle = dieStyle;
  }

  get dieClass() {
    return "die " + this.props.visibilityClass + " " + this.props.selectedClass;
  }

  get isNormalGame() {
    return this.props.gameDifficulty === constants.GAME_DIFFICULTY_NORMAL;
  }

  render() {
    return (
      <div className={this.dieClass} style={this.props.diePosition} onClick={this.props.onClick}>
        {
          !this.isNormalGame &&
          <span className="minor" style={this.dieStyle.firstMinor}>{this.props.castNumbers.firstMinor}</span>
        }
        <span className="major" style={this.dieStyle.major}>{this.props.castNumbers.major}</span>
        {
          !this.isNormalGame &&
          <span className="minor" style={this.dieStyle.secondMinor}>{this.props.castNumbers.secondMinor}</span>
        }
      </div>
    )
  }
}

export default Die;
