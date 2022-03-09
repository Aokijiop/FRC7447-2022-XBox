// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Dumper;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class TimedRightBackScoreAuton extends SequentialCommandGroup {
  public TimedRightBackScoreAuton(DriveTrain dt, Dumper d) {
    addCommands(new DriveTimed(dt, Constants.autonSpeed, 0.75f), 
        new TurnTimed(dt, Constants.autonTSpeed, 0.625f), 
        new ParallelCommandGroup(new DriveTimed(dt, Constants.autonSpeed, 0.2f),
        new DumperVomit(d).raceWith(new Wait(2.0f))), 
        new DriveTimed(dt, -Constants.autonSpeed, 7.0f)
    );
    
    // Testing
  }
}