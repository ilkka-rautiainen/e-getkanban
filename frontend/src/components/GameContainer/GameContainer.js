import React from 'react';
import { connect } from 'react-redux';
import StartScreen from '../StartScreen/StartScreen'
import Game from '../Game/Game';
import { startGame } from '../../actions';
import constants from '../../constants'
import './GameContainer.scss';

class GameContainer extends React.Component {

  constructor() {
    super();
    this.state = {value: '', playerName: 'noname', normal: true, medium: false, advanced: false};

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleNormalDifficulty = this.handleNormalDifficulty.bind(this);
    this.handleMediumDifficulty = this.handleMediumDifficulty.bind(this);
    this.handleAdvancedDifficulty = this.handleAdvancedDifficulty.bind(this);
  };

  handleChange(event) {
    this.setState({value: event.target.value});
    let playerName = event.target.value !== '' ? event.target.value : 'noname';
    this.setState({playerName: playerName});
  }

  handleSubmit(event) {
    event.preventDefault();
    let difficulty = constants.GAME_DIFFICULTY_NORMAL;
    difficulty = this.state.medium ? constants.GAME_DIFFICULTY_MEDIUM : difficulty;
    difficulty = this.state.advanced ? constants.GAME_DIFFICULTY_ADVANCED : difficulty;
    this.props.onStartGame(this.state.playerName, difficulty);
  }

  handleNormalDifficulty(event) {
    event.preventDefault();
    this.setState({normal: true, medium: false, advanced: false});
  }

  handleMediumDifficulty(event) {
    event.preventDefault();
    this.setState({normal: false, medium: true, advanced: false});
  }

  handleAdvancedDifficulty(event) {
    event.preventDefault();
    this.setState({normal: false, medium: false, advanced: true});
  }

  render() {
    return this.props.game ? <Game /> :
      <StartScreen
        onSubmit={this.handleSubmit}
        onChange={this.handleChange}
        value={this.state.value}
        normalDifficulty={this.state.normal}
        mediumDifficulty={this.state.medium}
        advancedDifficulty={this.state.advanced}
        normalDifficultyClick={this.handleNormalDifficulty}
        mediumDifficultyClick={this.handleMediumDifficulty}
        advancedDifficultyClick={this.handleAdvancedDifficulty}
      />
  }

}

const mapStateToProps = (state) => {
  return {
    game: state.game
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    onStartGame: (playerName, gameDifficulty) => {
      dispatch(startGame(playerName, gameDifficulty))
    }
  }
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GameContainer);
