package fi.aalto.ekanban.difficulty.advanced;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, features = "classpath:fi/aalto/ekanban/difficulty/advanced/", tags={"@focus"})
public class RunAdvancedDifficulty { }
