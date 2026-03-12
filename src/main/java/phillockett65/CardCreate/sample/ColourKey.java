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
    CARD_ID (0),
    CLUB_INDEX_ID (1),
    DIAMOND_INDEX_ID (2),
    HEART_INDEX_ID (3),
    SPADE_INDEX_ID (4),
    CLUB_PIP_ID (5),
    DIAMOND_PIP_ID (6),
    HEART_PIP_ID (7),
    SPADE_PIP_ID (8),
    COURT_WHITE_ID (9),
    COURT_STEEL_ID (10),
    COURT_HAIR_ID (11),
    COURT_FLESH_ID (12),
    COURT_YELLOW_ID (13),
    COURT_RED_ID (14),
    COURT_BLUE_ID (15),
    COURT_BLACK_ID (16),
    MAX_KEY (17);

    public final int key;

    ColourKey(int k) { key = k; }

    public int getKey() { return key; }

    public static ColourKey getKey(int key) { 
        switch (key) {
            case 0: return CARD_ID;
            case 1: return CLUB_INDEX_ID;
            case 2: return DIAMOND_INDEX_ID;
            case 3: return HEART_PIP_ID;
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
