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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import phillockett65.Debug.Debug;


public class ColourController {

    // Debug delta used to adjust the local logging level.
    private static final int DD = 0;

    private Model model;


    /************************************************************************
     * Support code for "Settings" panel. 
     */

    /**
     * Called by the FXML mechanism to initialize the controller. Sets up the 
     * images on the buttons and initialises all the controls.
     */
    @FXML public void initialize() {
        Debug.trace(DD, "ColourController initialize()");
        model = Model.getInstance();
    }

    /**
     * Called by Controller after the stage has been set. Is provided with
     * reference to model, then completes the initialization.
     */
    public void init() {
        Debug.trace(DD, "ColourController init()");
        initializeStandardColours();
        syncUI();
    }

    /**
     * Synchronise all controls with the model.
     */
    public void syncUI() {
        Debug.trace(DD, "ColourController syncUI()");

        clubIndexColourPicker.setValue(model.getClubIndexColour());
        diamondIndexColourPicker.setValue(model.getDiamondIndexColour());
        heartIndexColourPicker.setValue(model.getHeartIndexColour());
        spadeIndexColourPicker.setValue(model.getSpadeIndexColour());

        clubPipColourPicker.setValue(model.getClubPipColour());
        diamondPipColourPicker.setValue(model.getDiamondPipColour());
        heartPipColourPicker.setValue(model.getHeartPipColour());
        spadePipColourPicker.setValue(model.getSpadePipColour());

        whiteColourPicker.setValue(model.getCourtsWhiteColour());
        steelColourPicker.setValue(model.getCourtsSteelColour());
        hairColourPicker.setValue(model.getCourtsHairColour());
        fleshColourPicker.setValue(model.getCourtsFleshColour());

        yellowColourPicker.setValue(model.getCourtsYellowColour());
        redColourPicker.setValue(model.getCourtsRedColour());
        blueColourPicker.setValue(model.getCourtsBlueColour());
        blackColourPicker.setValue(model.getCourtsBlackColour());

        clubIndexColourPicker.setDisable(!model.isStandardIndices());
        diamondIndexColourPicker.setDisable(!model.isStandardIndices());
        heartIndexColourPicker.setDisable(!model.isStandardIndices());
        spadeIndexColourPicker.setDisable(!model.isStandardIndices());

        clubPipColourPicker.setDisable(!model.isStandardPips());
        diamondPipColourPicker.setDisable(!model.isStandardPips());
        heartPipColourPicker.setDisable(!model.isStandardPips());
        spadePipColourPicker.setDisable(!model.isStandardPips());

        whiteColourPicker.setDisable(!model.isStandardFaces());
        steelColourPicker.setDisable(!model.isStandardFaces());
        hairColourPicker.setDisable(!model.isStandardFaces());
        fleshColourPicker.setDisable(!model.isStandardFaces());

        yellowColourPicker.setDisable(!model.isStandardFaces());
        redColourPicker.setDisable(!model.isStandardFaces());
        blueColourPicker.setDisable(!model.isStandardFaces());
        blackColourPicker.setDisable(!model.isStandardFaces());
    }



    /************************************************************************
     * Support code for "Select Standard Index/Pip Colour" panel. 
     */

    @FXML
    private Label clubIndexLabel;

    @FXML
    private Label diamondIndexLabel;

    @FXML
    private Label heartIndexLabel;

    @FXML
    private Label spadeIndexLabel;

    @FXML
    private ColorPicker clubIndexColourPicker;

    @FXML
    private ColorPicker diamondIndexColourPicker;

    @FXML
    private ColorPicker heartIndexColourPicker;

    @FXML
    private ColorPicker spadeIndexColourPicker;

    @FXML
    private ColorPicker clubPipColourPicker;

    @FXML
    private ColorPicker diamondPipColourPicker;

    @FXML
    private ColorPicker heartPipColourPicker;

    @FXML
    private ColorPicker spadePipColourPicker;

    @FXML
    private ColorPicker whiteColourPicker;

    @FXML
    private ColorPicker steelColourPicker;

    @FXML
    private ColorPicker hairColourPicker;

    @FXML
    private ColorPicker fleshColourPicker;

    @FXML
    private ColorPicker yellowColourPicker;

    @FXML
    private ColorPicker redColourPicker;

    @FXML
    private ColorPicker blueColourPicker;

