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

import com.github.malapert.jsplitpolygon.geojson.GeoJson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.experimental.categories.Category;

/**
 *
 * @author malapert
 */
@Category(UnitTest.class)
public class PolygonTest {

    public PolygonTest() {
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

    private List<Coordinate> parseCoordinates(InputStream is) throws IOException {
        List<Coordinate> coordinates = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.
                defaultCharset()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(" ");
                coordinates.add(new Coordinate(Double.parseDouble(values[0]),
                        Double.parseDouble(values[1])));
            }
        }
        return coordinates;
    }

    @Test
    public void testPolygons() throws IOException {
        for (int i = 0; i <= 17; i++) {
            try (InputStream is = PolygonTest.class.getResourceAsStream("/test" + i + ".data")) {
                List<Coordinate> coords = parseCoordinates(is);
                Polygon polygon = new Polygon(coords);
                final boolean isSplitted = polygon.split();
                final GeoJson geojson;
                if (isSplitted) {
                    List<Polygon> polygons = polygon.getPolygons();
                    geojson = new MultiPolygons(polygons);
                } else {
                    geojson = polygon;
                }
            }
        }
        assertTrue(true);
    }

    @Test
    public void testPolygonsWithRef() throws IOException {
        boolean result = true;
        for (int i = 0; i <= 19; i++) {
            InputStream is_expected;
            
            try (InputStream is = PolygonTest.class.getResourceAsStream("/test" + i + ".data")) {
                is_expected = PolygonTest.class.getResourceAsStream(
                        "/test" + i + "_result.data");
                Scanner s = new Scanner(is_expected).useDelimiter("\\A");
                String result_txt = s.hasNext() ? s.next() : "";
                List<Coordinate> coords = parseCoordinates(is);
                Polygon polygon = new Polygon(coords);
                final boolean isSplitted = polygon.split();
                final GeoJson geojson;
                if (isSplitted) {
                    List<Polygon> polygons = polygon.getPolygons();
                    geojson = new MultiPolygons(polygons);
                } else {
                    geojson = polygon;
                }   
                result = result && geojson.toGeoJson().equals(result_txt);
            }
            is_expected.close();
        }
        assertTrue(result);
    }

    /**
     * Test of isClockwisedPolygon method, of class Polygon.
     * @throws java.io.IOException
     */
    @Test
    public void testIsClockwisedPolygon() throws IOException {
        InputStream is = PolygonTest.class.getResourceAsStream("/test0.data");
        List<Coordinate> vertices = parseCoordinates(is);
        boolean expResult = false;
        boolean result = Polygon.isClockwisedPolygon(vertices);
        assertEquals(expResult, result);
    }

    /**
     * Test of split method, of class Polygon.
     * @throws java.io.IOException
     */
    @Test
    public void testSplit() throws IOException {
        InputStream is = PolygonTest.class.getResourceAsStream("/test0.data");
        List<Coordinate> vertices = parseCoordinates(is);
        Polygon instance = new Polygon(vertices);
        boolean expResult = true;
        boolean result = instance.split();
        assertEquals(expResult, result);
    }

    /**
     * Test of isCut method, of class Polygon.
     * @throws java.io.IOException
     */
    @Test
    public void testIsCut() throws IOException {
        InputStream is = PolygonTest.class.getResourceAsStream("/test0.data");
        List<Coordinate> vertices = parseCoordinates(is);
        Polygon instance = new Polygon(vertices);
        instance.split();
        boolean expResult = true;
        boolean result = instance.isCut();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPolygons method, of class Polygon.
     * @throws java.io.IOException
     */
    @Test
    public void testGetPolygons() throws IOException {
        InputStream is = PolygonTest.class.getResourceAsStream("/test0.data");
        List<Coordinate> vertices = parseCoordinates(is);
        Polygon instance = new Polygon(vertices);
        instance.split();
        int expResult = 2;
        List<Polygon> result = instance.getPolygons();
        assertTrue(expResult == result.size());
    }

    /**
     * Test of getCoordinates method, of class Polygon.
     */
    @Test
    public void testGetCoordinates() {
        List<Coordinate> coord = Arrays.asList(
                new Coordinate(0, 0),
                new Coordinate(10, 0),
                new Coordinate(10, 10),
                new Coordinate(0, 10),
                new Coordinate(0, 0)
        );
        Polygon instance = new Polygon(coord);
        List<Coordinate> expResult = coord;
        List<Coordinate> result = instance.getCoordinates();
        assertEquals(expResult, result);
    }

}
