package Marks;

import java.util.ArrayList;

/**
 * Created by ROSE on 11/4/2016.
 */
public class SeriesMarks {
    public String indexNo;
    public String name;
    public ArrayList<String> seriesChem;
    public ArrayList<String> seriesPhy;
    public ArrayList<String> seriesBio;
    public ArrayList<String> seriesMath;

    public String  physicsAttendance[];
    public String chemistryAttendance[];
    public String bioAttendance[];
    public String mathsAttendance[];


    public SeriesMarks(){
        seriesPhy = new ArrayList<>();
        seriesChem = new ArrayList<>();
        seriesBio = new ArrayList<>();
        seriesMath = new ArrayList<>();

        physicsAttendance = new String[2];
        chemistryAttendance = new String[2];
        bioAttendance = new String[2];
        mathsAttendance = new String[2];

    }

    public SeriesMarks(String name, String indexNo){
        this.name = name;
        this.indexNo = indexNo;
        seriesPhy = new ArrayList<>();
        seriesChem = new ArrayList<>();
        seriesBio = new ArrayList<>();
        seriesMath = new ArrayList<>();
    }

    public void setSeriesChem(ArrayList<String> seriesChem) {
        this.seriesChem = seriesChem;
    }

    public void setSeriesPhy(ArrayList<String> seriesPhy) {
        this.seriesPhy = seriesPhy;
    }

    public ArrayList<String> getSeriesBio() {
        return seriesBio;
    }

    public void setSeriesBio(ArrayList<String> seriesBio) {
        this.seriesBio = seriesBio;
    }

    public ArrayList<String> getSeriesMath() {
        return seriesMath;
    }

    public void setSeriesMath(ArrayList<String> seriesMath) {
        this.seriesMath = seriesMath;
    }

    public String[] getPhysicsAttendance() {
        return physicsAttendance;
    }

    public void setPhysicsAttendance(String[] physicsAttendance) {
        this.physicsAttendance = physicsAttendance;
    }

    public String[] getChemistryAttendance() {
        return chemistryAttendance;
    }

    public void setChemistryAttendance(String[] chemistryAttendance) {
        this.chemistryAttendance = chemistryAttendance;
    }

    public String[] getBioAttendance() {
        return bioAttendance;
    }

    public void setBioAttendance(String[] bioAttendance) {
        this.bioAttendance = bioAttendance;
    }

    public String[] getMathsAttendance() {
        return mathsAttendance;
    }

    public void setMathsAttendance(String[] mathsAttendance) {
        this.mathsAttendance = mathsAttendance;
    }

    public String getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(String indexNo) {
        this.indexNo = indexNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getSeriesChem() {
        return seriesChem;
    }

    public void addSeriesChem(String seriesChem) {
        this.seriesChem.add(seriesChem);
    }

    public void addSeriesMath(String math) {
        this.seriesMath.add(math);
    }
    public void addSeriesBio(String bio) {
        this.seriesBio.add(bio);
    }
    public ArrayList<String> getSeriesPhy() {
        return seriesPhy;
    }



    public void addSeriesPhy(String seriesPhy) {
        this.seriesPhy.add(seriesPhy);
    }
}
