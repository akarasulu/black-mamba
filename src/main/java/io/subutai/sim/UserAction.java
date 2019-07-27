package io.subutai.sim;

import com.sun.istack.internal.NotNull;
import lombok.Data;

import java.util.List;

/**
 * A user action is in the user plane.
 */
@Data
public class UserAction {
    @NotNull private String name;
    @NotNull private List<SampledParameter> parameters;
    @NotNull UserActionDriver driver;
}
