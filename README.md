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

One is often faced with the requirement to just fire a couple of simple URLs 
to verify the performance of a single feature, to create load to stress environments 
and monitor certain things, or simply to create load in addition to another load test. 

_TestSuite-NoCoding_ delivers an easy way to fire URLs defined in a CSV file.
Additionally, it can insert data, take over data from one page and insert it into the next request, 
run validations against returned data, as well as give you a way to extend all of that, 
because the source is free and licensed under the Apache License V2.0.

## Features

* Reads test definitions from CSV files.
* Supports data validation.
* Allows two modes to define and run tests:
  * DOM mode - you can use xpath to verify and retrieve data (HtmlUnit parses the responses into a DOM).
  * Lightweight mode - no DOM overhead, just plain HTTP and HTTPS traffic, and very low resource consumption.
* Cookie handling is automatic.
* Static content can be handled automatically or specified manually.
* Does not disable any XLT features.

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
```shell
  $> cd <testsuite>
  $> ant compile
```
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

### The Simplest Example

```
URL
http://justhost.org/a
http://justhost.org/ab
http://justhost.org/aksdfsa?jhsadjfha
```

### A More Sophisticated Example

```
# The header. This is for TURL, because it uses XPath.
Type, Name, URL, Method, Parameters, ResponseCode, XPath, Text, Encoded

# Just load the homepage
A,Homepage,"${host}/posters/",GET,,200,id('titleIndex'),"Check out our new panorama posters!",false

# Select a category
A,SelectCategory,"${host}/posters/category/Main%20Dishes?categoryId=5",GET,,200,id('titleCategoryName'),"Main Dishes",false

# View the product in detail
A,ProductDetailView,"${host}/posters/productDetail/XXL%20Burger?productId=48",GET,,200,id('titleProductName'),"XXL Burger",false

# Configure the product (select finish and size)
XA,ConfigureProduct,"${host}/posters/updatePrice",POST,size=32 x 24 in&productId=48,200,,,false

# Add the configured product to the cart
XA,AddToCart,"${host}/posters/addToCartSlider?productId=48&finish=gloss&size=32%20x%2024%20in",GET,,200,,,false

# View the cart
A,ViewCart,"${host}/posters/cart",GET,,200,id('titleCart'),"Cart",false

# Proceed to checkout
A,Checkout,"${hostsec}/posters/checkout",GET,,200,id('titleDelAddr'),"Select or enter a shipping address",false

# Fill out and submit shipping address form
A,COShipping,"${hostsec}/posters/shippingAddressCompleted",POST,fullName=${DATA.getFirstName(false)} ${DATA.getLastName(false)}&company=Acme Inc&addressLine=123 Rocky Rd&city=${DATA.getTown(false)}&state=California&zip=${RANDOM.Number(5)}&country=United States&billEqualShipp=Yes&btnAddDelAddr=,200,id('titlePayment'),"Select or enter a payment method",false

# Fill out and submit payment form
A,COBilling,"${hostsec}/posters/paymentMethodCompleted",POST,creditCardNumber=4111111111111111&name=Dagobert Duck&expirationDateMonth=01&expirationDateYear=2017&btnAddPayment=,200,id('titleOrderOverview'),"Order Overview",false

# Finally, place the order
A,PlaceOrder,"${hostsec}/posters/checkoutCompleted",POST,btnOrder=,200,"//script[not(@src) and contains(text(),'""Thank you for shopping with us!""')]",,false

```

### An Example with Static Content

```
Type, Name, URL, Method, Parameters, ResponseCode, XPath, Text, Encoded

# Just load the homepage including static content.
A,Homepage,"${host}/posters/",GET,,200,id('titleIndex'),"Check out our new panorama posters!",false
	S,,${host}/posters/assets/css/bootstrap-2.3.1.min.css,GET,,200,,,false
	S,,${host}/posters/assets/css/bootstrap-responsive-2.0.4.css,GET,,200,,,false
	S,,${host}/posters/assets/js/jquery-1.9.1.min.js,GET,,200,,,false
	S,,${host}/posters/assets/css/cartSlider.css,GET,,200,,,false
	S,,${host}/posters/assets/js/bootstrap-paginator-0.5.js,GET,,200,,,false
	S,,${host}/posters/assets/js/custom.js,GET,,200,,,false
	S,,${host}/posters/assets/js/customCartSlider.js,GET,,200,,,false
	S,,${host}/posters/assets/js/bootstrap-2.3.1.min.js,GET,,200,,,false
	S,,${host}/posters/assets/img/products/XXL/XXL_1.jpg,GET,,200,,,false
	S,,${host}/posters/assets/img/xceptanceLogo.png,GET,,200,,,false
	S,,${host}/posters/assets/img/products/XXL/XXL_2.jpg,GET,,200,,,false
	S,,${host}/posters/assets/img/products/XXL/XXL_3.jpg,GET,,200,,,false
	S,,${host}/posters/assets/img/products/XXL/XXL_4.jpg,GET,,200,,,false
	S,,${host}/posters/assets/img/cartSliderFooter.png,GET,,200,,,false
	S,,${host}/posters/assets/img/glyphicons-halflings.png,GET,,200,,,false
```

### Comments and Empty Lines

As you can see, you can use empty lines and comments (starting with \#) to format your CSV file and make it more readable.
If you use a spreadsheet program for editing, comments and empty lines might fail to be loaded or written.

### The Header

You have to specify a header to indicate what the parser can expect from the next lines.
Every line has to provide all columns or at least an empty column, otherwise the parser will complain. 
You can also put spaces after a comma to make things nicer, but make sure that your empty columns are really empty \(,,\) instead of specifying a space.

### Data

A static test is pretty boring. You might want to vary it. Either by random data or 
by data you have taken from a previous request, such as form data. 
This test suite supports data from property files, random data, and data from the `GeneralDataProvider` 
of XLT and therefore from another set of files.

#### Random Data

You can put random data into your URLs or parameters. Simply use these expressions:

* `${NOW}` : Current timestamp in milliseconds.
* `${RANDOM.String(x)}` : A random string with length `x`. Contains [A-Za-z].   
* `${RANDOM.String(x, s)}` : A random string with length `x`. Contains characters from `s`.   
* `${RANDOM.Number(max)}` : Returns an integer between 0 (inclusive) and `max` (inclusive).   
* `${RANDOM.Number(min, max)}` : Returns an integer between `min` (inclusive) and `max` (inclusive).   

#### Properties

You can simply say `${name}` and this data will be read from the property files of XLT 
that are currently active during this test. The normal property lookup hierarchy of XLT is being used.

#### Dynamic Data during Execution

You can grab data from the result of a request by using the headers `xpath1` to `xpath10` 
when using the DOM mode and `regexp1` to `regexp10` when using the lightweight mode. 

Please see the provided demo files for examples of how to do that. 

## Planned things

* Load static content via XHR.
* Ability to specify a variable name right when you get the data from the request. These are generic names such as `xpath1` or `regexp2` right now.
* Better indication where an error came up, potentially making the line of the CSV file part of the exception.

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

## License

The testsuite-nocoding is licensed under the Apache Version 2.0 license. See __LICENSE__ file for the full license text.
