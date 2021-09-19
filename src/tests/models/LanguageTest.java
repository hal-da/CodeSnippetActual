package models;

import junit.framework.TestCase;
import org.junit.Assert;

public class LanguageTest extends TestCase {
    private final String testName = "Java";
    private final Language language = new Language(testName);

    public void testTestGetName() {
        Assert.assertEquals(testName, language.getName());
    }
}