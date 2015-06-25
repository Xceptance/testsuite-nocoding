# TestSuite-NoCoding

This is a test suite for Xceptance LoadTest (XLT) - http://www.xceptance-loadtest.com/. 

* Name: testsuite-nocoding
* Version: 0.9
* Release: January 2014
* License: Apache V2.0
* License URI: http://www.apache.org/licenses/LICENSE-2.0.txt
* Tags: load testing, performance testing, testing
* Contributors:
 * Hartmut Arlt, Xceptance Software Technologies GmbH
 * René Schwietzke, Xceptance Software Technologies GmbH
 * Jörg Werner, Xceptance Software Technologies GmbH
 * Matthias Mitterreiter, Xceptance Software Technologies GmbH

## Introduction

One is often faced with the requirement to just fire a couple of simple URLs 
to verify the performance or functionality of a single feature, to create load to stress environments 
and monitor certain things, or simply to create load in addition to another load test. 

_TestSuite-NoCoding_ delivers an easy way to fire URLs.
Additionally, it can insert data, take over data from one page and insert it into the next request, 
run validations against returned data, as well as give you a way to extend all of that, 
because the source is free and licensed under the Apache License V2.0.

## Features

* Reads test definitions from files. Supported file types:
  * CSV (Comma-separated values)
  * YAML (YAML Ain’t Markup Language)
* Supports Http response validation.
* Allows two modes to define and run tests:
  * DOM mode : HtmlUnit parses the responses into a DOM (slower)
  * Lightweight mode - no DOM overhead, just plain HTTP and HTTPS traffic, and very low resource consumption.
* Cookie handling is automatic.
* Javascript can be executed automatically if wanted.
* Static content can be handled automatically or specified manually.
* Does not disable any XLT features.
* ...

##  Most Wanted 

The following is an unordered lsit of the most hurting issues and should be resolved fast in order to ensure the functionality of the test-suite.

Issue  |  Type | Status
------------- | :-------------: | :-------------:
The WebClient must be configurated with properties (js, static content, credentials ...).  | :question: | :x:
Data from the files don't overwrite the properties. | :beetle: | :x:
HtmlAttributes and Textfields arre not validatable | :beetle: | :x:
CSV file Loader does not work | :ghost: | :x:
Parameter encoding is messy. | :poop: | :x:
Functional and supporting logystem  | :ghost: | :x:
Expressive and functional exception handling (creation, execution, validation). | :ghost: | :x:
Are naming conventions expressive? | :question: | :x:
Write the documentation about automated request header setting | :ghost: | :x:
The "Matches" validation mode doesnt work! | :beetle: | :x:
Firebug delivers helpless "XPath"s. | :beetle: | :x: 
Unravel the mystery about "getEffectiveKey()". | :ghost: | :x:
Write the documentation about supported filetypes. | :ghost: | :x:
Support multiple request methods. |:ghost: | :x:
Testcase mapping doesn't work. | :beetle: | :x:


#### Symbology

:x: ... open, :white_check_mark: ... resolved.

:question: ... open question or unclear behaviour.

