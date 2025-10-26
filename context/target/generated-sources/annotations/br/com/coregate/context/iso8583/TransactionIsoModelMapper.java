package br.com.coregate.context.iso8583;

import java.util.*;

public class TransactionIsoModelMapper {

    public static TransactionIsoModel decode(String iso) {
        TransactionIsoModel model = new TransactionIsoModel();
        int offset = 0;
        String mti = iso.substring(offset, offset + 4);
        offset += 4;
        String bitmapHex = iso.substring(offset, offset + 16);
        offset += 16;
        BitSet bitmap = hexToBitSet(bitmapHex);
        for (int i = 1; i <= 64; i++) {
            if (!bitmap.get(i - 1)) continue;
            if (i == 2) {
                int len = Integer.parseInt(iso.substring(offset, offset + 2));
                offset += 2;
                String val = iso.substring(offset, offset + len);
                offset += len;
                model.setCardNumber(val);
            }
            if (i == 4) {
                String val = iso.substring(offset, offset + 12).trim();
                offset += 12;
                model.setAmount(val);
            }
            if (i == 7) {
                String val = iso.substring(offset, offset + 10).trim();
                offset += 10;
                model.setTransmissionDateTime(val);
            }
        }
        return model;
    }

    public static String encode(TransactionIsoModel model) {
        BitSet bitmap = new BitSet(64);
        bitmap.set(1);
        bitmap.set(3);
        bitmap.set(6);
        StringBuilder sb = new StringBuilder();
        sb.append("0200");
        sb.append(bitSetToHex(bitmap));
        {
            String val = model.getCardNumber();
            if (val == null) val = "";
            sb.append(String.format("%02d", val.length()));
            sb.append(val);
        }
        {
            sb.append(padRight(model.getAmount(), 12));
        }
        {
            sb.append(padRight(model.getTransmissionDateTime(), 10));
        }
        return sb.toString();
    }

    private static String padRight(String s, int n) {
        if (s == null) s = "";
        return String.format("%-" + n + "s", s);
    }

    private static String bitSetToHex(BitSet bitSet) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 64; i++) if (bitSet.get(i)) bytes[i / 8] |= 1 << (7 - (i % 8));
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02X", b));
        return sb.toString();
    }

    private static BitSet hexToBitSet(String hex) {
        BitSet bitSet = new BitSet(64);
        for (int i = 0; i < 16; i++) {
            int nibble = Character.digit(hex.charAt(i), 16);
            for (int j = 0; j < 4; j++) if ((nibble & (1 << (3 - j))) != 0) bitSet.set(i * 4 + j);
        }
        return bitSet;
    }
}
