package fi.aalto.ekanban.difficulty.medium;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, features = "classpath:fi/aalto/ekanban/difficulty/medium/")
public class RunMediumDifficulty { }
