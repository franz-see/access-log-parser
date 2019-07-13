package ph.net.see;

import io.micronaut.configuration.picocli.PicocliRunner;
import ph.net.see.cli.ParserCli;

public class Main {

    public static void main( String[] args ) throws Exception {
        PicocliRunner.run(ParserCli.class, args);
    }

}
