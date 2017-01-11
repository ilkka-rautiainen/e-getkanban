import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';
import { playTurn } from '../../actions';
import Board from '../Board/Board';
import BacklogDeckArea from '../BacklogDeckArea/BacklogDeckArea';
import CFD from '../CFD/CFD';
import './Game.scss';

class Game extends React.Component {
  static propTypes = {
    game: PropTypes.shape({
      playerName: PropTypes.string.isRequired,
      board: PropTypes.string.isRequired
    }).isRequired
  };

  constructor({ game }) {
    super();
    this.playerName = game.playerName.length > 16 ? game.playerName.substring(0,16)+"..." : game.playerName;
  };

  get buttonStyle() {
    return {
      marginLeft: 'auto',
      height: 50,
      marginTop: 10
    }
  }

  render() {
    return (
      <div className="game">
        <div className="game-toolbar">
          <div className="game-details">
            <div className="game-info player-name">Player Name: {this.playerName}</div>
            <div className="game-info day">Current Day: {this.props.game.currentDay}</div>
          </div>
          <RaisedButton
            label="Next Round"
            secondary={true}
            style={this.buttonStyle}
            onClick={() => {this.props.onPlayTurn(this.props.game.id, this.props.wipLimits); }}
          />
        </div>
        <BacklogDeckArea />
        <Board  />
        <CFD  />
      </div>
    )
  }

};

const mapStateToProps = (state) => {
  return {
    game: state.game,
    wipLimits: state.wipLimits
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    onPlayTurn: (gameId, wipLimits) => {
      dispatch(playTurn(gameId, wipLimits))
    }
  }
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Game);
