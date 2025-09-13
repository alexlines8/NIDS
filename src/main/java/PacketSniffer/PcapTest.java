package PacketSniffer;

import org.pcap4j.core.Pcaps;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.packet.Packet;

import java.util.List;

public class PcapTest {

    public static void main(String[] args) throws Exception {
        // List all network interfaces
        List<PcapNetworkInterface> allDevs = Pcaps.findAllDevs();
        if (allDevs == null || allDevs.isEmpty()) {
            System.out.println("No devices found.");
            return;
        }

        System.out.println("Network interfaces found:");
        for (int i = 0; i < allDevs.size(); i++) {
            PcapNetworkInterface dev = allDevs.get(i);
            System.out.println("[" + i + "] " + dev.getName() + " : " + dev.getDescription());
        }

        // Pick the first device just for testing
        PcapNetworkInterface nif = allDevs.get(0);
        System.out.println("Using device: " + nif.getName());

        // Open a handle to capture one packet
        PcapHandle handle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 1000);
        Packet packet = handle.getNextPacketEx();
        System.out.println("Captured packet:");
        System.out.println(packet);

        handle.close();
    }
}
