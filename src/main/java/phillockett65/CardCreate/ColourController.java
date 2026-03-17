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
 * ColourController is a class that is responsible for the controls of the 
 * colour settings window.
 */
package phillockett65.CardCreate;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import phillockett65.CardCreate.sample.ColourKey;
import phillockett65.ColourSelect.ColourEvent;
import phillockett65.ColourSelect.ColourExtend;
import phillockett65.ColourSelect.ColourSelect;
import phillockett65.Debug.Debug;


public class ColourController {

    // Debug delta used to adjust the local logging level.
    private static final int DD = 0;

    private Model model;
    private GridPane colourPaletteGrid;
    private ColourSelect colourSelect;
    private ColourExtend colourExtend;

    @FXML
    private VBox colourPaletteVBox;

    @FXML
    private VBox colourSelectVBox;

    @FXML
    private VBox colourSetUpVBox;



    /************************************************************************
     * Support code for "Colours" tab initialization and synchronization. 
     */

    /**
     * Called by the FXML mechanism to initialize the controller. Sets up the 
     * images on the buttons and initialises all the controls.
     */
    @FXML public void initialize() {
        Debug.trace(DD, "ColourController initialize()");
        model = Model.getInstance();

        colourPaletteGrid = buildPaletteGrid();
        colourSelect = new ColourSelect(true);
        colourExtend = new ColourExtend();

        colourPaletteVBox.getChildren().add(colourPaletteGrid);
        colourSelectVBox.getChildren().add(colourSelect);
        colourSetUpVBox.getChildren().add(colourExtend);
        
        colourSelect.addEventFilter(ColourEvent.ANY, this::handleColourSelectEvent);
        colourExtend.addEventFilter(ColourEvent.ANY, this::handleColourExtendEvent);

        initializeStandardColours();
    }

    /**
     * Called by Controller after the stage has been set. Is provided with
     * reference to model, then completes the initialization.
     */
    public void init() {
        Debug.trace(DD, "ColourController init()");
        syncUI();
        syncSelectedColour();
        swatches[model.getSelectedColourIndex()].setSelected(true);
    }

    /**
     * Synchronise all controls with the model.
     */
    public void syncUI() {
        Debug.trace(DD, "ColourController syncUI()");

        syncColours();
        syncDisabled();
    }

    private void syncColours() {
        ArrayList<String> colours = model.buildColourList();
        for (int i = 0; i < colours.size(); i++) {
            Swatch swatch = swatches[i];
            swatch.setColour(colours.get(i));
        }
    }

    private void syncDisabled() {
        boolean indexState = !model.isStandardIndices();
        boolean pipState = !model.isStandardPips();
        boolean courtState = !model.isStandardFaces();
        for (Swatch swatch : swatches) {
            swatch.setIndexDisabled(indexState);
            swatch.setPipDisabled(pipState);
            swatch.setCourtDisabled(courtState);
        }
    }



    /************************************************************************
     * Support code for Colour event handlers. 
     */

    private Color handleColourEvent(ColourEvent event) {
        final int index = model.getSelectedColourIndex();
        final Color colour = event.getColour();
        model.setSwatchColour(index, colour);
        swatches[index].setColour(colour);

        return colour;
    }

    private void handleColourSelectEvent(ColourEvent event) {
        final Color colour = handleColourEvent(event);
        colourExtend.handleColourEvent(colour);
    }

    private void handleColourExtendEvent(ColourEvent event) {
        final Color colour = handleColourEvent(event);
        colourSelect.setColour(colour);
    }


    private void syncSelectedColour() {
        final Color selectedColour = model.getSelectedColour();
        colourSelect.setColour(selectedColour);
        colourExtend.setColour(selectedColour);
    }

    private void selectedColourActionPerformed(int index) {
        Debug.trace(DD, "selectedColourActionPerformed(" + index + ")");

        final int previous = model.setSelectedColourIndex(index);
        swatches[previous].setSelected(false);
        swatches[index].setSelected(true);

        syncSelectedColour();
    }



    /************************************************************************
     * Support code for Colour Swatches and their placement in the GridPane. 
     */

    /**
     * Class that represents a colour swatch.
     */
    private class Swatch {
        private final Rectangle rectangle;
        private final Label label;
        private final ColourKey key;

        public Swatch(ColourKey k, String p, String t) {
            key = k;

            rectangle = new Rectangle(50, 28);
            rectangle.setStroke(Color.GREY);
            rectangle.setStrokeWidth(4);
            
            rectangle.setOnMousePressed(mouseEvent -> {
                selectedColourActionPerformed(key.getKey());
            });

            label = new Label(p);
            label.setTooltip(new Tooltip(t));

            setDisabled(false);
            setSelected(false);
        }

        public void setColour(String colour) {
            rectangle.setFill(Color.web(colour));
        }

        public void setColour(Color colour) {
            rectangle.setFill(colour);
        }

        public void setDisabled(boolean state) {
            rectangle.setDisable(state);
            label.setDisable(state);
        }

