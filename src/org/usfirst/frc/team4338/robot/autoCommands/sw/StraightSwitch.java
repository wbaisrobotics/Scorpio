package org.usfirst.frc.team4338.robot.autoCommands.sw;

import org.usfirst.frc.team4338.robot.autoCommands.AutoConstants;
import org.usfirst.frc.team4338.robot.autoCommands.AutoExtendFork;
import org.usfirst.frc.team4338.robot.autoCommands.AutoLiftElevator;
import org.usfirst.frc.team4338.robot.autoCommands.AutoReleaseCube;
import org.usfirst.frc.team4338.robot.autoCommands.AutoStraight;
import org.usfirst.frc.team4338.robot.systems.Elevator;
import org.usfirst.frc.team4338.robot.systems.Fork;
import org.usfirst.frc.team4338.robot.systems.Intake;
import org.usfirst.frc.team4338.robot.systems.SensorDrive;
import org.usfirst.frc.team4338.robot.systems.Elevator.Stage;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class StraightSwitch extends CommandGroup {

    public StraightSwitch(SensorDrive drive, Elevator elevator, Fork fork, Intake intake) {
        addSequential (new AutoStraight(drive, AutoConstants.DISTANCE_TO_SWITCH));
        addSequential (new AutoLiftElevator (elevator, Stage.SWITCH));
        addSequential (new AutoExtendFork (fork));
        addSequential (new AutoReleaseCube(intake));
    }
}
