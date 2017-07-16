package seng302.team18.test_mock.model;

/**
 * Created by afj19 on 10/07/17.
 */
public class RegattaBuilder1 extends BaseRegattaBuilder {

    @Override
    protected int getRegattaId() {
        return 4;
    }

    @Override
    protected String getRegattaName() {
        return "Atlantic/Bermuda Test";
    }
}
