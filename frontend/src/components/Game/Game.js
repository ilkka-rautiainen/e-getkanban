import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import Board from '../Board/Board';
import './Game.scss';

const Game = ({ game}) => {
  return <div className="game">
    <div className="player-name">Playername: {game.playerName}</div>
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
    game: state.game
  }
};

export default connect(
  mapStateToProps
)(Game);
