package main.UserInterface;

import main.DataStructs.FileSysNode;
import main.DataStructs.Machine;
import main.FileSys.Control;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInterfaceMain {
	
	
	public static void main(String[] args) {
		System.out.printf(" --------PCH Distributed System-------- \n");
		if(args.length < 6){
			System.err.println("Need host, username, and password for FileSystem Machine and Master Machine");
		}
		String hostFS = args[0];
		String userFS = args[1];
		String passwordFS = args[2];
		String hostMaster = args[3];
		String userMaster = args[4];
		String passwordMaster = args[5];

		Machine machineFS = new Machine(hostFS, userFS, passwordFS);
		Machine machineMaster = new Machine(hostMaster, userMaster, passwordMaster);

		List<Machine> fsMachines = new ArrayList<>();
		fsMachines.add(machineFS);
		FileSysNode root = new FileSysNode("", fsMachines);

		FileSysNode home = new FileSysNode("home", fsMachines);
		home.setParent(root);
		List<FileSysNode> rootChildren = new ArrayList<>();
		rootChildren.add(home);
		root.setChildren(rootChildren);
		FileSysNode project490 = new FileSysNode("project490", fsMachines);
		project490.setParent(home);
		List<FileSysNode> homeChildren = new ArrayList<>();
		homeChildren.add(project490);
		home.setChildren(homeChildren);

		Control ctrl = new Control(root, machineMaster);
		Scanner input = new Scanner(System.in);
		while(true) {
			System.out.printf(ctrl.getCurDir().getName() + "> ");
			String command = input.nextLine();
			if(command.equals("quit")) {
				break;
			}else{
				ctrl.processCommand(command);
			}
			//sending the command to the control class;
			System.out.printf("the command: " + command + "\n");
		}
		System.out.printf("See You \n");
	}
}
