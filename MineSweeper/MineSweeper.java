import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.net.URL;
import java.io.*;


class MineSweeper{
   static JFrame frame;
   static GamePanel gamePanel;
   static SettingsPanel settingsPanel;
 
public static void main(String[] args){
   frame = new JFrame("MineSweeper");
   gamePanel = new GamePanel();
   settingsPanel = new SettingsPanel(gamePanel);
   
  
   gamePanel.setRequestFocusEnabled(true);
   gamePanel.requestFocusInWindow();
  
   //gamePanel.addKeyListener(new KeyBoardListener());
	    
   frame.setLayout(new BorderLayout());   
   frame.add(gamePanel, BorderLayout.CENTER);
   frame.add(settingsPanel, BorderLayout.PAGE_START);
   //frame.add(playAgainButton, BorderLayout.PAGE_END);
 
    
   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   //frame.setResizable(false);
   frame.setVisible(true);
   frame.pack();
   
   }
}

class SettingsPanel extends JPanel{
    
static TextField boardSizeTxtField;
static TextField nMinesTxtField;

Color backgroundColor = Color.LIGHT_GRAY;
static final int SIZE_X= 500;
static final int SIZE_Y=110;

//int boardSize=50;
//int nMines=;
static ImageIcon FACE_ICON = new ImageIcon(Tile.class.getResource("Icons/Smile.PNG"));
static ImageIcon SAD_ICON = new ImageIcon(Tile.class.getResource("Icons/Sad.PNG"));
static ImageIcon LOSE_ICON = new ImageIcon(Tile.class.getResource("Icons/SmileyLose.jpg"));
static ImageIcon WIN_ICON = new ImageIcon(Tile.class.getResource("Icons/SmileyWin.jpg"));
static JButton faceButton;
GamePanel gamePanel;


public SettingsPanel(GamePanel gamePanel){
 FACE_ICON = new ImageIcon(FACE_ICON.getImage().getScaledInstance( 100,100 ,  java.awt.Image.SCALE_SMOOTH )) ;
 LOSE_ICON = new ImageIcon(LOSE_ICON.getImage().getScaledInstance( 100,100 ,  java.awt.Image.SCALE_SMOOTH ));
 SAD_ICON = new ImageIcon(SAD_ICON.getImage().getScaledInstance( 100,100 ,  java.awt.Image.SCALE_SMOOTH ));
 WIN_ICON = new ImageIcon(WIN_ICON.getImage().getScaledInstance( 100,100 ,  java.awt.Image.SCALE_SMOOTH ));
 faceButton=new JButton();
 faceButton.setPreferredSize(new Dimension(100,100));
 faceButton.setMaximumSize(new Dimension(100,100));
 faceButton.setIcon(FACE_ICON); 
 faceButton.setPressedIcon(SAD_ICON);

 this.gamePanel=gamePanel;
 this.setBackground(backgroundColor);
 this.setVisible(true);
 this.setFocusable(true);
 this.setPreferredSize(new Dimension(SIZE_X,SIZE_Y));
 this.setMaximumSize( new Dimension( Integer.MAX_VALUE, 120));
 
 boardSizeTxtField = new TextField(Integer.toString(GamePanel.DEFAULT_BOARD_SIZE));
 nMinesTxtField = new TextField(Integer.toString(GamePanel.DEFAULT_N_MINES));
 
 faceButton.addActionListener(new SettingsListener(this,gamePanel));
 
 this.add(boardSizeTxtField);
 this.add(faceButton);
 this.add(nMinesTxtField);
 
}

}


class SettingsListener implements ActionListener{
    static final int MAX_TILES=50*50;
    GamePanel gamePanel;
    SettingsPanel settingsPanel;
    public SettingsListener(SettingsPanel settingsPanel,GamePanel gamePanel){
        this.gamePanel=gamePanel;
        this.settingsPanel=settingsPanel;
    }
    
    public void actionPerformed(ActionEvent e){
        JButton face = (JButton)e.getSource();
        int n = Integer.parseInt(SettingsPanel.boardSizeTxtField.getText())*Integer.parseInt(SettingsPanel.boardSizeTxtField.getText());
        if( n >= Integer.parseInt(SettingsPanel.nMinesTxtField.getText()) && n <= MAX_TILES){
            gamePanel.setBoard(Integer.parseInt(SettingsPanel.boardSizeTxtField.getText()),Integer.parseInt(SettingsPanel.nMinesTxtField.getText()));        
            face.setIcon(SettingsPanel.FACE_ICON);
        }
        else{
            face.setIcon(SettingsPanel.SAD_ICON);
        }
    }
    
    
}


