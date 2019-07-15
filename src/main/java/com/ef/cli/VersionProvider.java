package com.ef.cli;

import io.micronaut.context.annotation.Value;
import picocli.CommandLine;

import javax.inject.Singleton;

@Singleton
public class VersionProvider implements CommandLine.IVersionProvider {

    @Value("${application.version}")
    private String version;

    @Override
    public String[] getVersion() {
        return new String[] {version};
    }
}
