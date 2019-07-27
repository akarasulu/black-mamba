package io.subutai.sim;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * A user action is in the user plane.
 */
@Data
public class UserAction {
    @NonNull private String name;
    @NonNull private List<SampledParameter> parameters;
    @NonNull UserActionDriver driver;
}
