package com.turboconsulting.Entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Experiment {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @OneToMany(mappedBy = "experiment", cascade = CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER)
    private Set<ConsentExperiment> consentExperiments;

    @OneToMany(mappedBy = "experiment" , fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<VisitorExperiment> visitors;


    @Column(unique=true)
    private String name;

    private String description;


    public Experiment(){};
    public Experiment(String name, String description) {
        this.name = name;
        this.description = description;
        visitors = new HashSet<>();
        consentExperiments = new HashSet<>();

    }

    public int getId() {

        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    public void setVisitors(Set<VisitorExperiment> visitors) {
        this.visitors = visitors;
    }
    public Set<VisitorExperiment> getVisitors() {
        return visitors;
    }
    public void addVisitorExperiment(VisitorExperiment e) {
        visitors.add(e);
    }
    public void removeVisitor(VisitorExperiment visitorExperiment)  {
        visitors.remove(visitorExperiment);
    }

    public Set<ConsentExperiment> getConsentExperiments() {
        return consentExperiments;
    }
    public void setConsentExperiments(Set<ConsentExperiment> consentExperiments) {
        this.consentExperiments = consentExperiments;
    }
    public void addConsentOption(ConsentExperiment consentOption)  {
        this.consentExperiments.add(consentOption);
    }
    public void removeConsentExperiment(ConsentExperiment consentExperiment)  {this.consentExperiments.remove(consentExperiment);}


}
