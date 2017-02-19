package org.bc.jcache.test.serial.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KodoCLI {

    private boolean stop;

    private KyroTransferClient kodoClient;
    
    @SuppressWarnings("resource")
    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("waiting for commands...");
        while (!stop) {
            String cmd = scanner.nextLine();
//            System.out.println(cmd);
            process(cmd);
        }
    }

    private void process(String cmd) {
    	if("".equals(cmd)){
    		return;
    	}

    	if ("quit".equals(cmd)) {
            System.out.println("byebye...");
            stop = true;
            kodoClient.stop();
            return;
        }
        List<String> token = toToken(cmd);
        if("put".equals(token.get(0))){
        	try{
        		kodoClient.put(token.get(1), token.get(2), 3600);
        	}catch(Exception ex){
        		System.out.println("put命令的参数不正确");
        	}
        	return;
        }
        if("get".equals(token.get(0))){
        	try{
        		kodoClient.get(token.get(1));
        	}catch(Exception ex){
        		System.out.println("put命令的参数不正确");
        	}
        	return;
        }
        if("remove".equals(token.get(0))){
        	try{
        		kodoClient.remove(token.get(1));
        	}catch(Exception ex){
        		System.out.println("put命令的参数不正确");
        	}
        	return;
        }
        System.out.println(cmd + " is not a valid command");
    }
    
    private List<String> toToken(String cmd) {
    	List<String> result = new ArrayList<String>();
    	String[] arr = cmd.split(" ");
    	for(int i=0;i<arr.length;i++){
    		if("".equals(arr[i])){
    			continue;
    		}
    		result.add(arr[i]);
    	}
		return result;
	}

	public static void main(String[] args) throws IOException{
        KodoCLI cli = new KodoCLI();
        cli.start();
    }

    public KyroTransferClient getKodoClient() {
        return kodoClient;
    }

    public void setKodoClient(KyroTransferClient kodoClient) {
        this.kodoClient = kodoClient;
    }
    
}
