import app.CsvService;
import builder.StreamOperationBuilder;
import conversion.Result;

import java.io.File;
import java.util.List;


public class MainRootFromScala {



    public static void main(String[] args) {
        CsvService service = new CsvService();
        File file = new File("/Users/xueli/Desktop/project/IOUtils/src/integration-test/resources/ApprovedInverter_Long.csv");

        StreamOperationBuilder<ValueObject> builder = new StreamOperationBuilder<>();

        Result<ValueObject> result = service.parse(file, ValueObject::new, builder.drop(1500).andThen(builder.take(100)));

        System.out.println(result.isSuccessful());

        List<ValueObject> successfulResult = result.getResult();

        System.out.println(successfulResult.size());

        for (ValueObject valueObject : successfulResult) {
            System.out.println(valueObject.getAcPower());
            System.out.println(valueObject.getApprovedDate());
            System.out.println(valueObject.getExpiredDate());
            System.out.println(valueObject.getManufacturer());
            System.out.println(valueObject.getModelNumber());
            System.out.println(valueObject.getSeries());
        }
    }
}
