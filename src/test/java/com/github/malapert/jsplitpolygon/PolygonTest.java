/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.malapert.jsplitpolygon;

import com.github.malapert.jsplitpolygon.geojson.GeoJson;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
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
            String line = null;
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
            InputStream is = PolygonTest.class.getResourceAsStream("/test" + i + ".data");
            List<Coordinate> coords = parseCoordinates(is);
            Polygon polygon = new Polygon(coords);
            final boolean isSplitted = polygon.split();
            final GeoJson geojson;
            if (isSplitted) {
                Polygon[] polygons = polygon.getPolygons();
                geojson = new MultiPolygons(polygons);
            } else {
                geojson = polygon;
            }
        }
        assertTrue(true);
    }

    /**
     * Test of isClockwisedPolygon method, of class Polygon.
     */
    @Test
    public void testIsClockwisedPolygon() throws IOException {
        System.out.println("isClockwisedPolygon");
        InputStream is = PolygonTest.class.getResourceAsStream("/test0.data");
        List<Coordinate> vertices = parseCoordinates(is);
        boolean expResult = false;
        boolean result = Polygon.isClockwisedPolygon(vertices);
        assertEquals(expResult, result);
    }

    /**
     * Test of split method, of class Polygon.
     */
    @Test
    public void testSplit() throws IOException {
        System.out.println("split");
        InputStream is = PolygonTest.class.getResourceAsStream("/test0.data");
        List<Coordinate> vertices = parseCoordinates(is);        
        Polygon instance = new Polygon(vertices);
        boolean expResult = true;
        boolean result = instance.split();
        assertEquals(expResult, result);
    }

    /**
     * Test of isCut method, of class Polygon.
     */
    @Test
    public void testIsCut() throws IOException {
        System.out.println("isCut");
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
     */
    @Test
    public void testGetPolygons() throws IOException {
        System.out.println("getPolygons");
        InputStream is = PolygonTest.class.getResourceAsStream("/test0.data");
        List<Coordinate> vertices = parseCoordinates(is);        
        Polygon instance = new Polygon(vertices);
        instance.split();         
        int expResult = 2;
        Polygon[] result = instance.getPolygons();
        assertTrue(expResult == result.length);
    }

//    /**
//     * Test of getCoordinates method, of class Polygon.
//     */
//    @Test
//    public void testGetCoordinates() {
//        System.out.println("getCoordinates");
//        Polygon instance = null;
//        List<Coordinate> expResult = null;
//        List<Coordinate> result = instance.getCoordinates();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of toString method, of class Polygon.
//     */
//    @Test
//    public void testToString() {
//        System.out.println("toString");
//        Polygon instance = null;
//        String expResult = "";
//        String result = instance.toString();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of toGeoJson method, of class Polygon.
//     */
//    @Test
//    public void testToGeoJson_0args() {
//        System.out.println("toGeoJson");
//        Polygon instance = null;
//        String expResult = "";
//        String result = instance.toGeoJson();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of toGeoJson method, of class Polygon.
//     */
//    @Test
//    public void testToGeoJson_int() {
//        System.out.println("toGeoJson");
//        int indent = 0;
//        Polygon instance = null;
//        String expResult = "";
//        String result = instance.toGeoJson(indent);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
