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
package com.github.malapert.jsplitpolygon.geojson;

import com.github.malapert.jsplitpolygon.MultiPolygons;
import com.github.malapert.jsplitpolygon.Polygon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * GeoJSon factory
 * @author Jean-Christophe Malapert
 */
public class GeoJsonFactory {
    
    /**
     * logger.
     */
    private static final Logger LOG = LogManager.getLogger(GeoJsonFactory.class.getName());    

    /**
     * Creates GeoJSon
     * @param polygons polygons
     * @return a GeoJSon
     * @throws IllegalArgumentException polygons cannot be null or empty
     */
    public static GeoJson create(final Polygon[] polygons) {
        LOG.traceEntry("Factory with {} polygons", polygons.length);
        final GeoJson geojson;
        switch (polygons.length) {
            case 0:
                LOG.debug("polygons length is empty.");
                throw LOG.throwing(new IllegalArgumentException("polygons cannot be null or empty"));
            case 1:
                LOG.debug("polygons length is one.");
                geojson = new Polygon(polygons[0]);
                break;
            default:
                LOG.debug("polygons length is mutitple");
                geojson = new MultiPolygons(polygons);
                break;
        }
        return LOG.traceExit(geojson);
    }

}
