package seng302.team18.visualiser.util;

import seng302.team18.racemodel.MockDataStream;
import seng302.team18.util.NautiLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * Manages the hosted server process
 */
public class ModelLoader {

    private static final String clientPrefix = "[client] ";
    private static final String serverPrefix = "[server] ";


    /**
     * Opens a game server process, redirects its output to System.out and System.err while differentiating from
     * output of this process.
     *
     * @param port the port the server will listen on
     * @throws IOException see {@link ProcessBuilder#start() ProcessBuilder#start()}
     */
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

        new Thread(new TextStreamPrepender(serverPrefix, model.getInputStream(), NautiLogger.getStdout()::println)).start();
        new Thread(new TextStreamPrepender(serverPrefix, model.getErrorStream(), NautiLogger.getStderr()::println)).start();

        Supplier<String> stringSupplier = () -> clientPrefix + NautiLogger.getTimeStamp();
        NautiLogger.setOutputPrefix(stringSupplier);
        NautiLogger.setErrorPrefix(stringSupplier);

        new Thread(() -> {
            try {
                model.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            NautiLogger.setDefaultOutput();
            System.out.println("Hosted server process closed (port=" + port + ")");
        }).start();
    }


    /**
     * Prepends the prefix to the input stream, then passes to the destination
     */
    private class TextStreamPrepender implements Runnable {
        private final String prefix;
        private final InputStream in;
        private final Consumer<String> consumeInputLine;


        TextStreamPrepender(String prefix, InputStream source, Consumer<String> destination) {
            this.in = source;
            this.prefix = prefix;
            this.consumeInputLine = destination;
        }


        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(in)).lines().forEach(line -> consumeInputLine.accept(prefix + line));
        }
    }
}
