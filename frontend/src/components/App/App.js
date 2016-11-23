import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import GameContainer from '../GameContainer/GameContainer';
import './App.scss';

export default () => (
  <MuiThemeProvider>
    <div className="app">
      <GameContainer/>
    </div>
  </MuiThemeProvider>
);

