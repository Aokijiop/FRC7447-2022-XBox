// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Dumper;

public class DumperLowerSlow extends CommandBase {
  Dumper m_dumper;
  Timer timer;
  private boolean finish;

  /** Creates a new DumperLowerSlow. */
  public DumperLowerSlow(Dumper d) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_dumper = d;
    timer = new Timer();
    addRequirements(m_dumper);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_dumper.checkArmPosition();
    timer.reset();
    timer.start();
    finish = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_dumper.getArmPosition() == 1) {
      if (m_dumper.getBottomSwitch().get()) {
        m_dumper.isDown();
        m_dumper.setToBrake();
        finish = true;
      }
      else {
        m_dumper.setToCoast();
        m_dumper.moveArm(Constants.dumperDownSlowSpeed);
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_dumper.stopArm();
    timer.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finish;
  }
}
