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
 * Model is a class that captures the dynamic shared data plus some supporting 
 * constants and provides access via getters and setters.
 */
package phillockett65.CardCreate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import phillockett65.CardCreate.sample.CardSample;
import phillockett65.CardCreate.sample.Default;
import phillockett65.CardCreate.sample.DoublePayload;
import phillockett65.CardCreate.sample.Handle;
import phillockett65.CardCreate.sample.ImagePayload;
import phillockett65.CardCreate.sample.Item;
import phillockett65.CardCreate.sample.MultiPayload;
import phillockett65.CardCreate.sample.Payload;
import phillockett65.CardCreate.sample.QuadPayload;
import phillockett65.Debug.Debug;


public class Model {

    // Debug delta used to adjust the local logging level.
    private static final int DD = 0;

    private final static String DATAFILE = "x_Settings.dat";

    public static final int INDEX_ID = 0;
    public static final int CORNER_PIP_ID = 1;
    public static final int STANDARD_PIP_ID = 2;
    public static final int FACE_PIP_ID = 3;
    public static final int FACE_ID = 4;

    /************************************************************************
     * Support code for embedded svg paths.
     */

    static class SvgPathData {
        String symbol;
        double width;
        double height;
        String path;

        SvgPathData(String s, double w, double h, String p) {
            symbol = s;
            width = w;
            height = h;
            path = p;
        }
    }

    private static final SvgPathData[] svgPathData = {

        new SvgPathData("Joker", 0.0, 0.0, ""),
        new SvgPathData("A", 25.26, 37, "M 2 35 v -2.82 h 2.69 l 5.74 -30.18 h 3.26 l 6.32 30.18 h 2.17 v 2.82 h -7.41 v -2.82 h 2 l -0.8 -4.62 h -7.47 l -0.58 4.62 h 2 v 2.82 z m 13.34 -10.74 l -3.25 -16.15 l -3.04 16.15 z"),
        new SvgPathData("2", 22.78, 35.74, "M 2 33.67 l 0.16 -2.03 c 0.51 -6.29 1.86 -8.41 9.52 -14.97 c 3.72 -3.18 4.94 -4.75 5.11 -6.57 c 0.49 -4.3 -3.31 -5.64 -5.79 -5.66 c -3.21 -0.03 -6.84 1.92 -5.11 6.46 l -3.6 1.08 c -0.72 -4.23 0.02 -8.21 3.39 -9.97 c 4.5 -2.3 10.27 -1.4 13.18 2.22 c 1.96 2.43 2.41 5.79 1.36 8.42 c -3.07 6.72 -13.48 9.68 -14.34 17.45 h 11.51 v -4.33 h 3.39 v 7.97 l -18.78 -0.07 z" ),
        new SvgPathData("3", 25.05, 36.97, "M 2 27.77 l 2.54 -1.87 c 2.2 3.59 4.67 5.96 8.44 5.55 c 5.66 -0.39 8 -6.32 6.82 -10.79 c -1.15 -4.01 -5.83 -5.96 -9.56 -3.83 c -0.88 0.51 -1.73 0.92 -1.89 0.92 c -0.39 0 -1.28 -1.76 -1.15 -2.3 c 2.5 -3.65 8.57 -10.05 8.57 -10.05 h -9.49 v 4.28 h -3.64 v -7.69 h 18.54 v 2.61 c 0 0 -4.97 5.15 -6.61 7.97 c 4.04 0.15 7.3 2.71 8.47 6.76 c 0.68 2.13 0.38 7.01 -0.52 9.11 c -1.49 3.51 -5.57 6.35 -9.3 6.51 c -6.26 0.42 -9.58 -3.96 -11.23 -7.21 z" ),
        new SvgPathData("4", 24.59, 37, "M 12.53 35 v -2.91 h 2.63 v -4.81 h -13.16 v -3.39 l 14.24 -21.88 l 2.33 0.01 v 21.87 h 4.03 v 3.39 h -4.03 l -0.08 4.81 h 2.2 v 2.92 l -8.17 0 z m 2.63 -24.99 l -9.04 13.87 h 9.04 z" ),
        new SvgPathData("5", 24.59, 37, "M 2 26.46 l 3.23 -0.03 c 0.62 1.99 1.98 4.02 3.84 4.58 c 5.24 1.25 8.61 -1.73 9.72 -5.17 c 1.35 -4.17 -0.03 -10.02 -3.64 -12.1 c -2.99 -1.72 -7.43 -0.27 -9.47 3.42 l -2.27 -1.41 l 1.22 13.74 h 14.8 v 3.25 h -11.99 l -0.19 5.72 c 2.58 -1.85 6.55 -1.83 9.13 -0.53 c 4.89 2.37 7.16 7.77 6.21 14.13 c -1.32 6.33 -4.22 9.91 -10.77 10.43 c -7.28 0.07 -9.85 -5.03 -9.82 -8.54 v 0 z" ),
        new SvgPathData("6", 23.74, 36.69, "M 2.06 24.54 c -0.41 -2.78 -0.4 -9.96 -0.06 -12.08 c 1.02 -6.51 3.51 -9.48 7.67 -10.46 c 4.04 -0.95 9.72 0.11 10.97 5.55 l -3.38 0.07 c -1.83 -3.68 -6.45 -2.69 -9.06 -1.09 c -2.08 1.27 -3.02 6.59 -2.62 6.92 c 0 0 3.93 -2.91 7.03 -2.7 c 6.62 0.69 9.36 6.43 9.12 11.86 c -0.21 6.53 -2.28 11.74 -9.3 12.09 c -6.68 -0.26 -9.73 -3.9 -10.38 -10.15 v 0 z m 16.46 -1.72 c 0.04 -4.53 -2.02 -7.92 -5.91 -8.29 c -3.89 -0.37 -7.81 3.42 -7.54 8.04 c 0.27 4.63 2.06 8.53 6.78 8.51 c 5.07 -0.02 6.64 -5.03 6.67 -8.26 z" ),
        new SvgPathData("7", 24.26, 37, "M 7.06 35 c 2.17 -11.47 4.48 -20.09 10.55 -29.43 h -12.33 v 4.02 h -3.27 v -7.59 h 18.03 l 2.22 3.56 c -6.44 7.03 -12.34 29.77 -11.29 29.43 z" ),
        new SvgPathData("8", 25.44, 36.99, "M 2 25.33 c 0.16 -4.43 1.76 -6.09 4.91 -8.41 c -2.52 -1.81 -3.58 -4.49 -3.36 -6.77 c 0.52 -5.51 4.78 -8.27 9.42 -8.15 c 4.65 0.12 8.65 3.81 8.97 7.82 c 0.27 3.48 -0.76 5.16 -3.23 7 c 2.93 2.48 4.81 4.1 4.74 8.47 c -0.07 4.36 -3.78 9.56 -10.25 9.71 c -6.47 0.16 -11.35 -5.23 -11.19 -9.66 z m 18.06 0.02 c 0.28 -4.01 -3.27 -6.72 -7.27 -6.76 c -3.9 -0.04 -7.38 3.28 -7.38 6.89 c 0 3.87 4.53 5.97 7.46 5.98 c 3.56 0.02 6.95 -2.65 7.19 -6.11 z m -1.28 -14.35 c 0.08 -3.37 -3.04 -5.8 -6.25 -5.7 c -2.86 0.09 -5.88 2.27 -5.89 5.09 c 0 3.53 3.44 5.3 6.38 5.37 c 2.67 0.06 5.7 -2.21 5.76 -4.76 z" ),
        new SvgPathData("9", 22.84, 36.32, "M 2.21 28.03 l 3.02 -0.96 c 1.14 3.04 3.44 3.94 5.88 3.75 c 4.37 -0.35 5.82 -5.23 6.15 -9.55 c -1.7 1.81 -4.26 2.72 -6.44 2.63 c -6.97 -0.37 -8.75 -5.95 -8.81 -11.78 c -0.19 -4.92 1.84 -8.48 5.33 -10.12 c 1.85 -0.83 6.08 -0.83 7.94 0.15 c 4.27 2.25 5.85 6.14 5.56 15.43 c 0 7.64 -1.62 16.54 -9.81 16.74 c -4.73 0.65 -8.33 -3.03 -8.81 -6.29 z m 11.86 -8.37 c 1.42 -0.9 2.22 -1.74 2.86 -3.01 c 0.52 -1.04 0.7 -1.58 0.55 -3.73 c -0.39 -5.54 -3.29 -8.69 -7.5 -8.06 c -2.35 0.35 -3.64 1.67 -4.37 4.11 c -1.36 4.52 0.37 9.81 3.6 11.03 c 1.46 0.55 3.71 0.39 4.87 -0.34 z" ),
        new SvgPathData("10", 23.31, 37, "M 2 35 v -33 h 3.2 v 33 z m 0 0 M 13.95 2 c -3.08 0 -5.55 2.81 -5.55 6.31 v 20.39 c 0 3.49 2.48 6.3 5.55 6.3 h 1.8 c 3.08 0 5.56 -2.81 5.56 -6.3 v -20.39 c 0 -3.49 -2.48 -6.31 -5.56 -6.31 z m 0.93 3.12 c 1.82 0 3.28 1.39 3.28 3.14 v 20.66 c 0 1.74 -1.46 3.15 -3.28 3.15 c -1.82 0 -3.28 -1.41 -3.28 -3.15 v -20.66 c 0 -1.74 1.47 -3.14 3.28 -3.14 z m 0 0" ),
        new SvgPathData("J", 24.2, 36.67, "M 11.99 2 v 3.45 h 3.35 c 0 0 0.08 3.33 0.11 10.39 c 0.03 6.86 0.17 10.26 -0.27 12.21 c -1.18 4.5 -7.7 4.53 -8.99 1.29 c -0.75 -1.88 -0.55 -3.35 -0.59 -5.24 h -3.61 c 0 0 0.02 0.95 0.03 2.12 c 0.02 5.06 1.7 7.48 5.62 8.44 c 4.03 1.09 8.75 -0.57 10.43 -4.02 c 1 -1.96 0.88 -2.1 0.91 -13.98 c 0.02 -7.92 0.14 -11.22 0.14 -11.22 h 3.09 v -3.45 h -10.21 z" ),
        new SvgPathData("Q", 24.05, 37, "M 4.76 24.25 h -2.76 v -3.76 h 2.76 c 0 0 0.02 -2.03 0.1 -5.6 c 0.17 -7.62 0.36 -8.8 2.57 -10.9 c 4.35 -4.14 11.65 -0.77 11.56 5.46 c -0.13 8.79 -0.04 17.18 -0.29 18.36 c -0.46 2.09 -0.5 2.21 -0.32 2.59 c 0.38 0.77 3.68 1.1 3.68 0.89 v 3.7 c -2.52 0.2 -3.56 -0.04 -5.57 -1.48 c -1.26 1.21 -5.08 2.43 -8.13 1 c -2.31 -1.09 -4.07 -3.9 -3.59 -10.27 z m 9.32 6.56 c -1.37 -1.97 -3.67 -4.67 -5.83 -5.63 c -0.14 2.27 0.06 4.17 0.86 5.2 c 1.27 1.63 3.32 2 4.97 0.43 z m 1.13 -21.72 c -1.01 -2.87 -3.62 -3.85 -5.52 -2.05 c -1.19 1.12 -1.38 2.3 -1.41 8.69 c -0.02 3.94 -0.02 5.6 -0.02 5.6 c 3.71 1.2 7.17 5.52 7.14 5.47 l 0.09 -11.93 c 0.07 -1.94 -0.02 -3.87 -0.29 -5.78 v 0 z" ),
        new SvgPathData("K", 26.13, 37, "M 13.86 31.37 h 3.32 l -4.88 -12.18 l -3.73 5.15 l 0.03 7.04 h 2.62 v 3.63 h -9.22 v -3.63 h 3.04 v -26.2 l -3.04 0.01 v -3.17 h 9.36 v 3.28 h -2.58 l -0.06 12.77 l 8.17 -12.88 h -2.88 v -3.17 h 9.95 v 3.17 h -2.63 l -6.72 10.52 l 6.58 15.67 h 2.92 l 0.02 3.63 h -10.24 l -0.03 -3.63 z" ),

        new SvgPathData("C", 48.97, 52, "M 24.48 2 c -9.82 0.21 -14.22 11.25 -6.95 19.83 c 1.05 1.25 1.16 1.97 -0.77 0.77 c -4.9 -3.27 -14.28 -1.12 -14.76 8.88 c -0.59 12.32 16.64 15.87 19.87 2.77 c 0.17 -1.38 1.3 -1.35 1.02 0.61 c -0.3 5.32 -1.96 10.3 -4.25 15.15 h 11.67 c -2.29 -4.85 -3.95 -9.83 -4.25 -15.15 c -0.27 -1.96 0.85 -1.99 1.02 -0.61 c 3.24 13.1 20.47 9.55 19.87 -2.77 c -0.48 -10 -9.87 -12.15 -14.76 -8.88 c -1.93 1.2 -1.82 0.48 -0.77 -0.77 c 7.27 -8.57 2.87 -19.61 -6.95 -19.83 c 0 0 0 0 -0.01 0 z" ),
        new SvgPathData("D", 38.92, 52, "M 19.46 50 c -5.19 -8.56 -10.64 -16.89 -17.46 -24 c 6.82 -7.11 12.27 -15.44 17.46 -24 c 5.19 8.56 10.64 16.89 17.46 24 c -6.82 7.11 -12.27 15.44 -17.46 24 z" ),
        new SvgPathData("H", 41.7, 51.99, "M 20.85 49.99 c 0 0 -4.67 -7.66 -10.96 -16.66 c -4.21 -6.02 -7.58 -12.55 -7.89 -18.16 c -0.36 -6.42 3.04 -12.86 9.19 -13.17 c 6.15 -0.31 8.64 4.95 9.66 9.28 c 1.02 -4.34 3.51 -9.6 9.66 -9.28 c 6.15 0.31 9.55 6.75 9.19 13.17 c -0.31 5.55 -3.6 11.98 -7.74 17.95 c -0.05 0.07 -0.1 0.15 -0.15 0.22 c -6.29 9 -10.96 16.66 -10.96 16.66 z" ),
        new SvgPathData("S", 40.88, 49.94, "M 20.44 2 c -8.83 12.59 -18.28 19.84 -18.44 29.67 c -0.05 3.18 1.67 8.59 7.06 9.62 c 3.5 0.67 8.31 -2.16 8.41 -7.91 c -0.02 -1.14 1.21 -1.11 1.2 0.45 c -0.16 4.53 -1.6 9.81 -4.01 14.11 h 11.57 c -2.41 -4.3 -3.85 -9.58 -4.01 -14.11 c -0.01 -1.57 1.22 -1.6 1.2 -0.45 c 0.1 5.75 4.91 8.58 8.41 7.91 c 5.39 -1.04 7.11 -6.44 7.06 -9.62 c -0.16 -9.83 -9.61 -17.07 -18.44 -29.67 z" ),

        new SvgPathData("CS", 21.99, 24, "M 10.99 2 c -3.93 0.09 -5.69 4.69 -2.78 8.26 c 0.42 0.52 0.47 0.82 -0.31 0.32 c -1.96 -1.36 -5.71 -0.47 -5.91 3.7 c -0.24 5.13 6.65 6.61 7.95 1.15 c 0.07 -0.58 0.52 -0.56 0.41 0.25 c -0.12 2.22 -0.78 4.29 -1.7 6.31 h 4.67 c -0.92 -2.02 -1.58 -4.09 -1.7 -6.31 c -0.11 -0.82 0.34 -0.83 0.41 -0.25 c 1.29 5.46 8.19 3.98 7.95 -1.15 c -0.19 -4.16 -3.95 -5.06 -5.91 -3.7 c -0.77 0.5 -0.73 0.2 -0.31 -0.32 c 2.91 -3.57 1.15 -8.17 -2.78 -8.26 c 0 0 0 0 0 0 z" ),
        new SvgPathData("DS", 18.55, 24, "M 9.28 22 c -2.16 -3.57 -4.43 -7.04 -7.28 -10 c 2.84 -2.96 5.11 -6.43 7.28 -10 c 2.16 3.57 4.43 7.04 7.28 10 c -2.84 2.96 -5.11 6.43 -7.28 10 z" ),
        new SvgPathData("HS", 21.98, 23.99, "M 10.99 21.99 c 0 0 -2.22 -3.19 -5.23 -6.94 c -2.01 -2.51 -3.61 -5.23 -3.76 -7.57 c -0.17 -2.67 1.45 -5.36 4.38 -5.49 c 2.93 -0.13 4.12 2.06 4.61 3.87 c 0.48 -1.81 1.67 -4 4.61 -3.87 c 2.93 0.13 4.55 2.81 4.38 5.49 c -0.15 2.34 -1.75 5.06 -3.76 7.57 c -3 3.75 -5.23 6.94 -5.23 6.94 z" ),
        new SvgPathData("SS", 20.83, 22.69, "M 10.42 2 c -4.03 5.12 -8.34 8.07 -8.42 12.07 c -0.02 1.29 0.76 3.49 3.22 3.92 c 1.6 0.27 3.79 -0.88 3.84 -3.22 c -0.01 -0.47 0.55 -0.45 0.55 0.18 c -0.07 1.84 -0.73 3.99 -1.83 5.74 h 5.28 c -1.1 -1.75 -1.76 -3.9 -1.83 -5.74 c -0.01 -0.64 0.55 -0.65 0.55 -0.18 c 0.05 2.34 2.24 3.49 3.84 3.22 c 2.46 -0.42 3.24 -2.62 3.22 -3.92 c -0.07 -4 -4.38 -6.95 -8.42 -12.07 z" ),

    };

