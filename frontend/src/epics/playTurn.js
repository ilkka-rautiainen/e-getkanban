import 'rxjs';
import { Observable } from 'rxjs/Observable';
import { normalize } from 'normalizr';
import { gameSchema } from '../reducers';
import { PLAY_TURN } from "../actions/actionTypes";
import { setGameData, removeDice, addDice, enableNextRoundButton } from "../actions";
import constants from "../constants";

export default function playTurn(action$, store) {
  const headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  };
  return action$.ofType(PLAY_TURN)
    .flatMap(action => {
      let delay = store.getState().game && store.getState().game.currentDay > 0 ? 500 : 0;
      return Observable.concat(
        Observable.of(removeDice()),
        Observable.of(addDice()).filter(() => store.getState().game && store.getState().game.currentDay > 0).delay(1000),
        Observable.ajax.put(constants.BACKEND_HOST+constants.GAMES_PATH+action.gameId, action.turn, headers).delay(delay)
          .map(data => normalize(data.response, gameSchema))
          .map(normalizedData => setGameData(normalizedData))
          .catch(error => Observable.of(
            console.log(error)
          )),
        Observable.of(enableNextRoundButton()).filter(() => store.getState().game && store.getState().game.currentDay > 0)
      )
    })
}
