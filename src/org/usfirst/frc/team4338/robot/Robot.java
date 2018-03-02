/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4338.robot;

import org.usfirst.frc.team4338.robot.autonomousData.GameInfo;
import org.usfirst.frc.team4338.robot.autonomousData.StartingPosition;
import org.usfirst.frc.team4338.robot.autonomousData.Target;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
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

		ELEVATOR_ENCODER_A(0),
		ELEVATOR_ENCODER_B(1),
		FORK_RETRACTED_SW(3),
		DRIVE_LEFT_B(5),
		DRIVE_LEFT_A(6),
		DRIVE_RIGHT_A(8),
		DRIVE_RIGHT_B(7),
		INTAKE_SW(9),
		TEAMMATE_LIFTER_LEFT_SW(2),
		TEAMMATE_LIFTER_RIGHT_SW(20),
		ELEVATOR_BOTTOM_SW(18),
		FORK_EXTENDED_SW(13);
		
		private int m_port;
		private DIOWiring (int port) {
			this.m_port = port;
		}
		
	}
	
	public enum CANWiring {
		
		DRIVE_LEFT (2),
		INTAKE_LEFT (3), 
		ELEVATOR (4),
		TEAMMATE_LIFTER_LEFT (5),
		FORK (6),
		TEAMMATE_LIFTER_RIGHT (7),
		INTAKE_RIGHT (8),
		RAMP_LEFT (9),
		RAMP_RIGHT (10),
		DRIVE_RIGHT (11);
		
		private int m_port;
		private CANWiring (int port) {
			this.m_port = port;
		}
	}
	
	public enum PCMWiring {
		
		DRIVE_B (0), DRIVE_A (7),
		INTAKE_A(1), INTAKE_B(6),
		RELEASE_A (2), GRIPPER_B(5),
		RELEASE_B(3), GRIPPER_A(4);
		
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
	
	private Drive drive;
	private Elevator elevator;
	private Intake intake;
	private Fork fork;
	private Ramp ramp;
	
	private XboxController pilot;
	private XboxController copilot;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		drive = new Drive (new WPI_TalonSRX (CANWiring.DRIVE_LEFT.m_port), 
				new WPI_TalonSRX (CANWiring.DRIVE_RIGHT.m_port), 
				PCMWiring.DRIVE_A.m_port, PCMWiring.DRIVE_B.m_port);
		elevator = new Elevator (CANWiring.ELEVATOR.m_port, DIOWiring.ELEVATOR_BOTTOM_SW.m_port, 
				DIOWiring.ELEVATOR_ENCODER_A.m_port, DIOWiring.ELEVATOR_ENCODER_B.m_port);
		intake = new Intake (CANWiring.INTAKE_LEFT.m_port, CANWiring.INTAKE_RIGHT.m_port,
				PCMWiring.INTAKE_B.m_port);
		fork = new Fork (CANWiring.FORK.m_port, DIOWiring.FORK_EXTENDED_SW.m_port, DIOWiring.FORK_RETRACTED_SW.m_port,
				PCMWiring.GRIPPER_A.m_port, PCMWiring.GRIPPER_B.m_port, PCMWiring.RELEASE_A.m_port, PCMWiring.RELEASE_B.m_port);
		ramp = new Ramp (CANWiring.RAMP_LEFT.m_port, CANWiring.RAMP_RIGHT.m_port,
				CANWiring.TEAMMATE_LIFTER_LEFT.m_port, CANWiring.TEAMMATE_LIFTER_RIGHT.m_port,
				DIOWiring.TEAMMATE_LIFTER_LEFT_SW.m_port, DIOWiring.TEAMMATE_LIFTER_RIGHT_SW.m_port);
		
		pilot = new XboxController (0);
		copilot = new XboxController (1);
		
		m_startingPositionChooser.addDefault("Left", StartingPosition.LEFT);
		m_startingPositionChooser.addObject("Center", StartingPosition.CENTER);
		m_startingPositionChooser.addObject("Right", StartingPosition.RIGHT);
		SmartDashboard.putData("Starting Position", m_startingPositionChooser);
		
		m_targetLocationChooser.addDefault("Auto Line", Target.FORWARD);
		m_targetLocationChooser.addObject("Switch", Target.OUR_SWITCH);
		m_targetLocationChooser.addObject("Scale", Target.SCALE);
		SmartDashboard.putData("Target Autonomous", m_targetLocationChooser);
		
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		
		m_gameInfo = GameInfo.fromGameMessage(DriverStation.getInstance().getGameSpecificMessage());
		System.out.println("Receieved game message: \n\t" + m_gameInfo);

		m_startPos = m_startingPositionChooser.getSelected();
		System.out.println("Reveived starting position: " + m_startPos);
		
		m_autonomousTarget = m_targetLocationChooser.getSelected();
		System.out.println("Received target location: " + m_autonomousTarget);
		
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		
		/* --------- Pilot --------- */
		
		// Toggle the gear speed for driving
		if (pilot.getBumperPressed(Hand.kLeft)) {
			drive.toggleGearSpeed();
		}
		
		// Toggle the robot's front for driving
		if (pilot.getYButtonPressed()) {
			drive.toggleInverted();
		}
		
		// Toggle retracting the intake
		if (pilot.getBumperPressed(Hand.kRight)) {
			intake.toggleArms();
		}
		
		// A button toggles if intake is sucking a cube in
		if (pilot.getAButtonPressed()) {
			if (intake.wheelsRunning()) {
				intake.stopWheels();
			}
			else {
				intake.cubeIn();
			}
		}
		
		// X button toggles if intake is pushing a cube out
		if (pilot.getXButtonPressed()) {
			if (intake.wheelsRunning()) {
				intake.stopWheels();
			}
			else {
				intake.cubeOut();
			}
		}
		
		drive.curvatureDrive(pilot.getY(Hand.kLeft), pilot.getX(Hand.kRight), false);
		
		//drive.arcadeDrive(pilot.getY(Hand.kLeft), pilot.getX(Hand.kRight), true);
		

		/* --------- Copilot --------- */
		
		if (copilot.getBButton()) {
			fork.retract();
		}
		else if (copilot.getAButton()) {
			fork.extend();
		}
		else {
			fork.stop();
		}
		
		if (copilot.getBumperPressed(Hand.kRight)) {
			fork.toggleGripper();
		}
	
		
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
