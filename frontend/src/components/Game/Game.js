import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import Board from '../Board/Board';
import BacklogDeckContainer from '../BacklogDeckContainer/BacklogDeckContainer';
import './Game.scss';

const Game = ({ game }) => {
  const playerName = game.playerName.length > 16 ? game.playerName.substring(0,16)+"..." : game.playerName;
  return <div className="game">
    <div className="game-details">
      <div className="game-info player-name">Playername: {playerName}</div>
      <div className="game-info day">Current Day: {game.currentDay}</div>
    </div>
    <BacklogDeckContainer />
    <Board  />
  </div>
};

Game.propTypes = {
  game: PropTypes.shape({
    playerName: PropTypes.string.isRequired,
    board: PropTypes.string.isRequired
  }).isRequired
};

const mapStateToProps = (state) => {
  return {
    game: state.game,
  }
};

export default connect(
  mapStateToProps
)(Game);
