package io.subutai.sim;

import com.sun.istack.internal.NotNull;
import lombok.Data;
import java.util.LinkedList;

/**
 * A list of values to use for a control parameter impacting the overall behavior
 * of the system. Usually you should only be varying one control parameter to
 * properly study the system behavior. It becomes the test control.
 */
@Data
public class ControlParameter extends LinkedList<ValueChange> {
    @NotNull private final String name;
    private final double rangeMin;
    private final double rangeMax;

    /**
     * This is what changes the value.
     *
     * @return the driver for changing the value
     */
    @NotNull private final ControlParameterDriver driver;

    /**
     * Should create the ValueChange object and insert it into the correct position
     * based on timeInMillis to maintain sorted order.
     *
     * @param value the real value to change the parameter to
     * @param timeInMillis the time in millisecond at which point the change is made
     */
    public void addValue(double value, long timeInMillis) {
        if (size() == 0) {
            add(new ValueChange(value, timeInMillis));
        } else if (get(0).getTimeInMillis() > timeInMillis) {
            add(0, new ValueChange(value, timeInMillis));
        } else if (get(size() - 1).getTimeInMillis() < timeInMillis) {
            add(size(), new ValueChange(value, timeInMillis));
        } else {
            int i = 0;
            while (get(i).getTimeInMillis() < timeInMillis) {
                i++;
            }
            add(i, new ValueChange(value, timeInMillis));
        }
    }
}
