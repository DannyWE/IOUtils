import app.CsvIOService;

import java.io.File;
import java.util.List;

import builder.StreamOperationBuilder;
import com.google.common.base.Function;
import conversion.Result;
import static builder.StreamOperationBuilder.*;


public class MainRootFromScala {



    public static void main(String[] args) {
        CsvIOService service = new CsvIOService();
        File file = new File("/Users/xueli/Desktop/project/IOUtils/src/integration-test/resources/ApprovedInverter_Long.csv");
        Result<ValueObject> result = service.parseCsvFile(file, ValueObject::new, drop(1500));

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
