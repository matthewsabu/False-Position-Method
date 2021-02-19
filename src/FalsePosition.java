import java.math.*;
import java.util.*;
//Created by: Matthew Simon M. Sabularse | ID 11813652
public class FalsePosition {

    public static void main(String[] args) {
        Scanner coefficients = new Scanner(System.in);
        Vector<Double> coeffVec = new Vector<Double>(); //[C,B,A]
        //algo initial conditions
        double iterNo=1,xrCurr,xrPrev,fxl,fxu,fxr,fxlfxr,e;
        xrCurr=xrPrev=fxl=fxu=fxr=fxlfxr=e=0;

        //set given assumptions
        double[] initxlxu = {3,15};   //initial [xl,xu]
        double[] stopCrit = {15,0.0001,-1}; //[iteration,|error%|,|f(xr)|] *if error or f(xr) is not given, use -1.
        coeffVec = setVector(coefficients,coeffVec);    
        printGiven(coeffVec,initxlxu,stopCrit);

        //set initial iteration row (Row #1)
        double[] iterData = {iterNo,initxlxu[0],initxlxu[1],xrCurr,xrPrev,fxl,fxu,fxr,fxlfxr,e};
        
        //perform algo and display table
        System.out.format("\n%-8s%-12s%-12s%-12s%-12s%-12s%-12s%-12s%-12s\n","iter #","xl","xu","xr","f(xl)","f(xu)","f(xr)","f(xl)f(xr)","error %");
        runFalsePositionAlgo(coeffVec,stopCrit,iterData);

        coefficients.close();
    }

    //user input
    public static Vector<Double> setVector(Scanner coefficients, Vector<Double> coeffVec){
        //define function
        System.out.println("False Position Method: Find your equation's root!");
        System.out.print("Polynomial Equation's coefficients ([C B A] Format): ");

        String[] abc = coefficients.nextLine().split(" "); //whitespaces can be denoted by \\s+        
        for(String strCoeff : abc){
            double intCoeff = Double.parseDouble(strCoeff);
            coeffVec.addElement(intCoeff);
        }
        return coeffVec;
    }

    public static void printGiven(Vector<Double> coeffVec, double[] initxlxu, double[] stopCrit){        
        //display assumptions
        System.out.println("Inputted Vector: " + coeffVec);
        System.out.println("\nInitial Conditions:");
        System.out.println("xl = " + initxlxu[0]);
        System.out.println("xu = " + initxlxu[1]);
        System.out.println("\nStopping Criteria:");
        System.out.println("Iterations = " + stopCrit[0]);
        System.out.println("Error % = " + new BigDecimal(Double.toString(stopCrit[1])));
        System.out.println("|f(xr)| = " + new BigDecimal(Double.toString(stopCrit[2])));
    }

    //0=iteration,1=xl,2=xu,3=xrCurr(NEW),4=xrPrev(OLD),5=fxl,6=fxu,7=fxr,8=fxl*fxr,9=e (iterData index references)
    public static void runFalsePositionAlgo(Vector<Double> coefficients, double[] stopCrit, double[] iterData) {
        boolean exactedRoot = false;
        double approxRoot,xlNew,xuNew;
        approxRoot=xlNew=xuNew=0;
        
        double xl = iterData[1];
        double xu = iterData[2];
        double fxl = createFunction(coefficients,coefficients.size(),xl); //f(xl)
        double fxu = createFunction(coefficients,coefficients.size(),xu); //f(xu)
        double xrCurr = getXr(xl,xu,fxl,fxu);   //xr (NEW xr)
        double fxr = createFunction(coefficients,coefficients.size(),xrCurr); //f(xr)
        double fxlfxr = getFxlFxr(fxl,fxr); //f(xl)*f(xr)
        double xrPrev = iterData[4];    //xr (OLD xr)
        double e = getE(xrCurr,xrPrev); //e
        xrPrev = xrCurr;  //update OLD xr

        if(fxlfxr < 0){ //f(xl)f(xr) < 0
            xlNew=xl;    //updated xl=xl
            xuNew=xrCurr;    //updated xu=xrCurr (NEW xr)
        } else if(fxlfxr > 0){ //f(xl)f(xr) > 0
            xlNew=xrCurr;    //xl=xrCurr (NEW xr)
            xuNew=xu;    //xu=xu
        } else {
            approxRoot = xrCurr;
            exactedRoot = true;   //f(xl)f(xri) = 0
        }

        double[] currIterData = {iterData[0],xl,xu,xrCurr,xrPrev,fxl,fxu,fxr,fxlfxr,e};
        printIterationData(currIterData,exactedRoot);   //print current iteration row

        iterData[0]++;  //increase iteration counter

        if(fxl*fxu < 0){
            //stopping criteria:
            // if iteration !> stopping Iteration && |error%| !<= stopping Error% && |f(xr)| !<= stopping f(xr)
            if(approxRoot!=xrCurr && !(iterData[0]>stopCrit[0]) && !(Math.abs(e)<=stopCrit[1]) && !(Math.abs(fxr)<=stopCrit[2])){        
                //update iteration row (Row #i)
                double[] newIterData = {iterData[0],xlNew,xuNew,xrCurr,xrPrev,fxl,fxu,fxr,fxlfxr,e};
                
                runFalsePositionAlgo(coefficients,stopCrit,newIterData);
            } else {
                approxRoot = xrCurr;
                System.out.println("\nA stopping criteria has been met--stopped iterating.");
                System.out.println("\nThe equation's root is approximately " + approxRoot); //in most cases exact root is not met hence approximation
            }
        }  else {
            System.out.println("\nCannot proceed with selected xl and xu: f(xl) * f(xu) > 0");
        }
        return;
    }

