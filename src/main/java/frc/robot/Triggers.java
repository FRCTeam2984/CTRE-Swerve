package frc.robot;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.Driver_Controller;
import frc.robot.subsystems.Intake;

public class Triggers extends Command{
    static BooleanSupplier elevatorCheck = () -> true;
    static BooleanSupplier elevatorCheck1 = () -> true;
    public static Command Score1 = Commands.run(() -> Score1()).until(elevatorCheck);
    public static Command Score2 = Commands.run(() -> Score2()).until(elevatorCheck1);
    static boolean test = false;

    public static void Score1(){
        
        if(Intake.insideSwitch.isPressed()){
            System.out.println("ending");
            elevatorCheck1 = () -> true;
             elevatorCheck = () -> false;
             
        } else{
            System.out.println(Intake.insideSwitch.isPressed());
        elevatorCheck = () -> true;}
        // suspend command until reason is true
        // lift elevator until it reaches L3
    }

    public static void Score2(){
        System.out.println("score!");

        elevatorCheck1 = () -> false;

    }

    public static Command move(){
        return Score1;
    }
    public static Command move1(){
        return Score2;
    }
}
