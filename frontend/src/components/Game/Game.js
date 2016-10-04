import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import './Game.scss';
import Board from '../Board/Board'

const Game = ({ game }) => {
  return <div className="Game">
    <div className="playerName">Playername: {game.playerName}</div>
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
