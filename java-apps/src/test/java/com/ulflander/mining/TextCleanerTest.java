package com.ulflander.mining;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/27/14
 */
public class TextCleanerTest {
    @Test
    public void testTrimLinesTab() {

        String result = TextCleaner.trimLines("\t<bold>text</bold>");

        Assert.assertEquals("Clean HTML shoudn't contain any indentation",
            "<bold>text</bold>", result);
    }

    @Test
    public void testTrimLinesTabSpace() {

        String result = TextCleaner.trimLines("\t    <bold>text</bold>    ");

        Assert.assertEquals("Clean HTML shoudn't contain any indentation",
            "<bold>text</bold>", result);
    }

    @Test
    public void testTrimLinesTabSpaceInside() {

        String result = TextCleaner.trimLines("    <bold>text    text</bold>");

        Assert.assertEquals("Clean HTML shoudn't remove 4 space in the line",
            "<bold>text    text</bold>", result);
    }
}
