package frc.robot;

public class Instruction {
    public Double time;
    public String action = "none", amplifier3;
    public Double x, y, amplifier1, amplifier2;
    public Instruction(Double t, String s, Double a1, Double a2, Double X, Double Y, String str){
        time = t;
        action = s;
        amplifier1 = a1;
        amplifier2 = a2;
        x = X;
        y = Y;
        amplifier3 = str;
    }
}