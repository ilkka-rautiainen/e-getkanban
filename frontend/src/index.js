import React from 'react';
import ReactDOM from 'react-dom';
import { createStore, applyMiddleware } from 'redux';
import { Provider } from 'react-redux';
import { createEpicMiddleware } from 'redux-observable';
import App from './components/App/App';
import reducers from './reducers';
import epics from './epics';
import './index.scss';

const epicMiddleware = createEpicMiddleware(epics);
const store = (createStore)(
  reducers,
  applyMiddleware(epicMiddleware)
);

ReactDOM.render(
  <Provider store={store}>
    <App />
  </Provider>,
  document.getElementById('root')
);
