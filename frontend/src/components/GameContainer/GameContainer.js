import React from 'react';
import { connect } from 'react-redux';
import StartScreen from '../StartScreen/StartScreen'
import Game from '../Game/Game';
import { startGame } from '../../actions';
import './GameContainer.scss';

class GameContainer extends React.Component {

  constructor() {
    super();
    this.state = {value: '', playerName: 'noname'};

    this.handleChange = this.handleChange.bind(this);
  };

  handleChange(event) {
    this.setState({value: event.target.value});
    let playerName = event.target.value !== '' ? event.target.value : 'noname';
    this.setState({playerName: playerName});
  }

  render() {
    return this.props.game ? <Game /> :
      <StartScreen
        onSubmit={(event) => {event.preventDefault(); this.props.onStartGame(this.state.playerName, "NORMAL");}}
        onChange={this.handleChange}
        value={this.state.value}
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
