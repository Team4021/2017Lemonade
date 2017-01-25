//2017 Lemonade
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
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	final String Bekahsauto = "Bekah's Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	RobotDrive Tankdrive;
	Joystick leftstick, rightstick;
	double TankDashLeft;
	double TankDashRight;
	double SRX;
	double pdpCurrent;
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	CANTalon TalonSRX = new CANTalon (0);

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		chooser.addObject("Bekah's Auto", Bekahsauto);
		SmartDashboard.putData("Auto choices", chooser);
		Tankdrive = new RobotDrive(0,1,2,3);
		leftstick = new Joystick(1);
		rightstick = new Joystick(2);
		TankDashLeft = leftstick.getY();
		TankDashRight = rightstick.getY();
		SRX = rightstick.getX();
		CameraServer.getInstance().startAutomaticCapture();
		CameraServer.getInstance().getVideo();
		CameraServer.getInstance().putVideo("Cam", 640, 480); 
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
		case customAuto:
			// Put custom auto code here
			break;
		case defaultAuto:
		default:
			Tankdrive.tankDrive(.3, .3);
			Timer.delay(5);
			Tankdrive.tankDrive(0, 0);
			Timer.delay(3);
			Tankdrive.tankDrive(-.3, -.3);
			Timer.delay(5);
			Tankdrive.tankDrive(0, 0);
			Timer.delay(3);
			Tankdrive.tankDrive(.7, -.2);
			Timer.delay(5);
			Tankdrive.tankDrive(0, 0);			
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
		TalonSRX();
	
	}
	
	
	public void TankDrive() {
		Tankdrive.tankDrive(leftstick, rightstick);
		
	}
	
	public void UpdateDash() {
		double pdpCurrent = pdp.getCurrent(1);
		SmartDashboard.putNumber("Left Stick", TankDashLeft);
		SmartDashboard.putNumber("Right Stick", TankDashRight);
		SmartDashboard.putNumber("TalonSRX", SRX);
		SmartDashboard.putNumber("Current", pdpCurrent);
	
	}
	
	public void TalonSRX() {
		TalonSRX.set(SRX);
		
	}
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	
	}
}