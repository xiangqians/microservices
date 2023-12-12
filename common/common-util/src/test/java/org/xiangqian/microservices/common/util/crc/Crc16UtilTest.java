package org.xiangqian.microservices.common.util.crc;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @author xiangqian
 * @date 19:21 2023/04/06
 */
public class Crc16UtilTest {

    @Test
    public void test() throws Exception {
        // ---------- 0xef 0x01 0x00 0x11 0x00 0x09 0x40 0xc4 0xe0 0x64 0x1d 0x5a 0x0f 0x21 0x78
        String hex = "0xef 0x01 0x00 0x11 0x00 0x09 0x40 0xc4 0xe0 0x64 0x1d 0x5a 0x0f";

        hex = hex.replace("0x", "").replace(" ", "");
        System.out.println(hex);

        int crc = Crc16Util.modbus(hex);
        System.out.println(crc);
        System.out.println(Integer.toHexString(crc));
        System.out.println(StringUtils.leftPad(Integer.toHexString(crc), 4, "0"));
        System.out.println(String.format("%04X", crc));
    }

}
