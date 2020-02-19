package main.UserInterface;

import java.util.Scanner;

public class UserInterfaceMain {
	
	
	public static void main(String[] args) {
		System.out.printf(" --------PCH Distributed System-------- \n");
		Scanner input = new Scanner(System.in);
		while(true) {
			System.out.printf("> ");
			String command = input.nextLine();
			if(command.equals("quit")) {
				break;
			}
			//sending the command to the control class;
			System.out.printf("the command: " + command + "\n");
		}
		System.out.printf("See You \n");
	}
}
