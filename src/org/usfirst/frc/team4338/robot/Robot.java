/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4338.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
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
		RAMP_LEFT_SW(2),
		RAMP_RIGHT_SW(20),
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
		RAMP_LIFT_LEFT (5),
		FORK (6),
		RAMP_LIFT_RIGHT (7),
		INTAKE_RIGHT (8),
		RAMP_LOWER_LEFT (9),
		RAMP_LOWER_RIGHT (10),
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

	
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	private DifferentialDrive drive;
	private Elevator elevator;
	
	private XboxController pilot;
	private XboxController copilot;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		drive = new DifferentialDrive (new WPI_TalonSRX (CANWiring.DRIVE_LEFT.m_port), 
				new WPI_TalonSRX (CANWiring.DRIVE_RIGHT.m_port));
		elevator = new Elevator (CANWiring.ELEVATOR.m_port, DIOWiring.ELEVATOR_BOTTOM_SW.m_port, 
				DIOWiring.ELEVATOR_ENCODER_A.m_port, DIOWiring.ELEVATOR_ENCODER_B.m_port);
				
		
		pilot = new XboxController (0);
		copilot = new XboxController (1);
		
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
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
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		switch (m_autoSelected) {
			case kCustomAuto:
				// Put custom auto code here
				break;
			case kDefaultAuto:
			default:
				// Put default auto code here
				break;
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		elevator.elevateUpDown(copilot.getY(Hand.kRight));
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
