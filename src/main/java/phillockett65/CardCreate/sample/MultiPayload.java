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
 * MultiPayload extends Payload specifically for number cards. 
 */
package phillockett65.CardCreate.sample;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import phillockett65.Debug.Debug;


public class MultiPayload extends Payload {

    // Debug delta used to adjust the local logging level.
    private static final int DD = 0;


    /************************************************************************
     * Support code for ImageView array management.
     */

    // Entry 0 priveds top-left and bottom-right for standard indices, corner 
    // pips and face pips and entry 4 is used for quad indices and corner pips.
    private final static int[][] flags = {
        { 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 },
        { 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
        { 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0 },
        { 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1 },
        { 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 },
        { 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 },
        { 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 }
    };

    private final static Loc[] locationList = {
        Loc.L_0, Loc.L_1, Loc.L_2, Loc.L_3,
        Loc.L_4, Loc.L_5, Loc.L_6, Loc.L_7,
        Loc.L_8, Loc.L_9, Loc.L10, Loc.L11,
        Loc.L12, Loc.L13, Loc.L14, Loc.L15,
        Loc.L16 
    };

    private ImageView[] views;
    private SVGPath[] paths;

    /**
     * @return the active ImageView count.
     */
    private int getImageCount() {
        return locationList.length;
    }


    /**
     * Determine if the Icon/Sprite is to be displayed for the given pattern.
     * @param pattern to check.
     * @param imageIndex of the specific Icon/Sprite.
     * @return true if the Icon should be displayed.
     */
    private boolean isIconVisible(int pattern, int imageIndex) {
        return flags[pattern][imageIndex] == 1;
    }

    /**
     * Get the corresponding Loc for the indexed ImageView.
     * 
     * @param imageIndex for the ImageView in views[].
     * @return the corresponding Loc for the indicated ImageView.
     */
    private Loc getLocation(int imageIndex) {
        return locationList[imageIndex];
    }

    /**
     * Create the ImageView array to hold the image for this Payload.
     * 
     * @param group node to add the ImageViews to.
     */
    private void createImageViewArray() {
        Debug.trace(DD, "createImageViewArray(" + getImageCount() + ") :: " + item);

        int iconCount = getImageCount();
        views = new ImageView[iconCount];
        paths = new SVGPath[iconCount];

        for (int i = 0; i < getImageCount(); ++i) {
            views[i] = new ImageView();
            views[i].setPreserveRatio(true);
            paths[i] = new SVGPath();

            if (getLocation(i).getRotate()) {
                views[i].setRotate(180);
                paths[i].setRotate(180);
            }
        }
    }

    public void removeFromGroup() {
        final Group group = model.getGroup();

        for (int i = 0; i < getImageCount(); ++i) {
            group.getChildren().remove(views[i]);
            group.getChildren().remove(paths[i]);
        }
    }

    public void addToGroup() {
        final Group group = model.getGroup();

        for (int i = 0; i < getImageCount(); ++i) {
            group.getChildren().add(views[i]);
            group.getChildren().add(paths[i]);
        }
    }

    /**
     * Create the ImageView array to hold the image for this Payload and add 
     * the ImageViews to the Group.
     */
    protected void createImageViews() {
        createImageViewArray();
        addToGroup();
    }

    /**
     * Get the indexed ImageView.
     * 
     * @param imageIndex for the ImageView in views[].
     * @return the indicated ImageView.
     */
    protected ImageView getImageView(int imageIndex) {
        return views[imageIndex];
    }

    private SVGPath getSVGPath(int imageIndex) {
        return paths[imageIndex];
    }

    /**
     * set the image in all ImageViews.
     * 
     * @param image for the ImageView in views[].
     */
    protected void setImages(Image image) {
        for (int i = 0; i < getImageCount(); ++i) {
            getImageView(i).setImage(image);
        }
    }

    /**
     * Indicates whether the indexed ImageView should be visible.
     * 
     * @param imageIndex for the ImageView in views[].
     * @return true if the image should be visible, false otherwise.
     */
    private boolean isImageViewVisible(int imageIndex) {
        if (!isVisible())
            return false;

        return isIconVisible(pattern, imageIndex);
    }

    /**
     * set the visibilty for all ImageViews and SVGPaths.
     * 
     * @param imageIndex for the ImageView in views[].
     */
    protected void setVisibility() {
        if (isVisible() == false) {
            for (int i = 0; i < getImageCount(); ++i) {
                getImageView(i).setVisible(false);
                getSVGPath(i).setVisible(false);
            }
        } else if (model.shouldStandardBeDisplayed(item)) {
            for (int i = 0; i < getImageCount(); ++i) {
                getImageView(i).setVisible(false);
                getSVGPath(i).setVisible(isImageViewVisible(i));
            }
        } else {
            for (int i = 0; i < getImageCount(); ++i) {
                getImageView(i).setVisible(isImageViewVisible(i));
                getSVGPath(i).setVisible(false);
            }
        }
    }



    /************************************************************************
     * Support code for the MultiPayload class.
     */

    private int pattern = 0;

    public MultiPayload(Item it) {
        super(it);

        // Set up the image views.
        createImageViews();

        // Set up image dependent values.
        initImageViews();
    }

    /**
     * Initialize the Image Views based on item.
     */
    protected void initImageViews() {
        setPath(item);
        Debug.trace(DD, "initImageViews(" + path + ") :: " + item);

        if (path.equals(""))
            return;

        if (loadNewImageFile()) {
            setImages(getImage());
            pattern = model.getCurrentPattern(item);
        }
    }

    /**
     * Draw the SVG paths associated with this payload.
     */
    private void drawSVGPaths() {
        Debug.trace(DD, "drawSVGPaths() :: " + item);

        SvgPathData svgPathData = svgPaths.getSvgPathData(item);
        if (svgPathData == null) {
            return;
        }

        final double imageWidth = svgPathData.getWidth();
        final double imageHeight = svgPathData.getHeight();
        final Data data = new Data(imageWidth, imageHeight);

        final double scale = svgPathData.getScale(data.widthPX, data.heightPX);
        final Color colour = model.getStandardColour(item);
        
        // Adjust for scaling around the centre of the path.
        final double adjust = -0.5;
        final double dX = imageWidth * adjust;
        final double dY = imageHeight * adjust;
        final double pX = data.svgX;
        final double pY = data.svgY;

        for (int i = 0; i < getImageCount(); ++i) {
            if (isImageViewVisible(i)) {
                Loc location = getLocation(i);
                final double offX = location.getXOffset() * data.winX;
                final double offY = location.getYOffset() * data.winY;

                SVGPath svgPath = getSVGPath(i);
                svgPath.setContent(svgPathData.getPath());
                svgPath.setScaleX(scale);
                svgPath.setScaleY(scale);
                svgPath.setTranslateX(dX);
                svgPath.setTranslateY(dY);
                svgPath.relocate(pX + offX, pY + offY);
                svgPath.setFill(colour);
            }
        }
    }

    /**
     * Set PNG ImageViews associated with this payload.
     */
    private void setImageView() {
    Debug.trace(DD, "setImageView() :: " + item);
        if (!hasImage())
            return;

        final Data data = new Data(getImageWidth(), getImageHeight());

        final double pX = data.originX;
        final double pY = data.originY;

        for (int i = 0; i < getImageCount(); ++i) {
            if (isImageViewVisible(i)) {
                Loc location = getLocation(i);
                final double offX = location.getXOffset() * data.winX;
                final double offY = location.getYOffset() * data.winY;
                Debug.trace(DD, "offset = " + offX + ", " + offY);

                ImageView view = getImageView(i);
                view.relocate(pX + offX, pY + offY);
                view.setFitWidth(data.widthPX);
                view.setFitHeight(data.heightPX);
            }
        }
    }

    /**
     * Paint the icons associated with this payload.
     */
    public void setPatterns() {
        Debug.trace(DD, "setPatterns() :: " + item);

        pattern = model.getCurrentPattern(item);

        if (model.shouldStandardBeDisplayed(item)) {
            drawSVGPaths();
        } else {
            setImageView();
        }

        setVisibility();
    }



    /************************************************************************
     * Support code for Playing Card Generation.
     */

    /**
     * Draw icons to a given graphics context using the user specification.
     * 
     * @param gc graphics context to draw on.
     * @param iconImage used for the icons.
     * @param rotatedImage rotated version of the image used for the icons.
     * @param pattern indicating the arrangement of icons.
     * @return true if the icons are drawn, false otherwise.
     */
    public boolean drawCard(GraphicsContext gc, Image iconImage, Image rotatedImage, int pattern) {
        if (iconImage == null)
            return false;
        Debug.trace(DD, "drawCard(" + pattern +  ")");

        final Data data = new Data(iconImage.getWidth(), iconImage.getHeight());

        for (int i = 0; i < getImageCount(); ++i) {
            if (isIconVisible(pattern, i)) {
                final Loc loc = getLocation(i);

                final double posX = data.originX + (loc.getXOffset() * data.winX);
                final double posY = data.originY + (loc.getYOffset() * data.winY);
                
                if (loc.getRotate())
                    gc.drawImage(rotatedImage, posX, posY, data.widthPX, data.heightPX);
                else
                    gc.drawImage(iconImage, posX, posY, data.widthPX, data.heightPX);
            }
        }

        return true;
    }

    /**
     * Draw standard icons to a given graphics context using the user 
     * specification.
     * 
     * @param gc graphics context to draw on.
     * @param pattern indicating the arrangement of icons.
     * @param symbol indicating the icon to be drawn.
     * @return true if the icons are drawn, false otherwise.
     */
    public boolean drawCard(GraphicsContext gc, int pattern, String symbol) {
        Debug.trace(DD, "drawCard(" + pattern + ", " + symbol + ")");
        SvgPathData svgPathData = SvgPaths.getSvgPathData(symbol);
        SvgPathData svgRotatedPath = SvgPaths.getSvgPathData("_" + symbol);
        final Data data = new Data(svgPathData.getWidth(), svgPathData.getHeight());

        final double scale = svgPathData.getScale(data.widthPX, data.heightPX);
        final Color colour = model.getCurrentStandardColour(item);

        gc.setFill(colour);
        gc.setLineWidth(0.0);
        for (int i = 0; i < getImageCount(); ++i) {
            if (isIconVisible(pattern, i)) {
                gc.save();

                final Loc loc = getLocation(i);
                final double offX = loc.getXOffset() * data.winX;
                final double offY = loc.getYOffset() * data.winY;
                final double posX = data.originX + offX;
                final double posY = data.originY + offY;

                gc.translate(posX, posY);
                gc.scale(scale, scale);
                
                gc.beginPath();
                if (loc.getRotate()) {
                    gc.appendSVGPath(svgRotatedPath.getPath());
                } else {
                    gc.appendSVGPath(svgPathData.getPath());
                }
                gc.closePath();
                gc.fill();

                gc.restore();
            }
        }

        return true;
    }


    /**
     * Draw top left and rotated bottom right images to a given graphics 
     * context for the Joker indices.
     * 
     * @param gc graphics context to draw on.
     * @param image used for the icons.
     * @param rotatedImage rotated version of the image used for the icons.
     * @return true if the icons are drawn, false otherwise.
     */
    public boolean drawJoker(GraphicsContext gc, Image image, Image rotatedImage) {
        if (image == null)
            return false;

        final double cardWidthPX = model.getWidth();
        final double cardHeightPX = model.getHeight();
        final double xOffset = model.getMpcBorderWidth();
        final double yOffset = model.getMpcBorderHeight();

        final double imageWidth = image.getWidth();
        final double imageHeight = image.getHeight();
        final boolean landscape = imageHeight < imageWidth;

        double width;
        double height;
        double posX;
        double posY;

        if (landscape) {
            height = cardHeightPX * 0.05;
            width = height * imageWidth / imageHeight;

            posX = cardHeightPX * 0.02;
            posY = model.getArcWidthPX()/4;
        } else {
            width = cardWidthPX * 0.07;
            height = width * imageHeight / imageWidth;

            posX = cardWidthPX * 0.02;
            posY = model.getArcHeightPX()/4;
        }
        gc.drawImage(image, posX + xOffset, posY + yOffset, width, height);

        posX = cardWidthPX - posX - width;
        posY = cardHeightPX - posY - height;
        gc.drawImage(rotatedImage, posX + xOffset, posY + yOffset, width, height);

        return true;
    }

}
