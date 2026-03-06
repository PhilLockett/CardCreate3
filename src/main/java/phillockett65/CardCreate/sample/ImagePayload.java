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
 * ImagePayload extends Payload specifically for image cards. 
 */
package phillockett65.CardCreate.sample;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Affine;
import phillockett65.Debug.Debug;


public class ImagePayload extends Payload {

    // Debug delta used to adjust the local logging level.
    private static final int DD = 0;


    /************************************************************************
     * Support code for ImageView array management.
     */

    private ImageView[] views;
    private SVGPath[] paths;

    /**
     * @return the active ImageView count.
     */
    private int getPathCount() {
        return ColourKey.MAX_KEY.getKey();
    }

    /**
     * Create the ImageView array to hold the image for this Payload.
     * 
     * @param group node to add the ImageViews to.
     */
    private void createImageViewArray() {
        Debug.trace(DD, "createImageViewArray(" + getPathCount() + ") :: " + item);

        views = new ImageView[2];
        views[0] = new ImageView();
        views[0].setPreserveRatio(true);
        views[1] = new ImageView();
        views[1].setPreserveRatio(true);
        views[1].setRotate(180);
        
        paths = new SVGPath[getPathCount()];
        for (int i = 0; i < getPathCount(); ++i) {
            paths[i] = new SVGPath();
        }
    }

    public void removeFromGroup() {
        final Group group = model.getGroup();

        group.getChildren().remove(views[0]);
        group.getChildren().remove(views[1]);

        for (int i = 0; i < getPathCount(); ++i) {
            group.getChildren().remove(paths[i]);
        }
    }

