package org.bc.jcache.test.serial.client;

import java.io.IOException;
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
            System.out.println(cmd);
            process(cmd);
        }
    }

    private void process(String cmd) {
        if ("quit".equals(cmd)) {
            System.out.println("byebye...");
            stop = true;
            kodoClient.stop();
            return;
        }
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
