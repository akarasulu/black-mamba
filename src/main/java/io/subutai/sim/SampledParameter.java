package io.subutai.sim;

import org.apache.commons.math3.distribution.RealDistribution;

/**
 * A parameter that can be sampled using a probability distribution. This could
 * the amount of GW being purchased by users.
 */
public interface SampledParameter {
    String getName();
    boolean isReal();
    boolean isInteger();
    double getRangeMin();
    double getRangeMax();
    RealDistribution getDistribution();
    void setDistribution(RealDistribution distribution);
    double sample();
}
