package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "TankDrive_KC")
public class KevinTest extends OpMode {

    DcMotor leftMotor, rightMotor;

    @Override
    public void init() {
        // map motors
        leftMotor  = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");

        // reverse one side so forward stick makes robot go forward
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addLine("Tank Drive Initialized");
    }

    @Override
    public void loop() {
        // left stick controls left motor, right stick controls right motor
        double leftPower  = -gamepad1.left_stick_y;   // invert so up = forward
        double rightPower = -gamepad1.right_stick_y;

        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);

        telemetry.addData("Left Power", leftPower);
        telemetry.addData("Right Power", rightPower);
    }
}
