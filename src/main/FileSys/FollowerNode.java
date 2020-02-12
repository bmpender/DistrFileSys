package main.FileSys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FollowerNode {
    //TODO: Make remote connection and execute the command

    private static void localCMDExec(String cmd){
        // This is for making linux command
        // tested for cat,cp,rm,mv
        String s;
        Process p;
        try {
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println(s); //output each line of the command
            p.waitFor();
            System.out.println ("exit: " + p.exitValue());
            p.destroy();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
