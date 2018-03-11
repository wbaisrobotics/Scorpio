package org.usfirst.frc.team4338.robot.autoCommands.sw;

import org.usfirst.frc.team4338.robot.autoCommands.AutoIntakeCube;
//import org.usfirst.frc.team4338.robot.Elevator.Stage;
//import org.usfirst.frc.team4338.robot.autoCommands.AutoExtendFork;
//import org.usfirst.frc.team4338.robot.autoCommands.AutoLiftElevator;
import org.usfirst.frc.team4338.robot.autoCommands.AutoReleaseCube;
import org.usfirst.frc.team4338.robot.autoCommands.AutoStraight;
import org.usfirst.frc.team4338.robot.autoCommands.AutoTurn;
import org.usfirst.frc.team4338.robot.systems.Elevator;
import org.usfirst.frc.team4338.robot.systems.Fork;
import org.usfirst.frc.team4338.robot.systems.Intake;
import org.usfirst.frc.team4338.robot.systems.SensorDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterSwitchLeft extends CommandGroup {

    public CenterSwitchLeft(SensorDrive drive, Elevator elevator, Fork fork, Intake intake) {
    	
        addSequential (new AutoStraight(drive, 0.5));
        addSequential (new AutoTurn (drive, -65));
        addSequential (new AutoStraight(drive, 2.0));
        addSequential (new AutoTurn (drive, 65));
        addSequential (new AutoStraight(drive, 1.0));
        
//        addSequential (new AutoLiftElevator (elevator, Stage.SWITCH));
//        addSequential (new AutoExtendFork (fork));
        addSequential (new AutoReleaseCube(intake, 0.1));
        
        addSequential (new AutoStraight(drive, -0.5));
        addSequential (new AutoTurn (drive, -65));
        addSequential (new AutoStraight(drive, -1.75));
        addSequential (new AutoTurn (drive, 65));
        
        addParallel (new AutoStraight(drive, 0.8));
        addSequential (new AutoIntakeCube (intake, 1));
        
        addSequential (new AutoStraight(drive, -0.5));
        addSequential (new AutoTurn (drive, -65));
        addSequential (new AutoStraight(drive, 1.75));
        addSequential (new AutoTurn (drive, 65));
        addSequential (new AutoStraight(drive, 0.5));
        
        addSequential (new AutoReleaseCube(intake, 1.0));
        

    }
}
