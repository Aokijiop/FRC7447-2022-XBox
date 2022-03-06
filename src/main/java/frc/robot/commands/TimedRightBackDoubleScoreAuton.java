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
public class TimedRightBackDoubleScoreAuton extends SequentialCommandGroup {
  /** Creates a new TerminalSideScoreAuton. */
  public TimedRightBackDoubleScoreAuton (DriveTrain dt, Dumper d) { 
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new ParallelCommandGroup(new DriveTimed(dt, Constants.autonSpeed, 2.0f),  new DumperMoveLimitSwitch(d), new DumperIntake(d).raceWith(new Wait(2.5f))), 
        new DriveTimed(dt, -Constants.autonSpeed, 0.02f),
        new TurnTimed(dt, Constants.autonTSpeed, Constants.turn180Time), 
        new DriveTimed(dt, Constants.autonSpeed, 2.5f), 
        new ParallelCommandGroup(new TurnTimed(dt, Constants.autonTSpeed, 0.75f), new DumperMoveLimitSwitch(d)), 
        new DriveTimed(dt, Constants.autonSpeed, 0.02f),
        new DumperVomit(d).raceWith(new Wait(3.0f)), 
        new DriveTimed(dt, -Constants.autonSpeed, 0.02f),
        new TurnTimed(dt, Constants.autonTSpeed, Constants.turn180Time)
    );

    // Testing
  }
}