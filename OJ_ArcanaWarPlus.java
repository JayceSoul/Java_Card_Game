// Arcana war plus version (improved graphics and sound!)                          \\
// By Jonathan Ong   Grade: 11                                                     \\ 
// Took aproximatly 60 hours plus to make and test. (Yes I calculated it)          \\
// Made from blood and sweat (no tears were shed but years were drained instead)   \\ 
// ICS3U1-02                                                                       \\
// A good way to have fun and possibly hurt your ears is to mash the arrow keys    \\
// and s key together this will cause the sound to mix and play over each other.   \\
// At least is doesn't crash the program...(10 mins later..fixed it...)            \\

import java.awt.*; //abstract windows toolkit used for gui objects
import java.io.File; //for files
import java.util.Collections; //Allows use of Collections
import java.awt.event.*; //Allows use of events
import javax.swing.*; //also used for gui objects
import javax.swing.Timer; // Allow use of timer
import javax.swing.event.*; // Allows use of events
import javax.imageio.*; // for images
import java.io.*; // for file reading/loading
import java.io.FileNotFoundException; //exception error
import sun.audio.*;     //sun.audio package imported (sound)     
import java.util.ArrayList; //allows use of Arraylist  
import java.awt.Color; //for color
import java.awt.Graphics; //for drawing graphics



public class OJ_ArcanaWarPlus extends JFrame
{// start
//==================================<Audio Streams>===================================
//Creating AudioStream objects (null is default declaration)
  public static AudioStream sfx = null; // audiostream for short sound effects
  public static AudioStream playing = null; // audiostream for music
//====================================================================================
  public ArrayList<Integer> intDeck = new ArrayList<Integer>();//stores cards in deck
  public ArrayList<Image> book = new ArrayList<Image>();//stores instructions like a book
//player lists global because they must be accessed by different classes 
  public ArrayList<Integer> playerHand = new ArrayList<Integer>();// holds the value in hand 
  public ArrayList<Integer> playerField = new ArrayList<Integer>();// holds a list of what is placed on the player's field 
  public ArrayList<Integer> playerRanks = new ArrayList<Integer>();// holds a list of what the player's rank is for battle/damage
//cpu lists global because they must be accessed by different classes
  public ArrayList<Integer> cpuHand = new ArrayList<Integer>();// holds a list of the values in hand
  public ArrayList<Integer> cpuRanks = new ArrayList<Integer>();// holds a list of what is placed on the cpus's field
  public ArrayList<Integer> cpuField = new ArrayList<Integer>();// holds a list of what the cpus's rank is for battle/damage
//====================================================================================
  public int pact,cpuact, select, result, pcharge, cpucharge, plife =30 , cpulife =30, wave = 0, frames = 0;// stores integers that will be accessed by different classes
  public boolean battle = false, damage = false, info = false, toss = false, pwin = false, cwin = false, game =false, player =false, first =false, start = true, title =true;// switches for the different parts of the game both graphical and logical  
//====================================================================================
  Timer t = new Timer(50,null);// updates graphics
  Timer cpu = new Timer(1200,null);// manages cpu turn
  Timer war = new Timer(1500,null);// manages battle system 
  Timer animation = new Timer(80,null);//manages transitions  
//==================================<Image Method>====================================
  public static Image loadImage (String name)  //Loads image from file
  {//start
    Image img = null;
    try
    {
      img = ImageIO.read (new File (name));
    }
    catch (IOException e)
    {
    }
    return img;
  }//end 
//==================================<Sound Method>====================================  
  public static void Player (String audioname)  // Music method
  {//start
    InputStream in = null; // Readies input stream  for audio
    try
    {
      in = new FileInputStream (new File (audioname)); // for importing Files into FileInputStream
    }
    catch (FileNotFoundException e)
    {
    }
    try
    {
      playing = new AudioStream (in);
    }
    catch (IOException e)
    {
    }
  }//end  
//========================================<Sound Method2>=============================
  public static void Player2 (String audioname)  // Music method //fixes music overlap
  {//start
    InputStream in2 = null; // Readies 2nd input stream  for audio 
    try
    {
      in2 = new FileInputStream (new File (audioname)); // for importing Files into FileInputStream
    }
    
    catch (FileNotFoundException e)
    {
    }
    
    try
    {
      sfx = new AudioStream (in2);
    }
    
    catch (IOException e)
    {
    }
  }//end   
//==================================<Remove>========================================
  public static ArrayList<Integer> remove(ArrayList<Integer> deck, int place) //removes card in index entered
  {
    deck.remove(place);
    return deck;
  }  
//==================================<Draw>==========================================
  public ArrayList<Integer> draw (ArrayList<Integer> deck, ArrayList<Integer> hand) //adds a card from integer deck to hand
  {
    if (hand.size() < 6)
    {
      if(intDeck.size() > 0)
      { hand.add(deck.get(deck.size() - 1));
        deck = remove(deck, deck.size() - 1);}
    }
    hand = sort(hand);
    return hand;
  }
//==================================<Convert rank===================================
  public ArrayList<Integer> convertRank(ArrayList<Integer> data)
  {
    int v, r; 
    ArrayList<Integer> rank = new ArrayList<Integer>();
    for (int p = 0; p < data.size(); p++)
    {
      
      v = data.get(p);      
      r = v%14;
      rank.add(r + 1);
    }
    return rank;
  }
//=======================================<Charge value>==================================
  public int cv(int v)
  {
    if (v > 10)
      return 1;
    else if (v >= 5)
      return 2;
    else
      return 3;
  }
//=========================================<Charge>======================================
  public void charge(String name)
  {
    int convert;
    
    Player2 ("sound/charge.wav"); //Loads music file
    AudioPlayer.player.start (sfx); // starts music 
    
    if (name.equals("p"))
    {
      convert = cv(playerHand.get(select)%14);//converts value to charge value
      playerHand = remove(playerHand,select); //removes the card
      if (intDeck.size() > 0)
      {
        playerHand = draw (intDeck,playerHand);//replaces card
      }
      pcharge += convert; //adds the carge value
    }
    else if (name.equals("c"))
    {           
      convert = cv(cpuHand.get(cpuHand.size() -1)%14);//converts value to charge value
      cpuHand = remove(cpuHand,cpuHand.size() -1);//removes the card   
      if (intDeck.size() > 0)
      {
        cpuHand = draw(intDeck,cpuHand);//replaces card
      }
      cpucharge += convert;//adds the carge value           
    }
    
  }    
//=====================================<sort>=========================================    
  public ArrayList<Integer> sort (ArrayList<Integer> hand) //runs the through the deck sorting cards by lowest value to highest value
  {
    int value1,value2;
    for(int loop = 0; loop < hand.size(); loop++)
    {
      //System.out.print(deck[loop]); this was me check the output
      for (int p = 1; p < hand.size(); p++)
      {
        
        int y = p-1;
        value1 = hand.get(p);
        value2 = hand.get(y);
        if((value1%14)  > (value2 %14))
        {
          hand.set(p, value2);
          hand.set(y, value1);
        }
        
      }
      
    }
    // System.out.println();//checking output
    return hand;
  }
//====================================<Load hand>===================================
  public ArrayList<Integer> loadHand (ArrayList<Integer> deck)
  {
    ArrayList<Integer> hand = new ArrayList<Integer>();
    
    for (int p = 0; p <4; p++)
    {
      hand = draw(deck,hand);
      // System.out.print(hand.get(p));   for testing
    }
    hand = sort(hand); //sorts had from least to greatest
    return hand;
  }
//====================================<Load deck>===========================================
  
