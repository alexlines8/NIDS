package PacketSniffer;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapDumper;
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

    public void capturePackets(int packetCount) {
        
        if (selectedInterface == null) {
            System.out.println("No interface selected.");
            return;
        }
        
        try {
            PcapHandle handle = selectedInterface.openLive(
                65536,
                PcapNetworkInterface.PromiscuousMode.PROMISCUOUS,
                10
            );

            PcapDumper dumper = handle.dumpOpen("dump.pcap");
            PacketListener listener = new PacketListener() {
                @Override
                public void gotPacket(Packet packet) {
                    System.out.println("Packet Captured");

                    try {
                        System.out.println("Timestamp: " + handle.getTimestamp());
                    } catch (Exception e) {

                    }

                    System.out.println("Packet length: " + packet.length() + " bytes");
                    System.out.println();

                    try {
                        dumper.dump(packet, handle.getTimestamp());
                    } catch (org.pcap4j.core.NotOpenException e) {
                        System.err.println("Error dumping packet: " + e.getMessage());
                    }
                }
            };

            System.out.println("Starting packet capture...");
            handle.loop(packetCount, listener);

            handle.close();
            System.out.println("Packet capture finished.");

        } catch (Exception e) {
            System.err.println("Error during packet capture: " + e.getMessage());
        }
    }
}