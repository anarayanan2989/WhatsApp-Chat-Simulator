======================================================
COSC 5302	Advanced Operating Systems Spring 2015(LU) 
======================================================

------------------------
Karthik/Adithya/Ojus
------------------------

-----------
Compilation
-----------
	- IDE 
		Import the project into a Java IDE (Eclipse or Intelli J)
		Make or build the project in the IDE
	- Manual
		javac *.java
		javac model/*.java
		javac strategy/*.java
	- cmd prompt
------------------
Config File Format
------------------
	For each line:
		[id] [ip] [port] [name] [group number]

	Example:
		0 127.0.0.1 8001 foo 1
		1 127.0.0.1 8002 bar 1
		2 127.0.0.1 8003 fb 1
		3 127.0.0.1 8004 lk 1	
----------------------------
Running / Command line usage
----------------------------
Extract the files into a directory
open command prompt, go to the directory where the file got extracted.
Then go the src Directory.
Then execute the program.

	javac Chat.java
	java Chat [selfId]
		selfId - the id of the chat client in configuration file(0,1,2...)
		
	After all clients connect 
	type messages to send

Open another command prompt, go to the same directory and execute the program
	java Chat [selfId]
	
This can be done upto maximum of 4 client i.e. command prompts, as we have making use of only 4 clients, if you want to add more clients just add the
clients in the config.txt file in the given format.

----------
Algorithms
----------
(See report for details)
	- Causal Order Multicast
		Uses vector/Lamport timestamps to achieve causal ordering in message delivery
		Chat clients deliver each message only when proper causality conditions are satisfied
		