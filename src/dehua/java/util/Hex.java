package dehua.java.util;

public class Hex {
    /**
     * reference apache commons <a
     * href="http://commons.apache.org/codec/">http://commons.apache.org/codec/</a>
     *
     * @author Aub
     *
     */
    /**
     * ���ڽ���ʮ������ַ�������Сд�ַ�����
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * ���ڽ���ʮ������ַ������Ĵ�д�ַ�����
     */
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * ���ֽ�����ת��Ϊʮ������ַ�����
     *
     * @param data byte[]
     * @return ʮ�����char[]
     */
    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * ���ֽ�����ת��Ϊʮ������ַ�����
     *
     * @param data        byte[]
     * @param toLowerCase <code>true</code> ������Сд��ʽ �� <code>false</code> �����ɴ�д��ʽ
     * @return ʮ�����char[]
     */
    public static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * ���ֽ�����ת��Ϊʮ������ַ�����
     *
     * @param data     byte[]
     * @param toDigits ���ڿ��������char[]
     * @return ʮ�����char[]
     */
    protected static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    /**
     * ���ֽ�����ת��Ϊʮ������ַ�
     *
     * @param data byte[]
     * @return ʮ�����String
     */
    public static String encodeHexStr(byte[] data) {
        return encodeHexStr(data, true);
    }

    /**
     * ���ֽ�����ת��Ϊʮ������ַ�
     *
     * @param data        byte[]
     * @param toLowerCase <code>true</code> ������Сд��ʽ �� <code>false</code> �����ɴ�д��ʽ
     * @return ʮ�����String
     */
    public static String encodeHexStr(byte[] data, boolean toLowerCase) {
        return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * ���ֽ�����ת��Ϊʮ������ַ�
     *
     * @param data     byte[]
     * @param toDigits ���ڿ��������char[]
     * @return ʮ�����String
     */
    protected static String encodeHexStr(byte[] data, char[] toDigits) {
        return new String(encodeHex(data, toDigits));
    }

    /**
     * ��ʮ������ַ�����ת��Ϊ�ֽ�����
     *
     * @param data ʮ�����char[]
     * @return byte[]
     * @throws RuntimeException ���Դʮ������ַ�������һ����ֵĳ��ȣ����׳�����ʱ�쳣
     */
    public static byte[] decodeHex(char[] data) {

        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * ��ʮ������ַ�ת����һ������
     *
     * @param ch    ʮ�����char
     * @param index ʮ������ַ����ַ������е�λ��
     * @return һ������
     * @throws RuntimeException ��ch����һ���Ϸ���ʮ������ַ�ʱ���׳�����ʱ�쳣
     */
    protected static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch
                    + " at index " + index);
        }
        return digit;
    }

    /**
     * ��16����ת��Ϊ������
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


    /**
     * ��������ת����16����
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

}
