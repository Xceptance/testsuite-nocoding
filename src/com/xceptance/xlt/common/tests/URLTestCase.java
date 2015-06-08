package com.xceptance.xlt.common.tests;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.common.util.URLActionData;
import com.xceptance.xlt.common.util.URLActionDataExecutable;

public class URLTestCase extends AbstractURLTestCase
{
    private URLActionDataExecutable previousExecutable;

    private URLActionData previousAction;

    @Test
    public void testURLs()
    {
        if (!actions.isEmpty())
        {
            handleFirstAction();

            for (final URLActionData action : actions)
            {
                action.outline();
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
        final URLActionDataExecutable executable = createExecutableFromAction(action, request);
        setPreviousExecutable(executable);
        setPreviousURLAction(action);
        removeActionFromActionList(action);
        action.outline();
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
        previousAction = action;
    }

    private URLActionData getFirstURLActionToExecute()
    {
        return actions.get(0);
    }

    private void checkIfFirstActionIsExecutable(final URLActionData firstAction)
    {
        if (!firstAction.isAction())
        {
            throw new IllegalArgumentException("");
        }
    }

    private WebRequest createActionWebRequest(final URLActionData action)
    {
        return requestBuilder.buildRequest(action);
    }

    private void handleAction(final URLActionData action)
    {
        final WebRequest request = createActionWebRequest(action);
        final URLActionDataExecutable executable = createExecutableFromAction(action, request);
        executePreviousExecutable();
        handleResponse();
        setPreviousExecutable(executable);
    }

    private void handleResponse()
    {
        responseHandler.handleURLActionResponse(previousAction,
                                                previousExecutable.getResult());
    }

    private void executePreviousExecutable()
    {
        previousExecutable.executeURLAction();
    }

    private URLActionDataExecutable createExecutableFromAction(final URLActionData action,
                                                       final WebRequest request)
    {
        return executableFactory.createPageAction(action.getName(), request);
    }

    private void handleXhrAction(final URLActionData xhrAction)
    {
        final WebRequest request = createXhrWebRequest(xhrAction);
        final URLActionDataExecutable executable = createExecutableFromXhr(xhrAction, request);
        executePreviousExecutable();
        handleResponse();
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
