package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class FirstTeleop extends OpMode {

    // Declare state tracking variables for the servos
    private boolean isOutsideClawOpen = false;
    private boolean isInsideSweepOut = false;

    // Declare motors and servos
    DcMotor frontRight, frontLeft, backLeft, backRight;
    DcMotor liftLeft, liftRight, armRight;
    Servo outsideClaw, insideSweep;

    @Override
    public void init() {
        // Get motors and servos from the hardware map
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");

        // Lift & Arm motors
        liftLeft = hardwareMap.get(DcMotor.class, "liftl");
        liftRight = hardwareMap.get(DcMotor.class, "liftr");
        armRight = hardwareMap.get(DcMotor.class, "rarm");

        // Servos
        outsideClaw = hardwareMap.get(Servo.class, "oclaw2");
        insideSweep = hardwareMap.get(Servo.class, "isweep");

        // Set motor directions
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        liftRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // Set motor modes
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        // Arm control using gamepad2
        double armPower = gamepad2.right_stick_x;
        if (Math.abs(armPower) > 0.05) {
            armRight.setPower(armPower);
        } else {
            armRight.setPower(0);
        }

        // Outside Claw Control
        if (gamepad2.dpad_down) {
            if (!isOutsideClawOpen) {
                outsideClaw.setPosition(1); // Open the claw
                isOutsideClawOpen = true;    // Update state
            }
        } else if (gamepad2.dpad_up) {
            if (isOutsideClawOpen) {
                outsideClaw.setPosition(0); // Close the claw
                isOutsideClawOpen = false;   // Update state
            }
        }

        // Inside Sweep Control
        if (gamepad2.dpad_left) {
            if (!isInsideSweepOut) {
                insideSweep.setPosition(1); // Sweep out
                isInsideSweepOut = true;    // Update state
            }
        } else if (gamepad2.dpad_right) {
            if (isInsideSweepOut) {
                insideSweep.setPosition(0); // Sweep in
                isInsideSweepOut = false;   // Update state
            }
        }

        // Lift control using gamepad2
        double liftPower = -gamepad2.right_stick_y; // Reversed for correct direction
        if (Math.abs(liftPower) > 0.05) {
            liftLeft.setPower(liftPower);
            liftRight.setPower(liftPower);
        } else {
            liftLeft.setPower(0);
            liftRight.setPower(0);
        }

        // Robot movement using gamepad1
        double y = -gamepad1.left_stick_y; // Forward/backward (reversed for correct direction)
        double x = gamepad1.left_stick_x * 1.1; // Strafing with slight adjustment
        double rx = gamepad1.right_stick_x; // Rotation

        // Apply dead zone
        if (Math.abs(y) < 0.05) y = 0;
        if (Math.abs(x) < 0.05) x = 0;
        if (Math.abs(rx) < 0.05) rx = 0;

        // Calculate motor power
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        // Apply power to motors
        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }

    @Override
    public void stop() {
        // Add any code to stop the robot or reset motors if necessary
    }
}
