package org.usfirst.frc.team4338.robot.autoPrograms;

import org.usfirst.frc.team4338.robot.Drive;
import org.usfirst.frc.team4338.robot.Elevator;
import org.usfirst.frc.team4338.robot.Robot;

public class SwitchCenterNull implements AutonomousProgram {
	
	private Drive drive;
	private Elevator elevator;

	public SwitchCenterNull(Drive drive, Elevator elevator) {
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
			drive.driveGyroStraight(0.7);
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