    public static SvgPathData getSvgPathData(int index) {
        if (index < 0 || index >= svgPathData.length) {
            Debug.trace(DD, "getSvgPathData() - invalid index: " + index);
            return null;
        }
        return svgPathData[index];
    }

    public static SvgPathData getSvgPathData(String symbol) {
        for (int i = 0; i < svgPathData.length; i++) {
            if (svgPathData[i].symbol.equals(symbol)) {
                return svgPathData[i];
            }
        }

        Debug.trace(DD, "getSvgPathData() - invalid symbol: " + symbol);
        return null;
    }


    /************************************************************************
     ************************************************************************
     * Support code for the Initialization of the Model.
     */  

    private final String[] cardItems = { "Index", "Corner Pip", "Standard Pip", "Face Pip", "Face Image" };

    private static Model model = new Model();

    private Stage stage;
    private MainController mainController;
    private PrimaryController primaryController;
    private AdditionalController additionalController;
    private CardSample sample;


    /************************************************************************
     * Support code for the Initialization of the Model.
     */

    /**
     * Private default constructor - part of the Singleton Design Pattern.
     * Called at initialization only, constructs the single private instance.
     */
    private Model() {
        Debug.trace(DD, "Model constructed.");
    }

    /**
     * Singleton implementation.
     * @return the only instance of the model.
     */
    public static Model getInstance() { return model; }

    public void close() {
        sample.close();
        stage.close();
    }

    /**
     * Called by the controller after the constructor to initialise any 
     * objects after the controls have been initialised.
     */
    public void initialize() {
        Debug.trace(DD, "Model initialize()");

        initializeMainPanel();
        initializePrimaryTabPanel();
        initializeSample();
        initializeAdditionalTabPanel();
    }

