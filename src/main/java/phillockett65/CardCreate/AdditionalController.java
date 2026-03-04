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
 * AdditionalController is a class that is responsible for the controls of the 
 * additional settings window.
 */
package phillockett65.CardCreate;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import phillockett65.Debug.Debug;


public class AdditionalController {

    // Debug delta used to adjust the local logging level.
    private static final int DD = 0;

    private Model model;


    /************************************************************************
     * Support code for "Settings" panel. 
     */

    @FXML
    private BorderPane settingsGUI;

    /**
     * Called by the FXML mechanism to initialize the controller. Sets up the 
     * images on the buttons and initialises all the controls.
     */
    @FXML public void initialize() {
        Debug.trace(DD, "AdditionalController initialize()");
        model = Model.getInstance();
    }

    /**
     * Called by Controller after the stage has been set. Is provided with
     * reference to model, then completes the initialization.
     */
    public void init() {
        Debug.trace(DD, "AdditionalController init()");
        initializeCardCorners();
        initializeDisplayWatermark();
        initializeModifySelectedCardItem();
        initializeCardItemPriority();
        initializeJokers();
        initializeStandardColours();
        syncUI();
    }

    /**
     * Synchronise all controls with the model.
     */
    public void syncUI() {
        Debug.trace(DD, "AdditionalController syncUI()");
        independentRadiiCheckBox.setSelected(model.isSetCornerRadiiIndependently());
        cropCornersCheckBox.setSelected(model.isCropCorners());

        courtWatermarkCheckBox.setSelected(model.isDisplayCourtWatermark());
        imageWatermarkCheckBox.setSelected(model.isDisplayImageWatermark());
        numberWatermarkCheckBox.setSelected(model.isDisplayNumberWatermark());

        lockXCheckBox.setSelected(model.isLockX());
        lockYCheckBox.setSelected(model.isLockY());
        leftHandedCheckBox.setSelected(model.isLeftHanded());
        showGuideBoxCheckBox.setSelected(model.isShowGuideBox());

        borderlessCheckBox.setSelected(model.isBorderlessJokers());

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
     * Support code for "Card Corners and Cropping" panel. 
     */

    @FXML
    private CheckBox independentRadiiCheckBox;

    @FXML
    private CheckBox cropCornersCheckBox;

    @FXML
    private Label arcWidthLabel;

    @FXML
    private Label arcHeightLabel;

    @FXML
    private Spinner<Double> arcWidthSpinner;

    @FXML
    private Spinner<Double> arcHeightSpinner;

    @FXML
    private Button arcWidthButton;

    @FXML
    private Button arcHeightButton;

    @FXML
    void independentRadiiCheckBoxActionPerformed(ActionEvent event) {
        final boolean indy = independentRadiiCheckBox.isSelected();

        model.setCornerRadiiIndependently(indy);

        setArcLabelsAndToolsTips(indy);

        model.getSample().syncCardSize();
    }

    @FXML
    void arcWidthButtonActionPerformed(ActionEvent event) {
        model.resetArcWidthSVF();
        model.getSample().syncCardSize();
    }

    @FXML
    void arcHeightButtonActionPerformed(ActionEvent event) {
        model.resetArcHeightSVF();
        model.getSample().syncCardSize();
    }

    @FXML
    void cropCornersCheckBoxActionPerformed(ActionEvent event) {
        final boolean crop = cropCornersCheckBox.isSelected();

        model.setCropCorners(crop);
    }

    private void setArcLabelsAndToolsTips(boolean indy) {

        if (indy) {
            arcHeightLabel.setText("Corner Radius Arc Height (%)");
            arcHeightLabel.setTooltip(new Tooltip("Card arc height radius as a percentage of card height (default: 10)"));
            arcHeightSpinner.setTooltip(new Tooltip("Set card arc height corner radius as a percentage of card height"));
            arcHeightButton.setTooltip(new Tooltip("Reset arc corner radius height to default value of 10 percent"));
        } else {
            arcHeightLabel.setText("Corner Radius (%)");
            arcHeightLabel.setTooltip(new Tooltip("Corner radius as a percentage of card height (default: 10)"));
            arcHeightSpinner.setTooltip(new Tooltip("Set corner radius as a percentage of card height"));
            arcHeightButton.setTooltip(new Tooltip("Reset corner radius to default value of 10 percent"));
        }

        arcWidthLabel.setDisable(!indy);
        arcWidthSpinner.setDisable(!indy);
        arcWidthButton.setDisable(!indy);

    }
    /**
     * Initialize "Card Corners" panel.
     */
    private void initializeCardCorners() {
        final boolean indy = model.isSetCornerRadiiIndependently();
        independentRadiiCheckBox.setSelected(indy);
        independentRadiiCheckBox.setTooltip(new Tooltip("Independently set card corner radius width and height"));

        arcWidthLabel.setTooltip(new Tooltip("Card arc width radius as a percentage of card width (default: 10)"));
        arcWidthSpinner.setTooltip(new Tooltip("Set card arc width corner radius as a percentage of card width"));
        arcWidthButton.setTooltip(new Tooltip("Reset arc width corner radius to default value of 10 percent"));

        arcWidthSpinner.setValueFactory(model.getArcWidthSVF());
        arcHeightSpinner.setValueFactory(model.getArcHeightSVF());

        setArcLabelsAndToolsTips(indy);

        arcWidthSpinner.valueProperty().addListener( (v, oldValue, newValue) -> {
            model.setArcWidth(newValue);
            model.getSample().syncCardSize();
        });

        arcHeightSpinner.valueProperty().addListener( (v, oldValue, newValue) -> {
            model.setArcHeight(newValue);
            model.getSample().syncCardSize();
        });

        cropCornersCheckBox.setSelected(model.isCropCorners());
        cropCornersCheckBox.setTooltip(new Tooltip("Crop the card corners, useful when the image overruns at the corners"));
    }



    /************************************************************************
     * Support code for "Display Watermark" panel. 
     */

    @FXML
    private CheckBox courtWatermarkCheckBox;

    @FXML
    private CheckBox imageWatermarkCheckBox;

    @FXML
    private CheckBox numberWatermarkCheckBox;

    @FXML
    void courtWatermarkCheckBoxActionPerformed(ActionEvent event) {
        model.setDisplayCourtWatermark(courtWatermarkCheckBox.isSelected());
    }

    @FXML
    void imageWatermarkCheckBoxActionPerformed(ActionEvent event) {
        model.setDisplayImageWatermark(imageWatermarkCheckBox.isSelected());
    }

    @FXML
    void numberWatermarkCheckBoxActionPerformed(ActionEvent event) {
        model.setDisplayNumberWatermark(numberWatermarkCheckBox.isSelected());
    }

    /**
     * Initialize "Display Watermark" panel.
     */
    private void initializeDisplayWatermark() {
        courtWatermarkCheckBox.setSelected(model.isDisplayCourtWatermark());
        imageWatermarkCheckBox.setSelected(model.isDisplayImageWatermark());
        numberWatermarkCheckBox.setSelected(model.isDisplayNumberWatermark());

        courtWatermarkCheckBox.setTooltip(new Tooltip("Display Watermark on Court cards"));
        imageWatermarkCheckBox.setTooltip(new Tooltip("Display Watermark on cards with images"));
        numberWatermarkCheckBox.setTooltip(new Tooltip("Display Watermark on Aces to 10 cards"));
    }



    /************************************************************************
     * Support code for "Modify Selected Card Item" panel. 
     */

    @FXML
    private CheckBox lockXCheckBox;

    @FXML
    private CheckBox lockYCheckBox;

    @FXML
    private CheckBox leftHandedCheckBox;

    @FXML
    private CheckBox showGuideBoxCheckBox;

    @FXML
    void lockXCheckBoxActionPerformed(ActionEvent event) {
        model.setLockX(lockXCheckBox.isSelected());
    }

    @FXML
    void lockYCheckBoxActionPerformed(ActionEvent event) {
        model.setLockY(lockYCheckBox.isSelected());
    }

    @FXML
    void leftHandedCheckBoxActionPerformed(ActionEvent event) {
        model.setLeftHanded(leftHandedCheckBox.isSelected());
    }

    @FXML
    void showGuideBoxCheckBoxActionPerformed(ActionEvent event) {
        model.setShowGuideBox(showGuideBoxCheckBox.isSelected());
    }

    /**
     * Initialize "Modify Selected Card Item" panel.
     */
    private void initializeModifySelectedCardItem() {
        lockXCheckBox.setSelected(model.isLockX());
        lockYCheckBox.setSelected(model.isLockY());
        leftHandedCheckBox.setSelected(model.isLeftHanded());
        showGuideBoxCheckBox.setSelected(model.isShowGuideBox());

        lockXCheckBox.setTooltip(new Tooltip("Lock X coordinate of Index and Corner pip together"));
        lockYCheckBox.setTooltip(new Tooltip("Lock Y separation of Index and Corner pip"));
        leftHandedCheckBox.setTooltip(new Tooltip("Show Indices and Corner pips in all four corners"));
        showGuideBoxCheckBox.setTooltip(new Tooltip("Display guide box to aid Card Item positioning"));
    }



    /************************************************************************
     * Support code for "Card Item Priority" panel. 
     */

    @FXML
    private ListView<String> cardItemListView;
    
    @FXML
    private Button upItemButton;

    @FXML
    private Button downItemButton;

    @FXML
    private Button priorityResetButton;

    @FXML
    public void upItemButtonActionPerformed(ActionEvent event) {
        int index = model.moveSelectedCardItemUp();
        cardItemListView.getSelectionModel().select(index);
    }

    @FXML
    public void downItemButtonActionPerformed(ActionEvent event) {
        int index = model.moveSelectedCardItemDown();
        cardItemListView.getSelectionModel().select(index);
    }

    @FXML
    public void priorityResetButtonActionPerformed(ActionEvent event) {
        model.resetPriorityList();
        cardItemListView.getSelectionModel().select(-1);
    }

    /**
     * Initialize "Card Item Priority" panel.
     */
    private void initializeCardItemPriority() {
        cardItemListView.setItems(model.getCardItemList());
        cardItemListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                model.setSelectedCardItem(newValue);

                upItemButton.setDisable(!model.isUpAvailable());
                downItemButton.setDisable(!model.isDownAvailable());
            }
        } );

        cardItemListView.setTooltip(new Tooltip("Use Up and Down buttons to change order of Card Items"));
        upItemButton.setTooltip(new Tooltip("Increase priority of selected Card Items"));
        downItemButton.setTooltip(new Tooltip("Decrease priority of selected Card Items"));
        priorityResetButton.setTooltip(new Tooltip("Reset selected Card Items priorities"));

        upItemButton.setDisable(true);
        downItemButton.setDisable(true);
    }



    /************************************************************************
     * Support code for "Jokers" panel. 
     */

    @FXML
    private CheckBox borderlessCheckBox;

    @FXML
    void borderlessCheckBoxActionPerformed(ActionEvent event) {
        model.setBorderlessJokers(borderlessCheckBox.isSelected());
    }

    /**
     * Initialize "Jokers" panel.
     */
    private void initializeJokers() {
        borderlessCheckBox.setSelected(model.isBorderlessJokers());

        borderlessCheckBox.setTooltip(new Tooltip("Don't use borders on Joker images"));
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

    // @FXML
    // private Label clubPipLabel;

    // @FXML
    // private Label diamondPipLabel;

    // @FXML
    // private Label heartPipLabel;

    // @FXML
    // private Label spadePipLabel;

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
