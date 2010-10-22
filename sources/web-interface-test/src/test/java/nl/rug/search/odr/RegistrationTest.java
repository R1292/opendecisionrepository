package nl.rug.search.odr;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class RegistrationTest extends AbstractSelenseTestCase {

    public static final String USER_NAME = "Ben Ripkens";

    public static final String PASSWORD = "super secure";

    public static final String EMAIL = "ben@ripkens.de";




    public static void registerUserWithDefaulCredentials(Selenium selenium) {
        selenium.click("loginForm:registerLink");
        selenium.waitForPageToLoad("30000");
        selenium.type("registerForm:inPassword", PASSWORD);
        selenium.type("registerForm:inName", USER_NAME);
        selenium.type("registerForm:inEmail", EMAIL);
        selenium.click("registerForm:registrationSubmitButton");
    }




    @Test
    public void testRegister() {
        registerUserWithDefaulCredentials(selenium);

        waitForAjaxRequest();

        verifyTrue(selenium.isTextPresent("successful"));
    }




    @Test
    public void testValidation() {
        registerUserWithDefaulCredentials(selenium);

        waitForAjaxRequest();

        selenium.open("/web-interface/");
        selenium.click("loginForm:registerLink");
        selenium.waitForPageToLoad("30000");
        selenium.type("registerForm:inName", USER_NAME);
        selenium.fireEvent("registerForm:inName", "blur");
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent("User name is not unique"));
        selenium.type("registerForm:inPassword", "1234");
        selenium.fireEvent("registerForm:inPassword", "blur");
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent("Has less than allowed 5 chars"));
        selenium.type("registerForm:inEmail", "wrongFormat");
        selenium.fireEvent("registerForm:inEmail", "blur");
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent("Please provide a valid e-mail address"));
        selenium.type("registerForm:inEmail", EMAIL);
        selenium.fireEvent("registerForm:inEmail", "blur");
        waitForAjaxRequest();
        verifyTrue(selenium.isTextPresent("This email is already registered"));
    }
}