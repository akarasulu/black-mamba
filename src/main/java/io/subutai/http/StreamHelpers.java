package io.subutai.http;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


public class StreamHelpers
{

    private StreamHelpers()
    {
        // this is a utility class
    }


    public static void close( Closeable c )
    {
        if ( c != null )
        {
            try
            {
                c.close();
            }
            catch ( IOException ex )
            {
                System.out.println(ex.getMessage());
//                LOGGER.error( "Failed to close resource", ex );
            }
        }
    }


    public static String readAsString( InputStream stream ) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try
        {
            br = new BufferedReader( new InputStreamReader( stream, StandardCharsets.UTF_8 ) );
            String line;
            while ( ( line = br.readLine() ) != null )
            {
                if ( sb.length() > 0 )
                {
                    sb.append( System.lineSeparator() );
                }
                sb.append( line );
            }
        }
        finally
        {
            close( br );
            close( stream );
        }
        return sb.toString();
    }


    public static byte[] readAsByteArray( InputStream stream ) throws IOException
    {
        try ( ByteArrayOutputStream out = new ByteArrayOutputStream() )
        {
            int bytes;
            byte[] buf = new byte[1024];
            while ( ( bytes = stream.read( buf ) ) > 0 )
            {
                out.write( buf, 0, bytes );
            }
            return out.toByteArray();
        }
        finally
        {
            close( stream );
        }
    }


    /**
     * Writes <code>InputStream</code> contents to temporary file. After finishing the use of the file, users should
     * delete the file to avoid pollution of temporary files location.
     *
     * @param stream the stream to dump
     *
     * @return path to temporary file
     *
     * @throws IOException
     */
    public static Path writeToTempFile( InputStream stream ) throws IOException
    {
        return writeToTempFile( stream, null );
    }


    public static Path writeToTempFile( InputStream stream, String ext ) throws IOException
    {
        Path target = Files.createTempFile( "hub-", ext );
        try
        {
            Files.copy( stream, target, StandardCopyOption.REPLACE_EXISTING );
            return target;
        }
        finally
        {
            close( stream );
        }
    }
}

