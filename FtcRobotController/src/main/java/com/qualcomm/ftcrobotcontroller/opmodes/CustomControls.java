/* Kustom Kontrols */
/* Driver Version */

package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class CustomControls extends OpMode {

	/*
	 * Note: the configuration of the servos is such that
	 * as the arm servo approaches 0, the arm position moves up (away from the floor).
	 * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
	 */
	// TETRIX VALUES.

	final static double CLAW_MIN_RANGE  = 0.0;
	final static double CLAW_MAX_RANGE  = 0.6;
	public float speedOverride = 3;
	public float directionOverride = 2;
	double clawPosition;
	double clawDelta = 0.05;

	DcMotor motorRightFront;
	DcMotor motorRightBack;
	DcMotor motorLeftFront;
	DcMotor motorLeftBack;
	DcMotor clawUpDown;
	DcMotor clawInOut;
	Servo clawL;
	Servo clawR;

	public CustomControls() {

	}

	@Override
	public void init() {

		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

		/*
		 * For the demo Tetrix K9 bot we assume the following,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_1" is on the right side of the bot.
		 *   "motor_2" is on the left side of the bot and reversed.
		 *
		 * We also assume that there are two servos "servo_1" and "servo_6"
		 *    "servo_1" controls the arm joint of the manipulator.
		 *    "servo_6" controls the claw joint of the manipulator.
		 */

		motorLeftFront = hardwareMap.dcMotor.get("leftFront");
		motorLeftBack = hardwareMap.dcMotor.get("leftBack");
		motorRightFront = hardwareMap.dcMotor.get("rightFront");
		motorRightBack = hardwareMap.dcMotor.get("rightBack");

		motorRightFront.setDirection(DcMotor.Direction.REVERSE);
		motorRightBack.setDirection(DcMotor.Direction.REVERSE);

		clawUpDown = hardwareMap.dcMotor.get("clawUpDown");
		clawInOut = hardwareMap.dcMotor.get("clawInOut");

		clawL = hardwareMap.servo.get("servo_1");
		clawR = hardwareMap.servo.get("servo_6");

		// assign the starting position of the wrist and claw
		clawPosition = 0.2;
	}

	@Override
	public void loop() {

		// throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
		// 1 is full down
		// direction: left_stick_x ranges from -1 to 1, where -1 is full left
		// and 1 is full right

		if(gamepad1.a){
			speedOverride = (float) 0.2;
			directionOverride = 1;
		}else{
			speedOverride = 3;
			directionOverride = (float) 1.7;
		}

		float throttle = gamepad1.left_stick_y / speedOverride;
		float direction = (gamepad1.left_stick_x / (directionOverride) );
		float right = throttle + direction;
		float left = throttle - direction;

		// clip the right/left values so that the values never exceed +/- 1
		right = Range.clip(right, -1, 1);
		left = Range.clip(left, -1, 1);

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		right = -(float)scaleInput(right);
		left =  -(float)scaleInput(left);

		// write the values to the motors
		motorLeftFront.setPower(left);
		motorLeftBack.setPower(left);
		motorRightFront.setPower(right);
		motorRightBack.setPower(right);

		// MOTOR CONTROLLERS FOR ARM
//        throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
//        1 is full down
//        direction: left_stick_x ranges from -1 to 1, where -1 is full left
//        and 1 is full right

		// Code for arm going in and out
//        boolean armIn = gamepad2.left_bumper;
//		float armOut = gamepad2.left_trigger;
//		float inOut = 0;
//		if (armIn){
//			inOut = -1;
//		}else if(armOut > 0){
//			inOut = 1;
//		}
		// float inOut = 0;
		float inOut = gamepad2.left_stick_y;

		telemetry.addData("Left Stick", inOut);
		inOut = Range.clip(inOut, -1, 1);
		inOut = (float)scaleInput(inOut);
		clawInOut.setPower(inOut);

		float upDown = gamepad2.right_stick_y;
		telemetry.addData("Right Stick", upDown);
		upDown = Range.clip(upDown, -1, 1);
		upDown = (float)scaleInput(upDown);
		clawUpDown.setPower(upDown);

//		// update the position of the arm.
		if (gamepad2.a) {
//			// if the A button is pushed on gamepad1, increment the position of
//			// the arm servo.
			clawPosition += clawDelta;
		}

		if (gamepad2.b) {
			clawPosition -= clawDelta;
		}
//
//        // clip the position values so that they never exceed their allowed range
	       clawPosition = Range.clip(clawPosition, CLAW_MIN_RANGE, CLAW_MAX_RANGE);
//
//		// write position values to the wrist and claw servo
		clawL.setPosition(1-clawPosition);
		clawR.setPosition(clawPosition);



		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        //telemetry.addData("Text", "*** Robot Data***");
		//telemetry.addData("Claw" + String.format("%.2f", inOut));
        //telemetry.addData("arm", "arm:  " + String.format("%.2f", armPosition));
        //telemetry.addData("claw", "claw:  " + String.format("%.2f", clawPosition));
        //telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        //telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));

	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}


	/*
	 * This method scales the joystick input so for low joystick values, the
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
	double scaleInput(double dVal)  {
		double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
				0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

		// get the corresponding index for the scaleInput array.
		int index = (int) (dVal * 16.0);

		// index should be positive.
		if (index < 0) {
			index = -index;
		}

		// index cannot exceed size of array minus 1.
		if (index > 16) {
			index = 16;
		}

		// get value from the array.
		double dScale = 0.0;
		if (dVal < 0) {
			dScale = -scaleArray[index];
		} else {
			dScale = scaleArray[index];
		}
		// return scaled value.
		return dScale;
	}

}
