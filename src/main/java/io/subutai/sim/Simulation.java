package io.subutai.sim;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class Simulation {
    @NonNull private List<ControlParameter> parameters;
    @NonNull private List<UserAction> userActions;
}
