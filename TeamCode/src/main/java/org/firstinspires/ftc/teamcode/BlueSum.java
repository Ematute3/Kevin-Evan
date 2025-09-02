package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp(name = "EvanCode")
public class BlueSum extends LinearOpMode {

    // --- QUICK-TUNE SERVO POSITIONS
    private static final double OUTTAKE_CLAW_OPEN   = 0.85;
    private static final double OUTTAKE_CLAW_CLOSED = -1.00;
    private static final double OUTTAKE_FLIP_UP     = -1.00;
    private static final double OUTTAKE_FLIP_DOWN   = 0.10;
    private static final double OUTTAKE_PIVOT_SCORE = 1.00; // tilt toward basket/bar
    private static final double OUTTAKE_PIVOT_HOME  = 0.00; // travel position
    private static final double LINKAGE_EXTEND_R    = 1.00; // right linkage extended
    private static final double LINKAGE_RETRACT_R   = 0.00; // right linkage retracted
    private static final double LINKAGE_EXTEND_L    = 0.00; // left linkage mirror
    private static final double LINKAGE_RETRACT_L   = 1.00;
    private static final double INTAKE_DEPLOY       = 0.80; // main intake down
    private static final double INTAKE_RETRACT      = -1.00; // main intake up
    private static final double INTAKE_ARM_IN       = 0.15; // side arms tucked
    private static final double INTAKE_ARM_OUT      = 0.85; // side arms out



    @Override
    public void runOpMode() throws InterruptedException {

        // --- MOTORS ---
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "fr");
        DcMotor frontLeft  = hardwareMap.get(DcMotor.class, "fl");
        DcMotor backLeft   = hardwareMap.get(DcMotor.class, "bl");
        DcMotor backRight  = hardwareMap.get(DcMotor.class, "br");

        DcMotor liftLeft   = hardwareMap.get(DcMotor.class,"liftMotor1");
        DcMotor liftRight  = hardwareMap.get(DcMotor.class,"liftMotor2");

        // --- SERVOS
        Servo rightLinkageServo= hardwareMap.get(Servo.class,"rightLinkageServo");
        Servo leftLinkageServo  = hardwareMap.get(Servo.class,"leftLinkageServo");

        //Servo intakeServo       = hardwareMap.get(Servo.class,"intakeServo");// main deploy/retract
        Servo rightIntakeServo  = hardwareMap.get(Servo.class,"rightIntakeServo"); // side arm
        Servo leftIntakeServo   = hardwareMap.get(Servo.class,"leftIntakeServo");  // side arm

        Servo outtakeClaw       = hardwareMap.get(Servo.class,"outtakeClaw");      // open/close
        Servo outtakePivot      = hardwareMap.get(Servo.class,"outtakePivot");     // tilt
        Servo outtakeFlip       = hardwareMap.get(Servo.class,"outtakeFlip");      // flip up/down

        // --- MOTOR SETUP ---
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Keep lift in place when not powered (helps prevent backdrive)
        liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // --- SAFE START SERVO POSITIONS ---
        outtakeClaw.setPosition(OUTTAKE_CLAW_CLOSED);
        outtakeFlip.setPosition(OUTTAKE_FLIP_DOWN);
        outtakePivot.setPosition(OUTTAKE_PIVOT_HOME);


        rightIntakeServo.setPosition(INTAKE_ARM_IN);
        leftIntakeServo.setPosition(INTAKE_ARM_IN);

        rightLinkageServo.setPosition(LINKAGE_RETRACT_R);
        leftLinkageServo.setPosition(LINKAGE_RETRACT_L);
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
            double liftPower = -gamepad2.left_stick_y; // push up = lift up
            if (Math.abs(liftPower) > 0.05) {
                // If your lifts are mirrored and fight each other, flip one sign here:
                liftLeft.setPower(liftPower);
                liftRight.setPower(-liftPower); // change to (-liftPower) if needed
            } else {
                liftLeft.setPower(0);
                liftRight.setPower(0);
            }

            // --- OUTTAKE CLAW  ---
            if (gamepad2.dpad_left) {
                outtakeClaw.setPosition(OUTTAKE_CLAW_OPEN);
            }else if (gamepad2.dpad_right) {
                outtakeClaw.setPosition(OUTTAKE_CLAW_CLOSED);
            }
            // --- OUTTAKE FLIP (X = up, Y = down) ---


            // made it negative is it right direction
            //not moving rn fix it
            if (gamepad2.x) {
                outtakeFlip.setPosition(OUTTAKE_FLIP_UP);
            } else if (gamepad2.y) {
                outtakeFlip.setPosition(OUTTAKE_FLIP_DOWN);
            }
           /* if (gamepad2.left_trigger > 0.1) {    // pulled left trigger
                intakeServo.setPosition(INTAKE_FRONT_INTAKE);
            } else if (gamepad2.right_trigger > 0.1) {   // pulled right trigger
                intakeServo.setPosition(INTAKE_FRONT_OUTAKE);
            }

            */

            // --- OUTTAKE PIVOT (D-Pad Left/Right) ---
            if (gamepad2.dpad_up) {
                outtakePivot.setPosition(OUTTAKE_PIVOT_HOME);
            } else if (gamepad2.dpad_down) {
                outtakePivot.setPosition(OUTTAKE_PIVOT_SCORE);
            }

            // --- INTAKE MAIN DEPLOY/RETRACT (A/B) ---
            //decrease intake deploy increase intke retract
            //decrease by 0.5 increase by 1
            //decrease more
            if (gamepad2.a) {
                leftIntakeServo.setPosition(INTAKE_DEPLOY);
                rightIntakeServo.setPosition(-INTAKE_DEPLOY);
            } else if (gamepad2.b) {
                leftIntakeServo.setPosition(INTAKE_RETRACT);
                rightIntakeServo.setPosition(INTAKE_RETRACT);
            }

            // --- INTAKE SIDE ARMS (Left/Right Bumper) ---
            if (gamepad2.left_bumper) {
                // extend linkages & swing intake arms out
                rightLinkageServo.setPosition(LINKAGE_EXTEND_R);
                leftLinkageServo.setPosition(LINKAGE_EXTEND_L);
                rightIntakeServo.setPosition(INTAKE_ARM_OUT);
                leftIntakeServo.setPosition(INTAKE_ARM_OUT);
            } else if (gamepad2.right_bumper) {
                rightLinkageServo.setPosition(LINKAGE_RETRACT_R);
                leftLinkageServo.setPosition(LINKAGE_RETRACT_L);
                rightIntakeServo.setPosition(INTAKE_ARM_IN);
                leftIntakeServo.setPosition(INTAKE_ARM_IN);
            }

            // --- TELEMETRY ---
            telemetry.addData("Drive", "FL %.2f  FR %.2f  BL %.2f  BR %.2f",
                    frontLeftPower, frontRightPower, backLeftPower, backRightPower);
            telemetry.addData("Lift", "Power %.2f  Lpos %d  Rpos %d",
                    liftPower, liftLeft.getCurrentPosition(), liftRight.getCurrentPosition());
            telemetry.addData("Outtake", "Claw %.2f  Flip %.2f  Pivot %.2f",
                    outtakeClaw.getPosition(), outtakeFlip.getPosition(), outtakePivot.getPosition());
            telemetry.addData("Intake", "LArm %.2f  RArm %.2f",
                     leftIntakeServo.getPosition(), rightIntakeServo.getPosition());
            telemetry.addData("Linkage", "L %.2f  R %.2f",
                    leftLinkageServo.getPosition(), rightLinkageServo.getPosition());
            telemetry.update();
        }
    }
}
