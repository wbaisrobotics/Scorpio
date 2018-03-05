package org.usfirst.frc.team4338.robot.autoPrograms;

import org.usfirst.frc.team4338.robot.Drive;
import org.usfirst.frc.team4338.robot.Elevator;
import org.usfirst.frc.team4338.robot.Fork;
import org.usfirst.frc.team4338.robot.Robot;

public class SameSideSwitch implements AutonomousProgram {
	
	private Drive drive;
	private Fork fork;
	private Elevator elevator;

	public SameSideSwitch(Drive drive, Fork fork, Elevator elevator) {
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
		if (Robot.timeSinceStart()<2000) {
			drive.driveGyroStraight(1.0);
		}
		else {
			drive.arcadeDrive(0, 0, false);
		}
		
		if (Robot.timeSinceStart() < 2000) {
			elevator.elevateUpDown(0.5);
		}
		else if (Robot.timeSinceStart() < 10000){
			elevator.stop();
			fork.extend();
		}
		else {
			fork.stop();
			fork.openGripper();
		}
	}

	@Override
	public void stop() {
	}

}
