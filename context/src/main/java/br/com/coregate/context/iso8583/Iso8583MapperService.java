package br.com.coregate.context.iso8583;

public class Iso8583MapperService {

    public TransactionIsoModel parse(String iso) {
        return TransactionIsoModelMapper.decode(iso);
    }

    public String build(TransactionIsoModel tx) {
        return TransactionIsoModelMapper.encode(tx);
    }

}
