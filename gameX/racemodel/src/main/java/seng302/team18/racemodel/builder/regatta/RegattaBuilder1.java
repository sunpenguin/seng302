package seng302.team18.racemodel.builder.regatta;

/**
 * Builds a preset regatta.
 * <p>
 * Concrete implementation of AbstractRegattaBuilder.
 *
 * @see AbstractRegattaBuilder
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
