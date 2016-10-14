package fi.aalto.ekanban;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.ekanban.builders.BaseCardBuilder;
import fi.aalto.ekanban.builders.PhaseBuilder;
import fi.aalto.ekanban.enums.FinancialValue;
import fi.aalto.ekanban.repositories.BaseCardRepository;
import fi.aalto.ekanban.repositories.PhaseRepository;

@RunWith(HierarchicalContextRunner.class)
public class ApplicationSeedTest extends SpringIntegrationTest {

    @Autowired
    private PhaseRepository phaseRepository;
    @Autowired
    private BaseCardRepository baseCardRepository;

    @Before
    public void emptyDb() {
        phaseRepository.deleteAll();
        baseCardRepository.deleteAll();
    }

    public class whenDataAlreadyInDb {

        @After
        public void seedDb() {
            phaseRepository.deleteAll();
            baseCardRepository.deleteAll();
            new ApplicationSeed(phaseRepository, baseCardRepository);
        }

        public class withBaseCardsInDb {

            private Long baseCardCountBeforeSeedAction;

            @Before
            public void doApplicationSeed() {
                BaseCardBuilder.aBaseCard()
                        .withFinancialValue(FinancialValue.HIGH)
                        .withSubscribesWhenDeployed("1")
                        .create(baseCardRepository);
                baseCardCountBeforeSeedAction = baseCardRepository.count();
                new ApplicationSeed(phaseRepository, baseCardRepository);
            }

            @Test
            public void constructorShouldNotCreateAdditionalBaseCards() {
                assertThat(baseCardRepository.count(), equalTo(baseCardCountBeforeSeedAction));
            }

        }

        public class withPhasesInDb {

            private Long phaseCountBeforeSeedAction;

            @Before
            public void doApplicationSeed() {
                PhaseBuilder.aPhase().analysis().create(phaseRepository);
                PhaseBuilder.aPhase().development().create(phaseRepository);
                PhaseBuilder.aPhase().test().create(phaseRepository);
                PhaseBuilder.aPhase().deployed().create(phaseRepository);
                phaseCountBeforeSeedAction = phaseRepository.count();
                new ApplicationSeed(phaseRepository, baseCardRepository);
            }

            @Test
            public void constructorShouldNotCreateAdditionalPhases() {
                assertThat(phaseRepository.count(), equalTo(phaseCountBeforeSeedAction));
            }

        }

    }

    public class whenDbIsEmpty {

        public class constructor {

            private Long baseCardCountBeforeSeedAction;
            private Long phaseCountBeforeSeedAction;

            @Before
            public void doApplicationSeed() {
                baseCardCountBeforeSeedAction = baseCardRepository.count();
                phaseCountBeforeSeedAction = phaseRepository.count();
                new ApplicationSeed(phaseRepository, baseCardRepository);
            }

            @Test
            public void shouldCreatePhases() {
                assertThat(phaseRepository.count(), greaterThan(phaseCountBeforeSeedAction));
            }

            @Test
            public void shouldCreateBaseCards() {
                assertThat(baseCardRepository.count(), greaterThan(baseCardCountBeforeSeedAction));
            }

        }

    }

}
