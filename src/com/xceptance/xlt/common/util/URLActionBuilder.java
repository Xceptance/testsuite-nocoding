package com.xceptance.xlt.common.util;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionBuilder
{
    private String name;

    private String type;

    private String url;

    private String method;

    private String encoded;

    private String httpResponceCode;

    private String body;

    private List<URLActionValidation> validations = Collections.emptyList();

    private List<URLActionStore> store = Collections.emptyList();

    private List<NameValuePair> parameters = Collections.emptyList();

    private List<NameValuePair> cookies = Collections.emptyList();

    private List<NameValuePair> headers = Collections.emptyList();

    private String d_name;

    private String d_type;

    private String d_url;

    private String d_method;

    private String d_encoded;

    private String d_httpResponceCode;

    private String d_body;

    private List<URLActionValidation> d_validations = Collections.emptyList();

    private List<URLActionStore> d_store = Collections.emptyList();

    private List<NameValuePair> d_parameters = Collections.emptyList();

    private List<NameValuePair> d_cookies = Collections.emptyList();

    private List<NameValuePair> d_headers = Collections.emptyList();

    private ParameterInterpreter interpreter;

    public void outline(){
        System.err.println("UrlActionBuilder");
        if(this.name != null) System.err.println("Name : " + this.name);
        if(this.d_name != null) System.err.println("D_Name : " + this.d_name);
        if(this.type != null) System.err.println("Type : " + this.type);
        if(this.d_type != null) System.err.println("D_Type : " + this.d_type);
        if(this.url != null) System.err.println("Url : " + this.url);
        if(this.d_url != null) System.err.println("D_Url : " + this.d_url);
        if(this.method != null) System.err.println("Method : " + this.method);
        if(this.d_method != null) System.err.println("D_Method : " + this.d_method);
        if(this.httpResponceCode != null) System.err.println("HttpCode : " + this.httpResponceCode);
        if(this.d_httpResponceCode != null) System.err.println("D_HttpCode : " + this.d_httpResponceCode);
        if(this.body != null) System.err.println("Body : " + this.body);
        if(this.d_body != null) System.err.println("D_Body : " + this.d_body);
        if(!parameters.isEmpty()){
            System.err.println("Parameters:");
            for(final NameValuePair nvp : parameters){
                
                System.err.println("\t" +  nvp.getName() + " : " + nvp.getValue());
            }
        }
        if(!d_parameters.isEmpty()){
            System.err.println("D_Parameters:");
            for(final NameValuePair nvp : d_parameters){
                
                System.err.println("\t" + nvp.getName() + " : " + nvp.getValue());
            }
        }
        if(!cookies.isEmpty()){
            System.err.println("Cookies:");
            for(final NameValuePair nvp : cookies){
                
                System.err.println("\t" +  nvp.getName() + " : " + nvp.getValue());
            }
        }
        if(!d_cookies.isEmpty()){
            System.err.println("D_Cookies:");
            for(final NameValuePair nvp : d_cookies){
                
                System.err.println("\t" + nvp.getName() + " : " + nvp.getValue());
            }
        }
        if(!headers.isEmpty()){
            System.err.println("Headers:");
            for(final NameValuePair nvp : headers){
                
                System.err.println("\t" +  nvp.getName() + " : " + nvp.getValue());
            }
        }
        if(!d_headers.isEmpty()){
            System.err.println("D_Headers:");
            for(final NameValuePair nvp : d_headers){
                
                System.err.println("\t" + nvp.getName() + " : " + nvp.getValue());
            }
        }
        if(!store.isEmpty()){
            System.err.println("Store:");
            for(final URLActionStore nvp : store){
                
                nvp.outline();
            }
        }
        if(!d_store.isEmpty()){
            System.err.println("D_Store:");
            for(final URLActionStore nvp : d_store){
                
                nvp.outline();
            }
        }
    }
    
    public URLAction build()
    {
        URLAction resultAction = null;
        try
        {
            resultAction = new URLAction(getName(), getUrl(), getInterpreter());
            resultAction.setType(getType());
            resultAction.setMethod(getMethod());
            resultAction.setEncoded(getEncoded());
            resultAction.setHttpResponceCode(getHttpResponceCode());
            resultAction.setBody(getBody());
            resultAction.setCookies(getCookies());
            resultAction.setHeaders(getHeaders());
            resultAction.setParameters(getParameters());
            resultAction.setValidations(getValidations());
            resultAction.setStore(getStore());
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException("Failed to create URLAction", e);
        }
        reset();
        return resultAction;
    }

    private void reset()
    {
        this.interpreter = null;
        this.name = null;
        this.type = null;
        this.url = null;
        this.method = null;
        this.encoded = null;
        this.httpResponceCode = null;
        this.body = null;
        this.validations = Collections.emptyList();
        this.store = Collections.emptyList();
        this.parameters = Collections.emptyList();
        this.cookies = Collections.emptyList();
        this.headers = Collections.emptyList();
    }

    @Nullable
    public String getName()
    {
        String result = null;
        if (this.name != null)
        {
            result = this.name;
        }
        else if (this.d_name != null)
        {
            result = d_name;
        }
        return result;
    }

    @Nullable
    public String getType()
    {
        String result = null;
        if (this.type != null)
        {
            result = this.type;
        }
        else if (this.d_type != null)
        {
            result = d_type;
        }
        return result;
    }

    @Nullable
    public String getUrl()
    {
        String result = null;
        if (this.url != null)
        {
            result = this.url;
        }
        else if (this.d_url != null)
        {
            result = d_url;
        }
        return result;
    }

    @Nullable
    public String getMethod()
    {
        String result = null;
        if (this.method != null)
        {
            result = this.method;
        }
        else if (this.d_method != null)
        {
            result = d_method;
        }
        return result;
    }

    @Nullable
    public String getEncoded()
    {
        String result = null;
        if (this.encoded != null)
        {
            result = this.encoded;
        }
        else if (this.d_encoded != null)
        {
            result = d_encoded;
        }
        return result;
    }

    @Nullable
    public String getHttpResponceCode()
    {
        String result = null;
        if (this.httpResponceCode != null)
        {
            result = this.httpResponceCode;
        }
        else if (this.d_httpResponceCode != null)
        {
            result = d_httpResponceCode;
        }
        return result;
    }

    @Nullable
    public String getBody()
    {
        String result = null;
        if (this.body != null)
        {
            result = this.body;
        }
        else if (this.d_body != null)
        {
            result = d_body;
        }
        return result;
    }

    @Nullable
    public List<URLActionValidation> getValidations()
    {
        List<URLActionValidation> result = Collections.emptyList();
        if (this.validations != null)
        {
            result = this.validations;
        }
        else if (this.d_validations != null)
        {
            result = d_validations;
        }
        return result;
    }

    @Nullable
    public List<NameValuePair> getParameters()
    {
        List<NameValuePair> result = Collections.emptyList();
        if (this.validations != null)
        {
            result = this.parameters;
        }
        else if (this.d_parameters != null)
        {
            result = d_parameters;
        }
        return result;
    }

    @Nullable
    public List<NameValuePair> getHeaders()
    {
        List<NameValuePair> result = Collections.emptyList();
        if (this.headers != null)
        {
            result = this.headers;
        }
        else if (this.d_headers != null)
        {
            result = d_headers;
        }
        return result;
    }

    @Nullable
    public List<NameValuePair> getCookies()
    {
        List<NameValuePair> result = Collections.emptyList();
        if (this.cookies != null)
        {
            result = this.cookies;
        }
        else if (this.d_cookies != null)
        {
            result = d_cookies;
        }
        return result;
    }

    @Nullable
    public List<URLActionStore> getStore()
    {
        List<URLActionStore> result = Collections.emptyList();
        if (this.store != null)
        {
            result = this.store;
        }
        else if (this.d_store != null)
        {
            result = d_store;
        }
        return result;
    }

    @Nullable
    public ParameterInterpreter getInterpreter()
    {
        return interpreter;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public void setType(final String type)
    {
        this.type = type;
    }

    public void setUrl(final String url)
    {
        this.url = url;
    }

    public void setMethod(final String method)
    {
        this.method = method;
    }

    public void setEncoded(final String encoded)
    {
        this.encoded = encoded;
    }

    public void setHttpResponceCode(final String httpResponceCode)
    {
        this.httpResponceCode = httpResponceCode;
    }

    public void setBody(final String body)
    {
        this.body = body;
    }

    public void setValidations(final List<URLActionValidation> validations)
    {
        this.validations = validations;
    }

    public void setStore(final List<URLActionStore> store)
    {
        this.store = store;
    }

    public void setParameters(final List<NameValuePair> parameters)
    {
        this.parameters = parameters;
    }

    public void setCookies(final List<NameValuePair> cookies)
    {
        this.cookies = cookies;
    }

    public void setHeaders(final List<NameValuePair> headers)
    {
        this.headers = headers;
    }

    public void setInterpreter(final ParameterInterpreter interpreter)
    {
        this.interpreter = interpreter;
    }

    public void setDefaultName(final String d_name)
    {
        this.d_name = d_name;
    }

    public void setDefaultType(final String d_type)
    {
        this.d_type = d_type;
    }

    public void setDefaultUrl(final String d_url)
    {
        this.d_url = d_url;
    }

    public String getDefaultMethod()
    {
        return d_method;
    }

    public void setDefaultMethod(final String d_method)
    {
        this.d_method = d_method;
    }

    public void setDefaultEncoded(final String d_encoded)
    {
        this.d_encoded = d_encoded;
    }

    public void setDefaultHttpResponceCode(final String d_httpResponceCode)
    {
        this.d_httpResponceCode = d_httpResponceCode;
    }

    public void setDefaultBody(final String d_body)
    {
        this.d_body = d_body;
    }

    public void setDefaultValidations(final List<URLActionValidation> d_validations)
    {
        this.d_validations = d_validations;
    }

    public void setDefaultStore(final List<URLActionStore> d_store)
    {
        this.d_store = d_store;
    }

    public void setDefaultParameters(final List<NameValuePair> d_parameters)
    {
        this.d_parameters = d_parameters;
    }

    public void setDefaultCookies(final List<NameValuePair> d_cookies)
    {
        this.d_cookies = d_cookies;
    }

    public void setDefaultHeaders(final List<NameValuePair> d_headers)
    {
        this.d_headers = d_headers;
    }

}
