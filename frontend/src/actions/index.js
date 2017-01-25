import _ from 'lodash';
import { CHANGE_WIP, START_GAME, PLAY_TURN, SET_GAME_DATA, REMOVE_DICE, ADD_DICE, ENABLE_NEXT_ROUND } from './actionTypes';

/*
 * action creators
 */

export function changeWip(phase, wipLimit) {
  return { type: CHANGE_WIP, phase, wipLimit };
}

export function removeDice() {
  return { type: REMOVE_DICE };
}

export function addDice() {
  return { type: ADD_DICE };
}

export function enableNextRoundButton() {
  return { type: ENABLE_NEXT_ROUND }
}

export function startGame(playerName, difficultyLevel) {
  return {
    type: START_GAME,
    payload: {
      playerName,
      difficultyLevel
    }
  };
}

export function playTurn(gameId, newWipLimits) {
  return {
    type: PLAY_TURN,
    gameId: gameId,
    turn: {
      adjustWipLimitsAction: {
        phaseWipLimits: newWipLimits
      },
      moveCardActions: [],
      drawFromBacklogActions: [],
      assignResourcesActions: []
    }
  }
}

export function setGameData(normalizedData) {
  const workingPhases = _.pickBy(normalizedData.entities.phases, function(value, key) {return value.isWorkPhase;});
  let wipLimits = {};
  Object.keys(workingPhases).forEach(function(key) {
    wipLimits[key] = workingPhases[key].wipLimit.toString()
  });
  return {
    type: SET_GAME_DATA,
    payload: {
      game: normalizedData.entities.games[normalizedData.result],
      board: normalizedData.entities.boards[normalizedData.entities.games[normalizedData.result].board],
      phases: normalizedData.entities.phases,
      columns: normalizedData.entities.columns,
      cards: normalizedData.entities.cards !== undefined ? normalizedData.entities.cards : [],
      phasePoints: normalizedData.entities.cardPhasePoints,
      wipLimits: wipLimits
    }
  }
}
