package io.subutai.sim;

import java.util.List;

public interface Simulation {
    List<ControlParameter> getControlParameters();
    List<UserAction> getUserActions();
}
