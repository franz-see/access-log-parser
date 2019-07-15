package com.ef;

import com.ef.cli.ParserCli;
import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;

import java.io.IOException;

import static com.ef.util.ContextUtil.getCtx;

public class Parser {

    public static void main(String[] args) throws Exception {
        run(args);
    }

    public static ApplicationContext run(String[] args) throws IOException {
        String env = System.getProperty("env", Environment.CLI);
        ApplicationContext ctx = getCtx(".env", env);
        PicocliRunner.run(ParserCli.class, ctx, args);
        return ctx;
    }

}
