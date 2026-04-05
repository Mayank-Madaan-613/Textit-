package src.Server;
import java.io.*;
import java.net.*;
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
        Socket client_socket=ss.accept();//returns an object for communication
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
        Thread o =new Thread(() -> {try{s.client_connect(5000);} catch(Exception e){System.out.println("error!!!");}});
        o.start();    
    }
}
class Clienthandler implements Runnable{
    private Socket soc;
    Clienthandler(Socket sc)
    {
        this.soc=sc;
    }
    public void run()
    {
        try{
        BufferedReader br=new BufferedReader(new InputStreamReader(this.soc.getInputStream()));
        String a="";
        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(this.soc.getOutputStream()));
        while(a!=null)
        {
        a=br.readLine();
        bw.write(a);
        bw.newLine();
        bw.flush();
        }
        }
        catch(Exception e)
        {
            System.out.println("Client disconnected...");
        }
    }
}