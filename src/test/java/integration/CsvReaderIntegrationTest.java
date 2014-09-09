package integration;

import app.CsvService;
import app.CsvReaderService;
import builder.StreamOperationBuilder;
import conversion.Result;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class CsvReaderIntegrationTest {

    private CsvService fileService;
    private CsvReaderService readerService;

    @Before
    public void setUp() throws Exception {
        fileService = new CsvService();
        readerService = new CsvReaderService();
    }

    @Test
    public void shouldUseCsvServiceToParseReader() throws Exception {
        Result<User> result = readerService.parse(new FileReader("src/test/resources/users.csv"), User::new);

        assertThat(result.isSuccessful(), is(true));

        List<User> userList = result.getResult();

        assertThat(userList.size(), is(7));

        assertThat(userList.get(0).getName(), is("username"));
        assertThat(userList.get(0).getCompany(), is("company"));
        assertThat(userList.get(0).getInterest(), is("interest"));
        assertThat(userList.get(0).getTeam(), is("team"));
        assertThat(userList.get(1).getName(), is("Cloud"));
        assertThat(userList.get(2).getName(), is("James"));
        assertThat(userList.get(3).getName(), is("Andres"));
        assertThat(userList.get(4).getName(), is("Yanhui"));
        assertThat(userList.get(5).getName(), is("Jason"));
        assertThat(userList.get(6).getName(), is("Varol"));
    }

    @Test
    public void shouldUseCsvServiceToParseCsvFile() throws Exception {
        File file = new File("src/test/resources/users.csv");

        Result<User> result = fileService.parse(file, User::new);

        assertThat(result.isSuccessful(), is(true));

        List<User> userList = result.getResult();

        assertThat(userList.size(), is(7));

        assertThat(userList.get(0).getName(), is("username"));
        assertThat(userList.get(0).getCompany(), is("company"));
        assertThat(userList.get(0).getInterest(), is("interest"));
        assertThat(userList.get(0).getTeam(), is("team"));
        assertThat(userList.get(1).getName(), is("Cloud"));
        assertThat(userList.get(2).getName(), is("James"));
        assertThat(userList.get(3).getName(), is("Andres"));
        assertThat(userList.get(4).getName(), is("Yanhui"));
        assertThat(userList.get(5).getName(), is("Jason"));
        assertThat(userList.get(6).getName(), is("Varol"));
    }

    @Test
    public void shouldUseCsvServiceToOperateStreamAndParseCsvFile() throws Exception {
        File file = new File("src/test/resources/users.csv");

        StreamOperationBuilder<User> builder = new StreamOperationBuilder<>();

        Result<User> result = fileService.parse(file, User::new, builder.drop(1).andThen(builder.take(1)));

        assertThat(result.isSuccessful(), is(true));

        List<User> userList = result.getResult();

        assertThat(userList.size(), is(1));

        assertThat(userList.get(0).getName(), is("Cloud"));
        assertThat(userList.get(0).getCompany(), is("AusRegistry"));
        assertThat(userList.get(0).getInterest(), is("Swimming"));
        assertThat(userList.get(0).getTeam(), is("dev"));
    }

    @Test
    public void shouldUseCsvServiceToGetErrorsFromFile() throws Exception {
        File file = new File("src/test/resources/users_invalid_1_rows.csv");

        Result<User> result = fileService.parse(file, User::new);

        assertThat(result.isFailed(), is(true));

        Map<Integer, Set<ConstraintViolation<User>>> failureResult = result.getFailureResult();

        assertThat(failureResult.size(), is(1));

        assertThat(failureResult.get(3).size(), is(3));

        Iterator<ConstraintViolation<User>> iterator = failureResult.get(3).iterator();

        ConstraintViolation<User> company = iterator.next();

        assertThat(company.getPropertyPath(), notNullValue());
        assertThat(company.getMessage(), is("may not be empty"));

        ConstraintViolation<User> interest = iterator.next();

        assertThat(interest.getPropertyPath(), notNullValue());
        assertThat(interest.getMessage(), is("may not be empty"));

        ConstraintViolation<User> team = iterator.next();
        assertThat(team.getPropertyPath(), notNullValue());
        assertThat(team.getMessage(), is("may not be empty"));
    }

    @Test
    public void shouldUseCsvServiceToGetErrorsFromAllTheRows() throws Exception {
        File file = new File("src/test/resources/users_invalid_3_rows.csv");

        Result<User> result = fileService.parse(file, User::new);

        assertThat(result.isFailed(), is(true));

        Map<Integer, Set<ConstraintViolation<User>>> failureResult = result.getFailureResult();

        assertThat(failureResult.size(), is(3));

    }

    @Test
    public void shouldUseCsvServiceWithBufferedValidation() {
        File file = new File("src/test/resources/users.csv");

        Result<User> result = fileService.parseWithLimitedValidation(file, User::new, 3);

        assertThat(result.isFailed(), is(false));
        assertThat(result.isSuccessful(), is(true));
    }

    @Test
    public void shouldGetBufferedValidationResult() throws Exception {
        File file = new File("src/test/resources/users_invalid_3_rows.csv");

        Result<User> result = fileService.parseWithLimitedValidation(file, User::new, 1);

        assertThat(result.isFailed(), is(true));

        Map<Integer, Set<ConstraintViolation<User>>> failureResult = result.getFailureResult();

        assertThat(failureResult.size(), is(1));

        Result<User> overflowResult = fileService.parseWithLimitedValidation(file, User::new, 4);

        assertThat(overflowResult.getFailureResult().size(), is(3));

    }

    @Test
    public void shouldUseCsvServiceWithStreamOperationAndBufferedValidation() throws Exception {
        File file = new File("src/test/resources/users_invalid_3_rows.csv");

        StreamOperationBuilder<User> builder = new StreamOperationBuilder<>();

        Result<User> result = fileService.parseWithLimitedValidation(file,
                User::new,
                builder.drop(1).andThen(builder.take(1)),
                3);

        assertThat(result.isSuccessful(), is(true));

        List<User> userList = result.getResult();

        assertThat(userList.size(), is(1));

        assertThat(userList.get(0).getName(), is("Cloud"));
        assertThat(userList.get(0).getCompany(), is("AusRegistry"));
        assertThat(userList.get(0).getInterest(), is("Swimming"));
        assertThat(userList.get(0).getTeam(), is("dev"));
    }

}