class GamePanel extends JPanel{

Color backgroundColor = Color.LIGHT_GRAY;
static int SIZE_X= 500;
static int SIZE_Y=500;
static final int DEFAULT_BOARD_SIZE=25; //10*10
static final int DEFAULT_N_MINES=75;
Board board;

public GamePanel(){


this.setBackground(backgroundColor);
	this.setVisible(true);
	this.setFocusable(true);
	this.setPreferredSize(new Dimension(SIZE_X,SIZE_Y));   
    this.setLayout(new GridBagLayout());
    
    this.setBoard(DEFAULT_BOARD_SIZE,DEFAULT_N_MINES);
     
    //this.pack();
    this.addComponentListener(new ComponentAdapter() 
    {  
        public void componentResized(ComponentEvent evt) {
            GamePanel gamePanel = (GamePanel)evt.getSource();
            gamePanel.SIZE_X=(int)gamePanel.getSize().getWidth();
            gamePanel.SIZE_Y=(int)gamePanel.getSize().getHeight();
            gamePanel.resizeBoard();
            
        }
    });
} 

public void setBoard(int size, int nMines){
    GridBagConstraints c = new GridBagConstraints();
        
    this.board = new Board(size,nMines);
    c.gridx=0;
    c.gridy=0;
    this.removeAll();
    for(int i=0;i<board.size;i++){
        for(int j=0;j<board.size;j++){
            this.add(board.tiles[i][j],c);
            c.gridx++;           
        }
        c.gridy++;
        c.gridx=0;
    }
    this.revalidate();
    this.repaint();
    
}

public void resizeBoard(){
     this.board.initTilesIcons();
     for(int i=0;i<this.board.tiles.length;i++){
            for(int j=0;j<this.board.tiles[0].length;j++){
                board.tiles[i][j].setPreferredSize(new Dimension(GamePanel.SIZE_X/board.size,GamePanel.SIZE_Y/board.size));
                board.tiles[i][j].setMaximumSize(new Dimension(GamePanel.SIZE_X/board.size,GamePanel.SIZE_Y/board.size));
                 if(board.tiles[i][j].revealed){
                    if(board.tiles[i][j].countMines!=-1){                    
                    board.tiles[i][j].setIcon(board.tiles[i][j].imageIcons[board.tiles[i][j].countMines]);
                    }
                    else{
                         board.tiles[i][j].setIcon(Tile.MINE_ICON);
                    }
                 }
                 else{
                    if(board.tiles[i][j].flagged){
                        board.tiles[i][j].setIcon(Tile.FLAG_ICON);
                    }
                    else{
                        board.tiles[i][j].setIcon(Tile.BLANK_ICON);
                    }
                 }
                board.tiles[i][j].revalidate();
                board.tiles[i][j].repaint();
            }   
     }
     this.revalidate();
     this.repaint();
}

}

class Coordinates{
    int x,y;
    public Coordinates(int x,int y){
        this.x=x;
        this.y=y;
    }
    
    public boolean equals(Object o){
        Coordinates c = (Coordinates) o;
        return (this.x == c.x && this.y==c.y);      
    }
    
    public int hashCode(){
        return Integer.parseInt(Integer.toString(this.x)+Integer.toString(this.y));
    }
    
    
}

class Board{
    int size;
    int nMines;
    
    Tile[][] tiles;
    ArrayList<Coordinates> mines = new ArrayList<Coordinates>();
    //HashSet<Coordinates> mines = new HashSet<Coordinates>();
    
    public Board(int size, int nMines){
         this.size=size;
         this.nMines=nMines;
         
         this.tiles = new Tile[size][size];
         mines.clear();
         this.init();        
    }
    