  public static ArrayList<Image> loadDeck (String source) //loads Image Deck depending the source inputted
  {
    String name;// holds the name of the card
    int track = 0; //track is a counter that keeps "track" of the rank of a card loaded
    
    ArrayList <Image> deckofcards = new ArrayList<Image>();
    for( int x = 0; x < 56; x++)
    {
      track += 1;
      if (track > 14)
        track = 1;
      if (x <= 13)
        name = track + "c";//stars with c = coin
      else if (x <= 26)
        name = track + "g";//changes to g = cup
      else if (x <= 40)
        name = track + "s";//changes to s = sword
      else
        name = track + "w";//changes to w = wand
      
      deckofcards.add(loadImage( source + "/" + name + ".png"));//combines to form a single string of the name and loads the image of it 
    }
    deckofcards.add(loadImage( source + "/back.png"));//load the back side of card
    //deckofcards.add(loadImage("deck/Back.png"));
    
    return deckofcards; //returns the image
  }
//===============================<Setup>============================================
  public void setup() // Initializes all vairables for the start of the game
  {
//list of values used    
    intDeck = Shuffle(initDeck ()); //initializes the main deck and suffles it 
    playerHand = loadHand(intDeck); //loads player hand with set amount of cards
    cpuHand = loadHand(intDeck); //loads cpu hand with set amount of cards
//boolean variable intialization
    battle = false; //will begin battle phase and graphics when true
    damage = false; // will begin damge phase and graphics when true
    pwin = false; //stores if player won
    cwin= false;//stores if cpu won
    player = false;//if player turn
//interger intialization
    cpuact = 0; // manages cpus action count
    pact = 0; // manages player action count
    plife = 30; // manages player life
    cpulife = 30; //manages cpu life
    select = 0; // manages player selections in the x plane
  }
//================================<New turn>========================================
  public void newTurn(String turn)
  {  
    
    if (turn.equals ("c"))
    {
      cpu.start();
      player = false;
      cpuact = 3;
      pact = 0;
    }
    
    else if (turn.equals( "p"))
    {
      Player2 ("sound/player.wav"); //Loads music file
      AudioPlayer.player.start (sfx); // starts music
      
      player = true;
      pact = 3;
      cpuact = 0;
    } 
    
  }
//===============================<Update>===========================================
  public void update ()
  {
    //revalidate();
    repaint();
  }
//================================<Shuffle>=========================================
  public static ArrayList<Integer> Shuffle(ArrayList<Integer> I)
  {//start
    for (int x = 1; x <= 10; x++)//shuffles the deck a few times
    {
      Collections.shuffle(I);
    }
    return I;
  }//end
//=================================<Reset>===============================================
  public void reset() //resets all values left over values for a new game (in case there are values left over for any given reason)
  {
    playerField.clear();  //holds a list of what is placed on the player's field 
    playerRanks.clear(); //holds a list of what the player's rank is for battle/damage
    cpuRanks.clear(); //holds a list of what is placed on the cpus's field
    cpuField.clear();//holds a list of what the cpus's rank is for battle/damage
    pcharge =0;// holds player's charge
    cpucharge =0;//holds cpu's charge
    wave = 0;//used in battle listener to manage phases
  }
//=================================<Integer deck>===================================
  public static ArrayList<Integer> initDeck ()  // initialize standard deck
  {
    ArrayList<Integer> deck  = new ArrayList<Integer>();
    for (int x = 1 ; x < 56 ; x++)
      deck.add(x); // cards represented by numbers 0-51
    return deck;
  }
//===================================<move left>====================================
  public void moveLeft(int index)
  {       if (index > 0)
    {
    if (select > 0)
      select -= 1;
    else if (index > 1)
      select = index -1; 
    else
      select = 0;
  }
  }
//===================================<move right>===================================
  public void moveRight(int index)
  { 
    if (index > 0)
    {
      if (select < index-1)
        select += 1;
      else 
        select = 0;
    }
    else 
      select = 0;
  }
//===============================<Who>===========================================
  public void who()//decides who goes first
  {//start
    result = flip(); //stores flip
    if ( result == 1) //check
    {first = true;
      newTurn("p");}  //sets player as first
    if ( result == 2)//check 
    {first = false;
      newTurn("c");} //sets cpu as first
  }//end
//===============================<Flip>==============================================
  public int flip ()// coin flip!!!
  {//start
    int f = (int)((Math.random() * 2) + 1);
    // System.out.print(f); for testing
    return f; //one or two
  }//end
//================================<Damage>================================================    
  public void damage ()
  {//start
    if(playerField.size() == 0 && cpuField.size() > 0)
    {//start check
      plife -= cpuRanks.get(0);//subtracks left over values from cpu life
      if (cpuField.size() > 0)//safety check
      { cpuRanks.remove(0);//removes numerical value
        cpuField.remove(0);}//removes graphical value
    }//end check
    else if(cpuField.size() == 0 && playerField.size() > 0)
    {//start check
      cpulife -= playerRanks.get(0);//subtracks left over values from cpu life
      if (playerField.size() > 0)//stafety check
      {playerRanks.remove(0);//removes numerical value
        playerField.remove(0);}//removes graphical value
    }//end check  
    if (playerField.size() == 0 && cpuField.size() == 0)
    {damage = false;}//end damage phase  
    
  }//end 
//========================================<AI>=========================================
  public int AI()//the cpu's AI 
  {//start
    int choose = (int)((Math.random()* 10) +1);//choices are somewhat random like poeple
    if (choose < 4)// if choose = 1,2,3 cpu will...
    { if (cpuHand.size() <= 4)//checks if his hand is small and prioritize drawing
      {return 1;} // if so cpu chooses to draw a card 
    else if (cpuHand.size() > 4 )//checks if hand is large
    {return 2;}}//if so he charges a card
    else if (choose > 4 && cpuHand.size() > 0)//if choose is = 5,6,7,8,9,10 cpu will...
    {return 3;}//play a card to the field
    else if (choose == 4 && cpuHand.size() > 0)//increases chance to charge since drawing is prioritized
    {return 2;}//charges card
    return 0;//if unable to do an action returns 0 
  }//end
//====================================<Spit>==========================================================
  public void spit(ArrayList<Integer> l1, ArrayList<Integer> l2) //spits out the values of 2 lists for xhecking values
  {//start
    for (int l = 0; l < l1.size(); l++)  
    {//start
      System.out.print("\nPlayer:" + l1.get(l) +  " ");
    }//end 
    System.out.println(); 
    for (int l = 0; l <  l2.size(); l++)  
    {//start
      System.out.print("\n Cpu:" + l2.get(l) +  " ");
    }//end
  }//end
//====================================<Battle>========================================================
  public void battle ()// compares list values and subtracts 
  {
    if (playerRanks.size() > 0 && cpuRanks.size() > 0)//one will do battle when one size has cards left
    {//starts check
      if (playerRanks.get(0) == cpuRanks.get(0))// check if values are equal
      {//start
        //no math required just removes both cards
        playerRanks.remove(0);//removes numerical value 
        cpuRanks.remove(0);//removes numerical value 
        playerField.remove(0);//removes graphical value 
        cpuField.remove(0);//removes graphical value
      }//end
      else if (playerRanks.get(0) > cpuRanks.get(0)) // if player's value is greater than cpu's
      {//start
        playerRanks.set(0, playerRanks.get(0) - cpuRanks.get(0));// subtracts player's value by cpu's value
        cpuRanks.remove(0);//removes numerical value
        cpuField.remove(0);//removes graphical value
        // Cpuranks =  promotion(convertRank(Cpufield), convertSuit(Cpufield), convertSuit(Playerfield));
      }//end
      else if (playerRanks.get(0) < cpuRanks.get(0))// if cpu's value is greater than player's
      {//start
        cpuRanks.set(0, cpuRanks.get(0) - playerRanks.get(0));// subtracts cpu's value by player's value  
        playerRanks.remove(0);//removes numerical value
        playerField.remove(0);//removes graphical value
        //Playerranks = promotion(convertRank(Playerfield), convertSuit(Playerfield), convertSuit(Cpufield));
      }//end
      Player2("sound/hit1.wav");
      AudioPlayer.player.start (sfx);
    }//end of check 
    else {battle = false; //stops battle phase
      Player2("sound/attack.wav");
      AudioPlayer.player.start (sfx);
      damage = true;}//starts damage phase
  }
//====================================<Random song generator>==========================================
  public String randomsong()//song randomizer
  {
    ArrayList<String> songbank = new  ArrayList<String>();//hold song name
    int ran;//stores random value
    //playlist of music
    songbank.add("Beats To Chaos");//if I hand drums and knew how to play them this is how it would sound
    songbank.add("Rythm of Day");//a guitar centered version of Tone of the Soul
    songbank.add("Synth of the Stars");//a Synthetic remix of Tone of the Soul 
    songbank.add("Tone of the Soul");//My first song I made (I got carried away and made it 9 mins long...)
    songbank.add("Tone of the Soul");// A Mix of different 
    ran = (int)(Math.random()* songbank.size());//genterates random value
    return songbank.get(ran);//returns the random string
    
  }
//============================================<Buff>===================================================  
  public int buff (ArrayList<Integer> index) //when charge reaches 10 next charge will grant a random buff costs no action point
  {//start
    int buff = (int)((Math.random() * 10) + 1);
    if (buff > 6 && index.size() <= 4 && intDeck.size() > 0)//when buff is equal to 7,8,9,10 and hand is less than or equal to 4 and deck is greater than 0
      return 1; //a card will be drawn
    else if (buff <= 3 && index.size() > 4)//when buff is equal to 3,2,1 and hand is greater than 0 
      return 2;// an action point will be given
    else//if none are true...
      return 3;//deals 5 damage to opponent
  }
//=============================================<Execute>====================================================
  public void execute (int b, String s)//chooses what to buff to activate
  {
    if (b == 1)
    {//start
      if (s.equals("p"))
      {
        Player2 ("sound/draw.wav"); //Loads music file
        AudioPlayer.player.start (sfx); // starts music 
        playerHand = draw(intDeck,playerHand); 
      }
      else if(s.equals("c"))
      {
        Player2 ("sound/draw.wav"); //Loads music file
        AudioPlayer.player.start (sfx); // starts music 
        cpuHand = draw(intDeck,cpuHand);  
      }
    }//end
    else if (b == 2)
    {//start
      if (s.equals("p"))
      {pact +=1;
        Player2 ("sound/exhaust.wav"); //Loads music file
        AudioPlayer.player.start (sfx);} 
      else if(s.equals("c"))
      {cpuact +=1;
        Player2 ("sound/exhaust.wav"); //Loads music file
        AudioPlayer.player.start (sfx);}           
    }//end
    else if (b == 3)
    {//start
      if (s.equals("p"))
      {cpulife -=10;
        Player2 ("sound/fire.wav"); 
        AudioPlayer.player.start (sfx);} 
      else if(s.equals("c"))
      {plife -=10;
        Player2 ("sound/fire.wav");
        AudioPlayer.player.start (sfx);}          
    }//end
    
    if (s.equals("p"))
    {pcharge -=10;} 
    else if(s.equals("c"))
    {cpucharge -=10;}    
    
  }
//===========================================<Decide>=====================================================  
  public void decide()
  {
    if (cpulife <= 0)//checks life
    {pwin = true;}//Player wins!!!
    else if (plife <= 0)//checks life
    {cwin = true;}//Cpu wins...
    
    if (pwin == true || cwin == true)//when a side wins 
    {//check
      //freezes all game interactions
      if (cpulife <= 0)//checks life
    { AudioPlayer.player.stop (playing);//stops all music
      Player2("sound/Victory.wav");
      AudioPlayer.player.start(sfx);}//Player wins!!!
    else if (plife <= 0)//checks life
    { AudioPlayer.player.stop (playing);//stops all music
      Player2("sound/Defeat.wav");
      AudioPlayer.player.start(sfx);}//Cpu wins...
      t.stop();
      player = false;//sets player to false
      pact = 0;//sets player actions as 0
      cpuact = 0;//sets cou's actions as 0
      war.stop();//stops battle
    }//check
    
  }   
//=================================<Panel constructor>============================== 
  public OJ_ArcanaWarPlus() 
  {// start of panel setup
    // Creating JPanel for the background and JPanel for interations (options)
    setLayout (new BorderLayout ());// Use BorderLayout for main panel
    JPanel background = new JPanel (); // Create a content panel to hold the backgournd images
    background.setLayout(null);// sets layout to null so that set bounds can be used 
    // timer setup   
    
    // Colouring panels  
    setBackground(Color.BLACK);
    //timer setup   
    t.addActionListener(new TimerListener());
    cpu.addActionListener(new CpuListener());
    war.addActionListener(new BattleListener());
    animation.addActionListener(new AnimationsListener());
    // Creating Drawing Area and Panel Setup
    DrawArea playfield = new DrawArea (1080, 740);
    background.add(playfield); // Output area
    setContentPane(background);
    background.setFocusable(true);
//--------------------------------------------------------------------        
    background.addKeyListener (new KeyListener () {
      @Override
      public void keyTyped(KeyEvent e){}
      
      @Override
      public void keyPressed(KeyEvent e) 
      { 
        //  System.out.println (select); //for testing
        
        if (pact > 0 && player == true && info == false) //actions only occur if greater than 0 and if players turn and if not looking not instructions
        {//start of check
          if (e.getKeyCode() == KeyEvent.VK_Q) //to play slected card
          {//start
            if (playerField.size() < 3 && playerHand.size() > 0)
            {
              Player2 ("sound/out.wav"); //Loads music file
              AudioPlayer.player.start (sfx); // starts music
              playerField.add(playerHand.get(select));//adds to field
              playerHand = remove(playerHand,select); //removes from hand
              pact -= 1;//consumes an action point
            }
          }//end
          else if (e.getKeyCode() == KeyEvent.VK_W)//to draw from the deck
          {//start
            if (playerHand.size() < 6 && intDeck.size() > 0)
            {
              Player2 ("sound/draw.wav"); //Loads music file
              AudioPlayer.player.start (sfx); // starts music 
              playerHand = draw(intDeck,playerHand); 
              pact -=1;
            }
          }//end
          else if (e.getKeyCode() == KeyEvent.VK_E)//to charge selected card
          {//start
            if (pcharge >=10)
            {execute(buff(playerHand),"p");}
            else if (intDeck.size() > 0)
            {
              charge("p");//charged the selected card
              pact -= 1;
            }
          }//end
          
        }//end of check
        
        if (start == true || player == true)
        {//start check
          if (e.getKeyCode() == KeyEvent.VK_ENTER) //closes/opens info screen
          {//start
            // System.out.println("hi"); for testing
            if (title == true)
            {
              title = false;
              start = false;
              setup();
              toss = true;
              game = true;
              animation.start();
            }
          }//end   
          else if (e.getKeyCode() == KeyEvent.VK_D) //ends turn
          {//start
            if (first == true)
            {//start
              //System.out.print("WORK DARN YOU WORK!!!");//testing output
              newTurn("c");
            }//end
            else
            {//start
              battle = true;
              war.start();
            }//end
          }//end 
          else if (e.getKeyCode() == KeyEvent.VK_R)// to add back card to hand 
          {//start
            if (playerField.size() > 0)
            {
              Player2 ("sound/in.wav"); //Loads music file
              AudioPlayer.player.start (sfx); // starts music
              playerHand.add(playerField.get(0));//adds to hand
              playerField = remove(playerField,0); //removes from field
              playerHand = sort(playerHand); //sorts hand (high to low)
              pact += 1;// returns an action point
            }          
          }//end 
          else if (e.getKeyCode() == KeyEvent.VK_T) //opens instructions
          {//start
            if (info == true)
            {
              // System.out.print("OPEN DARN YOU OPEN!!!");//testing output
              info = false;//turns instrctions off!
              select = 0;//resets select
              t.start();//turns updater back on
              Player2 ("sound/close.wav"); //Loads music file
              AudioPlayer.player.start (sfx); // starts music  
            }
            else if (info == false)
            {
              Player2 ("sound/open.wav"); //Loads music file
              AudioPlayer.player.start (sfx); // starts music  
              t.stop();//Stops update to save memory(your read instructions and i will keep updating and then memory will be used up)
              info = true;//turns instrctions on!
              select = 0;//resets select
              update();
            }
          }//end
          else if (e.getKeyCode() == KeyEvent.VK_RIGHT) //select move right
          {//start
            if (info != true) 
            {//start
              Player2 ("sound/switch.wav"); //Loads music file
              moveRight(playerHand.size());//changes select 
            }//end
            else
            {//start
              Player2 ("sound/forward.wav"); //Loads music file
              moveRight(book.size());//changes select
              update();//changes the page
            }//end
            AudioPlayer.player.start (sfx); // starts music   
          }//end
          else if (e.getKeyCode() == KeyEvent.VK_LEFT) //select moves left
          {//start
            if (info != true) 
            {//start
              Player2 ("sound/switch.wav"); //Loads music file
              moveLeft(playerHand.size());// changes select  
            }//end
            else
            {//start
              Player2 ("sound/back.wav"); //Loads music file
              moveLeft(book.size());// changes select    
              update();//changes the page
            }//end
            AudioPlayer.player.start (sfx); // starts music        
          }//end 
          else if (e.getKeyCode() == KeyEvent.VK_S) //select moves left
          {//start
            // System.out.print("play darn you PLAY!!!");// just me testing the output 
            AudioPlayer.player.stop (playing); // stops music           
            Player ("sound/orginal/"+ randomsong() +".wav"); //Loads music file
            AudioPlayer.player.start (playing); // starts music  
          }//end 
          else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) //closes the game forcefully...
          {//start
            setVisible(false); //ha ha ha you can't see me!             
            dispose(); //Destroy the JFrame object
            System.exit(0);//exits program and resets interactions interface 
          }//end 
        }//end of check
        else if (pwin == true || cwin == true)
        {//start of check
          if (e.getKeyCode() == KeyEvent.VK_ESCAPE) //closes the game forcefully...
          {//start
            setVisible(false); //ha ha ha you can't see me!             
            dispose(); //Destroy the JFrame object
            System.exit(0);//exits program and resets interactions interface 
          }//end  
          else if (e.getKeyCode() == KeyEvent.VK_ENTER) //closes the game forcefully...
          {//start
            // System.out.println("hi"); 
            reset();
            t.start();
            title = false;
            start = false;
            setup();
            toss = true;
            game = true;
            animation.start();
          }//end 
        }//end of check 
      }
      @Override
      public void keyReleased(KeyEvent e){}});
//--------------------------------------------------------------------
    pack();
    // Game window Setup  
    setTitle("Arcana War Plus");
    background.setPreferredSize (new Dimension (1080,740));
    setSize(1080,740); //Sets the JFrame size
    setVisible(true); //Reveals JFrame
    setResizable(false); 
    setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);//centers window on launch    
  }//end of panel setup