    @FXML
    private ColorPicker blackColourPicker;


    @FXML
    void clubIndexColourPickerActionPerformed(ActionEvent event) {
        model.setClubIndexColour(clubIndexColourPicker.getValue());
    }

    @FXML
    void diamondIndexColourPickerActionPerformed(ActionEvent event) {
        model.setDiamondIndexColour(diamondIndexColourPicker.getValue());
    }

    @FXML
    void heartIndexColourPickerActionPerformed(ActionEvent event) {
        model.setHeartIndexColour(heartIndexColourPicker.getValue());
    }

    @FXML
    void spadeIndexColourPickerActionPerformed(ActionEvent event) {
        model.setSpadeIndexColour(spadeIndexColourPicker.getValue());
    }

    @FXML
    void clubPipColourPickerActionPerformed(ActionEvent event) {
        model.setClubPipColour(clubPipColourPicker.getValue());
    }

    @FXML
    void diamondPipColourPickerActionPerformed(ActionEvent event) {
        model.setDiamondPipColour(diamondPipColourPicker.getValue());
    }

    @FXML
    void heartPipColourPickerActionPerformed(ActionEvent event) {
        model.setHeartPipColour(heartPipColourPicker.getValue());
    }

    @FXML
    void spadePipColourPickerActionPerformed(ActionEvent event) {
        model.setSpadePipColour(spadePipColourPicker.getValue());
    }

    @FXML
    void whiteColourPickerActionPerformed(ActionEvent event) {
        model.setCourtsWhiteColour(whiteColourPicker.getValue());
    }

    @FXML
    void steelColourPickerActionPerformed(ActionEvent event) {
        model.setCourtsSteelColour(steelColourPicker.getValue());
    }

    @FXML
    void hairColourPickerActionPerformed(ActionEvent event) {
        model.setCourtsHairColour(hairColourPicker.getValue());
    }

    @FXML
    void fleshColourPickerActionPerformed(ActionEvent event) {
        model.setCourtsFleshColour(fleshColourPicker.getValue());
    }

    @FXML
    void yellowColourPickerActionPerformed(ActionEvent event) {
        model.setCourtsYellowColour(yellowColourPicker.getValue());
    }

    @FXML
    void redColourPickerActionPerformed(ActionEvent event) {
        model.setCourtsRedColour(redColourPicker.getValue());
    }

    @FXML
    void blueColourPickerActionPerformed(ActionEvent event) {
        model.setCourtsBlueColour(blueColourPicker.getValue());
    }

    @FXML
    void blackColourPickerActionPerformed(ActionEvent event) {
        model.setCourtsBlackColour(blackColourPicker.getValue());
    }

    /**
     * Initialize "Select Standard Index/Pip Colour" panel.
     */
    private void initializeStandardColours() {
        clubIndexColourPicker.setTooltip(new Tooltip("Select Standard Club Index colour"));
        diamondIndexColourPicker.setTooltip(new Tooltip("Select Standard Diamond Index colour"));
        heartIndexColourPicker.setTooltip(new Tooltip("Select Standard Heart Index colour"));
        spadeIndexColourPicker.setTooltip(new Tooltip("Select Standard Spade Index colour"));

        clubPipColourPicker.setTooltip(new Tooltip("Select Standard Club Pip colour"));
        diamondPipColourPicker.setTooltip(new Tooltip("Select Standard Diamond Pip colour"));
        heartPipColourPicker.setTooltip(new Tooltip("Select Standard Heart Pip colour"));
        spadePipColourPicker.setTooltip(new Tooltip("Select Standard Spade Pip colour"));

        whiteColourPicker.setTooltip(new Tooltip("Select White Court colour"));
        steelColourPicker.setTooltip(new Tooltip("Select Steel Court colour"));
        hairColourPicker.setTooltip(new Tooltip("Select Hair Court colour"));
        fleshColourPicker.setTooltip(new Tooltip("Select Flesh Court colour"));

        yellowColourPicker.setTooltip(new Tooltip("Select Yellow Court colour"));
        redColourPicker.setTooltip(new Tooltip("Select Red Court colour"));
        blueColourPicker.setTooltip(new Tooltip("Select Blue Court colour"));
        blackColourPicker.setTooltip(new Tooltip("Select Black Court colour"));
    }


}
