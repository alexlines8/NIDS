package PacketSniffer;
import java.util.Enumeration;
import java.net.NetworkInterface;
import java.net.SocketException;


public class PacketSniffer {
    
    private NetworkInterface selectedInterface;

    public PacketSniffer() {
        System.out.println("PacketSniffer created");
    }
    

    /**
     * Print all the Network Interfaces that are up
     */
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

    /**
     * Select the wifi interface and print it out
     * @return true if wifi interface is found
     */
    public boolean selectWiFiInterface() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()){
                NetworkInterface netInterface = interfaces.nextElement();

                if (netInterface.getName().equals("wireless_32768") && netInterface.getDisplayName().contains("MediaTek Wi-Fi")) {
                    if (!netInterface.getInterfaceAddresses().isEmpty()) {
                        selectedInterface = netInterface;
                        System.out.println("Selected WiFi Interface: " + netInterface.getDisplayName());

                        netInterface.getInterfaceAddresses().forEach(address -> {
                            if (address.getAddress().getHostAddress().contains(".")) {
                                System.out.println("IP: " + address.getAddress().getHostAddress());
                            }
                        });

                        return true;
                    }
                }
            }
            System.out.println("WiFi LAN Card not found or not active");
            return false;
        } catch (SocketException e) {
            System.err.println("Error Selecting WiFi Interface: " + e.getMessage());
            return false;
        }
    }
    
    public NetworkInterface getSelectedInterface() {
        return selectedInterface;
    }
}
