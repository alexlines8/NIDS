package PacketSniffer;

public class Main{
    public static void main(String[] args){
        System.out.println("Starting NIDS Data Collector...");

        PacketSniffer sniffer = new PacketSniffer();
        sniffer.listInterfaces();
        sniffer.selectWiFiInterface();
        System.out.println(sniffer.getSelectedInterface());

    }
}