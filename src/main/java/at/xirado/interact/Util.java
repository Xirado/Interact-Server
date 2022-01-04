package at.xirado.interact;

import java.math.BigInteger;

public class Util
{
    public static byte[] hexToBytes(String hexString)
    {
        return new BigInteger(hexString, 16).toByteArray();
    }
}