    private void init(){
        this.initTilesIcons();
        Tile.revealedCount=0;
        
        //mines
        int n =0;
        Random random = new Random();
        
        while(n<this.nMines){             
            Coordinates newMine= new Coordinates(random.nextInt(this.size),random.nextInt(this.size));          
            if(!this.mines.contains(newMine)){
                this.mines.add(newMine);
                n++;
                System.out.println(newMine.x+" "+newMine.y);
            }
        }
        
        //numbers
        double start = System.currentTimeMillis();
        for(int i=0;i<this.size;i++){
            for(int j=0;j<this.size;j++){
               
                int count=0;
                for(int ai=-1; ai<=1; ai++){
                    for(int aj=-1; aj<=1; aj++){
                        if(i+ai>=0 && j+aj>=0){
                            if(this.mines.contains(new Coordinates(i+ai,j+aj))){
                                count++;
                            }
                        }
                    }  
                }  
                
                if(this.mines.contains(new Coordinates(i,j))){
                    this.tiles[i][j]= new Tile(i,j,this,-1);   
                }
                else{                    
                    this.tiles[i][j]= new Tile(i,j,this,count);
                }
                  
                
            }
            //System.out.println();            
        }
        
        double end = System.currentTimeMillis() - start;
        System.out.println(end+"ms");
        
        /*
        start = System.currentTimeMillis();
        for(int i=0;i<this.size;i++){
            for(int j=0;j<this.size;j++){
                this.tiles[i][j]= new Tile(i,j,this,0);
            }
        }
        
        for(Coordinates mine : mines){
            for(int ai=-1; ai<=1; ai++){
                    for(int aj=-1; aj<=1; aj++){
                        if(mine.x+ai>0 && mine.y+aj>0 && mine.x+ai<this.size  && mine.y+aj<this.size){
                            if(ai==0 && aj==0){
                                this.tiles[mine.x+ai][mine.y+aj]= new Tile(mine.x+ai,mine.y+aj,this,-1);
                            }
                            else if(!this.mines.contains(new Coordinates(mine.x+ai,mine.y+aj))){
                                this.tiles[mine.x+ai][mine.y+aj]= new Tile(mine.x+ai,mine.y+aj,this,this.tiles[mine.x+ai][mine.y+aj].countMines++);
                            }  
                        }                        
                    }
            }           
        }
        end = System.currentTimeMillis() - start;
        System.out.println(end+"ms");
        */
               
        //printBoard
        /*
        System.out.println(); 
        System.out.println(); 
        for(int i=0;i<this.size;i++){
            for(int j=0;j<this.size;j++){
                try{
                System.out.print(this.tiles[i][j].countMines);
                }catch(Exception e){
                    System.out.print("("+i+","+j+")");
                }
            }
            System.out.println();
        }
        */
        
    }
    
