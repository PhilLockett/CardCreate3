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
 * Interface for the Desc class.
 */

package phillockett65.CardCreate.sample;

import javafx.scene.transform.Affine;

public class Desc {

    private final CourtColourKey key;
    private final double width;
    private final double height;
    private final String path;
    private final Affine affine;

    public Desc(CourtColourKey k, double a, double b, double c, double d, double e, double f, String p) {
        key = k;
        path = p;
    
        // width = 1046.93;
        width = 163.28437;
        height = 263.19374;

        affine = new Affine(a,c,e,b,d,f);
    }

    public boolean isKey(CourtColourKey target) { return key == target; }
    public CourtColourKey getKey() { return key; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public String getPath() { return path; }
    public Affine getAffine() { return affine; }

};
