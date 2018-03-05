package org.usfirst.frc.team4338.robot.autoPrograms.side;

import org.usfirst.frc.team4338.robot.Drive;
import org.usfirst.frc.team4338.robot.Elevator;
import org.usfirst.frc.team4338.robot.Elevator.Stages;
import org.usfirst.frc.team4338.robot.Fork;
import org.usfirst.frc.team4338.robot.Robot;
import org.usfirst.frc.team4338.robot.autoPrograms.AutonomousProgram;

public class SameSideSwitchSide implements AutonomousProgram {
	
	private Drive drive;
	private Elevator elevator;
	private Fork fork;
	
	private boolean fromLeft;
	
	private boolean startedTurning = false;

	public SameSideSwitchSide(Drive drive, Elevator elevator, Fork fork, boolean fromLeft) {
		this.drive = drive;
		this.elevator = elevator;
		this.fork = fork;
		this.fromLeft = fromLeft;
	}

	@Override
	public void initialize() {
		drive.resetGyro();
	}

	@Override
	public void update() {
		
		if (Robot.timeSinceStart() < 3200) {
			drive.driveGyroStraight(0.7);
		}
		else if (Robot.timeSinceStart() < 5200) {
			if (startedTurning) {
				drive.turn(fromLeft?90:-90);
			}
			else {
				drive.resetGyro();
				startedTurning = true;
			}
		}
		else if (Robot.timeSinceStart() < 7200) {
			elevator.elevateToStageFromBelow(Stages.SWITCH);
		}
		else if (Robot.timeSinceStart() < 8200) {
			elevator.elevateToStageFromBelow(Stages.SWITCH);
			fork.extend();
		}
		else {
			fork.releaseFork();
		}
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
