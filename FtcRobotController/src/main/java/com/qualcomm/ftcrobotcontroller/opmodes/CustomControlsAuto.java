/* Kustom Kontrols */
/* Driver Version */

package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class CustomControlsAuto extends OpMode {

	/*
	 * Note: the configuration of the servos is such that
	 * as the arm servo approaches 0, the arm position moves up (away from the floor).
	 * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
	 */
    // TETRIX VALUES.

    final static double CLAW_MIN_RANGE  = 0.2;
    final static double CLAW_MAX_RANGE  = 0.6;
    public float speedOverride = 3;
    public float directionOverride = 2;
    double clawPosition;
    double clawDelta = 0.05;

    final int ENCODER_CPR = 1120; //Encoder Counts per Revolution
    final double GEAR_RATIO = 1; //Gear Ratio
    final int WHEEL_DIAMETER = 5; //Diameter of the wheel in inches
    final double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;


    DcMotor motorRightFront;
    DcMotor motorRightBack;
    DcMotor motorLeftFront;
    DcMotor motorLeftBack;
    DcMotor clawUpDown;
    DcMotor clawInOut;
    Servo clawL;
    Servo clawR;
    DcMotorController leftMotorController;
    DcMotorController rightMotorController;

    public CustomControlsAuto() {

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

        leftMotorController = hardwareMap.dcMotorController.get("leftMotorController");
        rightMotorController = hardwareMap.dcMotorController.get("rightMotorController");

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
    public void start() {

        moveForward(2);
        //turnRight(false);

        telemetry.addData("We should drive", 2);



    }

    public void moveForward(int feet){
        final int DISTANCE = 12 * feet; //Distance in inches to drive
        final double ROTATIONS = DISTANCE / CIRCUMFERENCE;
        final double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;

        telemetry.addData("We are driving rotations: ", COUNTS);

        motorLeftFront.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorLeftBack.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorRightFront.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorRightBack.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        motorLeftFront.setPower(0.1);
        motorLeftFront.setTargetPosition((int) COUNTS);
        motorLeftFront.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorLeftBack.setPower(0.1);
        motorLeftBack.setTargetPosition((int) COUNTS);
        motorLeftBack.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        motorRightFront.setPower(0.1);
        motorRightFront.setTargetPosition((int) COUNTS);
        motorRightFront.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRightBack.setPower(0.1);
        motorRightBack.setTargetPosition((int) COUNTS);
        motorRightBack.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    public void turnRight(boolean right){
        final int DISTANCE = 12; //Distance in inches to drive
        final double ROTATIONS = DISTANCE / CIRCUMFERENCE;
        final double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;

        if(right){
            motorRightFront.setTargetPosition((int) COUNTS);
            motorRightFront.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
            motorRightBack.setTargetPosition((int) COUNTS);
            motorRightBack.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        }else{
            motorLeftFront.setTargetPosition((int) COUNTS);
            motorLeftFront.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
            motorLeftBack.setTargetPosition((int) COUNTS);
            motorLeftBack.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        }
    }

    @Override
    public void loop() {
//        motor_7.setTargetPosition((int) COUNTS);
//        motor_7.setPower(0.1);
//        motor_2.setPower(0.1);
//        motor_4.setPower(0.1);
//        motor_6.setPower(0.1);
//        motor_1.setPower(0.1);
//        motor_3.setPower(0.1);
//        motor_5.setPower(0.1);
//
//        if (Math.abs(motor_7.getCurrentPosition() - COUNTS) < 5))
//        {​
//            motor_7.setPower(0);
//            motor_2.setPower(0);
//            motor_4.setPower(0);
//            motor_6.setPower(0);
//            motor_1.setPower(0);
//            motor_3.setPower(0);
//            motor_5.setPower(0);
//
//        }

        stop();

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
