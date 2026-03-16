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
    CARD_ID (0, false, false, false),
    CLUB_INDEX_ID (1, true, false, false),
    DIAMOND_INDEX_ID (2, true, false, false),
    HEART_INDEX_ID (3, true, false, false),
    SPADE_INDEX_ID (4, true, false, false),
    CLUB_PIP_ID (5, false, true, false),
    DIAMOND_PIP_ID (6, false, true, false),
    HEART_PIP_ID (7, false, true, false),
    SPADE_PIP_ID (8, false, true, false),
    COURT_WHITE_ID (9, false, false, true),
    COURT_STEEL_ID (10, false, false, true),
    COURT_HAIR_ID (11, false, false, true),
    COURT_FLESH_ID (12, false, false, true),
    COURT_YELLOW_ID (13, false, false, true),
    COURT_RED_ID (14, false, false, true),
    COURT_BLUE_ID (15, false, false, true),
    COURT_BLACK_ID (16, false, false, true),
    MAX_KEY (17, false, false, false);

    public final int key;
    public final boolean index;
    public final boolean pip;
    public final boolean face;

    ColourKey(int k, boolean i, boolean p, boolean f) {
        key = k;
        index = i;
        pip = p;
        face = f;
    }

    public int getKey() { return key; }
    public boolean isIndex() { return index; }
    public boolean isPip() { return pip; }
    public boolean isFace() { return face; }

    public static ColourKey getKey(int key) { 
        switch (key) {
            case 0: return CARD_ID;
            case 1: return CLUB_INDEX_ID;
            case 2: return DIAMOND_INDEX_ID;
            case 3: return HEART_INDEX_ID;
            case 4: return SPADE_INDEX_ID;
            case 5: return CLUB_PIP_ID;
            case 6: return DIAMOND_PIP_ID;
            case 7: return HEART_PIP_ID;
            case 8: return SPADE_PIP_ID;
            case 9: return COURT_WHITE_ID;
            case 10: return COURT_STEEL_ID;
            case 11: return COURT_HAIR_ID;
            case 12: return COURT_FLESH_ID;
            case 13: return COURT_YELLOW_ID;
            case 14: return COURT_RED_ID;
            case 15: return COURT_BLUE_ID;
            case 16: return COURT_BLACK_ID;
        }

        return MAX_KEY;
    }

};
