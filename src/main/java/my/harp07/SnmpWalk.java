package my.harp07;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
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

    static String IP = "10.73.2.28";
    static Map<String, String> walkMap;
    static Map<String, Integer> versionMap = new HashMap<>();
    static String snmp_comm = "look";
    static String snmp_vers = "2";//SnmpConstants.version2c;
    static String snmp_port = "161";

    // standart mibs = 
    // IP-MIB, 
    // IP-FORWARD-MIB, 
    // IF-MIB, 
    // RFC1213-MIB, 
    // TCP-MIB, 
    // UDP-MIB, 
    // EtherLike-MIB
    // SNMPv2-MIB,
    final static String base = "1.3.6.1.2.1.";
    final static String system_mib = "1";  // SNMPv2-MIB,
    final static String if_mib = "2";  // IF-MIB,
    final static String at_mib = "3";  // address translation
    final static String ip_mib = "4";  // IP-MIB, 
    final static String icmp_mib = "5";
    final static String tcp_mib = "6";  // TCP-MIB, 
    final static String udp_mib = "7";  // UDP-MIB,    
    final static String dot_mib = "10"; // = transmission = EtherLike-MIB
    final static String snmp_mib = "11"; // SNMPv2-MIB,
    final static String ifX_mib = "31";

    // ip-addresses of interfaces:
    final static String base_ipIF = base + ip_mib;
    final static String ip_index = ".20.1.2";
    final static String ip_masks = ".20.1.3";
    // udp
    final static String UDP_BASE ="1.3.6.1.2.1.7";
    final static String UDP_LOCAL_ADDR =".5.1.1";
    final static String UDP_LOCAL_PORT =".5.1.2";
    final static String UDP_ERRORS =".3";
    //
    final static String checkIf32 = "1.3.6.1.2.1.2.2.1.1";
    final static String checkIf64 = "1.3.6.1.2.1.31.1.1.1.1";

    static {
        versionMap.put("1", SnmpConstants.version1);
        versionMap.put("2", SnmpConstants.version2c);
        versionMap.put("3", SnmpConstants.version3);
    }

    public static void main(String[] args) throws Exception {
        walkMap = walkSNMP(IP, checkIf32, snmp_comm, snmp_port, snmp_vers); // ifTable, mib-2 interfaces
        System.out.println("32 bit empty = "+walkMap.isEmpty());
        walkMap.entrySet().forEach(x -> {
            System.out.println(x.getKey() + " = " + x.getValue());
        });
        walkMap = walkSNMP(IP, checkIf64, snmp_comm, snmp_port, snmp_vers); // ifTable, mib-2 interfaces
        System.out.println("64 bit empty = "+walkMap.isEmpty());
        walkMap.entrySet().forEach(x -> {
            System.out.println(x.getKey() + " = " + x.getValue());
        });        
        List<String> listIP_INDEX = walkSNMP(IP, base_ipIF + ip_index, snmp_comm, snmp_port, snmp_vers)
                .entrySet().stream().map(x -> StringUtils.substringAfter(x.getKey(), base_ipIF + ip_index + ".") + "=" + x.getValue()).collect(toList());
        List<String> listIP_MASKS = walkSNMP(IP, base_ipIF + ip_masks, snmp_comm, snmp_port, snmp_vers)
                .entrySet().stream().map(x -> StringUtils.substringAfter(x.getKey(), base_ipIF + ip_masks + ".") + "=" + x.getValue()).collect(toList());
        System.out.println("\n listIP_INDEX = " + listIP_INDEX);
        System.out.println("\n listIP_MASKS = " + listIP_MASKS);
        Map<String, String> mapIP_INDEX = walkSNMP(IP, base_ipIF + ip_index, snmp_comm, snmp_port, snmp_vers)
                .entrySet().stream().collect(Collectors.toMap(
                        x -> StringUtils.substringAfter(x.getKey(), base_ipIF + ip_index + "."),
                        x -> x.getValue()));
        Map<String, String> mapIP_MASKS = walkSNMP(IP, base_ipIF + ip_masks, snmp_comm, snmp_port, snmp_vers)
                .entrySet().stream().collect(Collectors.toMap(
                        x -> StringUtils.substringAfter(x.getKey(), base_ipIF + ip_masks + "."),
                        x -> x.getValue()));
        System.out.println("\n mapIP_INDEX = " + mapIP_INDEX);
        System.out.println("\n mapIP_MASKS = " + mapIP_MASKS);        
        //Stream combined = Stream.concat(mapIP_INDEX.entrySet().stream(), mapIP_MASKS.entrySet().stream());
        Map<String, String> result=new HashMap<>();
        /*for (Map.Entry<String, String> ipi : mapIP_INDEX.entrySet()) {
            for (Map.Entry<String, String> ipm : mapIP_MASKS.entrySet()) {
                if (ipi.getKey().equals(ipm.getKey())) {
                    result.put(ipi.getValue(), ipi.getKey() + " " + ipm.getValue());
                }
 
            }
        } */       
        mapIP_INDEX.entrySet().forEach((ipi) -> {
            mapIP_MASKS.entrySet().stream().filter((ipm) -> (ipi.getKey().equals(ipm.getKey()))).forEachOrdered((ipm) -> {
                result.put(ipi.getValue(), ipi.getKey() + " " + ipm.getValue());
            });
        });
        System.out.println("\n result = " + result);
        ////////////
        Map<String,String> mapUDP_ADDR = walkSNMP(IP, UDP_BASE + UDP_LOCAL_ADDR, snmp_comm, snmp_port, snmp_vers);
        Map<String,String> mapUDP_PORT = walkSNMP(IP, UDP_BASE + UDP_LOCAL_PORT, snmp_comm, snmp_port, snmp_vers);
        System.out.println("\n mapUDP_ADDR = " + mapUDP_ADDR);
        System.out.println("\n mapUDP_PORT = " + mapUDP_PORT+"\n"); 
        Set<String> udp_result=new HashSet<>();
        /*for (Map.Entry<String, String> ua : mapUDP_ADDR.entrySet()) {
            for (Map.Entry<String, String> up : mapUDP_PORT.entrySet()) {
                if (up.getKey().contains("."+ua.getValue()+".")) {
                    udp_result.add(ua.getValue()+":"+up.getValue());
                }
 
            }
        }*/         
        mapUDP_ADDR.entrySet().forEach((ua) -> {
            mapUDP_PORT.entrySet().stream().
                    filter((up) -> (up.getKey().contains("."+ua.getValue()+".")))
                    .forEachOrdered((up) -> {
                udp_result.add(ua.getValue()+":"+up.getValue());
            });
        });   
        udp_result.stream().sorted().forEach(x->System.out.println(x));
    }

    public static Map<String, String> walkSNMP(String ip, String oid, String comm, String port, String vers) throws IOException {
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(comm));
        target.setAddress(GenericAddress.parse("udp:" + ip + "/" + port)); // supply your own IP and snmp_port
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
