package seng302.team18.visualiser.util;

import seng302.team18.racemodel.MockDataStream;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ModelLoader {

    public void startModel(int port) throws IOException {
        String classpath = Arrays.stream(((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs())
                .map(URL::getFile)
                .collect(Collectors.joining(File.pathSeparator));

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
