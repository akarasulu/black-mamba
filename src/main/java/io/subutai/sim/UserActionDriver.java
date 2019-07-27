package io.subutai.sim;

import java.util.concurrent.Future;

public interface UserActionDriver {
    Future perform() throws Exception;
}
