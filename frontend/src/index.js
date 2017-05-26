import React from 'react';
import ReactDOM from 'react-dom';
import { createStore, applyMiddleware } from 'redux';
import { Provider } from 'react-redux';
import { createEpicMiddleware } from 'redux-observable';
import { Observable } from 'rxjs/Observable';
import App from './components/App/App';
import reducers from './reducers';
import epics from './epics';
import './index.scss';

const epicMiddleware = createEpicMiddleware(epics, {
  dependencies: {
    ajaxPost: Observable.ajax.post,
    ajaxPut: Observable.ajax.put
  }
});
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
