package seng302.team18.visualiser.util;

import seng302.team18.racemodel.MockDataStream;

import java.io.IOException;

public class ModelLoader {

    public void startModel(int port) throws IOException {
        String classpath = System.getProperty("java.class.path");

        ProcessBuilder builder = new ProcessBuilder(
                System.getProperty("java.home") + "/bin/java",
                "-classpath",
                classpath,
                MockDataStream.class.getCanonicalName(),
                ((Integer) port).toString()
        );

        builder.redirectError(ProcessBuilder.Redirect.INHERIT);
        builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);

        builder.start();
    }
}