    public void setControllers(Stage mainStage, 
        MainController mainC,
        PrimaryController primaryC,
        AdditionalController additionalC) {

        Debug.trace(DD, "Model setControllers()");

        stage = mainStage;
        mainController = mainC;
        primaryController = primaryC;
        additionalController = additionalC;
    }
    
    /**
     * Initialization after a base directory has been selected.
     */
    public void init() {
        Debug.trace(DD, "Model init()");
        sample = new CardSample(this, "Sample");

        // Add watermark to the group first so that it is displayed on the bottom.
        watermarkView = new ImageView();
        group.getChildren().add(watermarkView);
        
        setWatermark();
        
        initializeCardItemPayloads();
        makeCardsDirectory();
        
        // Add handle to the group last so that it is displayed on top.
        group.getChildren().add(box);
        group.getChildren().add(handle);
        
        primaryController.init();
        additionalController.init();
        sample.init();
    }

    /**
     * Synchronise all controls with the model.
     */
    public void syncAllUIs() {
        Debug.trace(DD, "syncAllUIs()");

        mainController.syncUI();
        primaryController.syncUI();
        additionalController.syncUI();
        sample.syncUI();
        updateDisplayForCurrentCard();
    }

    public Stage getStage() { return stage; }
    public MainController getMainController() { return mainController; }
    public PrimaryController getPrimaryController() { return primaryController; }
    public AdditionalController getAdditionalController() { return additionalController; }
    public CardSample getSample() { return sample; }

    public void rebuildGroup() {

        face.removeFromGroup();
        facePip.removeFromGroup();

        standardPip.removeFromGroup();

        cornerPip.removeFromGroup();
        index.removeFromGroup();

        group.getChildren().remove(box);
        group.getChildren().remove(handle);

        int[] priorities = getPriorityList();
        for (int i = priorities.length-1; i >= 0; --i) {

            final int priority = priorities[i];

            switch (priority) {
            case INDEX_ID:          index.addToGroup(); break;
            case CORNER_PIP_ID:     cornerPip.addToGroup(); break;

            case STANDARD_PIP_ID:   standardPip.addToGroup(); break;

            case FACE_PIP_ID:       facePip.addToGroup(); break;
            case FACE_ID:           face.addToGroup(); break;
            }
        }

        group.getChildren().add(box);
        group.getChildren().add(handle);
    }

    public ArrayList<CardItemData> buildCardItemDataList() {

        ArrayList<CardItemData> dataList = new ArrayList<CardItemData>(cardItems.length);

        int[] priorities = getPriorityList();
        for (int i = priorities.length-1; i >= 0; --i) {

            final int priority = priorities[i];

            switch (priority) {
            case INDEX_ID:          dataList.add(index.getData()); break;
            case CORNER_PIP_ID:     dataList.add(cornerPip.getData()); break;

            case STANDARD_PIP_ID:   dataList.add(standardPip.getData()); break;

            case FACE_PIP_ID:       dataList.add(facePip.getData()); break;
            case FACE_ID:           dataList.add(face.getData()); break;
            }
        }

        return dataList;
    }


    public void injectCardItemDataList(ArrayList<CardItemData> dataList) {

        cardItemList.clear();
        selectedCardItemListIndex = -1;

        for (CardItemData data : dataList) {

            final int id = data.getId();

            cardItemList.add(cardItems[id]);

            switch (id) {
            case INDEX_ID:          index.setData(data); break;
            case CORNER_PIP_ID:     cornerPip.setData(data); break;

            case STANDARD_PIP_ID:   standardPip.setData(data); break;

            case FACE_PIP_ID:       facePip.setData(data); break;
            case FACE_ID:           face.setData(data); break;
            }
        }

        rebuildGroup();
    }




    /************************************************************************
     ************************************************************************
     * Support code for "Main" Settings panel.
     */


    /**
     * Short cut to update the status line message.
     * @param Message to display on the status line.
     */
    public void setStatusMessage(String Message) {
        getMainController().setStatusMessage(Message);
    }

    /**
     * Initialize the Main settings panel.
     */
    private void initializeMainPanel() {
        Debug.trace(DD, "Main Settings panel initialized.");

    }




    /************************************************************************
     ************************************************************************
     * Support code for "Sample" panel.
     */

    private Group group;
    private Image handleImage;
    private Handle handle;
    private Rectangle box;
    private ImageView watermarkView;
    private Image watermarkImage;

    private void buildImageBox() {
        box = new Rectangle();
        box.setFill(null);
        box.setStrokeWidth(2);
        box.setStroke(Color.GREY);
        box.setVisible(false);
    }

