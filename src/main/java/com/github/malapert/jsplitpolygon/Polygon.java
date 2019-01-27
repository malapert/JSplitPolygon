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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.malapert.jsplitpolygon.geojson.GeoJson;
import org.json.JSONException;

/**
 *
 * This class handles a Polygon
 * @author Jean-Christophe Malapert
 */
public class Polygon implements GeoJson {

    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(Polygon.class.getName());
    
    /**
     * Half longitude of the sphere.
     */
    private static final double HALF_LONGITUDE = 180;

    /**
     * points in firstPolygon.
     */
    private final List<Coordinate> coordinates;
    
    //polygons
    private Polygon[] polygons;

    /**
     * Creates a firstPolygon based on a firstPolygon.
     * @param polygon firstPolygon
     */
    public Polygon(final Polygon polygon) {
        this(polygon.getCoordinates());
    }

    /**
     * Creates a firstPolygon based on a list of points.
     * @param coordinates points
     */
    public Polygon(final List<Coordinate> coordinates) {
        LOG.info("Creates a polygon of {} points", coordinates.size());
        final List<Coordinate> coords = new ArrayList<Coordinate>(coordinates);
        if (Polygon.isClockwisedPolygon(coords)) {
            LOG.info("polygon is clockwised, reverses the points");
            Collections.reverse(coords);
        }
        this.coordinates = coords;
    }

    /**
     * Test whether the firstPolygon is clockwise.
     *
     * @param vertices vertices
     * @return True when the firstPolygon is clockwise other False
     */
    public static boolean isClockwisedPolygon(final List<Coordinate> vertices) {
        LOG.traceEntry("Parameters - vertices : {}", vertices);
        double sum = 0.0;
        for (int i = 0; i < vertices.size(); i++) {
            Coordinate v1 = vertices.get(i);
            Coordinate v2 = vertices.get((i + 1) % vertices.size());
            sum += (v2.getLongitude() - v1.getLongitude()) * (v2.getLatitude() + v1.getLatitude());
        }
        LOG.debug("Polygon is clockwised : {0}", sum > 0.0);
        return LOG.traceExit(sum > 0.0);
    }

    public boolean split() {
        LOG.traceEntry();
        boolean isSplitted = false;
        List<Integer> indexAntiMeridian = new ArrayList<Integer>();
        if (isPassAntiMeridian(indexAntiMeridian)) {
            
            LOG.debug("polygon crosses the antimeridian");
            LOG.debug("indexAntiMeridian from coord : {}", indexAntiMeridian);
            List<Coordinate> ptsAntiMeridian = cut(indexAntiMeridian);
            LOG.debug("Computed antiMeridian pts : {}", ptsAntiMeridian);
            this.polygons = createPolygons(indexAntiMeridian, ptsAntiMeridian);
            isSplitted = true;
        }

        return LOG.traceExit(isSplitted);
    }
    
