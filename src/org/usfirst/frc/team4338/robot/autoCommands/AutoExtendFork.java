package org.usfirst.frc.team4338.robot.autoCommands;

import org.usfirst.frc.team4338.robot.Fork;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoExtendFork extends Command {
	
	private Fork fork;

    public AutoExtendFork(Fork fork) {
    		this.fork = fork;
    		requires (fork);
    }

    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    		fork.extend();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !fork.canExtend();
    }

    // Called once after isFinished returns true
    protected void end() {
    		fork.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    		end();
    }
}
