// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import edu.wpi.first.wpilibj.GenericHID;
//import frc.robot.commands.Autos;
//import frc.robot.commands.ExampleCommand;
//import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class NewerOLDRobotContainer {
  public final Elevator m_elevator = new Elevator();
  public final Climb m_climb = new Climb();
  public final Intake m_intake = new Intake();
  // The robot's subsystems and commands are defined here...
  //private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  public GenericHID m_driverController1, m_driverController2, m_driverController3, m_driverController4;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public NewerOLDRobotContainer() {
    // Configure the trigger bindings
    configureBindings();
    // The driver's controller
    System.out.println("Here");
    setDriveControllers();
  }

  // functions for getting controller values
  public Boolean speedSwitch(){
    return m_driverController2.getRawButton(5);}

  public Boolean buttonExtendClimb(){
    return m_driverController3.getRawButton(1);}
  public Boolean buttonRetractClimb(){
    return m_driverController3.getRawButton(2);}
  public Boolean buttonRemoveAlgae(){
    return m_driverController3.getRawButton(3);}
  public Boolean buttonTransportPivot(){
    return m_driverController3.getRawButton(4);}
  public Boolean buttonScoreAlgae(){
    return m_driverController3.getRawButton(5);}
  public Boolean buttonCoralStationIntake(){
    return m_driverController3.getRawButton(6);}
  public Boolean switchAlgaeIntake(){
    return m_driverController3.getRawButton(7);}
  public Boolean switchExtraOnOff(){
    return m_driverController3.getRawButton(8);}
  
  public Boolean buttonL4(){
    return m_driverController4.getRawButton(1);}
  public Boolean buttonL3(){
    return m_driverController4.getRawButton(2);}
  public Boolean buttonL2(){
    return m_driverController4.getRawButton(3);}
  public Boolean buttonL1(){
    return m_driverController4.getRawButton(4);}
  public Boolean buttonResetElevator(){
    return m_driverController4.getRawButton(5);}
  public Boolean buttonCoralIntakeGround(){
    return m_driverController4.getRawButton(6);}
  public Boolean buttonReverseCoral(){
    return m_driverController4.getRawButton(7);}

  public double buttonReefPosition(){
    if (m_driverController4.getRawButton(10))
      return (m_driverController4.getRawAxis(0)+2)%3 + m_driverController4.getRawAxis(0)*3+6;
    return (m_driverController4.getRawAxis(0)+2)%3 + m_driverController4.getRawAxis(0)*3;
  }

  public int getLevel(){
    if(buttonL1()) return 1;
    if(buttonL1()) return 2;
    if(buttonL1()) return 3;
    if(buttonL1()) return 4;
    return 0;
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    /*
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    new Trigger(m_exampleSubsystem::exampleCondition)
        .onTrue(new ExampleCommand(m_exampleSubsystem));

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());*/
  }

  // Use this method to set the Controllers to their proper port values
  private void setDriveControllers() {
    GenericHID m_tempController = new GenericHID(OperatorConstants.kDriverControllerPort1);
    // // Trigger controllerIDbutton1 = m_tempController.button(11);
    // // Trigger controllerIDbutton2 = m_tempController.button(12);

    // m_tempController.button(11)
    //     .and(m_tempController.button(12))
    //     .onTrue(
    //       private final CommandGenericHID m_driverController1 =
    //           new CommandGenericHID(OperatorConstants.kDriverControllerPort1);
    //     )

    for(int i=1; i<=4; i++) {
      switch(i) {
        case 1:
          m_tempController = new GenericHID(OperatorConstants.kDriverControllerPort1);
        case 2:
          m_tempController = new GenericHID(OperatorConstants.kDriverControllerPort2);
        case 3:
          m_tempController = new GenericHID(OperatorConstants.kDriverControllerPort3);
        case 4:
          m_tempController = new GenericHID(OperatorConstants.kDriverControllerPort4);
      }

      if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == false)){
        m_driverController1 = m_tempController;
        System.out.println(m_tempController);
      }
      if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == true)){
        m_driverController2 = m_tempController;
        System.out.println(m_tempController);
      }
      if ((m_tempController.getRawButton(11) == true) && (m_tempController.getRawButton(12) == true)){
        m_driverController3 = m_tempController;
        System.out.println(m_tempController);
      }
      if ((m_tempController.getRawButton(11) == true) && (m_tempController.getRawButton(12) == false)){
        m_driverController4 = m_tempController;
        System.out.println(m_tempController);
      }

        /* 
      // button 11 and 12 OFF
      m_tempController.button(11)
        .and(m_tempController.button(12))
        .onFalse(
          public final CommandGenericHID m_driverController1 = m_tempController;
        )
      // button 12 ON
      m_tempController.button(12)
        .onTrue(
          public final CommandGenericHID m_driverController2 = m_tempController;
        )
      // button 11 and 12 ON
      m_tempController.button(11)
        .and(m_tempController.button(12))
        .onTrue(
          public final CommandGenericHID m_driverController3 = m_tempController;
        )
      // button 11 ON
      m_tempController.button(11)
        .onTrue(
          public final CommandGenericHID m_driverController4 = m_tempController;
        )*/
    }

    // getting values for different buttons
    
    // private final CommandGenericHID m_driverController1 =
    //     new CommandGenericHID(OperatorConstants.kDriverControllerPort1);
    // private final CommandGenericHID m_driverController2 =
    //     new CommandGenericHID(OperatorConstants.kDriverControllerPort2);
    // private final CommandGenericHID m_driverController3 =
    //     new CommandGenericHID(OperatorConstants.kDriverControllerPort3);
    // private final CommandGenericHID m_driverController4 =
    //     new CommandGenericHID(OperatorConstants.kDriverControllerPort4);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  /*public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(m_exampleSubsystem);
  }*/
}
