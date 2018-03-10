package org.usfirst.frc.team4338.robot.autoCommands;

import org.usfirst.frc.team4338.robot.SensorDrive;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoTurn extends Command {
	
	public static final double ANGLE_ERROR = 5;
	
	private SensorDrive drive;
	private double angle;

    public AutoTurn(SensorDrive drive, double angle) {
    		this.drive = drive;
    		this.angle = angle;
    		requires(drive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    		drive.resetGyro();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    		drive.gyroTurn(angle);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(drive.getGyroAngle() - angle) < ANGLE_ERROR;
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
