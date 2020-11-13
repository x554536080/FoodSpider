package Entities;

public class FoodItem {
    public String url;
    public String fid;
    public String fname;
    public String ftype;
    public String ftag;
    public String fintro;
    public String fscore;

    public FoodItem(String fid, String fname, String ftype, String ftag, String fintro) {
        this.fid = fid;
        this.fname = fname;
        this.ftype = ftype;
        this.ftag = ftag;
        this.fintro = fintro;
    }

    public FoodItem(String url) {
        this.url = url;
    }
}
