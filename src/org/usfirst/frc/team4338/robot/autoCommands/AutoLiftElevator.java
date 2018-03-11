package org.usfirst.frc.team4338.robot.autoCommands;

import org.usfirst.frc.team4338.robot.Robot;
import org.usfirst.frc.team4338.robot.systems.Elevator;
import org.usfirst.frc.team4338.robot.systems.Elevator.Stage;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoLiftElevator extends Command {
	
	private Elevator elevator;
	private Stage stage;

    public AutoLiftElevator(Elevator elevator, Stage stage) {
       this.elevator = elevator;
       this.stage = stage;
       requires (elevator);
    }

    protected void initialize() {
    }

    protected void execute() {
    		elevator.elevateToStageFromBelow(stage);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return elevator.isAboveStage(stage);
    }

    // Called once after isFinished returns true
    protected void end() {
    		elevator.stop();
    		System.out.println("Finished AutoLiftElevator at " + Robot.timeSinceStart());
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    		end();
    }
}
