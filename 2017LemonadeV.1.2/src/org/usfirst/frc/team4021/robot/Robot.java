//2017 Lemonade Master
package org.usfirst.frc.team4021.robot;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import com.ctre.CANTalon;
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
	final String leftAuto = "Left Auto";
	final String middleAuto = "Middle Auto";
	final String rightAuto = "Right Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	RobotDrive Tankdrive;
	VictorSP frontLeft, frontRight, rearLeft, rearRight;
	Joystick leftstick, rightstick;
	CANTalon RopeClimber = new CANTalon (0);
	double SRX;
	double pdpCurrent;
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	UsbCamera Cam0;
	UsbCamera Cam1;
	Encoder encoder;
	boolean autoFirst;
	double finalLeft;
	double finalRight;
	boolean holdRope;
	

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
    	rearRight = new VictorSP(1);
    	frontRight = new VictorSP(2);
    	rearLeft = new VictorSP(3);
		Tankdrive = new RobotDrive(frontLeft, frontRight, rearLeft, rearRight);
		rightstick = new Joystick(2);
		leftstick = new Joystick(1);	
		Cam0 = CameraServer.getInstance().startAutomaticCapture(0);
		Cam1 = CameraServer.getInstance().startAutomaticCapture(1);	
		holdRope = false;
		
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
			if(autoFirst == true){
				Tankdrive.setSafetyEnabled(false);
				Timer.delay(0.5);
				Tankdrive.tankDrive(.5, .5);
				Timer.delay(2.6);
				Tankdrive.tankDrive(.5, -.5);
				Timer.delay(.5);
				Tankdrive.tankDrive(.5, .5);
				Timer.delay(0.5);
				Tankdrive.tankDrive(0, 0);
				autoFirst = false;
				Tankdrive.setSafetyEnabled(true);
			}
			break;
		case middleAuto:
		default:
			if(autoFirst == true){
				Tankdrive.setSafetyEnabled(false);
				Timer.delay(0.5);
				Tankdrive.tankDrive(0.5, 0.5);
				Timer.delay(1.7);
				Tankdrive.tankDrive(0, 0);
				autoFirst = false;
				Tankdrive.setSafetyEnabled(true);
			}
			break;
		case rightAuto:
			if(autoFirst == true){
				Tankdrive.setSafetyEnabled(false);
				Timer.delay(0.5);
				Tankdrive.tankDrive(.5, .5);
				Timer.delay(2.6);
				Tankdrive.tankDrive(-.5, .5);
				Timer.delay(.5);
				Tankdrive.tankDrive(.5, .5);
				Timer.delay(0.5);
				Tankdrive.tankDrive(0, 0);
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
		RopeClimb();
		System.out.println(holdRope);
		if(holdRope == true){
			RopeClimber.set(0.3);
		}
	}
	
	public void TankDrive() {
		finalLeft = leftstick.getY() * -1;
		finalRight = rightstick.getY() * -1;
		while (leftstick.getRawButton(1) | rightstick.getRawButton(1)) {
			finalLeft = leftstick.getY() * -0.5;
			finalRight = rightstick.getY() * -0.5;
			Tankdrive.tankDrive(finalLeft, finalRight);
			UpdateDash();
		}
		while (rightstick.getRawButton(2)) {
			finalLeft = leftstick.getY();
			finalRight = rightstick.getY();
			Tankdrive.tankDrive(finalLeft, finalRight);
			UpdateDash();
		}
		Tankdrive.tankDrive(finalLeft, finalRight);
	}
	
	public void UpdateDash() {
		double pdpCurrent = pdp.getCurrent(1);
		SmartDashboard.putNumber("TalonSRX", SRX);
		SmartDashboard.putNumber("Current", pdpCurrent);
		SmartDashboard.putNumber("Left Stick", finalLeft);
		SmartDashboard.putNumber("Right Stick", finalRight);
		SmartDashboard.putBoolean("Rope Drive", rightstick.getRawButton(2));
		SmartDashboard.putBoolean("Precision Driving", leftstick.getRawButton(1) | rightstick.getRawButton(1));
		
	}
	
	public void RopeClimb() {
		while(rightstick.getRawButton(5)){
			RopeClimber.set(.8);
			UpdateDash();
	  }
		while(rightstick.getRawButton(6)){
			RopeClimber.set(-.8);
			UpdateDash();
		}
		if (rightstick.getRawButton(12)){
			holdRope = true;
		}
	  RopeClimber.set(0);
	  UpdateDash();
	}
	

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	
	}
}