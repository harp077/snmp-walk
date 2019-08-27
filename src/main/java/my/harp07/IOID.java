
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
    final static String ip_mask = ".20.1.3"; 
    
    // udp
    final static String UDP_BASE ="1.3.6.1.2.1.7";
    final static String UDP_LOCAL_ADDR =".5.1.1";
    final static String UDP_LOCAL_PORT =".5.1.2";
    final static String UDP_ERRORS =".3";
    
    // ENTERPRISES MIBS:  1.3.6.1.4.1.*
    /*
          9   ciscoSystems
         11   Hewlett-Packard
         23   Novell
         35   Nortel Networks
         43   3Com
        318   APC
       2011   HUAWEI Technology Co.,Ltd 
       2636   Juniper Networks, Inc.    
    */
    
}
