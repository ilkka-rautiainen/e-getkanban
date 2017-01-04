import 'rxjs';
import { Observable } from 'rxjs/Observable';
import { normalize } from 'normalizr';
import { gameSchema } from '../reducers';
import { START_GAME } from "../actions/actionTypes";
import { setGameData } from "../actions";
import constants from "../constants";

export default function startGame(action$) {
  return action$.ofType(START_GAME)
    .mergeMap(action =>
      Observable.ajax.post(constants.BACKEND_HOST+constants.GAMES_PATH, action.payload)
        .map(data => normalize(data.response, gameSchema))
        .map(normalizedData => setGameData(normalizedData))
        .catch(error => Observable.of(
          console.log(error)
        ))
    );
}
