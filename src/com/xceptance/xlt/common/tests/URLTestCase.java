package com.xceptance.xlt.common.tests;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.common.util.URLActionData;
import com.xceptance.xlt.common.util.URLActionDataExecutable;

public class URLTestCase extends AbstractURLTestCase
{
    private URLActionDataExecutable previousExecutable;

    private URLActionData previousActionData;

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
            }
            handleLastAction();
        }
        else
        {
            // log
        }
    }

    private void handleFirstAction()
    {
        final URLActionData action = getFirstURLActionToExecute();
        checkIfFirstActionIsExecutable(action);
        final WebRequest request = createActionWebRequest(action);
        final URLActionDataExecutable executable = createExecutableFromAction(action,
                                                                              request);
        setPreviousExecutable(executable);
        setPreviousURLAction(action);
        removeActionFromActionList(action);
        action.outline();
    }

    private void handleLastAction()
    {
        executePreviousExecutable();
        handleResponse();
    }

    private void removeActionFromActionList(final URLActionData action)
    {
        actions.remove(action);
    }

    private void setPreviousExecutable(final URLActionDataExecutable executable)
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
        final WebRequest request = createActionWebRequest(action);
        final URLActionDataExecutable executable = createExecutableFromAction(action,
                                                                              request);
        executePreviousExecutable();
        handleResponse();
        setPreviousURLAction(action);
        setPreviousExecutable(executable);
    }

    private void handleResponse()
    {
        responseHandler.handleURLActionResponse(previousActionData,
                                                previousExecutable.getResult());
    }

    private void executePreviousExecutable()
    {
        previousExecutable.executeAction();
    }

    private URLActionDataExecutable createExecutableFromAction(final URLActionData action,
                                                               final WebRequest request)
    {
        return executableFactory.createPageAction(action.getName(), request);
    }

    private void handleXhrAction(final URLActionData xhrAction)
    {
        final WebRequest request = createXhrWebRequest(xhrAction);
        final URLActionDataExecutable executable = createExecutableFromXhr(xhrAction,
                                                                           request);
        executePreviousExecutable();
        handleResponse();
        setPreviousURLAction(xhrAction);
        setPreviousExecutable(executable);
    }

    private WebRequest createXhrWebRequest(final URLActionData xhrAction)
    {
        return requestBuilder.buildXhrRequest(xhrAction, previousExecutable.getUrl());
    }

    private URLActionDataExecutable createExecutableFromXhr(final URLActionData action,
                                                            final WebRequest request)
    {
        return executableFactory.createXhrPageAction(action.getName(), request);
    }

    private void handleStaticAction(final URLActionData staticAction)
    {
        previousExecutable.addStaticRequest(staticAction.getUrl());
    }

}
