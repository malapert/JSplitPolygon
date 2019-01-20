/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.malapert.jsplitpolygon;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.experimental.categories.Category;

/**
 *
 * @author malapert
 */
@Category(UnitTest.class)
public class CoordinateTest {
    
    public CoordinateTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getLongitude method, of class Coordinate.
     */
    @Test
    public void testGetLongitude() {
        System.out.println("getLongitude");
        Coordinate instance = new Coordinate(12.5, -40.5);
        double expResult = 12.5;
        double result = instance.getLongitude();
        assertEquals(expResult, result, 1e-10);
    }

    /**
     * Test of getLatitude method, of class Coordinate.
     */
    @Test
    public void testGetLatitude() {
        System.out.println("getLatitude");
        Coordinate instance = new Coordinate(12.5, -40.5);
        double expResult = -40.5;
        double result = instance.getLatitude();
        assertEquals(expResult, result, 1e-10);
    }

    /**
     * Test of toString method, of class Coordinate.
     */
    @Test
    @Ignore("TO DO")
    public void testToString() {
        System.out.println("toString");
        Coordinate instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
