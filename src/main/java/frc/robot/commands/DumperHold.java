// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Dumper;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class DumperHold extends CommandBase {
  Dumper m_dumper;
  
  /** Creates a new DumperHold. */
  public DumperHold(Dumper d) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_dumper = d;
    addRequirements(m_dumper);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_dumper.checkArmPosition();
    SmartDashboard.putData(m_dumper.getTopSwitch());
    SmartDashboard.putData(m_dumper.getBottomSwitch());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(m_dumper.getArmPosition() == 1){
      m_dumper.moveArm(Constants.dumperHoldUpSpeed); 
      m_dumper.passiveSpin(RobotContainer.m_joystick.getRawAxis(Constants.RTrigger) - (RobotContainer.m_joystick.getRawAxis(Constants.LTrigger)));
    }
    else if(m_dumper.getArmPosition() == -1) {
      m_dumper.moveArm(Constants.dumperHoldDownSpeed);
      m_dumper.passiveSpin(0.5 + RobotContainer.m_joystick.getRawAxis(Constants.RTrigger) - (RobotContainer.m_joystick.getRawAxis(Constants.LTrigger) * 1.5));
    }
    else {
      m_dumper.passiveSpin(RobotContainer.m_joystick.getRawAxis(Constants.RTrigger) - (RobotContainer.m_joystick.getRawAxis(Constants.LTrigger)));
    }
    SmartDashboard.putData(m_dumper.getTopSwitch());
    SmartDashboard.putData(m_dumper.getBottomSwitch());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
