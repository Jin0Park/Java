/*
 * File: AdvRoom.java
 * ------------------
 * This file defines a class that models a single room in the
 * Adventure game.
 */

import java.io.*;
import java.util.*;

/* Class: AdvRoom */
/**
 * This class defines a single room in the Adventure game. A room is
 * characterized by the following properties:
 * 
 * <ul>
 * <li>A room number, which must be greater than zero
 * <li>Its name, which is a one-line string identifying the room
 * <li>Its description, which is a multiline array describing the room
 * <li>A list of objects contained in the room
 * <li>A flag indicating whether the room has been visited
 * <li>A motion table specifying the exits and where they lead </li>
 * 
 * The external format of the room data file is described in the assignment
 * handout. The comments on the methods exported by this class show how to use
 * the initialized data structure.
 */

public class AdvRoom {

	private int roomNumber;
	private String roomName;
	private String[] roomDescription;
	private boolean flagVisited;
	private AdvMotionTableEntry[] advMotionTable;
	private List<AdvObject> objectList = new ArrayList<AdvObject>();

	
	/* Method: getRoomNumber() */
	/**
	 * Returns the room number.
	 * 
	 * @usage int roomNumber = room.getRoomNumber();
	 * @return The room number
	 */
	public int getRoomNumber() {
		return roomNumber; 
	}

	/* Method: getName() */
	/**
	 * Returns the room name, which is its one-line description.
	 * 
	 * @usage String name = room.getName();
	 * @return The room name
	 */
	public String getName() {
		return roomName; 
	}

	/* Method: getDescription() */
	/**
	 * Returns an array of strings that correspond to the long description of
	 * the room (including the list of the objects in the room).
	 * 
	 * @usage String[] description = room.getDescription();
	 * @return An array of strings giving the long description of the room
	 */
	public String[] getDescription() {
		return roomDescription; 
	}

	/* Method: addObject(obj) */
	/**
	 * Adds an object to the list of objects in the room.
	 * 
	 * @usage room.addObject(obj);
	 * @param The
	 *            AdvObject to be added
	 */
	public void addObject(AdvObject obj) {
		if(obj != null) {
			objectList.add(obj);
		}
	}

	/* Method: removeObject(obj) */
	/**
	 * Removes an object from the list of objects in the room.
	 * 
	 * @usage room.removeObject(obj);
	 * @param The
	 *            AdvObject to be removed
	 */
	public void removeObject(AdvObject obj) {
		objectList.remove(obj);
	}

	/* Method: containsObject(obj) */
	/**
	 * Checks whether the specified object is in the room.
	 * 
	 * @usage if (room.containsObject(obj)) . . .
	 * @param The
	 *            AdvObject being tested
	 * @return true if the object is in the room, and false otherwise
	 */
	public boolean containsObject(AdvObject obj) {
		return objectList.contains(obj);
	}

	/* Method: getObjectCount() */
	/**
	 * Returns the number of objects in the room.
	 * 
	 * @usage int nObjects = room.getObjectCount();
	 * @return The number of objects in the room
	 */
	public int getObjectCount() {
		return objectList.size();
	}

	/* Method: getObject(index) */
	/**
	 * Returns the specified element from the list of objects in the room.
	 * 
	 * @usage AdvObject obj = room.getObject(index);
	 * @return The AdvObject at the specified index position
	 */
	public AdvObject getObject(int index) {
		return objectList.get(index);
	}

	/* Method: setVisited(flag) */
	/**
	 * Sets the flag indicating that this room has been visited according to the
	 * value of the parameter. Calling setVisited(true) means that the room has
	 * been visited; calling setVisited(false) restores its initial unvisited
	 * state.
	 * 
	 * @usage room.setVisited(flag);
	 * @param flag
	 *            The new state of the "visited" flag
	 */
	public void setVisited(boolean flag) {
		flagVisited = flag; 
	}

	/* Method: hasBeenVisited() */
	/**
	 * Returns true if the room has previously been visited.
	 * 
	 * @usage if (room.hasBeenVisited()) . . .
	 * @return true if the room has been visited; false otherwise
	 */
	public boolean hasBeenVisited() {
		return flagVisited; 
	}

	/* Method: getMotionTable() */
	/**
	 * Returns the motion table associated with this room, which is an array of
	 * directions, room numbers, and enabling objects stored in a
	 * AdvMotionTableEntry.
	 * 
	 * @usage AdvMotionTableEntry[] motionTable = room.getMotionTable();
	 * @return The array of motion table entries associated with this room
	 */
	public AdvMotionTableEntry[] getMotionTable() {
		return advMotionTable;
	}

	/* Method: readFromFile(rd) */
	/**
	 * Reads the data for this room from the Scanner scan, which must have been
	 * opened by the caller. This method returns a room if the room
	 * initialization is successful; if there are no more rooms to read,
	 * readFromFile returns null.
	 * 
	 * @usage AdvRoom room = AdvRoom.readFromFile(scan);
	 * @param scan
	 *            A scanner open on the rooms data file
	 * @return a room if successfully read; null if at end of file
	 */
	public static AdvRoom readFromFile(Scanner scan) {
		try{
			AdvRoom room = new AdvRoom();
			
			// read room number
			room.roomNumber = Integer.parseInt(scan.nextLine());
			
			// read room name
			room.roomName = scan.nextLine();
			
			// read room description
			String line, description = "";
			while(!(line = scan.nextLine()).equals("-----")) {
				description += (description.length() == 0 ? "" : System.lineSeparator()) + line;
			}			
			room.roomDescription = description.split(System.lineSeparator());
			
			// read room motion commands
			List<AdvMotionTableEntry> table = new ArrayList<AdvMotionTableEntry>();
			String direction, keyName;
			int destinationRoom;
			while(scan.hasNext() && !(line = scan.nextLine()).equals("")) {
				// read direction (string before space)
				direction = line.substring(0, line.indexOf(" "));
				
				// read the room number and key name (string after space)
				String str = line.substring(line.lastIndexOf(" ") + 1);
				
				// if it contains "/"
				if(str.indexOf("/") != -1) {
					destinationRoom = Integer.parseInt((str.substring(0, str.indexOf("/"))));
					keyName = str.substring(str.indexOf("/")+1);
				}else {
					destinationRoom = Integer.parseInt(str);
					keyName = null;
				}
				
				// add to the arrayList
				table.add(new AdvMotionTableEntry(direction, destinationRoom, keyName));
			}
			room.advMotionTable = new AdvMotionTableEntry[table.size()];
			for(int i = 0; i < table.size(); i++) {
				room.advMotionTable[i] = table.get(i);
			}
			
			return room;
			
		}catch(NullPointerException e) {
			return null;
		}
	}

	/* Private instance variables */
	// Add your own instance variables here
}
