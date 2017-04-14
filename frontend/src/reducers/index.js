import { combineReducers } from 'redux'
import { Schema, arrayOf } from 'normalizr';
import { CHANGE_WIP, SET_GAME_DATA, REMOVE_DICE, ADD_DICE, ENABLE_NEXT_ROUND,
         SET_ACTIVE_DICE, SET_ACTIVE_CARD, ASSIGN_DIE, REMOVE_ASSIGNED_DICE, SET_DICE_DATA, SET_PHASE_POINTS,
         ADD_ASSIGNED_RESOURCE, RESET_ASSIGNED_RESOURCES } from '../actions/actionTypes';
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
      if (action.payload.phasePoints !== undefined) {
        return action.payload.phasePoints;
      } else {
        return state;
      }
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

function wipLimits(state = null, action) {
  switch (action.type) {
    case CHANGE_WIP:
      return Object.assign({}, state, {
        [action.phase]: action.wipLimit
      });
    case SET_GAME_DATA:
      return action.payload.wipLimits;
    default:
      return state;
  }
}

function assignResources(state = [], action) {
  switch (action.type) {
    case ADD_ASSIGNED_RESOURCE:
      return state.concat([action.resource]);
    case RESET_ASSIGNED_RESOURCES:
      return [];
    default:
      return state;
  }
}

function assignedDice(state = [], action) {
  switch (action.type) {
    case ASSIGN_DIE:
      return state.concat([action.die]);
    case REMOVE_ASSIGNED_DICE:
      return [];
    default:
      return state;
  }
}

function cfdData(state = null, action) {
  switch (action.type) {
    case SET_GAME_DATA:
      return action.payload.cfdData;
    default:
      return state;
  }
}

function cfdConfig(state = null, action) {
  switch (action.type) {
    case SET_GAME_DATA:
      return action.payload.cfdConfig;
    default:
      return state;
  }
}

function nextRoundUIState(state = { showDice: false, enableButtonPress: true }, action) {
  switch (action.type) {
    case REMOVE_DICE:
      return { showDice: false, enableButtonPress: false };
    case ADD_DICE:
      return { showDice: true, enableButtonPress: false };
    case ENABLE_NEXT_ROUND:
      return Object.assign({}, state, {
        enableButtonPress: true
      });
    default:
      return state;
  }
}

function phaseDice(state = null, action) {
  switch (action.type) {
    case SET_ACTIVE_DICE:
      return action.phase;
    default:
      return state;
  }
}

function dice(state = null, action) {
  switch (action.type) {
    case SET_DICE_DATA:
      return action.dice;
    default:
      return state;
  }
}

function workedPhasePoints(state = null, action) {
  switch (action.type) {
    case SET_PHASE_POINTS:
      return action.phasePoints;
    default:
      return state;
  }
}

function activeCard(state = null, action) {
  switch (action.type) {
    case SET_ACTIVE_CARD:
      return action.card;
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
  backlogDeck,
  dice,
  wipLimits,
  assignResources,
  assignedDice,
  nextRoundUIState,
  cfdData,
  cfdConfig,
  phaseDice,
  workedPhasePoints,
  activeCard
});

export default reducers;
