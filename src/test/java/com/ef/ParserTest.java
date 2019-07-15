package com.ef;

import org.junit.jupiter.api.*;

import java.io.File;
import java.net.URL;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ParserTest {

    @BeforeAll
    static void setup() {
        System.setProperty("env", "withdb");
    }

    @Test
    @Order(1)
    void oneLineTest() throws Exception {
        File file = getFileFromClasspath("access-logs/basic.log");
        Parser.main(new String[] {
                format("--accesslog=%s", file.getAbsolutePath()),
                "--startDate=2017-01-01.13:00:00",
                "--duration=hourly",
                "--threshold=100"
        });
    }

    @Test
    @Order(2)
    void oneThousandLineTest() throws Exception {
        File file = getFileFromClasspath("access-logs/1000-lines.log");
        Parser.main(new String[] {
                format("--accesslog=%s", file.getAbsolutePath()),
                "--startDate=2017-01-01.13:00:00",
                "--duration=hourly",
                "--threshold=100"
        });
    }

    @Test
    @Order(3)
    void noAccesslogParameter() throws Exception {
        Parser.main(new String[] {
                "--startDate=2017-01-01.13:00:00",
                "--duration=hourly",
                "--threshold=100"
        });
    }

    private File getFileFromClasspath(String path) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        assertThat(resource).as("[GUARD] should have found %s from the classpath.", path).isNotNull();
        return new File(resource.getFile());
    }
}