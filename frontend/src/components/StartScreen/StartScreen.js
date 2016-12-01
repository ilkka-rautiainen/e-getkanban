import React, { PropTypes } from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';

const StartScreen = ({onSubmit, onChange, value}) => {
  const buttonStyle = {
    marginLeft: 20
  };

  return <div className="game-container">
    <h1>eKanban</h1>
    <form onSubmit={onSubmit}>
      <TextField
        hintText="Player name"
        floatingLabelText="Insert player name here"
        value={value} onChange={onChange}
      />
      <RaisedButton type="submit" label="Start game" primary={true} style={buttonStyle} />
    </form>
  </div>
};

StartScreen.propTypes = {
  onSubmit: PropTypes.func.isRequired,
  onChange: PropTypes.func.isRequired,
  value: PropTypes.string
};


export default StartScreen;
