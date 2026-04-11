package src.Server;
import java.io.*;
import java.net.*;
import java.util.*;
public class Server
{   
    private ServerSocket ss;
    private BufferedReader br;
    private BufferedWriter bw;
    public void client_connect(int port) throws Exception
    {
        ss=new ServerSocket(port);
        while(true)
            {
                Socket client_socket=ss.accept();//returns a socket  object for communication for a specific client
                System.out.println("client connected..");
                Thread obj =new Thread(new Clienthandler(client_socket));
                obj.start();
            }
        }
        // public void broadcast_msg() throws IOException
        // {
            //     while(true){
                //     br=new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
    //     String s;
    //     s=br.readLine();
    //     System.out.println(s);
    //     // OutputStream os = client_socket.getOutputStream();
    //     // PrintWriter out = new PrintWriter(new OutputStreamWriter(os), true); // 'true' enables auto-flush
    //     // out.println(s);
    //     // bw=new BufferedWriter(new OutputStreamWriter(client_socket.getOutputStream()));
    //     // bw.write(s);
    //     // bw.newLine();        
    //     // bw.flush();
    //     }
    
    public static void main(String[]args)
    {  
        Server s=new Server();
        Thread o =new Thread(() -> {try{s.client_connect(5000);} catch(Exception e){System.out.println("error!!!");}});//creates a new thread for allowing multiple clients tom connect 
        o.start();    
    }
}
class Clienthandler implements Runnable{
    private BufferedWriter bw;
    public static Map<Socket,BufferedWriter> client_map=new HashMap<>();
    private Socket soc;
    Clienthandler(Socket sc)
    {
        this.soc=sc;
        try
        {
        this.bw=new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
        client_map.put(sc,bw);
        }
        catch(Exception e){
            System.out.println("cannot create client maps");
        }
    }
    public void run()
    {
        try{
        BufferedReader br=new BufferedReader(new InputStreamReader(this.soc.getInputStream()));//gets in the messages from the paricular object.
        while(true)
            {
                String a=br.readLine(); 
                if (a!=null){
                broadcast(a,soc);
                } 
            }
        }
        catch(Exception e)
        {
            System.out.println("Client disconnected...");
            client_map.remove(soc);
        }
    }
    void broadcast(String message, Socket soc)throws IOException
    {
        for(Map.Entry<Socket,BufferedWriter> client_mapEntry: client_map.entrySet()){
            BufferedWriter value=client_mapEntry.getValue();
            Socket key=client_mapEntry.getKey();
            if(key!=soc){
            value.write(message);
            value.newLine();
            value.flush();}
        }
    }
}