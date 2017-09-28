package seng302.team18.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.function.Supplier;

/**
 * Prefixes all println() calls with a specified prefix.
 */
public class PrefixedPrintStream extends PrintStream {

    private final Supplier<String> prefix;


    /**
     * @param out    the output stream to print to
     * @param prefix the prefix supplier
     */
    public PrefixedPrintStream(OutputStream out, Supplier<String> prefix) {
        super(out);
        this.prefix = prefix;
    }

    @Override
    public void println() {
        super.print(prefix.get());
        super.println();
    }

    @Override
    public void println(boolean x) {
        super.print(prefix.get());
        super.println(x);
    }

    @Override
    public void println(char x) {
        super.print(prefix.get());
        super.println(x);
    }

    @Override
    public void println(int x) {
        super.print(prefix.get());
        super.println(x);
    }

    @Override
    public void println(long x) {
        super.print(prefix.get());
        super.println(x);
    }

    @Override
    public void println(float x) {
        super.print(prefix.get());
        super.println(x);
    }

    @Override
    public void println(double x) {
        super.print(prefix.get());
        super.println(x);
    }

    @Override
    public void println(char[] x) {
        super.print(prefix.get());
        super.println(x);
    }

    @Override
    public void println(String x) {
        super.print(prefix.get());
        super.println(x);
    }

    @Override
    public void println(Object x) {
        super.print(prefix.get());
        super.println(x);
    }
}
