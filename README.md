#  TestSuite-NoCoding

This is a test suite for Xceptance LoadTest (XLT) - http://www.xceptance-loadtest.com/.

* Name: testsuite-nocoding
* Version: 2.0
* Release: July 2015
* License: Apache V2.0
* License URI: http://www.apache.org/licenses/LICENSE-2.0.txt
* Tags: load testing, performance testing, testing
* Contributors:
 * Hartmut Arlt, Xceptance Software Technologies GmbH
 * René Schwietzke, Xceptance Software Technologies GmbH
 * Jörg Werner, Xceptance Software Technologies GmbH
 * Matthias Mitterreiter, Xceptance Software Technologies GmbH


***

### Introduction

One is often faced with the requirement to just fire a couple of simple URLs, to verify the performance or functionality of a single feature, to create load in order to stress environments and monitor certain things, or simply to create load in addition to another load test.

TestSuite-NoCoding  is a test-suite for XLT and delivers an easy way to define and fire HTTP requests and offers various tools to validate the responses.
Additionally it is possible to filter, select and store data from the response with the view of inserting it into the next request or its validation. This is a fast and likewise easy way to define test cases, since **no programming** is needed!

Since the source is open and licensed under the Apache License V2.0, feel free to extend and customize it. 

***

#### [Getting Started](https://github.com/Xceptance/testsuite-nocoding/wiki)

***

### Features

* Reads test definitions from files.
  Supported file types :
  * YAML
  * CSV
* Supports the validation of:
  * HTTP response headers.
  * Cookies
  *  HTTP Response Content. 
* Offers various validation modes in order to validate data.
* Supports two modes to run the test-cases:
  * DOM : the response are parsed into the DOM, which allows to select elements by xpath for validation purpose
  * LIGHT : there exsists no DOM, which makes the test case fast to execute.
* Cookie handling is automatic
* JavaScript and Static Content can be handled automatically and also be switched off.
* None of the XLT features are disabled.
* ...

### Getting Started

To learn more, try one of the following links.

* [Overview](https://github.com/Xceptance/testsuite-nocoding/wiki): In order to get an overview.
* [Quickstart](https://github.com/Xceptance/testsuite-nocoding/wiki/Quickstart) : To get started.
* [Examples](https://github.com/Xceptance/testsuite-nocoding/wiki/Examples): To see some examples.


 


