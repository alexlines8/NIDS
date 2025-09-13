package PacketSniffer;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.IpV6Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;
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

            PacketListener listener = new PacketListener() {
                @Override
                public void gotPacket(Packet packet) {
                    System.out.println("Packet Captured");

                    try {
                        System.out.println("Timestamp: " + handle.getTimestamp());
                    } catch (Exception e) {

                    }

                    // Ethernet
                EthernetPacket eth = packet.get(EthernetPacket.class);
                if (eth != null) {
                    System.out.println("Ethernet: " +
                        eth.getHeader().getSrcAddr() + " -> " +
                        eth.getHeader().getDstAddr());
                }

                // IPv4
                IpV4Packet ipv4 = packet.get(IpV4Packet.class);
                if (ipv4 != null) {
                    System.out.println("IPv4: " +
                        ipv4.getHeader().getSrcAddr() + " -> " +
                        ipv4.getHeader().getDstAddr());
                    System.out.println("Protocol: " + ipv4.getHeader().getProtocol());
                }

                // IPv6
                IpV6Packet ipv6 = packet.get(IpV6Packet.class);
                if (ipv6 != null) {
                    System.out.println("IPv6: " +
                        ipv6.getHeader().getSrcAddr() + " -> " +
                        ipv6.getHeader().getDstAddr());
                    System.out.println("Protocol: " + ipv6.getHeader().getNextHeader());
                }

                // TCP
                TcpPacket tcp = packet.get(TcpPacket.class);
                if (tcp != null) {
                    System.out.println("TCP: " +
                        tcp.getHeader().getSrcPort() + " -> " +
                        tcp.getHeader().getDstPort());
                }

                // UDP
                UdpPacket udp = packet.get(UdpPacket.class);
                if (udp != null) {
                    System.out.println("UDP: " +
                        udp.getHeader().getSrcPort() + " -> " +
                        udp.getHeader().getDstPort());
                }

                System.out.println("Packet length: " + packet.length() + " bytes");
                System.out.println();


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