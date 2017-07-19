package seng302.team18.test_mock.model;

/**
 * Builds a preset regatta.
 * <p>
 * Concrete implementation of AbstractRegattaBuilder.
 *
 * @see seng302.team18.test_mock.model.AbstractRegattaBuilder
 */
public class RegattaBuilder1 extends AbstractRegattaBuilder {

    @Override
    protected int getRegattaId() {
        return 4;
    }


    @Override
    protected String getRegattaName() {
        return "Atlantic/Bermuda Test";
    }

}
