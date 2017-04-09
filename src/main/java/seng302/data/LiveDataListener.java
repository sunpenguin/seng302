package seng302.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Sets up a client socket to read from the AC35 data stream.
 *
 * Created by jds112 and spe76 on 9/04/17.
 */

public class LiveDataListener {
    private Socket socket;
    private BufferedReader in;
    private String currentline;
    private int portNumber;

    private boolean keepRunning;

    public LiveDataListener(int portNumber) {
        this.portNumber = portNumber;
        this.keepRunning = true;
        this.currentline = "";

        // Create input and output streams for reading in data
        try {
            socket = new Socket("livedata.americascup.com", portNumber);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

        // The input host is unknown
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: livedata.americascup.com");
            System.exit(1);

        // An issue occurred setting up the IO streams
        } catch (IOException e) {
            System.out.println("No I/O");
            System.exit(1);
        }
    }

    /**
     * Used in a loop to collect data from the given data stream
     */
    public void run() {
        try {
            currentline = in.readLine();
            System.out.println(currentline);
        }catch(IOException e){
            System.out.println("Read failed");
            //e.printStackTrace();
        }

    }

    public String getCurrentline() {
        System.out.println(currentline);
        return currentline;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getIn() {
        return in;
    }

    public boolean isKeepRunning() {
        return keepRunning;
    }

    public void setKeepRunning(boolean keepRunning) {
        this.keepRunning = keepRunning;
    }
}
