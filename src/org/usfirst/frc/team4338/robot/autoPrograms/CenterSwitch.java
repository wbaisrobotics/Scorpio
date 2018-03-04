package org.usfirst.frc.team4338.robot.autoPrograms;

import org.usfirst.frc.team4338.robot.Drive;
import org.usfirst.frc.team4338.robot.Fork;
import org.usfirst.frc.team4338.robot.Robot;

public class CenterSwitch implements AutonomousProgram {
	
	private Drive drive;
	private Fork fork;
	private boolean left;

	public CenterSwitch(Drive drive, Fork fork, boolean left) {
		this.drive = drive;
		this.fork = fork;
		this.left = left;
	}

	@Override
	public void initialize() {
		drive.resetGyro();
	}

	@Override
	public void update() {
		if (Robot.timeSinceStart()<200) {
			drive.driveGyroStraight(0.3);
			fork.extend();
		}
		else if ((System.currentTimeMillis()-Robot.getStartTime())<500) {
			drive.arcadeDrive(0, left?-0.3:0.3);
			fork.extend();
		}
		else if ((System.currentTimeMillis()-Robot.getStartTime())<1000) {
			drive.driveGyroStraight(0.3);
			fork.extend();
		}
		else if ((System.currentTimeMillis()-Robot.getStartTime())<1300) {
			drive.arcadeDrive(0, left?0.3:-0.3);
			fork.extend();
		}
		else if ((System.currentTimeMillis()-Robot.getStartTime())<1800) {
			drive.driveGyroStraight(0.3);
			fork.extend();
		}
		else {
			fork.stop();
			fork.openGripper();
			drive.arcadeDrive(0, 0, false);
		}
	}

	@Override
	public void stop() {
	}

}