    private Polygon[] createPolygons(final List<Integer> indexAntiMeridian, final List<Coordinate> ptsAntiMeridian) { 
        LOG.traceEntry("Parameters - indexAntiMeridian: {}   ptsAntiMeridian: {}", indexAntiMeridian, ptsAntiMeridian);
        final int numberPolygons = indexAntiMeridian.size()/2+1;        
        LOG.info("number of polygons to create: {}", numberPolygons);
        final Polygon[] polygons = new Polygon[numberPolygons];
        final List<Coordinate> otherPolygon = new ArrayList<Coordinate>();
        final List<Coordinate> firstPolygon = new ArrayList<Coordinate>();
        
        Coordinate meridian1;
        Coordinate meridian2;        
        int indexPolygon = 1;
        
        // save the first points until the last point before the meridian
        firstPolygon.addAll(this.coordinates.subList(0, indexAntiMeridian.get(0)));

        // iter on indexes from coordinates. The index is just after the meridian.
        for (int i = 1 ; i < indexAntiMeridian.size() - 1; i++) {            
            // the two meridians
            meridian1 = ptsAntiMeridian.get(i-1);
            meridian2 = ptsAntiMeridian.get(i);
            
            // save the two meridians
            firstPolygon.add(meridian1);
            firstPolygon.add(new Coordinate(meridian2.getLongitude() * -1, meridian2.getLatitude()));
            
            // save the points from the point just after the second meridian to the pts just before the next meridian
            firstPolygon.addAll(this.coordinates.subList(indexAntiMeridian.get(i), indexAntiMeridian.get(i+1)));   

            // create other polygon
            otherPolygon.clear();
            otherPolygon.add(new Coordinate(meridian1.getLongitude() * -1, meridian1.getLatitude()));        
            otherPolygon.addAll(this.coordinates.subList(indexAntiMeridian.get(i-1), indexAntiMeridian.get(i)));
            otherPolygon.add(meridian2);
            
            // save the other polygon
            polygons[indexPolygon++] = new Polygon(otherPolygon);
        }
        meridian1 = ptsAntiMeridian.get(ptsAntiMeridian.size()-2);
        meridian2 = ptsAntiMeridian.get(ptsAntiMeridian.size()-1);
        
        firstPolygon.add(meridian1);
        firstPolygon.add(new Coordinate(meridian2.getLongitude() * -1, meridian2.getLatitude()));
        firstPolygon.addAll(this.coordinates.subList(indexAntiMeridian.get(indexAntiMeridian.size()-1), this.coordinates.size() - 1));         
        
        otherPolygon.clear();        
        otherPolygon.add(new Coordinate(meridian1.getLongitude() * -1, meridian1.getLatitude()));        
        otherPolygon.addAll(this.coordinates.subList(indexAntiMeridian.get(indexAntiMeridian.size()-2), indexAntiMeridian.get(indexAntiMeridian.size()-1)));
        otherPolygon.add(meridian2);
        polygons[indexPolygon++] = new Polygon(otherPolygon);
        polygons[0] = new Polygon(firstPolygon);
        return LOG.traceExit(polygons);
    }

    /**
     * Tests if the firstPolygon crosses the anti-meridian.
     * @param indexAntiMeridian indexes for which the firstPolygon crosses the anti-meridian
     * @return 
     */
    private boolean isPassAntiMeridian(final List<Integer> indexAntiMeridian) {
        LOG.traceEntry("Parameters - indedexAntiMeridian : {}", indexAntiMeridian);
        boolean isPassAntiMeridian = false;
        Coordinate c1 = this.coordinates.get(0);
        for (int i = 1; i < this.coordinates.size(); i++) {
            Coordinate c2 = this.coordinates.get(i);
            if (Math.abs(c2.getLongitude() - c1.getLongitude()) > HALF_LONGITUDE) {
                isPassAntiMeridian = true;
                indexAntiMeridian.add(i);
            }
            c1 = c2;
        }
        if (Math.abs(this.coordinates.get(0).getLongitude() - this.coordinates.get(this.coordinates.
                size() - 1).getLongitude()) > HALF_LONGITUDE) {
            indexAntiMeridian.add(0);
        }
        
        if(indexAntiMeridian.size() == 1) {
            isPassAntiMeridian = false;
            LOG.warn("The meridian is crossed only once instead of two !!! "
                    + "Does not cross the meridian. The source polygon is returned");
        } else if(indexAntiMeridian.size() > 2) {
            throw LOG.throwing(new RuntimeException("Polygon not supported"));
        }

        return LOG.traceExit(isPassAntiMeridian);
    }

    /**
     * Computes firstPolygon from the anti-meridian indexes
     * @param indexAntiMeridian anti-meridian idexes
     * @return firstPolygon represented by a list of points
     */
    private List<Coordinate> cut(List<Integer> indexAntiMeridian) {
        LOG.traceEntry("Parameter - indexAntiMeridian : {}", indexAntiMeridian);
        List<Coordinate> ptsAntiMeridian = new ArrayList<Coordinate>();
        for (Integer index : indexAntiMeridian) {
            Coordinate[] antiMeridian = extractCoordinateForAntiMeridian(index);
            Double[] equation = computeLinearRegression(antiMeridian[0], antiMeridian[1]);
            Coordinate coordinateAntiMeridian = computePointAntiMeridian(equation);
            ptsAntiMeridian.add(coordinateAntiMeridian);
        }
        return LOG.traceExit(ptsAntiMeridian);
    }

