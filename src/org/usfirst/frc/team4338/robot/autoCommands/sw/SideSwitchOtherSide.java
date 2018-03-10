package org.usfirst.frc.team4338.robot.autoCommands.sw;

import org.usfirst.frc.team4338.robot.SensorDrive;
import org.usfirst.frc.team4338.robot.Elevator;
import org.usfirst.frc.team4338.robot.Elevator.Stage;
import org.usfirst.frc.team4338.robot.Fork;
import org.usfirst.frc.team4338.robot.Intake;
import org.usfirst.frc.team4338.robot.autoCommands.AutoExtendFork;
import org.usfirst.frc.team4338.robot.autoCommands.AutoLiftElevator;
import org.usfirst.frc.team4338.robot.autoCommands.AutoReleaseCube;
//import org.usfirst.frc.team4338.robot.autoCommands.AutoStraight;
import org.usfirst.frc.team4338.robot.autoCommands.AutoStraightFullPower;
import org.usfirst.frc.team4338.robot.autoCommands.AutoTurn;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Represents switch from side (next to switch not in front of)
 * @author orianleitersdorf
 *
 */
public class SideSwitchOtherSide extends CommandGroup {

	public SideSwitchOtherSide(SensorDrive drive, Elevator elevator, Fork fork, Intake intake, boolean fromLeftSide) {

		addSequential (new AutoStraightFullPower (drive, 3.8));
		addSequential (new AutoTurn (drive, fromLeftSide?90:-90, 1.0));
		addSequential (new AutoStraightFullPower (drive, 2.6));
		addSequential (new AutoTurn (drive, fromLeftSide?90:-90));
		//addSequential (new AutoStraight (drive, 0.2));

		addSequential (new AutoLiftElevator (elevator, Stage.SWITCH));
		addSequential (new AutoExtendFork (fork));
		addSequential (new AutoReleaseCube(intake));

	}
}
