package org.usfirst.frc.team4338.robot.autoPrograms.side;

import org.usfirst.frc.team4338.robot.Elevator;
import org.usfirst.frc.team4338.robot.Fork;
import org.usfirst.frc.team4338.robot.Robot;
import org.usfirst.frc.team4338.robot.SensorDrive;
import org.usfirst.frc.team4338.robot.autoPrograms.AutonomousProgram;

public class StraightSwitch implements AutonomousProgram {
	
	private SensorDrive drive;
	private Fork fork;
	private Elevator elevator;

	public StraightSwitch(SensorDrive drive, Fork fork, Elevator elevator) {
		this.drive = drive;
		this.fork = fork;
		this.elevator = elevator;
	}

	@Override
	public void initialize() {
		drive.resetGyro();
	}

	@Override
	public void update() {
		if (Robot.timeSinceStart()<2900) {
			drive.gyroStraight(0.7);
		}
		else {
			drive.arcadeDrive(0, 0, false);
		}
		
		if (Robot.timeSinceStart() < 2000) {
			elevator.elevateUpDown(0.5);
		}
		else if (Robot.timeSinceStart() < 5500){
			elevator.stop();
			fork.extend();
		}
		else {
			fork.stop();
			fork.openGripper();
		}
//		else {
//			//fork.retract();
//		}
	}

	@Override
	public void stop() {
	}

}
