package io.subutai.sim;

import java.util.List;

/**
 * A list of values to use for a control parameter impacting the overall behavior
 * of the system. Usually you should only be varying one control parameter to
 * properly study the system behavior. It becomes the test control.
 */
public interface ControlParameter extends List<ValueChange> {
    String getName();
    double getRangeMin();
    double getRangeMax();
}
