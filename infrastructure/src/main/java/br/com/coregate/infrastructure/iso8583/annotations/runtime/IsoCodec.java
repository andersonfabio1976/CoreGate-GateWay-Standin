package br.com.coregate.infrastructure.iso8583.annotations.runtime;

import br.com.coregate.infrastructure.iso8583.annotations.iso.VarType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Codec simplificado: assume ISO em ASCII:
 *  - MTI (4)
 *  - Primary Bitmap (16 hex)  -> campos 1..64
 *  - Campos em sequência, na ordem do bitmap
 *    FIXED: tamanho fixo
 *    LLVAR/LLLVAR: prefixo 2 ou 3 dígitos de tamanho
 */
public final class IsoCodec {

    public static final int PRIMARY_BITMAP_BITS = 64;

    public static Map<Integer,String> decodeFields(String raw, Map<Integer, FieldSpec> specByField) {
        String mti = raw.substring(0, 4);
        String bitmapHex = raw.substring(4, 20); // 16 hex = 64 bits
        BitSet bs = hexToBitSet(bitmapHex);

        Map<Integer,String> out = new LinkedHashMap<>();
        int pos = 20;

        for (int f = 1; f <= PRIMARY_BITMAP_BITS; f++) {
            if (!bs.get(f-1)) continue;
            if (f == 1) continue; // (reservado para secondary bitmap)

            FieldSpec spec = specByField.get(f);
            if (spec == null) throw new IllegalStateException("Campo "+f+" presente no bitmap mas sem spec!");

            String val;
            if (spec.varType == VarType.FIXED) {
                val = raw.substring(pos, pos + spec.length);
                pos += spec.length;
            } else if (spec.varType == VarType.LLVAR) {
                int len = Integer.parseInt(raw.substring(pos, pos+2));
                pos += 2;
                val = raw.substring(pos, pos + len);
                pos += len;
            } else { // LLLVAR
                int len = Integer.parseInt(raw.substring(pos, pos+3));
                pos += 3;
                val = raw.substring(pos, pos + len);
                pos += len;
            }
            out.put(f, val);
        }
        // valida se consumiu exatamente
        if (pos != raw.length())
            throw new IllegalArgumentException("Sobrou/faltou conteúdo. pos="+pos+" total="+raw.length());
        return out;
    }

    public static String encodeFields(String mti, Map<Integer,String> fields, Map<Integer,FieldSpec> specByField) {
        // monta bitmap (1..64)
        BitSet bs = new BitSet(PRIMARY_BITMAP_BITS);
        fields.keySet().forEach(k -> { if (k>=1 && k<=64 && k!=1) bs.set(k-1); });

        String bitmapHex = bitSetToHex(bs);
        StringBuilder sb = new StringBuilder();
        sb.append(mti).append(bitmapHex);

        // campos na ordem
        List<Integer> ordered = fields.keySet().stream().sorted().collect(Collectors.toList());
        for (Integer f : ordered) {
            if (f == 1) continue;
            FieldSpec spec = specByField.get(f);
            if (spec == null) throw new IllegalStateException("Campo "+f+" sem spec!");

            String val = Objects.toString(fields.get(f), "");
            if (spec.varType == VarType.FIXED) {
                String padded = spec.padLeft
                        ? leftPad(val, spec.length, spec.padChar)
                        : rightPad(val, spec.length, spec.padChar);
                if (padded.length() != spec.length)
                    throw new IllegalArgumentException("Campo "+f+" excede FIXED("+spec.length+")");
                sb.append(padded);
            } else if (spec.varType == VarType.LLVAR) {
                if (val.length() > 99) throw new IllegalArgumentException("LLVAR > 99 para campo "+f);
                sb.append(String.format("%02d", val.length())).append(val);
            } else {
                if (val.length() > 999) throw new IllegalArgumentException("LLLVAR > 999 para campo "+f);
                sb.append(String.format("%03d", val.length())).append(val);
            }
        }
        return sb.toString();
    }

    // ===== helpers =====
    private static BitSet hexToBitSet(String hex) {
        BitSet bs = new BitSet(PRIMARY_BITMAP_BITS);
        for (int i = 0; i < hex.length(); i++) {
            int nibble = Integer.parseInt(hex.substring(i, i+1), 16);
            for (int b=0; b<4; b++) if (((nibble >> (3-b)) & 1) == 1) bs.set(i*4 + b);
        }
        return bs;
    }
    private static String bitSetToHex(BitSet bs) {
        StringBuilder hex = new StringBuilder(16);
        for (int i=0; i<16; i++) {
            int nibble = 0;
            for (int b=0; b<4; b++) {
                if (bs.get(i*4 + b)) nibble |= (1 << (3-b));
            }
            hex.append(Integer.toHexString(nibble).toUpperCase());
        }
        return hex.toString();
    }
    private static String leftPad(String s, int len, char ch) {
        if (s.length() >= len) return s.substring(0, len);
        return String.valueOf(ch).repeat(len - s.length()) + s;
    }
    private static String rightPad(String s, int len, char ch) {
        if (s.length() >= len) return s.substring(0, len);
        return s + String.valueOf(ch).repeat(len - s.length());
    }
}
