package api.service;

import api.entity.LancamentoEntity;
import api.exception.ModelException;
import api.model.SaldoModel;
import api.model.errors.LancamentoErrors;
import api.repository.LancamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class SaldoService {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    public BigDecimal calcularSaldo(List<LancamentoEntity> lancamentos, String tipoLancamento) {
        return lancamentos.stream()
                .filter(lancamento -> lancamento.getTipoLancamento().getValue().equals(tipoLancamento))
                .map(LancamentoEntity::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public SaldoModel mostrarSaldo(LocalDate data) {
        BigDecimal saldoTotal;
        List<LancamentoEntity> lancamentos;

        if (data != null) {
            lancamentos = lancamentoRepository.findByDataLancamento(data);
        } else {
            lancamentos = lancamentoRepository.findAll();
        }

        if (lancamentos.isEmpty()) {
            throw new ModelException(LancamentoErrors.NOT_FOUND);
        }

        BigDecimal saldoCreditos = calcularSaldo(lancamentos, "CREDITO");
        BigDecimal saldoDebitos = calcularSaldo(lancamentos, "DEBITO");
        saldoTotal = saldoCreditos.subtract(saldoDebitos);

        SaldoModel saldo = new SaldoModel();
        saldo.setData(data);
        saldo.setSaldoCreditos(saldoCreditos);
        saldo.setSaldoDebitos(saldoDebitos);
        saldo.setSaldoTotal(saldoTotal);

        return saldo;
    }

}
