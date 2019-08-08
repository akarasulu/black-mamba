package io.subutai.experiment;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.CauchyDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ConstantRealDistribution;
import org.apache.commons.math3.distribution.GeometricDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import io.subutai.http.HttpUtil;

import static org.apache.http.HttpHeaders.USER_AGENT;


public class Start
{
    public static Map<Integer, Integer> binomialDistribution()
    {
        RandomGenerator rg = new JDKRandomGenerator();
        rg.setSeed( 300 );
        Map<Integer, Integer> map = new HashMap<>();

        BinomialDistribution bd = new BinomialDistribution( rg, 300, 0.5 );
        final int[] arr = bd.sample( 300 );

        return parseInt( arr );
    }


    public static Map<Integer, Integer> normalDistribution()
    {
        RandomGenerator rg = new JDKRandomGenerator();

        rg.setSeed( 300 );

        NormalDistribution tp = new NormalDistribution( rg, 1, 200 );

        final double[] arr = tp.sample( 300 );

        return parseDouble( arr );
    }


    public static Map<Integer, Integer> parseDouble( double[] arr )
    {
        Map<Integer, Integer> map = new HashMap<>();

        int arr1[] = new int[arr.length];
        int count = 0;
        for ( double n : arr )
        {
            arr1[count] = ( int ) n;
            count++;
        }
        for ( int n : arr1 )
        {
            map.put( n, frequency( arr1, n ) );
        }

        for ( Integer key : map.keySet() )
        {
            System.out.println( key + " " + map.get( key ) );
        }

        return map;
    }


    public static Map<Integer, Integer> parseInt( int[] arr )
    {
        Map<Integer, Integer> map = new HashMap<>();

        for ( int n : arr )
        {
            map.put( n, frequency( arr, n ) );
        }

        for ( Integer key : map.keySet() )
        {
            System.out.println( key + " " + map.get( key ) );
        }

        return map;
    }


    public static Map<Integer, Integer> cauchyDistribution()
    {
        RandomGenerator rg = new JDKRandomGenerator();
        rg.setSeed( 300 );

        CauchyDistribution cd = new CauchyDistribution( rg, 300, 0, 1 );
        final double[] arr = cd.sample( 300 );

        return parseDouble( arr );
    }


    public static Map<Integer, Integer> chiSquaredDistribution()
    {
        RandomGenerator rg = new JDKRandomGenerator();
        rg.setSeed( 300 );

        ChiSquaredDistribution cd = new ChiSquaredDistribution( rg, 6 );
        final double[] arr = cd.sample( 300 );

        return parseDouble( arr );
    }


    public static Map<Integer, Integer> geometricDistribution()
    {
        RandomGenerator rg = new JDKRandomGenerator();
        rg.setSeed( 300 );

        GeometricDistribution gd = new GeometricDistribution( rg, 0.5 );

        final int[] arr = gd.sample( 300 );

        return parseInt( arr );
    }


    public static Map<Integer, Integer> betaDistribution()
    {
        RandomGenerator rg = new JDKRandomGenerator();
        rg.setSeed( 300 );

        BetaDistribution betaDistribution = new BetaDistribution( rg, 1, 0.5 );

        final double[] arr = betaDistribution.sample( 300 );

        double[] arr1 = new double[arr.length];
        int count = 0;
        for ( double p : arr )
        {
            arr1[count] = p * 10;
            count++;
        }

        return parseDouble( arr1 );
    }


    public static Map<Integer, Integer> constantRealDistribution()
    {
        ConstantRealDistribution crd = new ConstantRealDistribution( 0.5 );
        final double[] arr = crd.sample( 300 );

        System.out.println( Arrays.toString( arr ) );

        return parseDouble( arr );
    }


    public static void main( String[] args )
    {

        //        Binomial Binomial
        //        Geometric Binomial
        //        Beta Binomial
        //        Cauchy Binomial
        //        Binomial Geometric
        //        Geometric Geometric
        //        Beta Geometric
        //        Cauchy Geometric
        //        Binomial Beta
        //        Geometric Beta
        //        Beta Beta
        //        Cauchy Beta
        //        Binomial Cauchy
        //        Geometric Cauchy
        //        Beta Cauchy
        //        Cauchy Cauchy


        //chiSquaredDistribution();
        //cauchyDistribution();

        final Map<Integer, Integer> map = binomialDistribution();

        map.keySet().stream().forEach( key -> {

            for ( int i = 0; i < map.get( key ); i++ )
            {
//                Map<String, String> params = new HashMap<>();
//                params.put( "amount", key.toString() );

                try
                {
//                    HttpUtil.postByURLConnection( "http://localhost:3000/khan", params, 20000 );
                    sendPost(key.toString());

                    Thread.sleep( 6000 );
                }
                catch (  Exception e )
                {
                    e.printStackTrace();
                }
            }
        } );


        //geometricDistribution();
        //betaDistribution();
    }


    public static int frequency( int[] arr, int n )
    {
        int count = 0;
        for ( double p : arr )
        {
            if ( p == n )
            {
                count++;
            }
        }

        return count;
    }

    private static void sendPost(String  amount ) throws Exception {

        String url = "http://localhost:3000/khan?amount="+amount;

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", USER_AGENT);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
//        urlParameters.add(new BasicNameValuePair("amount", amount) );


        post.setEntity(new UrlEncodedFormEntity(urlParameters) );

        HttpResponse response = client.execute(post );
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + post.getEntity());
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());

    }
}
