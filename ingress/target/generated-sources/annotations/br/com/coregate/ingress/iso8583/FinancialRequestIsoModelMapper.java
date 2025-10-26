package br.com.coregate.ingress.iso8583;

import java.util.*;

public class FinancialRequestIsoModelMapper {

    public static FinancialRequestIsoModel decode(String iso) {
        FinancialRequestIsoModel model = new FinancialRequestIsoModel();
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
            if (i == 3) {
                String val = iso.substring(offset, offset + 6).trim();
                offset += 6;
                model.setProcessingCode(val);
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
            if (i == 11) {
                String val = iso.substring(offset, offset + 6).trim();
                offset += 6;
                model.setStan(val);
            }
            if (i == 12) {
                String val = iso.substring(offset, offset + 6).trim();
                offset += 6;
                model.setLocalTime(val);
            }
            if (i == 13) {
                String val = iso.substring(offset, offset + 4).trim();
                offset += 4;
                model.setLocalDate(val);
            }
            if (i == 32) {
                int len = Integer.parseInt(iso.substring(offset, offset + 2));
                offset += 2;
                String val = iso.substring(offset, offset + len);
                offset += len;
                model.setAcquiringId(val);
            }
            if (i == 37) {
                String val = iso.substring(offset, offset + 12).trim();
                offset += 12;
                model.setRetrievalReferenceNumber(val);
            }
            if (i == 41) {
                String val = iso.substring(offset, offset + 8).trim();
                offset += 8;
                model.setTerminalId(val);
            }
            if (i == 42) {
                String val = iso.substring(offset, offset + 15).trim();
                offset += 15;
                model.setMerchantId(val);
            }
            if (i == 49) {
                String val = iso.substring(offset, offset + 3).trim();
                offset += 3;
                model.setCurrencyCode(val);
            }
        }
        return model;
    }

    public static String encode(FinancialRequestIsoModel model) {
        BitSet bitmap = new BitSet(64);
        bitmap.set(1);
        bitmap.set(2);
        bitmap.set(3);
        bitmap.set(6);
        bitmap.set(10);
        bitmap.set(11);
        bitmap.set(12);
        bitmap.set(31);
        bitmap.set(36);
        bitmap.set(40);
        bitmap.set(41);
        bitmap.set(48);
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
            sb.append(padRight(model.getProcessingCode(), 6));
        }
        {
            sb.append(padRight(model.getAmount(), 12));
        }
        {
            sb.append(padRight(model.getTransmissionDateTime(), 10));
        }
        {
            sb.append(padRight(model.getStan(), 6));
        }
        {
            sb.append(padRight(model.getLocalTime(), 6));
        }
        {
            sb.append(padRight(model.getLocalDate(), 4));
        }
        {
            String val = model.getAcquiringId();
            if (val == null) val = "";
            sb.append(String.format("%02d", val.length()));
            sb.append(val);
        }
        {
            sb.append(padRight(model.getRetrievalReferenceNumber(), 12));
        }
        {
            sb.append(padRight(model.getTerminalId(), 8));
        }
        {
            sb.append(padRight(model.getMerchantId(), 15));
        }
        {
            sb.append(padRight(model.getCurrencyCode(), 3));
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
