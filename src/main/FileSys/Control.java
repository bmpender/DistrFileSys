package main.FileSys;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import main.Communication.sshRemoteExampleUserInfo;
import main.DataStructs.FileSysNode;
import main.DataStructs.Machine;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Control {

    private FileSysNode root;
    private FileSysNode fileSys;
    private Machine masterMachine;


    public Control(FileSysNode fileSys, Machine masterMachine){
        this.fileSys = fileSys;
        this.masterMachine = masterMachine;
        this.root = fileSys;
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
        }//else if (cmd.startsWith("cp")){
         //   handlecp(cmd);}
        else if (cmd.startsWith("push")){
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
        try {
            this.fileSys = findFileNode(arg, this.fileSys);
        }catch(FileNotFoundException e){
            return;
        }
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
        String arg = cmd.replaceFirst("rm ", "");
        try {
            FileSysNode cur = findFileNode(arg.substring(1), root);
            List<FileSysNode> parentChildren = cur.getParent().getChildren();
            parentChildren.remove(cur);
            cur.getParent().setChildren(parentChildren);
        }catch(FileNotFoundException e){
            return;
        }

        for(Machine machine : fileSys.getMachinesContainingNode()){
            String output = executeCommandOnMachine(machine, cmd);
        }
    }

    private void handlemv(String cmd) {
        String arg = cmd.replaceFirst("mv ", "");
        String[] args = arg.split(" ");
        try {
            FileSysNode curFrom = findFileNode(args[0], this.fileSys);
            FileSysNode curTo = findFileNode(args[1], this.fileSys);

            curFrom.getParent().getChildren().remove(curFrom);

            curFrom.setParent(curTo);

            curTo.getChildren().add(curFrom);

        }catch(FileNotFoundException e){
            return;
        }

        for(Machine machine : fileSys.getMachinesContainingNode()){
            String output = executeCommandOnMachine(machine, cmd);
        }
    }

//    private void handlecp(String cmd) {
//        String arg = cmd.replaceFirst("cp ", "");
//        String[] args = arg.split(" ");
//        if (args.length < 2){
//            System.err.println("Invalid args");
//            return;
//        }
//        String to = args[0];
//        String from = args[1];
//
//        String[] folders =  to.split("/");
//        FileSysNode curTo = this.fileSys;
//        for (String folder : folders){
//            if(folder.equals("..")){
//                curTo = curTo.getParent();
//            }else if (!folder.equals(".")){
//                boolean foundFolder = false;
//                for(FileSysNode node : curTo.getChildren()){
//                    if (node.getName().equals(folder)){
//                        foundFolder = true;
//                        curTo = node;
//                        break;
//                    }
//                }
//                if (!foundFolder){
//                    curTo = this.fileSys;
//                    System.out.println("Folder " + folder + " does not exist.");
//                    break;
//                }
//            }
//        }
//
//        String[] foldersFrom =  from.split("/");
//        FileSysNode curFrom = this.fileSys;
//        for (String folder : foldersFrom){
//            if(folder.equals("..")){
//                curFrom = curFrom.getParent();
//            }else if (!folder.equals(".")){
//                boolean foundFolder = false;
//                for(FileSysNode node : curFrom.getChildren()){
//                    if (node.getName().equals(folder)){
//                        foundFolder = true;
//                        curFrom = node;
//                        break;
//                    }
//                }
//                if (!foundFolder){
//                    curFrom = this.fileSys;
//                    System.err.println("Folder " + folder + " does not exist.");
//                    break;
//                }
//            }
//        }
//
//
//
//
//    }

    private void handlepush(String cmd) {
        String arg = cmd.replaceFirst("push ", "");
        String[] args = arg.split(" ");
        if (args.length < 2){
            System.err.println("Invalid args");
            return;
        }
        String[] names = args[0].split("/");
        if (names.length < 1){
            System.err.println("Not a path");
            return;
        }
        String fileName = names[names.length - 1];
        Machine machine = root.getMachinesContainingNode().get(0);
        String commandTranslation = "sshpass -p " + machine.getPassword() + " scp " + args[0] + " " + machine.getUser() + "@" + machine.getHost() + ":" + args[1];
        try {
            Process p = Runtime.getRuntime().exec(commandTranslation);
            p.waitFor();
        }catch(Exception e){
            e.printStackTrace();
        }

        List<Machine> machines = new ArrayList<>();
        machines.add(machine);
        FileSysNode newNode = new FileSysNode(fileName, machines);

        try {
            FileSysNode parent = findFileNode(args[1].substring(1), root);
            newNode.setParent(parent);
            parent.getChildren().add(newNode);
        }catch(FileNotFoundException e){
            return;
        }

    }

    private void handlepull(String cmd) {
        String arg = cmd.replaceFirst("pull ", "");
        String[] args = arg.split(" ");
        if (args.length < 2){
            System.err.println("Invalid args");
            return;
        }
        String commandTranslation = "sshpass -p " + masterMachine.getPassword() + " scp " + args[0] + " " + masterMachine.getUser() + "@" + masterMachine.getHost() + ":" + args[1];
        List<Machine> machinesName = this.fileSys.getMachinesContainingNode();
        if (machinesName.size() < 1){
            System.out.println("No machines have this file.");
            return;
        }
        String returnedExecuteInfo = executeCommandOnMachine(machinesName.get(0), commandTranslation);
    }

    private String executeCommandOnMachine(Machine machine, String cmd){
        String host = machine.getHost();
        String user = machine.getUser();
        String password = machine.getPassword();
        String command = cmd + "\nexit\n";
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

    private FileSysNode findFileNode(String cmd, FileSysNode relativeNode) throws FileNotFoundException {
        String[] folders =  cmd.split("/");
        FileSysNode cur = relativeNode;
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
                    cur = relativeNode;
                    System.err.println("Folder " + folder + " does not exist.");
                    throw new FileNotFoundException();
                }
            }
        }
        return cur;
    }
}