    public void initTilesIcons(){
        Tile.BLANK_ICON = new ImageIcon(Tile.class.getResource("Icons/Blank.PNG"));
        Tile.FLAG_ICON =  new ImageIcon(Tile.class.getResource("Icons/Flag.PNG"));
        Tile.MINE_ICON =  new ImageIcon(Tile.class.getResource("Icons/Mine.PNG"));
        Tile.ICON_0 =  new ImageIcon(Tile.class.getResource("Icons/0.jpg"));
        Tile.ICON_1 =  new ImageIcon(Tile.class.getResource("Icons/1.jpg"));
        Tile.ICON_2 = new ImageIcon(Tile.class.getResource("Icons/2.jpg"));
        Tile.ICON_3 =  new ImageIcon(Tile.class.getResource("Icons/3.jpg"));
        Tile.ICON_4 =  new ImageIcon(Tile.class.getResource("Icons/4.jpg"));
        Tile.ICON_5 =  new ImageIcon(Tile.class.getResource("Icons/5.jpg"));
        Tile.ICON_6 =  new ImageIcon(Tile.class.getResource("Icons/6.jpg"));
        Tile.ICON_7 =  new ImageIcon(Tile.class.getResource("Icons/7.jpg"));
        Tile.ICON_8 =  new ImageIcon(Tile.class.getResource("Icons/8.jpg"));
        Tile.BLANK_ICON = new ImageIcon( Tile.BLANK_ICON.getImage().getScaledInstance( GamePanel.SIZE_X/this.size,GamePanel.SIZE_Y/this.size,  java.awt.Image.SCALE_SMOOTH )) ;
        Tile.FLAG_ICON =  new ImageIcon( Tile.FLAG_ICON.getImage().getScaledInstance( GamePanel.SIZE_X/this.size,GamePanel.SIZE_Y/this.size,  java.awt.Image.SCALE_SMOOTH )) ;
        Tile.MINE_ICON =  new ImageIcon( Tile.MINE_ICON.getImage().getScaledInstance( GamePanel.SIZE_X/this.size,GamePanel.SIZE_Y/this.size,  java.awt.Image.SCALE_SMOOTH )) ;
        Tile.ICON_0 =  new ImageIcon( Tile.ICON_0.getImage().getScaledInstance( GamePanel.SIZE_X/this.size,GamePanel.SIZE_Y/this.size,  java.awt.Image.SCALE_SMOOTH )) ;
        Tile.ICON_1 =  new ImageIcon( Tile.ICON_1.getImage().getScaledInstance( GamePanel.SIZE_X/this.size,GamePanel.SIZE_Y/this.size,  java.awt.Image.SCALE_SMOOTH )) ;
        Tile.ICON_2 =  new ImageIcon( Tile.ICON_2.getImage().getScaledInstance( GamePanel.SIZE_X/this.size,GamePanel.SIZE_Y/this.size,  java.awt.Image.SCALE_SMOOTH )) ;
        Tile.ICON_3 =  new ImageIcon( Tile.ICON_3.getImage().getScaledInstance( GamePanel.SIZE_X/this.size,GamePanel.SIZE_Y/this.size,  java.awt.Image.SCALE_SMOOTH )) ;
        Tile.ICON_4 =  new ImageIcon( Tile.ICON_4.getImage().getScaledInstance( GamePanel.SIZE_X/this.size,GamePanel.SIZE_Y/this.size,  java.awt.Image.SCALE_SMOOTH )) ;
        Tile.ICON_5 =  new ImageIcon( Tile.ICON_5.getImage().getScaledInstance( GamePanel.SIZE_X/this.size,GamePanel.SIZE_Y/this.size,  java.awt.Image.SCALE_SMOOTH )) ;
        Tile.ICON_6 =  new ImageIcon( Tile.ICON_6.getImage().getScaledInstance( GamePanel.SIZE_X/this.size,GamePanel.SIZE_Y/this.size,  java.awt.Image.SCALE_SMOOTH )) ;
        Tile.ICON_7 =  new ImageIcon( Tile.ICON_7.getImage().getScaledInstance( GamePanel.SIZE_X/this.size,GamePanel.SIZE_Y/this.size,  java.awt.Image.SCALE_SMOOTH )) ;
        Tile.ICON_8 =  new ImageIcon( Tile.ICON_8.getImage().getScaledInstance( GamePanel.SIZE_X/this.size,GamePanel.SIZE_Y/this.size,  java.awt.Image.SCALE_SMOOTH )) ;
        Tile.imageIcons[0]= Tile.ICON_0;
        Tile.imageIcons[1]= Tile.ICON_1;
        Tile.imageIcons[2]= Tile.ICON_2;
        Tile.imageIcons[3]= Tile.ICON_3;
        Tile.imageIcons[4]= Tile.ICON_4;
        Tile.imageIcons[5]= Tile.ICON_5;
        Tile.imageIcons[6]= Tile.ICON_6;
        Tile.imageIcons[7]= Tile.ICON_7;
        Tile.imageIcons[8]= Tile.ICON_8;
        
    }
      
}

class Tile extends JButton{
    //boolean isMine;
    static int revealedCount=0;
    boolean revealed=false;
    boolean flagged=false;
    int countMines;
    
    //ICONS
    static ImageIcon BLANK_ICON = new ImageIcon(Tile.class.getResource("Icons/Blank.PNG"));
    static ImageIcon FLAG_ICON =  new ImageIcon(Tile.class.getResource("Icons/Flag.PNG"));
    static ImageIcon MINE_ICON =  new ImageIcon(Tile.class.getResource("Icons/Mine.PNG"));
    static ImageIcon ICON_0 =  new ImageIcon(Tile.class.getResource("Icons/0.jpg"));
    static ImageIcon ICON_1 =  new ImageIcon(Tile.class.getResource("Icons/1.jpg"));
    static ImageIcon ICON_2 = new ImageIcon(Tile.class.getResource("Icons/2.jpg"));
    static ImageIcon ICON_3 =  new ImageIcon(Tile.class.getResource("Icons/3.jpg"));
    static ImageIcon ICON_4 =  new ImageIcon(Tile.class.getResource("Icons/4.jpg"));
    static ImageIcon ICON_5 =  new ImageIcon(Tile.class.getResource("Icons/5.jpg"));
    static ImageIcon ICON_6 =  new ImageIcon(Tile.class.getResource("Icons/6.jpg"));
    static ImageIcon ICON_7 =  new ImageIcon(Tile.class.getResource("Icons/7.jpg"));
    static ImageIcon ICON_8 =  new ImageIcon(Tile.class.getResource("Icons/8.jpg"));
       
        
    static ImageIcon[] imageIcons = new ImageIcon[9];
    
