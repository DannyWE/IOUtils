package unused;

import org.hibernate.validator.constraints.NotEmpty;

public class ValueObject {

    @NotEmpty
    private String manufacturer;
    @NotEmpty
    private String series;
    @NotEmpty
    private String modelNumber;
    @NotEmpty
    private String acPower;
    @NotEmpty
    private String approvedDate;
    @NotEmpty
    private String expiredDate;


    public ValueObject(String manufacturer, String series, String modelNumber, String acPower, String approvedDate, String expiredDate) {
        this.manufacturer = manufacturer;
        this.series = series;
        this.modelNumber = modelNumber;
        this.acPower = acPower;
        this.approvedDate = approvedDate;
        this.expiredDate = expiredDate;
    }

    public ValueObject(String[] input) {
        this.manufacturer = input[0];
        this.series = input[1];
        this.modelNumber = input[2];
        this.acPower = input[3];
        this.approvedDate = input[4];
        this.expiredDate = input[5];
    }


    public String getManufacturer() {
        return manufacturer;
    }

    public String getSeries() {
        return series;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public String getAcPower() {
        return acPower;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public String getExpiredDate() {
        return expiredDate;
    }
}
