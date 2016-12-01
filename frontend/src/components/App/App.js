import React from 'react';
import injectTapEventPlugin from 'react-tap-event-plugin';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import GameContainer from '../GameContainer/GameContainer';
import './App.scss';

injectTapEventPlugin();

export default () => (
  <MuiThemeProvider>
    <div className="app">
      <GameContainer/>
    </div>
  </MuiThemeProvider>
);

