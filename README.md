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

The following is an unordered list of the most hurting issues and should be resolved fast in order to ensure the functionality of the test-suite.

Issue  |  Type | Status
------------- | :-------------: | :-------------:
The WebClient must be configurated with properties (js, static content, credentials ...).  | :question: | :x:
HtmlAttributes and Textfields are not validatable. | :beetle: | :white_check_mark:
CSV file Loader does not work. (because not implemented) | :ghost: | :x:
Parameter encoding is messy. | :poop: | :x:
The "Matches" validation mode doesnt work! | :beetle: | :x:
Firebug delivers helpless "XPath"s. Assimilate! | :beetle: | :x: 
Unravel the mystery about "getEffectiveKey()". | :ghost: | :x:
Testcase mapping doesn't work. | :beetle: | :x:
Load multiple files | :ghost: | :x:


#### Symbology

:x: ... open, :white_check_mark: ... resolved.

:question: ... open question or unclear behavior.

:beetle: ... [group of insects which are biologically classified in the order Coleoptera](https://en.wikipedia.org/?title=Beetle). Kill it!

:ghost: ... fictional creature which is somehow there but isnt!. Must be fully implemented or taken out completely!

:poop: ... self-descriptive.

## Planed

The following is an unordered list of futuristic features.

* Dynamic UserAgent adjusting.
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
AbstractURLTestCase | :white_check_mark: |:white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
ConcreteNodeList  | :white_check_mark: | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
CSVBasedURLActionDataListBuilder  |:x: | :x: | :x: | :x: | :x:  
Downloader  |:white_check_mark: | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark:  
HtmlPageAction  | :white_check_mark:| :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
HtmlPageActionFactory  | :white_check_mark:| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark:  
LightWeightPageAction  |:white_check_mark: | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark:   
LightWeightPageActionFactory  | :white_check_mark:| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
MockObjects  |:white_check_mark: | :white_circle: | :white_circle: | :white_circle: | :white_check_mark: 
ModifiedAbstractHtmlPageAction  | :white_check_mark:| :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
ModifiedAbstractLightWeightPageAction  |:white_check_mark: | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
ParameterInterpreter  | :white_check_mark:|:white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
ParameterInterpreterNow  | :white_check_mark:| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
ParameterInterpreterRandom  | :white_check_mark:|:white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
ParameterUtils  |:white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
StaticContentDownloader |:white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
URLActionData |:white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark:
URLActionDataBuilder |:white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
URLActionDataExecutable | :white_check_mark:| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
URLActionDataExecutableFactory | :white_check_mark: |:x: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
URLActionDataExecutableFactoryBuilder | :white_check_mark:|:white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
URLActionDataExecutableResult |:white_check_mark: |:white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
URLActionDataExecutableResultFactory |:white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
URLActionDataListBuilder |:white_check_mark: | :x: | :white_circle: | :white_check_mark: | :white_check_mark: 
URLActionDataListFacade |:white_check_mark: | :white_circle: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
URLActionDataRequestBuilder | :white_circle:| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
URLActionDataResponseHandler |:white_check_mark: | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
URLActionDataStore | :white_check_mark:| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
URLActionDataStoreBuilder |:white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
URLActionDataStoreResponseHandler |:white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
URLActionDataValidationBuilder |:white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
URLActionDataValidationResponseHandler | :white_check_mark:| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark:  
URLActionValidation | :white_check_mark:| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
URLTestCase | :white_check_mark:| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
XhrHtmlPageAction |:white_check_mark: | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
XhrLightWeightPageAction |:white_check_mark: | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
XPathGetable | :white_check_mark:| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark:
XPathWithHtmlPage | :white_check_mark:  |:white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
XPathWithLightWeightPage | :white_check_mark:| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
XPathWithNonParseableWebResponse |:white_check_mark: | :white_circle: | :white_circle: | :white_circle: | :white_circle: 
XPathWithParseableWebResponse | :white_check_mark:| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 
YAMLBasedURLActionDataListBuilder | :white_check_mark:| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: 

#### Symbology

:white_check_mark: ... done.

:white_circle: ... partly done.

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
