# Chatr Android

Welcome to the official repository for the Chartr Android App!

## Overview

This repository holds the Android Studio project and source code for our Android app. 
Currently, the Android App supports the following actions:
* Creating new users
* Posting new trips
* Searching trips
* Requesting to join trips
* Accepting/rejecting interested riders from trips
* Viewing past trips
* Viewing posted/pending/confirmed trips

## Contributors

* Christian Cygnus
* Marco Valentino
* Gabriel Hayat
* Pawel Galusza
* Shin Taniguchi
* Michael Rush

## Contributing

The contributing guide can be found [here](CONTRIBUTING.md).

## Project Documentation

The final project documentation can be found [here](project_documentation.pdf).

Javadoc HTML documentation can be generated in Android Studio by going to `Tools -> Generate Javadoc`. This will give you a selection of which packages to generate the Javadoc for, as well as the output location. From there, documentation for all of our public classes and methods can be viewed.

## Installation

To install the Chartr Android App (for development or deployment), follow these steps:
1. Install `Android Studio` and the `Java Development Kit` on your system
3. Clone the Chartr Android repository with git
4. Build the project through Android Studio or by running `./gradlew build` in the Chartr Android directory

The rest of the process of working on the Chartr App is similar to any other Android app. You can run the app on an emulator through Android Studio, and run the unit tests by either running the build as described above, or clicking on the "test" package and selecting "Run All" in the Android Studio menu.