//========================================<Draw class>======================================= 
  class DrawArea extends JPanel
  {//start of drawarea
    // load images for use
    ArrayList <Image> system = new ArrayList<Image>(); // systems graphics example: gameboard  
    ArrayList <Image> deckr = loadDeck("deckr"); // reversed image deck for cpu
    ArrayList <Image> deck = loadDeck("deck"); // normal image deck 
    
    public DrawArea (int width, int height)// Create panel of given size
    {//start of drawarea
      this.setBounds( 0, 0, width, height);//(new Dimension (width, height));
    }//end of drawarea
    
    public void paintComponent (Graphics g)  // g can be passed to a class method
    {//start of paintComponent
      Image back = loadImage("system/wallpaper.png");//loads background
      g.drawImage(back,40,5,null);//draws background
      
      if (title == true)//draws if true
      {//start
        showStart(g);//shows title screen
      }//end
      else if (toss == true)
      {
        showToss(g);
      }
      else if (cwin == true || pwin == true)
      {//start
        showWinner(g);
      }//end
      else if (info == true)//draws if true
      {//start
        showInfo(g);//displays info
      }//end
      else if (game == true)
      {
        drawGame(g,deck,deckr);
      }
      //cpu y=5 y2 =188
      //player y =538 y2 =358
    }//end of paintComponent
    public void drawGame(Graphics g, ArrayList<Image> deck,ArrayList<Image> deckr)  //draws all game graphics
    {//start
      showBoarders(g);
      Image info = loadImage("system/info.png");//loads image 
      g.drawImage(info,920,590,null);//draws image
      showHand(g,playerHand,deck,538,"p");
      showHand(g,cpuHand,deckr,5,"c");
      showField(g,playerField,deck,358,"p");
      showField(g,cpuField,deckr,188,"c");
      selection(g,deck);
      showValues(g); 
    }//end
    //----------------------------------------------------------------------------------------
    public void showHand (Graphics g, ArrayList<Integer> hand, ArrayList<Image> image,int y, String s)
    {//start
      for ( int p = 0; p < hand.size(); p++)
      {
        if(s.equals ("p"))//if equal to player  
        {
          g.drawImage(image.get(hand.get(p)),  ((p* 130)+ (540 -(67* hand.size()))) , y,null);// shows player hand
        }
        if (s.equals ("c") )//if equal to cpu
        {
          if (p == (cpuHand.size()/2) && first == true)//if player is first 
            g.drawImage (image.get(hand.get(p)),  (((p* 130)+ (540 -(67* hand.size()))))-50  , y,null);//shows middle card
          else //if not
            g.drawImage (image.get(56),  (((p* 130)+ (540 -(67* hand.size()))))-50, y,null); //cards are hidden
        }
      }
    }//end
    //-------------------------------------------------------------------------------------
    public void showField (Graphics g, ArrayList<Integer> hand,ArrayList<Image> image, int y, String s)
    {//start
      if(hand.size() > 0)
      {
        for (int p = 0; p < hand.size(); p++)
        {
          //draws the back of the card depending on if it's cpu or player
          if (s.equals("p"))//if equals player
            g.drawImage (image.get(56), (540-((p+hand.size())*84)) + (p* 265), y,null);//draw
          else if (s.equals("c"))//if equals cpu
            g.drawImage (image.get(56), ((540-((p+hand.size())*84)) + (p* 265)) -50, y,null);//draw
          if (battle == true || damage == true )//reveals face of card when in battle or damage phase
          {//start check
            //draws the front of the card depending on if it's cpu or player   
            if (s.equals("p"))//if equals player
              g.drawImage (image.get(hand.get(p)), (540-((p+hand.size())*84)) + (p* 265), y,null);//draw
            else if (s.equals("c"))//if equals cpu
              g.drawImage (image.get(hand.get(p)), ((540-((p+hand.size())*84)) + (p* 265))-50, y,null);//draw
          }//end check
        }
      }
    }//end   
    //-------------------------------------------------------------------------------------
    public void showValues (Graphics g)
    {//start
      Font f1 = new Font ("Impact", Font.PLAIN, 36);
      Font f2 = new Font ("Impact", Font.PLAIN, 24);
      g.setFont(f1);
      g.setColor(Color.WHITE);
      //displays life values------------------------------------------
      g.drawString("Cpu Life",80,220);
      g.drawString(String.valueOf(cpulife),80,260);
      g.drawString("Player Life",80,480);
      g.drawString(String.valueOf(plife),80,520); 
      //displays charge values----------------------------------------
      g.setFont(f2);
      g.drawString("Player charge",850,480);
      g.drawString(String.valueOf(pcharge),850,510); 
      g.drawString("Cpu charge",850,220);
      g.drawString(String.valueOf(cpucharge),850,250); 
      //displays action counter---------------------------------------
      g.drawString("Player Acts",840,400);
      g.drawString(String.valueOf(pact),840,430); 
      g.drawString("Cpu Acts",840,290);
      g.drawString(String.valueOf(cpuact),840,330); 
      //diplays cards left in deck -----------------------------------
      g.drawString("Cards left in deck: "+String.valueOf(intDeck.size()),820,360);
    }//end
    //-----------------------------------------------------------------------------------
    public void showBoarders (Graphics g)
    {//start
      Color niceblue = new Color(33,35,60);//it's a nice blue what else would it be
      g.setColor(niceblue);// sets the color
      g.fillRect(0,0,1080,740);// draws the background
      Image back = loadImage("system/wallpaper.png");//loads background
      g.drawImage(back,40,5,null);//draws background
      Image mat = loadImage("system/battlemat.png");//loads battle field 
      g.drawImage(mat,257,177,null);//draws battle field 
    }//end
    //---------------------------------------------------------------------------------
    public void selection (Graphics g, ArrayList<Image> deck)//shows selection
    {//start
      if (playerHand.size() != 0)//if hand size is not equal to 0
      {//check
        g.setColor(Color.ORANGE);//set color to orange as it is the complimentary color to blue  
        if(playerHand.size() > 0)
        {//2nd check
          if(select >= playerHand.size())//if select exceeds hand size
            select -=1;//shifts select
        }//end of 2nd check
        else 
          select =0;//shifts select
        g.fillRect((select  * 130) + (535 - (67 * playerHand.size())), 538, 130,300); //draw a rectange for outline
        g.drawImage(deck.get(playerHand.get(select)), (select  * 130) + (540 - (67 * playerHand.size())), 538,null); //redraws card 
      }//end of check    
    }//end
    //---------------------------------------------------------------------------------
    public void showStart(Graphics g)
    {//start
      Image titlescreen = loadImage("system/title.png");//loads title screen
      g.drawImage(titlescreen,40,5,null);//draws title screen
    }//end
    //--------------------------------------------------------------------------------
    public void showInfo(Graphics g)//draws instructions
    {//start
      for (int p = 1; p <= 4; p++)
      {
        book.add(loadImage("system/book/page" +p+ ".png"));// loads image
      }
      g.drawImage(book.get(select),60,0,null);//draws image
    }//end
    //---------------------------------------------------------------------------------
    public void showWinner(Graphics g)
    {
      Font f1 = new Font ("Impact", Font.PLAIN, 30);
      g.setFont(f1);
      g.setColor(Color.WHITE);
      if (cwin == true)
      {Image banner = loadImage("system/defeat.png");// loads image
        g.drawImage(banner,260,0,null);}//draws image
      else if (pwin ==true)//
      {Image banner = loadImage("system/victory.png");// loads image
      //end prompt for close/continue  
      g.drawImage(banner,90,0,null);}//draw image
      g.drawString("Press [ENTER] To Play Again",130,650);//message
      g.drawString("Press [ESC] To Close",670,650);//message
      
    }
    //--------------------------------------------------------------------------------
    public void showToss(Graphics g)//flips the coin
    {
      Font f1 = new Font ("Impact", Font.PLAIN, 72);//creates font
      g.setFont(f1);//sets font
      Color gold = new Color (218,165,32);//creates a gold color
      g.setColor(gold);//sets color
      ArrayList<Image> coin = new ArrayList<Image>();//list to hold frames
      for (int p = 1; p <= 14 ; p++)
      {
        coin.add(loadImage("system/coin/"+p+".png"));//loads in frames
      }
      if (frames < coin.size())//if frames is within the list size
      {
        g.drawImage(coin.get(frames),450,270,null); //display this animation
      }
      else //if not
      {
        g.drawImage(coin.get(13),450,270,null);//displays the flat part of the coin
        g.drawString(String.valueOf(result),525,390);//displays the result of flip
      }
    }
  }//end of drawarea
