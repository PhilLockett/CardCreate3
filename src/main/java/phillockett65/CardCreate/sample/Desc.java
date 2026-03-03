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

public class Desc {

    final ColourKey key;
	final double a;
	final double b;
	final double c;
	final double d;
	final double e;
	final double f;
    final double width;
    final double height;
    final String path;

    Desc(ColourKey k, double a, double b, double c, double d, double e, double f, String p) {
        key = k;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        path = p;
    
        // width = 1046.93;
        width = 1051.5;
        height = 1673.16;
    }
};
