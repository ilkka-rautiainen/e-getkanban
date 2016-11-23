import 'rxjs';
import { Observable } from 'rxjs/Observable';
import { normalize } from 'normalizr';
import { gameSchema } from '../reducers';
import { START_GAME } from "../actionTypes";
import { setGameData } from "../actions";

export default function startGame(action$) {
  return action$.ofType(START_GAME)
    .delay(1000)
    .mergeMap(action =>
      Observable.ajax.post("http://localhost:8080/games", action.payload)
        .map(data => normalize(data.response, gameSchema))
        .map(normalizedData => setGameData(normalizedData))
        .catch(error => Observable.of(
          console.log(error)
        ))
    );
}
