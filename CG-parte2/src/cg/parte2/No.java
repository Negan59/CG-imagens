package cg.parte2;

public class No {
    private double yMax;
    private double xMin;
    private double incrX;

    public double getyMax() {
        return yMax;
    }

    public void setyMax(double yMax) {
        this.yMax = yMax;
    }

    public double getxMin() {
        return xMin;
    }

    public void setxMin(double xMin) {
        this.xMin = xMin;
    }

    public double getIncrX() {
        return incrX;
    }

    public void setIncrX(double incrX) {
        this.incrX = incrX;
    }

    public No(double yMax, double xMin, double incrX) {
        this.yMax = yMax;
        this.xMin = xMin;
        this.incrX = incrX;
    }
}
