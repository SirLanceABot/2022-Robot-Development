package frc.robot;

import java.lang.invoke.MethodHandles;

// *** IMPORTANT - PLEASE READ ***
// 1. Put your test code in your own frc.test.[yourname]Test.java file
// 2. Uncomment one of the IMPORT statements below
// 3. Uncomment one of the CLASS VARIABLES below
// 4. Test your code
// 5. Comment your IMPORT statement and CLASS VARIABLE statement when finished

import frc.test.BlankTest;
// import frc.test.AburriTest;
// import frc.test.DfifeTest;
// import frc.test.EmeaselTest;
// import frc.test.IguptaTest;
// import frc.test.JsawyerTest;
// import frc.test.MmcconomyTest;
// import frc.test.OdomsTest;
// import frc.test.RkapaluruTest;
// import frc.test.TjadhavTest;

public class TestMode implements ModeTransition
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***
    private static final BlankTest myTest = new BlankTest();
    // private static final AburriTest myTest = new AburriTest();
    // private static final DfifeTest myTest = new DfifeTest();
    // private static final EmeaselTest myTest = new EmeaselTest();
    // private static final IguptaTest myTest = new IguptaTest();
    // private static final JsawyerTest myTest = new JsawyerTest();
    // private static final MmcconomyTest myTest = new MmcconomyTest();
    // private static final OdomsTest myTest = new OdomsTest();
    // private static final RkapaluruTest myTest = new RkapaluruTest();
    // private static final TjadhavTest myTest = new TjadhavTest();

    // *** CLASS CONSTRUCTOR ***
    public TestMode()
    {

    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        myTest.init();
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        myTest.periodic();
    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        myTest.exit();
    }    
}
