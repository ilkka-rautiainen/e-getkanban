import { ActionsObservable } from 'redux-observable';
import { Observable } from 'rxjs/Observable';
import { START_GAME, SET_GAME_DATA } from '../../../actions/actionTypes';
import { startGame } from '../../../actions/index';
import startGameEpic from '../../../epics/startGame';
import game from "../../resources/initialData";


describe('startGameEpic', () => {

  it('should emit a SET_GAME_DATA action with different game objects in action payload', () => {
    const mockResponse = {response: game};
    const dependencies = {
        ajaxPost: () => Observable.of(mockResponse)
    };
    const payload = { playerName: "player", difficultyLevel: "NORMAL"};

    const actions$ = ActionsObservable.of(startGame(payload.playerName, payload.difficultyLevel));

    //this is an example how to inject mocked response into epic for testing purposes
    return startGameEpic(actions$, null, dependencies).toPromise()
      .then((actionReceived) => {
        expect(actionReceived.type).toBe(SET_GAME_DATA);
        expect(actionReceived.payload).toHaveProperty('board');
        expect(actionReceived.payload).toHaveProperty('phases');
        expect(actionReceived.payload).toHaveProperty('columns');
        expect(actionReceived.payload).toHaveProperty('cards');
        expect(actionReceived.payload).toHaveProperty('phasePoints');
        expect(actionReceived.payload).toHaveProperty('wipLimits');
        expect(actionReceived.payload).toHaveProperty('cfdData');
        expect(actionReceived.payload).toHaveProperty('cfdConfig');
      })
  })

})
