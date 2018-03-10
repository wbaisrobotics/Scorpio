package org.usfirst.frc.team4338.robot.autoCommands;

import org.usfirst.frc.team4338.robot.Robot;
import org.usfirst.frc.team4338.robot.systems.SensorDrive;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoTurn extends Command {
	
	public static final double ANGLE_ERROR = 1;
	
	private SensorDrive drive;
	private double angle;
	private double magnitude;
	
	public AutoTurn (SensorDrive drive, double angle) {
		this (drive, angle, 0);
	}

    public AutoTurn(SensorDrive drive, double angle, double magnitude) {
    		this.drive = drive;
    		this.angle = angle;
    		this.magnitude = magnitude;
    		requires(drive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    		drive.initGyroTurn(angle);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    		drive.executeGyroTurn(magnitude);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(drive.getGyroTurnError()) < ANGLE_ERROR;
    }

    // Called once after isFinished returns true
    protected void end() {
    		drive.stop();
    		System.out.println("Finished AutoTurn at " + Robot.timeSinceStart());
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    		end();
    }
}
