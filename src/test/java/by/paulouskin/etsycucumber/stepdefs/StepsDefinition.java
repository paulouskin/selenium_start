package by.paulouskin.etsycucumber.stepdefs;

import by.paulouskin.etsycucumber.pageobjects.EtsyComPageObject;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.stringContainsInOrder;

public class StepsDefinition {

    WebDriver webDriver = new ChromeDriver();
    private EtsyComPageObject etsyPage;

    //Given

    @Given("^I am on the main page$")
    public void goToMainPage() {
        etsyPage = new EtsyComPageObject(webDriver);
    }

    // When section

    @When("^I accept terms and conditions$")
    public void i_accept_terms_and_conditons() {
        etsyPage.acceptTermsAndConditions();
    }

    @When("^I search for \"(.*?)\" items$")
    public void search_for_items(String query) {
        etsyPage.searchForItem(query);
    }

    @When("^I search for items and apply filters:$")
    public void search_for_items_and_apply_filters(DataTable table) {
        List<List<String>> values = table.asLists();
        String searchQuery = values.get(1).get(0);
        String category = values.get(1).get(1);
        String filter = values.get(1).get(2);
        etsyPage.searchForItem(searchQuery);
        if (etsyPage.isSearchResultsVisible()){
            etsyPage.applyFilterFromCategory(category, filter);
        }

    }

    @When("^I apply \"(.*?)\" shipping filter$")
    public void applyShippingFilterOnSearchResults(String filter) {
        etsyPage.applyFilterFromCategory("Shipping" , filter);
    }

    @When("^I apply \"(.*?)\" shop location filter$")
    public void applyShopLocationFilterOnSearchResults(String filter) {
        etsyPage.applyFilterFromCategory("Shop location" , filter);
    }

    @When("^I apply \"(.*?)\" item type filter$")
    public void applyItemTypeFilterOnSearchResults(String filter) {
        etsyPage.applyFilterFromCategory("Item type" , filter);
    }

    //Then section
    @Then("^I see search results$")
    public void isSearchResultsTableVisible() {
        assertThat("Search results is visible",
                etsyPage.isSearchResultsVisible());
    }

    @Then("^I get search results for \"(.*?)\" items$")
    public void i_get_results_page_for_search_query(String items) {
        assertThat(etsyPage.getTitle(),
                stringContainsInOrder(Arrays.asList(items.split(" "))));
    }

    @Then("^next filter tags are visible:$")
    public void checkAppliedFilterTagsOnSearchResults(DataTable table) {
        List<String> expectedTags = table.rows(1).asList();
        //expectedTags.forEach(System.out::println);
        List<String> actualTags = etsyPage.getAppliedFilterTagsForSearchResults();
        for (String tag : actualTags) {
            Assertions.assertTrue(expectedTags.contains(tag),"Checking tag "+ tag);
        }
    }



    @After
    public void tearDown() {
        System.out.println("Cucumber tests ended");
        webDriver.quit();
    }

}
