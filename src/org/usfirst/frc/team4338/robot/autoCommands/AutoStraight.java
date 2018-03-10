package org.usfirst.frc.team4338.robot.autoCommands;

import org.usfirst.frc.team4338.robot.SensorDrive;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoStraight extends Command {

	public static final double DRIVE_MAGNITUDE = 0.5;
	public static final double COAST_DISTANCE = 0.4;

	private SensorDrive drive;
	private double distance;

	public AutoStraight(SensorDrive drive, double distance) {
		this.drive = drive;
		this.distance = distance;
		requires (drive);
	}

	protected void initialize() {
		drive.resetGyro();
		drive.resetEncoders();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		drive.gyroStraight(DRIVE_MAGNITUDE);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return drive.getAvgDistance() > (distance-COAST_DISTANCE);
	}

	// Called once after isFinished returns true
	protected void end() {
		drive.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
