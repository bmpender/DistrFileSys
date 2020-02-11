package main.FileSys;

import main.DataStructs.FileSysNode;

import java.util.List;

public class Control {

    private FileSysNode fileSys;

    public Control(FileSysNode fileSys){
        this.fileSys = fileSys;
    }

    public FileSysNode getCurDir(){
        return this.fileSys;
    }

    public void processCommand(String cmd){
        if (cmd.startsWith("ls")){
            handlels(cmd);
        }else if (cmd.startsWith("cd")){
            handlecd(cmd);
        }else{
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
}