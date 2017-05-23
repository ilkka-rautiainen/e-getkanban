import React, { PropTypes } from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';

class StartScreen extends React.Component {
  static propTypes = {
    onSubmit: PropTypes.func.isRequired,
    onChange: PropTypes.func.isRequired,
    value: PropTypes.string
  };

  constructor({onSubmit, onChange, normalDifficultyClick, mediumDifficultyClick, advancedDifficultyClick}) {
    super();

    this.onSubmit = onSubmit;
    this.onChange = onChange;
    this.normalDifficultyClick = normalDifficultyClick;
    this.mediumDifficultyClick = mediumDifficultyClick;
    this.advancedDifficultyClick = advancedDifficultyClick;
  };

  get buttonStyle() {
    return {
      marginLeft: 20,
      position: "relative",
      top: 12
    };
  }
  get inputStyle() {
    return {top: 15};
  }
  get difficultyButtonStyle() {
    return {
      position: "absolute",
      top: 20
    };
  }
  get secondDifficultyButtonStyle() {
    return {
      ...this.difficultyButtonStyle,
      left: "39%"
    };
  }
  get thirdDifficultyButtonStyle() {
    return {
      ...this.difficultyButtonStyle,
      left: "68.5%"
    };
  }

  render() {
    return (
      <div className="game-container">
        <h1>eKanban</h1>
          <form onSubmit={this.onSubmit}>
          <RaisedButton className="button" label="NORMAL" primary={this.props.normalDifficulty}
                        style={this.difficultyButtonStyle} onClick={this.normalDifficultyClick}  />
          <span className="tooltip">Player can adjust WIP-limits, other actions are made by AI</span>
          <RaisedButton className="button" label="MEDIUM" primary={this.props.mediumDifficulty}
                        style={this.secondDifficultyButtonStyle} onClick={this.mediumDifficultyClick} />
          <span className="tooltip">Player can adjust WIP-limits and assign resource dice to cards</span>
          <RaisedButton className="button" label="ADVANCED" primary={this.props.advancedDifficulty}
                        style={this.thirdDifficultyButtonStyle} onClick={this.advancedDifficultyClick} />
          <span className="tooltip">
            Player can adjust WIP-limits and assign resource dice to cards.
            3-day start and deploy-queue is introduced.
          </span>
          <TextField
            hintText="Player name"
            floatingLabelText="Insert player name here"
            value={this.props.value} onChange={this.onChange}
            style={this.inputStyle}
          />
          <RaisedButton type="submit" label="Start game" primary={true} style={this.buttonStyle} />
        </form>
        <h3>Made By Antti Ahonen & Ilkka Rautiainen</h3>
      </div>
    )
  };

}

export default StartScreen;
