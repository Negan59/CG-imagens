
package cg.parte2;

public class No {
     private double ymax, xmin;
        private double incx;
     
        public No(double ymax, double xmin, double incx)
        {
            this.ymax = ymax;
            this.xmin = xmin;
            this.incx = incx;
        }

        public double getXmin()
        {
            return xmin;
        }

        public double getYmax()
        {
            return ymax;
        }

        public double getIncX()
        {
            return incx;
        }
        public void setXmin(double x)
        {
            xmin = x;
        }
        public void setYmax(double y)
        {
            ymax = y;
        }
        public void setIncX(double inc)
        {
            incx = inc;
        }
}
