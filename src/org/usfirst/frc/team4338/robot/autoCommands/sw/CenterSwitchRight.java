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

public class CenterSwitchRight extends CommandGroup {

    public CenterSwitchRight(SensorDrive drive, Elevator elevator, Fork fork, Intake intake) {
    	
        addSequential (new AutoStraight(drive, 0.5));
        addSequential (new AutoTurn (drive, 55));
        addSequential (new AutoStraight(drive, 1.2));
        addSequential (new AutoTurn (drive, -55));
        addSequential (new AutoStraight(drive, 1.2));
        
        addSequential (new AutoLiftElevator (elevator, Stage.SWITCH));
        addSequential (new AutoExtendFork (fork));
        addSequential (new AutoReleaseCube(intake));
        
    }
}
