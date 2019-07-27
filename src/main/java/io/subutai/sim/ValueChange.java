package io.subutai.sim;

import lombok.Data;

/**
 * The value to change a control parameter to at a point in time in the simulation.
 */
@Data
final class ValueChange {
    private final double value;
    private final long timeInMillis;

    @Override
    public final int hashCode() {
        return (int) timeInMillis;
    }
}
