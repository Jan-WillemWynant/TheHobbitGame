import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Game {
    private Location currentLocation; //the current location the player is in.
    private List<Item> itemsInInventory; //the items the player has in inventory currently.
    private int daysLeft; //the days the player has left to complete the game (i.e. reach Erebor).
    private int strengthLevel; //the current strength level of the player. Determines the chances of winning a fight against orcs (e.g. strengthLevel=10 means a 10% chance of winning a fight with orcs).
    private boolean invisible; //boolean which denotes whether player is currently invisible.

    //the enum GeneralCommand will hold the general commands which are handled in the Game class (such as 'travel', 'look' etc.).
    private enum GeneralCommand {
        TRAVEL(new String[]{"travel"}),
        LOOK(new String[]{"look"}),
        EXAMINE(new String[]{"examine", "inspect"}),
        PICK_UP(new String[]{"pick up", "take"}),
        TALK_TO(new String[]{"talk to", "speak with"}),
        HELP(new String[]{"help"}),
        INVENTORY(new String[]{"inventory"});

        private String[] possibleDenominations; //this array of Strings will contain the possible ways a player can invoke a certain command. (for example: "pick up" and "take" both can be used to invoke the PICK_UP command)

        GeneralCommand(String[] possibleDenominations) {
            this.possibleDenominations = possibleDenominations;
        }

        /*
         * This method checks if the command is invoked by the passed String[] command, which is the command inputted by the player in a String[] format.
         * The method returns an integer:
         * It returns -1 if the passed command does not refer to the command.
         * It returns a positive integer if the command does refer to the command.
         * The positive integer signifies the next index in the String[] array to look at.
         * For example the inputted line 'travel north' invokes the TRAVEL command. The CommandHandler() should look at index 1 after to check the direction to travel in. So 1 is returned by the method.
         * Another example: the inputted line 'speak with gandalf' invokes the TALK_TO command. The commandHandler() should then look at index 2 after to check which character the player wishes to talk to.
         */
        public int commandInvoked(String[] command) {
            for (String denomination : possibleDenominations) {  //go through the possible denominations which could invoke this generalCommand
                boolean commandInvoked = true;
                String[] denominationAsStringArray = denomination.split("\\s+");
                if (denominationAsStringArray.length > command.length)
                    continue; //if the number of words in the possible denomination is higher than the number of words in the inputted line, they definitely don't correspond.
                for (int i = 0; i < denominationAsStringArray.length; i++) {  //check if all words in the inputted line correspond to the possible denomination
                    if (!denominationAsStringArray[i].equals(command[i])) { //if one of the words in the inputted line does not match the possible denomination
                        commandInvoked = false;
                        break;
                    }
                }
                if (commandInvoked)
                    return denominationAsStringArray.length;  //returns a positive integer, which signifies the next index to inspect in the inputted String[]. (for example if inputted line is 'inspect <item>', next index the commandHandler() should look at is 1, to determine the item.)
            }
            return -1; //signifies that this command was not invoked by the inputted command
        }

    }


    public static void main(String[] args) {
        while (true) {
            Game game = new Game();
            game.generatePlayingField();
            game.play();

            //if we get to this line, the player has either won or lost the game.
            //We ask the player now if he wants to restart the game or not.
            Scanner input = new Scanner(System.in);
            System.out.println("Do you wish to restart the game?");
            //check the player's response
            while (true) {
                String yesOrNo = input.nextLine().toLowerCase();
                if (yesOrNo.equals("yes")) { //if player replies yes, restart the game (i.e. continue with the outer while loop).
                    break;
                } else if (yesOrNo.equals("no")) { //if the player replies no, return (which effectively breaks the outer while loop).
                    return;
                } else {
                    System.out.println("Please answer with 'yes' or 'no'.");
                }
            }
        }

    }

    public void play() {
        daysLeft = 32;
        strengthLevel = 10; //initial strength level
        invisible = false;
        //Print beginning message.
        System.out.println("Welcome to Middle-Earth\n");
        System.out.println("You are Bilbo Baggins, a hobbit, and you are on a quest with a company of dwarves to reclaim their homeland on the Lonely Mountain. Unfortunately, you have lost the company of dwarves. \nYou are currently on a hilly field in Dunland. As Bilbo Baggins, you know the following things: the dwarves will meet atop the Lonely Mountain at the last light of Durin’s day, which is in 32 days. It is your goal to join them.\n");
        System.out.println("The Lonely Mountain is far from Dunland, and you will have to traverse many locations to reach your destination. Many obstacles and perils lie ahead, and you will need to pick up tools and encounter friends ánd foes to bring your journey to a good end. \nCurrently, your hobbit’s pockets are rather empty. You only have a pair of binoculars in them. You can always look at what is in your pockets by typing ‘inventory’.\n");
        System.out.println("In order to travel ‘north’, ‘east’, and so forth, you can type ‘travel north’, ‘travel east’ and so on. Each time you travel north or in another direction, it costs you a day. It is therefore wise to first ‘look north’, ‘look east’… before travelling. \nNote, however, that your hobbit’s binoculars are limited and looking in a direction only gives you an estimation of what lies ahead.\n");
        System.out.println("You can at any moment type ‘help’ to get information on the possible commands.\nGood luck on your journey!");

        Scanner input = new Scanner(System.in); //We'll constantly loop and look for the next command
        while ((!currentLocation.getName().equals("Erebor")) && (daysLeft > 0)) { //if the goal of the game is not reached, or if the player hasn't run out of days
            String commandText = input.nextLine().toLowerCase(); //take the next command inputted by the user -> immediately convert to lower case to avoid case-sensitivity.
            String[] command = commandText.trim().split("\\s+"); //remove any potential leading/trailing whitespaces, then split the input on whitespace.
            commandHandler(command); //handle the actual command
        }

        //if we get to this line, either the player has won the game (Erebor is reached) or he has lost the game (ran out of days)
        System.out.println(); //just for lay-out purposes
        if (currentLocation.getName().equals("Erebor")) {
            System.out.println("Congratulations! You have reached the Dwarves atop Erebor in time. \nYou have won the game.");
        } else {
            System.out.println("What a shame! You haven't managed to reach the Dwarves in time. \nWithout your help, they will be killed by the dragon Smaug.");
        }

    }

    /*
     *This method generates the playing field, i.e. the map of Middle-Earth this game operates in.
     *In general, the process will be as follows:
     *The method starts from the starting location (Dunland), adds all its neighboring locations.
     *Then, the method proceeds with one of these neighboring locations, adds descriptions/items/characters to this location.
     *Then, it adds all the remaining neigboring locations, then continues the process with one of these neighboring locations.
     *It continues this process until the full map is completed.
     */
    public void generatePlayingField() {

        Location dunland = new Location("Dunland");  //create the Dunland location and add descriptions.
        dunland.setDescription("The rolling hills stretch endlessly before you, their grassy slopes dotted with jagged stones and sparse, wind-bent shrubs.\nThe air is cool and carries a faint scent of earth and heather. A few gnarled trees stand stubbornly against the breeze, their twisted branches creaking softly. \nThe place feels desolate, yet alive with the quiet hum of unseen creatures scuttling through the undergrowth.");
        dunland.setDescriptionFromAfar("You recognize the grassy hills where not that long ago you woke up lost and alone, after being separated from the dwarves. \nThe fields are as green, and the landscape as desolate, as the last time you roamed through those lands.");
        currentLocation = dunland;  //Dunland will be the starting location of the player.

        itemsInInventory = new ArrayList<Item>();  //intialize the list of items in inventory
        Item binoculars = new Item("Binoculars", false);  //add the binoculars Item to the starting inventory.
        binoculars.setDescription("These binoculars allow you to get an estimation of what lies ahead to the north, east... \nType 'look north', 'look east' etc. to use them.");
        itemsInInventory.add(binoculars); //this is the only item the player starts with in their inventory.
        binoculars.setInInventory(true);


        Location mistyMountains = new Location("Misty Mountains");  //create the Misty Mountains location. This will be a 'border' location: this location can only be looked at, and never reached.
        mistyMountains.setDescriptionFromAfar("The Misty Mountains rise high, their sharp peaks hidden in swirling clouds. \nDark slopes and deep shadows make them feel dangerous and mysterious.");
        dunland.addNeighboringLocation(Direction.SOUTH, mistyMountains, false);  //the player can from Dunland look to the Misty Mountains in the south, but never travel to them.

        Location rivendell = new Location("Rivendell"); //create Rivendell
        dunland.addNeighboringLocation(Direction.NORTH, rivendell, true); //add it as a neighboring location to Dunland

        DoorsOfDurin doorsOfDurin = new DoorsOfDurin("Doors of Durin"); //create Doors of Durin
        dunland.addNeighboringLocation(Direction.EAST, doorsOfDurin, true); //add it as a neigboring location to Dunland

        Enedwaith enedwaith = new Enedwaith("Enedwaith"); //create Enedwaith
        dunland.addNeighboringLocation(Direction.WEST, enedwaith, true); //add it as a neighboring location to Dunland

        //set descriptions of Rivendell
        rivendell.setDescription("Nestled in a deep, hidden valley, Rivendell feels like a sanctuary carved from the heart of the world. Tall, graceful trees line the paths, \ntheir leaves shimmering in the soft light that seems to glow from everywhere and nowhere at once. The sound of gentle waterfalls fills the air, mingling with the faint notes of distant, melodic voices. \nThe buildings, elegant and timeless, seem to blend into the natural beauty around them, as though grown rather than built. This place feels safe, ancient, and touched by a quiet magic.");
        rivendell.setDescriptionFromAfar("A valley seems to shimmer with a gentle light, nestled between towering cliffs and lush greenery. You can make out the glint of waterfalls cascading down the rocks, their soft spray catching the sunlight. \nThe buildings are barely visible, blending seamlessly with the trees and the land, as though the valley itself were alive and welcoming.");

        //add the two non-reachable locations Angmar and Misty mountains as neighboring locations to Rivendell.
        Location angmar = new Location("Witch-Kingdom of Angmar");
        angmar.setDescriptionFromAfar("Angmar lies cold and desolate, its jagged hills and crumbling ruins shrouded in a bleak, gray mist. \nThe land feels lifeless, with dark shapes of broken towers and cliffs rising like shadows against the dim horizon.");
        rivendell.addNeighboringLocation(Direction.NORTH, angmar, false);
        rivendell.addNeighboringLocation(Direction.EAST, mistyMountains, false);//Misty Mountain is not located here!

        Location bree = new Location("Bree"); //create Bree
        rivendell.addNeighboringLocation(Direction.WEST, bree, true); //add it as a neighboring location to Rivendell

        //set descriptions of Bree
        bree.setDescription("Bree feels warm and lively, its timber and stone houses nestled snugly against the Bree-hill. The cobbled streets are bustling with travelers, traders, and townsfolk, their chatter mingling with the creak of wagons. \nOn a bench near the bustling inn, you spot a curious figure— Radagast the Brown, a scruffy old man with a tangled beard and a brown cloak, fast asleep. Beneath the bench, a bottle lies tucked away, its label faintly visible.");
        bree.setDescriptionFromAfar("A cluster of timber and stone houses surrounded by a sturdy hedge, sits snugly against the side of a low hill. Smoke rises from chimneys, and faint lights glow from windows.");

        Location theShire = new Location("The Shire"); //create the Shire
        bree.addNeighboringLocation(Direction.WEST, theShire, true); //add it as a neighboring location to Bree
        Location untamedHills = new Location("Untamed Hills");  //create and add Untamed Hills as a non-reachable neigboring location to Bree.
        untamedHills.setDescriptionFromAfar("The hills roll endlessly into the distance, their slopes wild and overgrown with patches of thorny bushes and wind-bent trees. Shadows gather in the hollows, and the rugged terrain feels untouched, as if few have dared to wander there.");
        bree.addNeighboringLocation(Direction.NORTH, untamedHills, false);
        bree.addNeighboringLocation(Direction.SOUTH, enedwaith, true); //add Enedwaith as a neighboring location to Bree.

        GameCharacter radagast = new GameCharacter("Radagast", true);
        radagast.setDialogue("Bilbo, you curious little fellow, why did you wake me up? I'm going back to sleep.");
        bree.addCharacter(radagast);
        RadagastsPotion radagastsPotion = new RadagastsPotion();
        radagastsPotion.setPossibleDenominations(new String[]{"bottle", "potion", "the bottle", "the potion", "radagast's potion"});
        radagastsPotion.setDescription("The potion is contained in a small, weathered glass bottle. The label reads \"For Strength and Valor\" with a symbol of crossed swords, promising enhanced fighting strength.");
        bree.addItem(radagastsPotion);
        bree.setStringToBeRemovedIfItemPickedUp(radagastsPotion, " Beneath the bench, a bottle lies tucked away, its label faintly visible.");


        //set the descriptions of the Shire.
        theShire.setDescription("The Shire is a patchwork of green, rolling hills dotted with cozy hobbit-holes, their round doors painted in cheerful colors. \nIt’s a peaceful, timeless land, where every corner feels like home.");
        theShire.setDescriptionFromAfar("You recognize the green scenery and rolling hills of your home, The Shire. \nYou long for that homely feeling, but you know it would lead you farther from your quest.");
        theShire.addNeighboringLocation(Direction.NORTH, untamedHills, false);

        //add neighboring locations to the Shire.
        Location luneRiver = new Location("Lune River");
        luneRiver.setDescriptionFromAfar("The Lune River winds gracefully through the landscape, its silver waters sparkling in the sunlight. The river glimmers like a ribbon of light, weaving its way toward the distant sea.");
        theShire.addNeighboringLocation(Direction.WEST, luneRiver, false);
        Location marshland = new Location("Marshland");
        marshland.setDescriptionFromAfar("Marshland spreads out like a dark, glistening patchwork, its wet ground broken by tangled reeds and stagnant pools. Low mists hang over the area, shrouding it in an eerie stillness.");
        theShire.addNeighboringLocation(Direction.SOUTH, marshland, false);

        //set descriptions of Enedwaith, add neighboring locations and add the Elvish Sword as an Item.
        enedwaith.setDescription("The land here feels untamed, a patchwork of grassy meadows and tangled thickets. Narrow streams crisscross the plains, their bubbling waters reflecting the pale sky above. \nAmong the rough hills, you spot the dark mouth of a cave tucked into a rocky outcrop, its shadowy depths both intriguing and foreboding. Pockets of ancient trees stand like sentinels nearby, their bark rough and moss-covered.");
        enedwaith.setDescriptionFromAfar("The land looks wild and uneven, with grassy plains dotted by thickets and crooked trees. At the base of a rocky hill, you notice the dark opening of a cave, its shadow stark against the stone. \nThin streams glimmer as they weave through the landscape.");
        Location theGreatSea = new Location("The Great Sea");
        theGreatSea.setDescriptionFromAfar("The Great Sea spreads wide and endless, its surface sparkling under the sun.");
        enedwaith.addNeighboringLocation(Direction.WEST, theGreatSea, false);

        Location gapOfRohan = new Location("Gap of Rohan");
        enedwaith.addNeighboringLocation(Direction.SOUTH, gapOfRohan, true);

        //set descriptions for the Gap of Rohan, set that it has orcs and add neighboring locations.
        gapOfRohan.setDescription("The wide, open plain stretches far in every direction, flanked by the towering peaks of the Misty Mountains to the north and the rugged hills of the White Mountains to the south. A steady wind sweeps through the gap. \nThe land feels alive with movement—herds of grazing animals in the distance, and the faint echo of birdsong riding the breeze. The path ahead is well-trodden, a natural corridor through the wilds.");
        gapOfRohan.setDescriptionFromAfar("A wide plain stretches between two great mountain ranges, open and windswept. Grasses ripple in the breeze, and you think you spot the shapes of animals grazing far off. \nA faint trail cuts through the middle, offering a clear way forward through the vastness.");
        gapOfRohan.setHasOrcs(true);

        Location whiteMountains = new Location("White Mountains");
        whiteMountains.setDescriptionFromAfar("The White Mountains rise in sharp, majestic peaks, their snowy summits gleaming bright against the sky. Steep cliffs and jagged ridges make the range feel both imposing and untouchable.");
        gapOfRohan.addNeighboringLocation(Direction.SOUTH, whiteMountains, false);
        gapOfRohan.addNeighboringLocation(Direction.WEST, theGreatSea, false);

        Location rohan = new Location("Rohan");
        gapOfRohan.addNeighboringLocation(Direction.EAST, rohan, true);

        //set descriptions for Doors of Durin, add Gandalf as a character and add neighboring locations.
        doorsOfDurin.setDescription("The Misty Mountains rise up around you large and impressive. At the foot however, what appears to be an immense door, stands in between you and a passage through the mountains. The immense door lacks both a door handle as well as a keyhole. \nThe door is embroidered with Elvish illuminated decorations. It is clear this door will not budge through any manual labor. \nGandalf, the wise wizard, stands in front of the door. He notices you, but his attention mostly remains focused on the door.");
        doorsOfDurin.setDescriptionFromAfar("At the base of the towering Misty Mountains, you notice a flat wall of stone that stands out from the rugged cliffside. The faint outline of a door is visible. \nYou also notice an imposing figure. You can’t discern the person, but you expect to find wise words for your travels there.");
        GameCharacter gandalf = new GameCharacter("Gandalf", true);
        doorsOfDurin.addCharacter(gandalf);
        gandalf.setDialogue("Hello Bilbo, I see you have lost the Dwarves. You absolutely must rejoin them in Erebor in time. The fastest way is here through these doors into the Mines of Moria.\nHowever, I see you don't have any weapons. I will not send you on your way like this. You must first travel back further west.\nI have heard of a cave further west where you should find an Elvish sword. Go and return with the sword. I will open these doors then for you.");

        doorsOfDurin.addNeighboringLocation(Direction.NORTH, mistyMountains, false);
        doorsOfDurin.addNeighboringLocation(Direction.SOUTH, mistyMountains, false);
        Location moria = new Location("Moria");
        doorsOfDurin.addNeighboringLocation(Direction.EAST, moria, false); //the beginning setting is that the player cannot travel from the doorsOfDurin to Moria (as the door is initially closed).

        //set the descriptions of Moria, add the Ring as an item, and add neighboring locations.
        moria.setDescription("The vast, shadowed halls of Moria stretch endlessly before you, their grandeur both awe-inspiring and unsettling. Massive stone pillars rise like ancient trees, supporting a ceiling lost in darkness above. The air is heavy and cool, carrying the faint echoes of long-forgotten footsteps. As you walk through these halls, at the base of one of these pillars, you see a small golden ring, which seems to shimmer despite the lack of light source, as if it came straight out of the fire it was forged in. The only rays of light in this place seem to come through the gate on the east."); //this is the initial description. Once the doorsOfDurin are opened, the dynamic doorsOfDurin will adjust this description.
        moria.setDescriptionFromAfar("Through the gate at the base of the Misty Mountains, you see nothing but vast dark halls.");
        Location darkHalls = new Location("Dark Halls");
        darkHalls.setDescriptionFromAfar("You see nothing but dark halls.");
        moria.addNeighboringLocation(Direction.NORTH, darkHalls, false);
        moria.addNeighboringLocation(Direction.WEST, darkHalls, false); //this overrides the location set by doorsOfDurin previously. Once doorsOfDurin are opened, it sets the connection between the locations again.
        moria.addNeighboringLocation(Direction.SOUTH, darkHalls, false);
        TheOneRing theOneRing = new TheOneRing();
        theOneRing.setDescription("The One Ring is a simple golden band, unmarked at first glance, yet it seems to shimmer with an unnatural light. Its surface is smooth and flawless, cold to the touch, and unnervingly heavy for its size. \nIn certain light, fiery letters appear etched into the gold, their strange script radiating a sinister, otherworldly power.");
        theOneRing.setPossibleDenominations(new String[]{"ring", "golden ring", "the ring", "the golden ring"});  //all possible ways we allow the player to refer to the item in the location.
        moria.addItem(theOneRing);
        moria.setStringToBeRemovedIfItemPickedUp(theOneRing, " As you walk through these halls, at the base of one of these pillars, you see a small golden ring, which seems to shimmer despite the lack of light source, as if it came straight out of the fire it was forged in.");

        Location lorien = new Location("Lorien");
        moria.addNeighboringLocation(Direction.EAST, lorien, true);

        //set descriptions of lorien, and add neighboring locations.
        lorien.setDescription("The air in Lórien feels different—lighter, as if the very world here breathes with peace. Tall, silver-trunked mallorn trees rise high above, their golden leaves shimmering in the soft, dappled light that filters through the canopy. \nThis is a place of beauty and quiet magic, untouched by the world’s troubles.");
        lorien.setDescriptionFromAfar("A forest seems to glow with an otherworldly light, its golden leaves gleaming even in shadow. The trees are tall and elegant, their silvery trunks rising straight and strong.");
        lorien.addNeighboringLocation(Direction.NORTH, mistyMountains, false);

        Location anduinRiver = new Location("Anduin River");
        lorien.addNeighboringLocation(Direction.EAST, anduinRiver, true);
        Location fangorn = new Location("Fangorn");
        lorien.addNeighboringLocation(Direction.SOUTH, fangorn, true);

        ////set descriptions of Anduin, and add neighboring locations.
        anduinRiver.setDescription("The great Anduin flows wide and steady before you, its waters glimmering under the light and rippling softly with the current. \nAlong the shore, a few wooden boats lie abandoned, their hulls weathered and cracked, as though long-forgotten by their owners.");
        anduinRiver.setDescriptionFromAfar("You see a wide and steady river. You don’t notice a bridge, but you do see a few abandoned wooden boots across the shore.");
        anduinRiver.setHasOrcs(true);
        Location northAnduinRiver = new Location("North Anduin River");
        Location southAnduinRiver = new Location("South Anduin River");
        northAnduinRiver.setDescriptionFromAfar("The Anduin River stretches out for miles.");
        southAnduinRiver.setDescriptionFromAfar("The Anduin River stretches out for miles.");
        anduinRiver.addNeighboringLocation(Direction.NORTH, northAnduinRiver, false);
        anduinRiver.addNeighboringLocation(Direction.SOUTH, southAnduinRiver, false);

        Mirkwood mirkwood = new Mirkwood("Mirkwood");
        anduinRiver.addNeighboringLocation(Direction.EAST, mirkwood, true);

        //set descriptions of Fangorn, and add neighboring locations.
        fangorn.setDescription("The forest looms before you, ancient and brooding, with towering trees whose massive trunks are gnarled and twisted with age. Their dense canopy blocks out much of the light, casting the ground below in deep shadow. \nIt feels alive in a way that is both awe-inspiring and deeply unsettling, as though the trees might move or speak when you’re not looking.");
        fangorn.setDescriptionFromAfar("A forest rises like a dark, impenetrable wall against the horizon, its towering trees standing close together in defiance of time. \nThe canopy appears dense and shadowy.");

        fangorn.addNeighboringLocation(Direction.WEST, mistyMountains, false);
        fangorn.addNeighboringLocation(Direction.SOUTH, rohan, true);
        Location theWold = new Location("The Wold");
        fangorn.addNeighboringLocation(Direction.EAST, theWold, true);

        //set descriptions of the Wold, and add neighboring locations.
        theWold.setDescription("The Wold stretches out before you, an endless expanse of rolling grasslands rippling under the breeze. The horizon feels impossibly far, with only a few scattered boulders and shrubs breaking the monotony of the plains. \nYou stand in what appears to be the middle of the plains, on a bridge over the Anduin River.");
        theWold.setDescriptionFromAfar("A vast, rolling sea of grass, stretching endlessly toward the horizon. The land is open and unbroken, save for a few dark shapes of scattered rocks or distant shrubs. \nYou notice what seems to be a bridge in the middle of the plains.");
        theWold.addNeighboringLocation(Direction.SOUTH, rohan, true);
        Location mordor = new Location("Mordor");
        mordor.setDescriptionFromAfar("Mordor looms dark and menacing, its jagged peaks rising like cruel teeth against a smoky, ash-filled sky. The land beyond appears lifeless and \nbarren, with shadows stretching over its cracked and blackened plains.");
        theWold.addNeighboringLocation(Direction.EAST, mordor, false);
        theWold.addNeighboringLocation(Direction.NORTH, mirkwood, true);

        //set descriptions of Rohan, and add neighboring locations.
        rohan.setDescription("A land of wide, open plains, where golden grasses ripple like waves under the constant winds. Rolling hills break the horizon, dotted with herds of horses grazing freely. \nThe air is fresh and carries the faint scent of wildflowers and earth, and the distant sound of hooves echoes across the vast expanse. It is a place of strength and freedom, full of life and motion.");
        rohan.setDescriptionFromAfar("A vast sea of golden grasslands, rippling under the breeze. Gentle hills rise and fall in the distance, and you can just make out the dark shapes of grazing horses moving across the plains.");
        rohan.addNeighboringLocation(Direction.SOUTH, whiteMountains, false);
        rohan.addNeighboringLocation(Direction.EAST, mordor, false);

        //set descriptions of Mirkwood, and add neighboring locations.
        mirkwood.setDescription("A shadowy, oppressive forest where ancient trees rise high, their gnarled branches forming a thick canopy that blocks out the sunlight. \nThe trees are so high and the canopy so thick, it leaves you completely disoriented.");
        mirkwood.setDescriptionFromAfar("A forest looms like a dark, endless wall of trees, their tangled branches forming a forbidding barrier. The forest appears heavy and impenetrable, with shadows pooling at its edge.");

        Location seaOfRhun = new Location("Sea of Rhun");
        seaOfRhun.setDescriptionFromAfar("The Sea of Rhûn glimmers on the horizon, a vast expanse of water stretching into the distance.");
        mirkwood.addNeighboringLocation(Direction.EAST, seaOfRhun, false);
        Location laketown = new Location("Laketown");
        mirkwood.addNeighboringLocation(Direction.NORTH, laketown, true);

        //set descriptions of Laketown, and add neighboring locations.
        laketown.setDescription("Laketown rises from the waters of the great lake, its wooden buildings perched on tall stilts and connected by a maze of narrow walkways and bridges. The air smells of damp wood and fish, and the gentle lapping of water against the supports fills the air with a rhythmic sound. \nDespite its worn appearance, there’s a warmth here, a sense of life and resilience amid the shimmering expanse of the lake.");
        laketown.setDescriptionFromAfar("A town seems to float on the shimmering surface of the lake, its wooden buildings standing on tall stilts above the water. It is connected to the shore of the lake by a bridge on the south side and a bridge on the north side.");
        Location lake = new Location("Lake");
        lake.setDescriptionFromAfar("The lake stretches out further.");
        laketown.addNeighboringLocation(Direction.EAST, lake, false);
        laketown.addNeighboringLocation(Direction.WEST, lake, false);

        Location erebor = new Location("Erebor");
        laketown.addNeighboringLocation(Direction.NORTH, erebor, true); //Erebor only needs a descriptionFromAfar since once Erebor is reached, the game is finished.
        erebor.setDescriptionFromAfar("Erebor, the Lonely Mountain, rises like a dark crown from the surrounding plains, its jagged peak looming against the sky. The dragon Smaug has awakened though. \nYou will have to find a way to get past him to the Dwarves atop the mountain unseen.");

    }

    /*
     * Takes the inputted line as an array of Strings, and handles the inputted command.
     * The method first checks which command is invoked.
     * Then, it calls the right method to execute what the command calls for.
     */
    public void commandHandler(String[] command) {
        //The hierarchy of handling a command is as follows:
        //first it is checked whether any of the items in inventory implements SpecificCommandHandler, and if they do, whether the Item handles the inputted command.
        //After this it is checked whether there are any Item objects in the currentLocation which implement SpecificCommandHandler, and if any of them handle the command inputted.
        //Then it is checked whether any Character objects which implement SpecificCommandHanlder, handle the inputted command.
        //Then it is checked if the currentLocation implements SpecificCommandHandler and if so, whether it handles the inputted command.
        //If none of the aforementioned, handle the command, it is checked if it is one of the general commands, and thus handled in Game itself.
        //This also means that commands can be 'overloaded'. If a specific location requires another functionality for example 'look north', this can be done by handling 'look north' in the Location object (provided the subclass implements SpecificCommandHandler), as this specific command will be checked first.

        for (Item item : itemsInInventory) { //go through the items in inventory, check if any of them implement SpecificCommandHandler and lastly, check if any of them handle the inputted command.
            if (item instanceof SpecificCommandHandler) {
                if (((SpecificCommandHandler) item).specificCommandHandler(command, this)) {
                    return; //If the Item has handled the command, we don't need to further handle anything, so we can return.
                }
            }
        }

        for (Item item : currentLocation.getItemsInLocation()) { //go through the items in the current location, check if implement SpecificCommandHandler and lastly, check if any of them handle the inputted command.
            if (item instanceof SpecificCommandHandler) {
                if (((SpecificCommandHandler) item).specificCommandHandler(command, this)) {
                    return; //If the Item has handled the command, we don't need to further handle anything, so we can return.
                }
            }
        }

        for (GameCharacter character : currentLocation.getCharactersInLocation()) { //go through the characters in the current location, check if any of them implement SpecificCommandHandler and lastly, check if any of them handle the inputted command.
            if (character instanceof SpecificCommandHandler) {
                if (((SpecificCommandHandler) character).specificCommandHandler(command, this)) {
                    return; //If the Character has handled the command, we don't need to further handle anything, so we can return.
                }
            }
        }

        if (currentLocation instanceof SpecificCommandHandler) {
            if (((SpecificCommandHandler) currentLocation).specificCommandHandler(command, this)) {
                return; //if command is handled in the currentLocation, no need to further handle it. -> should return
            }
        }

        //if we get to this line, the inputted command can only potentially still be a general command.

        GeneralCommand inputCommand = null;

        for (GeneralCommand generalCommand : GeneralCommand.values()) {
            int nextIndexToInspect; //this integer will denote the next index to look at in the String[] array of the inputted command
            if ((nextIndexToInspect = generalCommand.commandInvoked(command)) > 0) { //if this is true, the inputted command refers to this generalCommand. Else commandInvoked() would have returned -1, which denotes the inputted command does not refer to this generalCommand.
                inputCommand = generalCommand;
                switch (inputCommand) {

                    case TRAVEL:
                        if (command.length <= nextIndexToInspect) { //this would mean the player inputted 'travel' or 'go' and nothing else.
                            System.out.println("You must type 'travel' plus a direction ('north','east','south' or 'west') to travel somewhere.");
                            return;
                        }
                        for (Direction direction : Direction.values()) {
                            if ((command[nextIndexToInspect].equals(direction.getDirectionName()))) { //checks if direction player wants to travel in is 'north','east','south' or 'west'.
                                travel(direction);
                                return;
                            }
                        }
                        System.out.println("You must type 'travel' plus a direction ('north','east','south' or 'west') to travel somewhere.");
                        return;

                    case LOOK:
                        if (command.length <= nextIndexToInspect) { //this would mean the player inputted just 'look', so we look around by calling look().
                            look();
                            return;
                        }
                        for (Direction direction : Direction.values()) {
                            if ((command[nextIndexToInspect].equals(direction.getDirectionName()))) { //checks if direction player wants to travel in is 'north','east','south' or 'west'.
                                look(direction);
                                return;
                            }
                        }
                        System.out.println("You must type 'look' plus a direction ('north','east','south' or 'west') to look in a certain direction.");
                        return;

                    case EXAMINE:
                        if (command.length <= nextIndexToInspect) { //this would mean the player inputted 'examine' or 'inspect' and nothing else.
                            System.out.println("You must type 'examine' plus the name of an item to examine an item.");
                            return;
                        }
                        //Now we must check if the player wants to examine an item in inventory, or an item in the currentLocation.
                        for (Item item : itemsInInventory) {  //go through the items in inventory.
                            if (item.refersToItem(Arrays.copyOfRange(command, nextIndexToInspect, command.length))) {  //check if player referred to an item in the inventory. //For this we pass the rest of the String[] command to the .refersToItem() method.
                                examine(item);
                                return;
                            }
                        }

                        for (Item item : currentLocation.getItemsInLocation()) {    //go through the items in the current location.
                            if (item.refersToItem(Arrays.copyOfRange(command, nextIndexToInspect, command.length))) { //check if player referred to an in item in the current location. //For this we pass the rest of the String[] command to the .refersToItem() method.
                                examine(item);
                                return;
                            }
                        }

                        System.out.println("There is no such item for you to examine here.");
                        return;

                    case PICK_UP:
                        if (command.length <= nextIndexToInspect) { //this would mean the player inputted 'pick up' or 'take' and nothing else.
                            System.out.println("You must type 'pick up' plus the name of an item to pick up an item.");
                            return;
                        }

                        for (Item item : currentLocation.getItemsInLocation()) { //go through the items in the current location.
                            if (item.refersToItem(Arrays.copyOfRange(command, nextIndexToInspect, command.length))) { //check if player referred to an in item in the current location. //For this we pass the rest of the String[] command to the .refersToItem() method.
                                pickUp(item);
                                return;
                            }
                        }

                        System.out.println("There is no such item for you to pick up here.");
                        return;

                    case TALK_TO:
                        if (command.length <= nextIndexToInspect) { //this would mean the player inputted 'talk to' or 'speak with' and nothing else.
                            System.out.println("You must type 'talk to' plus the name of a character to talk to a character.");
                            return;
                        }

                        for (GameCharacter character : currentLocation.getCharactersInLocation()) {
                            if (command[nextIndexToInspect].equals(character.getName().toLowerCase())) {
                                talkToCharacter(character);
                                return;
                            }
                        }

                        System.out.println("There is no such character for you to talk to here.");
                        return;

                    case HELP:
                        if (command.length <= nextIndexToInspect) { //this would mean the player inputted 'help' and nothing else.
                            help();
                            return;
                        } else if (command[nextIndexToInspect].equals("directions")) { // if the player inputted 'help directions', we call the helpWithDirections() method.
                            helpWithDirections();
                        } else { //if the player typed 'help' plus something else, we just display the general help() message.
                            help();
                        }

                    case INVENTORY:
                        displayInventory();
                        return;

                }

            }
        }

        //if you get to this line, the inputted command is not recognized by any of the commandHandlers.
        System.out.println("This command is not recognized. You can type 'help' to get more information on the possible commands.");

    }

    /*
     * Takes a direction as argument, and executes the travelling to that direction.
     */
    public void travel(Direction direction) {

        if (currentLocation.canTravelTo(direction)) { //first check if the player can travel in that direction.

            if (currentLocation.getNeighboringLocation(direction).hasOrcs()) { //if the direction the player wants to travel to, has orcs. We check if the player has the Elvish Sword already. If he has it, we warn him.
                for (Item item : itemsInInventory) {
                    if (item.getName().equals("Elvish Sword")) {
                        Scanner yesOrNoInput = new Scanner(System.in);
                        System.out.println("You start going " + direction.getDirectionName() + ", but as you approach, your blade starts glowing blue.\nDo you wish to still travel " + direction.getDirectionName() + "?");
                        while (true) {
                            String yesOrNo = yesOrNoInput.nextLine().toLowerCase();
                            if (yesOrNo.equals("no")) { //if player replies no, we don't go through with the travel.
                                System.out.println("You stay in " + currentLocation.getName() + ".");
                                return;
                            } else if (yesOrNo.equals("yes")) { //if the player replies yes, the travel goes through.
                                break;
                            } else {
                                System.out.println("Please answer with 'yes' or 'no'.");
                            }
                        }
                        break;
                    }
                }
            }

            if (currentLocation.getNeighboringLocation(direction).getName().equals("Erebor") && (!invisible)) { //if the player wants to travel to Erebor and is not invisible, the dragon Smaug will notice the player and throw him into the lake (resulting in the player staying in Laketown).
                System.out.println("You approach Erebor, but the dragon Smaug sees you. He picks you up and throws you into the Lake. You crawl back onto shore in Laketown.\nYou must find a way to get past him unseen.");
                daysLeft--; //the ordeal does cost the player a day.
                return;
            }

            currentLocation = currentLocation.getNeighboringLocation(direction); //update the currentLocation
            currentLocation.arrive();
            daysLeft--; //assuming every step north, east etc. takes 1 day

            if (currentLocation.hasOrcs()) {
                fightWithOrcs();
            }
        } else {
            System.out.println("You can not travel in this direction.\nType 'help directions' to find out what directions you can travel to from your current location.");
        }
    }

    private void fightWithOrcs() {
        //
    }

    /*
     * Takes a direction as argument, and executes the looking to that direction.
     */
    public void look(Direction direction) {
        System.out.println("To the " + direction.getDirectionName() + " you see:");
        if (currentLocation.getNeighboringLocation(direction).getAlreadyVisited())
            System.out.println(currentLocation.getNeighboringLocation(direction).getName());  //The name of the location the player looks to, is only printed if he knows the location, i.e. has already visited the location.
        System.out.println(currentLocation.getNeighboringLocation(direction).getDescriptionFromAfar());
    }

    /*
     * Executes the looking around.
     */
    public void look() {
        //method which allows the player to 'look around'. So just give the description of the location again.
        System.out.println(currentLocation.getName() + ":");
        System.out.println(currentLocation.getDescription());
    }

    /*
     * Takes an item as argument, and executes the examining of the item.
     */
    public void examine(Item item) {
        System.out.println(item.getDescription());
    }

    /*
     * Takes an item as argument, and executes the picking up of the item.
     */
    public void pickUp(Item item) {
        if (item.canPickUp()) {
            itemsInInventory.add(item);
            System.out.println("You have picked up " + item.getName() + ".");
            item.setInInventory(true);
            currentLocation.removeItem(item); //the item is picked up so it should not be available anymore in the location anymore.
        } else {
            System.out.println("You can not pick up this item.");
        }
    }

    /*
     * Takes a character as argument, and executes the talking to the character.
     */
    public void talkToCharacter(GameCharacter character) {
        if (character.canTalk()) {
            System.out.println(character.getDialogue());
        } else {
            System.out.println("This character can't talk.");
        }
    }

    /*
     * Displays a list of the items currently in inventory.
     */
    public void displayInventory() {
        System.out.println("You have the following items in your pockets:");
        System.out.println(itemsInInventory);
    }

    public boolean isItemInInventory(Item item) {
        return itemsInInventory.contains(item);
    }

    /*
     * Method displays a general help message.
     */
    public void help() {
        //
    }

    /*
     * Method displays the directions the player can travel in from the current location.
     */
    public void helpWithDirections() {
        //
    }

    /*
     * Method increases the strength level of the player with a certain amount.
     */
    public void increaseStrength(int amount) {
        strengthLevel += amount;
    }

    /*
     * Method returns the items the player has in inventory currently.
     */
    public List<Item> getItemsInInventory(){
        return itemsInInventory;
    }

}
