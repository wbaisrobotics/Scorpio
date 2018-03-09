package org.usfirst.frc.team4338.robot.autoPrograms.side;

import org.usfirst.frc.team4338.robot.Elevator;
import org.usfirst.frc.team4338.robot.Robot;
import org.usfirst.frc.team4338.robot.SensorDrive;
import org.usfirst.frc.team4338.robot.autoPrograms.AutonomousProgram;

public class StraightSwitchWrong implements AutonomousProgram {
	
	private SensorDrive drive;
	private Elevator elevator;

	public StraightSwitchWrong(SensorDrive drive, Elevator elevator) {
		this.drive = drive;
		this.elevator = elevator;
	}

	@Override
	public void initialize() {
		drive.resetGyro();
	}

	@Override
	public void update() {
		if (Robot.timeSinceStart()<2800) {
			drive.gyroStraight(0.7);
		}
		else if (Robot.timeSinceStart() < 4800){
			drive.arcadeDrive(0, 0, false);
			elevator.elevateUpDown(0.5);
		}
		else {
			stop();
		}
	}

	@Override
	public void stop() {
		drive.arcadeDrive(0, 0);
		elevator.elevateUpDown(0.0);
	}

}
