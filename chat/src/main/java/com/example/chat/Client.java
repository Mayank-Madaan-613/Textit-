package com.example.chat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
public class Client
{
    
    private Socket client_socket;
    // private BufferedReader br;
    // private BufferedWriter bw;
    public void server_connect(int port, String ip) throws IOException
    {
        client_socket=new Socket(ip,port);// used for connecting to server
        System.out.println("Server connected..."); 
        Thread sender=new Thread( new Message_sender(client_socket));
        sender.start();
        Thread reciever=new Thread(new Message_reciever(client_socket));
        reciever.start();
    }
    // public void send_msg() throws IOException
    // {
    //     br=new BufferedReader(new InputStreamReader(System.in));//takes in the message to be sent--obj created
    //     bw= new BufferedWriter(new OutputStreamWriter(client_socket.getOutputStream())); //sends messages to server--obj connected
    //     System.out.println("enter your message: ");
    //     String msg=br.readLine();
    //     bw.write(msg);
    //     bw.newLine();//adds newline so that while reading it reads till the newline
    //     bw.flush();//ensures all buffered data is pushed to output stream 
        
    // }
    // public void recieve_message() throws IOException
    // {
    //     br=new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
    //     String input_message=br.readLine();
    //     System.out.println(input_message);
    // }
    // public void close_resource() throws IOException{
    //     client_socket.close();
    //     br.close();
    //     bw.close();
    // }
    public static void main(String[] args)
    {
        Client obj_main=new Client();
        try
        {
        obj_main.server_connect(5000,"localhost");
        }
        catch (Exception e)
        {
            System.out.println("Server offline");
            System.exit(1);
        }
    }
}
class Message_sender implements Runnable
{
    private Socket client_Socket;
    Message_sender(Socket Client)
    {
            this.client_Socket=Client;
    }
    public void run() 
    {
        try
        {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(this.client_Socket.getOutputStream()));
        while(true){
        System.out.println("Enter your message.....");
        String msg=br.readLine();
        bw.write(msg);
        bw.newLine();
        bw.flush();}
        }

        catch(IOException e)
        {
            System.out.println("ERROR!!!");
        }
    }
}
class Message_reciever implements Runnable{
    private Socket client_socket;
    Message_reciever(Socket Client)
    {
        this.client_socket=Client;
    }
    public void run(){
        try 
        {
            BufferedReader br=new BufferedReader(new InputStreamReader(this.client_socket.getInputStream()));
            while(true)
            {
                String recieved_msg=br.readLine();
                if(recieved_msg!=null)
                {
                    System.out.println(recieved_msg);
                }
            }
        } catch (IOException e) 
        {
            System.out.println("Error!!");
        }
    }
}


