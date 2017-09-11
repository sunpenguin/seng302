package seng302.team18.visualiser.util;

import seng302.team18.racemodel.MockDataStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

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

        Process model = builder.start();
        new Thread(new TextStreamPrepender("[server stdout] ", model.getInputStream(), System.out::println)).start();
        new Thread(new TextStreamPrepender("[server stderr] ", model.getErrorStream(), System.err::println)).start();
    }


    private class TextStreamPrepender implements Runnable {
        private final String prefix;
        private final InputStream in;
        private final Consumer<String> consumeInputLine;

        TextStreamPrepender(String prefix, InputStream source, Consumer<String> destination) {
            this.in = source;
            this.prefix = prefix;
            this.consumeInputLine = destination;
        }

        public void run() {
            new BufferedReader(new InputStreamReader(in)).lines().forEach(line -> consumeInputLine.accept(prefix + line));
        }
    }
}
