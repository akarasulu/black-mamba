package io.subutai.http;


import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.bouncycastle.util.encoders.Base64;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;


public class SSLUtil
{
    private static final String BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
    private static final String END_CERTIFICATE = "-----END CERTIFICATE-----";


    public static void disableCertificateValidation()
    {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager()
                {
                    public X509Certificate[] getAcceptedIssuers()
                    {
                        return new X509Certificate[0];
                    }


                    public void checkClientTrusted( X509Certificate[] certs, String authType )
                    {
                    }


                    public void checkServerTrusted( X509Certificate[] certs, String authType )
                    {
                    }
                }
        };


        HostnameVerifier hv = new HostnameVerifier()
        {
            public boolean verify( String hostname, SSLSession session )
            {
                return true;
            }
        };


        try
        {
            SSLContext sc = SSLContext.getInstance( "SSL" );
            sc.init( null, trustAllCerts, new SecureRandom() );
            HttpsURLConnection.setDefaultSSLSocketFactory( sc.getSocketFactory() );
            HttpsURLConnection.setDefaultHostnameVerifier( hv );
        }
        catch ( Exception e )
        {
            System.out.println(e.getMessage());
//            log.error( e.getMessage() );
        }
    }


    /*
    Checks if PEM file SSL certificate is valid.
    See https://stackoverflow.com/a/24139603
     */
    private static boolean isValidSslCertificate( String sslCert )
    {
        if ( Strings.isNullOrEmpty( sslCert ) )
        {
            return false;
        }

        sslCert = sslCert.trim();

        try
        {
            CertificateFactory fact = CertificateFactory.getInstance( "X.509" );
            X509Certificate cer =
                    ( X509Certificate ) fact.generateCertificate( new ByteArrayInputStream( sslCert.getBytes() ) );
            //PublicKey key = cer.getPublicKey();
            cer.checkValidity();

            return true;
        }
        catch ( Exception e )
        {
            System.out.println(e.getMessage());
//            log.error( "SSL certificate validation failed: " + e.getMessage() );
        }

        return false;
    }


    /*
    Checks if PEM file SSL certificate's private key is valid
    See https://stackoverflow.com/a/7221381
    TODO: if needed, add processing for "BEGIN RSA PRIVATE KEY", like described in https://stackoverflow.com/a/30929175
     */
    private static boolean isValidSslPemPrivateKey( String sslPrivateKey )
    {
        if ( Strings.isNullOrEmpty( sslPrivateKey ) )
        {
            return false;
        }

        sslPrivateKey = sslPrivateKey.trim();

        try
        {
            String pkStartingLine = findPrivateKeyStartingLine( sslPrivateKey );
            String pkEndingLine = findPrivateKeyEndingLine( sslPrivateKey );

            if ( pkStartingLine != null && pkEndingLine != null )
            {
                sslPrivateKey = sslPrivateKey.replace( pkStartingLine, "" );
                sslPrivateKey = sslPrivateKey.replace( pkEndingLine, "" );
            }

            // Base64 decode the data

            byte[] encoded = Base64.decode( sslPrivateKey );

            // PKCS8 decode the encoded RSA private key

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec( encoded );
            KeyFactory kf = KeyFactory.getInstance( "RSA" );
            PrivateKey privateKey = kf.generatePrivate( keySpec );

            return privateKey != null;
        }
        catch ( Exception e )
        {

            System.out.println(e.getMessage());
//            log.error( "SSL RSA private key validation failed: " + e.getMessage() );
        }

        return false;
    }


    public static String validateNginxSslPem( final String pem )
    {
        String sslCert = null, sslPrivKey = null;

        Preconditions.checkArgument( StringUtils.isNotBlank( pem ), "No SSL certificate provided." );

        // extract and validate certificate

        int start = pem.indexOf( BEGIN_CERTIFICATE );
        int end = pem.indexOf( END_CERTIFICATE );

        if ( start >= 0 && start < end )
        {
            String _sslCert = pem.substring( start, end + END_CERTIFICATE.length() );

            if ( isValidSslCertificate( _sslCert ) )
            {
                sslCert = _sslCert;
            }
        }

        Preconditions.checkArgument( sslCert != null, "Invalid SSL certificate in PEM" );


        // extract and validate private key

        int len = 0;
        String pkStartingLine = findPrivateKeyStartingLine( pem ), pkEndingLine = findPrivateKeyEndingLine( pem );

        if ( pkStartingLine != null && pkEndingLine != null )
        {
            start = pem.indexOf( pkStartingLine );
            end = pem.indexOf( pkEndingLine );
            len = pkEndingLine.length();
        }

        if ( start >= 0 && start < end )
        {
            String _sslPrivKey = pem.substring( start, end + len );

            if ( isValidSslPemPrivateKey( _sslPrivKey ) )
            {
                sslPrivKey = _sslPrivKey;
            }
        }

        Preconditions.checkArgument( sslPrivKey != null, "Invalid SSL private key in PEM" );


        return sslPrivKey + "\n" + sslCert;
    }


    public static boolean isValidateNginxSslPem( final String pem )
    {
        try
        {
            validateNginxSslPem( pem );
        }
        catch ( Exception e )
        {
            return false;
        }
        return true;
    }


    private static String findPrivateKeyStartingLine( String key )
    {
        String[] lines = key.split( System.getProperty( "line.separator" ) );

        for ( final String line : lines )
        {
            if ( line.contains( "-----BEGIN" ) && line.contains( "PRIVATE KEY-----" ) )
            {
                return line;
            }
        }

        return null;
    }


    private static String findPrivateKeyEndingLine( String key )
    {
        String[] lines = key.split( System.getProperty( "line.separator" ) );

        for ( final String line : lines )
        {
            if ( line.contains( "-----END" ) && line.contains( "PRIVATE KEY-----" ) )
            {
                return line;
            }
        }

        return null;
    }
}
