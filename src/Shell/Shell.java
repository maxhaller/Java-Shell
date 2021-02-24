package Shell;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Shell {

    /**
     * @param in        Command Input (usually System.in)
     * @param out       Shell Output (usually System.out)
     * @param map       Map storing Method Name and Method Object
     * @param o         Object on which Method is to be invoked
     * @param prompt    Configurable Prefix
     */

    private final BufferedReader in;
    private final PrintWriter out;
    private final HashMap<String, Method> map;
    private Object o;
    private String prompt = "";

    /*
    Constructor
     */
    public Shell(InputStream in, PrintStream out){
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = new PrintWriter(out);
        this.map = new HashMap<>();
    }

    /*
    Collects all with @Command annotated Methods and maps their Names to their invokable Object
     */
    public void reg(Object o){
        this.o = o;
        for(Method method : this.o.getClass().getMethods()){
            if(method.isAnnotationPresent(Command.class)){
                map.put(method.getName(), method);
            }
        }
    }

    /*
    Runs Shell: Waits for User to enter Command and invokes corresponding Method if it's been annotated with @Command
     */
    public void run() {
        try {
            out.print(prompt);
            out.flush();
            String s;
            while ((s = in.readLine()) != null) {
                out.print(prompt);
                String[] parts;
                parts = null;
                if(s.contains("(") && s.contains(")")){
                    parts = s.split("\\(");
                    s = parts[0];
                    String t = parts[1].substring(0, parts[1].length()-1);
                    parts = t.split(", ");

                }
                try {
                    if(map.containsKey(s)) {
                        out.println("Invoking " + s);
                        out.flush();
                        if(parts != null){
                            map.get(s).invoke(o, (Object[]) parts);
                        } else {
                            map.get(s).invoke(o);
                        }
                    } else {
                        out.println("No such command found: " + s);
                    }
                } catch (IllegalAccessException | NullPointerException e){
                    out.println("No such command found: " + s);
                } catch (InvocationTargetException e){
                    out.println("Invocation failed: " + s);
                }
                if(!s.equals("interrupt")) {
                    out.print(prompt);
                }
                out.flush();
            }
        } catch (IOException e){
            //
        }
    }

    /*
    Lists all Commands annotated with @Command
     */
    public void listAllCommands(){
        map.keySet().forEach(out::println);
        out.flush();
    }

    /*
    Sets individual Prefix
     */
    public void setPrompt(String s){
        prompt = s;
    }

    /*
    Closes Shell in a clean Manner
     */
    public void close() {
        try {
            in.close();
        } catch (IOException e){
            //
        }
    }
}

