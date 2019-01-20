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

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.malapert.jsplitpolygon.geojson.GeoJson;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

public class MultiPolygons implements GeoJson {

    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(MultiPolygons.class.getName());

    /**
     * polygons.
     */
    private final Polygon[] polygons;

    /**
     * Creates a multi-polygons based on several polygons.
     * @param polygons polygons
     */
    public MultiPolygons(final Polygon[] polygons) {
        this.polygons = Arrays.copyOf(polygons, polygons.length);
    }

    /**
     * Builds GeoJSon.
     * @return a GeoJson object
     * @throws JSONException when a problem happens
     */
    private JSONObject buildGeoJson() throws JSONException {
        LOG.traceEntry();
        JSONArray coords = new JSONArray();
        for (Polygon poly : polygons) {
            JSONArray polygon = new JSONArray();
            JSONArray noHole = new JSONArray();
            List<Coordinate> coordsPoly = poly.getCoordinates();
            for (Coordinate coord : coordsPoly) {
                JSONArray point = new JSONArray();
                point.put(coord.getLongitude());
                point.put(coord.getLatitude());
                noHole.put(point);
            }

            noHole.put(noHole.get(0));
            polygon.put(noHole);
            coords.put(polygon);
        }

        JSONObject json = new JSONObject();
        json.put("type", "MultiPolygon");
        json.put("coordinates", coords);
        return LOG.traceExit(json);
    }

    @Override
    public String toGeoJson() {
        LOG.traceEntry();
        try {
            return LOG.traceExit(buildGeoJson().toString());
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
            LOG.error(ex);
            throw LOG.throwing(new RuntimeException(ex));
        }
    }

}
