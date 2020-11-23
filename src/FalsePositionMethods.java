import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class FalsePositionMethods implements FalsePositionInterface{

    @Override
    public Vector<Double> stringToDblVector(String coefficients) {
        String[] abc = coefficients.split(" "); //whitespaces can be denoted by \\s+

        Vector<Double> coeffVec = new Vector<Double>();

        for(String strCoeff : abc){
            double intCoeff = Double.parseDouble(strCoeff);
            coeffVec.addElement(intCoeff);
        }

        System.out.println("Inputted Vector: " + coeffVec);
        return coeffVec;
    }

    @Override
    public void printIterationData(double[] iterData) {
        int index=0;
        for(double column : iterData){
            if(index != 4) {
                if(index == 0) System.out.printf("%-8.0f",column);
                else System.out.printf("%-12s",column);
            }
            index++;
        }
        System.out.println();
        return;
    }

    @Override
    public double createFunction(Vector<Double> coefficients, int vectorSize, double value) {
        double sum = 0;
        for(int x=vectorSize-1;x>=0;x--){
            double term = coefficients.get(x)*Math.pow(value,x);
            sum = sum + term;
        }
        DecimalFormat df = new DecimalFormat("0.00000");
        if(sum > 0) df.setRoundingMode(RoundingMode.FLOOR);
        else df.setRoundingMode(RoundingMode.CEILING);
        double truncatedSum = Double.parseDouble(df.format(sum));
        return truncatedSum;
    }

    @Override
    public double getXr(double xl, double xu, double fxl, double fxu) {
        double xr;
        double numerator = (xu*fxl)-(xl*fxu);
        double denominator = (fxl-fxu);
        if(numerator == 0 || denominator == 0) xr = 0;
        else xr = numerator/denominator;
        DecimalFormat df = new DecimalFormat("0.00000");
        if(xr > 0) df.setRoundingMode(RoundingMode.FLOOR);
        else df.setRoundingMode(RoundingMode.CEILING);
        double truncatedXr = Double.parseDouble(df.format(xr));
        return truncatedXr;
    }
    
    @Override
    public double getE(double xri, double xr) {
        double e = Math.abs(((xri - xr)/xri) * 100);
        DecimalFormat df = new DecimalFormat("0.00000");
        df.setRoundingMode(RoundingMode.FLOOR);
        double truncatedE = Double.parseDouble(df.format(e));
        return truncatedE;
    }
    
}
