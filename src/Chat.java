/*
@author karthik/Adithya/Ojus
*/

import model.*;
import strategy.CausalOrderMulticast;
import strategy.ReliableUnicastReceiver;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Chat {
	private ReliableUnicastReceiver _receiver;
	//declaring for the count of 32
	int cnt=0;
    /*
        Constructor
        Tries to parse the config file and sets up networking
     */
	public Chat(int delayTime, double dropRate, String file, int id, String orderType, String mode, String boost){
		List<Member> members = new ArrayList<Member>();
		Profile profile = Profile.getInstance();
		MemberIndexer memberIndexer = MemberIndexer.getInstance();
		CausalOrderMulticast causalOrderMulticast = CausalOrderMulticast.getInstance();
		try {
			Scanner scanner = new Scanner(new File(file));
			while(scanner.hasNext()){
				String line = scanner.nextLine();
				String[] parts = line.split(" ");
				Member member = new Member();
				member._id =  Integer.parseInt(parts[0]);
				member._ip =  parts[1];
				member._port = Integer.parseInt(parts[2]);
				member._userName = parts[3];
				member._groupId = Integer.parseInt(parts[4]);

                

				members.add(member);
				if(Integer.parseInt(parts[0]) == id){
					profile.id = id;
					profile.ip = parts[1];
					profile.port = Integer.parseInt(parts[2]);
					profile.name = parts[3];
					profile.delay = delayTime;
					profile.dropRate = dropRate;
					profile.setMulticastType(MulticastType.CausalOrder);
					
					
				}
				
			}
		}
        catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		memberIndexer.addMembers(members);

        // Set up networking
		this._receiver = ReliableUnicastReceiver.getInstance(); 
		this._receiver.init(profile.ip, profile.port);
	}

    /*
        Main chat loop
        Processes user input and sends it to other Chat clients
     */
	public void waitUserMessage(){
		Console console = System.console();
		if(Profile.getInstance().isBoost){
	        int myId = Profile.getInstance().getId();
	        for(int i = 1; i <= 20; ++i) {
	            //System.out.println(String.format("broadcasting %d", i));
	            multicastMessage(myId + " broadcast " + i);
	            try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
		}
		// set condition true till the user stops sending msgs or till the msgs exide 32 
		while (true) {
		// count of the msgs
			if(cnt<32){
			System.out.println("type your message");
			String command = console.readLine();
			if(command.equals("exit")){
				break;
			}
			if(command.trim().length() > 0){
				multicastMessage(command);
			}
			}
			else
			{
			break;
			}
			cnt++;
		}
		if(cnt==32)
		System.out.println("You have Reached the Limit 32\n All the Msg have Delivered");
		System.out.println("Bye.");
	}

    /*
        Start listening on a socket in another thread
     */
	public void startListen(){
		(new Thread(this._receiver)).start();
	}

    /*
        Multicast message based on the order type
     */
	private void multicastMessage(String content){
		if(Profile.getInstance().getMulticastType() == MulticastType.CausalOrder){
			CausalOrderMulticast causalOrderMulticast = CausalOrderMulticast.getInstance();
			causalOrderMulticast.send(1, content);
		}
	}
	// main function
	public static void main(String[] args){
		
		
		if(args.length != 1){
			System.out.println("usage: java Chat [configFile] [delayTime(ms)] [dropRate(0-1)] [selfId]  [causal][detail|brief] [boost|normal]");
			return;
		}
		//read the parameters
		Chat chat = new Chat(
				0,
				0,
				"config.txt",
				Integer.parseInt(args[0]),
				"causal",
				"brief",
				"normal");
		chat.startListen();
		chat.waitUserMessage();
	}
}
