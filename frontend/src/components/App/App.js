import React from 'react';
import injectTapEventPlugin from 'react-tap-event-plugin';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import {orange500} from 'material-ui/styles/colors';
import GameContainer from '../GameContainer/GameContainer';
import './App.scss';

injectTapEventPlugin();

const muiTheme = getMuiTheme({
  palette: {
    accent1Color: orange500
  }
});

export default () => (
  <MuiThemeProvider muiTheme={muiTheme}>
    <div className="app">
      <GameContainer/>
    </div>
  </MuiThemeProvider>
);

