//package seng302.team18.messageinterpreting;
//
//
//import seng302.team18.messageparsing.AC35RaceStatusMessage;
//import seng302.team18.messageparsing.MessageBody;
//
///**
// * Created by spe76 on 27/04/17.
// */
//public class RaceStatusInterpreter extends MessageInterpreter {
//
//
//    @Override
//    public void interpret(MessageBody message) {
//        final int WARNING = 1;
//        final int PRE_START = 10;
//        if (message instanceof AC35RaceStatusMessage) {
//            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
//            int status = statusMessage.getRaceStatus();
//            if (status == WARNING || status == PRE_START) {
////                controllerManager.
//            }
//        }
//    }
//}
