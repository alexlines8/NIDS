package PacketSniffer;

public class Main{
    public static void main(String[] args){
        System.out.println("Starting NIDS Data Collector...");

        PacketSniffer sniffer = new PacketSniffer();
        // sniffer.listInterfaces();
        boolean wifiInterfaceFound = sniffer.selectWiFiInterface();
        if (wifiInterfaceFound){
            System.out.println("WiFi interface selected successfully");
            sniffer.capturePackets(10);
        } else {
            System.err.println("Failed to select WiFI interface");
        }

    }
}