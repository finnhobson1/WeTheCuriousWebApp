package com.turboconsulting.Entity;

import org.apache.tomcat.util.bcel.Const;
import org.apache.commons.text.WordUtils;



import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ConsentOption {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int consentId;

    @OneToMany(mappedBy = "consentOption", cascade = CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER)
    private Set<ConsentExperiment> consentExperiments;

    @OneToMany(mappedBy = "defaultConsent", cascade = CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER)
    private Set<Visitor> visitors;

    @OneToMany(mappedBy = "consentOption", cascade = CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER)
    private Set<VisitorExperiment> visitorExperiments;

    @Column(unique = true)
    private String name;

    @Column(columnDefinition="Text")
    private String description;

    public ConsentOption() {};
    public ConsentOption(String name, String description) {
        this.name = WordUtils.capitalizeFully(name);
        this.description = description;
        this.consentExperiments = new HashSet<>();
        this.visitors = new HashSet<>();
        this.visitorExperiments = new HashSet<>();
    }

    public void setConsentId(int consentId) {
        this.consentId = consentId;
    }

    public int getConsentId() {
        return consentId;
    }

    public String getName() {
        return WordUtils.capitalizeFully(name);
    }
    public void setName(String name) {
        this.name = WordUtils.capitalizeFully(name);
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    public Set<ConsentExperiment> getConsentExperiments() {
        return consentExperiments;
    }
    public void setConsentExperiments(Set<ConsentExperiment> consentExperiments) {
        this.consentExperiments = consentExperiments;
    }
    public void addConsentExperiment(ConsentExperiment consentExperiment)  {
        this.consentExperiments.add(consentExperiment);
    }
    public void removeConsentExperiment(ConsentExperiment consentExperiment)  {
        consentExperiments.remove(consentExperiment);
    }

    public Set<Visitor> getVisitors() {
        return visitors;
    }
    public void setVisitors(Set<Visitor> visitors) {
        this.visitors = visitors;
    }
    public void addVisitor(Visitor v)  {
        this.visitors.add(v);
    }
    public void removeVisitor(Visitor v)  {this.visitors.remove(v);}

    public Set<VisitorExperiment> getVisitorExperiments() {
        return visitorExperiments;
    }
    public void setVisitorExperiments(Set<VisitorExperiment> visitorExperiments) {
        this.visitorExperiments = visitorExperiments;
    }
    public void addExperiment(VisitorExperiment visitorExperiment)  {
        this.visitorExperiments.add(visitorExperiment);
    }
    public void removeVisitorExperiment(VisitorExperiment visitorExperiment)  {
        this.visitorExperiments.remove(visitorExperiment);
    }

}
