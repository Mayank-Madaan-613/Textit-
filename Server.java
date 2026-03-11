import java.io.*;
import java.net.*;
public class Server
{   
    private Socket client_socket;
    private ServerSocket ss;
    private BufferedReader br;
    private BufferedWriter bw;
    public void client_connect(int port) throws IOException{
        
        ss=new ServerSocket(port);
        System.out.println("Server is on...");
        client_socket=ss.accept();
        System.out.println("Client connected...");
        
    }
    public void recieve_msg() throws IOException
    {
        br=new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
        String s;
        s=br.readLine();
        System.out.println(s);
    }
    public static void main(String[]args)
    {  
        boolean flag=true;
        Server s=new Server();
        while(flag){
        try
        {
        s.client_connect(5000);
        flag=false;
        }
        catch (Exception e)
        {
            System.out.println("some error occured...");
            
        }}
        try
        {
            s.recieve_msg();
        } 
        catch (Exception e) 
        {
            System.out.println("some error ocurred...");
        }

    }
}