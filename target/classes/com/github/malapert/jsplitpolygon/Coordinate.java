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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class handles Earth coordinates.
 * @author Jean-Christophe Malapert
 */
public class Coordinate {

    /**
     * logger.
     */
    private static final Logger LOG = LogManager.getLogger(Coordinate.class.getName());

    /**
     * longitude in decimal degree.
     */
    private final double longitude;
    /**
     * latitude in decimal degree
     */
    private final double latitude;

    /**
     * Creates a coordinate based on both longitude and latitude.
     * @param longitude longitude in decimal degree
     * @param latitude  latitude in decimal degree
     */
    public Coordinate(double longitude, double latitude) {
        LOG.traceEntry("Constructor - longitude : {}, latitude : {}", longitude, latitude);
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Returns the longitude.
     * @return the longitude in decimal degree
     */
    public double getLongitude() {
        LOG.traceEntry();
        return LOG.traceExit(longitude);
    }

    /**
     * Returns the latitude.
     * @return the latitude in decimal degree.
     */
    public double getLatitude() {
        LOG.traceEntry();
        return LOG.traceExit(latitude);
    }

    /**
     * Returns the coordinates as GeoJson array.
     * @return the coordinates as GeoJson array
     */
    @Override
    public String toString() {
        LOG.traceEntry();
        return LOG.traceExit("[" + this.longitude + ", " + this.latitude + "]");
    }

}
