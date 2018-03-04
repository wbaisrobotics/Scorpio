package org.usfirst.frc.team4338.robot.autoPrograms;

import org.usfirst.frc.team4338.robot.Drive;
import org.usfirst.frc.team4338.robot.Robot;

public class DriveStraight implements AutonomousProgram {
	
	private Drive drive;

	public DriveStraight(Drive drive) {
		this.drive = drive;
	}

	@Override
	public void initialize() {
		drive.resetGyro();
	}

	@Override
	public void update() {
		if (Robot.timeSinceStart()<4000) {
			drive.driveGyroStraight(0.7);
		}
		else {
			stop();
		}
	}

	@Override
	public void stop() {
		drive.arcadeDrive(0, 0);
	}

}
