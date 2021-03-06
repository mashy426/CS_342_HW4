
//Name: Priyan Sureshkumar
//NetID: psures5
//Class that defines the Place objects.  It holds a collection of artifacts and directions and characters relevant to the Place object
//This Class essentially holds all Objects created for the game's function

import java.util.*;
import java.util.regex.Pattern;

public class Place
{

    static final HashMap<Integer, Place> allPlacesMap; // Can optionally make hashmap public --- use
                                                       // DoubleBraceInitialization??
    static
    {
        allPlacesMap = new HashMap<Integer, Place>();
    }

    public static Place getPlacebyID(int id)
    {
        return allPlacesMap.get(id);
    }

    // Place variables. Final values.
    private final Pattern g_delim_Pattern = Pattern.compile("\r?\n\r?\n");
    private final String PNAME;
    private final String PDESCRIPTION;
    private final int PID;
    private List<Direction> paths = new ArrayList<Direction>();
    private List<Artifact> placeArtifacts = new ArrayList<Artifact>();
    private List<Character> placeCharacters = new ArrayList<Character>();

    public Place(int ID, String name, String description)
    {
        PID = ID;
        PNAME = name;
        PDESCRIPTION = description;
        if (!allPlacesMap.containsKey(PID))
            allPlacesMap.put(PID, this);
    }

    public Place(Scanner scn, int ver)
    {
        StringBuilder desc = new StringBuilder();
        Scanner sc = new Scanner(CleanLineScanner.gameFileParser(scn, g_delim_Pattern));

        PID = Integer.valueOf(sc.next(("\\d+")));
        PNAME = sc.nextLine().trim();
        sc.nextLine();
        while (sc.hasNext())
            desc.append(sc.nextLine() + "\n");
        PDESCRIPTION = desc.toString();

        if (!allPlacesMap.containsKey(PID))
            allPlacesMap.put(PID, this);
        sc.close();

    }

    public boolean checkID(int idToCheck) // Function used to cross check the Place object's ID with value passed
    { // although it allows a person to just check every number against PID until the return value is true
        return PID == idToCheck;
    }

    public String name()
    {
        return PNAME;
    }
/*
    public String description()
    {
        return PDESCRIPTION;
    }*/

    public void addDirection(Direction dir)
    {
        if (dir.leadsToNowhere()) // Function to check if target place that direction leads to is a "Nowhere" and then
                                  // auto locks it.
            dir.lock();

        paths.add(dir); // Adds Direction object to vector within Place object
        return;
    }

    public Place followDirection(String pToGo)
    {
        // Place result = this; //Sets place to calling object in case no matches are found
        for (Direction tmp : paths) // checks passed string value against every direction object in Place's vector
                                    // member
        {
            if (tmp.match(pToGo)) // calls Directions match method to determine what to do
                return tmp.follow(); // Always returns first Match found in the places Direction Vector
            // return (result = tmp.follow());
        }
        return this;
        // return result;
    }

    public void addArtifact(Artifact item)
    {
        placeArtifacts.add(item);
    }

    public void useKey(Artifact arti)
    {
        for (Direction tmp : paths)
        {
            tmp.useKey(arti);
        }
    }
    
    public void addCharacter(Character charVar)
    {
        placeCharacters.add(charVar);
    }

    public void removeCharacter(Character charVar)
    {
        placeCharacters.remove(charVar);
    }
    
    /*
     * public Artifact get(String itemName) 
     * { 
     *      for (Artifact tmp: placeArtifacts ) 
     *      {
     *          if((itemName.trim()).equals(tmp.name())) 
     *          { 
     *          Artifact retrieve = tmp; placeArtifacts.remove(tmp); 
     *          return retrieve;
     *          } 
     *      } 
     *      return null; 
     * }
     */

    public Artifact removeArtifactByName(String itemName)
    {
        for (Artifact tmp : placeArtifacts)
        {
            if ((itemName.trim()).equals(tmp.name()))
            {
                Artifact retrieve = tmp;
                placeArtifacts.remove(tmp);
                return retrieve;
            }
        }
        return null;
    }
    
    public void display()
    {
        int count = 1;
        System.out.println("\nCurrent Location ->" + PNAME + ":\n" + PDESCRIPTION);
        if (!PNAME.equals("Exit"))
        {
            System.out.println(PNAME + " Directions:");
            for (Direction tmp : paths)
                tmp.display();
            System.out.println("\n" + PNAME + " Artifacts:");
            for (Artifact tmp : placeArtifacts)
            {
                System.out.print(count++ + ":");
                tmp.display();
            }
            count = 0;
            System.out.println("\nCharacters in " + PNAME + ":");
            for (Character charVar : placeCharacters)
            {
                System.out.print(count++ + ":");
                charVar.display();
            }
        }
        System.out.println("\n");

        return;
    }

    public void print()
    {

        System.out.println("Place Name: " + PNAME + "\nDescription:" + PDESCRIPTION + "\nID:" + PID);
        System.out.println("Directions:");
        for (Direction tmp : paths)
            tmp.display();
        return;
    }

    public void printAll()
    {
        int count = 1;
        Collection<Place> everyPlace = Place.allPlacesMap.values();
        for(Place tmpPlace: everyPlace)
        {
            tmpPlace.print();
            System.out.println(PNAME + " Directions:");
            for (Direction tmp : paths)
                tmp.print();
            System.out.println("\n" + PNAME + " Artifacts:");
            for (Artifact tmp : placeArtifacts)
            {
                System.out.println(count++ + ":");
                tmp.print();
            }
            count = 0;
            System.out.println("\nCharacters in " + PNAME + ":");
            for (Character charVar : placeCharacters)
            {
                System.out.print(count++ + ":");
                charVar.print();
            }
        
            System.out.println("\n");
        }
    }
    
    public static int getSizePlaces()
    {
        return allPlacesMap.size();
    }
    
    public static HashMap<Integer,Place> getPlaceDirectory(){ return allPlacesMap;}
    
    // return random place other than nowhere and exit
    public static Place getRandomPlace() {
        // make list of place IDs
        ArrayList<Integer> IDs = new ArrayList<Integer>(allPlacesMap.keySet());
        // get random ID from list (excluding nowhere and exit)
        int randomID = IDs.get(2 + new Random().nextInt(IDs.size() - 2));

        return allPlacesMap.get(randomID);   // return place
    }//end getRandomPlace()
}
