package org.usfirst.frc.team4338.robot.autoCommands.sw;

import org.usfirst.frc.team4338.robot.autoCommands.AutoExtendFork;
import org.usfirst.frc.team4338.robot.autoCommands.AutoLiftElevator;
import org.usfirst.frc.team4338.robot.autoCommands.AutoReleaseCube;
import org.usfirst.frc.team4338.robot.autoCommands.AutoStraight;
import org.usfirst.frc.team4338.robot.autoCommands.AutoTurn;
import org.usfirst.frc.team4338.robot.systems.Elevator;
import org.usfirst.frc.team4338.robot.systems.Fork;
import org.usfirst.frc.team4338.robot.systems.Intake;
import org.usfirst.frc.team4338.robot.systems.SensorDrive;
import org.usfirst.frc.team4338.robot.systems.Elevator.Stage;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Represents switch from side (next to switch not in front of)
 * @author orianleitersdorf
 *
 */
public class SideSwitch extends CommandGroup {

	public SideSwitch(SensorDrive drive, Elevator elevator, Fork fork, Intake intake, boolean fromLeftSide) {

		addSequential (new AutoStraight (drive, 3.5));
		addSequential (new AutoTurn (drive, fromLeftSide?90:-90));
		addSequential (new AutoStraight (drive, 0.6));

		addSequential (new AutoLiftElevator (elevator, Stage.SWITCH));
		addSequential (new AutoExtendFork (fork));
		addSequential (new AutoReleaseCube(intake, 1.0));

	}
}