
package org.usfirst.frc.team3019.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team3019.robot.commands.AutonomousCommandGroup;
import org.usfirst.frc.team3019.robot.commands.Drive;
import org.usfirst.frc.team3019.robot.subsystems.AgitatorSystem;
import org.usfirst.frc.team3019.robot.subsystems.ClimberSystem;
import org.usfirst.frc.team3019.robot.subsystems.Drivetrain;
import org.usfirst.frc.team3019.robot.subsystems.PickupSystem;
import org.usfirst.frc.team3019.robot.subsystems.ShooterSystem;
import org.usfirst.frc.team3019.robot.utilities.AutonomousMode;
import org.usfirst.frc.team3019.robot.utilities.CurrentAutoCommand;
import org.usfirst.frc.team3019.robot.utilities.PickupState;
import org.usfirst.frc.team3019.robot.utilities.SystemStates;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot {

	public static SystemStates pickupPowerState = SystemStates.OFF;
	public static PickupState pickupStates = PickupState.INTAKE;
	public static CurrentAutoCommand currentAutoCommand = CurrentAutoCommand.STOP;

	public static Drivetrain driveTrain;
	public static PickupSystem pickupSystem;
	public static ShooterSystem shooterSystem;
	public static ClimberSystem climberSystem;
	public static AgitatorSystem agitatorSystem;
	public static OI oi;

	String autonomousCommandString;
	Command autonomousCommand;
	SendableChooser<String> chooser;
	SendableChooser<AutonomousMode> testChooser;

	public Robot() {
		instantiateSubsystems();
		// create the chooser with all auto options

	}

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
//		chooser = new SendableChooser<String>();
		testChooser = new SendableChooser<AutonomousMode>();
		for (AutonomousMode x : AutonomousMode.values()) {
			if(x.equals(AutonomousMode.DRIVEFWD)){
				testChooser.addDefault(x.toString(), x);
			}else{
				testChooser.addObject(x.toString(), x);
			}
			
		}
		
//		chooser.addDefault("DRIVEFWD", "DRIVEFWD");
//		chooser.addObject("RED-TENSHOT", "REDTENSHOT");
//		chooser.addObject("BLUE-TENSHOT", "BLUETENSHOT");
//		chooser.addObject("RED-GEAR", "REDGEAR");
//		chooser.addObject("BLUE-GEAR", "BLUEGEAR");
//		chooser.addObject("RED-COMBO", "REDCOMBO");
//		chooser.addObject("BLUE-COMBO", "BLUECOMBO");
		SmartDashboard.putData("autoChooser", testChooser);
		oi = new OI();
		
//		new Thread(() -> {
//			
//            UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
//            camera.setResolution(640, 480);
//            
//            CvSink cvSink = CameraServer.getInstance().getVideo();
//            CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
//            
//            Mat source = new Mat();
//            Mat output = new Mat();
//            
//            while(!Thread.interrupted()) {
//                cvSink.grabFrame(source);
//                Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
//                outputStream.putFrame(output);
//            }
//			
//        }).start();
		// chooser.addObject("My Auto", new MyAutoCommand());
		
	}

	private void instantiateSubsystems() {
		// TODO Auto-generated method stub

		driveTrain = new Drivetrain();
		pickupSystem = new PickupSystem();
		shooterSystem = new ShooterSystem();
		climberSystem = new ClimberSystem();
		agitatorSystem = new AgitatorSystem();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		
	}

	@Override
	public void disabledPeriodic() {
			if(!SmartDashboard.containsKey("autoChooser")){
				SmartDashboard.putData("autoChooser",testChooser);
			}
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = new AutonomousCommandGroup(testChooser.getSelected());
//		autonomousCommandString = chooser.getSelected();
//		switch (autonomousCommandString) {
//		case "REDTENSHOT":
//			autonomousCommand = new AutonomousCommandGroup(AutonomousMode.REDTENSHOT);
//			break;
//		case "REDGEAR":
//			autonomousCommand = new AutonomousCommandGroup(AutonomousMode.REDGEAR);
//			break;
//		case "REDCOMBO":
//			autonomousCommand = new AutonomousCommandGroup(AutonomousMode.REDCOMBO);
//			break;
//		case "BLUETENSHOT":
//			autonomousCommand = new AutonomousCommandGroup(AutonomousMode.BLUETENSHOT);
//			break;
//		case "BLUEGEAR":
//			autonomousCommand = new AutonomousCommandGroup(AutonomousMode.BLUEGEAR);
//			break;
//		case "BLUECOMBO":
//			autonomousCommand = new AutonomousCommandGroup(AutonomousMode.BLUECOMBO);
//			break;
//		default:
//			autonomousCommand = new AutonomousCommandGroup(AutonomousMode.DRIVEFWD);
//			break;
//		}

		// schedule the autonomous command (example)
		 if (autonomousCommand != null)
		 autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		if (pickupPowerState == SystemStates.ON) {
			if (pickupStates == PickupState.OUTTAKE) {
				pickupSystem.reverseMotor();
			} else {
				pickupSystem.runMotor();
			}
		} else {
			pickupSystem.stopMotor();
		}
		SmartDashboard.putNumber("drivefactor", RobotMap.DRIVE_SCALE_FACTOR);
		SmartDashboard.putNumber("shooterSpeed", RobotMap.SHOOTSPEED_SCALE_FACTOR);
		SmartDashboard.putBoolean("Joystick", oi.shooterSwitch.get());
		SmartDashboard.putString("pickupState", pickupStates.toString());
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
