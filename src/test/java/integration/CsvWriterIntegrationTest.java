package integration;

import app.CsvWriterService;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

public class CsvWriterIntegrationTest {

    private CsvWriterService service;

    @Before
    public void setUp() throws Exception {
        service = new CsvWriterService();
    }

    @Test
    public void shouldWriteToOutputStream() throws FileNotFoundException {
        FileOutputStream fileOutputStream = new FileOutputStream("src/test/resources/output.csv");
        BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/users.csv"));

//        service.writeTOStream(reader, );
    }
}
