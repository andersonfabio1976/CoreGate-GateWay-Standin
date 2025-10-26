package br.com.coregate.application.port.out;

import br.com.coregate.domain.model.Advice;

public interface AdvicePublisherPort {
    void publishAdvice(Advice advice);
}