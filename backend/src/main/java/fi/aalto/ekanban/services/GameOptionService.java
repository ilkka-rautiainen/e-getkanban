package fi.aalto.ekanban.services;

import static fi.aalto.ekanban.ApplicationConstants.ANALYSIS_PHASE_ID;
import static fi.aalto.ekanban.ApplicationConstants.DEPLOYED_PHASE_ID;

import org.springframework.stereotype.Service;

import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.db.phases.Column;


@Service
public class GameOptionService {

    public Game enableCardThreeDayMoveCycle(Game gameToChange) {
        Column inProgressColumnOfAnalysis = gameToChange.getBoard().getPhaseWithId(ANALYSIS_PHASE_ID).getFirstColumn();
        Column firstColumnOfDeployed = gameToChange.getBoard().getPhaseWithId(DEPLOYED_PHASE_ID).getFirstColumn();
        inProgressColumnOfAnalysis.setDayMultiplierToEnter(3);
        firstColumnOfDeployed.setDayMultiplierToEnter(3);
        return gameToChange;
    }

}
