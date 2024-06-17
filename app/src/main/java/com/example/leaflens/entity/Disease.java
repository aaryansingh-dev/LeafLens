package com.example.leaflens.entity;

import java.util.ArrayList;

public class Disease {

    private String name;
    private String severity;
    private String diseaseCategory;
    private String description;
    private ArrayList<String> symptoms;
    private ArrayList<String> treatments;

    public Disease(String name, String severity)
    {
        this.name = name;
        this.severity = severity;
    }

    public Disease(String name, String severity, ArrayList<String> symptoms, ArrayList<String> treatments)
    {
        this.name = name;
        this.severity = severity;
        this.symptoms = symptoms;
        this.treatments = treatments;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setSeverity(String severity)
    {
        this.severity = severity;
    }

    public String getSeverity()
    {
        return this.severity;
    }

    public void setSymptoms(ArrayList<String> symptoms)
    {
        this.symptoms = symptoms;
    }

    public ArrayList<String> getSymptoms()
    {
        return this.symptoms;
    }

    public void setTreatments(ArrayList<String> treatments)
    {
        this.treatments = treatments;
    }

    public ArrayList<String> getTreatments(){
        return this.treatments;
    }

    public void setDiseaseCategory(String category)
    {
        diseaseCategory = category;
    }

    public String getDiseaseCategory()
    {
        return this.diseaseCategory;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
}
