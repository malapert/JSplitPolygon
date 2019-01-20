/**
 * Copyright (C) 2019 - Jean-Christophe Malapert.
 *
 * This file is part of JSplitPolygon.
 * JSplitPolygon is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JSplitPolygon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JSplitPolygon.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.malapert.jsplitpolygon;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
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
        Configurator.setRootLevel(Level.OFF);        
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
        Coordinate instance = new Coordinate(12.5, -40.5);
        double expResult = -40.5;
        double result = instance.getLatitude();
        assertEquals(expResult, result, 1e-10);
    }
    
}
