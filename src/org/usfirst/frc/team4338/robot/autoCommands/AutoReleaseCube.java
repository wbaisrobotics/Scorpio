package org.usfirst.frc.team4338.robot.autoCommands;

import org.usfirst.frc.team4338.robot.systems.Intake;

import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 *
 */
public class AutoReleaseCube extends TimedCommand {
	
	private Intake intake;

    public AutoReleaseCube(Intake intake) {
    		super (1.0);
        this.intake = intake;
        requires (intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    		intake.cubeOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    		intake.stopWheels();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    		end();
    }
}
