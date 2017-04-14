import React, { PropTypes } from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';

class StartScreen extends React.Component {
  static propTypes = {
    onSubmit: PropTypes.func.isRequired,
    onChange: PropTypes.func.isRequired,
    value: PropTypes.string
  };

  constructor({onSubmit, onChange, normalDifficultyClick, mediumDifficultyClick}) {
    super();

    this.onSubmit = onSubmit;
    this.onChange = onChange;
    this.normalDifficultyClick = normalDifficultyClick;
    this.mediumDifficultyClick = mediumDifficultyClick;
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
      left: 150
    };
  }

  render() {
    return (
      <div className="game-container">
        <h1>eKanban</h1>
          <form onSubmit={this.onSubmit}>
          <RaisedButton label="NORMAL" primary={this.props.normalDifficulty} style={this.difficultyButtonStyle} onClick={this.normalDifficultyClick}  />
          <RaisedButton label="MEDIUM" primary={this.props.mediumDifficulty} style={this.secondDifficultyButtonStyle} onClick={this.mediumDifficultyClick} />
          <TextField
            hintText="Player name"
            floatingLabelText="Insert player name here"
            value={this.props.value} onChange={this.onChange}
            style={this.inputStyle}
          />
          <RaisedButton type="submit" label="Start game" primary={true} style={this.buttonStyle} />
        </form>
      </div>
    )
  };

}

export default StartScreen;