//==================================================================================
  //                                 Listeners                                  \\
//==================================================================================
  class TimerListener implements ActionListener //reacts to timer
  {//start
    TimerListener()
    {//start
      t.start();//starts timer
    }//end
    public void actionPerformed (ActionEvent e)
    {//start of void
      update();//updates graphics
      decide();//decides what to do when a side wins
    } //end of void
  }//end of class 
//===============================================================  
  class CpuListener implements ActionListener //reacts to timer
  {//start   
    int move = 0;//manages cpu's moves
    public void actionPerformed (ActionEvent e)//rolls through the AI and decides what it wants to do
    {//start of void    
      move = AI();
      //System.out.println("I am alive!!!: " + move);//testing output
      
      if (cpuact > 0)
      {//start check
        if (move == 1)//draws card or charges a card
        {//start
          if (cpuHand.size() < 6)
          {//start
            if (intDeck.size() > 0)
            {//start
              Player2 ("sound/draw.wav"); //Loads music file
              AudioPlayer.player.start (sfx); // starts music 
              cpuHand = draw(intDeck, cpuHand);//draws a card if hand is less than 6 and drck is greater than 0  
              cpuact -= 1;//comsumes an action point
            }//end
          }//end
        }//end       
        else if (move == 2)//charges a card if hand is greater than 0
        {//start
          if(cpucharge >=10)
          {execute(buff(cpuHand),"c");}
          else if (cpuHand.size() > 0 && intDeck.size() > 0)//only charges if hand is greater than 0 and deck is greater than 0
          {//start
            charge ("c");//charges cpu
            cpuact -= 1;//consumes an action point
          }//end
        }//end
        else if (move == 3)
        {//start
          if(cpuHand.size() > 0)
          {//start
            if (cpuField.size() < 3)
            {Player2("sound/out.wav");
              AudioPlayer.player.start (sfx);
              cpuField.add(cpuHand.get(0));//adds card to field
              cpuHand.remove(0);//removes card from hand
              cpuact -= 1;}//consumes an action point
          }//end
        }//end
      }//end of check
      else if (first == false)// if cpu is first
      {//start
        newTurn("p");
        cpu.stop(); //ends turn 
      }//end
      else if(first == true)//if cpu is second
      {//start
        battle = true; //goes to battle phase
        war.start();//bgins the war
        cpu.stop(); //ends turn
      }//end
    }//end of void       
  }//end of class   
