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
 * Loc is an enumeration that captures the icon locations (as a percentage) and 
 * whether it should be up side down and provides access via getters.
 */
package phillockett65.CardCreate.sample;

/**
 *
 * @author Phil
 */
public enum ColourKey {
    WHITE_ID (0),
    STEEL_ID (1),
    FLESH_ID (2),
    HAIR_ID (3),
    YELLOW_ID (4),
    RED_ID (5),
    BLUE_ID (6),
    BLACK_ID (7),
    MAX_KEY (8);

    public final int key;

    ColourKey(int k) { key = k; }

    public int getKey() { return key; }

    public static ColourKey getColourKey(int key) { 
        switch (key) {
            case 0: return WHITE_ID;
            case 1: return STEEL_ID;
            case 2: return FLESH_ID;
            case 3: return HAIR_ID;
            case 4: return YELLOW_ID;
            case 5: return RED_ID;
            case 6: return BLUE_ID;
            case 7: return BLACK_ID;
        }

        return MAX_KEY;
    }

};