:beetle: ... [group of insects which are biologically classified in the order Coleoptera](https://en.wikipedia.org/?title=Beetle). Kill it!

:ghost: ... fictional creature which is somehow there but isnt!. Must be fully implemented or taken out completely!

:poop: ... self-descriptive.

## Planed

The following is an unordered list of futuristic features.

* Support multiple request encoding types.  
* Custom js execution engine for dynamic parameter creation. 
* Implement ParamterInterpreter that handles method invocation recursively 
* Expand the DataProvider with a set of valid data multiples.
* Wiki with simple tutorials. 
* Support CSS Path. 



## Classes

The following is a simple overview of the classes and their individual stage of production.

Class | Scripted | Tested | Documented| Error Handling | Logging
----|:----:|:----:|:----:|:----:|:----:
AbstractURLTestCase | :white_check_mark: |:x: | :x: | :x: | :x: 
ConcreteNodeList  | :white_check_mark: | :x: | :x: | :x: | :x: 
CSVBasedURLActionDataListBuilder  |:white_check_mark: | :x: | :x: | :x: | :x:  
Downloader  |:white_check_mark: | :x: | :x: | :x: | :x:  
HtmlPageAction  | :white_check_mark:| :x: | :x: | :x: | :x: 
HtmlPageActionFactory  | :white_check_mark:| :x: | :x: | :x: | :x:  
LightWeightPageAction  |:white_check_mark: | :x: | :x: | :x: | :x:   
LightWeightPageActionFactory  | :white_check_mark:| :x: | :x: | :x: | :x: 
MockObjects  |:white_check_mark: | :x: | :x: | :x: | :x: 
MockWebResponse  |:white_check_mark: | :x: | :x: | :x: | :x: 
ModifiedAbstractHtmlPageAction  | :white_check_mark:| :x: | :x: | :x: | :x: 
ModifiedAbstractLightWeightPageAction  |:white_check_mark: | :x: | :x: | :x: | :x: 
ModifiedLWPageUtilities  | :white_check_mark:| :x: | :x: | :x: | :x: 
ModifiedRegExUtils  | :white_check_mark:|:x: | :x: | :x: | :x: 
ParameterInterpreter  | :white_check_mark:|:x: | :x: | :x: | :x: 
ParameterInterpreterNow  | :white_check_mark:| :x: | :x: | :x: | :x: 
ParameterInterpreterRandom  | :white_check_mark:|:x: | :x: | :x: | :x: 
ParameterInterpreterRandomTest  |:white_check_mark: | :x: | :x: | :x: | :x: 
ParameterUtils  |:white_check_mark: | :x: | :x: | :x: | :x: 
ParameterUtils.Reason  |:white_check_mark: | :x: | :x: | :x: | :x: 
RegexLab  |:white_check_mark: | :x: | :x: | :x: | :x: 
RegexTestHarness |:white_check_mark: |:x: | :x: | :x: | :x: 
StaticContentDownloader |:white_check_mark: | :x: | :x: | :x: | :x: 
URLAction | :white_check_mark:| :x: | :x: | :x: | :x: 
URLActionData |:white_check_mark: | :x: | :x: | :x: 
URLActionDataBuilder |:white_check_mark: | :x: | :x: | :x: | :x: 
URLActionDataExecutable | :white_check_mark:| :x: | :x: | :x: | :x: 
URLActionDataExecutableFactory | :white_check_mark:|:x: | :x: | :x: | :x: 
URLActionDataExecutableFactoryBuilder | :white_check_mark:|:x: | :x: | :x: | :x: 
URLActionDataExecutableResult |:white_check_mark: |:x: | :x: | :x: | :x: 
URLActionDataExecutableResultFactory |:white_check_mark: | :x: | :x: | :x: | :x: 
URLActionDataListBuilder |:white_check_mark: | :x: | :x: | :x: | :x: 
URLActionDataListFacade |:white_check_mark: | :x: | :x: | :x: | :x: 
URLActionDataRequestBuilder | :white_check_mark:| :x: | :x: | :x: | :x: 
URLActionDataResponseHandler |:white_check_mark: | :x: | :x: | :x: | :x: 
URLActionDataStore | :white_check_mark:| :x: | :x: | :x: | :x: 
URLActionDataStoreBuilder |:white_check_mark: | :x: | :x: | :x: | :x: 
URLActionDataStoreResponseHandler |:white_check_mark: | :x: | :x: | :x: | :x: 
URLActionDataValidation |:white_check_mark: | :x: | :x: | :x: | :x: 
URLActionDataValidationBuilder |:white_check_mark: | :x: | :x: | :x: | :x: 
URLActionDataValidationResponseHandler | :white_check_mark:| :x: | :x: | :x: | :x: 
URLActionStore | :white_check_mark:|:x: | :x: | :x: | :x: 
URLActionValidation | :white_check_mark:| :x: | :x: | :x: | :x: 
URLTestCase | :white_check_mark:| :x: | :x: | :x: | :x: 
UserAgentUtils |:white_check_mark: | :x: | :x: | :x: | :x: 
XhrHtmlPageAction |:white_check_mark: | :x: | :x: | :x: | :x: 
XhrLightWeightPageAction |:white_check_mark: | :x: | :x: | :x: | :x: 
XltMockWebConnection |:white_check_mark: | :x: | :x: | :x: | :x: 
XPathGetable | :white_check_mark:| :x: | :x: | :x: | :x: 
XPathWithHtmlPage |:white_check_mark: | :x: | :x: | :x: | :x: 
XPathWithLightWeightPage | :white_check_mark:| :x: | :x: | :x: | :x: 
XPathWithNonParseableWebResponse |:white_check_mark: | :x: | :x: | :x: | :x: 
XPathWithParseableWebResponse | :white_check_mark:| :x: | :x: | :x: | :x: 
YAMLBasedURLActionBuilder |:white_check_mark: |:x: | :x: | :x: | :x: 
YAMLBasedURLActionDataListBuilder | :white_check_mark:| :x: | :x: | :x: | :x: 

#### Symbology

:white_check_mark: ... done.

:x: ... open.

## Requirements

You only need [Xceptance LoadTest](http://www.xceptance-loadtest.com/) and, of course, Java installed. 
XLT&reg; is available free of charge for functional and small scale performance testing.
If you want to debug or extend the test suite, Ant and Eclipse or any other IDE are helpful.

* Xceptance LoadTest: http://www.xceptance-loadtest.com/
* JDK 7 
* Apache Ant v1.7.0 or higher
* Eclipse or any other Java IDE

## Installation

This test suite is a standard XLT test suite.
If you already know how to work with XLT and test suites, you can skip this chapter.

* Git clone this test suite or download an archive.
* Compile it with Ant or simply let your IDE do it (adjust the build path):

	$> cd <testsuite>
	$> ant compile

* Tell your master controller about the test suite:
  * Open the file `<xlt>/config/mastercontroller.properties` in your favorite editor.
  * Make sure that it contains the following line:    
   `com.xceptance.xlt.mastercontroller.testSuitePath = <full path to test suite here>`
* Configure the tests.
* Run it.

Please see the [XLT user manual](http://www.xceptance-loadtest.com/xlt/documentation.html) 
for further instructions on how to work with test suites and configure load tests.

## Configuration

You can find a lot of examples of how to use this test suite in `<testsuite>/config/data`.
These test configurations can be executed against the XLT demo application _Poster Store_ that is part of the XLT distribution.

The file `<testsuite>/config/data/torder.csv` contains the most up-to-date configuration documentation.
Please take a closer look. The following lines are just a quick demonstration of what you can do 
and do not cover all details.

## Get involved

In case you like the test suite, extended it, fixed bugs, or simply did anything useful with it, we would like to hear about it. 
If you feel like sharing, even better. Fork it and send us a pull request. 
Make sure you can share your modifications under the Apache License as well.

* Questions? File a ticket or contact us.
* Ideas? Fork, change, and send us a pull request or file a ticket... or both.
* Bugs? Yes... file a ticket. 

## History

* v0.9: Initial release

## Used Libraries

### Xlt 

* https://lab.xceptance.de/releases/xlt/latest/apidoc/
* License : https://lab.xceptance.de/releases/xlt/latest/license.html

### Beanshell

* http://www.beanshell.org/
* License LGPL: http://www.gnu.org/copyleft/lesser.html

### Snakeyaml
* https://bitbucket.org/asomov/snakeyaml
* License: Apache License V2.0 http://www.apache.org/licenses/



### Apache Commons CSV

* http://commons.apache.org/proper/commons-csv/
* License: Apache License V2.0 http://www.apache.org/licenses/
