/*
C:\>cmd /?

*/

class Launcher{

    public static void main(String[] args){
        
        try{
        String path = Launcher.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String filename= "MineSweeper";
        //Runtime.getRuntime().exec("cmd /c start cmd /c java "+filename);
        //Runtime.getRuntime().exec("cmd /c start cmd /k java "+filename);
        Runtime.getRuntime().exec("cmd /c java "+filename);
        }catch(Exception e){System.err.println(e);}
       
        }
}