    //algorithm functions:
    public static void printIterationData(double[] iterData, boolean exactedRoot) {
        int index = 0;
        for(double column : iterData){
            if(index != 4) {
                if(iterData[0] == 1) {
                    switch(index){
                        case 0:
                            System.out.format("%-8.0f",column);
                            break;
                        case 9:
                            System.out.format("%-12s","-");
                            break;
                        default:
                            System.out.format("%-12s",column);
                            break;
                    }
                } else {
                    if(index == 0) System.out.format("%-8.0f",column);
                    else {
                        if(exactedRoot && index==9) System.out.format("%-12s","-");
                        else System.out.format("%-12s",column);
                    }
                }     
            }       
            index++;
        }
        System.out.println();
        return;
    }
    
    //BigDecimal is more precise than double
    public static double createFunction(Vector<Double> coefficients, int vectorSize, double value) {    //coeff*(value^exponent)
        BigDecimal sum = new BigDecimal("0");
        for(int x=vectorSize-1;x>=0;x--){
            BigDecimal term = new BigDecimal(Double.toString(coefficients.get(x)*Math.pow(value,x)));
            sum = sum.add(term);
        }
        return truncate(sum,5,false);
    }

    public static double getFxlFxr(double fxl, double fxr){ //conditional = fxl*fxr
        BigDecimal fxlfxr = new BigDecimal(Double.toString(fxl*fxr));
        return truncate(fxlfxr,5,false);
    }

    public static double getXr(double xl, double xu, double fxl, double fxu) {  //xr = (xu*fxl)-(xl*fxu) / (fxl-fxu)
        BigDecimal xr;
        BigDecimal numerator = new BigDecimal(Double.toString((xu*fxl)-(xl*fxu)));
        BigDecimal denominator = new BigDecimal(Double.toString((fxl-fxu)));
        if(numerator.doubleValue() == 0 || denominator.doubleValue() == 0) xr = new BigDecimal("0");   //to avoid infinity
        else xr = numerator.divide(denominator,MathContext.DECIMAL64); //limits to 16 decimal points; avoids non-terminating decimals
        return truncate(xr,5,false);
    }

    public static double getE(double xrNew, double xrOld) {   //error = |(xNew - xOld) / xNew| * 100
        BigDecimal e;
        if(xrNew !=0) {  //to avoid infinity
            BigDecimal error = new BigDecimal(Double.toString((xrNew-xrOld)/xrNew));
            e = ((error.multiply(new BigDecimal("100")))).abs();   
        } else e = new BigDecimal("0");
        return truncate(e,5,false);
    }

    public static double truncate(BigDecimal value, int trunLimit, boolean reverse){
        if(value.doubleValue() != 0) {
            if(!reverse){
                if(value.doubleValue() > 0) value = value.setScale(trunLimit,RoundingMode.FLOOR);   //FLOOR rounds to negative infinity
                else value = value.setScale(trunLimit,RoundingMode.CEILING);    //CEILING rounds to positive infinity
            } else {
                if(value.doubleValue() > 0) value = value.setScale(trunLimit,RoundingMode.CEILING);
                else value = value.setScale(trunLimit,RoundingMode.FLOOR);
            }
        }
        return value.doubleValue();
    }
}
