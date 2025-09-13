package PacketSniffer;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.core.PcapNativeException;
import java.util.List;

public class PacketSniffer {

    private PcapNetworkInterface selectedInterface;

    public void listInterfaces() {
        try {
            List<PcapNetworkInterface> interfaces = Pcaps.findAllDevs();
            if (interfaces == null || interfaces.isEmpty()) {
                System.out.println("No network interfaces found by Pcap4J.");
                return;
            }
            int i = 0;
            for (PcapNetworkInterface dev : interfaces) {
                System.out.println("[" + i++ + "] " + dev.getName() + " - " + dev.getDescription());
            }
        } catch (PcapNativeException e) {
            System.err.println("Error listing interfaces: " + e.getMessage());
        }
    }

    public boolean selectWiFiInterface() {
        try {
            List<PcapNetworkInterface> interfaces = Pcaps.findAllDevs();
            for (PcapNetworkInterface dev : interfaces) {
                if (dev.getDescription().toLowerCase().contains("wi-fi")) {
                    selectedInterface = dev;
                    System.out.println("Selected WiFi Interface: " + dev.getName() + " - " + dev.getDescription());
                    return true;
                }
            }
            System.out.println("WiFi interface not found.");
            return false;
        } catch (PcapNativeException e) {
            System.err.println("Error selecting WiFi interface: " + e.getMessage());
            return false;
        }
    }

    public PcapNetworkInterface getSelectedInterface() {
        return selectedInterface;
    }
}