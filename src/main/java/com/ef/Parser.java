package com.ef;

import com.ef.cli.ParserCli;
import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.env.Environment;

import static com.ef.util.ContextUtil.getCtx;

public class Parser {

    public static void main(String[] args) throws Exception {
        String env = System.getProperty("env", Environment.CLI);
        PicocliRunner.run(ParserCli.class, getCtx(".env", env), args);
    }

}
