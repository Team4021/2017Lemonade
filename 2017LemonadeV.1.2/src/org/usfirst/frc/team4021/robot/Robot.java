//2017 Lemonade Test
package org.usfirst.frc.team4021.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import com.ctre.CANTalon;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Encoder;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot {
	final String rightAuto = "Right Auto";
	final String middleAuto = "Middle Auto";
	final String leftAuto = "Left Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	
	RobotDrive Tankdrive;
	VictorSP frontLeft, frontRight, rearLeft, rearRight;
	Joystick leftstick, rightstick;
	double TankDashLeft;
	double TankDashRight;
	double PrecisionLeft;
	double PrecisionRight;
	
	//CANTalon RopeClimber = new CANTalon (0);
	double SRX;
	double pdpCurrent;
	PowerDistributionPanel pdp = new PowerDistributionPanel();

	//UsbCamera Cam0;
	//UsbCamera Cam1;
	
	//Encoder encoder;
	//Talon encoderMotor;
	boolean autoFirst;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	
	@Override
	public void robotInit() {
		chooser.addDefault("Left Auto", leftAuto);
		chooser.addObject("Middle Auto", middleAuto);
		chooser.addObject("Right Auto", rightAuto);
		SmartDashboard.putData("Auto choices", chooser);
		
    	frontLeft = new VictorSP(0);
    	frontRight = new VictorSP(1);
    	rearLeft = new VictorSP(2);
    	rearRight = new VictorSP(3);
		Tankdrive = new RobotDrive(frontLeft, frontRight, rearLeft, rearRight);
		
		leftstick = new Joystick(1);
		rightstick = new Joystick(2);
		
		//Cam0 = CameraServer.getInstance().startAutomaticCapture(0);
		//Cam1 = CameraServer.getInstance().startAutomaticCapture(1);	
		/*
		encoderMotor = new Talon(0);
		encoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
		encoder.setMaxPeriod(.1);
		encoder.setMinRate(10);
		encoder.setDistancePerPulse(5);
		encoder.setReverseDirection(true);
		encoder.setSamplesToAverage(7);
		encoder.reset();
		*/

	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		autoFirst = true;
		autoSelected = chooser.getSelected();
		autoSelected = SmartDashboard.getString("Auto Selector",autoSelected);
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	
	@Override
	public void autonomousPeriodic() {
		switch (autoSelected) {
		case leftAuto:
			if(autoFirst == false){
				Tankdrive.setSafetyEnabled(true);
				Timer.delay(.5);
				Tankdrive.tankDrive(0.75, 0.75);
				Timer.delay(1.15);
				Tankdrive.tankDrive(.8, -.8);
				Timer.delay(.25);
				Tankdrive.tankDrive(.5, .5);
				Timer.delay(.4);
				Tankdrive.tankDrive(0,0);
				autoFirst = false;
				Tankdrive.setSafetyEnabled(true);
			}
			break;
		case middleAuto:
		default:
			if(autoFirst == true){
				Tankdrive.setSafetyEnabled(false);
				Timer.delay(0.5);
				Tankdrive.tankDrive(0.6, 0.6);
				Timer.delay(1.3);
				Tankdrive.tankDrive(0, 0);

				
				
				autoFirst = false;
				Tankdrive.setSafetyEnabled(true);
			}
			break;
		case rightAuto:
			if(autoFirst == true){
				Tankdrive.setSafetyEnabled(false);
				Timer.delay(.5);
				Tankdrive.tankDrive(0.75, 0.75);
				Timer.delay(1.15);
				Tankdrive.tankDrive(-.8, .8);
				Timer.delay(.25);
				Tankdrive.tankDrive(.5, .5);
				Timer.delay(.4);
				Tankdrive.tankDrive(0,0);
				autoFirst = false;
				Tankdrive.setSafetyEnabled(true);
			}
			break;
			}
		}

	/**
	 * This function is called periodically during operator control
	 */
	
	@Override
	public void teleopPeriodic() {
		TankDrive();
		UpdateDash();
		//RopeClimb();
		//EncoderTest();

	}
	
	public void TankDrive() {
		while (leftstick.getRawButton(1) | rightstick.getRawButton(1)) {
			PrecisionLeft = leftstick.getY() * 0.5;
			PrecisionRight = rightstick.getY() * 0.5;
			Tankdrive.tankDrive(PrecisionLeft, PrecisionRight);
			UpdateDash();
		}
		Tankdrive.tankDrive(leftstick, rightstick);
	}
	
	public void UpdateDash() {
		TankDashLeft = leftstick.getY();
		TankDashRight = rightstick.getY();
		SmartDashboard.putNumber("Left Stick", TankDashLeft * -1);
		SmartDashboard.putNumber("Right Stick", TankDashRight * -1);
		SmartDashboard.putNumber("Precision Left", TankDashLeft * -0.5);
		SmartDashboard.putNumber("Precision Right", TankDashRight * -0.5);
		SmartDashboard.putNumber("TalonSRX", SRX);	
		SmartDashboard.putBoolean("Left Trigger", rightstick.getRawButton(1));
		SmartDashboard.putBoolean("Right Trigger", leftstick.getRawButton(1));
		/*
		SmartDashboard.putNumber("Count", encoder.get());
		SmartDashboard.putNumber("Distance", encoder.getDistance());
		SmartDashboard.putNumber("Raw", encoder.getRaw());
		SmartDashboard.putNumber("Period", encoder.getPeriod());
		SmartDashboard.putNumber("Rate", encoder.getRate());
		SmartDashboard.putBoolean("Direction", encoder.getDirection());
		SmartDashboard.putBoolean("Stopped", encoder.getStopped());
		*/
		SmartDashboard.putNumber("Current", pdpCurrent);	
	}
	/*
	public void RopeClimb() {
	  while(leftstick.getRawButton(5)){
			RopeClimber.set(.5);
			UpdateDash();
	  }
	  RopeClimber.set(0);
	  UpdateDash();
	}
	
	public void EncoderTest() {
		while (leftstick.getRawButton(1) && encoder.get() > -2000) {
			encoderMotor.set(1);
			UpdateDash();
		}
		while (rightstick.getRawButton(1) && encoder.get() < 2000) {
			encoderMotor.set(-1);
			UpdateDash();	
		}
			encoderMotor.set(0);
			UpdateDash();	
	}
	*/
	/**
	 * This function is called periodically during test mode
	 */
	
	@Override
	public void testPeriodic() {
	
	}
}