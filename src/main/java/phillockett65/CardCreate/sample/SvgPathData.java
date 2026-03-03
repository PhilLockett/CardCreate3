/*  CardCreate3 - a JavaFX based playing card image generator.
 *
 *  Copyright 2026 Philip Lockett.
 *
 *  This file is part of CardCreate3.
 *
 *  CardCreate3 is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CardCreate3 is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CardCreate3.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * SvgPathData captures an SVG path identified by a symbol.
 */
package phillockett65.CardCreate.sample;

import javafx.scene.shape.SVGPath;

public class SvgPathData {
    private final String symbol;
    private final double width;
    private final double height;
    private final String path;

    public SvgPathData(String s, double w, double h, String p) {
        symbol = s;
        path = p;

        SVGPath pathSize = new SVGPath();
        pathSize.setContent(path);
        width = pathSize.getBoundsInLocal().getWidth();
        height = pathSize.getBoundsInLocal().getHeight();

        // width = w;
        // height = h;
    }

    public String getSymbol() { return symbol; }
    public boolean isSymbol(String target) { return target.equals(symbol); }

    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getAspectRatio() { return width/height; }
    public String getPath() { return path; }
    public String getBorder() {
        // return "M 0 0 h " + width + " v " + height + " h -" + width + " Z";
        return "M 0 0 l " + width + " " + height + " M " + width + " 0 l -" + width + " " + height;
    }
    
    public boolean isLandscape() { return getHeight() < getWidth(); }
    public boolean isPortrait() { return !isLandscape(); }

    public double getScale(double w, double h) {
        final double aspectRation = w / h;

        if (getAspectRatio() < aspectRation)
        {
            return w / getWidth();

        }

        return h / getHeight();
    }

};
