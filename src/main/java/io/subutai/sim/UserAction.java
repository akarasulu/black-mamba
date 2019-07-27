package io.subutai.sim;

import java.util.List;

/**
 * A user action is in the user plane.
 */
public interface UserAction {
    String getName();
    List<UserActionParameter> getUserActionParameters();
    UserActionDriver getDriver();
}
