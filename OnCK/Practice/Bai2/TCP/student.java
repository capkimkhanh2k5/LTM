package OnCK.Practice.Bai2.TCP;

public class student {
    private String id;
    private String name;
    private double[] cores;

    public student(String id, String name, double[] cores) {
        this.id = id;
        this.name = name;
        this.cores = cores;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double[] getCores() {
        return cores;   
    }
}
