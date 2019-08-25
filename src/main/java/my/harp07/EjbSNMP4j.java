package my.harp07;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

public class EjbSNMP4j {
    
    private final int Retries=1;
    private final int Timeout=255;
    private Map<String, Integer> versionMap = new HashMap<>();
    //private Snmp snmpGbufer = null;
    //private TransportMapping transportGbufer=null;
    private Snmp snmpW = null;
    private TransportMapping transportW=null; 

    @PostConstruct
    public void afterBirn() {
        versionMap.put("1", SnmpConstants.version1);
        versionMap.put("2", SnmpConstants.version2c);
        versionMap.put("3", SnmpConstants.version3);
    }


    public Map<String, String> snmpWalk(String ip, String oid, String comm, String port, String vers) throws IOException, NullPointerException {
        try {
            if(snmpW!=null) snmpW.close();
            if(transportW!=null)transportW.close();
        } catch (IOException ex) {
            Logger.getLogger(EjbSNMP4j.class.getName()).log(Level.SEVERE, null, ex);
        }        
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(comm));
        target.setAddress(GenericAddress.parse("udp:" + ip + "/" + port)); // supply your own IP and snmp_port
        target.setRetries(Retries);
        target.setTimeout(Timeout);
        target.setVersion(versionMap.get(vers));
        Map<String, String> result = new TreeMap<>();
        //TransportMapping<? extends Address> 
        transportW = new DefaultUdpTransportMapping();
        transportW.listen();        
        snmpW = new Snmp(transportW);
        TreeUtils treeUtils = new TreeUtils(snmpW, new DefaultPDUFactory());
        List<TreeEvent> events = treeUtils.getSubtree(target, new OID(oid));
        if (events == null || events.size() == 0) {
            System.out.println("@@@@@@@_____"+ip+" = Error: Unable to read table...");
            return result;
        }
        for (TreeEvent event : events) {
            if (event == null) {
                continue;
            }
            if (event.isError()) {
                System.out.println("@@@@@@@_____"+ip+" = Error: table OID [" + oid + "] " + event.getErrorMessage());
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
        //transportW.close();
        snmpW.close();
        transportW.close();
        return result;
    }

    public String snmpGet(String ip, String oid, String comm, String port, String vers) {
        String str = "";
        //Snmp snmp = null;
        /*try {
            if(snmpGbufer!=null) snmpGbufer.close();
            if(transportGbufer!=null)transportGbufer.close();
        } catch (IOException ex) {
            System.out.println("__snmpGet Close exception: " + ex.getMessage());
            //Logger.getLogger(EjbRrdSNMP.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        // обьявлять надо перед TRY - чтобы можно было их потом 
        // гарантированно убить в блоках CATCH + FINALLY !!!
        Snmp snmpG=null;
        TransportMapping transportG=null;
        try {
            CommunityTarget comtarget = new CommunityTarget();
            comtarget.setCommunity(new OctetString(comm));
            comtarget.setVersion(versionMap.get(vers));
            comtarget.setAddress(new UdpAddress(ip + "/" + port));
            comtarget.setRetries(Retries);
            comtarget.setTimeout(Timeout);
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(oid)));
            pdu.setType(PDU.GET);
            //TransportMapping 
            transportG = new DefaultUdpTransportMapping();
            transportG.listen();
            //transportGbufer=transportG;
            snmpG = new Snmp(transportG);
            //snmpGbufer=snmpG;
            ResponseEvent response = snmpG.get(pdu, comtarget);
            if (response != null) {
                if (response.getResponse().getErrorStatusText().equalsIgnoreCase("Success")) {
                    PDU pduresponse = response.getResponse();
                    str = pduresponse.getVariableBindings().firstElement().toString();
                    if (str.contains("=")) {
                        int len = str.indexOf("=");
                        str = str.substring(len + 1, str.length());
                    }
                }
            } else {
                System.out.println("@_"+ISDTF.sdtf.format(new Date())+"@@@@@@@_____"+ip+" = snmpGet TIMEOUT");
            }
            //transportG.close();
            snmpG.close();
            transportG.close();
        } catch (IOException | NullPointerException ex) {
            //snmpG=null;
            //transportG=null;
            System.out.println("@_"+ISDTF.sdtf.format(new Date())+"__snmpGet exception: " + ex.getMessage() + ", ip="+ip);
            return "";
        } 
        finally {
            try {
                if(snmpG!=null) snmpG.close();
                if(transportG!=null)transportG.close();
            } catch (IOException ex) {
                Logger.getLogger(EjbSNMP4j.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return str.trim();
    }    

}
