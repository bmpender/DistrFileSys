package DataStructs;

import java.util.List;

public class FileSysNode {
    private String name;
    private FileSysNode parent;
    private List<String> machinesContainingNode;
    private List<FileSysNode> children;

    public FileSysNode(String name, FileSysNode parent, List<String> machinesContainingNode, List<FileSysNode> children) {
        this.name = name;
        this.parent = parent;
        this.machinesContainingNode = machinesContainingNode;
        this.children = children;
    }

    public String getName(){
        return this.name;
    }

    public FileSysNode getParent(){
        return this.parent;
    }

    public List<String> getMachinesContainingNode(){
        return this.machinesContainingNode;
    }

    public List<FileSysNode> getChildren(){
        return this.children;
    }
}
