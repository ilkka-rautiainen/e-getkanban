import 'rxjs';
import { Observable } from 'rxjs/Observable';
import { normalize } from 'normalizr';
import { gameSchema } from '../reducers';
import { PLAY_TURN } from "../actions/actionTypes";
import { setGameData, setDiceData, setPhasePoints, removeDice, addDice,
         enableNextRoundButton, removeAssignedDice, resetAssignedResources } from "../actions";
import constants from "../constants";

export default function playTurn(action$, store, { ajaxPut }) {
  const headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  };
  return action$.ofType(PLAY_TURN)
    .flatMap(action => {

      let gameTurnAtLeastOne = store.getState().game && store.getState().game.currentDay > 0;
      let isNormalGame = store.getState().game && store.getState().game.difficultyLevel === constants.GAME_DIFFICULTY_NORMAL;
      let nextRoundButtonDelay = isNormalGame ? 200 : 500;
      let playTurnDelay = gameTurnAtLeastOne ? 500 : 0;
      let setGameDataDelay = isNormalGame || !gameTurnAtLeastOne ? 0 : 1000;
      let addDiceDelay = isNormalGame ? 200 : 500;
      let phasePointDelay = isNormalGame ? setGameDataDelay : 1000;
      const mediumOrAdvancedGameWithNoAssignedDices = !isNormalGame && store.getState().assignedDice
                                                      && store.getState().assignedDice.length === 0;
      if (mediumOrAdvancedGameWithNoAssignedDices) {
        nextRoundButtonDelay = 200;
        phasePointDelay = 0;
        setGameDataDelay = 0;
        addDiceDelay = 0;
      }

      return Observable.concat(
        Observable.of(removeDice()),
        ajaxPut(constants.BACKEND_HOST+constants.GAMES_PATH+action.gameId, action.turn, headers).delay(playTurnDelay)
          .flatMap(data => {
            const normalizedData = normalize(data.response, gameSchema);
            return Observable.concat(
                Observable.of(setDiceData(normalizedData)), // set first the dice values
                Observable.of(addDice()).delay(addDiceDelay),
                Observable.of(setPhasePoints(normalizedData)).delay(phasePointDelay), // then advance phase point resources
                Observable.of(setGameData(normalizedData)).delay(setGameDataDelay), // finally update the whole board, move cards etc
                Observable.of(removeAssignedDice()).filter(() => store.getState().game && store.getState().game.currentDay > 1),
                Observable.of(enableNextRoundButton()).delay(nextRoundButtonDelay)
            )
          })
          .catch(error => Observable.concat(
            Observable.of(console.log(error),
              Observable.of(alert(constants.GAME_ERROR)))
          )),
        Observable.of(resetAssignedResources()).filter(() => store.getState().game && store.getState().game.currentDay > 1)
      )
    })
}
