package io.subutai.experiment;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.CauchyDistribution;
import org.apache.commons.math3.distribution.ConstantRealDistribution;
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

        parseInt( arr );
    }


    public static void normalDistribution()
    {
        RandomGenerator rg = new JDKRandomGenerator();

        rg.setSeed( 300 );

        NormalDistribution tp = new NormalDistribution( rg, 1, 200 );

        final double[] arr = tp.sample( 300 );

        parseDouble( arr );
    }


    public static void parseDouble( double[] arr )
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
    }


    public static void parseInt( int[] arr )
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

        System.out.println( arr.length );
    }


    public static void cauchyDistribution()
    {
        RandomGenerator rg = new JDKRandomGenerator();
        rg.setSeed( 300 );

        CauchyDistribution cd = new CauchyDistribution( rg, 300, 1, 0.1 );
        final double[] arr = cd.sample( 300 );

        parseDouble( arr );
    }


    public static void geometricDistribution()
    {
        RandomGenerator rg = new JDKRandomGenerator();
        rg.setSeed( 300 );

        GeometricDistribution gd = new GeometricDistribution( rg, 0.5 );

        final int[] arr = gd.sample( 300 );

        parseInt( arr );
    }


    public static void betaDistribution()
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

        parseDouble( arr1 );
    }


    public static void constantRealDistribution()
    {
        ConstantRealDistribution crd = new ConstantRealDistribution( 0.5 );
        final double[] arr = crd.sample( 300 );

        System.out.println( Arrays.toString( arr ) );

        parseDouble( arr );
    }


    public static void main( String[] args )
    {
        //        cauchyDistribution();
        //        binomialDistribution();
                geometricDistribution();
        //        betaDistribution();
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
