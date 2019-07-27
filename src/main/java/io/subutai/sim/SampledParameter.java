package io.subutai.sim;

import lombok.Data;
import lombok.NonNull;
import org.apache.commons.math3.distribution.RealDistribution;

/**
 * A parameter that can be sampled using a probability distribution. This could
 * the amount of GW being purchased by users.
 */
@Data
public class SampledParameter {
    @NonNull private final String name;
    private final double rangeMin;
    private final double rangeMax;
    @NonNull private final RealDistribution distribution;
    private final double sampleRate;

    public double sample() {
        return distribution.sample();
    }
}
