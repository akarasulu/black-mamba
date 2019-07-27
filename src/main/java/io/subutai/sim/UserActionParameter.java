package io.subutai.sim;

/**
 * A parameter associated with a user action that will be sampled using a distribution: i.e. KHAN amount when buying GW with KHAN.
 */
public interface UserActionParameter extends SampledParameter {
    String getName();
}
