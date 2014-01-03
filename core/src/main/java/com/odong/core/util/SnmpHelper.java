package com.odong.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by flamen on 14-1-3上午10:05.
 * pacman -S net-snmp
 * systemctl enable snmpd
 * <p>
 * mkdir /etc/snmp/
 * echo rouser read_only_user >> /etc/snmp/snmpd.conf
 * mkdir -p /var/net-snmp/
 * echo createUser read_only_user SHA password1 AES password2 > /var/net-snmp/snmpd.conf
 * <p>
 * snmpwalk -v 3 -u read_only_user -a SHA -A password1 -x DES -X password2 -l authNoPriv localhost | less
 * <p>
 */
@Component("core.snmpHelper")
public class SnmpHelper {
    public List<String> getV3(String host, int port, String username, String password, String... oids) {
        List<String> list = new ArrayList<>();
        Snmp snmp = null;
        try {
            Address targetAddress = GenericAddress.parse("udp:" + host + "/" + port);
            TransportMapping transport = new DefaultUdpTransportMapping();
            USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
            SecurityModels.getInstance().addSecurityModel(usm);
            snmp = new Snmp(transport);
            transport.listen();

            // add user to the USM
            snmp.getUSM().addUser(new OctetString("MD5DES"),
                    new UsmUser(new OctetString("MD5DES"),
                            AuthMD5.ID,
                            new OctetString(username),
                            PrivDES.ID,
                            new OctetString(password)));

            // create the target
            UserTarget target = new UserTarget();
            target.setAddress(targetAddress);
            target.setRetries(1);
            target.setTimeout(5000);
            target.setVersion(SnmpConstants.version3);
            target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
            target.setSecurityName(new OctetString("MD5DES"));

            // create the PDU
            PDU pdu = new ScopedPDU();
            for (String oid : oids) {
                pdu.add(new VariableBinding(new OID(oid)));
            }
        /*
        get操作用来提取特定的网络管理信息；
        get-next操作通过遍历活动来提供强大的管理信息提取能力；
        set操作用来对管理信息进行控制（修改、设置）；
        trap操作用来报告重要的事件
         */
            pdu.setType(PDU.GETNEXT);

            // send the PDU
            ResponseEvent response = snmp.send(pdu, target);
            if (response != null) {
                // extract the response PDU
                PDU responsePDU = response.getResponse();
                for (int i = 0; i < responsePDU.size(); i++) {
                    list.add(responsePDU.get(i).getVariable().toString());
                }
                // extract the address used by the agent to send the response:
                //Address peerAddress = response.getPeerAddress();


            } else {
                logger.error("未收到消息");
            }


        } catch (IOException e) {
            logger.error("抓取SNMP信息出错", e);
        } finally {

            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException e) {
                    logger.error("关闭SNMP Session出错", e);
                }
            }
        }
        return list;

    }

    @PostConstruct
    void init() {

    }


    private final static Logger logger = LoggerFactory.getLogger(SnmpHelper.class);

}
