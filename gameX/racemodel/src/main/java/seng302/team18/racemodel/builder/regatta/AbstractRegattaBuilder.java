package seng302.team18.racemodel.builder.regatta;

import seng302.team18.model.Regatta;

/**
 * Base implementation class for regatta builders
 * <p>
 * This class provides a skeleton for classes that builder a regatta.
 */
public abstract class AbstractRegattaBuilder {

    /**
     * Builds a regatta
     *
     * @return the constructed regatta
     */
    public Regatta buildRegatta() {
        Regatta regatta = new Regatta();
        regatta.setRegattaName(getRegattaName());
        regatta.setRegattaID(getRegattaId());

        return regatta;
    }


    /**
     * @return the id to use for the regatta
     */
    protected abstract int getRegattaId();


    /**
     * @return the name to use for the regatta
     */
    protected abstract String getRegattaName();
}
