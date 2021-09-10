package models;

import junit.framework.TestCase;
import org.junit.Assert;

public class CodeSnippetTest extends TestCase {

    CodeSnippet codeSnippet = new CodeSnippet();
    private final int testId = 420;


    public void testGetId() {
        codeSnippet.setId(testId);
        Assert.assertEquals(testId, codeSnippet.getId());
    }
}