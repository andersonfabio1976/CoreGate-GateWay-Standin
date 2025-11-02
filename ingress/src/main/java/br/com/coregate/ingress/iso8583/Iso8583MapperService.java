//package br.com.coregate.ingress.iso8583;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * Serviço de alto nível para uso dentro da SAGA e do Ingress.
// */
//@Slf4j
//public class Iso8583MapperService {
//
//    public Object parse(String isoRaw) {
//        log.debug("🔍 Decodificando ISO8583 via Iso8583Registry...");
//        return Iso8583Registry.decode(isoRaw);
//    }
//
//    public String build(Object isoModel) {
//        log.debug("🧩 Codificando modelo ISO8583 via Iso8583Registry...");
//        return Iso8583Registry.encode(isoModel);
//    }
//}
