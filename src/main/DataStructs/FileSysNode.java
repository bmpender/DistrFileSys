package main.DataStructs;

import java.util.ArrayList;
import java.util.List;

public class FileSysNode {
    private String name;
    private FileSysNode parent;
    private List<String> machinesContainingNode;
    private List<FileSysNode> children;

    public FileSysNode(String name, List<String> machinesContainingNode) {
        this.name = name;
        this.parent = null;
        this.machinesContainingNode = machinesContainingNode;
        this.children = new ArrayList<>();
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

    public void setParent(FileSysNode parent){
        this.parent = parent;
    }

    public void setChildren(List<FileSysNode> children){
        this.children = children;
    }

    public void setMachinesContainingNode(List<String> machinesContainingNode){
        this.machinesContainingNode = machinesContainingNode;
    }
}
