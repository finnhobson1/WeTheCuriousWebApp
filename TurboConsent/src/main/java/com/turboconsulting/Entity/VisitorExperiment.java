package com.turboconsulting.Entity;

import javax.persistence.*;
import java.util.GregorianCalendar;

@Entity
public class VisitorExperiment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int compoundKey;

    @ManyToOne
    @JoinColumn(name="visitorId", nullable = false)
    private Visitor visitor;

    @ManyToOne
    @JoinColumn(name="experimentId", nullable = false)
    private Experiment experiment;

    @ManyToOne
    @JoinColumn(name="consentId", nullable = false)
    private ConsentOption consentOption;

    private GregorianCalendar date;

    public Visitor getVisitor() {
        return visitor;
    }

    private boolean changedConsent;

    public VisitorExperiment(){}

    public VisitorExperiment(Visitor v, Experiment e) {
        this.visitor = v;
        this.experiment = e;
        this.consentOption = visitor.getDefaultConsent();
        this.changedConsent = false;
        this.date = new GregorianCalendar();
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public int getCompoundKey() {
        return compoundKey;
    }
    public void setCompoundKey(int compoundKey) {
        this.compoundKey = compoundKey;
    }

    public boolean getChangedConsent() {
        return changedConsent;
    }
    public void setChangedConsent(boolean changedConsent) {
        this.changedConsent = changedConsent;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public GregorianCalendar getDate() {
        return date;
    }
    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public ConsentOption getConsentOption() {
        return consentOption;
    }
    public void setConsentOption(ConsentOption consentOption) {
        changedConsent = true;
        this.consentOption = consentOption;
    }
}
