package integration;

import app.CsvWriterService;
import com.google.common.base.Function;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nullable;
import java.io.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CsvWriterIntegrationTest {

    private CsvWriterService service;

    @Before
    public void setUp() throws Exception {
        service = new CsvWriterService();
    }

    @Test
    public void shouldWriteToOutputStream() throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter("src/test/resources/output.csv"));
        BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/users.csv"));
        FileIteratorAdaptor adaptor = new FileIteratorAdaptor(reader);


        service.writeTOStream(adaptor, User::produce, fileWriter);

        BufferedReader result = new BufferedReader(new FileReader("src/test/resources/output.csv"));
        assertThat(result.readLine(), is("username,company,interest,team"));
    }
}
