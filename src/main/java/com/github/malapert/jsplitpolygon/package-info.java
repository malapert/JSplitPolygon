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
/**
 * This packages provides the classes to cut a polygon to two polygons when the polygon crosses
 * the antimeridian.
 * 
 * <pre>
 * <code>
 * final Polygon polygon = new Polygon(coordinates);
 * final boolean isSplitted = polygon.split();
 * final GeoJson geojson;
 * if (isSplitted) {
 *   Polygon[] polygons = polygon.getPolygons();
 *   geojson = new MultiPolygons(polygons);
 * } else {
 *   geojson = polygon;
 * }
 * final String result = geojson.toGeoJson();
 * </code>
 * </pre>
 */
package com.github.malapert.jsplitpolygon;
