import { combineEpics } from 'redux-observable';
import startGame from "./startGame";
import playTurn from "./playTurn";

const epics = combineEpics(
  startGame,
  playTurn
);

export default epics;