//===============================================================  
  class BattleListener implements ActionListener //reacts to timer
  {//start   
    public void actionPerformed (ActionEvent e)//rolls through the AI and decides what it wants to do
    {//start of void    
      if(wave == 0)// if wave equals 0
      { if (cpuField.size() > 0)
        cpuRanks = convertRank(cpuField);
      if (playerField.size() > 0)
        playerRanks =  convertRank(playerField);
      wave++;
      Player2("sound/battle.wav");
      AudioPlayer.player.start (sfx);
      }//end
      else if (battle == true)
      { battle();}//battle phase       
      else if (damage == true)
      {damage();//damage phase
        Player2("sound/hit1.wav");
        AudioPlayer.player.start (sfx);}
      else if (wave < 3) //new round gives 2 new cards to players
      { cpuHand = draw(intDeck, cpuHand);
        playerHand = draw(intDeck, playerHand);
        Player2 ("sound/draw.wav"); //Loads music file
        AudioPlayer.player.start (sfx); // starts music 
        wave++;}
      else if (first == true)//if player was first
      {newTurn("p");//sets turn to player
        war.stop();//stops war timer 
        wave = 0;}//resets wave
      else if (first == false)//if cpu was first
      {newTurn("c");//sets turn to cpu
        war.stop(); //stops war timer
        wave = 0;}//resets wave
    }//end of void
  }//end   
//====================================================================
  class AnimationsListener implements ActionListener
  {//start of class
    public void actionPerformed (ActionEvent e)
    {//start of void
      if (frames < 14 && toss == true)
      {//start of check
        if (frames == 2)
        {Player2 ("sound/flip.wav"); //Loads music file
          AudioPlayer.player.start (sfx);}// starts music
        frames++;}//end of check
      else if (frames >= 14 && frames < 28)
      {frames ++;
        if(frames == 15)
        {who();}}//so that player can see results
      else
      {toss = false;
        frames = 0;
        animation.stop();}//end of else    
    }//end of void
  }//end of class
//================================================================================== 
  public static void main(String[] args)
  {///start of main 
    OJ_ArcanaWarPlus game = new  OJ_ArcanaWarPlus ();
    game.setVisible (true);
  }//end of main
}///end of class
