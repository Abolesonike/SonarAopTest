package sonar;


import sonar.sensor.JavaFileSensor;

public class MainProgram {

    public static void main(String[] args) {
        JavaFileSensor lineSensor = new JavaFileSensor();
        lineSensor.doJavaMethod(null);
    }




}
