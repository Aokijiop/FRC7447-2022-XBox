// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Dumper;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class LeftBackDoubleScoreAuton extends SequentialCommandGroup {
  /** Creates a new LeftBackDoubleScoreAuton. */
  public LeftBackDoubleScoreAuton(DriveTrain dt, Dumper d) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new ParallelCommandGroup(new DriveToDistance(dt, 1.5f), new DumperMove(d), new DumperIntake(d).raceWith(new Wait(3.0f))), 
      new TurnToAngle(dt, 180.0f), 
      new DriveToDistance(dt, 3.75f), 
      new ParallelCommandGroup(new TurnToAngle(dt, -15.0f), 
      new DumperMove(d)),
      new DriveToDistance(dt, 0.05f), 
      new DumperVomit(d).raceWith(new Wait(3.0f)), 
      new DriveToDistance(dt, -0.05f), 
      new ParallelCommandGroup(new TurnToAngle(dt, 180.0f), new DumperMove(d)), 
      new DriveToDistance(dt, 3.5f)
    );

    // Testing
  }
}
