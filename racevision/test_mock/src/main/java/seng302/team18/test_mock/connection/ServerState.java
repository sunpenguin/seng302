package seng302.team18.test_mock.connection;

/**
 * Created by hqi19 on 24/07/17.
 */
public enum ServerState {
    CLOSED(0);

    int state;

    ServerState(int state) {
        this.state = state;
    }

}
