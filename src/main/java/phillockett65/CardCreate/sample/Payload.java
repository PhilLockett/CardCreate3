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
 * Payload is a class that is responsible for maintaining the image file and 
 * the icon size and position.
 */
package phillockett65.CardCreate.sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import phillockett65.CardCreate.CardItemData;
import phillockett65.CardCreate.Model;
import phillockett65.CardCreate.Utils;
import phillockett65.Debug.Debug;


public class Payload {

    // Debug delta used to adjust the local logging level.
    private static final int DD = 0;


    /************************************************************************
     * Support code for ImageView array management.
     */

    public void removeFromGroup() { }

    public void addToGroup() { }

    /**
     * Get the indexed ImageView.
     * 
     * @param imageIndex for the ImageView in views[].
     * @return the indicated ImageView.
     */
    protected ImageView getImageView(int imageIndex) {
        return null;
    }

    protected class Real {
        private final boolean height;
        private double percent = 0;
        
        public Real(boolean height) {
            this.height = height;
        }

        private double getScale() {
            return height ? model.getHeight() : model.getWidth();
        }

        public void setPercent(double value) {
            percent = value;
        }

        public void setPixels(double value) {
            percent = value * 100 / getScale();
        }
        
        public double getPercent() {
            return percent;
        }

        public double getPixels() {
            return percent * getScale() / 100;
        }

        public long getIntPixels() {
            return Math.round(getPixels());
        }

    }

    public CardItemData getData() {

        CardItemData data = new CardItemData(item.index(), 
            spriteHeight.getPercent(), 
            centreX.getPercent(), 
            centreY.getPercent());

        return data;
    }

    public boolean setData(CardItemData data) {
        if (data.getId() != item.index())
            return false;

        setSize(data.getHeight());
        setX(data.getCentreX());
        setY(data.getCentreY());

        return true;
    }



    /************************************************************************
     * Support code for the Payload class.
     */

    // "image" refers to the image in the file, 
    // "sprite" refers to the image on screen (AKA icon).
    protected Model model;
    protected SvgPaths svgPaths;

    protected final Item item;
    protected String path;
    private Image image = null;

    protected boolean display = true;
    protected final Real centreX;
    protected final Real centreY;
    protected final Real spriteHeight;


    public Payload(Item it) {
        Debug.trace(DD, "Payload(" + it + ")");

        model = Model.getInstance();
        svgPaths = SvgPaths.getInstance();

        item = it;

        centreX = new Real(false);
        centreY = new Real(true);
        spriteHeight = new Real(true);

        // Set up default percentages.
        spriteHeight.setPercent(item.getH());
        centreX.setPercent(item.getX());
        centreY.setPercent(item.getY());
    }

    /**
     * Initialize the Image Views based on item.
     */
    protected void initImageViews() { }


    /**
     * Load an image into this payload and set up the attributes for the image 
     * width and height.
     * 
     * @return true if the image file was found, false otherwise.
     */
    protected boolean loadNewImageFile() {
        Debug.trace(DD, "loadNewImageFile(" + path + ")");

        image = Utils.loadImage(path);

        return image != null;
    }

    protected void setPath(Item item) {
        path = model.getImagePath(item);
    }

    /**
     * Set up the new image file based on the current file path.
     * 
     * @return true if the new file was loaded, false otherwise.
     */
    public boolean syncImageFile() {
        return false;
    }


    /**
     * Paint the icons associated with this payload if visible.
     */
    public void setPatterns() { }

    /**
     * Synchronise to the current card size.
     */
    public void syncCardSize() { }

    private boolean isValidPercentage(double value) {
        if ((value < 0D) || (value > 100D))
            return false;

        return true;
    }

    /**
     * Set the X co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card width.
     * @return true if value is valid, false otherwise.
     */
    protected boolean setSpriteCentreX(double value) {
        if (!isValidPercentage(value))
            return false;

        centreX.setPercent(value);
        
        return true;
    }

    /**
     * Set the X co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card width.
     */
    public void setX(double value) { }

    /**
     * Set the Y co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card height.
     * @return true if value is valid, false otherwise.
     */
    protected boolean setSpriteCentreY(double value) {
        if (!isValidPercentage(value))
            return false;

        centreY.setPercent(value);

        return true;
    }

    /**
     * Set the Y co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card height.
     */
    public void setY(double value) { }

