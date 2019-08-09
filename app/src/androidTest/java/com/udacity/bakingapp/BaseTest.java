package com.udacity.bakingapp;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

public abstract class BaseTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    protected @interface TabletTest {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    protected @interface PhoneTest {
    }

    @Rule
    public TestName testName = new TestName();

    protected abstract boolean isTablet();

    protected abstract boolean isPhone();

    @Before
    public void setUp() {
        assertDeviceOrSkip();
    }

    private void assertDeviceOrSkip() {
        try {
            Method m = getClass().getMethod(testName.getMethodName());
            if (m.isAnnotationPresent(TabletTest.class)) {
                Assume.assumeTrue(isTablet());
            } else if (m.isAnnotationPresent(PhoneTest.class)) {
                Assume.assumeTrue(isPhone());
            }
        } catch (NoSuchMethodException e) {
            /* no-op */
        }
    }
}
