// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Dumper;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  private static AddressableLED m_led;
  private static AddressableLEDBuffer m_ledBuffer;
  private PowerDistribution m_PowerDistr;
  private Thread m_visionThread;
  private Timer m_timer;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    m_led = new AddressableLED(Constants.RGBPort);
    m_ledBuffer = new AddressableLEDBuffer(Constants.RGBLength);
    m_led.setLength(m_ledBuffer.getLength());
    m_PowerDistr = new PowerDistribution(0, ModuleType.kCTRE);
    m_timer = new Timer();

    m_led.setData(m_ledBuffer);
    m_led.start();

    m_visionThread = 
      new Thread(
        () -> {
          // Get the UsbCamera from CameraServer
          UsbCamera camera = CameraServer.startAutomaticCapture();
          // Set the resolution
          camera.setResolution(640, 480);

          // Get a CvSink. This will capture Mats from the camera
          CvSink cvSink = CameraServer.getVideo();
          // Setup a CvSource. This will send images back to the Dashboard
          CvSource outputStream = CameraServer.putVideo("Rectangle", 640, 480);

          // Mats are very memory expensive. Lets reuse this Mat.
          Mat mat = new Mat();

          // This cannot be 'true'. The program will never exit if it is. This
          // lets the robot stop this thread when restarting robot code or
          // deploying.
          while (!Thread.interrupted()) {
            // Tell the CvSink to grab a frame from the camera and put it
            // in the source mat.  If there is an error notify the output.
            if (cvSink.grabFrame(mat) == 0) {
              // Send the output the error.
              outputStream.notifyError(cvSink.getError());
              // skip the rest of the current iteration
              continue;
            }
            // Put a rectangle on the image
            Imgproc.rectangle(
                mat, new Point(100, 100), new Point(400, 400), new Scalar(255, 255, 255), 5);
            // Give the output stream a new image to display
            outputStream.putFrame(mat);
          }
        });
    m_visionThread.setDaemon(true);
    m_visionThread.start();
  }
   /* This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    SmartDashboard.putData(CommandScheduler.getInstance());

    // Implements PowerDistribution Module
    SmartDashboard.putNumber("Amps for Channel 0 (MC 4 - Dumper Intake/Outtake): ", m_PowerDistr.getCurrent(0));
    SmartDashboard.putNumber("Amps for Channel 1 (MC 3 - Right Back): ", m_PowerDistr.getCurrent(1));
    // SmartDashboard.putNumber("Amps for Channel 2: ", m_PowerDistr.getCurrent(2));
    SmartDashboard.putNumber("Amps for Channel 3 (MC 2 - Right Front): ", m_PowerDistr.getCurrent(3));
    // SmartDashboard.putNumber("Amps for Channel 4: ", m_PowerDistr.getCurrent(4));
    // SmartDashboard.putNumber("Amps for Channel 5: ", m_PowerDistr.getCurrent(5));
    // SmartDashboard.putNumber("Amps for Channel 6: ", m_PowerDistr.getCurrent(6));
    // SmartDashboard.putNumber("Amps for Channel 7: ", m_PowerDistr.getCurrent(7));
    // SmartDashboard.putNumber("Amps for Channel 8: ", m_PowerDistr.getCurrent(8));
    // SmartDashboard.putNumber("Amps for Channel 9: ", m_PowerDistr.getCurrent(9));
    // SmartDashboard.putNumber("Amps for Channel 10: ", m_PowerDistr.getCurrent(10));
    // SmartDashboard.putNumber("Amps for Channel 11: ", m_PowerDistr.getCurrent(11));
    SmartDashboard.putNumber("Amps for Channel 12 (MC 1 - Left Back): ", m_PowerDistr.getCurrent(12));
    SmartDashboard.putNumber("Amps for Channel 13 (MC 0 - Left Front): ", m_PowerDistr.getCurrent(13));
    SmartDashboard.putNumber("Amps for Channel 14 (MC 8 - Dumper Arm): ", m_PowerDistr.getCurrent(14));
    // SmartDashboard.putNumber("Amps for Channel 15: ", m_PowerDistr.getCurrent(15));
    SmartDashboard.putNumber("Total Power from Monitored Channels: ", m_PowerDistr.getTotalPower());
    SmartDashboard.putNumber("Input Voltage of PDP: ", m_PowerDistr.getVoltage());
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {
    teamColors();
  }

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    m_timer.reset();
    m_timer.start();
    while (m_timer.get() < 3.0f) {
      RobotContainer.m_joystick.setRumble(RumbleType.kLeftRumble, 1);
      RobotContainer.m_joystick.setRumble(RumbleType.kRightRumble, 1);
    }
    RobotContainer.m_joystick.setRumble(RumbleType.kLeftRumble, 0);
    RobotContainer.m_joystick.setRumble(RumbleType.kRightRumble, 0);
    m_timer.stop();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  private void teamColors() {
    for (int i = 0; i < m_ledBuffer.getLength(); i++) {
      if (i % 2 == 0) {
        m_ledBuffer.setRGB(i, 66, 277, 245);
      }
      else {
        m_ledBuffer.setRGB(i, 232, 253, 255);
      }
    }
  }

  public static void rainbow() {
    int m_rainbowFirstPixelHue = 1;
    // For every pixel
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      // Calculate the hue - hue is easier for rainbows because the color
      // shape is a circle so only one value needs to precess
      final var hue = (m_rainbowFirstPixelHue + (i * 180 / m_ledBuffer.getLength())) % 180;
      // Set the value
      m_ledBuffer.setHSV(i, hue, 255, 128);
    }
    // Increase by to make the rainbow "move"
    m_rainbowFirstPixelHue += 3;
    // Check bounds
    m_rainbowFirstPixelHue %= 180;
  }

  public static void dumperDownColor() {
    int count = 0;
    for (int i = 0; i < m_ledBuffer.getLength(); i++) {
      while (count < 5) {
        m_ledBuffer.setRGB(i, 200, 78, 237);
        count++;
      }
      while (count >= 5 && count < 10) {
        m_ledBuffer.setRGB(i, 255, 255, 255);
        count++;
      }
      if (count == 10) {
        count = 0;
      }
    }
  }

  public static void dumperUpColor() {
    for (int i = 0; i < m_ledBuffer.getLength(); i++) {
      if (i % 2 == 0) {
        m_ledBuffer.setRGB(i, 0, 0, 255);
      } else {
        m_ledBuffer.setRGB(i, 255, 255, 255);
      }
    }
  }
}
