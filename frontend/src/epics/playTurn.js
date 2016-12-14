import 'rxjs';
import { Observable } from 'rxjs/Observable';
import { normalize } from 'normalizr';
import { gameSchema } from '../reducers';
import { PLAY_TURN } from "../actions/actionTypes";
import { setGameData } from "../actions";

export default function playTurn(action$) {
  const headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  };
  return action$.ofType(PLAY_TURN)
    .mergeMap(action =>
      Observable.ajax.put("http://localhost:8080/games/"+action.gameId, action.turn, headers)
        .map(data => normalize(data.response, gameSchema))
        .map(normalizedData => setGameData(normalizedData))
        .catch(error => Observable.of(
          console.log(error)
        ))
    );
}
