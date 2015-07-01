package com.xceptance.xlt.common.tests;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.actions.Downloader;
import com.xceptance.xlt.common.util.action.data.URLActionData;
import com.xceptance.xlt.common.util.action.execution.URLActionDataExecutionable;
import com.xceptance.xlt.common.util.action.validation.URLActionDataResponseHandler;

/**
 * Here, the important stuff is done:
 * <ul>
 * <li>for every {@link URLActionData} object from the {@link #actions} List, 
 * that was prepared in {@link AbstractURLTestCase @Before}:
 * <ul>
 * <li>create a {@link WebRequest} from the {@link URLActionData}, depending on the type of request.
 * <li>execute the {@link WebRequest} via {@link URLActionDataExecutionable}.
 * <li>receive the result {@link URLActionDataExecutionableResult}
 * <li>handle and validate the {@link URLActionDataExecutionableResult} via {@link URLActionDataResponseHandler}.
 * </ul>
 * <li>static requests are treated differently. They go to the {@link Downloader} and cannot be validated.
 * Therefore before executing an action, we have to take a look onto the next actions in the list to see whether they are 
 * static requests and must be added to the Downloader.
 * </ul>
 * 
 * @author matthias mitterreiter
 */
public class URLTestCase extends AbstractURLTestCase
{
    private URLActionDataExecutionable previousExecutable;

    private URLActionData previousActionData;

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
                action.outline();
            }
            handleLastAction();
            System.err.println(Session.getCurrent().getUserName());
        }
        else
        {
            XltLogger.runTimeLogger.info("Did not do anything, because there was no URLActionData objects available!");
        }
    }

    private void handleFirstAction()
    {
        final URLActionData action = getFirstURLActionToExecute();
        checkIfFirstActionIsExecutable(action);
        final WebRequest request = createActionWebRequest(action);
        final URLActionDataExecutionable executable = createExecutionableFromAction(action,
                                                                                 request);
        setPreviousExecutionable(executable);
        setPreviousURLAction(action);
        removeActionFromActionList(action);
        action.outline();
    }

    private void handleLastAction()
    {
        executePreviousExecutionable();
        handleResponse();
    }

    private void removeActionFromActionList(final URLActionData action)
    {
        actions.remove(action);
    }

    private void setPreviousExecutionable(final URLActionDataExecutionable executable)
    {
        previousExecutable = executable;
    }

    private void setPreviousURLAction(final URLActionData action)
    {
        previousActionData = action;
    }

    private URLActionData getFirstURLActionToExecute()
    {
        return actions.get(0);
    }

    private void checkIfFirstActionIsExecutable(final URLActionData firstAction)
    {
        if (!firstAction.isAction())
        {
            throw new IllegalArgumentException("First Request cannot be of type xhr | static");
        }
    }

    private WebRequest createActionWebRequest(final URLActionData action)
    {
        return requestBuilder.buildRequest(action);
    }
    
    private void handleAction(final URLActionData action)
    {
        executePreviousExecutionable();
        handleResponse();

        final WebRequest request = createActionWebRequest(action);
        final URLActionDataExecutionable executable = createExecutionableFromAction(action,
                                                                                 request);
        setPreviousURLAction(action);
        setPreviousExecutionable(executable);
    }

    private void handleResponse()
    {
        responseHandler.handleURLActionResponse(previousActionData,
                                                previousExecutable.getResult());
    }

    private void executePreviousExecutionable()
    {
        previousExecutable.executeAction();
    }

    private URLActionDataExecutionable createExecutionableFromAction(final URLActionData action,
                                                                  final WebRequest request)
    {
        return executionableFactory.createPageAction(action.getName(), request);
    }

    private void handleXhrAction(final URLActionData xhrAction)
    {
        final WebRequest request = createXhrWebRequest(xhrAction);
        final URLActionDataExecutionable executable = createExecutionableFromXhr(xhrAction,
                                                                              request);
        executePreviousExecutionable();
        handleResponse();
        setPreviousURLAction(xhrAction);
        setPreviousExecutionable(executable);
    }

    private WebRequest createXhrWebRequest(final URLActionData xhrAction)
    {
        return requestBuilder.buildXhrRequest(xhrAction,
                                              previousExecutable.getUrl());
    }

    private URLActionDataExecutionable createExecutionableFromXhr(final URLActionData action,
                                                               final WebRequest request)
    {
        return executionableFactory.createXhrPageAction(action.getName(),
                                                        request);
    }

    private void handleStaticAction(final URLActionData staticAction)
    {
        previousExecutable.addStaticRequest(staticAction.getUrl());
    }

}
