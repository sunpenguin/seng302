package seng302.team18.test_mock.model;

/**
 * This interface provides a skeleton for classes that supply the default values used when building a Boats.xml message
 * from model classes.
 * <p>
 * When the model supports all the data currently given by defaults, this interface and its implementations will no
 * longer be necessary
 */
public interface IBoatsXmlDefaults {

    int getVersion();


    String getRaceBoatType();


    double getBoatLength();


    double getHullLength();


    double getMarkZoneSize();


    double getCourseZoneSize();


    double getLimit1();


    double getLimit2();


    double getLimit3();


    double getLimit4();


    double getLimit5();


    double getGpsX();


    double getGpsY();


    double getGpsZ();


    double getFlagX();


    double getFlagY();


    double getFlagZ();
}
