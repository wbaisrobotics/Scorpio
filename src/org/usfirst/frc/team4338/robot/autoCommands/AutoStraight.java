package org.usfirst.frc.team4338.robot.autoCommands;

import org.usfirst.frc.team4338.robot.Robot;
import org.usfirst.frc.team4338.robot.systems.SensorDrive;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoStraight extends Command {

	public static final double DISTANCE_ERROR = 0.05;

	private SensorDrive drive;
	private double distance;

	public AutoStraight(SensorDrive drive, double distance) {
		this.drive = drive;
		this.distance = distance;
		requires (drive);
	}

	protected void initialize() {
		drive.initEncoderStraight(distance);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		drive.executeEncoderStraight();
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Math.abs(drive.getEncoderStraightError()) < DISTANCE_ERROR;
	}

	// Called once after isFinished returns true
	protected void end() {
		drive.stop();
		System.out.println("Finished AutoStraight at " + Robot.timeSinceStart());
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
