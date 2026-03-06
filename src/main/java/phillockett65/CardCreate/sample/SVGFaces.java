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
 * Interface for the SVGFaces class.
 */

package phillockett65.CardCreate.sample;

public class SVGFaces {

    static CJ cj = new CJ();
    static CQ cq = new CQ();
    static CK ck = new CK();
    static DJ dj = new DJ();
    static DQ dq = new DQ();
    static DK dk = new DK();
    static HJ hj = new HJ();
    static HQ hq = new HQ();
    static HK hk = new HK();
    static SJ sj = new SJ();
    static SQ sq = new SQ();
    static SK sk = new SK();

    public static Face getFace(String id) {
        switch (id) {
        case "CJ": return cj;
        case "CQ": return cq;
        case "CK": return ck;
        case "DJ": return dj;
        case "DQ": return dq;
        case "DK": return dk;
        case "HJ": return hj;
        case "HQ": return hq;
        case "HK": return hk;
        case "SJ": return sj;
        case "SQ": return sq;
        case "SK": return sk;
        }

        return null;
    }

    public static boolean isFaceColour(String id, ColourKey key) {
        Face face = getFace(id);
        if (face == null) {
            return false;
        }
        Desc[] paths = face.getDescs();
        for (int i = 0; i < paths.length; ++i) {
            if (paths[i].isKey(key))
                return true;
        }

        return false;
    }

    public static boolean isFaceColour(String id, int index) {
        return isFaceColour(id, ColourKey.getColourKey(index));
    }

    public static int getFacePathCount(String id) {
        Face face = getFace(id);
        if (face == null) {
            return 0;
        }
        return face.getDescCount();
    }

};

