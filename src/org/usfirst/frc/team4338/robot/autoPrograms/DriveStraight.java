package org.usfirst.frc.team4338.robot.autoPrograms;

import org.usfirst.frc.team4338.robot.Robot;
import org.usfirst.frc.team4338.robot.SensorDrive;

public class DriveStraight implements AutonomousProgram {
	
	private SensorDrive drive;
	private double time;

	public DriveStraight(SensorDrive drive, double time) {
		this.drive = drive;
		this.time = time;
	}

	@Override
	public void initialize() {
		drive.resetGyro();
	}

	@Override
	public void update() {
		if (drive.getAvgDistance() < 3.156) {
			drive.gyroStraight(0.5);
		}
		else {
			stop();
		}
//		if (Robot.timeSinceStart()<time) {
//			drive.gyroStraight(0.7);
//		}
//		else {
//			stop();
//		}
	}

	@Override
	public void stop() {
		drive.arcadeDrive(0, 0);
	}

}
