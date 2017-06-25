//2017 Lemonade Testhi
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
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	final String leftAuto = "Left Side";
	final String middleAuto = "Middle";
	final String rightAuto = "Right Side";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	RobotDrive tankDrive;
	VictorSP frontLeft, frontRight, rearLeft, rearRight, ropeClimb1, ropeClimb2;
	Joystick xBox;
	double TankDashLeft;
	double TankDashRight;
	double pdpCurrent;
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	UsbCamera Cam0;
	UsbCamera Cam1;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser.addDefault("Left Side", leftAuto);
		chooser.addObject("Middle", middleAuto);
		chooser.addObject("Right Side", rightAuto);
		SmartDashboard.putData("Auto choices", chooser);
    	frontLeft = new VictorSP(0);   
    	rearRight = new VictorSP(1);
    	frontRight = new VictorSP(2);  
    	rearLeft = new VictorSP(3);    
    	ropeClimb1 = new VictorSP(4);
    	ropeClimb2 = new VictorSP(5);
		tankDrive = new RobotDrive(frontLeft, frontRight, rearLeft, rearRight);
		xBox = new Joystick(1);
		
		Cam0 = CameraServer.getInstance().startAutomaticCapture(0);
		Cam1 = CameraServer.getInstance().startAutomaticCapture(1);	}

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
		autoSelected = chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		switch (autoSelected) {
		case leftAuto:
			// Put auto code here
			break;
		case middleAuto:
		default:
			// Put auto code here		
			break;
		case rightAuto:
			// Put auto code here		
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
	
	}
	
	
	public void TankDrive() {
		tankDrive.arcadeDrive(xBox);
		
	}
	
	public void UpdateDash() {
		double pdpCurrent = pdp.getCurrent(1);
		SmartDashboard.putNumber("Left Stick", TankDashLeft);
		SmartDashboard.putNumber("Right Stick", TankDashRight);
		SmartDashboard.putNumber("Current", pdpCurrent);
	
	}
	
	public void RopeClimb() {
	  while(xBox.getRawButton(1)){
		  ropeClimb1.set(1);
		  ropeClimb2.set(1);
	  }
	  ropeClimb1.set(0);
	  ropeClimb2.set(0);
	}
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	
	}
}