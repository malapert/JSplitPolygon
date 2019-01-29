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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.github.malapert.jsplitpolygon.geojson.GeoJson;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

/**
 *
 * @author malapert
 */
public class JSplitPolygon {

    /**
     * logger.
     */
    private static final Logger LOG = LogManager.getLogger(JSplitPolygon.class.getName());

    private static final String APP_NAME = JSplitPolygon.class.getSimpleName();

    private static final String COPYRIGHT = "Copyright (C) 2019 - Jean-Christophe Malapert";

    private static final String VERSION = "1.0.1";

    /**
     * Displays version.
     */
    private static void displayVersion() {
        System.out.println(APP_NAME+" ("+COPYRIGHT+") - Version:"+VERSION+"\n");
        System.exit(0);
    }

    private static void displayHelp() {
        StringBuilder help = new StringBuilder();
        help.append("------------ Help for JSplitPolygon -----------\n");
        help.append("\n");
        help.append("Usage: java -jar ").append(APP_NAME).append(".jar -i <fileInput> [OPTIONS]\n");
        help.append("\n\n");
        help.append("with OPTIONS:\n");
        help.append("  -h|--help                    : This output\n");
        help.append("  -i|--input <filepath>        : input file\n");
        help.append("  -o|--output <filepath>       : Loads the configuation file\n");
        help.append("  -l|--level <level>           : debug level : OFF, INFO, DEBUG, TRACE\n");
        help.append("  -v|--version                 : version\n");
        help.append("\n");
        help.append("\n");
        System.out.println(help.toString());
        System.exit(1);
    }

    /**
     * Main
     *
     * @param argv the command line arguments
     */
    public static void main(String[] argv) throws Exception {

        File ficIn = null;
        File ficOut = null;
        Level level = Level.INFO;

        int c;
        String arg;

        StringBuffer sb = new StringBuffer();
        LongOpt[] longopts = new LongOpt[8];
        longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
        longopts[1] = new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'v');
        longopts[2] = new LongOpt("input", LongOpt.REQUIRED_ARGUMENT, null, 'i');
        longopts[3] = new LongOpt("output", LongOpt.REQUIRED_ARGUMENT, null, 'o');
        longopts[4] = new LongOpt("level", LongOpt.REQUIRED_ARGUMENT, null, 'l');

        //
        Getopt g = new Getopt("JSplitPolygon", argv, "hvi:o:l:", longopts);

        while ((c = g.getopt()) != -1) {
            switch (c) {
                case 'h':
                    LOG.debug("h option is selected");
                    displayHelp();
                    break;
                case 'v':
                    LOG.debug("v option is selected");
                    displayVersion();
                    break;
                case 'i':
                    LOG.debug("i is selected");
                    ficIn = new File(g.getOptarg());
                    if (!ficIn.canRead()) {
                        throw new Exception("Cannot read " + ficIn.toString());
                    }
                    break;
                case 'o':
                    LOG.debug("o is selected");
                    ficOut = new File(g.getOptarg());
                    ficOut.createNewFile();
                    break;
                case 'l':
                    LOG.debug("l is selected");
                    level = Level.getLevel(g.getOptarg());
                    if (level == null) {
                        throw new IllegalArgumentException("Level "+g.getOptarg()+" is not supported");
                    }
                    break;
                case '?':
                    break; // getopt() already printed an error
                //
                default:
                    System.out.println("getopt() returned "+c+"\n");
            }
        }

        for (int i = g.getOptind(); i < argv.length; i++) {
            System.out.println("Non option argv element: "+argv[i]+"\n");
        }

        if (argv.length == 0 || ficIn == null) {
            displayHelp();
        }

        try {
            Configurator.setRootLevel(level);
            LOG.info("Processing file {}", ficIn.toString());
            List<Coordinate> coordinates = new ArrayList<>();            
            FileInputStream fis = new FileInputStream(ficIn);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.
                    defaultCharset()))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(" ");
                    LOG.debug("Split values in long:{0}lat:{1}",
                            new Object[]{values[0], values[1]});
                    coordinates.add(new Coordinate(Double.parseDouble(values[0]),
                            Double.parseDouble(values[1])));
                }
            }

            Polygon polygon = new Polygon(coordinates);
            final boolean isSplitted = polygon.split();
            final GeoJson geojson;
            if (isSplitted) {
                List<Polygon> polygons = polygon.getPolygons();
                geojson = new MultiPolygons(polygons);
            } else {
                geojson = polygon;
            }

            String result = geojson.toGeoJson();

            LOG.debug("result: {}", result);
            if (ficOut == null) {
                System.out.println(result);
            } else {
                FileOutputStream fout = new FileOutputStream(ficOut);
                try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fout, Charset.
                        defaultCharset()))) {
                    bw.write(result);
                }
                LOG.info("result wrote in {}", ficOut.toString());
            }
        } catch (IOException | NumberFormatException ex) {
            LOG.error(ex);
        } catch (RuntimeException ex) {
            LOG.error(ex);
        }
    }

}
