package etnc.piscine.moulinette.console.repl;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class ReplIo {
    private final BufferedReader in;
    private final PrintWriter out;

    public ReplIo(Reader in, Writer out) {
        this.in = new BufferedReader(in);
        this.out = new PrintWriter(out, true);
    }
    public static ReplIo stdio() {
        return new ReplIo(
            new InputStreamReader(System.in, StandardCharsets.UTF_8),
            new OutputStreamWriter(System.out, StandardCharsets.UTF_8));
    }
    public String readLine() throws IOException { return in.readLine(); }
    public void write(String s) { out.print(s); out.flush(); }
}
