package com.upward.lab;

import com.upward.lab.util.UPDateTimeUtils;
import com.upward.lab.util.UPFileUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String str = UPDateTimeUtils.convertToDurationTime(10000000);
        System.out.println(str);
    }
}