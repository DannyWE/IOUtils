package integration;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class Pec {

    @NotNull
    private Long pecId;
    @NotEmpty
    private String eiteEntity;
    @NotNull
    private Integer assessmentYear;
    @NotEmpty
    private String liabilityAccountNumber;
    @NotEmpty
    private String eiteActivity;
    @NotEmpty
    private String partialExemption;
    @NotNull
    private Boolean cancelledFlag;

    public Pec(String[] input) {
        this.pecId = Long.parseLong(input[0]);
        this.eiteEntity = input[1];
        this.assessmentYear = Integer.parseInt(input[2]);
        this.liabilityAccountNumber = input[3];
        this.eiteActivity = input[4];
        this.partialExemption = input[5];
        this.cancelledFlag = Boolean.parseBoolean(input[6]);
    }

    public Long getPecId() {
        return pecId;
    }

    public String getEiteEntity() {
        return eiteEntity;
    }

    public Integer getAssessmentYear() {
        return assessmentYear;
    }

    public String getLiabilityAccountNumber() {
        return liabilityAccountNumber;
    }

    public String getEiteActivity() {
        return eiteActivity;
    }

    public String getPartialExemption() {
        return partialExemption;
    }

    public Boolean getCancelledFlag() {
        return cancelledFlag;
    }
}
