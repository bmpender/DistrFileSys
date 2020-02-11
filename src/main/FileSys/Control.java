package FileSys;

import DataStructs.FileSysNode;

public class Control {

    private FileSysNode fileSys;

    public Control(FileSysNode fileSys){
        fileSys = fileSys;
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
        for (FileSysNode node : fileSys.getChildren()){
            System.out.println(node.getName());
        }
    }

    private void handlecd(String cmd){
        String arg = cmd.substring(cmd.indexOf(' '));
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
                    }
                }
                if (!foundFolder){
                    System.out.println("folder " + folder + " does not exist.");
                    break;
                }
            }
        }
    }
}
