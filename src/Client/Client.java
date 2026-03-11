package src.Client;

import java.io.*;
import java.net.*;
public class Client
{
    private Socket client_socket;
    private BufferedReader br;
    private BufferedWriter bw;
    public void server_connect(int port, String ip) throws IOException
    {
        client_socket=new Socket(ip,port);
        System.out.println("Server connected...");
        
        
    }
    public void send_msg() throws IOException
    {
        br=new BufferedReader(new InputStreamReader(System.in));//takes in the message to be sent--obj created
        bw= new BufferedWriter(new OutputStreamWriter(client_socket.getOutputStream())); //sends messages to server--obj connected
        System.out.println("enter your message: ");
        String msg=br.readLine();
        bw.write(msg);
        bw.newLine();//adds newline so that while reading it reads till the newline
        bw.flush();//ensures all buffered data is pushed to output stream 
        
    }
    public void recieve_message() throws IOException
    {
        br=new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
        String input_message=br.readLine();
        System.out.println(input_message);
    }
    public void close_resource() throws IOException{
        client_socket.close();
        br.close();
        bw.close();
    }
    public static void main(String[] args)
    {
        Client obj_main=new Client();
        try
        {
        obj_main.server_connect(5000,"localhost");}
        catch (Exception e)
        {
            System.out.println("some error occurred");
            return;
        }
        // try
        // {
        //     obj_main.send_msg();
        // } 
        // catch (Exception e)
        // {
        //     System.out.println("error ouccured while sending message...kindly retry");
        //     return;
        // }
        try 
        {
            
            obj_main.recieve_message();
        } 
        catch (Exception e)
        {
            System.out.println("error occurred");
        }
        try
        {
        obj_main.close_resource();
        }
        catch(Exception e)
        {
            System.out.println("error!!");
        }
    }

}

