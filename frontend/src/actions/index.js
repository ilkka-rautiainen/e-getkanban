import _ from 'lodash';
import { CHANGE_WIP, START_GAME, PLAY_TURN, SET_GAME_DATA, REMOVE_DICE,
         ADD_DICE, ENABLE_NEXT_ROUND, SET_ACTIVE_CARD, SET_ACTIVE_DICE,
         ASSIGN_DIE, REMOVE_ASSIGNED_DICE, SET_DICE_DATA, SET_PHASE_POINTS,
         ADD_ASSIGNED_RESOURCE, RESET_ASSIGNED_RESOURCES} from './actionTypes';
import constants from '../constants';


/*
 * action creators
 */

export function changeWip(phase, wipLimit) {
  return { type: CHANGE_WIP, phase, wipLimit };
}

export function assignResource(cardId, cardPhaseId, diePhaseId, dieIndex) {
  return {
    type: ADD_ASSIGNED_RESOURCE,
    resource: {
      cardId: cardId,
      cardPhaseId: cardPhaseId,
      diePhaseId: diePhaseId,
      dieIndex: dieIndex,
      points: 0
    }
  }
}

export function resetAssignedResources() {
  return { type: RESET_ASSIGNED_RESOURCES };
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

export function playTurn(gameId, newWipLimits, assignResourcesActions) {
  return {
    type: PLAY_TURN,
    gameId: gameId,
    turn: {
      adjustWipLimitsAction: {
        phaseWipLimits: newWipLimits
      },
      moveCardActions: [],
      drawFromBacklogActions: [],
      assignResourcesActions: assignResourcesActions,
      diceCastActions: []
    }
  }
}

export function setActiveDice(phaseId, dieIndex, diePageX, diePageY) {
  let phaseValue = null;
  if (phaseId) {
    phaseValue = {
      id: phaseId,
      diceIndex: dieIndex,
      diePageX: diePageX,
      diePageY: diePageY
    }
  }
  return {
    type: SET_ACTIVE_DICE,
    phase: phaseValue
  }
}

export function setActiveCard(cardId, phaseId, cardPageX, cardPageY) {
  let cardValue = null;
  if (cardId) {
    cardValue = {
      id: cardId,
      phaseId: phaseId,
      cardPageX: cardPageX,
      cardPageY: cardPageY
    }
  }
  return {
    type: SET_ACTIVE_CARD,
    card: cardValue
  }
}

export function assignDie(phaseId, dieIndex, xMovement, yMovement) {
  return {
    type: ASSIGN_DIE,
    die: {
      phaseId: phaseId,
      index: dieIndex,
      xMovement: xMovement,
      yMovement: yMovement
    }
  }
}

export function removeAssignedDice() {
  return { type: REMOVE_ASSIGNED_DICE }
}

export function setDiceData(normalizedData) {
  const game = normalizedData.entities.games[normalizedData.result];
  const diceCastActions = game.lastTurn.diceCastActions;
  return {
    type: SET_DICE_DATA,
    dice: diceCastActions
  }
}

export function setPhasePoints(normalizedData) {
  return {
    type: SET_PHASE_POINTS,
    phasePoints: normalizedData.entities.cardPhasePoints
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
