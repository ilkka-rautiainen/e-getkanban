import { combineReducers } from 'redux'
import { normalize, Schema, arrayOf } from 'normalizr';
import initialData from './initialData';

const gameSchema = new Schema('games');
const boardSchema = new Schema('boards');
const phaseSchema = new Schema('phases');
const columnSchema = new Schema('columns');
const cardSchema = new Schema('cards');
const cardPhasePointsSchema = new Schema("cardPhasePoints");

cardSchema.define({
  cardPhasePoints: arrayOf(cardPhasePointsSchema)
});

columnSchema.define({
  cards: arrayOf(cardSchema)
});

phaseSchema.define({
  columns: arrayOf(columnSchema)
});

boardSchema.define({
  phases: arrayOf(phaseSchema)
});

gameSchema.define({
  board: boardSchema
});

const normalizedData = normalize(initialData, gameSchema);

const initialGame = normalizedData.entities.games[normalizedData.result];
const initialBoard = normalizedData.entities.boards[initialGame.board];
const initialPhases = normalizedData.entities.phases;
const initialColumns = normalizedData.entities.columns;
const initialCards = normalizedData.entities.cards || [];
const initialPhasePoints = normalizedData.entities.cardPhasePoints || [];


function game(state = initialGame, action) {
  switch (action.type) {
    default:
      return state
  }
}

function board(previousState = initialBoard, action) {
  switch (action.type) {
    default:
      return previousState
  }
}

function phases(previousState = initialPhases, action) {
  switch (action.type) {
    default:
      return previousState
  }
}

function columns(previousState = initialColumns, action) {
  switch (action.type) {
    default:
      return previousState
  }
}

function cards(previousState = initialCards, action) {
  switch (action.type) {
    default:
      return previousState;
  }
}

function cardPhasePoints(previousState = initialPhasePoints, action) {
  switch (action.type) {
    default:
      return previousState;
  }
}

const reducers = combineReducers({
  game,
  board,
  phases,
  columns,
  cards,
  cardPhasePoints
});

export default reducers;
