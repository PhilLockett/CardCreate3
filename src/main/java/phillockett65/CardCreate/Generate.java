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
 * Generate is a concurrent task used to generate the card images. 
 */
package phillockett65.CardCreate;

import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import phillockett65.CardCreate.Model.DisplayType;
import phillockett65.CardCreate.sample.Default;
import phillockett65.CardCreate.sample.Item;
import phillockett65.Debug.Debug;


public class Generate extends Task<Long> {

    private final Model model;
    private Long progress;
    private Canvas canvas;
    private int defaults;

    /**
     * Generate task constructor.
     * 
     * @param progress so far.
     * @param defaults number of times no joker image file was found, used to 
     *     vary default joker generation.
     */
    public Generate(Long progress, int defaults) {
        model = Model.getInstance();
        this.progress = progress;
        this.defaults = defaults;

        // Update progress bar to stop it jittering.
        updateProgress(progress, Default.GENERATE_STEPS.getInt());
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public int getDefaults() {
        return defaults;
    }

    private class CardContext {
        private final double width;
        private final double height;
        private final double xOffset;
        private final double yOffset;
        private final Canvas canvas;
        private final GraphicsContext gc;

        public CardContext() {

            width = model.getWidth();
            height = model.getHeight();
            xOffset = model.getMpcBorderWidth();
            yOffset = model.getMpcBorderHeight();

            final double xMax = width + (xOffset * 2);
            final double yMax = height + (yOffset * 2);

            canvas = new Canvas(xMax, yMax);
            gc = canvas.getGraphicsContext2D();

            gc.setFill(model.getBackgroundColour());
            
            if (!model.isMpcCardSize()) {
                final double arcWidth = model.getArcWidthPX();
                final double arcHeight = model.getArcHeightPX();

                gc.fillRoundRect(0, 0, xMax, yMax, arcWidth, arcHeight);
                gc.setStroke(model.border);
                gc.setLineWidth(Default.BORDER_WIDTH.getInt());
                gc.strokeRoundRect(0, 0, xMax, yMax, arcWidth, arcHeight);
            } else {
                gc.fillRect(0, 0, xMax, yMax);
            }
        }

        public Canvas getCanvas() { return canvas; }
        public GraphicsContext getGraphicsContext() { return gc; }
        public double getWidth() { return width; }
        public double getHeight() { return height; }
        public double getXOffset() { return xOffset; }
        public double getYOffset() { return yOffset; }

    }


    /**
     * Generate the indicated card using the current settings.
     */
    private void generateCard() {
        final int suit = model.currentSuit();
        final int card = model.currentCard();
        final int pattern = model.currentCornerPattern();
        final int facePip = model.currentFacePattern();
        final Image[] images = model.currentImages();
        Debug.trace(0, "generateCard(" + pattern +  ")");

        // Create blank card.
        CardContext cc = new CardContext();
        GraphicsContext gc = cc.getGraphicsContext();

        // Add the icons using the Payloads in priority order.
        if (model.showWatermark(suit, card))
            gc.drawImage(model.getWatermark(), cc.getXOffset(), cc.getYOffset(), cc.getWidth(), cc.getHeight());

        Item[] priorities = model.getPriorityList();
        DisplayType type = DisplayType.NONE;
        for (int i = priorities.length-1; i >= 0; --i) {

            final Item priority = priorities[i];

            switch (priority) {
            case INDEX:
                if (model.shouldIndexBeDisplayed()) {
                    if (model.isStandardIndices()) {
                        model.drawCardIndex(gc, pattern, card);
                    } else {
                        Image image = Utils.loadImage(model.getIndexImagePath(suit, card));
                        Image rotatedImage = Utils.rotateImage(image);
            
                        model.drawCardIndex(gc, image, rotatedImage, pattern);
                    }
                }
            break;

            case CORNER_PIP:
                if (model.shouldCornerPipBeDisplayed()) {
                    if (model.isStandardPips()) {
                        model.drawCardCornerPip(gc, pattern, suit);
                    } else {
                        model.drawCardCornerPip(gc, images[2], images[3], pattern);
                    }
                }
            break;

            case STANDARD_PIP:
                type = model.getDisplayType(suit, card);
                if (type == DisplayType.NONE)
                    break;

                if (type == DisplayType.SVG_PIPS) {
                    model.drawCardStandardPip(gc, card, suit);
                } else if (type == DisplayType.FILE_PIPS) {
                    model.drawCardStandardPip(gc, images[0], images[1], card);
                }
            break;

            case FACE_PIP:
                if (model.shouldFacePipBeDisplayed(card)) {
                    if (model.isStandardPips()) {
                        model.drawCardFacePip(gc, facePip, suit);
                    } else {
                        model.drawCardFacePip(gc, images[4], images[5], facePip);
                    }
                }
            break;

            case FACE:
                type = model.getDisplayType(suit, card);
                if (type == DisplayType.NONE)
                    break;

                if (type == DisplayType.SVG_FACE) {
                    model.drawCardFace(gc, suit, card);
                } else if (type == DisplayType.FILE_FACE) {
                    Image image = Utils.loadImage(model.getFaceImagePath(suit, card));
                    Image rotatedImage = Utils.rotateImage(image);
                    
                    model.drawCardFace(gc, image, rotatedImage);
                }
            break;
            }
        }

        canvas = cc.getCanvas();
    }

    /**
     * Generate the indicated joker using the current settings.
     */
    private void generateJoker() {
        final int suit = model.currentSuit();

        // Create blank card.
        CardContext cc = new CardContext();
        GraphicsContext gc = cc.getGraphicsContext();

        // Draw Joker image specific to the suit.
        Image faceImage = Utils.loadImage(model.getFaceImagePath(suit, 0));
        if (faceImage == null) {
            defaults++;
            if (defaults % 2 == 1) {
                faceImage = Utils.loadImage(model.getBaseDirectory() + "\\boneyard\\Back.png");
            }
        }
        model.drawJokerFace(gc, faceImage);

        // Draw Joker indices specific to the suit.
        Image indexImage = Utils.loadImage(model.getJokerIndexImagePath(suit));
        if (indexImage != null) {
            Image rotatedImage = Utils.rotateImage(indexImage);

            model.drawJokerIndex(gc, indexImage, rotatedImage);
        }

        canvas = cc.getCanvas();
    }


    /**
     * Generate the card images and add them to the observable list.
     */
    @Override
    protected Long call() throws Exception {

        if (isCancelled()) {
            return progress;
        }
        
        // Generate the card.
        if (model.isCurrentJoker()) {
            generateJoker();
        } else {
            generateCard();
        }

        updateProgress(++progress, Default.GENERATE_STEPS.getInt());

        return progress;
    }

}
