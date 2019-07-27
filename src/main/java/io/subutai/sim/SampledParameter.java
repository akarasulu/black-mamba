package io.subutai.sim;

import lombok.Data;
import org.apache.commons.math3.distribution.RealDistribution;

/**
 * A parameter that can be sampled using a probability distribution. This could
 * the amount of GW being purchased by users.
 */
@Data
public class SampledParameter {
    private final String name;
    private final double rangeMin;
    private final double rangeMax;
    private final RealDistribution distribution;
    private final double sampleRate;

    public double sample() {
        return distribution.sample();
    }
}
