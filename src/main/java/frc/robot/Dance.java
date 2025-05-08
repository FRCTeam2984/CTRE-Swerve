package frc.robot;

import frc.robot.subsystems.Driver_Controller;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj2.command.Command;

public class Dance {
    public static Command m_autonomousCommand;
    public static Double timer = 0.0;
    public static Boolean resetLastPressed = false, activated = false, retractElevatorArm = true;
    //public static final int instructionNumber = 4;
    public static Instruction moves[] = {
        /*new Instruction(24.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "4 Light Flash"),
        new Instruction(25.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "blue waves"),
        new Instruction(40.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "green lines"),
        new Instruction(44.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "none"),
        new Instruction(46.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "fade blue"),
        new Instruction(49.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "flashing red"),
        new Instruction(51.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "yellow green fade"),
        new Instruction(54.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "flashing red"),
        new Instruction(57.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "light waves"),
        new Instruction(62.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "blue yellow fade"),
        new Instruction(67.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "flashing yellow"),
        new Instruction(70.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "pink"),
        new Instruction(72.5, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "pink yellow fade"),
        new Instruction(75.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "flashing red"),
        new Instruction(78.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "yellow green fade"),
        new Instruction(81.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "flashing red"),
        new Instruction(82.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "light waves"),
        new Instruction(89.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "blue green fade"),
        new Instruction(104.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "white flash"),
        new Instruction(105.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "purple waves"),
        new Instruction(115.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "flashing red"),
        new Instruction(117.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "yellow green fade"),
        new Instruction(120.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "flashing red"),
        new Instruction(122.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "light waves"),
        new Instruction(131.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "red"),
        new Instruction(132.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "flashing yellow"),
        new Instruction(133.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "blue"),
        new Instruction(134.0, "run LED pattern", 0.0, 0.0, 0.0, 0.0, "none"),*/



        new Instruction(0.0, "spin", 45.0, 50.0, 0.0, 0.0, ""), //left is positive
        new Instruction(1.0, "spin", -90.0, 98.0, 0.0, 0.0, ""),
        new Instruction(2.96, "spin", 90.0, 96.0, 0.0, 0.0, ""),
        new Instruction(4.88, "spin", -90.0, 94.0, 0.0, 0.0, ""),
        new Instruction(6.76, "spin", 90.0, 92.0, 0.0, 0.0, ""),
        new Instruction(8.6, "spin", -90.0, 90.0, 0.0, 0.0, ""),
        new Instruction(10.4, "spin", 90.0, 88.0, 0.0, 0.0, ""),
        new Instruction(12.16, "spin", -90.0, 86.0, 0.0, 0.0, ""),
        new Instruction(13.88, "spin", 90.0, 84.0, 0.0, 0.0, ""),
        new Instruction(15.56, "spin", -90.0, 82.0, 0.0, 0.0, ""),
        new Instruction(17.2, "spin", 90.0, 80.0, 0.0, 0.0, ""),
        new Instruction(18.8, "spin", -90.0, 78.0, 0.0, 0.0, ""),
        new Instruction(20.36, "spin", 90.0, 76.0, 0.0, 0.0, ""),
        new Instruction(21.88, "spin", -90.0, 74.0, 0.0, 0.0, ""),
        new Instruction(23.36, "spin", 45.0, 32.0, 0.0, 0.0, ""),
        
        new Instruction(25.0, "run path", 0.0, 0.0, 0.0, 0.0, "Shark Attack A"),
        
        /*new Instruction(25.0, "move", 0.0, 0.0, 0.5, 0.0, ""),
        new Instruction(25.5, "move", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(26.0, "move", 0.0, 0.0, -0.5, 0.0, ""),
        new Instruction(26.5, "move", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(27.0, "move", 0.0, 0.0, 0.0, 0.5, ""),
        new Instruction(27.5, "move", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(28.0, "move", 0.0, 0.0, 0.0, -0.5, ""),
        new Instruction(28.5, "move", 0.0, 0.0, 0.0, 0.0, ""),

        new Instruction(29.0, "move", 0.0, 0.0, 0.9, 0.0, ""),
        new Instruction(30.0, "circle", 0.9, 150.0, 0.0, 0.0, ""),
        new Instruction(33.0, "move", 0.0, 0.0, 0.0, 0.0, ""),*/
        new Instruction(33.0, "elevator", Elevator.levelPosition[2], 0.0, 0.0, 0.0, ""),
        new Instruction(33.0, "ground intake", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(35.0, "retract", 0.0, 0.0, 0.0, 0.0, ""),
        //new Instruction(35.5, "move", 0.0, 0.0, 2.0, 0.0, ""),

        new Instruction(38.0, "ground intake", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(38.5, "retract", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(39.0, "ground intake", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(39.5, "retract", 0.0, 0.0, 0.0, 0.0, ""),
        //new Instruction(40.0, "set speed", 0.8, 0.0, -2.0, 0.0, ""),
        //new Instruction(40.0, "move", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(40.0, "ground intake", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(40.5, "retract", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(41.0, "ground intake", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(41.5, "retract", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(42.0, "ground intake", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(42.5, "retract", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(43.0, "ground intake", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(43.5, "retract", 0.0, 0.0, 0.0, 0.0, ""),
        /*new Instruction(44.0, "set speed", 3.0, 0.0, 0.0, 0.0, ""),
        new Instruction(46.0, "spin", -45.0, 25.0, 0.0, 0.0, ""),
        new Instruction(46.25, "spin", 90.0, 50.0, 0.0, 0.0, ""),
        new Instruction(46.75, "spin", -90.0, 50.0, 0.0, 0.0, ""),
        new Instruction(47.25, "spin", 90.0, 50.0, 0.0, 0.0, ""),
        new Instruction(47.75, "spin", -90.0, 50.0, 0.0, 0.0, ""),
        new Instruction(48.25, "spin", 90.0, 50.0, 0.0, 0.0, ""),
        new Instruction(48.75, "spin", -45.0, 25.0, 0.0, 0.0, ""),

        new Instruction(49.0, "move", 0.0, 0.0, 2.0, 0.0, ""),*/
        new Instruction(49.0, "ground intake", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(49.5, "retract", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(50.0, "ground intake", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(50.5, "retract", 0.0, 0.0, 0.0, 0.0, ""),
        //new Instruction(51.0, "move", 0.0, 0.0, -2.0, 0.0, ""),
        //new Instruction(54.0, "set speed", 3.0, 0.0, 0.0, 0.0, ""),
        //new Instruction(54.0, "move", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(54.0, "ground intake", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(54.5, "retract", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(55.0, "ground intake", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(55.5, "retract", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(56.0, "ground intake", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(56.5, "retract", 0.0, 0.0, 0.0, 0.0, ""),
        
        //new Instruction(57.0, "spin", 180.0, 100.0, 0.0, 0.0, ""),
        //new Instruction(58.0, "move", 0.0, 0.0, -2.0, 0.0, ""),
        new Instruction(58.0, "ground intake", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(58.5, "retract", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(59.0, "ground intake", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(59.5, "retract", 0.0, 0.0, 0.0, 0.0, ""),

        new Instruction(60.5, "ground intake", 0.0, 0.0, 0.0, 0.0, ""),
        new Instruction(61.0, "retract", 0.0, 0.0, 0.0, 0.0, ""),
        
        new Instruction(62.0, "elevator", Elevator.levelPosition[3], 0.0, 0.0, 0.0, ""),
        new Instruction(63.26, "elevator", Elevator.levelPosition[2], 0.0, 0.0, 0.0, ""),
        new Instruction(64.5, "elevator", Elevator.levelPosition[3], 0.0, 0.0, 0.0, ""),
        new Instruction(65.76, "elevator", Elevator.levelPosition[2], 0.0, 0.0, 0.0, ""),
        
        new Instruction(70.0, "toggle elevator arm", 0.0, 0.0, 0.0, 0.0, ""),

        new Instruction(-1.0, "stop", 0.0, 0.0, 0.0, 0.0, "")
    };
    public static Double currentX, currentY, desiredAngle = 0.0, currentElevator = 0.0, speed = 2.1;
    public static Double Xoffset = 0.0, Yoffset = 0.0, angleOffset = 0.0;
    public static String intakeState = "ground intake";

    public static void dance(){
        Double angle = ((RobotContainer.drivetrain.getPigeon2().getYaw().getValueAsDouble() + 360*1000 + 180)%360) - 180;
        if (resetLastPressed == false && Driver_Controller.buttonAutoDrive()){
            timer = 60.0;
            activated = true;
        }
        resetLastPressed = Driver_Controller.buttonAutoDrive();
        if (activated){
        for (int i = 0; moves[i].action != "stop"; ++i){
            Instruction cur = moves[i];
            switch(cur.action){
                /*case "move":
                    if (Math.abs(timer-cur.time) <= 0.01){
                        currentX = cur.x;
                        currentY = cur.y;
                    }
                    break;*/
                case "spin":
                    if (timer >= cur.time && timer <= (cur.time + 0.021)){
                        desiredAngle = angle - angleOffset;
                    }
                    if (timer >= cur.time && timer <= (cur.time + (cur.amplifier2/50))){
                        //desiredAngle += (cur.amplifier1-desiredAngle)/(50*(timer-(cur.time+(cur.amplifier2/50))));
                        desiredAngle += (cur.amplifier1/cur.amplifier2);
                        //System.out.println(cur.amplifier1/cur.amplifier2);
                        //System.out.println(desiredAngle);
                    }
                    break;
                /*case "circle":
                    if (timer >= cur.time && timer <= (cur.time + (cur.amplifier2/50))){
                        Double driveAngle = Math.atan2(currentY - cur.y, currentX - cur.x);
                        driveAngle += (Math.PI*360.0/cur.amplifier2)/180;
                        currentX = cur.x+cur.amplifier1*Math.cos(driveAngle);
                        currentY = cur.y+cur.amplifier1*Math.sin(driveAngle);
                        desiredAngle += 360.0/cur.amplifier2;
                    }
                    break;*/
                case "run LED pattern":
                    //if (timer-cur.time <= 0.01 && timer-cur.time > -0.01)LED.runLED(cur.amplifier3);
                    break;
                case "run path":
                    if (Math.abs(timer-cur.time) <= 0.011 && Driver_Controller.buttonDanceMove()){
                        m_autonomousCommand = RobotContainer.startPathplannerMove(cur.amplifier3);
                        m_autonomousCommand.schedule();
                    }
                    break;
                case "set speed":
                    if (Math.abs(timer-cur.time) <= 0.01)speed = cur.amplifier1;
                    break;
                case "elevator":
                    if (Math.abs(timer-cur.time) <= 0.01)currentElevator = cur.amplifier1;
                    break;
                case "reset intake":
                    if (Math.abs(timer-cur.time) <= 0.01)intakeState = "reset intake";
                    break;
                case "retract":
                    if (Math.abs(timer-cur.time) <= 0.01)intakeState = "retract";
                    break;
                case "ground intake":
                if (Math.abs(timer-cur.time) <= 0.01){
                    intakeState = "ground intake";
                }
                    break;
                case "toggle elevator arm":
                    if (Math.abs(timer-cur.time) <= 0.01)retractElevatorArm = !retractElevatorArm;
                    break;
            
            }
        }
        if (true){
        if (currentElevator > 0.0){
            Elevator.elevatorTo(currentElevator);
        }else{
            if (Double.parseDouble(Elevator.elevatorMotor.getRotorPosition().toString().substring(0, 10)) > 3 && Driver_Controller.buttonResetElevator()) Elevator.elevatorTo(-99999.0);
            else Elevator.elevatorMotor.set(0.0);
        }

        intakeState = "MY PRECIOUSSSSSSSSSSS";
        switch(intakeState){
            case "reset intake": 
            if (Intake.outsideSwitch.isPressed()) Intake.intakePivot.set(0);
            else Intake.intakePivot.set(-0.3);
            Intake.intakeLastUsed = 'D';
            break;
            case "ground intake": Intake.intakeCoral(Driver_Controller.buttonResetIntake()); break;
            case "station intake": Intake.intakeTo("stationIntakeCoral"); Intake.spinRollers(0.35); break;
            case "intake algae": Intake.intakeTo("intakeAlgae"); break;
            case "retract": Intake.retractIntake(); break;
        } 
        }
        
        
        
        if (!retractElevatorArm){
            if (Elevator.extendedOrRetracted != "extended") Elevator.moveElevatorArm("extend");
            else Elevator.armMotor.set(ControlMode.PercentOutput, 0.0);
        }else{
            if (Elevator.extendedOrRetracted != "retracted") Elevator.moveElevatorArm("retract");
            else Elevator.armMotor.set(ControlMode.PercentOutput, 0.0);
        }
        timer += 0.02;
        }
        else{
            if (Driver_Controller.buttonResetElevator()){
                //if (Double.parseDouble(Elevator.elevatorMotor.getRotorPosition().toString().substring(0, 10)) > 3)
                Elevator.elevatorTo(-999999.0);
            }else{
                Elevator.elevatorMotor.set(0.0);
            }
        }
        //System.out.println(desiredAngle);
        //System.out.println(currentX);
        //System.out.println(currentY);
        //if (Driver_Controller.buttonDanceMove() == false || timer > 35.0) AutoDriveFinal.driveToXYA(0.0, 0.0, angle, 0.0);
        //else AutoDriveFinal.driveToXYA(currentX - Xoffset, currentY - Yoffset, desiredAngle - angleOffset, speed/2);
    }
}
