//package br.com.coregate.infrastructure.enums;
//
///**
// * Representa os eventos de transição entre modos operacionais.
// * Cada evento está associado a um novo modo destino.
// */
//public enum ModeChangeEventType {
//
//    RESTORE_MODE_GATEWAY_REQUEST_ON(OperationalMode.GATEWAY),
//    RESTORE_MODE_GATEWAY_AUTOMATIC(OperationalMode.GATEWAY),
//    CHANGE_MODE_STANDIN_REQUEST_ON(OperationalMode.STANDIN_REQUESTED),
//    CHANGE_MODE_STANDIN_AUTOMATIC(OperationalMode.STANDIN_AUTOMATIC);
//
//    private final OperationalMode targetMode;
//
//    ModeChangeEventType(OperationalMode targetMode) {
//        this.targetMode = targetMode;
//    }
//
//    public OperationalMode getTargetMode() {
//        return targetMode;
//    }
//
//    public static ModeChangeEventType from(String value) {
//        try {
//            return ModeChangeEventType.valueOf(value.toUpperCase());
//        } catch (Exception e) {
//            return null;
//        }
//    }
//}
