package my.harp07;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
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
    
    static String IP="127.0.0.1";
    static Map<String, String> walkMap;
    static Map<String, Integer> versionMap=new HashMap<>();
    static String snmp_comm="coldrom";
    static String snmp_vers="2";//SnmpConstants.version2c;
    static String snmp_port="161";
    // standart mibs = IP, IP-FORWARD, IF, RFC1213, TCP, UDP
    final static String       base = "1.3.6.1.2.1.";
    final static String system_mib = "1";
    final static String     if_mib = "2";
    final static String     at_mib = "3";
    final static String     ip_mib = "4";
    final static String   icmp_mib = "5"; 
    final static String    tcp_mib = "6";
    final static String    udp_mib = "7";    
    final static String    dot_mib = "10"; // = transmission
    final static String   snmp_mib = "11";
    final static String    ifX_mib = "31";    
    
    static {
       versionMap.put("1", SnmpConstants.version1);
       versionMap.put("2", SnmpConstants.version2c);
       versionMap.put("3", SnmpConstants.version3);
    }
    

    public static void main(String[] args) throws Exception {
        walkMap = walkSNMP(IP, base + dot_mib, snmp_comm, snmp_port, snmp_vers); // ifTable, mib-2 interfaces
        walkMap.entrySet().forEach( x->{
           System.out.println(x.getKey() + " = " + x.getValue()); 
        });
        //System.out.println("\n Walk Map = "+walkMap);
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
