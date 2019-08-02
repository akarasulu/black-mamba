package io.subutai.http;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.ssl.SSLContextBuilder;


public class HttpUtil
{
    private static final int TIMEOUT_30_SEC = 30 * 1000;


    public static HttpResult get( String url ) throws IOException
    {
        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = new HttpGet( url );

        HttpResponse response = client.execute( get );

        String body = StreamHelpers.readAsString( response.getEntity().getContent() );

        return new HttpResult( response.getStatusLine().getStatusCode(), response.getAllHeaders(), body );
    }


    public static HttpResult getWithContext( String url ) throws IOException
    {
        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = new HttpGet( url );

        HttpResponse response = client.execute( get, new BasicHttpContext() );

        String body = StreamHelpers.readAsString( response.getEntity().getContent() );

        return new HttpResult( response.getStatusLine().getStatusCode(), response.getAllHeaders(), body );
    }


    public static HttpResult getWithContext( String url, int ttlInMillis ) throws IOException
    {
        RequestConfig.Builder requestBuilder =
                RequestConfig.custom().setConnectTimeout( ttlInMillis ).setSocketTimeout( ttlInMillis )
                             .setConnectionRequestTimeout( ttlInMillis );

        HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig( requestBuilder.build() ).build();

        HttpGet get = new HttpGet( url );

        HttpResponse response = client.execute( get, new BasicHttpContext() );

        String body = StreamHelpers.readAsString( response.getEntity().getContent() );

        return new HttpResult( response.getStatusLine().getStatusCode(), response.getAllHeaders(), body );
    }


    public static HttpResult get( String url, int timeout )
            throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException
    {
        //SSL certificate ignore
        SSLContextBuilder sslBuilder = new SSLContextBuilder();
        sslBuilder.loadTrustMaterial( null, new TrustSelfSignedStrategy() );
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory( sslBuilder.build(),
                SSLConnectionSocketFactory.getDefaultHostnameVerifier() );

        HttpClient client = getHttpClientBuilderWithTimeout( timeout ).setSSLSocketFactory( sslsf ).build();

        HttpGet get = new HttpGet( url );

        HttpResponse response = client.execute( get );

        String body = StreamHelpers.readAsString( response.getEntity().getContent() );

        return new HttpResult( response.getStatusLine().getStatusCode(), response.getAllHeaders(), body );
    }


    public static HttpResult post( String url, Map<String, String> params, int timeout ) throws IOException
    {
        HttpClient client = getHttpClientBuilderWithTimeout( timeout ).build();

        HttpPost post = new HttpPost( url );

        List<NameValuePair> urlParams = new ArrayList<>();

        params.forEach( ( k, v ) -> {
            urlParams.add( new BasicNameValuePair( k, v ) );
        } );

        post.setEntity( new UrlEncodedFormEntity( urlParams ) );

        HttpResponse response = client.execute( post );

        String body = StreamHelpers.readAsString( response.getEntity().getContent() );

        return new HttpResult( response.getStatusLine().getStatusCode(), response.getAllHeaders(), body );
    }


    /**
     * Get HTTP request to specified URL
     *
     * @param timeout HTTP timeout value in milliseconds
     */
    public static HttpResult getSslCertIgnore( String url, int timeout ) throws IOException
    {
        HttpClient client = getHttpsClient( timeout );

        HttpGet get = new HttpGet( url );

        HttpResponse response = client.execute( get );

        String body = StreamHelpers.readAsString( response.getEntity().getContent() );

        return new HttpResult( response.getStatusLine().getStatusCode(), response.getAllHeaders(), body );
    }


    /**
     * Post requests with params in URL
     */
    public static HttpResult postHttpsUrl( String url, int timeoutMillis ) throws IOException
    {
        HttpClient client = getHttpsClient( timeoutMillis );

        HttpPost post = new HttpPost( url );

        HttpResponse response = client.execute( post );

        String body =
                response.getEntity() != null ? StreamHelpers.readAsString( response.getEntity().getContent() ) : null;

        return new HttpResult( response.getStatusLine().getStatusCode(), response.getAllHeaders(), body );
    }


    public static HttpClientBuilder getHttpClientBuilderWithTimeout( int mills )
    {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal( 128 );
        RequestConfig.Builder requestBuilder = RequestConfig.custom();
        requestBuilder = requestBuilder.setConnectTimeout( mills );
        requestBuilder = requestBuilder.setSocketTimeout( mills );

        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setDefaultRequestConfig( requestBuilder.build() );
        builder.setConnectionManager( cm );

        return builder;
    }


    public static String getByURLConnection( String urlString, int timeout ) throws IOException
    {
        //skip ssl cert
        SSLUtil.disableCertificateValidation();
        URL url = new URL( urlString );
        URLConnection con = url.openConnection();
        con.setConnectTimeout( timeout );
        con.setReadTimeout( timeout );

        return IOUtils.toString( con.getInputStream() );
    }


    public static HttpURLConnection getHttpURLConnection( String urlString, int timeout ) throws IOException
    {
        //skip ssl cert
        SSLUtil.disableCertificateValidation();
        URL url = new URL( urlString );
        HttpURLConnection connection = ( HttpURLConnection ) url.openConnection();
        connection.setConnectTimeout( timeout );
        connection.setReadTimeout( timeout );
        connection.setRequestMethod( "GET" );
        connection.connect();

        return connection;
    }


    public static String postByURLConnection( String urlString, Map<String, String> params, int timeout )
            throws IOException
    {
        //skip ssl cert
        SSLUtil.disableCertificateValidation();
        URL url = new URL( urlString );

        StringBuilder postData = new StringBuilder();
        for ( Map.Entry<String, String> param : params.entrySet() )
        {
            if ( postData.length() != 0 )
            {
                postData.append( '&' );
            }
            postData.append( URLEncoder.encode( param.getKey(), StandardCharsets.UTF_8.name() ) );
            postData.append( '=' );
            postData.append( URLEncoder.encode( String.valueOf( param.getValue() ), StandardCharsets.UTF_8.name() ) );
        }
        String urlParameters = postData.toString();
        URLConnection conn = url.openConnection();
        conn.setReadTimeout( timeout );
        conn.setConnectTimeout( timeout );
        conn.setDoOutput( true );

        OutputStreamWriter writer = new OutputStreamWriter( conn.getOutputStream() );

        writer.write( urlParameters );
        writer.flush();

        return IOUtils.toString( conn.getInputStream() );
    }


    /**
     * Get HTTP client which ignores self-signed SSL certificates
     */
    public static CloseableHttpClient getHttpsClient( int timeoutSeconds )
    {
        try
        {
            RequestConfig config =
                    RequestConfig.custom().setSocketTimeout( timeoutSeconds ).setConnectTimeout( timeoutSeconds )
                                 .build();

            SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
            sslContextBuilder.loadTrustMaterial( null, new TrustSelfSignedStrategy() );
            SSLConnectionSocketFactory sslSocketFactory =
                    new SSLConnectionSocketFactory( sslContextBuilder.build(), NoopHostnameVerifier.INSTANCE );

            return HttpClients.custom().setDefaultRequestConfig( config ).setSSLSocketFactory( sslSocketFactory )
                              .build();
        }
        catch ( NoSuchAlgorithmException | KeyStoreException | KeyManagementException e )
        {
            System.out.println( e.getMessage() );
            //            log.error( e.getMessage() );
        }

        return HttpClients.createDefault();
    }
}
