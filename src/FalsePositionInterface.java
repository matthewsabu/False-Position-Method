import java.util.*;

public interface FalsePositionInterface {
    public Vector<Double> stringToDblVector(String coefficients);
    public void printIterationData(double[] iterData);
    public double createFunction(Vector<Double> coefficients, int vectorSize, double value);
    public double getXr(double xl, double xu, double fxl, double fxu);
    public double getE(double xri, double xr);
}
