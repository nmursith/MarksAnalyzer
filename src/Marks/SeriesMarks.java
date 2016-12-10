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

    public SeriesMarks(){
        seriesPhy = new ArrayList<>();
        seriesChem = new ArrayList<>();
    }

    public SeriesMarks(String name, String indexNo){
        this.name = name;
        this.indexNo = indexNo;
        seriesPhy = new ArrayList<>();
        seriesChem = new ArrayList<>();
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

    public ArrayList<String> getSeriesPhy() {
        return seriesPhy;
    }

    public void addSeriesPhy(String seriesPhy) {
        this.seriesPhy.add(seriesPhy);
    }
}
