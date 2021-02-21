package Test;

// Import Shell Module
import Shell.*;

// Implement Runnable and Interface
public class Test implements Runnable, IObject {

    Shell shell;

    public static void main(String[] args) {
        // Create Instance of Object to use it in a non-static Context and run it
        IObject object = GenerateInstance.createTest();
        object.run();
    }

    public void run(){
        /*
        Here could be any standard Code that will be executed at Startup of that Instance. (e.g. Nameserver Registration)
        At the End of this Code Block we want to run the Shell so that we can invoke Methods on this Instance.
         */
        runShell(); // This will enter a not ending While-Loop. To exit it, just enter the Command "interrupt".
    }

    public void runShell(){
        shell = new Shell(System.in, System.out); // First Construct the Shell using System.in and System.out (Console In- and Output)
        shell.reg(this); // Register Shell with this Object so that the Shell knows in which Class it has to find annotated Methods
        shell.setPrompt("Hi > "); // Set Prefix (Optional)
        shell.run();
    }

    @Command
    public void listAllCommands(){
        shell.listAllCommands();
    }

    /*
    Cleanly closes Shell without throwing an Exception.
     */
    @Command
    public void interrupt(){
        try {
            shell.close();
            System.out.println("Shell successfully closed!");
        } catch (Exception e){
            //
        }
    }

    @Command
    public void testMethod(){
        System.out.println("Test Method successfully invoked!");
    }

    @Command
    public void printHello(){
        System.out.println("Hello!");
    }
}
