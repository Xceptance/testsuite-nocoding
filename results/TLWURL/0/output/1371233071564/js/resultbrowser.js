(function($){

    function startProgressMeter()
    {
        $('#progressmeter').show();
        $('body').css("cursor","wait");
    }

    function stopProgressMeter()
    {
        $('#progressmeter').hide();
        $('body').css("cursor","");
    }

    function htmlEncode(value)
    {
        return $('<div/>').text(value).html();
    }

    function showAction(element)
    {
        // only show this action if not shown yet
        if (!$(element).hasClass("active"))
        {
            // switch active state of navigation
            $("#actionlist .active").removeClass("active");
            $(element).addClass("active");

            // hide request content
            $('#requestcontent').hide();

            // update and show action content iframe
            var data = $(element).data("json");
            $('#actioncontent').attr('src', data.fileName);
            $('#actioncontent').show();
        }
    }

    function expandCollapseAction(element)
    {
        // lazily create the requests
        if ($('ul.requests', element).length == 0)
        {
            createRequestsForAction(element);
        }

        // show/hide the requests
        $('ul.requests', element).slideToggle(200);

        // show/hide the requests
        $('.expander', element).toggleClass("expanded");
    }

    function createRequestsForAction(actionElement)
    {
        // build requests element
        var requests = $('<ul class="requests"></ul>');
        $(actionElement).append(requests);

        // make sure, we do not see it building up
        $(requests).hide();

        // ok, we have to add the data from the json object to it
        var action = $(actionElement).data('json');
        for ( var i in action.requests)
        {
            var request = action.requests[i];
            var name = request.name;
            var requestClass = determineClass(request.mimeType, request.responseCode);
            var title = "[" + request.responseCode + "] " + request.url;

            var requestElement =
                                 $('<li class="request" title="' + htmlEncode(title) + '"><span class="name ' + htmlEncode(requestClass) + '">' + htmlEncode(name)
                                         + '</span></li>');

            // store the json object for later
            $(requestElement).data("json", request);

            // setup onclick to show request content
            $(requestElement).click( function(event)
            {
                showRequest(this);
                event.stopPropagation();
            });

            // setup ondblclick to do nothing
            $(requestElement).dblclick( function(event)
            {
                event.stopPropagation();
            });

            // insert into DOM
            $(requests).append(requestElement);
        }
    }

    function determineClass(mimeType, responseCode)
    {
        if (responseCode >= 400 || responseCode == 0)
        {
            return "httpError";
        }
        if (responseCode == 301 || responseCode == 302)
        {
            return "httpRedirect";
        }
        if (mimeType.indexOf("image/") == 0)
        {
            return "contentTypeImage";
        }
        if (mimeType == "text/css")
        {
            return "contentTypeCSS";
        }
        if (mimeType.indexOf("javascript") >= 0 || mimeType == "application/json")
        {
            return "contentTypeJS";
        }
        return "";
    }

    function populateKeyValueTable(table, keyValueArray)
    {
        $(table).empty();

        if (keyValueArray.length == 0)
        {
            var tableRow = $('<tr><td class="empty" colspan="2">None.</td></tr>');
            table.append(tableRow);
        }
        else
        {
            for ( var i in keyValueArray)
            {
                var name = htmlEncode(keyValueArray[i].name_);
                var value = keyValueArray[i].value_;

                if (name.toLowerCase() == 'cookie')
                {
                    // format cookies - one per line
                    var cookies = value.split(";");
                    
                    value = "";
                    for ( var j in cookies)
                    {
                        value = value + htmlEncode(cookies[j]) + '<br/>';
                    }
                }
                else
                {
                    value = htmlEncode(value);
                }

                var tableRow = $('<tr><td class="key">' + name + '</td><td class="value">' + value + '</td></tr>');
                table.append(tableRow);
            }
        }

        return table;
    }

    function activateTab(element)
    {
        // switch active tab header
        $('#requestcontent .selected').removeClass('selected');
        $(element).addClass('selected');

        // switch active tab panel
        $('#requestcontent > div').hide();
        var index = $('#requestcontent li').index(element);
        var tab = $('#requestcontent > div').get(index);
        $(tab).show();
    }

    function showRequest(element)
    {
        // only show this request if not shown yet
        if (!$(element).hasClass("active"))
        {
            // switch active state of navigation
            $("#actionlist .active").removeClass("active");
            $(element).addClass("active");

            // hide action content
            $('#actioncontent').hide();
            $('#errorMessage').hide();

            // retrieve the request data
            var requestData = $(element).data("json");

            // update content view tab based on the mime type
            if (requestData.mimeType.indexOf('image/') == 0)
            {
                // update the image
                $('#requestimage').attr('src', requestData.fileName);

                $('#requestimage').show();
                $('#requesttext').hide();
            }
            else
            {

                $('#requestimage').hide();

                // check for redirect (response file is empty and will cause an error when trying to be read in)
                if(/30[0-8]/.test(requestData.responseCode))
                {
                    $('#requesttext').text('').show();
                }
                else
                {
                    // update the text, load it from file
                    $.ajax({
                        url : requestData.fileName,
                        dataType : 'text',
                        success : function(data)
                        {
                            $('#requesttext').text(data).show();
                        },
                        error : function(xhr,textStatus,errorThrown)
                        {
                            $('#requesttext').hide();
                            $('#errorMessage .filename').text(requestData.fileName);
                            $('#errorMessage').show();
                            centerErrorMessage();
                        }
                    });
                }
            }

            // update the request information tab
            $("#url").empty().append($('<a>').attr('href', requestData.url).text(requestData.url));
            $("#requestmethod").text(requestData.requestMethod);
            
            $("#time-start").text(formatDate(new Date(requestData.startTime)));
            $("#time-connect").text(requestData.connectTime + " ms");
            $("#time-send").text(requestData.sendTime + " ms");
            $("#time-serverbusy").text(requestData.serverBusyTime + " ms");
            $("#time-receive").text(requestData.receiveTime + " ms");
            $("#time-tofirst").text(requestData.timeToFirst + " ms");
            $("#time-tolast").text(requestData.timeToLast + " ms");
            $("#time-end").text(formatDate(new Date(requestData.endTime)));
            
            populateKeyValueTable($("#requestparameters"), requestData.requestParameters);
            populateKeyValueTable($("#requestheaders"), requestData.requestHeaders);

            // update the request content tab
            var reqBody = requestData.requestBodyRaw;
            if (reqBody != undefined)
            {
                $("#requestbody").text(reqBody);
            }
            else
            {
                $("#requestbody").text("");
            }

            // update the response information tab
            $("#status").text(requestData.status);
            $("#loadtime").text(requestData.loadTime + " ms");
            populateKeyValueTable($("#responseheaders"), requestData.responseHeaders);

            // finally show the request content
            $('#requestcontent').show();
        }
    }
    
    function formatDate(date)
    {
        var d = moment(date);
        var result = d.format();
        result = result.replace("T", " ");
        result = result.replace(/\+.*/, ("."+d.format("SSS")+" GMT"+d.format("ZZ")));
        return result;
    }
    
    function centerErrorMessage()
    {
        var content = $('#content'),
            errMsg = $('#errorMessage'),
            height = Math.floor(0.333 * content.height()),
            width = content.width() - 700;
        errMsg.css({ position: 'absolute', left: width/2 + 'px', top: height/2 + 'px' });
    }

    /*
     * Resize the action content area
     */
    function resizeContent()
    {
        // get the current size of the main content area
        var width = 800;
        var height = 800;

        // get the current viewport size
        if (typeof window.innerWidth != 'undefined')
        {
            // the more standards compliant browsers (mozilla/netscape/opera/IE7)
            width = window.innerWidth;
            height = window.innerHeight;
        }
        else if (typeof document.documentElement != 'undefined' && typeof document.documentElement.clientWidth != 'undefined'
                && document.documentElement.clientWidth != 0)
        {
            // IE6 in standards compliant mode
            width = document.documentElement.clientWidth;
            height = document.documentElement.clientHeight;
        }

        // resize the content area
        var contentElement = $('#content');

        var leftPosition = contentElement.css('left').replace(/px/, '');
        var topPosition = contentElement.css('top').replace(/px/, '');

        var newWidth = width - leftPosition - 2;
        var newHeight = height - topPosition;

        contentElement.width(newWidth);
        contentElement.height(newHeight);
        
        // resize the split bar height
        var contentElement = $('.vsplitbar');

        contentElement.height(newHeight);

        // resize the height of the navigation area as well
        var navigationElement = $('#navigation');
        var headerElement = $('#header');
        navigationElement.height(newHeight - headerElement.height());
        centerErrorMessage();
    }

    function loadJSON()
    {
        // get the json data from the external file
        var transaction = jsonData;

        document.title = transaction.user + " - XLT Result Browser";

        var actions = $('<ul class="actions"></ul>');
        $(actions).hide();

        for ( var i in transaction.actions)
        {
            var action = transaction.actions[i];
            var actionElement = $('<li class="action" title="Double-click to show/hide request details."><span class="expander"/><span class="name">' + htmlEncode(action.name) + '</span></li>');

            // store the json object for later
            $(actionElement).data("json", action);

            // setup onclick to show action content
            $(actionElement).click( function(event)
            {
                showAction(this);
            });

            // setup click to show/hide requests
            $('.expander', actionElement).click( function(event)
            {
                expandCollapseAction(this.parentNode);
            });

            // setup ondblclick to show/hide requests
            $(actionElement).dblclick( function(event)
            {
                expandCollapseAction(this);
            });

            // insert into DOM
            $(actions).append(actionElement);
        }

        // insert the actions into the DOM
        $('#actionlist').append(actions);

        // show them
        $(actions).slideDown(200);
    }
    
    // insert the (vertical) split bar on page
    function makeSplitter()
    {
    	$("#container").splitter({
    		type: "v",
     		sizeLeft: 250
    	});
    }

    // the on load setup
    $(document).ready( function()
    {
        startProgressMeter();

        loadJSON();

        // take care of the size of the content display area to
        // adjust it to the window size
        $(window).bind("resize", function(event)
        {
            resizeContent();
        });

        // setup onclick for the tabbed panel in the request content
        // area
        $('#requestcontent .tabs-nav li').click( function(event)
        {
            activateTab(this);
        });

        // page splitter
        makeSplitter();        
        
        // resize in the beginning already
        resizeContent();

        // open the first action
        $('#actionlist li.action:nth-child(1)').click();

        stopProgressMeter();
    });

})(jQuery);
