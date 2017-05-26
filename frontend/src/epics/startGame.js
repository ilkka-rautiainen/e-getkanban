import 'rxjs';
import { Observable } from 'rxjs/Observable';
import { normalize } from 'normalizr';
import { gameSchema } from '../reducers';
import { START_GAME } from "../actions/actionTypes";
import { setGameData } from "../actions";
import constants from "../constants";

export default function startGame(action$, store, { ajaxPost }) {
  return action$.ofType(START_GAME)
    .mergeMap(action =>
      ajaxPost(constants.BACKEND_HOST+constants.GAMES_PATH, action.payload)
        .map(data => normalize(data.response, gameSchema))
        .map(normalizedData => setGameData(normalizedData))
        .catch(error => Observable.concat(
          Observable.of(console.log(error),
          Observable.of(alert(constants.GAME_ERROR)))
        ))
    );
}
