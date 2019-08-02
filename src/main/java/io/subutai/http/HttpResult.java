package io.subutai.http;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.Header;


public class HttpResult
{
    public static final int OK = 200;
    public static final int ACCEPTED = 202;

    private final int statusCode;

    private String body;

    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, List<String>> headersAdvanced = new HashMap<>();


    public HttpResult( int statusCode, Header[] headers, String body )
    {
        this.statusCode = statusCode;
        this.body = body;

        initHeaders( headers );
    }


    public int getStatusCode()
    {
        return statusCode;
    }


    private void initHeaders( Header[] headers )
    {
        for ( Header h : headers )
        {
            this.headers.put( h.getName(), h.getValue() );

            // WARNING: header names may be not unique, thus it will overwrite previous headers
            if ( this.headersAdvanced.containsKey( h.getName() ) )
            {
                this.headersAdvanced.get( h.getName() ).add( h.getValue() );
            }
            else
            {
                ArrayList l = new ArrayList();
                l.add( h.getValue() );
                this.headersAdvanced.put( h.getName(), l );
            }
        }
    }


    public Map<String, String> getHeaders()
    {
        return headers;
    }


    public String getBody()
    {
        return body;
    }


    public boolean isOK()
    {
        return statusCode == OK || statusCode == 204;
    }

    public Map<String, List<String>> getHeadersAdvanced()
    {
        return this.headersAdvanced;
    }


    @Override
    public String toString()
    {
        return new ToStringBuilder( this, ToStringStyle.SHORT_PREFIX_STYLE )
                .append( "statusCode", statusCode )
                .append( "body", StringUtils.abbreviate( body, 100 ) )
                .toString();
    }
}
