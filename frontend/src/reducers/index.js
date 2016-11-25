import { combineReducers } from 'redux'
import { Schema, arrayOf } from 'normalizr';
import { SET_GAME_DATA } from '../actionTypes';

export const gameSchema = new Schema('games');
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
  phases: arrayOf(phaseSchema),
  backlogDeck: arrayOf(cardSchema)
});

gameSchema.define({
  board: boardSchema
});

function game(state = null, action) {
  switch (action.type) {
    case SET_GAME_DATA:
      return action.payload.game;
    default:
      return state;
  }
}

function board(state = null, action) {
  switch (action.type) {
    case SET_GAME_DATA:
      return action.payload.board;
    default:
      return state
  }
}

function phases(state = null, action) {
  switch (action.type) {
    case SET_GAME_DATA:
      return action.payload.phases;
    default:
      return state
  }
}

function columns(state = null, action) {
  switch (action.type) {
    case SET_GAME_DATA:
      return action.payload.columns;
    default:
      return state
  }
}

function cards(state = null, action) {
  switch (action.type) {
    case SET_GAME_DATA:
      return action.payload.cards;
    default:
      return state;
  }
}

function cardPhasePoints(state = null, action) {
  switch (action.type) {
    case SET_GAME_DATA:
      if (action.payload.phasePoints !== undefined)
        return action.payload.phasePoints;
      else
        return state;
    default:
      return state;
  }
}

function backlogDeck(state = null, action) {
  switch (action.type) {
    case SET_GAME_DATA:
      return action.payload.board.backlogDeck;
    default:
      return state;
  }
}


const reducers = combineReducers({
  game,
  board,
  phases,
  columns,
  cards,
  cardPhasePoints,
  backlogDeck
});

export default reducers;
