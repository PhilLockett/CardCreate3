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
 * Interface for the Face class.
 */

package phillockett65.CardCreate.sample;


public class Face {

    private final String name;
    private final double width;
    private final double height;
    private final Desc[] descs;

    Face(String name, double width, double height, Desc[] descs) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.descs = descs;
    }

    public String getName() { return name; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public Desc[] getDescs() { return descs; }
    public int getDescCount() { return descs.length; }
};
