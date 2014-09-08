package integration;

import app.CsvIOService;
import base.FailDirectly;
import base.Strategy;
import builder.StreamOperationBuilder;
import conversion.Result;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class CsvIntegrationTest {

    private CsvIOService service;

    @Before
    public void setUp() throws Exception {
        service = new CsvIOService();
    }

    @Test
    public void shouldUseCsvServiceToParseCsvFile() throws Exception {
        File file = new File("src/test/resources/users.csv");

        Result<User> result = service.parseCsvFile(file, User::new);

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

        Result<User> result = service.parseCsvFile(file, User::new, builder.drop(1).andThen(builder.take(1)));

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

        Result<User> result = service.parseCsvFile(file, User::new);

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

        Result<User> result = service.parseCsvFile(file, User::new);

        assertThat(result.isFailed(), is(true));

        Map<Integer, Set<ConstraintViolation<User>>> failureResult = result.getFailureResult();

        assertThat(failureResult.size(), is(3));

    }

    @Test
    public void shouldUseCsvServiceToUseFailDirectStrategy() throws Exception {
        File file = new File("src/test/resources/users_invalid_3_rows.csv");

        Result<User> result = service.parseCsvFileWithStrategy(file, User::new);

        assertThat(result.isFailed(), is(true));

        Map<Integer, Set<ConstraintViolation<User>>> failureResult = result.getFailureResult();

        assertThat(failureResult.size(), is(3));

    }
}
