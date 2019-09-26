package com.turboconsulting.Entity;

import javax.persistence.*;

@Entity
public class ConsentExperiment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int compoundKey;

    @ManyToOne
    @JoinColumn(name="consentId", nullable = false)
    private ConsentOption consentOption;

    @ManyToOne
    @JoinColumn(name="experimentId", nullable = false)
    private Experiment experiment;

    public ConsentExperiment(){};

    public ConsentExperiment(ConsentOption consentOption, Experiment experiment)  {
        this.consentOption = consentOption;
        this.experiment = experiment;
    }

    public int getCompoundKey() {
        return compoundKey;
    }

    public ConsentOption getConsentOption() {
        return consentOption;
    }
    public void setConsentOption(ConsentOption consentOption) {
        this.consentOption = consentOption;
    }

    public Experiment getExperiment() {
        return experiment;
    }
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

}
