package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(publish = true,features = {"src/test/java/features"},glue ="stepdefinitions",plugin = {"json:target/cucumber.json","pretty"})

public class TestRunner extends AbstractTestNGCucumberTests {

}