    private void setWatermark() {
        final String path = getFaceDirectory() + "\\Watermark.png";
        File file = new File(path);

        if (!file.exists()) {
            watermarkView.setImage(null);
            watermarkImage = null;

            return;
        }

        try {
            watermarkImage = new Image(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        watermarkView.setImage(watermarkImage);
    }

    public Image getWatermark() {
        return watermarkImage;
    }

    public boolean showWatermark(int s, int c) {
        if (watermarkImage == null)
            return false;

        if (isFaceCard(c))
            return displayCourtWatermark;

        if (isImageCard(s, c))
            return displayImageWatermark;

        return displayNumberWatermark;
    }

    private void showCurrentWatermark() {
        watermarkView.setVisible(showWatermark(suit, card));
    }


    private void showImageBox() {
        Debug.trace(DD, "showImageBox() : keepAspectRatio = " + keepAspectRatio);
        if (!showGuideBox) {
            box.setVisible(false);

            return;
        }

        final double pX = current.getCentreX();
        final double pY = current.getCentreY();
        final double winX = getWidth() - (2*pX);
        final double winY = cardHeightPX - (2*pY);

        box.setX(pX);
        box.setY(pY);
        box.setWidth(winX);
        box.setHeight(winY);
        box.setVisible(true);
    }

    /**
     * @return the Group used by the "Sample" panel.
     */
    public Group getGroup() {
        return group;
    }

    /**
     * @return the Handle used by the "Sample" panel.
     */
    public Handle getHandle() {
        return handle;
    }

    /**
     * Increase the size of the current card item.
     */
    public void incCurrent() {
        current.incSize();
        syncSpinners();
    }

    /**
     * Decrease the size of the current card item.
     */
    public void decCurrent() {
        current.decSize();
        syncSpinners();
    }

    /**
     * Move the current card item up.
     */
    public void moveCurrentUp(int steps) {
        current.moveUp(steps);
        syncSpinners();
    }

    /**
     * Move the current card item down.
     */
    public void moveCurrentDown(int steps) {
        current.moveDown(steps);
        syncSpinners();
    }

    /**
     * Move the current card item left.
     */
    public void moveCurrentLeft(int steps) {
        current.moveLeft(steps);
        syncSpinners();
    }

    /**
     * Move the current card item right.
     */
    public void moveCurrentRight(int steps) {
        current.moveRight(steps);
        syncSpinners();
    }

    /**
     * Resize of the current card item.
     */
    public void resizeCurrent(int steps) {
        current.resize(steps);
        syncSpinners();
    }

    /**
     * Initialize "Sample" panel.
     */
    private void initializeSample() {
        group = new Group();
        handleImage = new Image(getClass().getResourceAsStream("Handle.png"));
        buildImageBox();
    }




    /************************************************************************
     ************************************************************************
     * Support code for "Primary" Settings Tab panel.
     */

    /**
     * Initialize the Primary settings Tab panel.
     */
    private void initializePrimaryTabPanel() {
        Debug.trace(DD, "Primary Settings Tab panel initialized.");

        initializeInputDirectories();
        initializeGenerate();
        initializeOutputDirectory();
        initializeSampleNavigation();
        initializeCardSize();
        initializeBackgroundColour();
        initializeDisplayCardItems();
        initializeSelectCardItem();
        initializeModifySelectedCardItem();
    }


    /************************************************************************
     * Support code for "Input Directories" panel.
     */

    private final String PATHSFILE = "Files.txt";

    private boolean validBaseDirectory = false;
    private String baseDirectory = ".";
    private String faceStyle;
    private String indexStyle;
    private String pipStyle;

    private Boolean useStandardPips = false;
    private Boolean useStandardIndices = false;

    ObservableList<String> baseList = FXCollections.observableArrayList();
    ObservableList<String> faceList = FXCollections.observableArrayList();
    ObservableList<String> indexList = FXCollections.observableArrayList();
    ObservableList<String> pipList = FXCollections.observableArrayList();

    public ObservableList<String> getBaseList() { return baseList; }

    public ObservableList<String> getFaceList()  { return faceList; }
    public ObservableList<String> getIndexList() { return indexList; }
    public ObservableList<String> getPipList()   { return pipList; }


    public boolean isValidBaseDirectory() { return validBaseDirectory; }

    public String getBaseDirectory() {
        return baseDirectory;
    }

    private String getFaceDirectory() {
        return baseDirectory + "\\faces\\" + faceStyle;
    }

    public String getIndexDirectory() {
        return baseDirectory + "\\indices\\" + indexStyle;
    }

    private String getPipDirectory() {
        return baseDirectory + "\\pips\\" + pipStyle;
    }

    public String getFaceStyle()  { return faceStyle; }
    public String getIndexStyle() { return indexStyle; }
    public String getPipStyle()   { return pipStyle; }

    public Boolean isStandardIndices() { return useStandardIndices; }
    public Boolean isStandardPips() { return useStandardPips; }

    public void setUseStandardIndices(boolean state) {
        useStandardIndices = state;
    }
    public void setUseStandardPips(boolean state) {
        useStandardPips = state;
        standardPip.setVisible(useStandardPips);
        // facePip.setVisible(!useStandardPips);
        // updateCardItemDisplayStatus();
        // updateHandleState();
    }
    /**
     * Set the selected face style and update the necessary card item Payloads.
     * 
     * @param style selected.
     */
    public void setFaceStyle(String style) {
        faceStyle = style;
        setFaceCardItemPayload();
        showCurrentWatermark();
        updateCardItemDisplayStatus();
        updateHandleState();
    }

    /**
     * Set the selected index style and update the necessary card item Payloads.
     * 
     * @param style selected.
     */
    public void setIndexStyle(String style) {
        indexStyle = style;
        setIndexCardItemPayload();
        updateCardItemDisplayStatus();
        updateHandleState();
    }

    /**
     * Set the selected pip style and update the necessary card item Payloads.
     * 
     * @param style selected.
     */
    public void setPipStyle(String style) {
        pipStyle = style;
        setPipCardItemPayloads();
        updateCardItemDisplayStatus();
        updateHandleState();
    }


    /**
     * Read base directory file paths from disc and set up Base, Face, Index 
     * and Pip pull-down lists.
     * 
     * @return
     */
    private boolean readBaseDirectoryFilePathsFromDisc() {
        Debug.trace(DD, "readBaseDirectoryFilePathsFromDisc()");

        // Check if PATHSFILE exists.
        File file = new File(PATHSFILE);
        if (!file.exists()) {
            file = new File(".");

            return false;
        }

        // Read path list file into array.
        try (FileReader reader = new FileReader(PATHSFILE); BufferedReader br = new BufferedReader(reader)) {

            String line;
            while ((line = br.readLine()) != null) {
                baseList.add(line);
                Debug.info(DD, line);
            }
            br.close();
        } catch (IOException e) {
            // e.printStackTrace();
        }

        // If array is not empty use it to fill in baseDirectoryjComboBox.
        if (!baseList.isEmpty()) {
            setBaseDirectory(baseList.get(0));

            if (validBaseDirectory)
                return true;
        }

        return false;
    }

    /**
     * Write the list of base directories to disc with current baseDirectory
     * first.
     * 
     * @return true if list successfully saved to disc, false otherwise.
     */
    private boolean writeBaseDirectoryFilePathsToDisc() {
        Debug.trace(DD, "writeBaseDirectoryFilePathsToDisc()");

        try (FileWriter writer = new FileWriter(PATHSFILE); BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write(baseDirectory + System.lineSeparator());
            for (final String directory : baseList) {
                final String item = directory + System.lineSeparator();
                if (!baseDirectory.equals(directory))
                    bw.write(item);
            }
            bw.close();
        } catch (IOException e) {
            // e.printStackTrace();
            Debug.critical(DD, "writeBaseDirectoryFilePathsToDisc() - Failed saving: " + e);

            return false;
        }

        return true;
    }


    private boolean fillDirectoryList(ObservableList<String> styleList, String directory, String item) {

        String directoryName = directory + "\\" + item;
        final File style= new File(directoryName);

        styleList.clear();
        for (final File styleEntry : style.listFiles()) {
            if (styleEntry.isDirectory()) {
                Debug.info(DD, directoryName + "\\" + styleEntry.getName());
                styleList.add(styleEntry.getName());
            }
        }

        return !styleList.isEmpty();
    }

    /**
     * Set up the base directory, check if it is valid and set up the style 
     * lists. 
     * 
     * @param base path of the directory.
     * @return true if base is a valid base directory, false otherwise.
     */
    public boolean setBaseDirectory(String base) {
        Debug.trace(DD, "model.setBaseDirectory(" + base + ")");

        if (base.equals(""))
            return false;

        if (base.equals(baseDirectory))
            return false;

        File directory = new File(base);

        if (!directory.isDirectory())
            return false;

        baseDirectory = base;

        boolean faces = false;
        boolean indices = false;
        boolean pips = false;

        for (final File fileEntry : directory.listFiles()) {
            if (fileEntry.isDirectory()) {
                final String directoryName = directory.getPath();
                final String item = fileEntry.getName();
                Debug.info(DD, directoryName);
                switch (fileEntry.getName()) {
                case "faces":
                    faces = fillDirectoryList(faceList, directoryName, item);
                    break;
                case "indices":
                    indices = fillDirectoryList(indexList, directoryName, item);
                    break;
                case "pips":
                    pips = fillDirectoryList(pipList, directoryName, item);
                    break;
                }
            }
        }

        validBaseDirectory = (faces && indices && pips);
        if (validBaseDirectory) {
            if (!baseList.contains(baseDirectory))
                baseList.add(baseDirectory);
            writeBaseDirectoryFilePathsToDisc();

            faceStyle = faceList.get(0);
            indexStyle = indexList.get(0);
            pipStyle = pipList.get(0);

            makeCardsDirectory();
        }

        return validBaseDirectory;
    }

    /**
     * Initialize "Input Directories" panel.
     */
    private void initializeInputDirectories() {
        readBaseDirectoryFilePathsFromDisc();
    }



    /************************************************************************
     * Support code for "Generate" panel.
     */

    private boolean generating = false;

    private int currentIndex = 0;
    private Image[] images;

    public boolean isGenerating() { return generating; }
    private void setGenerating(boolean state) { generating = state; }

    public final Color border = Color.GREY;

    public void drawCardIndex(GraphicsContext gc, Image image, Image rotatedImage) {
        index.drawCard(gc, image, rotatedImage);
    }

    public void drawCardCornerPip(GraphicsContext gc, Image image, Image rotatedImage) {
        cornerPip.drawCard(gc, image, rotatedImage);
    }

    public void drawCardFace(GraphicsContext gc, Image image, Image rotatedImage) {
        face.drawCard(gc, image, rotatedImage);
    }

    public void drawCardStandardPip(GraphicsContext gc, Image image, Image rotatedImage, int pattern) {
        standardPip.drawCard(gc, image, rotatedImage, pattern);
    }

    public void drawCardFacePip(GraphicsContext gc, Image image, Image rotatedImage) {
        facePip.drawCard(gc, image, rotatedImage);
    }

    public void drawJokerIndex(GraphicsContext gc, Image image, Image rotatedImage) {
        index.drawJoker(gc, image, rotatedImage);
    }

    public void drawJokerFace(GraphicsContext gc, Image image) {
        face.drawJoker(gc, image);
    }



    private void loadPipImages(int suit) {
        images = new Image[6];

        images[0] = Utils.loadImage(getStandardPipImagePath(suit));
        images[1] = Utils.rotateImage(images[0]);
        images[2] = Utils.loadImage(getCornerPipImagePath(suit));
        images[3] = Utils.rotateImage(images[2]);
        images[4] = Utils.loadImage(getFacePipImagePath(suit));
        images[5] = Utils.rotateImage(images[4]);
    }

    public void startGenerate() {
        // Ensure that the output directory exists.
        makeOutputDirectory();
        setGenerating(true);

        currentIndex = 0;
    }

    /**
     * Get the next card index.
     * @return true if there is a next card, false if finished.
     */
    public boolean nextCardIndex() {
        ++currentIndex;
        if (IsFinished()) {
            return false;
        }

        // Load the pip images ready for the standard cards.
        if (currentIsAce()) {
            final int suit = currentSuit();
            loadPipImages(suit);
        }

        return true;
    }

    public void finishGenerate() {
        setGenerating(false);
    }

    private int currentId() { return currentIndex; }
    private boolean IsFinished() { return currentId() >= Default.DECK_COUNT.getInt(); }

    public Image[] currentImages() { return images; }
    public int currentSuit() { return currentId() / Default.CARD_COUNT.getInt(); }
    public int currentCard() { return currentId() % Default.CARD_COUNT.getInt(); }
    public boolean currentIsJoker() { return currentCard() == 0; }
    private boolean currentIsAce() { return currentCard() == 1; }

    public String currentOutputImagePath() { 
        return getOutputImagePath(currentSuit(), currentCard());
    }


    /**
     * Initialize"Generate" panel.
     */
    private void initializeGenerate() {
    }



    /************************************************************************
     * Support code for "Output Directory" panel.
     */

    private boolean manual = false;
    private String outputName = "";

    public boolean isManual() { return manual; }
    public String getManualOutputName() { return outputName; }

    public void setOutputNameManually(boolean state) {
        manual = state;
    }

    public String getOutputName() {
        if (manual)
            return outputName.equals("") ? "anon" : outputName;

        return faceStyle;
    }

    public void setOutputName(String name) {
        outputName = name;
    }

    public String getOutputDirectory() {
        return baseDirectory + "\\cards\\" + getOutputName();
    }

    public String getSettingsFile() {
        return getOutputDirectory() + "\\" + DATAFILE;
    }

    public boolean isSettingsFileExist() {
        File file = new File(getSettingsFile());

        return file.exists();
    }

    public String getOutputImagePath(int s, int c) {
        return getOutputDirectory() + "\\" + suits[s] + cards[c] + ".png";
    }

    public boolean makeOutputDirectory() {
        File dir = new File(getOutputDirectory());
        if (dir.exists())
            return true;

        return dir.mkdir();
    }

    private boolean makeCardsDirectory() {
        File dir = new File(baseDirectory + "\\cards");
        if (dir.exists())
            return true;

        return dir.mkdir();
    }

    /**
     * Initialize"Output Directory" panel.
     */
    private void initializeOutputDirectory() {
    }



    /************************************************************************
     * Support code for "Sample Navigation" panel.
     */

    private int suit = 0;
    private int card = 10;

    private final String[] suits = { "C", "D", "H", "S" };
    private final String[] alts  = { "S", "H", "D", "C" };
    private final String[] cards = { "Joker", "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };

    
    public int lastSuit() { return suits.length; }
    public int lastCard() { return cards.length; }

    public int getSuit() { return suit; }
    public int getCard() { return card; }

    public void setSuit(int value) { suit = value; }
    public void setCard(int value) { card = value; }

    /**
     * Synchronize the display status of the card items, the watermark and the 
     * handle based on the currently selected card.
     */
    private void updateDisplayForCurrentCard() {
        showCurrentWatermark();
        updateCardItemDisplayStatus();
        setCardItemPayloads();
        updateHandleState();
    }

    public int nextSuit() {
        if (++suit >= suits.length)
            suit = 0;

        updateDisplayForCurrentCard();

        return suit;
    }

    public int nextCard() {
        if (++card >= cards.length)
            card = 1;

        updateDisplayForCurrentCard();

        return card;
    }

    public int prevSuit() {
        if (--suit < 0)
            suit = suits.length - 1;

        updateDisplayForCurrentCard();

        return suit;
    }

    public int prevCard() {
        if (--card <= 0)
            card = cards.length - 1;

        updateDisplayForCurrentCard();

        return card;
    }

    /**
     * @return true if the specified card is a face card (court card), false 
     * otherwise.
     */
    private boolean isFaceCard(int c) {
        return c > 10;
    }

    /**
     * @return true if the specified card has an image file, false otherwise.
     */
    private boolean isImageCard(int s, int c) {
        return isFaceImageExists(s, c);
    }

    /**
     * Initialize "Sample Navigation" panel.
     */
    private void initializeSampleNavigation() {
    }



    /************************************************************************
     * Support code for "Card Size" panel.
     */

    private enum CardSize { POKER, BRIDGE, FREE, MPC };
    
    private CardSize cardSize = CardSize.POKER;

    private double cardWidthPX = Default.WIDTH.getInt();
    private double cardHeightPX = Default.HEIGHT.getInt();

    private SpinnerValueFactory<Integer> widthSVF;
    private SpinnerValueFactory<Integer> heightSVF;
    private SpinnerValueFactory<Double>  radiusSVF;

    public boolean isPokerCardSize() { return cardSize == CardSize.POKER; }
    public boolean isBridgeCardSize() { return cardSize == CardSize.BRIDGE; }
    public boolean isFreeCardSize() { return cardSize == CardSize.FREE; }
    public boolean isMpcCardSize() { return cardSize == CardSize.MPC; }
    public boolean isAutoCardWidth() { return !isFreeCardSize(); }
    public boolean isAutoCardHeight() { return isMpcCardSize(); }
    public boolean isAutoCorners() { return isMpcCardSize(); }

    private void setCardSize(CardSize size) {
        cardSize = size;

        syncCardItemsWithCardSize();
        sample.syncCardSize();
    }

    public void setPokerCardSize() { setCardSize(CardSize.POKER); }
    public void setBridgeCardSize() { setCardSize(CardSize.BRIDGE); }
    public void setFreeCardSize() { setCardSize(CardSize.FREE); }
    public void setMpcCardSize() { setCardSize(CardSize.MPC); }

    /**
     * @return the user set card width in pixels.
     */
    public double getUserWidth() { return cardWidthPX; }

    /**
     * @return the calculated card width in pixels for Poker and Bridge, or 
     * the freely set width.
     */
    public double getWidth() {
        if (cardSize == CardSize.MPC)
            return Default.MPC_WIDTH.getFloat();

        if (cardSize == CardSize.POKER)
            return cardHeightPX * 5 / 7;

        if (cardSize == CardSize.BRIDGE)
            return cardHeightPX * 9 / 14;

        return cardWidthPX;
    }

    /**
     * Set the freely set card width in pixels.
     * 
     * @param width value in pixels.
     */
    public void setWidth(double width) {
        Debug.trace(DD, "setWidth(" + width + ")");
        cardWidthPX = width;

        syncCardItemsWithCardSize();
        sample.syncCardSize();
    }

    /**
     * @return the card height in pixels.
     */
    public double getHeight() {
        if (cardSize == CardSize.MPC)
            return Default.MPC_HEIGHT.getFloat();

        return cardHeightPX;
    }

    /**
     * Set the card height in pixels.
     * 
     * @param height value in pixels.
     */
    public void setHeight(double height) {
        Debug.trace(DD, "setHeight(" + height + ")");
        cardHeightPX = height;

        syncCardItemsWithCardSize();
        sample.syncCardSize();
    }

    public double getMpcBorderWidth() {
        if (!isGenerating())
            return 0D;

        if (cardSize == CardSize.MPC)
            return Default.MPC_BORDER_WIDTH.getFloat();

        return 0D;
    }

    public double getMpcBorderHeight() {
        if (!isGenerating())
            return 0D;

        if (cardSize == CardSize.MPC)
            return Default.MPC_BORDER_WIDTH.getFloat();

        return 0D;
    }

    /**
     * Convert X coordinate percentage to pixels.
     * 
     * @param x coordinate as a percentage of the card width.
     * @return equivalent pixel count.
     */
    public long percentageToPX(double x) {
        return Math.round(x * cardWidthPX / 100); 
    }

    /**
     * Convert Y coordinate percentage to pixels.
     * 
     * @param y coordinate as a percentage of the card height.
     * @return equivalent pixel count.
     */
    public long percentageToPY(double y) {
        return Math.round(y * cardHeightPX / 100); 
    }

    public SpinnerValueFactory<Integer> getWidthSVF()   { return widthSVF; }
    public SpinnerValueFactory<Integer> getHeightSVF()  { return heightSVF; }
    public SpinnerValueFactory<Double>  getRadiusSVF()  { return radiusSVF; }

    public void resetCardWidthSVF()     { widthSVF.setValue(Default.WIDTH.getInt()); }
    public void resetCardHeightSVF()    { heightSVF.setValue(Default.HEIGHT.getInt()); }
    public void resetRadiusSVF()        { radiusSVF.setValue((double)Default.RADIUS.getFloat()); }

    /**
     * Initialize "Card Size" panel.
     */
    private void initializeCardSize() {
        final int WIDTH = Default.WIDTH.getInt();
        final int MIN_WIDTH = Default.MIN_WIDTH.getInt();
        final int MAX_WIDTH = Default.MAX_WIDTH.getInt();
        final int HEIGHT = Default.HEIGHT.getInt();
        final int MIN_HEIGHT = Default.MIN_HEIGHT.getInt();
        final int MAX_HEIGHT = Default.MAX_HEIGHT.getInt();
        final double RADIUS = Default.RADIUS.getFloat();
        final double MIN_RADIUS = Default.MIN_RADIUS.getFloat();
        final double MAX_RADIUS = Default.MAX_RADIUS.getFloat();

        widthSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_WIDTH, MAX_WIDTH, WIDTH);
        heightSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_HEIGHT, MAX_HEIGHT, HEIGHT);
        radiusSVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(MIN_RADIUS, MAX_RADIUS, RADIUS, 0.2);
    }



    /************************************************************************
     * Support code for "Background Colour" panel.
     */

    private Color backgroundColour = Color.WHITE;

    public Color getBackgroundColour() { return backgroundColour; }
    public void setBackgroundColour(Color colour) { backgroundColour = colour; }

    private int colourRealToInt(double comp) {
        return (int)(comp * 256);
    }

    public String getBackgroundColourString() {
        return String.format("rgb(%d, %d, %d)",
                colourRealToInt(backgroundColour.getRed()),
                colourRealToInt(backgroundColour.getGreen()),
                colourRealToInt(backgroundColour.getBlue()));
    }

    /**
     * Initialize "Background Colour" panel.
     */
    private void initializeBackgroundColour() {
    }



    /************************************************************************
     * Support code for "Display Card Items" panel.
     */

    private boolean displayIndex = true;
    private boolean displayCornerPip = true;
    private boolean displayStandardPip = true;
    private boolean displayFaceImage = true;
    private boolean displayFacePip = true;

    public boolean isDisplayIndex() { return displayIndex; }
    public boolean isDisplayCornerPip() { return displayCornerPip; }
    public boolean isDisplayStandardPip() { return displayStandardPip; }
    public boolean isDisplayFaceImage() { return displayFaceImage; }
    public boolean isDisplayFacePip() { return displayFacePip; }

    /**
     * @return true if the index Item for the current card should be 
     * displayed, false otherwise.
     */
    public boolean shouldIndexBeDisplayed() {
        return displayIndex;
    }

    /**
     * @return true if the corner pip Item for the current card should be 
     * displayed, false otherwise.
     */
    public boolean shouldCornerPipBeDisplayed() {
        return displayCornerPip;
    }

    /**
     * @return true if the standard pip Item for the current card should be 
     * displayed, false otherwise.
     */
    public boolean shouldStandardPipBeDisplayed() {
        return shouldStandardPipBeDisplayed(suit, card);
    }

    /**
     * @return true if the face image Item for the current card should be 
     * displayed, false otherwise.
     */
    public boolean shouldFaceImageBeDisplayed() {
        return shouldFaceImageBeDisplayed(suit, card);
    }

    /**
     * @return true if the face pip Item for the current card should be 
     * displayed, false otherwise.
     */
    public boolean shouldFacePipBeDisplayed() {
        return shouldFacePipBeDisplayed(card);
    }

    /**
     * @return true if the standard pip Item should be displayed for the given 
     * card, false otherwise.
     */
    public boolean shouldStandardPipBeDisplayed(int s, int c) {
        if (!isImageCard(s, c))
            return displayStandardPip;
        
        return false;
    }

    /**
     * @return true if the face image Item should be displayed for the given 
     * card, false otherwise.
     */
    public boolean shouldFaceImageBeDisplayed(int s, int c) {
        if (isImageCard(s, c))
            return displayFaceImage;

        return false;
    }

    /**
     * @return true if the face pip Item should be displayed for the given 
     * card, false otherwise.
     */
    public boolean shouldFacePipBeDisplayed(int c) {
        if (isFaceCard(c))
            return displayFacePip;

        return false;
    }

    /**
     * @return true if the specified Item should be displayed for the current 
     * card, false otherwise.
     */
    private boolean shouldItemBeDisplayed(Item item) {
        if (item == Item.INDEX)
            return shouldIndexBeDisplayed();
        if (item == Item.CORNER_PIP)
            return shouldCornerPipBeDisplayed();
        if (item == Item.STANDARD_PIP)
            return shouldStandardPipBeDisplayed();
        if (item == Item.FACE)
            return shouldFaceImageBeDisplayed();
        if (item == Item.FACE_PIP)
            return shouldFacePipBeDisplayed();
 
        return false;
    }

    /**
     * Update the state of the handle based on a change to a Card Item check 
     * box. If the current card item is disabled find the next one available, 
     * otherwise set the state for the current.
     */
    private void updateHandleState() {
        if (!current.isVisible())
            setNextPayload();
        else
            handle.syncDisplayState(shouldItemBeDisplayed(current.getItem()));
    }

    public void setDisplayIndex(boolean state) {
        displayIndex = state;
        index.setVisible(shouldIndexBeDisplayed());
        updateHandleState();
    }

    public void setDisplayCornerPip(boolean state) {
        displayCornerPip = state;
        cornerPip.setVisible(shouldCornerPipBeDisplayed());
        updateHandleState();
    }

    public void setDisplayStandardPip(boolean state) {
        displayStandardPip = state;
        standardPip.setVisible(shouldStandardPipBeDisplayed());
        updateHandleState();
    }

    public void setDisplayFaceImage(boolean state) {
        displayFaceImage = state;
        face.setVisible(shouldFaceImageBeDisplayed());
        updateHandleState();
    }

    public void setDisplayFacePip(boolean state) {
        displayFacePip = state;
        facePip.setVisible(shouldFacePipBeDisplayed());
        updateHandleState();
    }

    /**
     * Initialize "Display Card Items" panel.
     */
    private void initializeDisplayCardItems() {
    }



    /************************************************************************
     * Support code for "Select Card Item" panel.
     */

    public void setCurrentCardItemToIndex() {
        Debug.trace(DD, "setCurrentCardItemToIndex()");
        changeCurrentCardItemAndSyncSpinners(index);
        handle.syncDisplayState(shouldIndexBeDisplayed());
    }

    public void setCurrentCardItemToCornerPip() {
        Debug.trace(DD, "setCurrentCardItemToCornerPip()");
        changeCurrentCardItemAndSyncSpinners(cornerPip);
        handle.syncDisplayState(shouldCornerPipBeDisplayed());
    }

    public void setCurrentCardItemToStandardPip() {
        Debug.trace(DD, "setCurrentCardItemToStandardPip()");
        changeCurrentCardItemAndSyncSpinners(standardPip);
        handle.syncDisplayState(shouldStandardPipBeDisplayed());
    }

    public void setCurrentCardItemToFace() {
        Debug.trace(DD, "setCurrentCardItemToFace()");
        changeCurrentCardItemAndSyncSpinners(face);
        handle.syncDisplayState(shouldFaceImageBeDisplayed());
    }

    public void setCurrentCardItemToFacePip() {
        Debug.trace(DD, "setCurrentCardItemToFacePip()");
        changeCurrentCardItemAndSyncSpinners(facePip);
        handle.syncDisplayState(shouldFacePipBeDisplayed());
    }

    /**
     * Initialize "Select Card Item" panel.
     */
    private void initializeSelectCardItem() {
    }



    /************************************************************************
     * Support code for "Modify Selected Card Item" panel.
     */

    private QuadPayload index = null;
    private QuadPayload cornerPip = null;
    private MultiPayload standardPip = null;
    private ImagePayload face = null;
    private DoublePayload facePip = null;
    private Payload current = null;
    private Payload[] payloadSlider;
    
    private SpinnerValueFactory<Double> itemHeightSVF;
    private SpinnerValueFactory<Double> itemCentreXSVF;
    private SpinnerValueFactory<Double> itemCentreYSVF;

    private boolean keepAspectRatio = true;


    /**
     * @return the current Item.
     */
    public Item getCurrentItem() {
        return current.getItem();
    }

    /**
     * @return the current Item id.
     */
    public int getCurrentItemId() {
        return current.getItem().index();
    }

    /**
     * Set the current Card Item using an id.
     */
    public void setCurrentItemFromId(int id) {
        switch (id) {
        case INDEX_ID:          current = index; break;
        case CORNER_PIP_ID:     current = cornerPip; break;

        case STANDARD_PIP_ID:   current = standardPip; break;

        case FACE_PIP_ID:       current = facePip; break;
        case FACE_ID:           current = face; break;
        }
    }

    /**
     * @return the name of the currently Selected Card Item.
     */
    public String getCurrentCardItemName() {
        final Item item = current.getItem();

        if (item == Item.INDEX)
            return "indices";

        if (item == Item.STANDARD_PIP)
            return "standard pips";

        if (item == Item.FACE_PIP)
            return "face pips";

        if (item == Item.CORNER_PIP)
            return "corner pips";

        return "";
    }


    /**
     * Check if the face image file exists.
     * 
     * @param s suit of card to generate.
     * @param c card number of card to generate.
     * @return true if the face image file exists.
     */
    private boolean isFaceImageExists(int s, int c) {
        File file = new File(getFaceImagePath(s, c));

        return file.exists();
    }

    /**
     * Generate the file path for the face image.
     * 
     * @param s suit of card to generate.
     * @param c card number of card to generate.
     * @return the file path for the face image of the specified card in the 
     * current style.
     */
    public String getFaceImagePath(int s, int c) {
        return getFaceDirectory() + "\\" + suits[s] + cards[c] + ".png";
    }

    /**
     * Generate the file path for the index image.
     * 
     * @param s suit of card to generate.
     * @param c card number of card to generate.
     * @return the file path for the index image of the specified card in the 
     * current style.
     */
    public String getIndexImagePath(int s, int c) {
        String pathToImage = getIndexDirectory() + "\\" + suits[s] + cards[c] + ".png";
        File file = new File(pathToImage);
        if (!file.exists())
            pathToImage = getIndexDirectory() + "\\" + alts[s] + cards[c] + ".png";

        return pathToImage;
    }

    public String getJokerIndexImagePath(int s) {
        return getIndexDirectory() + "\\" + suits[s] + cards[0] + ".png";
    }

    /**
     * Generate the file path for the standard pip image.
     * 
     * @param s suit of card to generate.
     * @return the file path for the standard pip image of the specified card 
     * in the current style.
     */
    public String getStandardPipImagePath(int s) {
        return getPipDirectory() + "\\" + suits[s] + ".png";
    }

    /**
     * Generate the file path for the face pip image.
     * 
     * @param s suit of card to generate.
     * @return the file path for the face pip image of the specified card in 
     * the current style.
     */
    public String getFacePipImagePath(int s) {
        String pathToImage = getPipDirectory() + "\\" + suits[s] + "F.png";
        File file = new File(pathToImage);
        if (file.exists())
            return pathToImage;

        return getStandardPipImagePath(s);
    }

    /**
     * Generate the file path for the corner pip image.
     * 
     * @param s suit of card to generate.
     * @return the file path for the corner pip image of the specified card in 
     * the current style.
     */
    public String getCornerPipImagePath(int s) {
        String pathToImage = getPipDirectory() + "\\" + suits[s] + "S.png";
        File file = new File(pathToImage);
        if (file.exists())
            return pathToImage;

        return getStandardPipImagePath(s);
    }


    /**
     * @return the file path for the face image of the current card in the 
     * current style.
     */
    private String getFaceImagePath() {
        return getFaceImagePath(suit, card);
    }

    /**
     * @return the file path for the index image of the current card in the 
     * current style.
     */
    private String getIndexImagePath() {
        return getIndexImagePath(suit, card);
    }

    /**
     * @return the file path for the standard pip image of the current card in 
     * the current style.
     */
    private String getStandardPipImagePath() {
        return getStandardPipImagePath(suit);
    }

    /**
     * @return the file path for the face pip image of the current card in the 
     * current style.
     */
    private String getFacePipImagePath() {
        return getFacePipImagePath(suit);
    }

    /**
     * @return the file path for the corner pip image of the current card in 
     * the current style.
     */
    private String getCornerPipImagePath() {
        return getCornerPipImagePath(suit);
    }

    /**
     * Get the file path for the image for a specified Item.
     * 
     * @param item for which the image file path is required.
     * @return the file path for the image of the given Item.
     */
    public String getImagePath(Item item) {
        if (item == Item.FACE)
            return getFaceImagePath();

        if (item == Item.INDEX)
            return getIndexImagePath();

        if (item == Item.STANDARD_PIP)
            return getStandardPipImagePath();

        if (item == Item.FACE_PIP)
            return getFacePipImagePath();

        if (item == Item.CORNER_PIP)
            return getCornerPipImagePath();

        return "";
    }

    private void setFaceCardItemPayload() {
        setWatermark();
        face.syncImageFile();
    }

    private void setIndexCardItemPayload() {
        index.syncImageFile();
    }

    private void setPipCardItemPayloads() {
        standardPip.syncImageFile();

        facePip.syncImageFile();

        cornerPip.syncImageFile();
    }

    /**
     * Synchronize the card items with the current selected images.
     */
    private void setCardItemPayloads() {
        setFaceCardItemPayload();
        setIndexCardItemPayload();
        setPipCardItemPayloads();
    }

    /**
     * Set the display status of the card items specifically for the current 
     * card.
     */
    private void updateCardItemDisplayStatus() {
        index.setVisible(shouldIndexBeDisplayed());
        cornerPip.setVisible(shouldCornerPipBeDisplayed());

        showImageBox();
        face.setVisible(shouldFaceImageBeDisplayed());
        standardPip.setVisible(shouldStandardPipBeDisplayed());

        facePip.setVisible(shouldFacePipBeDisplayed());

        handle.syncDisplayState(shouldItemBeDisplayed(current.getItem()));
    }

    /**
     * Find the next displayable card item for the current card and update the 
     * current card item.
     * 
     * @return true if a new displayable card item is found, false otherwise.
     */
    public boolean setNextPayload() {
        int idx = current.getItem().index() + 1;

        while (payloadSlider[idx].getItem() != current.getItem()) {
            if (shouldItemBeDisplayed(payloadSlider[idx].getItem())) {
                changeCurrentCardItemAndSyncSpinners(payloadSlider[idx]);

                handle.syncDisplayState(shouldItemBeDisplayed(current.getItem()));

                return true;
            }

            idx++;
            if (idx > payloadSlider.length)   // Safety check.
                break;
        }

        handle.syncDisplayState(shouldItemBeDisplayed(current.getItem()));

        return false;
    }

    /**
     * Create the card item Payload instances after the base directory has 
     * been selected.
     */
    private void initializeCardItemPayloads() {
        Debug.trace(DD, "initializeCardItemPayloads()");

        face        = new ImagePayload();
        facePip     = new DoublePayload(Item.FACE_PIP);

        standardPip = new MultiPayload();
        
        cornerPip   = new QuadPayload(Item.CORNER_PIP);
        index       = new QuadPayload(Item.INDEX);

        // Set up payload slider used to determine next item.
        final int ITEMS = Default.CARD_ITEM_COUNT.getInt();
        payloadSlider = new Payload[ITEMS * 2];
        payloadSlider[Item.FACE.index()] = face;
        payloadSlider[Item.FACE.index() + ITEMS] = face;
        payloadSlider[Item.INDEX.index()] = index;
        payloadSlider[Item.INDEX.index() + ITEMS] = index;
        payloadSlider[Item.STANDARD_PIP.index()] = standardPip;
        payloadSlider[Item.STANDARD_PIP.index() + ITEMS] = standardPip;
        payloadSlider[Item.FACE_PIP.index()] = facePip;
        payloadSlider[Item.FACE_PIP.index() + ITEMS] = facePip;
        payloadSlider[Item.CORNER_PIP.index()] = cornerPip;
        payloadSlider[Item.CORNER_PIP.index() + ITEMS] = cornerPip;

        initCurrentCardItemAndSyncSpinners(index);
        updateCardItemDisplayStatus();
    }

    /**
     * Tells the card items to synchronize with the current card size and the 
     * handle to reposition.
     */
    private void syncCardItemsWithCardSize() {
        Debug.trace(DD, "syncCardItemsWithCardSize()");

        index.syncCardSize();
        cornerPip.syncCardSize();
        standardPip.syncCardSize();
        face.syncCardSize();
        facePip.syncCardSize();

        showImageBox();
        handle.syncPosition();
        watermarkView.setFitWidth(getWidth());
        watermarkView.setFitHeight(getHeight());
    }


    public SpinnerValueFactory<Double> getItemHeightSVF()   { return itemHeightSVF; }
    public SpinnerValueFactory<Double> getItemCentreXSVF()  { return itemCentreXSVF; }
    public SpinnerValueFactory<Double> getItemCentreYSVF()  { return itemCentreYSVF; }

    public void setKeepImageAspectRatio(boolean state) {
        keepAspectRatio = state;
        face.setKeepAspectRatio(keepAspectRatio);
    }


    public Tooltip getCurrentHButtonTip() { return new Tooltip(current.getItem().getHButtonTip()); }
    public Tooltip getCurrentXButtonTip() { return new Tooltip(current.getItem().getXButtonTip()); }
    public Tooltip getCurrentYButtonTip() { return new Tooltip(current.getItem().getYButtonTip()); }

    public Tooltip getCurrentHToolTip() { return new Tooltip(current.getItem().getHToolTip()); }
    public Tooltip getCurrentXToolTip() { return new Tooltip(current.getItem().getXToolTip()); }
    public Tooltip getCurrentYToolTip() { return new Tooltip(current.getItem().getYToolTip()); }

    public String getCurrentHLabel() { return current.getItem().getHLabel(); }
    public String getCurrentXLabel() { return current.getItem().getXLabel(); }
    public String getCurrentYLabel() { return current.getItem().getYLabel(); }

    /**
     * @return true if the height of the current card item should be user
     * changable, false otherwise.
     */
    public boolean isCurrentHeightChangable() {
        if (!shouldItemBeDisplayed(current.getItem()))
            return false;

        return current.getItem().isCentre();
    }

    /**
     * @return true if the x coordinate of the centre of the current card item 
     * should be user changable, false otherwise.
     */
    public boolean isCurrentXCentreChangable() {
        return shouldItemBeDisplayed(current.getItem());
    }

    /**
     * @return true if the y coordinate of the centre of the current card item 
     * should be user changable, false otherwise.
     */
    public boolean isCurrentYCentreChangable() {
        return shouldItemBeDisplayed(current.getItem());
    }

    /**
     * Round a value to the nearest Default.STEP_SIZE.
     * 
     * @param value to be rounded.
     * @return rounded value.
     */
    private double roundPercentage(double value) {
        long round = Math.round(value / Default.STEP_SIZE.getFloat());
        value = (double)round * Default.STEP_SIZE.getFloat();

        return value;
    }

    /**
     * Set itemHeightSVF to the default value of the currently selected card 
     * item.
     */
    public void resetCurrentHSVF() {
        itemHeightSVF.setValue(roundPercentage(current.getItem().getH()));
    }

    /**
     * Set itemCentreXSVF to the default value of the currently selected card 
     * item.
     */
    public void resetCurrentXSVF() {
        itemCentreXSVF.setValue(roundPercentage(current.getItem().getX()));
    }

    /**
     * Set itemCentreYSVF to the default value of the currently selected card 
     * item.
     */
    public void resetCurrentYSVF() {
        itemCentreYSVF.setValue(roundPercentage(current.getItem().getY()));
    }

    /**
     * Set the height of the currently selected card item.
     * 
     * @param value as a percentage of the card height.
     */
    public void setCurrentH(double value) {
        Debug.trace(DD, "model.setCurrentH(" + value + ");");

        current.setSize(value);
    }

    /**
     * Set the X co-ordinate of the centre of the currently selected card item.
     * 
     * @param value as a percentage of the card width.
     */
    public void setCurrentX(double value) {
        Debug.trace(DD, "model.setCurrentX(" + value + "); :: " + current.getItem());

        current.setX(value);
        if (lockX) {
            if (current.getItem() == Item.INDEX)
                cornerPip.setX(value);
            else if (current.getItem() == Item.CORNER_PIP)
                index.setX(value);
        }
        showImageBox();
        handle.syncPosition();
    }

    /**
     * Set the Y co-ordinate of the centre of the currently selected card item.
     * 
     * @param value as a percentage of the card height.
     */
    public void setCurrentY(double value) {
        Debug.trace(DD, "model.setCurrentY(" + value + "); :: " + current.getItem());

        current.setY(value);
        if (lockY) {
            if (current.getItem() == Item.INDEX)
                cornerPip.setY(value + deltaY);
            else if (current.getItem() == Item.CORNER_PIP)
                index.setY(value - deltaY);
        }
        showImageBox();
        handle.syncPosition();
    }

    public void setCurrentPos(double xPos, double yPos) {
        Debug.trace(DD, "model.setCurrentPos(" + xPos + ", " + yPos + "); :: " + current.getItem());

        current.setPos(xPos, yPos);
        showImageBox();
        handle.syncPosition();

        itemCentreXSVF.setValue(roundPercentage(xPos));
        itemCentreYSVF.setValue(roundPercentage(yPos));
    }

    /**
     * Get the X co-ordinate of the centre of the currently selected card item.
     */
    public double getCurrentX() {
        return current.getCentreX();
    }

    /**
     * Get the Y co-ordinate of the centre of the currently selected card item.
     */
    public double getCurrentY() {
        return current.getCentreY();
    }


    /**
     * Init the current card item, create the handle and adjust the Card Item 
     * spinners.
     * 
     * @param item currently selected card item Payload.
     */
    private void initCurrentCardItemAndSyncSpinners(Payload item) {
        current = item;
        handle = new Handle(handleImage, current);
        handle.setPayload(current);
        syncSpinners();
    }

    /**
     * Change the current card item, adjust the Card Item spinners and update 
     * the handle with the new Payload.
     * 
     * @param item currently selected card item Payload.
     */
    private void changeCurrentCardItemAndSyncSpinners(Payload item) {
        current = item;
        handle.setPayload(current);
        syncSpinners();
    }

    /**
     * Sync the Card Item spinners to the current payload.
     */
    private void syncSpinners() {
        Debug.trace(DD, "syncSpinners() :: " + current.getItem());

        final double h = current.getSpriteH();
        final double x = current.getSpriteX();
        final double y = current.getSpriteY();

        itemHeightSVF.setValue(roundPercentage(h));
        itemCentreXSVF.setValue(roundPercentage(x));
        itemCentreYSVF.setValue(roundPercentage(y));

        current.update(x, y, h);
        showImageBox();
        handle.syncPosition();
    }

    /**
     * Initialize "Modify Selected Card Item" panel.
     */
    private void initializeModifySelectedCardItem() {
        final double step = Default.STEP_SIZE.getFloat();
        itemHeightSVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(step, 100, 10, step);
        itemCentreXSVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 10, step);
        itemCentreYSVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 10, step);
    }




    /************************************************************************
     ************************************************************************
     * Support code for "Additional" Settings Tab panel.
     */

    /**
     * Initialize the Additional settings Tab panel.
     */
    private void initializeAdditionalTabPanel() {
        Debug.trace(DD, "Additional Settings Tab panel initialized.");

        initializeCardCorners();
        initializeDisplayWatermark();
        initializeModifyCardItem();
        initializeCardItemPriority();
        initializeJokers();
        initializeStandardColours();
    }


    /************************************************************************
     * Support code for "Card Corners and Cropping" panel. 
     */

    private boolean independentlySetCornerRadii = false;

    private double arcWidth = Default.RADIUS.getFloat();
    private double arcHeight = Default.RADIUS.getFloat();

    private SpinnerValueFactory<Double>  arcWidthSpinner;
    private SpinnerValueFactory<Double>  arcHeightSpinner;

    private boolean cropCorners = false;

    public double getArcWidth() { return arcWidth; }
    public double getArcHeight() { return arcHeight; }

    
    /**
     * @return the user defined corner arc width radius in pixels.
     */
    private double getUserArcWidthPX() {
        return getWidth() * arcWidth / 100;
    }

    /**
     * @return the corner arc width radius in pixels.
     */
    public double getArcWidthPX() {
        if (independentlySetCornerRadii)
            return getUserArcWidthPX();

        return getArcHeightPX();
    }

    /**
     * @return the corner arc height radius in pixels.
     */
    public double getArcHeightPX() {
        return getHeight() * arcHeight / 100;
    }

    /**
     * Set the corner arc width radius as a percentage of the card width.
     * 
     * @param radius value as a percentage of the card width.
     */
    public void setArcWidth(double radius) {
        arcWidth = radius;

        syncCardItemsWithCardSize();
    }

    /**
     * Set the corner arc height radius as a percentage of the card height.
     * 
     * @param radius value as a percentage of the card height.
     */
    public void setArcHeight(double radius) {
        arcHeight = radius;

        syncCardItemsWithCardSize();
    }

    public boolean isSetCornerRadiiIndependently() { return independentlySetCornerRadii; }
    public void setCornerRadiiIndependently(boolean state) { independentlySetCornerRadii = state; }

    public SpinnerValueFactory<Double>  getArcWidthSVF()  { return arcWidthSpinner; }
    public SpinnerValueFactory<Double>  getArcHeightSVF()  { return arcHeightSpinner; }

    public void resetArcWidthSVF()    { arcWidthSpinner.setValue((double)Default.RADIUS.getFloat()); }
    public void resetArcHeightSVF()   { arcHeightSpinner.setValue((double)Default.RADIUS.getFloat()); }

    public boolean isCropCorners() { return isAutoCorners() ? false : cropCorners; }
    public void setCropCorners(boolean state) { cropCorners = state; }


    /**
     * Initialize "Card Corners" panel.
     */
    private void initializeCardCorners() {
        final double RADIUS = Default.RADIUS.getFloat();
        final double MIN_RADIUS = Default.MIN_RADIUS.getFloat();
        final double MAX_RADIUS = Default.MAX_RADIUS.getFloat();

        arcWidthSpinner = new SpinnerValueFactory.DoubleSpinnerValueFactory(MIN_RADIUS, MAX_RADIUS, RADIUS, 0.2);
        arcHeightSpinner = new SpinnerValueFactory.DoubleSpinnerValueFactory(MIN_RADIUS, MAX_RADIUS, RADIUS, 0.2);
    }



    /************************************************************************
     * Support code for "Display Watermark" panel. 
     */

    private boolean displayCourtWatermark = true;
    private boolean displayImageWatermark = true;
    private boolean displayNumberWatermark = true;

    public boolean isDisplayCourtWatermark() { return displayCourtWatermark; }
    public void setDisplayCourtWatermark(boolean state) {
        displayCourtWatermark = state;
        showCurrentWatermark();
    }

    public boolean isDisplayImageWatermark() { return displayImageWatermark; }
    public void setDisplayImageWatermark(boolean state) {
        displayImageWatermark = state;
        showCurrentWatermark();
    }

    public boolean isDisplayNumberWatermark() { return displayNumberWatermark; }
    public void setDisplayNumberWatermark(boolean state) { 
        displayNumberWatermark = state;
        showCurrentWatermark();
    }


    /**
     * Initialize "Display Watermark" panel.
     */
    private void initializeDisplayWatermark() {
    }



    /************************************************************************
     * Support code for "Modify Selected Card Item" panel. 
     */

    private boolean lockX = false;
    private boolean lockY = false;
    private double deltaY = 0;

    private boolean leftHanded = false;
    private boolean showGuideBox = false;

    public boolean isLockX() { return lockX; }
    public boolean isLockY() { return lockY; }

    public void setLockX(boolean state) {
        lockX = state;

        if (lockX)
            cornerPip.setX(index.getSpriteX());

        showImageBox();
        handle.syncPosition();
    }

    public void setLockY(boolean state) {
        lockY = state;

        if (lockY)
            deltaY = cornerPip.getSpriteY() - index.getSpriteY();

        showImageBox();
        handle.syncPosition();
    }

    public boolean isLeftHanded() { return leftHanded; }
    public boolean isShowGuideBox() { return showGuideBox; }

    public void setLeftHanded(boolean state) {
        leftHanded = state;
        index.syncQuadState();
        cornerPip.syncQuadState();
    }

    public void setShowGuideBox(boolean state) {
        showGuideBox = state;
        showImageBox();
    }

    /**
     * Initialize "Modify Selected Card Item" panel.
     */
    private void initializeModifyCardItem() {
    }



    /************************************************************************
     * Support code for "Card Item Priority" panel. 
     */

    private ObservableList<String> cardItemList;
    private int selectedCardItemListIndex = -1;

    public ObservableList<String> getCardItemList() { return cardItemList; }

    public void setSelectedCardItem(String item) {
        selectedCardItemListIndex = cardItemList.indexOf(item);
    }

    public int getSelectedCardItemListIndex() { return selectedCardItemListIndex; }
    public boolean isUpAvailable() { return selectedCardItemListIndex != 0; }
    public boolean isDownAvailable() { return selectedCardItemListIndex != 4; }

    public int moveSelectedCardItemUp() {
        int index = selectedCardItemListIndex;
        String item = cardItemList.get(index);

        index--;
        cardItemList.remove(item);
        cardItemList.add(index, item);
        selectedCardItemListIndex = index;
        rebuildGroup();

        return selectedCardItemListIndex;
    }

    public int moveSelectedCardItemDown() {
        int index = selectedCardItemListIndex;
        String item = cardItemList.get(index);

        index++;
        cardItemList.remove(item);
        cardItemList.add(index, item);
        selectedCardItemListIndex = index;
        rebuildGroup();

        return selectedCardItemListIndex;
    }

    public void resetPriorityList() {
        cardItemList.clear();
        cardItemList.addAll(cardItems);
        selectedCardItemListIndex = -1;
        rebuildGroup();
    }

    public int[] getPriorityList() {
        int[] priorities = new int[cardItems.length];

        for (int i = 0; i < priorities.length; i++)
            priorities[cardItemList.indexOf(cardItems[i])] = i;

        return priorities;
    }

    private void initializeCardItemPriority() {
        cardItemList = FXCollections.observableArrayList(cardItems);
    }

    /************************************************************************
     * Support code for "Jokers" panel. 
     */

    private boolean borderlessJokers = false;

    public boolean isBorderlessJokers() { return borderlessJokers; }

    public void setBorderlessJokers(boolean state) {
        borderlessJokers = state;
    }

    /**
     * Initialize "Jokers" panel.
     */
    private void initializeJokers() {
    }

    /************************************************************************
     * Support code for "Select Standard Index/Pip Colour" panel. 
     */
    private Color clubIndexColour = Color.BLACK;
    private Color diamondIndexColour = Color.RED;
    private Color heartIndexColour = Color.RED;
    private Color spadeIndexColour = Color.BLACK;
    private Color clubPipColour = Color.BLACK;
    private Color diamondPipColour = Color.RED;
    private Color heartPipColour = Color.RED;
    private Color spadePipColour = Color.BLACK;

    public Color getClubIndexColour() { return clubIndexColour; }
    public Color getDiamondIndexColour() { return diamondIndexColour; }
    public Color getHeartIndexColour() { return heartIndexColour; }
    public Color getSpadeIndexColour() { return spadeIndexColour; }
    public Color getClubPipColour() { return clubPipColour; }
    public Color getDiamondPipColour() { return diamondPipColour; }
    public Color getHeartPipColour() { return heartPipColour; }
    public Color getSpadePipColour() { return spadePipColour; }

    public void setClubIndexColour(Color colour) { clubIndexColour = colour; }
    public void setDiamondIndexColour(Color colour) { diamondIndexColour = colour; }
    public void setHeartIndexColour(Color colour) { heartIndexColour = colour; }
    public void setSpadeIndexColour(Color colour) { spadeIndexColour = colour; }
    public void setClubPipColour(Color colour) { clubPipColour = colour; }
    public void setDiamondPipColour(Color colour) { diamondPipColour = colour; }
    public void setHeartPipColour(Color colour) { heartPipColour = colour; }
    public void setSpadePipColour(Color colour) { spadePipColour = colour; }

    public Color getIndexColour() {
        switch (suit) {
            case 0: return clubIndexColour;
            case 1: return diamondIndexColour;
            case 2: return heartIndexColour;
        }

        return spadeIndexColour;
    }

    public Color getPipColour() {
        switch (suit) {
            case 0: return clubPipColour;
            case 1: return diamondPipColour;
            case 2: return heartPipColour;
        }

        return spadePipColour;
    }

    /**
     * Initialize "Select Standard Index/Pip Colour" panel.
     */
    private void initializeStandardColours() {

    }

}
