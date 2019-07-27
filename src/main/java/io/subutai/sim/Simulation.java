package io.subutai.sim;

import com.sun.istack.internal.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class Simulation {
    @NotNull private List<ControlParameter> parameters;
    @NotNull private List<UserAction> userActions;
}
