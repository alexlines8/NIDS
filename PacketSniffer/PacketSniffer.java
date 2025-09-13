package PacketSniffer;
import java.util.Enumeration;
import java.net.NetworkInterface;
import java.net.SocketException;


public class PacketSniffer {
    
    public PacketSniffer() {
        System.out.println("PacketSniffer created");
    }
    
    public void listInterfaces() {
        try {
            System.out.println("Listing network interfaces:");
            
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            int count = 0;

            while (interfaces.hasMoreElements()) {
                NetworkInterface netInterface = interfaces.nextElement();

                if (!netInterface.isUp() || netInterface.isVirtual() || netInterface.isLoopback()) {
                    continue;
                }

                System.out.println("[" + count++ + "] " + netInterface.getName() + 
                                  " - " + netInterface.getDisplayName());
                
                // Print IP addresses associated with this interface
                netInterface.getInterfaceAddresses().forEach(address -> {
                    System.out.println("    Address: " + address.getAddress().getHostAddress());
                });
            }
            
            if (count == 0) {
                System.out.println("No active network interfaces found");
            }
            
        } catch (SocketException e) {
            System.err.println("Error retrieving network interfaces: " + e.getMessage());
        }
    }

    
}
