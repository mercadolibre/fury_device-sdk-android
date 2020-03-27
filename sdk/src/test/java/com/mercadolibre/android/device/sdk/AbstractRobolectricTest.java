package com.mercadolibre.android.device.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowSQLiteConnection;

import javax.annotation.Nonnull;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@Config(
        sdk = Build.VERSION_CODES.LOLLIPOP
        , manifest = "AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
@SuppressWarnings({ "PMD.AbstractClassWithoutAbstractMethod" })
@SuppressFBWarnings(value = "MISSING_TO_STRING_OVERRIDE", justification = "We don't want a toString of this class")
public abstract class AbstractRobolectricTest {

    /**
     * Used for log messages.
     */
    @SuppressWarnings("checkstyle:membername")
    protected final String tag = getClass().getSimpleName();
    /**
     * Used to divide each test's output in the report because output from all tests in one class are merged into one.
     */
    @Rule
    public TestName name = new TestName();
    private ActivityController<? extends Activity> controller;

    /**
     * Initial configurations
     */
    @Before
    @CallSuper
    public void setUp() {
        configureLogOutput();
        Log.i(tag, String.format("===> Running test: %s#%s()", getClass().getSimpleName(), name.getMethodName()));
    }

    @SuppressFBWarnings(value = "FORBIDDEN_SYSTEM", justification = "The idea is to not depend on LOG")
    private void configureLogOutput() {
        ShadowLog.stream = System.out;
    }

    /**
     * If we have a current controller destroy it to free up space
     */
    @After
    @CallSuper
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public void tearDown() {
        ShadowSQLiteConnection.reset();
        try {
            if (controller != null) {
                controller.pause().stop().destroy();
                controller = null;
            }
        } catch (final Exception e) {
            Log.e(tag
                    , String.format("There was an exception executing %s#tearDown(): %s", getClass().getSimpleName(),
                            e.getMessage()));
        }
    }

    /**
     * Helper function to retrieve the current base context.
     *
     * @return the current base context.
     */
    @Nonnull
    @SuppressWarnings("unused")
    protected Context getBaseContext() {
        return RuntimeEnvironment.application.getBaseContext();
    }

    /**
     * Helper function to retrieve the current application context
     *
     * @return the current application context
     */
    @Nonnull
    @SuppressWarnings("unused")
    protected Context getApplicationContext() {
        return RuntimeEnvironment.application.getApplicationContext();
    }

    @Nonnull
    @SuppressWarnings("unused")
    protected Application getApplication() {
        return (Application) getContext();
    }

    /**
     * @return The execution context that should be used when working with Android components.
     */
    @Nonnull
    protected Context getContext() {
        return RuntimeEnvironment.application;
    }


}
