package main.FileSys;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import main.Communication.sshRemoteExampleUserInfo;
import main.DataStructs.FileSysNode;
import main.DataStructs.Machine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Control {

    private FileSysNode fileSys;
    private Machine masterMachine;


    public Control(FileSysNode fileSys, Machine masterMachine){
        this.fileSys = fileSys;
        this.masterMachine = masterMachine;
    }

    public FileSysNode getCurDir(){
        return this.fileSys;
    }

    public void processCommand(String cmd){
        if (cmd.startsWith("ls")){
            handlels(cmd);
        }else if (cmd.startsWith("cd")){
            handlecd(cmd);
        }else if (cmd.startsWith("cat")){
            handlecat(cmd);
        }else if (cmd.startsWith("rm")){
            handlerm(cmd);
        }else if (cmd.startsWith("mv")){
            handlemv(cmd);
        }else if (cmd.startsWith("cp")){
            handlecp(cmd);
        }else if (cmd.startsWith("push")){
            handlepush(cmd);
        }else if (cmd.startsWith("pull")){
            handlepull(cmd);
        }else {
            System.out.println("Not a valid command.");
        }
    }

    private void handlels(String cmd){
        List<FileSysNode> nodes = fileSys.getChildren();
        for (FileSysNode node : fileSys.getChildren()){
            System.out.println(node.getName());
        }
        System.out.println();
    }

    private void handlecd(String cmd){
        String arg = cmd.replaceFirst("cd ", "");
        String[] folders =  arg.split("/");
        FileSysNode cur = this.fileSys;
        for (String folder : folders){
            if(folder.equals("..")){
                cur = cur.getParent();
            }else if (!folder.equals(".")){
                boolean foundFolder = false;
                for(FileSysNode node : cur.getChildren()){
                    if (node.getName().equals(folder)){
                        foundFolder = true;
                        cur = node;
                        break;
                    }
                }
                if (!foundFolder){
                    cur = this.fileSys;
                    System.out.println("Folder " + folder + " does not exist.");
                    break;
                }
            }
        }
        this.fileSys = cur;
    }

    private void handlecat(String cmd) {
       List<Machine> machinesName = this.fileSys.getMachinesContainingNode();
        if (machinesName.size() < 1){
            System.out.println("No machines have this file.");
            return;
        }
       System.out.println(executeCommandOnMachine(machinesName.get(0), cmd));
    }

    private void handlerm(String cmd) {
    }

    private void handlemv(String cmd) {
    }

    private void handlecp(String cmd) {

    }

    private void handlepush(String cmd) {
        // sshpass -p project490 scp ./random2.txt project490@137.112.224.31:/home/project490
        // TODO: implement the pushing to file system
    }

    private void handlepull(String cmd) {
        String arg = cmd.replaceFirst("pull ", "");
        String[] args = arg.split(" ");
        if (args.length < 2){
            System.out.println("Invalid args");
            return;
        }
        String commandTranslation = "ssshpass -p " + masterMachine.getPassword() + " scp " + args[0] + masterMachine.getUser() + "@" + masterMachine.getHost() + ":" + args[1];
        List<Machine> machinesName = this.fileSys.getMachinesContainingNode();
        if (machinesName.size() < 1){
            System.out.println("No machines have this file.");
            return;
        }
        String returnedExecuteInfo = executeCommandOnMachine(machinesName.get(0), commandTranslation);
        //TODO: implement the pulling from file system
    }

    private String executeCommandOnMachine(Machine machine, String cmd){
        String host = machine.getHost();
        String user = machine.getUser();
        String password = machine.getPassword();
        String command = cmd + "exit\n";
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user,host, 22);
            session.setUserInfo(new sshRemoteExampleUserInfo(user, password));
            session.connect();
            Channel channel = session.openChannel("shell");
            channel.setInputStream(new ByteArrayInputStream(command.getBytes(StandardCharsets.UTF_8)));
            channel.setOutputStream(System.out);
            InputStream in = channel.getInputStream();
            StringBuilder outBuff = new StringBuilder();
            int exitStatus = -1;

            channel.connect();

            while (true) {
                for (int c; ((c = in.read()) >= 0);) {
                    outBuff.append((char) c);
                }

                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    exitStatus = channel.getExitStatus();
                    break;
                }
            }
            channel.disconnect();
            session.disconnect();

            // print the buffer's contents
            return outBuff.toString();
        } catch (IOException | JSchException ioEx) {
            System.err.println(ioEx.toString());
        }
        return "";
    }
}
