import React from 'react';
import { connect } from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';
import Game from '../Game/Game';
import { startGame } from '../../actions';
import './GameContainer.scss';

class GameContainer extends React.Component {

  constructor() {
    super();
    this.state = {value: ''};

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  };

  handleChange(event) {
    this.setState({value: event.target.value});
  }

  handleSubmit(event) {
    event.preventDefault();
    let playerName = this.state.value !== '' ? this.state.value : 'noname';
    this.props.dispatch(startGame(playerName, "NORMAL"));
  }

  render() {
    const buttonStyle = {
      marginLeft: 20
    };
    let startScreen = <div className="game-container">
        <h1>eKanban</h1>
        <form onSubmit={this.handleSubmit}>
          <TextField
            hintText="Player name"
            floatingLabelText="Insert player name here"
            value={this.state.value} onChange={this.handleChange}
          />
          <RaisedButton type="submit" label="Start game" primary={true} style={buttonStyle} />
        </form>
      </div>;
    return this.props.game ? <Game /> : startScreen;
  }

};

const mapStateToProps = (state) => {
  return {
    game: state.game,
  }
};

export default connect(
  mapStateToProps
)(GameContainer);
