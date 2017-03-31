package com.xceptance.xlt.common.tests;

import java.util.ArrayList;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.actions.Downloader;
import com.xceptance.xlt.common.util.action.data.URLActionData;
import com.xceptance.xlt.common.util.action.execution.URLActionDataExecutionable;
import com.xceptance.xlt.common.util.action.validation.URLActionDataResponseHandler;

/**
 * Here, the important stuff is done:
 * <ul>
 * <li>for every {@link URLActionData} object from the {@link #actions} List, that was prepared in
 * {@link AbstractURLTestCase @Before}:
 * <ul>
 * <li>create a {@link WebRequest} from the {@link URLActionData}, depending on the type of request.
 * <li>execute the {@link WebRequest} via {@link URLActionDataExecutionable}.
 * <li>receive the result {@link URLActionDataExecutionableResult}
 * <li>handle and validate the {@link URLActionDataExecutionableResult} via {@link URLActionDataResponseHandler}.
 * </ul>
 * <li>static requests are treated differently. They go to the {@link Downloader} and cannot be validated. Therefore
 * before executing an action, we have to take a look onto the next actions in the list to see whether they are static
 * requests and must be added to the Downloader.
 * </ul>
 * 
 * @author matthias mitterreiter
 */
public class URLTestCase extends AbstractURLTestCase
{
    protected URLActionDataExecutionable previousExecutable;

    protected URLActionData previousActionData;
    
	private ArrayList<URLActionData> xhrActionList;
    
    private ArrayList <WebRequest> requestList;

    /**
     * The first and the last action are treated differently: <br>
     * The first action cannot be a static request or a XmlHttpRequest. <br>
     * The last action must be executed and validated, but no additional URLActionData is loaded. <br>
     */
    @Test
    public void testURLs()
    {
        if (!actions.isEmpty())
        {
            handleFirstAction();

            for (final URLActionData action : actions)
            {
                if (action.isAction())
                {
                	handleLastAction();
                	xhrActionList = new ArrayList <URLActionData>();
                	requestList = new ArrayList<WebRequest>();
                    handleAction(action);
                }
                else if (action.isXHRAction())
                {
                    handleXhrAction(action);
                }
                else if (action.isStaticContent())
                {
                    handleStaticAction(action);
                }
            }
            handleLastAction();
        }
        else
        {
            XltLogger.runTimeLogger.info("Did not do anything, because there was no URLActionData objects available!");
        }
    }

    protected void handleFirstAction()
    {
    	xhrActionList = new ArrayList <URLActionData>();
    	requestList = new ArrayList<WebRequest>();
        final URLActionData action = getFirstURLActionToExecute();
        checkIfFirstActionIsExecutable(action);
        final WebRequest request = createActionWebRequest(action);
        final URLActionDataExecutionable executable = createExecutionableFromAction(action, request);
        setPreviousExecutionable(executable);
        setPreviousURLAction(action);
        removeActionFromActionList(action);
    }

    protected void handleLastAction()
    {
        executePreviousExecutionable();
        handleStore();
    }

    protected void removeActionFromActionList(final URLActionData action)
    {
        actions.remove(action);
    }

    protected void setPreviousExecutionable(final URLActionDataExecutionable executable)
    {
        previousExecutable = executable;
    }

    protected void setPreviousURLAction(final URLActionData action)
    {
        previousActionData = action;
    }

    protected URLActionData getFirstURLActionToExecute()
    {
        return actions.get(0);
    }

    protected void checkIfFirstActionIsExecutable(final URLActionData firstAction)
    {
        if (!firstAction.isAction())
        {
            throw new IllegalArgumentException("First Request cannot be of type xhr | static");
        }
    }

    protected WebRequest createActionWebRequest(final URLActionData action)
    {
        return requestBuilder.buildRequest(action);
    }

    protected void handleAction(final URLActionData action)
    {
        final WebRequest request = createActionWebRequest(action);
        final URLActionDataExecutionable executable = createExecutionableFromAction(action, request);
        setPreviousURLAction(action);
        setPreviousExecutionable(executable);
    }

    protected void handleStore()
    {
        responseHandler.handleStore(previousActionData, previousExecutable.getResult());
    }

    protected void executePreviousExecutionable()
    {
        previousExecutable.executeAction(this);
    }

    protected URLActionDataExecutionable createExecutionableFromAction(final URLActionData action, final WebRequest request)
    {
        return executionableFactory.createPageAction(action.getName(), request);
    }

    protected void handleXhrAction(final URLActionData xhrAction)
    {
        final WebRequest request = createXhrWebRequest(xhrAction);
        requestList.add(request);
        xhrActionList.add(xhrAction);
    }

    protected WebRequest createXhrWebRequest(final URLActionData xhrAction)
    {
        return requestBuilder.buildXhrRequest(xhrAction, previousExecutable.getUrl());
    }

    protected URLActionDataExecutionable createExecutionableFromXhr(final URLActionData action, final WebRequest request)
    {
        return executionableFactory.createXhrPageAction(action.getName(), request);
    }

    protected void handleStaticAction(final URLActionData staticAction)
    {
        previousExecutable.addStaticRequest(staticAction.getUrl());
    }
    
    
    //----------------------------------------------------------------
    //Getters
    
    public ArrayList <WebRequest> getRequestList()
    {
    	return requestList;
    }
    
    public URLActionData getPreviousActionData()
    {
    	return previousActionData;
    }
    
    public ArrayList <URLActionData> getXhrActionList()
    {
    	return xhrActionList;
    }
    
    public URLActionDataResponseHandler getReponseHandler()
    {
    	return responseHandler;
    }

}
