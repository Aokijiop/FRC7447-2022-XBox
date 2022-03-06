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
public class TimedHubScoreAuton extends SequentialCommandGroup {
  /** Creates a new TimerTerminalSideScoreAuton. */
  public TimedHubScoreAuton(Dumper d, DriveTrain dt) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new DumperVomit(d).raceWith(new Wait(2.5f)), 
      new DriveTimed(dt, -Constants.autonSpeed, 0.02f),
      new ParallelCommandGroup(new DumperMoveLimitSwitch(d), new DriveTimed(dt, -Constants.autonSpeed, 3.0f))
    );

    // Testing
  }
}
