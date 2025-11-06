package frc.robot;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.Driver_Controller;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.NewAutoDrive;

public class Triggers extends Command{
    static BooleanSupplier elevatorCheck = () -> false;
    static BooleanSupplier elevatorCheck1 = () -> false;
    public static Command Score1 = Commands.run(() -> Score1()).until(elevatorCheck);
    public static Command Score2 = Commands.run(() -> Score2()).until(elevatorCheck1);
    static boolean test = false;
    static Double outtakeTimer = 0.0, lastPosition = -1000.0;
    static int targetLevel = 2;

    public static void Score1(){
        Elevator.currentLevel = targetLevel;
        if ((Elevator.currentPosition-lastPosition) < 0.01 &&//maybe change
        Math.abs(Elevator.currentPosition-Elevator.levelPosition[targetLevel]) < Elevator.maxError){//} && NewAutoDrive.driveToXYA(NewAutoDrive.scoringPosRed[10][0], NewAutoDrive.scoringPosRed[10][1], NewAutoDrive.scoringAngles[10]+180, 0.5)){//check if elevator is basically stopped and at l4
            elevatorCheck = () -> true;
        }
        /*if(Intake.insideSwitch.isPressed()){
            System.out.println("ending");
            elevatorCheck1 = () -> true;
             elevatorCheck = () -> false;
             
        } else{
            System.out.println(Intake.insideSwitch.isPressed());
        elevatorCheck = () -> true;}*/
    }

    public static void Score2(){
        //Double outtakeTime = 0.5;
        //outtakeTimer += 0.02;
        //if (outtakeTimer > outtakeTime){
        //    Elevator.outtakeMotor.set(0.0);
        //    Elevator.currentLevel = 0;
        //    outtakeTimer = 0.0;
        //    lastPosition = -1000.0;
        //    elevatorCheck1 = () -> true;
        //}else{
        //    Elevator.outtakeMotor.set(0.8);
        //    lastPosition = Elevator.currentPosition;
        //}
        System.out.println("score!");
    }

    public static Command move(){
        return Score1;
    }
    public static Command move1(){
        return Score2;
    }
}
