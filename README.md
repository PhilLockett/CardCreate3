# CardCreate3

`CardCreate3` is a JavaFX application for rapid playing card prototyping.

**USE AT OWN RISK.**

## Overview

`CardCreate3` generates .png images representing a deck of playing cards from 
existing images. It is very configurable, allowing for playing cards of 
greatly varying styles to be created.

To use `CardCreate3` you will need a Java Development Kit and Maven installed. 

## Cloning and Building

The code has been structured as a standard Maven project which requires Maven 
and a JDK to be installed. A quick web search will help, alternatively 
https://maven.apache.org/install.html should guide you through the install.

The following command clones `CardCreate3`:

    git clone https://github.com/PhilLockett/CardCreate3.git

## Setting up the 'CardCreate3' environment

`CardCreate3` works with an environment that provides the component images 
needed to compose the playing card images. **Before** running `CardCreate3` it 
is recommended that the environment is setup first. The GitHub repository 
contains the file `CardCreate3/CardWork.tar.gz` which provides this 
environment. It is recommended that this environment is set up outside of the 
"CardCreate3" directory. The environment can be set up (for example) in the 
parent directory of "CardCreate3" with the following commands:

    cp CardCreate3/CardWork.tar.gz .
    tar zxf CardWork.tar.gz
    rm CardWork.tar.gz
    cd CardWork/
    ./setup.sh
    cd ..

## Running 'CardCreate3'

`CardCreate3` can be launched from the "CardCreate3" directory using the maven 
command:

    mvn clean javafx:run

When running for the **first time**, `CardCreate3` requires that you select the 
environment, such as the one setup in the parent directory as described above.
Browse to and select the "CardWork" directory.
Once setup you will not be prompted again, however, you can click on the 
"Browse..." button at any time to select another instance of the environment. 
Also, the environment "Base" path text box pull-down allows for easy 
switching between previously selected environment paths.

Warning: the standard `mvn clean` command will remove all generated files, 
including any environment file paths previously set up.

## Further reading

The document "Card Create User Guide.pdf" describes the installation, the 
environment set up and `CardCreate3` usage with many examples.

## Additional packages

Additional packages are currently unavailable.

## Points of interest

This code has the following points of interest:

  * 'CardCreate3' is the JavaFX version of [CardCreate](https://github.com/PhilLockett/CardCreate.git).
  * 'CardCreate3' is a maven project.
  * 'CardCreate3' is structured as an MVC project (FXML being the Video component).
  * The Model is implemented as a basic (non thread safe) Singleton.
  * Multi stage initialization minimizes the need for null checks.
  * The user GUI was developed using SceneBuilder utilizing FXML and CSS.
  * A `static` Debug object helps control diagnostic output when needed.
