package my.harp07;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import static java.util.stream.Collectors.toList;
import org.apache.commons.lang3.StringUtils;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

public class SnmpWalk {
    
    static String IP="10.73.2.22";
    static Map<String, String> walkMap;
    static Map<String, Integer> versionMap=new HashMap<>();
    static String snmp_comm="look";
    static String snmp_vers="2";//SnmpConstants.version2c;
    static String snmp_port="161";
    
    // standart mibs = 
    // IP-MIB, 
    // IP-FORWARD-MIB, 
    // IF-MIB, 
    // RFC1213-MIB, 
    // TCP-MIB, 
    // UDP-MIB, 
    // EtherLike-MIB
    // SNMPv2-MIB,
    final static String       base = "1.3.6.1.2.1.";
    final static String system_mib = "1";  // SNMPv2-MIB,
    final static String     if_mib = "2";  // IF-MIB,
    final static String     at_mib = "3";  // address translation
    final static String     ip_mib = "4";  // IP-MIB, 
    final static String   icmp_mib = "5"; 
    final static String    tcp_mib = "6";  // TCP-MIB, 
    final static String    udp_mib = "7";  // UDP-MIB,    
    final static String    dot_mib = "10"; // = transmission = EtherLike-MIB
    final static String   snmp_mib = "11"; // SNMPv2-MIB,
    final static String    ifX_mib = "31";
    
    // ip-addresses of interfaces:
    final static String base_ipIF = base + ip_mib;
    final static String ip_index = ".20.1.2";
    final static String ip_masks = ".20.1.3";
    
    static {
       versionMap.put("1", SnmpConstants.version1);
       versionMap.put("2", SnmpConstants.version2c);
       versionMap.put("3", SnmpConstants.version3);
    }
    

    public static void main(String[] args) throws Exception {
        walkMap = walkSNMP(IP, base + ip_mib + ".20.1.3", snmp_comm, snmp_port, snmp_vers); // ifTable, mib-2 interfaces
        walkMap.entrySet().forEach( x->{
           System.out.println(x.getKey() + " = " + x.getValue()); 
        });
        List<String> listIP_INDEX = walkSNMP(IP, base_ipIF+ip_index, snmp_comm, snmp_port, snmp_vers)
                .entrySet().stream().map(x->StringUtils.substringAfter(x.getKey(), base_ipIF+ip_index+".")+"="+x.getValue()).collect(toList());
        List<String> listIP_MASKS = walkSNMP(IP, base_ipIF+ip_masks, snmp_comm, snmp_port, snmp_vers)
                .entrySet().stream().map(x->StringUtils.substringAfter(x.getKey(), base_ipIF+ip_masks+".")+"="+x.getValue()).collect(toList());
        System.out.println("\n listIP_INDEX = "+listIP_INDEX);
        System.out.println("\n listIP_MASKS = "+listIP_MASKS);
    }

    public static Map<String, String> walkSNMP(String ip, String oid, String comm, String port, String vers) throws IOException {
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(comm));
        target.setAddress(GenericAddress.parse("udp:"+ip+"/"+port)); // supply your own IP and snmp_port
        target.setRetries(2);
        target.setTimeout(1000);
        target.setVersion(versionMap.get(vers));        
        Map<String, String> result = new TreeMap<>();
        TransportMapping<? extends Address> transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        transport.listen();
        TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
        List<TreeEvent> events = treeUtils.getSubtree(target, new OID(oid));
        if (events == null || events.size() == 0) {
            System.out.println("Error: Unable to read table...");
            return result;
        }
        for (TreeEvent event : events) {
            if (event == null) {
                continue;
            }
            if (event.isError()) {
                System.out.println("Error: table OID [" + oid + "] " + event.getErrorMessage());
                continue;
            }
            VariableBinding[] varBindings = event.getVariableBindings();
            if (varBindings == null || varBindings.length == 0) {
                continue;
            }
            for (VariableBinding varBinding : varBindings) {
                if (varBinding == null) {
                    continue;
                }
                result.put("." + varBinding.getOid().toString(), varBinding.getVariable().toString());
            }
        }
        snmp.close();
        return result;
    }

}
