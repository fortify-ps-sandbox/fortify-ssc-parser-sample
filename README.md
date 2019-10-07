# Fortify SSC Sample Parser

This project provides an alternative implementation for the Fortify SSC sample
parser provided at https://github.com/fortify/sample-parser.

## Comparison with original sample parser

Compared to the original sample parser:

* This project only provides parser functionality; it currently doesn't include
  functionality for generating sample parser input.
* This project does not include the Gradle wrapper, in order to make use of more
  recent Gradle version features. You will need to have Gradle installed on
  your system in order to build this project; Gradle version 5.3 is known to
  work fine.
* This alternative implementation provides better separation of concerns:
    * The main parser plugin class just provides very simple implementations for 
     the parser SPI methods; actual parsing is done by dedicated parser classes.
    * All technical JSON parsing (looking for start and end of objects/arrays) is done in the
     AbstractParser class.
    * Concrete parser classes extend from AbstractParser; these concrete classes follow 
     the structure of the JSON input data, and simply define handlers or 
     @JsonPropery-annotated fields for handling specific JSON elements.
* This implementation includes some incomplete unit tests. These unit tests will
  try to parse a sample input file, failing if there are any parsing exceptions.
  Information about the parsed data is sent to stderr, but the unit tests don't
  test whether the actual data was parsed correctly.
     
## Usage instructions

* Download/clone the source code
* Run `gradle build` in the project directory
* Install and enable the plugin jar in SSC; the plugin jar is available in the 
  `build/libs` directory after running `gradle build`
* Upload the `src/main/resources/fixed-sample-scan.json` file as an
  artifact to an SSC application version, selecting the `3rd party results` 
  checkbox and `SAMPLE_ALTERNATIVE` as the scan type in the SSC artifact upload 
  dialog
  
