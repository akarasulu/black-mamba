package io.subutai.sim;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The value to change a control parameter to at a point in time in the simulation.
 */
@AllArgsConstructor
public final class ValueChange {
    @Getter private final double value;
    @Getter private final long timeInMillis;
}
