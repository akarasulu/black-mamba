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

    /**
     * This is what changes the value.
     *
     * @return the driver for changing the value
     */
    ControlParameterDriver getDriver();

    /**
     * Should create the ValueChange and put it in the correct position based on timeInMillis.
     *
     * @param value the real value to change the parameter to
     * @param timeInMillis the time in millisecond at which point the change is made
     */
    void addValue(double value, long timeInMillis);
}
