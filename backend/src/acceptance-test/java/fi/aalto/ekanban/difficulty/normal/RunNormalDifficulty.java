package fi.aalto.ekanban.difficulty.normal;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, features = "classpath:fi/aalto/ekanban/difficulty/normal/")
public class RunNormalDifficulty { }
