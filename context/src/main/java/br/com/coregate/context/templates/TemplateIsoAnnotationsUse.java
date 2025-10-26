package br.com.coregate.context.templates;

import br.com.coregate.context.iso8583.TransactionIsoModel;
import br.com.coregate.context.iso8583.TransactionIsoModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TemplateIsoAnnotationsUse {

    public void execute() {
        // ===== TESTE ENCODE (obj → ISO) =====
        TransactionIsoModel tx = new TransactionIsoModel();
        tx.setCardNumber("453211******9999");   // f2 LLVAR
        tx.setAmount("000000010000");           // f4 FIXED(12)
        tx.setTransmissionDateTime("1022143030"); // f7 FIXED(10) MMddHHmmss

        String iso = TransactionIsoModelMapper.encode(tx);
        System.out.println("ISO ENCODED: " + iso);

        // ===== TESTE DECODE (ISO → obj) =====
        TransactionIsoModel back = TransactionIsoModelMapper.decode(iso);
        System.out.println("PAN: " + back.getCardNumber());
        System.out.println("AMT: " + back.getAmount());
        System.out.println("DT : " + back.getTransmissionDateTime());
    }

}
