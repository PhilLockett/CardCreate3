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
 * DataStore is a class that serializes the settings data for saving to and 
 * restoring from disc.
 */
package phillockett65.CardCreate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import phillockett65.Debug.Debug;


public class DataStore3 implements Serializable {
    private static final long serialVersionUID = 1L;

    // Debug delta used to adjust the local logging level.
    private static final int DD = 0;

    private Boolean useStandardFaces;
    private Boolean useStandardIndices;
    private Boolean useStandardPips;
    private Boolean fileOverride;

    private String faceStyle;
    private String indexStyle;
    private String pipStyle;

    private Boolean manual;
    private String outputName;

    private Integer suit;
    private Integer card;

    private Integer cardSize;
    private Double cardHeightPX;
    private Double cardWidthPX;

    ArrayList<String> colourList; 

    private Boolean displayIndex;
    private Boolean displayCornerPip;
    private Boolean displayStandardPip;
    private Boolean displayFaceImage;
    private Boolean displayFacePip;

    ArrayList<CardItemData> cardItemDataList; 
    private Integer currentItemId;

    private Boolean independentlySetCornerRadii;
    private Double arcWidth;
    private Double arcHeight;
    private Boolean cropCorners;

    private Boolean displayCourtWatermark = true;
    private Boolean displayImageWatermark = true;
    private Boolean displayNumberWatermark = true;

    private Boolean lockX;
    private Boolean lockY;
    private Boolean leftHanded;
    private Boolean showGuideBox;

    private Boolean borderlessJokers;

    public DataStore3() {

    }

    private boolean pull(Model model) {
        boolean success = true;

        useStandardFaces = model.isStandardFaces();
        useStandardIndices = model.isStandardIndices();
        useStandardPips = model.isStandardPips();
        fileOverride = model.isFileOverride();

        faceStyle   = model.getFaceStyle();
        indexStyle  = model.getIndexStyle();
        pipStyle    = model.getPipStyle();

        manual = model.isManual();
        outputName = model.getManualOutputName();

        suit = model.getSuitIndex();
        card = model.getCardIndex();

        cardSize = getCardSize(model);
        cardHeightPX = model.getHeight();
        cardWidthPX = model.getUserWidth();

        colourList = model.buildColourList();

        displayIndex = model.isDisplayIndex();
        displayCornerPip = model.isDisplayCornerPip();
        displayStandardPip = model.isDisplayStandardPip();
        displayFaceImage = model.isDisplayFaceImage();
        displayFacePip = model.isDisplayFacePip();

        cardItemDataList = model.buildCardItemDataList();
        currentItemId = model.getCurrentItemId();

        independentlySetCornerRadii = model.isSetCornerRadiiIndependently();
        arcWidth = model.getArcWidth();
        arcHeight = model.getArcHeight();
        cropCorners = model.isCropCorners();

        displayCourtWatermark = model.isDisplayCourtWatermark();
        displayImageWatermark = model.isDisplayImageWatermark();
        displayNumberWatermark = model.isDisplayNumberWatermark();

        lockX = model.isLockX();
        lockY = model.isLockY();
        leftHanded = model.isLeftHanded();
        showGuideBox = model.isShowGuideBox();

        borderlessJokers = model.isBorderlessJokers();
 
        return success;
    }

    private boolean push(Model model) {
        boolean success = true;

        model.setUseStandardFaces(useStandardFaces);
        model.setUseStandardIndices(useStandardIndices);
        model.setUseStandardPips(useStandardPips);
        model.setFileOverride(fileOverride);

        model.setFaceStyle(faceStyle);
        model.setIndexStyle(indexStyle);
        model.setPipStyle(pipStyle);

        model.setOutputNameManually(manual);
        model.setOutputName(outputName);

        model.setSuitIndex(suit);
        model.setCardIndex(card);

        setCardSize(model);
        model.setHeight(cardHeightPX);
        model.setWidth(cardWidthPX);

        model.injectColourList(colourList);

        model.setDisplayIndex(displayIndex);
        model.setDisplayCornerPip(displayCornerPip);
        model.setDisplayStandardPip(displayStandardPip);
        model.setDisplayFaceImage(displayFaceImage);
        model.setDisplayFacePip(displayFacePip);

        model.injectCardItemDataList(cardItemDataList);
        model.setCurrentItemFromId(currentItemId);

        model.setCornerRadiiIndependently(independentlySetCornerRadii);
        model.setArcWidth(arcWidth);
        model.setArcHeight(arcHeight);
        model.setCropCorners(cropCorners);

        model.setDisplayCourtWatermark(displayCourtWatermark);
        model.setDisplayImageWatermark(displayImageWatermark);
        model.setDisplayNumberWatermark(displayNumberWatermark);

        model.setLockX(lockX);
        model.setLockY(lockY);
        model.setLeftHanded(leftHanded);
        model.setShowGuideBox(showGuideBox);

        model.setBorderlessJokers(borderlessJokers);

        return success;
    }



    /************************************************************************
     * Support code for static public interface.
     */

    public static boolean writeData() {
        boolean success = false;
        Model model = Model.getInstance();

        // Ensure that the output directory exists.
        model.makeOutputDirectory();

        DataStore3 dataStore = new DataStore3();
        dataStore.pull(model);

        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(model.getSettingsFile(3)));

            objectOutputStream.writeObject(dataStore);
            success = true;
        } catch (IOException e) {
            // e.printStackTrace();
            Debug.critical(DD, e.getMessage());
        }

        return success;
    }

    public static boolean readData() {
        boolean success = false;
        Model model = Model.getInstance();

        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(model.getSettingsFile(3)));

            DataStore3 dataStore = (DataStore3)objectInputStream.readObject();
            success = dataStore.push(model);
        } catch (IOException e) {
            Debug.critical(DD, e.getMessage());
        } catch (ClassNotFoundException e) {
            Debug.critical(DD, e.getMessage());
        }

        return success;
    }



    /************************************************************************
     * Support code for mapping data.
     */

    private void setCardSize(Model model) {
        if (cardSize == 0)
            model.setPokerCardSize();
        else if (cardSize == 1)
            model.setBridgeCardSize();
        else if (cardSize == 2)
            model.setFreeCardSize();
        else
            model.setMpcCardSize();
    }

    private int getCardSize(Model model) {
        if (model.isPokerCardSize()) return 0;

        if (model.isBridgeCardSize()) return 1;

        if (model.isFreeCardSize()) return 2;

        if (model.isMpcCardSize()) return 3;

        return 0;
    }

}