    public void addToGroup() {
        final Group group = model.getGroup();

        group.getChildren().add(views[0]);
        group.getChildren().add(views[1]);

        for (int i = 0; i < getPathCount(); ++i) {
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
     * Set the image in all ImageViews.
     * 
     * @param image to be set in each ImageView in views[].
     */
    protected void setImages(Image image) {
        views[0].setImage(image);
        views[1].setImage(image);
    }

    /**
     * Indicates whether the indexed ImageView should be visible.
     * 
     * @param imageIndex for the ImageView in views[] (0 or 1).
     * @return true if the image should be visible, false otherwise.
     */
    private boolean isImageViewVisible(int imageIndex) {
        if (imageIndex == 0)
            return true;

        if (isLandscape())
            return true;

        return false;
    }

    /**
     * set the visibilty for all ImageViews and SVGPaths.
     * 
     * @param imageIndex for the ImageView in views[].
     */
    protected void setVisibility() {
        if (isVisible() == false) {
            getImageView(0).setVisible(false);
            getImageView(1).setVisible(false);

            for (int i = 0; i < getPathCount(); ++i) {
                getSVGPath(i).setVisible(false);
            }
        } else if (model.shouldStandardBeDisplayed(item)) {
            getImageView(0).setVisible(false);
            getImageView(1).setVisible(false);

            final String symbol = model.getStandardSymbol(item);
            final int count = SVGFaces.getFacePathCount(symbol);
            for (int i = 0; i < count; ++i) {
                getSVGPath(i).setVisible(true);
            }
            for (int i = count; i < getPathCount(); ++i) {
                getSVGPath(i).setVisible(false);
            }
        } else {
            getImageView(0).setVisible(isImageViewVisible(0));
            getImageView(1).setVisible(isImageViewVisible(1));

            for (int i = 0; i < getPathCount(); ++i) {
                getSVGPath(i).setVisible(false);
            }
        }

    }


    /************************************************************************
     * Support code for the ImagePayload class.
     */

    protected boolean keepAspectRatio = true;

    public ImagePayload() {
        super(Item.FACE);

        // Set up the image views.
        createImageViews();

        // Set up image dependent values.
        initImageViews();
    }

    /**
     * Initialize the Image Views based on item.
     */
    protected void initImageViews() {
        setPath(Item.FACE);
        Debug.trace(DD, "initImageViews(" + path + ") :: " + item);

        if (path.equals(""))
            return;

        if (loadNewImageFile()) {
            setImages(getImage());
        }
    }

    /**
     * Draw the SVG paths associated with this payload.
     */
    private void drawSVGPaths() {
        Debug.trace(DD, "drawSVGPaths() :: " + item);

        final String symbol = model.getStandardSymbol(item);
        Face face = SVGFaces.getFace(symbol);
        if (face == null) {
            return;
        }

        final double cardWidthPX = model.getWidth();
        final double cardHeightPX = model.getHeight();
        final double xOffset = model.getMpcBorderWidth();
        final double yOffset = model.getMpcBorderHeight();

        final double pixelsX = centreX.getPixels();
        final double pixelsY = centreY.getPixels();

        Desc[] facePathDescs = face.getDescs();
        if (facePathDescs == null) {
            Debug.major(DD, "drawSVGPaths() facePathDescs[" + symbol + "] == null " + item);
            return;
        }

        for (int i = 0; i < facePathDescs.length; ++i) {
            Desc pathDesc = facePathDescs[i];

            final double pathWidth = pathDesc.getWidth();
            final double pathHeight = pathDesc.getHeight();
            final Color colour = model.getStandardColour(pathDesc.getKey());

            double winX = cardWidthPX - (2*pixelsX);
            double winY;
            
            double dX = 0;
            double dY = 0;

            winY = cardHeightPX - (2*pixelsY);
            
            double scaleX = winX / pathWidth;
            double scaleY = winY / pathHeight;
            if (scaleX < scaleY) {
                dY = (winY - (pathHeight * scaleX)) / 2;
                winY = pathHeight * scaleX;
            } else {
                dX = (winX - (pathWidth * scaleY)) / 2;
                winX = pathWidth * scaleY;
            }

            SVGPath svgPath = getSVGPath(i);
            svgPath.setContent(pathDesc.getPath());
            Affine affine = new Affine(scaleX, 0, pixelsX + dX + xOffset, 0, scaleY, pixelsY + dY + yOffset);
            affine.append(pathDesc.getAffine());
            svgPath.getTransforms().clear();
            svgPath.getTransforms().add(affine);
            svgPath.setFill(colour);
        }
    }

    /**
     * Paint image(s) associated with this payload.
     */
    private void paintImage() {
        Debug.trace(DD, "paintImage() :: " + item);

        if (!hasImage())
            return;

        final double cardWidthPX = model.getWidth();
        final double cardHeightPX = model.getHeight();

        final double imageWidthPX = getImage().getWidth();
        final double imageHeightPX = getImage().getHeight();

        final double pX = centreX.getPixels();
        final double pY = centreY.getPixels();
        final double winX = cardWidthPX - (2*pX);

        double dX = 0;
        double dY = 0;

        if (isLandscape()) {
            final double winY = (cardHeightPX / 2) - pY;

            if (keepAspectRatio) {
                double scaleX = winX / imageWidthPX;
                double scaleY = winY / imageHeightPX;
                if (scaleX < scaleY) {
                    dY = (winY - (imageHeightPX * scaleX));
                } else {
                    dX = (winX - (imageWidthPX * scaleY)) / 2;
                }
            }

            ImageView view = getImageView(0);
            view.relocate(pX + dX, pY + dY);
            view.setFitWidth(winX);
            view.setFitHeight(winY);

            view = getImageView(1);
            view.relocate(pX + dX, cardHeightPX/2);
            view.setFitWidth(winX);
            view.setFitHeight(winY);
        } else {
            final double winY = cardHeightPX - (2*pY);

            if (keepAspectRatio) {
                double scaleX = winX / imageWidthPX;
                double scaleY = winY / imageHeightPX;
                if (scaleX < scaleY) {
                    dY = (winY - (imageHeightPX * scaleX)) / 2;
                } else {
                    dX = (winX - (imageWidthPX * scaleY)) / 2;
                }
            } 

            ImageView view = getImageView(0);
            view.relocate(pX + dX, pY + dY);
            view.setFitWidth(winX);
            view.setFitHeight(winY);

            view = getImageView(1);
            view.setVisible(false);
        }
    }

    /**
     * Paint the icons associated with this payload if visible.
     */
    public void setPatterns() {
        Debug.trace(DD, "setPatterns() :: " + item);

        if (model.shouldStandardBeDisplayed(item)) {
            drawSVGPaths();
        } else {
            paintImage();
        }

        setVisibility();
    }


    /**
     * Flag whether the Payload image should maintain it's aspect ratio.
     * @param keepAspectRatio when displaying the image if true.
     */
    public void setKeepAspectRatio(boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;

        getImageView(0).setPreserveRatio(keepAspectRatio);
        getImageView(1).setPreserveRatio(keepAspectRatio);
        paintImage();
    }



    /************************************************************************
     * Support code for Playing Card Generation.
     */

    /**
     * Draw the card image and, if a landscape image, the rotated image to a
     * given graphics context using the user specification.
     * 
     * @param gc graphics context to draw on.
     * @param iconImage used for the icons.
     * @param rotatedImage rotated version of the image used for the icons.
     * @param pattern ignored.
     * @return true if the icons are drawn, false otherwise.
     */
    public boolean drawCard(GraphicsContext gc, Image iconImage, Image rotatedImage, int pattern) {
        if (iconImage == null)
            return false;

        Debug.trace(DD, "drawCard(" + pattern + ")");
        final double cardWidthPX = model.getWidth();
        final double cardHeightPX = model.getHeight();
        final double xOffset = model.getMpcBorderWidth();
        final double yOffset = model.getMpcBorderHeight();

        final double imageWidthPX = iconImage.getWidth();
        final double imageHeightPX = iconImage.getHeight();
        final boolean landscape = imageHeightPX < imageWidthPX;

        final double pixelsX = centreX.getPixels();
        final double pixelsY = centreY.getPixels();
        double winX = cardWidthPX - (2*pixelsX);
        double winY;

        double dX = 0;
        double dY = 0;

        if (landscape) {
            Debug.info(DD, "landscape");

            winY = (cardHeightPX / 2) - pixelsY;

            if (keepAspectRatio) {
                double scaleX = winX / imageWidthPX;
                double scaleY = winY / imageHeightPX;
                if (scaleX < scaleY) {
                    dY = (winY - (imageHeightPX * scaleX));
                    winY = imageHeightPX * scaleX;
                } else {
                    dX = (winX - (imageWidthPX * scaleY)) / 2;
                    winX = imageWidthPX * scaleY;
                }
            }

            gc.drawImage(rotatedImage, pixelsX + dX + xOffset, cardHeightPX/2 + yOffset, winX, winY);
            gc.drawImage(iconImage, pixelsX + dX + xOffset, pixelsY + dY + yOffset, winX, winY);
        } else {
            Debug.info(DD, "portrait");

            winY = cardHeightPX - (2*pixelsY);

            if (keepAspectRatio) {
                double scaleX = winX / imageWidthPX;
                double scaleY = winY / imageHeightPX;
                if (scaleX < scaleY) {
                    dY = (winY - (imageHeightPX * scaleX)) / 2;
                    winY = imageHeightPX * scaleX;
                } else {
                    dX = (winX - (imageWidthPX * scaleY)) / 2;
                    winX = imageWidthPX * scaleY;
                }
            }

            gc.drawImage(iconImage, pixelsX + dX + xOffset, pixelsY + dY + yOffset, winX, winY);
        }

        return true;
    }


    /**
     * Draw standard court image to a given graphics context using the user 
     * specification.
     * 
     * @param gc graphics context to draw on.
     * @param pattern ignored.
     * @param symbol indicating the face to be drawn (e.g. "CK").
     * @return true if the icons are drawn, false otherwise.
     */
    public boolean drawCard(GraphicsContext gc, int pattern, String symbol) {
        Debug.trace(DD, "drawCard(" + symbol + ")");

        Face face = SVGFaces.getFace(symbol);
        if (face == null) {
            return false;
        }

        final double cardWidthPX = model.getWidth();
        final double cardHeightPX = model.getHeight();
        final double xOffset = model.getMpcBorderWidth();
        final double yOffset = model.getMpcBorderHeight();
    
        final double pixelsX = centreX.getPixels();
        final double pixelsY = centreY.getPixels();

        Desc[] facePathDescs = face.getDescs();
        for (int i = 0; i < facePathDescs.length; ++i) {
            Desc pathDesc = facePathDescs[i];

            final double pathWidth = pathDesc.getWidth();
            final double pathHeight = pathDesc.getHeight();
            final Color colour = model.getStandardColour(pathDesc.getKey());

            double winX = cardWidthPX - (2*pixelsX);
            double winY;

            double dX = 0;
            double dY = 0;

            winY = cardHeightPX - (2*pixelsY);

            double scaleX = winX / pathWidth;
            double scaleY = winY / pathHeight;
            if (scaleX < scaleY) {
                dY = (winY - (pathHeight * scaleX)) / 2;
                winY = pathHeight * scaleX;
            } else {
                dX = (winX - (pathWidth * scaleY)) / 2;
                winX = pathWidth * scaleY;
            }

            gc.save();

            gc.setFill(colour);
            gc.translate(pixelsX + dX + xOffset, pixelsY + dY + yOffset);
            gc.scale(scaleX, scaleY);
            gc.transform(pathDesc.getAffine());
            gc.beginPath();
            gc.appendSVGPath(pathDesc.getPath());
            gc.closePath();
            gc.fill();

            gc.restore();
        }

        return true;
    }

    /**
     * Draw single portrait image to a given graphics context using hard coded 
     * specification for the Joker image.
     * 
     * @param gc graphics context to draw on.
     * @param image used for the icons.
     * @param rotatedImage ignored.
     * @return true if the icons are drawn, false otherwise.
     */
    public boolean drawJoker(GraphicsContext gc, Image image, Image rotatedImage) {
        if (image == null)
            return false;

        final double cardWidthPX = model.getWidth();
        final double cardHeightPX = model.getHeight();
        final double xOffset = model.getMpcBorderWidth();
        final double yOffset = model.getMpcBorderHeight();

        final double imageWidthPX = image.getWidth();
        final double imageHeightPX = image.getHeight();

        final boolean borderless = model.isBorderlessJokers();
        final double pixelsX = borderless ? 0 : cardWidthPX * 0.07D;
        final double pixelsY = borderless ? 0 : cardHeightPX * 0.05D;
        double winX = cardWidthPX;
        double winY = cardHeightPX;

        if (!borderless) {
            winX -= 2 * pixelsX;
            winY -= 2 * pixelsY;
        }

        double dX = 0;
        double dY = 0;

        double scaleX = winX / imageWidthPX;
        double scaleY = winY / imageHeightPX;
        if (scaleX < scaleY) {
            dY = (winY - (imageHeightPX * scaleX)) / 2;
            winY = imageHeightPX * scaleX;
        } else {
            dX = (winX - (imageWidthPX * scaleY)) / 2;
            winX = imageWidthPX * scaleY;
        }

        gc.drawImage(image, pixelsX + dX + xOffset, pixelsY + dY + yOffset, winX, winY);

        return true;
    }

}
