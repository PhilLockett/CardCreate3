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
    CARD_ID (0, ColourKeyType.NONE),
    CLUB_INDEX_ID (1, ColourKeyType.INDEX),
    DIAMOND_INDEX_ID (2, ColourKeyType.INDEX),
    HEART_INDEX_ID (3, ColourKeyType.INDEX),
    SPADE_INDEX_ID (4, ColourKeyType.INDEX),
    CLUB_PIP_ID (5, ColourKeyType.PIP),
    DIAMOND_PIP_ID (6, ColourKeyType.PIP),
    HEART_PIP_ID (7, ColourKeyType.PIP),
    SPADE_PIP_ID (8, ColourKeyType.PIP),
    COURT_WHITE_ID (9, ColourKeyType.COURT),
    COURT_STEEL_ID (10, ColourKeyType.COURT),
    COURT_HAIR_ID (11, ColourKeyType.COURT),
    COURT_FLESH_ID (12, ColourKeyType.COURT),
    COURT_YELLOW_ID (13, ColourKeyType.COURT),
    COURT_RED_ID (14, ColourKeyType.COURT),
    COURT_BLUE_ID (15, ColourKeyType.COURT),
    COURT_BLACK_ID (16, ColourKeyType.COURT),
    MAX_KEY (17, ColourKeyType.MAX);

    private enum ColourKeyType { NONE, INDEX, PIP, COURT, MAX; };
    public final int key;
    public final ColourKeyType type;

    ColourKey(int k, ColourKeyType t) {
        key = k;
        type = t;
    }

    public int getKey() { return key; }
    public boolean isIndex() { return type == ColourKeyType.INDEX; }
    public boolean isPip() { return type == ColourKeyType.PIP; }
    public boolean isFace() { return type == ColourKeyType.COURT; }

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
