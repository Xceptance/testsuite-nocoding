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
* Supports http response validation.
* Allows two modes to define and run tests:
  * DOM mode : HtmlUnit parses the responses into a DOM
  * Lightweight mode - no DOM overhead, just plain HTTP and HTTPS traffic, and very low resource consumption.
* Cookie handling is automatic.
* Static content can be handled automatically or specified manually.
* Does not disable any XLT features.

##  Issues
Problem  | Status
------------- | -------------
The WebClient must be configurated with properties (js, static content, credentials ...)  | open
Data from the files should overwrite the properties | open
HtmlAttributes and Textfields must be validateable | done
Implement the CSV file Loader | open
Parameter encoding is messy | open
Rework log system | open
Rework exception handling (creation, execution, validation) | open
Rework class naming | open
Document automated header setting | open
Rework "Matches" Validation mode | open
Firebug Xpath problem | open
find out about getEffectiveKey | open
documentatio about supported filetypes | open
support more http methods |open
testcase mapping | open
support encoding type | open

## Planed
Item  | Status
------------- | -------------
Support multible request encoding types   | open
documentation | planed
Expand ParameterInterpreter with js support | open
Implement recursive ParamterInterpreter | open
Expand DataProvider with a set of valid data multibles | open
CLassed Overview | open


## Documentation, Error Handling, Logging

Class | Docu| Error Handling | Logging
----|----|----|----
URLActionData | -[x] | -[x] | -[x]




## Requirements

You only need [Xceptance LoadTest](http://www.xceptance-loadtest.com/) and, of course, Java installed. 
XLT&reg; is available free of charge for functional and small scale performance testing.
If you want to debug or extend the test suite, Ant and Eclipse or any other IDE are helpful.

* Xceptance LoadTest: http://www.xceptance-loadtest.com/
* JDK 7 (6 might still work, but we have not tested that)
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

### Beanshell

* http://www.beanshell.org/
* License LGPL: http://www.gnu.org/copyleft/lesser.html

### Apache Commons CSV

* http://commons.apache.org/proper/commons-csv/
* License: Apache License V2.0 http://www.apache.org/licenses/