    /**
     * Set the position of the centre of the sprite and the size.
     * @param x co-ordinate as a percentage of the card width.
     * @param y co-ordinate as a percentage of the card height.
     */
    public void setPos(double x, double y) { }

    /**
     * Set the size of the sprite.
     * @param size as a percentage of the card height.
     * @return true if size is valid, false otherwise.
     */
    protected boolean setSpriteSize(double size) {
        if (!isValidPercentage(size))
            return false;

        Debug.trace(DD, "setSpriteSize(" + size + ") :: " + item);

        spriteHeight.setPercent(size);

        return true;
    }

    /**
     * Set the size of the sprite.
     * @param size as a percentage of the card height.
     */
    public void setSize(double size) { }

    /**
     * Set the position of the centre of the sprite and the size.
     * @param x co-ordinate as a percentage of the card width.
     * @param y co-ordinate as a percentage of the card height.
     * @param size as a percentage of the card height.
     */
    public void update(double x, double y, double size) { }


    /**
     * Increase the size of the sprite.
     * @param steps number of Default.STEP_SIZE steps to increase by.
     * @return true if size is increased, false otherwise.
     */
    protected boolean incSpriteSize(int steps) {
        double size = spriteHeight.getPercent();

        if (size == 100D)
            return false;

        size += Default.STEP_SIZE.getFloat() * steps;
        if (size > 100D)
            size = 100D;

        setSpriteSize(size);

        return true;
    }

    /**
     * Increase the size of the sprite.
     */
    public void incSize() { }

    /**
     * Decrease the size of the sprite.
     * @param steps number of Default.STEP_SIZE steps to decrease by.
     * @return true if size is decreased, false otherwise.
     */
    protected boolean decSpriteSize(int steps) {
        double size = spriteHeight.getPercent();

        if (size == 0D)
            return false;

        size -= Default.STEP_SIZE.getFloat() * steps;
        if (size <= 0D)
            size = Default.STEP_SIZE.getFloat();

        setSpriteSize(size);
        
        return true;
    }

    /**
     * Decrease the size of the sprite.
     */
    public void decSize() { }

    /**
     * Resize of the sprite.
     * @param steps number of Default.STEP_SIZE steps to resize by.
     */
    public void resize(int steps) { }

    protected boolean moveSpriteUp(int steps) {
        double value = centreY.getPercent();

        if (value == 0D)
            return false;

        value -= Default.STEP_SIZE.getFloat() * steps;
        if (value < 0D)
            value = 0D;

        setY(value);
        
        return true;
    }

    /**
     * Move the sprite up.
     */
    public void moveUp(int steps) { }
    public void moveUp() { }

    protected boolean moveSpriteDown(int steps) {
        double value = centreY.getPercent();

        if (value == 100D)
            return false;

        value += Default.STEP_SIZE.getFloat() * steps;
        if (value > 100D)
            value = 100D;

        setY(value);

        return true;
    }

    /**
     * Move the sprite down.
     */
    public void moveDown(int steps) { }
    public void moveDown() { }

    protected boolean moveSpriteLeft(int steps) {
        double value = centreX.getPercent();

        if (value == 0D)
            return false;

        value -= Default.STEP_SIZE.getFloat() * steps;
        if (value < 0D)
            value = 0D;

        setX(value);
        
        return true;
    }

    /**
     * Move the sprite left.
     */
    public void moveLeft(int steps) { }
    public void moveLeft() { }

    protected boolean moveSpriteRight(int steps) {
        double value = centreX.getPercent();

        if (value == 100D)
            return false;

        value += Default.STEP_SIZE.getFloat() * steps;
        if (value > 100D)
            value = 100D;

        setX(value);

        return true;
    }

    /**
     * Move the sprite right.
     */
    public void moveRight(int steps) { }
    public void moveRight() { }


    public double getCentreX() {
        return centreX.getPixels();
    }

    public double getCentreY() {
        return centreY.getPixels();
    }

    /**
     * @return the centre X co-ordinate of the sprite as a percentage of the card width.
     */
    public double getSpriteX() {
        return centreX.getPercent();
    }

    /**
     * @return the centre Y co-ordinate of the sprite as a percentage of the card height.
     */
    public double getSpriteY() {
        return centreY.getPercent();
    }

    /**
     * @return the height of the sprite as a percentage of the card height.
     */
    public double getSpriteH() {
        return spriteHeight.getPercent();
    }

    /**
     * @return the Item this Payload represents.
     */
    public Item getItem() {
        return item;
    }

