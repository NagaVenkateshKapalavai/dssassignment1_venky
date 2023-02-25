package org.example.utility;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ListPrintStream extends PrintStream {

    private final List<String> output = new ArrayList<>();

    public ListPrintStream() {
        super(System.out);
    }

    public void println(final String x) {
        super.print(x);
        output.add(x);
    }

    public void print(final String x) {
        super.print(x);
        output.add(x);
    }

    // override other print methods for other types

    public List<String> getOutput() {
        return output;
    }
}
