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
* Upload the `src/test/resources/fixed-sample-scan.json` file as an
  artifact to an SSC application version, selecting the `3rd party results` 
  checkbox and `SAMPLE_ALTERNATIVE` as the scan type in the SSC artifact upload 
  dialog
  
## Developing your own parser plugin

The official documentation for developing SSC parser plugins is available at
https://github.com/fortify/plugin-api, and the official sample plugin implementation is available at https://github.com/fortify/sample-parser.

If you want to use this alternative example as a starting point for developing
your own custom SSC parser plugin:

* Generic changes:
    * Update `settings.gradle` according to the name of your project.
    * Rename packages/classes according to the type of data that you
      will be parsing.
    * Update `src/main/resources/images` with your plugin icon and logo.
    * Update `src/main/resources/resources` with relevant message properties.
    * Update `src/main/resources/viewtemplate` with the SSC view template
      used to display issue details.
    * Update `src/main/resources/plugin.xml` with your plugin name, 
      icon/logo, message property files, view template, ...
    * Update CustomVulnAttribute.java to reflect the custom issue
      attributes that can be reported by your plugin.
    * Implement the actual functionality for parsing your data 
      (see below).
    * Update the main parser plugin class to invoke your actual
      parser implementations, and log the appropriate start/stop
      messages.
    * Add one or more sample input files in `src/test/resources`,
      and create/update corresponding JUnit tests (see 
      SampleParserPluginTest.java).
    * Update `src/main/resources/plugin.xml` based on all changes
      above and other plugin configuration.
    * Update `src/main/resources/META-INF/services/com.fortify.plugin.spi.ParserPlugin`
      to point to your renamed parser plugin class.
    * If you need any additional/other dependencies for your plugin,
      update `build.gradle`.
     
* If you need to parse JSON data:
    * Develop AbstractParser subclasses similar to ScanParser and 
      FindingsParser in this example project, according to the 
      structure of your JSON data, using either lambda-expression 
      based handlers or @JsonProperty annotations to process the 
      actual JSON data.
    * Depending on the complexity of your JSON data, you may want to
      add some additional utility methods to AbstractParser.
* If you need to parse other types of data, for example XML:
     * Develop a new abstract parser class that handles the technical
       aspects of parsing data structures of a given type, for example
       based on XML streaming API's.
     * Develop subclasses of your abstract parser class, according to 
       the structure of your JSON data, using either lambda-expression
       based handlers or annotations to process the actual data.

      

# Licensing

See [LICENSE.TXT](LICENSE.TXT)

