package com.technobells.rohit.movieexplorer;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by rohit on 30/12/15.
 */
public class FullTestSuite extends TestSuite {
    public static Test suite(){
        return new TestSuiteBuilder(FullTestSuite.class)
                .includeAllPackagesUnderHere().build();
    }

    public FullTestSuite(){
        super();
    }
}
