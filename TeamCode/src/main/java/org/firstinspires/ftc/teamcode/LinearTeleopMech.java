package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "EvanCode")
public class LinearTeleopMech extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor frontRight = hardwareMap.get(DcMotor.class, "fr");
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "fl");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "bl");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "br");
        DcMotor liftLeft = hardwareMap.get(DcMotor.class,"liftl");
        DcMotor liftRight = hardwareMap.get(DcMotor.class,"liftr");
        DcMotor armRight = hardwareMap.get(DcMotor.class,"rarm");



        Servo insideSweep = hardwareMap.get(Servo.class, "swivel_claw");
        Servo outsideClaw = hardwareMap.get(Servo.class, "oclaw");



        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while(opModeIsActive()){

//            double armPower = gamepad2.right_stick_x;
//            if (Math.abs(armPower) > 0.05) {
//                armRight.setPower(armPower);
//
//            } else {
//                armRight.setPower(0);
//
//            }
//
//
//            // Outside Claw Control
//            if (gamepad2.dpad_down) {
//                if (!isOutsideClawOpen) {
//                    outsideClaw.setPosition(1); // Open the claw
//                    isOutsideClawOpen = true;    // Update state
//                }
//            } else if (gamepad2.dpad_up) {
//                if (isOutsideClawOpen) {
//                    outsideClaw.setPosition(0); // Close the claw
//                    isOutsideClawOpen = false;   // Update state
//                }
//            }
//
//            // Inside Sweep Control
//            if (gamepad2.dpad_left) {
//                if (!isInsideSweepOut) {
//                    insideSweep.setPosition(1); // Sweep out
//                    isInsideSweepOut = true;    // Update state
//                }
//            } else if (gamepad2.dpad_right) {
//                if (isInsideSweepOut) {
//                    insideSweep.setPosition(0); // Sweep in
//                    isInsideSweepOut = false;   // Update state
//                }
//            }
//
//            // Lift control using gamepad2
//            double liftPower = -gamepad2.right_stick_y; // Reversed for correct direction
//            if (Math.abs(liftPower) > 0.05) {
//                liftLeft.setPower(liftPower);
//                liftRight.setPower(liftPower *-1);
//            } else {
//                liftLeft.setPower(0);
//                liftRight.setPower(0);
//            }

            // Robot movement using gamepad1
            double y = gamepad1.left_stick_y; // Forward/backward (reversed for correct direction)
            double x = -gamepad1.left_stick_x; // Strafing with slight adjustment
            double rx = -gamepad1.right_stick_x; // Rotation

            // Calculate motor power-07
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            if (frontLeftPower == 0 && backLeftPower == 0 && frontRightPower == 0 && backRightPower == 0) {
                frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }

            // Apply power to motors
            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);

            //  LIFT CONTROL (gamepad2 left stick Y)
            double liftPower = -gamepad2.left_stick_y; // push stick up = lift up
            if (Math.abs(liftPower) > 0.05) {
                liftLeft.setPower(liftPower);
                liftRight.setPower(liftPower);  // if your lift is mirrored, flip sign (*-1)
            } else {
                liftLeft.setPower(0);
                liftRight.setPower(0);
            }

            // ARM CONTROL (gamepad2 right stick X)
            double armPower = gamepad2.right_stick_x;
            if (Math.abs(armPower) > 0.05) {
                armRight.setPower(armPower);
            } else {
                armRight.setPower(0);
            }

            // OUTSIDE CLAW CONTROL (Dpad Up/Down)
            if (gamepad2.dpad_down) {
                outsideClaw.setPosition(1);  // Open claw
            } else if (gamepad2.dpad_up) {
                outsideClaw.setPosition(0);  // Close claw
            }

            // INSIDE SWEEP CONTROL (Dpad Left/Right)
            if (gamepad2.dpad_left) {
                insideSweep.setPosition(1);  // Sweep out
            } else if (gamepad2.dpad_right) {
                insideSweep.setPosition(0);  // Sweep in
            }

        }
    }
}
