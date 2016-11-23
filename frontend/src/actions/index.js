import { CHANGE_WIP, START_GAME, SET_GAME_DATA } from '../actionTypes';

/*
 * action creators
 */

export function changeWip(phase, wipLimit) {
  return { type: CHANGE_WIP, phase, wipLimit };
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

export function setGameData(normalizedData) {
  return {
    type: SET_GAME_DATA,
    payload: {
      game: normalizedData.entities.games[normalizedData.result],
      board: normalizedData.entities.boards[normalizedData.entities.games[normalizedData.result].board],
      phases: normalizedData.entities.phases,
      columns: normalizedData.entities.columns,
      cards: normalizedData.entities.cards !== undefined ? normalizedData.entities.cards : [],
      phasePoints: normalizedData.entities.cardPhasePoints
    }
  }
}
