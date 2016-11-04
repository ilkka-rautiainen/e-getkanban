package fi.aalto.ekanban.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import fi.aalto.ekanban.models.db.gameconfigurations.BaseCard;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.CardPhasePoint;
import fi.aalto.ekanban.repositories.BaseCardRepository;

public class CardsBuilder {
    private List<CardBuilder> cardBuilders;

    public static CardsBuilder aSetOfCards() {
        CardsBuilder builder = new CardsBuilder();
        builder.cardBuilders = new ArrayList<>();
        return builder;
    }

    public CardsBuilder withNormalDifficultyBacklog(BaseCardRepository baseCardRepository) {
        List<BaseCard> baseCards = baseCardRepository.findAll();

        cardBuilders = new ArrayList<>();
        for (Integer i = 0; i < baseCards.size(); i++) {
            CardBuilder cardFromBaseCard = CardBuilder.aCard()
                    .fromBaseCard(baseCards.get(i))
                    .withOrderNumber(i);
            cardBuilders.add(cardFromBaseCard);
        }

        return this;
    }

    public CardsBuilder withNormalDifficultyMockBacklog() {
        IntStream.range(0, 5).map(i -> i*3).forEach(i -> {
            cardBuilders.add(CardBuilder.aCard()
                    .withMockPhasePoints()
                    .withOrderNumber(i+1));
            cardBuilders.add(CardBuilder.aCard()
                    .withMockPhasePoints()
                    .withOrderNumber(i+2));
            cardBuilders.add(CardBuilder.aCard()
                    .withMockPhasePoints()
                    .withOrderNumber(i+3));
        });

        return this;
    }

    public List<Card> build() {
        return cardBuilders.stream()
                .map(CardBuilder::build)
                .collect(Collectors.toList());
    }
}
