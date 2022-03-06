// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Dumper;

public class DumperMoveLimitSwitch extends CommandBase {
  Dumper m_dumper;
  private boolean finish;

  /** Creates a new DumperMoveLimitSwitch. */
  public DumperMoveLimitSwitch(Dumper d) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_dumper = d;
    addRequirements(m_dumper);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_dumper.checkArmPosition();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // If arm is down, move up
    if(m_dumper.getArmPosition() == -1) {
      if (m_dumper.getTopSwitch().get()) {
        m_dumper.isUp();
        m_dumper.setToBrake();
        finish = true;
      }
      else {
        m_dumper.setToCoast();
        m_dumper.moveArm(Constants.dumperUpSpeed);
        System.out.println("Moving upppp");
      }
  }

  // If arm is up, move down
    else if(m_dumper.getArmPosition() == 1) {
      if (m_dumper.getBottomSwitch().get()) {
        m_dumper.isDown();
        m_dumper.setToBrake();
        finish = true;
      }
      else {
        m_dumper.setToCoast();
        m_dumper.moveArm(Constants.dumperDownSpeed);
        System.out.println("moving downnnnnn!!");
      }
    }

    // If arm is middle, move up
    else if (m_dumper.getArmPosition() == 0) {
      if (m_dumper.getTopSwitch().get()) {
        m_dumper.isUp();
        m_dumper.setToBrake();
        finish = true;
      }
      else {
        m_dumper.setToCoast();
        m_dumper.moveArm(Constants.dumperUpSpeed);
        System.out.println("Moving upppp");
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_dumper.stopArm();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finish;
  }
}
