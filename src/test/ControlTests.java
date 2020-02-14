package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import main.DataStructs.FileSysNode;
import main.DataStructs.Machine;
import main.FileSys.Control;
import org.junit.jupiter.api.Test;



import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ControlTests {

    @Test
    public void testlsCommand (){
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));


        List<FileSysNode> rootFiles = new ArrayList<>();
        FileSysNode randomDir = new FileSysNode("randomDir", new ArrayList<>());
        FileSysNode secondRandomDir = new FileSysNode("secondRandomDir", new ArrayList<>());

        rootFiles.add(randomDir);
        rootFiles.add(secondRandomDir);

        FileSysNode root = new FileSysNode("root", new ArrayList<>());
        randomDir.setParent(root);
        secondRandomDir.setParent(root);

        root.setChildren(rootFiles);

        Control ctrl = new Control(root, new Machine());
        ctrl.processCommand("ls");
        assertEquals("randomDir\nsecondRandomDir\n\n", outContent.toString());

        PrintStream originalOut = System.out;
        System.setOut(originalOut);

    }

    @Test
    public void testEmptyls (){
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        FileSysNode root = new FileSysNode("root", new ArrayList<>());
        Control ctrl = new Control(root, new Machine());


        ctrl.processCommand("ls");


        assertEquals("\n", outContent.toString());

        PrintStream originalOut = System.out;
        System.setOut(originalOut);
    }

    @Test
    public void testcdCommand(){
        List<FileSysNode> rootFiles = new ArrayList<>();
        FileSysNode randomDir = new FileSysNode("randomDir", new ArrayList<>());
        FileSysNode secondRandomDir = new FileSysNode("secondRandomDir", new ArrayList<>());

        rootFiles.add(randomDir);
        rootFiles.add(secondRandomDir);

        FileSysNode root = new FileSysNode("root", new ArrayList<>());
        randomDir.setParent(root);
        secondRandomDir.setParent(root);

        root.setChildren(rootFiles);

        Control ctrl = new Control(root, new Machine());
        ctrl.processCommand("cd randomDir");

        assertEquals("randomDir", ctrl.getCurDir().getName());

    }

    @Test
    public void testInvalidcd(){
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        List<FileSysNode> rootFiles = new ArrayList<>();
        FileSysNode randomDir = new FileSysNode("randomDir", new ArrayList<>());
        FileSysNode secondRandomDir = new FileSysNode("secondRandomDir", new ArrayList<>());

        rootFiles.add(randomDir);
        rootFiles.add(secondRandomDir);

        FileSysNode root = new FileSysNode("root", new ArrayList<>());
        randomDir.setParent(root);
        secondRandomDir.setParent(root);

        root.setChildren(rootFiles);

        Control ctrl = new Control(root, new Machine());
        ctrl.processCommand("cd typo");

        assertEquals("root", ctrl.getCurDir().getName());

        assertEquals("Folder typo does not exist.\n", outContent.toString());

        PrintStream originalOut = System.out;
        System.setOut(originalOut);
    }

    @Test
    public void testMultiLevelcd(){
        List<FileSysNode> rootFiles = new ArrayList<>();
        FileSysNode randomDir = new FileSysNode("randomDir", new ArrayList<>());

        rootFiles.add(randomDir);

        List<FileSysNode>  secondLayer = new ArrayList<>();
        FileSysNode secondRandomDir = new FileSysNode("secondRandomDir", new ArrayList<>());

        secondLayer.add(secondRandomDir);

        FileSysNode root = new FileSysNode("root", new ArrayList<>());
        randomDir.setParent(root);

        secondRandomDir.setParent(randomDir);

        root.setChildren(rootFiles);
        randomDir.setChildren(secondLayer);

        Control ctrl = new Control(root, new Machine());
        ctrl.processCommand("cd randomDir/../randomDir/secondRandomDir");

        assertEquals("secondRandomDir", ctrl.getCurDir().getName());
    }


}