    /**
     * Indicates if the payload has an associated image file.
     * @return true if the Payload has an image file.
     */
    public boolean hasImage() {
        return (image != null);
    }

    /**
     * @return the Payload image.
     */
    protected Image getImage() {
        return image;
    }

    /**
     * @return the Payload image width in pixels.
     */
    protected double getImageWidth() {
        return image.getWidth();
    }

    /**
     * @return the Payload image height in pixels.
     */
    protected double getImageHeight() {
        return image.getHeight();
    }

    /**
     * Indicates if the associated image file has a landscape aspect ratio.
     * @return true if the image file is landscape.
     */
    public boolean isLandscape() {
        Debug.trace(DD, "isLandscape() :: " + item);
        if (image != null)
            return image.getHeight() < image.getWidth();

        return false;
    }

    public void setKeepAspectRatio(boolean keepAspectRatio) {
    }

    /**
     * Hide/display all locations of icons for this item.
     * @param state if true, display the icons, hide them otherwise.
     */
    public void setVisible(boolean state) { }

    /**
     * Indicates whether the Payload image should be drawn.
     * @return true if the image should be drawn, false otherwise.
     */
    public boolean isVisible() {
        return display;
    }



    /************************************************************************
     * Support code for Playing Card Generation.
     */

    /**
     * Class to build the data needed to render the icons.
     */
    protected class Data {
        public final double cardWidthPX;
        public final double cardHeightPX;
        private final double iconWidthPX;
        private final double iconHeightPX;
        public final double aspectRatio;
        public final double widthPX;
        public final double heightPX;

        private final double pixelsX;
        private final double pixelsY;
        public final double winX;
        public final double winY;

        public final double svgX;
        public final double svgY;
        public final double originX;
        public final double originY;

        /**
         * Calculate the data needed to render the icons based on the current 
         * card size, and Sprite size and position.
         * @param width of image in pixels.
         * @param height of image in pixels.
         */
        public Data(double width, double height) {
            cardWidthPX = model.getWidth();
            cardHeightPX = model.getHeight();

            iconWidthPX = width;
            iconHeightPX = height;
            heightPX = spriteHeight.getPixels();
            aspectRatio = iconWidthPX / iconHeightPX;
            widthPX = heightPX * aspectRatio;
    
            pixelsX = centreX.getPixels();
            pixelsY = centreY.getPixels();
            winX = cardWidthPX - (2*pixelsX);
            winY = cardHeightPX - (2*pixelsY);
    
            svgX = pixelsX + model.getMpcBorderWidth();
            svgY = pixelsY + model.getMpcBorderHeight();
            originX = svgX - (widthPX/2);
            originY = svgY - (heightPX/2);
        }

        public void dump() {
            Debug.trace(DD, "Data :: card size   = " + cardWidthPX + ", " + cardHeightPX);
            Debug.trace(DD, "Data :: input size  = " + iconWidthPX + ", " + iconHeightPX);
            Debug.trace(DD, "Data :: aspectRatio = " + aspectRatio);
            Debug.trace(DD, "Data :: sprite size = " + widthPX + ", " + heightPX);
            Debug.trace(DD, "Data :: centre pos  = " + pixelsX + ", " + pixelsY);
            Debug.trace(DD, "Data :: window size = " + winX + ", " + winY);
            Debug.trace(DD, "Data :: svg pos     = " + svgX + ", " + svgY);
            Debug.trace(DD, "Data :: origin pos  = " + originX + ", " + originY);
        }
     }


    /**
     * Draw top left and rotated bottom right images to a given graphics 
     * context using hard coded specification.
     * 
     * @param gc graphics context to draw on.
     * @param iconImage used for the icons.
     * @param rotatedImage rotated version of the image used for the icons.
     * @param pattern ignored.
     * @return true if the icons are drawn, false otherwise.
     */
    public boolean drawCard(GraphicsContext gc, Image iconImage, Image rotatedImage, int pattern) {
        return true;
    }

    public boolean drawCard(GraphicsContext gc, int pattern, String symbol) {
        return true;
    }

    /**
     * Draw top left and rotated bottom right images to a given graphics 
     * context for the Joker indices.
     * 
     * @param gc graphics context to draw on.
     * @param iconImage used for the icons.
     * @param rotatedImage rotated version of the image used for the icons.
     * @return true if the icons are drawn, false otherwise.
     */
    public boolean drawJoker(GraphicsContext gc, Image iconImage, Image rotatedImage) {
        return true;
    }

}
