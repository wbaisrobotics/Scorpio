/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4338.robot;

import org.usfirst.frc.team4338.robot.autoCommands.AutoConstants;
import org.usfirst.frc.team4338.robot.autoCommands.AutoStraight;
import org.usfirst.frc.team4338.robot.autoCommands.sw.CenterSwitchLeft;
import org.usfirst.frc.team4338.robot.autoCommands.sw.CenterSwitchRight;
import org.usfirst.frc.team4338.robot.autoCommands.sw.SideSwitch;
import org.usfirst.frc.team4338.robot.autoCommands.sw.SideSwitchOtherSide;
import org.usfirst.frc.team4338.robot.autoCommands.sw.StraightSwitch;
import org.usfirst.frc.team4338.robot.autonomousData.GameInfo;
import org.usfirst.frc.team4338.robot.autonomousData.StartingPosition;
import org.usfirst.frc.team4338.robot.autonomousData.Target;
import org.usfirst.frc.team4338.robot.systems.Climber;
import org.usfirst.frc.team4338.robot.systems.Elevator;
import org.usfirst.frc.team4338.robot.systems.Fork;
import org.usfirst.frc.team4338.robot.systems.Intake;
import org.usfirst.frc.team4338.robot.systems.SensorDrive;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {

	public enum DIOWiring {

		ELEVATOR_ENCODER_A(3),
		ELEVATOR_ENCODER_B(4),
		FORK_RETRACTED_SW(1),
		DRIVE_LEFT_B(5),
		DRIVE_LEFT_A(6),
		DRIVE_RIGHT_A(8),
		DRIVE_RIGHT_B(7),
		INTAKE_SW(9), 
		TEAMMATE_LIFTER_LEFT_SW(2),
		TEAMMATE_LIFTER_RIGHT_SW(20),
		ELEVATOR_BOTTOM_SW(18),
		FORK_EXTENDED_SW(0);

		private int m_port;
		private DIOWiring (int port) {
			this.m_port = port;
		}

	}

	public enum CANWiring {

		DRIVE_FIRST_LEFT (2),
		INTAKE_LEFT (3), 
		ELEVATOR (4),
		CLIMBER_LEFT (5),
		FORK (6),
		CLIMBER_RIGHT (7),
		INTAKE_RIGHT (8),
		DRIVE_SECOND_LEFT (9),
		DRIVE_SECOND_RIGHT (10),
		DRIVE_FIRST_RIGHT (11);

		private int m_port;
		private CANWiring (int port) {
			this.m_port = port;
		}
	}

	public enum PCMWiring {

		DRIVE_B (0), DRIVE_A (7),
		INTAKE_A(1), INTAKE_B(6);

		private int m_port;
		private PCMWiring (int port) {
			this.m_port = port;
		}
	}

	private GameInfo m_gameInfo;
	private StartingPosition m_startPos;
	private Target m_autonomousTarget;	

	private SendableChooser<StartingPosition> m_startingPositionChooser = new SendableChooser<>();
	private SendableChooser<Target> m_targetLocationChooser = new SendableChooser<>();
	private Command autoProgram;

	private SensorDrive drive;
	private Elevator elevator;
	private Intake intake;
	private Fork fork;
	private Climber climber;

	private XboxController pilot;
	private XboxController copilot;

	private static long startTime;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {

		initializeCameraConfig();

		drive = new SensorDrive (
				new WPI_TalonSRX (CANWiring.DRIVE_FIRST_LEFT.m_port), new WPI_TalonSRX (CANWiring.DRIVE_SECOND_LEFT.m_port) ,
				new WPI_TalonSRX (CANWiring.DRIVE_FIRST_RIGHT.m_port), new WPI_TalonSRX (CANWiring.DRIVE_SECOND_RIGHT.m_port), 
				new DoubleSolenoid (PCMWiring.DRIVE_A.m_port, PCMWiring.DRIVE_B.m_port),
				new Encoder (DIOWiring.DRIVE_LEFT_A.m_port, DIOWiring.DRIVE_LEFT_B.m_port),
				new Encoder (DIOWiring.DRIVE_RIGHT_A.m_port, DIOWiring.DRIVE_RIGHT_B.m_port)
				);

		elevator = new Elevator (
				new WPI_TalonSRX(CANWiring.ELEVATOR.m_port), 
				new Encoder (DIOWiring.ELEVATOR_ENCODER_A.m_port, DIOWiring.ELEVATOR_ENCODER_B.m_port),
				new DigitalInput (DIOWiring.ELEVATOR_BOTTOM_SW.m_port)
				);

		intake = new Intake (
				new WPI_TalonSRX(CANWiring.INTAKE_LEFT.m_port),
				new WPI_TalonSRX(CANWiring.INTAKE_RIGHT.m_port),
				new DoubleSolenoid(PCMWiring.INTAKE_A.m_port, PCMWiring.INTAKE_B.m_port));

		fork = new Fork (
				new WPI_TalonSRX(CANWiring.FORK.m_port),
				new DigitalInput (DIOWiring.FORK_EXTENDED_SW.m_port),
				new DigitalInput (DIOWiring.FORK_RETRACTED_SW.m_port)
				);

		climber = new Climber (
				new WPI_TalonSRX (CANWiring.CLIMBER_LEFT.m_port),
				new WPI_TalonSRX (CANWiring.CLIMBER_RIGHT.m_port)
				);

		pilot = new XboxController (0);
		copilot = new XboxController (1);

		m_startingPositionChooser.addDefault("Left Side", StartingPosition.LEFT_SIDE);
		m_startingPositionChooser.addDefault("Left Switch", StartingPosition.LEFT_SWITCH);
		m_startingPositionChooser.addObject("Center", StartingPosition.CENTER);
		m_startingPositionChooser.addObject("Right Switch", StartingPosition.RIGHT_SWITCH);
		m_startingPositionChooser.addObject("Right Side", StartingPosition.RIGHT_SIDE);
		SmartDashboard.putData("Starting Position", m_startingPositionChooser);

		m_targetLocationChooser.addDefault("Auto Line", Target.AUTO_LINE);
		m_targetLocationChooser.addObject("Switch", Target.OUR_SWITCH);
		m_targetLocationChooser.addObject("Scale", Target.SCALE);
		SmartDashboard.putData("Target Autonomous", m_targetLocationChooser);

		SmartDashboard.putBoolean("Elevator Coast", false);
		SmartDashboard.putBoolean("Override Elevator Encoder Bottom", false);
		SmartDashboard.putBoolean("Zero Elevator", false);

	}

	private void resetMode () {
		intake.closeArms();
		drive.resetEncoders();
		drive.resetGyro();
	}

	/**
	 * Choses which autonomous to schedule 
	 */
	public void autonomousInit() {

		m_gameInfo = GameInfo.fromGameMessage(DriverStation.getInstance().getGameSpecificMessage());
		System.out.println("Receieved game message: \n\t" + m_gameInfo);

		m_startPos = m_startingPositionChooser.getSelected();
		System.out.println("Reveived starting position: " + m_startPos);

		m_autonomousTarget = m_targetLocationChooser.getSelected();
		System.out.println("Received target location: " + m_autonomousTarget);

		drive.setInverted(true);

		startTime = System.currentTimeMillis();
		switch (m_autonomousTarget){

		case AUTO_LINE:

			switch (m_startPos) {

			case CENTER:
				autoProgram = new AutoStraight (drive, AutoConstants.DISTANCE_TO_SWITCH);
				break;
			case LEFT_SIDE: case RIGHT_SIDE:
				autoProgram = new AutoStraight (drive, AutoConstants.DISTANCE_TO_SWITCH_SIDE);
				break;
			case LEFT_SWITCH: case RIGHT_SWITCH:
				autoProgram = new AutoStraight (drive, AutoConstants.DISTANCE_TO_SWITCH);
				break;

			}

			break;
		case OUR_SWITCH:

			switch (m_startPos) {

			case CENTER:

				if (m_gameInfo.isOurSwitchLeft()) {
					autoProgram = new CenterSwitchLeft (drive, elevator, fork, intake);
				}
				else {
					autoProgram = new CenterSwitchRight (drive, elevator, fork, intake);
				}

				break;
			case LEFT_SIDE: case RIGHT_SIDE:

				if (m_gameInfo.isAllignedWithPos(m_startPos)) {
					autoProgram = new SideSwitch (drive, elevator, fork, intake, m_startPos == StartingPosition.LEFT_SIDE);
				}
				else {
					autoProgram = new SideSwitchOtherSide (drive, elevator, fork, intake, m_startPos == StartingPosition.LEFT_SIDE);
				}

				//autoProgram = new AutoTurn (drive, 90, 1.0);

				break;
			case LEFT_SWITCH: case RIGHT_SWITCH:

				if (m_gameInfo.isAllignedWithPos(m_startPos)) {
					autoProgram = new StraightSwitch(drive, elevator, fork, intake);
				}
				else {
					autoProgram = new AutoStraight (drive, AutoConstants.DISTANCE_TO_SWITCH);
				}
				break;

			}

			break;
		case SCALE:

			break;

		}

		resetMode();

		if (autoProgram != null) {
			autoProgram.start();
		}

	}


	/**
	 * Uses the scheduler to run the autonomous command chosen
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * Makes sure to set drive inverted to false in the beginning of teleop
	 */
	public void teleopInit () {
		drive.setInverted(false);
	}

	/**
	 * Reads input from the controls and outputs to the subsystems
	 */
	@Override
	public void teleopPeriodic() {

		/* --------- SmartDashboard --------- */

		elevator.setBrakeMode(!SmartDashboard.getBoolean("Elevator Coast", false));

		if (SmartDashboard.getBoolean("Zero Elevator", false)) {
			elevator.resetCurrentHeight();
		}

		elevator.setOverrideEncoderBottom(SmartDashboard.getBoolean("Override Elevator Encoder Bottom", false));

		SmartDashboard.putBoolean("Intake Running", intake.wheelsRunning());


		/* --------- Copilot --------- */

		// Toggle retracting the intake
		if (copilot.getBumperPressed(Hand.kRight)) {
			intake.toggleArms();
		}

		// Hold for intake controls
		if (copilot.getAButton()) {
			intake.cubeIn();
		}
		else if (copilot.getXButton()) {
			intake.cubeOut();
			intake.openArms();
		}
		else if (copilot.getYButton()) {
			intake.cubeOutFullPower();
			intake.openArms();
		}
		else {
			intake.stop();
		}
		
		// Use POV for Fork controls: up cooresponds to extending, down to retracting (while held)
		if (copilot.getPOV()== 0) {
			fork.extend();
		}
		else if (copilot.getPOV() == 180) {
			fork.retract();
		}
		else {
			fork.stop();
		}

		// If all three pressed, go down (used for reset in pit or in unexpected scenario in game) 
		if (copilot.getStartButton() && copilot.getBackButton() && copilot.getBumper(Hand.kLeft)) {
			climber.down();
		}
		// If only these two pressed, climb up (used in end of match)
		else if (copilot.getStartButton() && copilot.getBackButton()) {
			climber.up();
		}
		// Else, stop climbing (need to hold for climbing)
		else {
			climber.stop();
		}

		// Elevate the elevator according to the axis
		elevator.elevate(-copilot.getY(Hand.kLeft));


		/* --------- Pilot --------- */
		
		// Toggle the gear speed for driving
		if (pilot.getBumperPressed(Hand.kLeft)) {
			drive.toggleGearSpeed();
		}

		// Toggle the robot's front for driving
		if (pilot.getYButtonPressed()) {
			drive.toggleInverted();
		}

		// Arcade drive using left joystick for x speed, and right joystick for z rotation
		drive.arcadeDrive(pilot.getY(Hand.kLeft), pilot.getX(Hand.kRight), true);

	}

	/**
	 * Calibrates the gyro in test mode (robot must be stationary)
	 */
	public void testInit() {
		drive.calibrateGyro();
	}

	/**
	 * Does nothing currently.
	 */
	public void testPeriodic() {	
	}

	/**
	 * Initializes the settings which allow for the dashbaords to view 
	 * the camera streasm from the raspberry pi.
	 */
	private void initializeCameraConfig(){
		NetworkTableInstance.getDefault()
		.getEntry("/CameraPublisher/ForkCamera/streams")
		.setStringArray(new String[]
				{"mjpeg:http://10.43.38.8:1180/?action=stream"});
		NetworkTableInstance.getDefault()
		.getEntry("/CameraPublisher/BackCamera/streams")
		.setStringArray(new String[]
				{"mjpeg:http://10.43.38.8:1182/?action=stream"});
	}

	/**
	 * Returns the start time of the match (as given by System.currentTimeMillis())
	 * @return
	 */
	public static long getStartTime () {
		return startTime;
	}

	/**
	 * Returns the time since the beginning of the match
	 * @return
	 */
	public static long timeSinceStart () {
		return System.currentTimeMillis() - startTime;
	}
}