    /**
     * Computes intersection between firstPolygon and anti-meridian.
     * @param equation coefficient of the linear equation between the two points that cross the
     * anti-meridian
     * @return the intersection coordinate
     */
    private Coordinate computePointAntiMeridian(Double[] equation) {
        LOG.traceEntry("Parameter - a: {}, b: {}, firstPt: {}", equation[0], equation[1], equation[2]);
        double a = equation[0];
        double b = equation[1];
        double longitude = HALF_LONGITUDE;
        double latitude = a * longitude + b;
        return LOG.traceExit(new Coordinate(longitude * (equation[2] > 0 ? 1 : -1), latitude));
    }

    /**
     * Extracts coordinate from an indexPolygon.
     * @param index indexPolygon to extract
     * @return coordinate
     */
    private Coordinate[] extractCoordinateForAntiMeridian(int index) {
        LOG.traceEntry("Paramaeter - index: {}", index);
        Coordinate c2 = this.coordinates.get(index);
        Coordinate c1;
        if (index == 0) {
            c1 = this.coordinates.get(this.coordinates.size() - 1);
        } else {
            c1 = this.coordinates.get(index - 1);
        }
        return LOG.traceExit(new Coordinate[]{c1, c2});
    }

    /**
     * Returns a and b from y = ax + b and the first longitude.
     *
     * @param c1 coordinate 1
     * @param c2 coordinate 2
     * @return the coefficients of the equation
     */
    private Double[] computeLinearRegression(Coordinate c1, Coordinate c2) {
        LOG.traceEntry("Parameters - c1: {}, c2: {}", c1, c2);
        double long1 = c1.getLongitude() > 0 ? c1.getLongitude() : c1.getLongitude() + 360;
        double long2 = c2.getLongitude() > 0 ? c2.getLongitude() : c2.getLongitude() + 360;
        double a = (c2.getLatitude() - c1.getLatitude()) / (long2 - long1);
        double b = c1.getLatitude() - a * long1;
        return LOG.traceExit(new Double[]{a, b, c1.getLongitude()});
    }

    /**
     * Builds GeoJSON
     * @return jsonObject
     * @throws JSONException when a problem happens 
     */
    private JSONObject buildGeoJson() throws JSONException {
        LOG.traceEntry();
        JSONArray coords = new JSONArray();
        JSONArray polygon = new JSONArray();
        List<Coordinate> coordsPoly = this.getCoordinates();
        for (Coordinate coord : coordsPoly) {
            JSONArray point = new JSONArray();
            point.put(coord.getLongitude());
            point.put(coord.getLatitude());
            polygon.put(point);
        }
        coords.put(polygon);
        JSONObject json = new JSONObject();
        json.put("type", "Polygon");
        json.put("coordinates", coords);
        return LOG.traceExit(json);
    }

    /**
     * Tests if the firstPolygon is cut.
     * @return True when the firstPolygon is cut otherwise False
     */
    public boolean isCut() {
        LOG.traceEntry();
        return LOG.traceExit(this.polygons.length > 1);
    }

    /**
     * Returns the firstPolygon(s).
     * @return the firstPolygon(s)
     */
    public Polygon[] getPolygons() {
        LOG.traceEntry();
        return LOG.traceExit(this.polygons);
    }

    /**
     * Returns the coordinates of the firstPolygon to cut.
     * @return the coordinates of the firstPolygon to cut
     */
    public List<Coordinate> getCoordinates() {
        LOG.traceEntry();
        return LOG.traceExit(this.coordinates);
    }

    @Override
    public String toString() {
        LOG.traceEntry();
        StringBuilder toJson = new StringBuilder();
        for (int i = 0; i < this.coordinates.size() - 1; i++) {
            toJson.append(this.coordinates.get(i).toString()).append(",");
        }
        toJson.append(this.coordinates.get(this.coordinates.size() - 1));
        return LOG.traceExit(toJson.toString());
    }

    @Override
    public String toGeoJson() {
        LOG.traceEntry();
        try {
            return LOG.traceExit(buildGeoJson().toString(0));
        } catch (JSONException ex) {
            LOG.error(ex);
            throw LOG.throwing(new RuntimeException(ex));
        }
    }

    @Override
    public String toGeoJson(int indent) {
        LOG.traceEntry("Parameter - indent: {}", indent);
        try {
            return LOG.traceExit(buildGeoJson().toString(indent));
        } catch (JSONException ex) {
            LOG.error((ex));
            throw LOG.throwing(new RuntimeException(ex));
        }
    }
}
