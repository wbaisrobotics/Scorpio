package org.usfirst.frc.team4338.robot.autoCommands;

import org.usfirst.frc.team4338.robot.Robot;
import org.usfirst.frc.team4338.robot.systems.SensorDrive;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Faster but may overshoot
 * @author orianleitersdorf
 *
 */
public class AutoStraightFullPower extends Command {

	public static final double DISTANCE_ERROR = 0.05;

	private SensorDrive drive;
	private double distance;

	public AutoStraightFullPower(SensorDrive drive, double distance) {
		this.drive = drive;
		this.distance = distance;
		requires (drive);
	}

	protected void initialize() {
		drive.resetEncoders();
		drive.resetGyro();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		drive.gyroStraight(1.0);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return drive.getAvgDistance() > distance;
	}

	// Called once after isFinished returns true
	protected void end() {
		drive.stop();
		System.out.println("Finished AutoStraightFullPower at " + Robot.timeSinceStart());
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
