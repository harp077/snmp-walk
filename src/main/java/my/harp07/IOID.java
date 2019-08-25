
package my.harp07;

public interface IOID {
    
    ///  LOW-SPEED
    final String LS_IF_BASE = ".1.3.6.1.2.1.2.2.1";
    final String LS_IF_INDEX = ".1";
    final String LS_IF_DESC = ".2";
    final String LS_IF_TYPE = ".3";
    final String LS_IF_MTU = ".4";
    final String LS_IF_SPEED = ".5";
    final String LS_IF_MAC = ".6";
    //final String LS_IF_ADMIN_STATUS = ".7";
    final String LS_IF_OPER_STATUS = ".8";
    final String LS_IF_LAST_CHANGE = ".9";
    final String LS_IF_INPUT = ".10";
    final String LS_IF_OUTPUT = ".16"; 
    final String LS_IF_DISCARDS_INP = ".13";
    final String LS_IF_DISCARDS_OUT = ".19";     
    final String LS_IF_ERRORS_INP = ".14";
    final String LS_IF_ERRORS_OUT = ".20"; 
    
    ///  HIGH-SPEED
    final String HS_IF_BASE = ".1.3.6.1.2.1.31.1.1.1";
    final String HS_IF_NAME = ".1";
    //
    final String HS_IF_INPUT = ".6";
    final String HS_IF_INPUT_UCAST = ".7";
    final String HS_IF_INPUT_MCAST = ".8";
    final String HS_IF_INPUT_BCAST = ".9";
    //
    final String HS_IF_OUTPUT = ".10";
    final String HS_IF_OUTPUT_UCAST = ".11";
    final String HS_IF_OUTPUT_MCAST = ".12";
    final String HS_IF_OUTPUT_BCAST = ".13";
    //    
    final String HS_IF_SPEED = ".15";
    final String HS_IF_CONNECTOR_PRESENT = ".17";
    final String HS_IF_ALIAS = ".18"; // port description
    
    /// SYS
    final String SYS_BASE = ".1.3.6.1.2.1.1";
    final String SYS_DESCR = ".1.0";
    final String SYS_UPTIME = ".3.0";
    final String SYS_NAME = ".5.0";  
    
    // IP-MIB
    final String IP_MIB_BASE = ".1.3.6.1.2.1.4.22.1";
    final String IP_MIB_BASE_MAC = ".2";
    final String IP_MIB_BASE_IP  = ".3";
    
    // GENERIC ERRORS
    final String  TCP_INP_ERRORS = ".1.3.6.1.2.1.6.14";
    final String  UDP_INP_ERRORS = ".1.3.6.1.2.1.7.3";
    final String ICMP_INP_ERRORS = ".1.3.6.1.2.1.5.2";
    final String ICMP_OUT_ERRORS = ".1.3.6.1.2.1.5.15";
    ///   
    //final String IF_MIB_OID = LS_IF_BASE + LS_IF_DESC; // ifDescr low-speed
    final String IF_MIB_OID = HS_IF_BASE + HS_IF_NAME;  // ifName  high-speed 
    
    //
    // snmpwalk -c public -v 2c 127.0.0.1 if
    // snmpwalk -c public -v 2c 127.0.0.1 ifX
    // snmpwalk -c public -v 2c 127.0.0.1 system
    // snmpwalk -c public -v 2c 127.0.0.1 ip
    // snmpwalk -c public -v 2c 127.0.0.1 udp
    // snmpwalk -c public -v 2c 127.0.0.1 tcp
    // snmpwalk -c public -v 2c 127.0.0.1 icmp
    // snmpwalk -c public -v 2c 127.0.0.1 snmp
    // snmpwalk -c public -v 2c 127.0.0.1 snmpv2
    // snmpwalk -c public -v 2c 127.0.0.1 dot = snmpwalk -c coldrom -v 2c 127.0.0.1 transmission
    // snmpwalk -c public -v 2c 127.0.0.1 smi
    // snmpwalk -c public -v 2c 127.0.0.1 host
    // snmpwalk -c coldrom -v 2c 127.0.0.1 ipForward
    // snmpwalk -c coldrom -v 2c 127.0.0.1 at (address translation)
    
}
