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

## Introduction
One is often faced with the requirement to just fire a couple of simple urls to verify the performance of a single feature, to create load to stress environments and monitor certain things, or simply to create load in addition to another load test. 

The testsuite-nocoding delivers an easy way to fire urls defined in a CSV file. Additionally, it can insert data, take over data from one page and insert it into the next request, run validations against returned data, as well as give you a way to extend all of that, because the source is free and licensed under the Apache License V2.0.

## Features
* Reads test definitions from CSV files.
* Supports data validation.
* Allows two modes to define and run tests:
	* DOM mode, that is you can use xpath to verify and get data (HtmlUnit parses the responses into a DOM).
	* Lightweight mode which means no DOM overhead, just plain http and https traffic, and very low resource consumption.
* Cookie handling is automatic.
* Static content can be handled automatically or specified manually.
* Does not disable any XLT features.	

## Requirements

You only need Xceptance LoadTest and of course Java installed. XLT is available free of charge for functional and small scale performance testing. http://www.xceptance-loadtest.com/ If you want to debug or extend the test suite, Ant and Eclipse or any other IDE are helpful.

* Xceptance LoadTest: http://www.xceptance-loadtest.com/
* JDK 7 (6 might still work, but we have not tested that)
* Apache Ant: http://ant.apache.org/
* Eclipse or any other Java IDE

## Installation

This test suite is a standard XLT test suite. If you already know how to work with XLT and test suites, you can skip this chapter.

* Git clone this test suite or download an archive.
* Compile it with Ant or simply let your IDE do it (adjust the build path):
	* cd <testsuite directory>
	* ant compile
* Tell your mastercontroller about the test suite:
	* edit <xlt>/config/mastercontroller.properties
	* com.xceptance.xlt.mastercontroller.testSuitePath = <full path to test suite here>
* Configure the tests.
* Run it.

Please see the XLT user manual for further instructions on how to work with test suites and configure load tests.

## Configuration

You can find a lot of examples of how to use this test suite in <testsuite>/config/data. These test configurations can be executed against the XLT demo application Pebble. Pebble is part of the XLT download.

The file <testsuite>/config/data/tauthor.csv contains the most up-to-date configuration documentation. Please take a closer look. The following lines are just a quick demonstration of what you can do and do not cover all details.

### The Simplest Example

    URL
    http://justhost.org/a
    http://justhost.org/ab
    http://justhost.org/aksdfsa?jhsadjfha

### A More Sophisticated Example

    # The header. This is for TURL, because it uses XPath.
    Name, URL, Method, Parameters, ResponseCode, XPath, Text, Encoded, xpath1, xpath2

    # Just load the homepage
    Homepage, "${host}/pebble/",GET,,200,id('blogName'),"Pebble Test Suite",false,,

    # Just log on
    Login,"${host}/pebble/j_acegi_security_check",POST,"redirectUrl=%2F&j_username=username&j_password=password",200,"id('sidebar')/div[@class='sidebarItem']/div[@class='sidebarItemTitle']/span","Logged in as username",true,,

    # Goto to new article creation, verify existence of xpath, content is not needed, fill xpath1 and xpath2 with data for later use
    NewArticle,"${host}/pebble/addBlogEntry.secureaction#form",GET,,200,"id('content')/div[@class='contentItem unpublished']",,false,//input[@name='entry']/@value,//input[@name='date']/@value


### An Example with Static Content

    Type, Name, URL, Method, Parameters, ResponseCode, RegExp, Text, Encoded
     
    # Just load the homepage including static content
    A, Homepage, "${host}/pebble/", GET,, 200, "&l;div id=""blogName"" &gt;&lt;span>(.*?)&lt;/span>&lt;/div&gt;", "Pebble Test Suite", false
    S,,${host}/pebble/themes/xceptance-style/screen.css,GET,,200,,,false
    S,,${host}/pebble/themes/_pebble/print.css,GET,,200,,,false
    S,,${host}/pebble/themes/_pebble/handheld.css,GET,,200,,,false
    S,,${host}/pebble/FCKeditor/fckeditor.js,GET,,200,,,false
    S,,${host}/pebble/scripts/pebble.js,GET,,200,,,false

### Comments and Empty Lines

As you can see, you can use empty lines and comments (starting with #) to format your CSV file and make it more readable. If you use a spreadsheet program for editing, comments and empty lines might fail to be loaded or written.

### The Header

You have to specify a header to indicate what the parser can expect from the next lines. Every line has to provide all columns or at least an empty column, otherwise the parser will complain. You can also put spaces after a comma to make things nicer, but make sure that your empty columns are really empty (,,) instead of specifying a space.

### Data

A static test is pretty boring. You might want to vary it. Either by random data or by data you have taken from a previous request, such as form data. This test suite supports data from property files, random data, and data from the GeneralDataProvider of XLT and therefore from another set of files.

#### Random Data

You can put random data into your urls or parameters. Simply use these expressions:

* ${NOW} : Current timestamp in milliseconds.
* ${RANDOM.String(x)} : A random string with length x. Contains [A-Za-z].   
* ${RANDOM.String(x, s)} : A random string with length x. Contains characters from s.   
* ${RANDOM.Number(max)} : Returns an integer between 0 (inclusive) and max (inclusive).   
* ${RANDOM.Number(min, max)} : Returns an integer between min (inclusive) and max (inclusive).   

#### Properties

You can simply say ${name} and this data will be read from the property files of XLT that are currently active during this test. The normal property lookup hierarchy of XLT is being used.

#### Dynamic Data during Execution

You can grab data from the result of a request by using the headers xpath1 to xpath10 when using the DOM mode and regexp1 to regexp10 when using the lightweight mode. Please see the provided demo files for examples of how to do that. 

## Planned things

* Support for XHR support requests. 
	* Subrequests work fine, but the header is not send yet.
* Ability to specify a variable name right when you get the data from the request. These are generic names such as xpath1 or regexp2 right now.
* Better indication where an error came up, potentially making the line of the CSV file part of the exception.

## Get involved

In case you like the test suite, extended it, fixed bugs, or simply did anything useful with it, we would like to hear about it. If you feel like sharing, even better. Fork it and send us a pull request. Make sure you can share your modifications under the Apache License as well.

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

## License

The testsuite-nocoding is licensed under the Apache Version 2.0 license. See LICENSE file for the full license text.
