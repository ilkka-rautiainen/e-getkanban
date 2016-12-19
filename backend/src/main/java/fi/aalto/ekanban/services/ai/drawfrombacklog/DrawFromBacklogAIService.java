package fi.aalto.ekanban.services.ai.drawfrombacklog;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fi.aalto.ekanban.builders.DrawFromBacklogActionBuilder;
import fi.aalto.ekanban.enums.BacklogDeckType;
import fi.aalto.ekanban.models.DrawFromBacklogAction;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.db.phases.Phase;

@Service
public class DrawFromBacklogAIService {
    public List<DrawFromBacklogAction> getDrawFromBacklogActions(Game game) {
        List<Card> cardsToDraw = getCardsToDraw(game);
        return cardsToDraw.stream()
                .map(card -> DrawFromBacklogActionBuilder.aDrawFromBacklogAction()
                        .withDeckType(BacklogDeckType.STANDARD)
                        .withIndexToPlaceCardAt(0)
                        .build())
                .collect(Collectors.toList());
    }

    private List<Card> getCardsToDraw(Game game) {
        List<Card> backlogDeck = game.getBoard().getBacklogDeck();
        Integer placesInFirstPhase = getPlacesInFirstPhase(game);
        Integer backlogSize = backlogDeck.size();
        return backlogDeck.subList(0, Math.min(backlogSize, placesInFirstPhase));
    }

    private Integer getPlacesInFirstPhase(Game game) {
        Phase firstPhase = game.getBoard().getPhases().get(0);
        return Math.max(0, firstPhase.getWipLimit() - firstPhase.getTotalAmountOfCards());
    }
}
