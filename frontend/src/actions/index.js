import _ from 'lodash';
import { CHANGE_WIP, START_GAME, PLAY_TURN, SET_GAME_DATA, REMOVE_DICE, ADD_DICE, ENABLE_NEXT_ROUND } from './actionTypes';
import constants from '../constants';


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
      assignResourcesActions: [],
      diceCastActions: []
    }
  }
}

export function setGameData(normalizedData) {
  const workingPhases = _.pickBy(normalizedData.entities.phases, function(value, key) {return value.isWorkPhase;});
  let wipLimits = {};
  Object.keys(workingPhases).forEach(function(key) {
    wipLimits[key] = workingPhases[key].wipLimit.toString()
  });
  const game = normalizedData.entities.games[normalizedData.result];
  const board = normalizedData.entities.boards[normalizedData.entities.games[normalizedData.result].board];
  const phases = normalizedData.entities.phases;
  const cfdData = getCfdData(game.cfd);
  const cfdConfig = getCfdConfigs(phases, board);
  return {
    type: SET_GAME_DATA,
    payload: {
      game: game,
      board: board,
      phases: phases,
      columns: normalizedData.entities.columns,
      cards: normalizedData.entities.cards !== undefined ? normalizedData.entities.cards : [],
      phasePoints: normalizedData.entities.cardPhasePoints,
      wipLimits: wipLimits,
      cfdData: cfdData,
      cfdConfig: cfdConfig
    }
  }
}

function getCfdData(cfdData) {
  let cfdDataItems = [];
  if (cfdData && cfdData.cfdDailyValues) {
    cfdDataItems = cfdData.cfdDailyValues.map(function (item) {
      const newItem = {
        day: item.day,
        ENTERED_BOARD: item.enteredBoard,
        ANALYSIS: item.phaseValues.ANALYSIS,
        DEVELOPMENT: item.phaseValues.DEVELOPMENT,
        DEPLOYED: item.phaseValues.DEPLOYED
      }
      return newItem;
    });
  }
  return cfdDataItems;
}

function getCfdConfigs(phases, board) {
  const enteredBoardConfig = {
    color: "#"+board.enteredBoardTrackLineColor,
    valueField: constants.ENTERED_BOARD,
    title: constants.ENTERED_BOARD_TITLE
  };
  let cfdConfigs = [enteredBoardConfig];
  _.forOwn(phases, function(value, key){
    if (key !== constants.TEST) {
      cfdConfigs.push({
        color: "#"+value.color,
        valueField: key,
        title: value.name
      })
    }
  });
  return cfdConfigs;
}
