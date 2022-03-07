// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

//Imported Libraries 
//Do not remove
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;


//Do not remove
public class Robot extends TimedRobot{

//DriveTrain Motor Controllers
//Do Not Remove

//Left Motor Controller Group
MotorController m_frontLeft = new PWMSparkMax(1);
MotorController m_rearLeft = new PWMSparkMax(2);
MotorControllerGroup m_left = new MotorControllerGroup(m_frontLeft, m_rearLeft);

//Right Motor Controller Group
MotorController m_frontRight = new PWMSparkMax(7);
MotorController m_rearRight = new PWMSparkMax(8);
MotorControllerGroup m_right = new MotorControllerGroup(m_frontRight, m_rearRight);

//Drivetrain Configuration 
DifferentialDrive m_drive = new DifferentialDrive(m_left, m_right);

//Controller Configuration
XboxController m_stick = new XboxController(0);
XboxController liftControl = new XboxController(1);

//Subsystem Motor Controllers
PWMSparkMax arm = new PWMSparkMax(5);
PWMSparkMax intake = new PWMSparkMax(6);
PWMSparkMax lift = new PWMSparkMax(7);


  //Constants for controlling the arm. NEED TO BE TUNED.
  final double armHoldUp = 0.08;
  final double armHoldDown = 0.13;
  final double armTravel = 0.5;

  final double armTimeUp = 0.5;
  final double armTimeDown = 0.35;

  //Varibles needed for the code
  boolean armUp = true; //Arm initialized to up for the start of the match. 
  boolean burstMode = false;
  double lastBurstTime = 0;
  double autoStart = 0;
  boolean goForAuto = false;



  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
      m_right.setInverted(true);

      //Configure motors to turn correct direction. WE MAY NEED TO CHANGE OUR INVERTED MOTORS. 
      m_frontLeft.setInverted(true);
      m_rearLeft.setInverted(true);
      m_frontRight.setInverted(false);
      m_rearRight.setInverted(false);
      

      //Arm must always start up
      arm.setInverted(false);
  
  }

 
  public void teleopPeriodic() {
    // Drive with arcade drive. 
    m_drive.arcadeDrive(-m_stick.getLeftY(), m_stick.getLeftX());

    // Intake Controls

    //If RS is pressed... spin the motor at full speed clockwise
    if(m_stick.getRawButtonPressed(10)){
      intake.set(1);;
    } 
    //If LS is pressed... spin the motor at full speed counterclockwise 
    else if(m_stick.getRawButtonPressed(9)){
      intake.set(-1);
    }
    //If neither trigger is pressed... motor won't spin 
    else{
      intake.set(0);
    }

    // Arm Controls

    //If the arm is down... go up
    if(armUp){
      if(Timer.getFPGATimestamp() - lastBurstTime < armTimeUp){
        arm.set(armTravel);
      }
      //If the arm is up... stay up
      else{
        arm.set(armHoldUp);
      }
    }
      //If the arm is up... go down
    else{
      if(Timer.getFPGATimestamp() - lastBurstTime < armTimeDown){
        arm.set(-armTravel);
      }
      //If the arm is down... stay down
      else{
        arm.set(-armHoldDown);
      }
    }
      //If RB is pressed... Arm up command runs
    if(m_stick.getRawButtonPressed(5) && !armUp){
      lastBurstTime = Timer.getFPGATimestamp();
      armUp = true;
    }
      //If LB is pressed... Arm down command runs
    else if(m_stick.getRawButtonPressed(4) && armUp){
      lastBurstTime = Timer.getFPGATimestamp();
      armUp = false;
    }  


    // Lift Controls

    //If RS is pressed... lift at full speed 
    if(liftControl.getRawButtonPressed(10)){
      lift.set(1);;
    }
    //If LS is pressed... lower at full speed
    else if (liftControl.getRawButtonPressed(9)){
      lift.set(-1);
    }
    //If neither is pressed... do nothing 
    else{
      lift.set(0);
    }
  }
}
