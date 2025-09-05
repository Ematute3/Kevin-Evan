package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp(name = "Mechanum")
public class Mechanum extends LinearOpMode {

    // --- QUICK-TUNE SERVO POSITIONS



    @Override
    public void runOpMode() throws InterruptedException {

        // --- MOTORS ---
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "fr");
        DcMotor frontLeft  = hardwareMap.get(DcMotor.class, "fl");
        DcMotor backLeft   = hardwareMap.get(DcMotor.class, "bl");
        DcMotor backRight  = hardwareMap.get(DcMotor.class, "br");



        // --- SERVOS
          // flip up/down

        // --- MOTOR SETUP ---
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


        // Keep lift in place when not powered (helps prevent backdrive)



        //intakeServo.setPosition(gamepad2.left_trigger);

        waitForStart();

        while(opModeIsActive()){

            // --- DRIVE (gamepad1) ---
            double y = gamepad1.left_stick_y; // Forward/backward (reversed for correct direction)
            double x = -gamepad1.left_stick_x; // Strafing with slight adjustment
            double rx = -gamepad1.right_stick_x; // Rotation
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

            // --- LIFT (gamepad2 left stick Y) ---





        }
    }
}