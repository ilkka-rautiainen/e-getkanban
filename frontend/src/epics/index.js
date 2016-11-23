import { combineEpics } from 'redux-observable';
import startGame from "./startGame";

const epics = combineEpics(
  startGame,
);

export default epics;


