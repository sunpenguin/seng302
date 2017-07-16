package seng302.team18.test_mock.model;

import seng302.team18.model.Regatta;

/**
 * Created by afj19 on 10/07/17.
 */
public abstract class BaseRegattaBuilder {

    public Regatta buildRegatta() {
        Regatta regatta = new Regatta();
        regatta.setRegattaName(getRegattaName());
        regatta.setRegattaID(getRegattaId());

        return regatta;
    }

    protected abstract int getRegattaId();

    protected abstract String getRegattaName();
}
