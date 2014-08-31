import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

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
