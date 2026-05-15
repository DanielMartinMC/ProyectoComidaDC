package es.pcomida.proyectocomida.metodopago.mapper;

import es.pcomida.proyectocomida.metodopago.dto.MetodoPagoCreateDto;
import es.pcomida.proyectocomida.metodopago.dto.MetodoPagoDto;
import es.pcomida.proyectocomida.metodopago.dto.MetodoPagoUpdateDto;
import es.pcomida.proyectocomida.metodopago.models.MetodoPago;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MetodoPagoMapper {

    public MetodoPago toMetodoPago(MetodoPagoCreateDto dto) {
        return MetodoPago.builder()
                .tipo(dto.getTipo())
                .numeroTarjeta(dto.getNumeroTarjeta())
                .fechaExpiracion(dto.getFechaExpiracion())
                .isDefault(dto.getIsDefault())
                .saldoDisponible(new BigDecimal("100.00"))
                .build();
    }

    public MetodoPago toMetodoPago(MetodoPagoUpdateDto dto, MetodoPago original) {
        return MetodoPago.builder()
                .id(original.getId())
                .tipo(dto.getTipo() != null ? dto.getTipo() : original.getTipo())
                .numeroTarjeta(dto.getNumeroTarjeta() != null ? dto.getNumeroTarjeta() : original.getNumeroTarjeta())
                .fechaExpiracion(dto.getFechaExpiracion() != null ? dto.getFechaExpiracion() : original.getFechaExpiracion())
                .isDefault(dto.getIsDefault() != null ? dto.getIsDefault() : original.getIsDefault())
                .saldoDisponible(original.getSaldoDisponible())
                .usuario(original.getUsuario())
                .build();
    }

    public MetodoPagoDto toMetodoPagoDto(MetodoPago metodoPago) {
        return MetodoPagoDto.builder()
                .id(metodoPago.getId())
                .tipo(metodoPago.getTipo())
                .numeroTarjeta(metodoPago.getNumeroTarjeta()) // Idealmente, solo los últimos 4 dígitos
                .fechaExpiracion(metodoPago.getFechaExpiracion())
                .isDefault(metodoPago.getIsDefault())
                .saldoDisponible(metodoPago.getSaldoDisponible())
                .build();
    }
}