        public void setIndexDisabled(boolean state) {
            if (key.isIndex()) {
                setDisabled(state);
            }
        }

        public void setPipDisabled(boolean state) {
            if (key.isPip()) {
                setDisabled(state);
            }
        }

        public void setCourtDisabled(boolean state) {
            if (key.isFace()) {
                setDisabled(state);
            }
        }

        public void setSelected(boolean state) {
            rectangle.setStroke(state ? Color.WHITE : Color.GRAY);
        }

        public void addToGrid(GridPane grid, int row) {
            final int col = key.isFace() ? 2 : 0;
            grid.add(rectangle, col, row);
            grid.add(label, col+1, row);
            if (row == 0) {
                GridPane.setColumnSpan(label, 2);
            }
        }
    }

    Swatch[] swatches = new Swatch[ColourKey.MAX_KEY.getKey()];

    private void setSwatch(ColourKey key, int row, String label, String tooltip) {
        Swatch swatch = new Swatch(key, label, tooltip);
        swatch.addToGrid(colourPaletteGrid, row);

        swatches[key.getKey()] = swatch;
    }

    /**
     * Create a new swatch, add it to the swatches, and add the component Label 
     * and Rectangle to colourPaletteGrid to be displayed.
     * Note: the display order differs from the ColourKey order and hence the 
     * order they appear in colourPaletteGrid.
     */
    private void setSwatches() {
        setSwatch(ColourKey.CARD_ID, 0, "Background Colour", "Select the background colour for the card");

        int row = 2;
        setSwatch(ColourKey.SPADE_INDEX_ID, row++, "Spade Indices", "Select Standard Spade Index colour");
        setSwatch(ColourKey.SPADE_PIP_ID, row++, "Spade Pips", "Select Standard Spade Pip colour");
        setSwatch(ColourKey.CLUB_INDEX_ID, row++, "Club Indices", "Select Standard Club Index colour");
        setSwatch(ColourKey.CLUB_PIP_ID, row++, "Club Pips", "Select Standard Club Pip colour");

        setSwatch(ColourKey.DIAMOND_INDEX_ID, row++, "Diamond Indices", "Select Standard Diamond Index colour");
        setSwatch(ColourKey.DIAMOND_PIP_ID, row++, "Diamond Pips", "Select Standard Diamond Pip colour");
        setSwatch(ColourKey.HEART_INDEX_ID, row++, "Heart Indices", "Select Standard Heart Index colour");
        setSwatch(ColourKey.HEART_PIP_ID, row++, "Heart Pips", "Select Standard Heart Pip colour");

        row = 2;
        setSwatch(ColourKey.COURT_WHITE_ID, row++, "Courts White", "Select Standard Court White colour");
        setSwatch(ColourKey.COURT_STEEL_ID, row++, "Courts Steel", "Select Standard Court Steel colour");
        setSwatch(ColourKey.COURT_HAIR_ID, row++, "Courts Hair", "Select Standard Court Hair colour");
        setSwatch(ColourKey.COURT_FLESH_ID, row++, "Courts Flesh", "Select Standard Court Flesh colour");

        setSwatch(ColourKey.COURT_YELLOW_ID, row++, "Courts Yellow", "Select Standard Court Yellow colour");
        setSwatch(ColourKey.COURT_RED_ID, row++, "Courts Red", "Select Standard Court Red colour");
        setSwatch(ColourKey.COURT_BLUE_ID, row++, "Courts Blue", "Select Standard Court Blue colour");
        setSwatch(ColourKey.COURT_BLACK_ID, row++, "Courts Black", "Select Standard Court Black colour");
    }



    /************************************************************************
     * Support code for Colour Colour palette GridPane. 
     */

    private void addColumnConstraint(GridPane grid, double width) {
        ColumnConstraints col = new ColumnConstraints();
        col.setHalignment(HPos.LEFT);
        col.setHgrow(Priority.SOMETIMES);
        col.setMinWidth(width);
        col.setMaxWidth(width);
        col.setPrefWidth(width);

        grid.getColumnConstraints().add(col);
    }

    private void addRowConstraint(GridPane grid, double height) {
        RowConstraints row = new RowConstraints();
        row.setVgrow(Priority.SOMETIMES);
        row.setMinHeight(height);
        row.setPrefHeight(height);

        grid.getRowConstraints().add(row);
    }

    /**
     * Build the Grid layout, but don't fill it.
     */
    private GridPane buildPaletteGrid() {
        GridPane grid = new GridPane();

        final double swatchWidth = 56.0;
        // final double labelWidth = 120.0;
        addColumnConstraint(grid, swatchWidth);
        addColumnConstraint(grid, 120.0);
        addColumnConstraint(grid, swatchWidth);
        addColumnConstraint(grid, 100.0);

        final double rowHeight = 30.0;
        for (int i = 0; i < 10; ++i) {
            addRowConstraint(grid, rowHeight);
        }

        return grid;
    }


    /**
     * Initialize "Select Standard Index/Pip Colour" panel.
     */
    private void initializeStandardColours() {
        setSwatches();
    }


}
