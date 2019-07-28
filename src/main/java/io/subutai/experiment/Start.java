package io.subutai.experiment;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.CauchyDistribution;
import org.apache.commons.math3.distribution.GeometricDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;


public class Start
{


    public static void binomialDistribution()
    {
        RandomGenerator rg = new JDKRandomGenerator();
        rg.setSeed( 300 );
        Map<Integer, Integer> map = new HashMap<>();

        BinomialDistribution bd = new BinomialDistribution( rg, 300, 0.5 );
        final int[] arr = bd.sample( 300 );
        for ( int n : arr )
        {
            map.put( n, frequency( arr, n ) );
        }

        for ( Integer key : map.keySet() )
        {
            System.out.println(key + " " + map.get( key ));
        }

        System.out.println( arr.length );
    }


    public static void normalDistribution()
    {
        RandomGenerator rg = new JDKRandomGenerator();
        rg.setSeed( 300 );

        Map<Integer, Integer> map = new HashMap<>();

        NormalDistribution tp = new NormalDistribution( rg, 1, 200 );

        final double[] arr = tp.sample( 300 );
        int arr1[] = new int[arr.length];
        int count = 0;
        for ( double n : arr )
        {
            arr1[count] = (int) n;
            count++;

        }
        for ( int n : arr1 )
        {
            map.put( n, frequency( arr1, n ) );
        }

        for ( Integer key : map.keySet() )
        {
            System.out.println(key + " " + map.get( key ));
        }
    }


    public static void cauchyDistribution()
    {
        RandomGenerator rg = new JDKRandomGenerator();
        rg.setSeed( 300 );

        CauchyDistribution cd = new CauchyDistribution( rg, 500, 5, 0.5 );
        final double[] arr = cd.sample( 300 );

        for ( double n : arr )
        {
            System.out.println( Collections.frequency( Arrays.asList( arr ), n ) );
        }

        System.out.println( arr.length );
    }


    public static void geometricDistribution()
    {
        RandomGenerator rg = new JDKRandomGenerator();
        rg.setSeed( 300 );

        GeometricDistribution gd = new GeometricDistribution( rg, 0.5 );

        final int[] arr = gd.sample( 300 );

        for ( double n : arr )
        {
            System.out.println(n);
        }

        System.out.println( arr.length );
    }


    public static void main( String[] args )
    {

        //        cauchyDistribution();
//        binomialDistribution();
                geometricDistribution();
//                        normalDistribution();

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
}
