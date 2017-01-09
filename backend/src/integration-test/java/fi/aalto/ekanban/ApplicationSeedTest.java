package fi.aalto.ekanban;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.ekanban.builders.*;
import fi.aalto.ekanban.enums.FinancialValue;
import fi.aalto.ekanban.repositories.DifficultyConfigurationRepository;
import fi.aalto.ekanban.repositories.BaseCardRepository;
import fi.aalto.ekanban.repositories.PhaseRepository;

@RunWith(HierarchicalContextRunner.class)
public class ApplicationSeedTest extends SpringIntegrationTest {

    @Autowired
    private PhaseRepository phaseRepository;
    @Autowired
    private BaseCardRepository baseCardRepository;
    @Autowired
    private DifficultyConfigurationRepository difficultyConfigurationRepository;

    @Before
    public void emptyDb() {
        phaseRepository.deleteAll();
        baseCardRepository.deleteAll();
        difficultyConfigurationRepository.deleteAll();
    }

    public class whenDataAlreadyInDb {

        @After
        public void seedDb() {
            phaseRepository.deleteAll();
            baseCardRepository.deleteAll();
            difficultyConfigurationRepository.deleteAll();
            new ApplicationSeed(phaseRepository, baseCardRepository, difficultyConfigurationRepository);
        }

        public class withBaseCardsInDb {

            private Long baseCardCountBefore;

            @Before
            public void doApplicationSeed() {
                BaseCardBuilder.aBaseCard()
                        .withFinancialValue(FinancialValue.HIGH)
                        .withSubscribesWhenDeployed("1")
                        .create(baseCardRepository);
                baseCardCountBefore = baseCardRepository.count();
                new ApplicationSeed(phaseRepository, baseCardRepository, difficultyConfigurationRepository);
            }

            @Test
            public void constructorShouldNotCreateAdditionalBaseCards() {
                assertThat(baseCardRepository.count(), equalTo(baseCardCountBefore));
            }

        }

        public class withPhasesInDb {

            private Long phaseCountBefore;

            @Before
            public void doApplicationSeed() {
                PhasesBuilder.aSetOfPhases().withPhasesForAllGameDifficulties().createIfNotCreated(phaseRepository);
                phaseCountBefore = phaseRepository.count();
                new ApplicationSeed(phaseRepository, baseCardRepository, difficultyConfigurationRepository);
            }

            @Test
            public void constructorShouldNotCreateAdditionalPhases() {
                assertThat(phaseRepository.count(), equalTo(phaseCountBefore));
            }
        }

        public class withDiffConfigsInDb {

            private Long diffConfigCountBefore;

            @Before
            public void doApplicationSeed() {
                DifficultyConfigurationsBuilder.aSetOfDifficultyConfigurations()
                        .withAllDifficultyConfigurations()
                        .createIfNotCreated(difficultyConfigurationRepository);
                diffConfigCountBefore = difficultyConfigurationRepository.count();
                new ApplicationSeed(phaseRepository, baseCardRepository, difficultyConfigurationRepository);
            }

            @Test
            public void constructorShouldNotCreateAdditionalPhases() {
                assertThat(difficultyConfigurationRepository.count(), equalTo(diffConfigCountBefore));
            }
        }

    }

    public class whenDbIsEmpty {

        public class constructor {

            private Long baseCardCountBefore;
            private Long phaseCountBefore;
            private Long diffConfigCountBefore;

            @Before
            public void doApplicationSeed() {
                baseCardCountBefore = baseCardRepository.count();
                phaseCountBefore = phaseRepository.count();
                diffConfigCountBefore = difficultyConfigurationRepository.count();
                new ApplicationSeed(phaseRepository, baseCardRepository, difficultyConfigurationRepository);
            }

            @Test
            public void shouldCreatePhases() {
                assertThat(phaseRepository.count(), greaterThan(phaseCountBefore));
            }

            @Test
            public void shouldCreateBaseCards() {
                assertThat(baseCardRepository.count(), greaterThan(baseCardCountBefore));
            }

            @Test
            public void shouldCreateDiffConfigs() {
                assertThat(difficultyConfigurationRepository.count(), greaterThan(diffConfigCountBefore));
            }

        }

    }

}
