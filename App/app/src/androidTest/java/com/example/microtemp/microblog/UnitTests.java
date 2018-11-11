package com.example.microtemp.microblog;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.example.microtemp.microblog.activity.RegisterActivity.validateEmail;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class UnitTests {
    @Test
    public void mailTest1() {
        String testMail = "jacek@wp.pl";
        assertEquals(true, validateEmail(testMail));
    }

    @Test
    public void mailTest2() {
        String testMail = "jacekp.pl";
        assertEquals(false, validateEmail(testMail));
    }
}
