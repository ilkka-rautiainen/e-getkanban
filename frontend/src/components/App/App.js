import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import './App.scss';
import Game from '../Game/Game';

export default () => (
  <MuiThemeProvider>
    <div className="App">
      <Game/>
    </div>
  </MuiThemeProvider>
);