    ImageIcon image;
    int x,y;
    
    Board board;
    
    public Tile(int x, int y, Board board, int countMines){
        this.board=board;
        this.x=x;
        this.y=y;
        
        this.setPreferredSize(new Dimension(GamePanel.SIZE_X/board.size,GamePanel.SIZE_Y/board.size));
        this.setMaximumSize(new Dimension(GamePanel.SIZE_X/board.size,GamePanel.SIZE_Y/board.size));
        this.countMines=countMines;
       
        this.setIcon(BLANK_ICON);
        
        this.addMouseListener(new ButtonListener(this));
    }
        
}

class ButtonListener extends MouseAdapter{
    Tile tile;
    public ButtonListener(Tile tile){
        this.tile=tile;
    }
    
    public void mouseClicked(MouseEvent e){
        
         //System.out.println(e.getButton()+":");
         //System.out.println("left?"+MouseEvent.BUTTON1);
         //System.out.println("right?"+MouseEvent.BUTTON3);
         if(!this.tile.revealed){
             
             
             if (e.getButton() == MouseEvent.BUTTON1 &&  !this.tile.flagged){
                 //this.tile.getModel().setPressed(true);
                 //this.tile.setEnabled(false);
                 //this.tile.setText(Integer.toString(this.tile.countMines));
                if(this.tile.countMines==-1){
                    this.tile.setIcon(Tile.MINE_ICON);
                    SettingsPanel.faceButton.setIcon(SettingsPanel.LOSE_ICON);
                     //Tile.revealedCount--;
                }
                else{
                    this.tile.setIcon(Tile.imageIcons[this.tile.countMines]);
                    if(this.tile.countMines==0){
                        this.tile.revealed=true;
                        Tile.revealedCount++;
                        checkForBlanks(this.tile);
                        
                        //Tile.revealedCount--; //cheat fix
                    }
                    else{
                        this.tile.revealed=true;
                        Tile.revealedCount++;
                    }
                }
                 //this.tile.revealed=true;
                 //Tile.revealedCount++;
                 System.out.print(Tile.revealedCount+"==");
                 System.out.print(tile.board.size*tile.board.size+"-");
                 System.out.println(tile.board.nMines);
                 if(Tile.revealedCount==(tile.board.size*tile.board.size)-tile.board.nMines){
                    SettingsPanel.faceButton.setIcon(SettingsPanel.WIN_ICON);
                 }
             }
                 
             if (e.getButton() == MouseEvent.BUTTON3){
                 if(!tile.flagged){
                    this.tile.setIcon(Tile.FLAG_ICON);
                    tile.flagged=true;
                 }
                 else{
                    this.tile.setIcon(Tile.BLANK_ICON);
                    tile.flagged=false;
                 }
             }
         }             
    }
    
       static void checkForBlanks(Tile tile){
        //ArrayList<Tile> blanks = new ArrayList<Tile>();
        
        for(int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){
                if(tile.x+i>=0 && tile.x+i<tile.board.size && tile.y+j>=0 && tile.y+j<tile.board.size){
                    Tile at = tile.board.tiles[tile.x+i][tile.y+j];
                    if(!at.equals(tile)){
                        if(!at.revealed){
                            if(at.countMines==0){
                            Tile.revealedCount++;
                            at.revealed=true;
                            at.setIcon(Tile.imageIcons[0]);
                            checkForBlanks(at);
                            }
                            else{
                                 Tile.revealedCount++;
                                 at.revealed=true;
                                 at.setIcon(Tile.imageIcons[at.countMines]);
                            }
                        }                       
                    }                   
                }
            }
        }       
    }     
}









