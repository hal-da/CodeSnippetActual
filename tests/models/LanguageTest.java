package models;

import junit.framework.TestCase;
import org.junit.Assert;

public class LanguageTest extends TestCase {
    private final String testName = "Suaheli";
    private final Language language = new Language();

    public void testTestGetName() {
        language.setName(testName);
        Assert.assertEquals(testName, language.getName());
    }
}