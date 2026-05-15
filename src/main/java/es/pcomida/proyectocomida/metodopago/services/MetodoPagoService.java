package es.pcomida.proyectocomida.metodopago.services;

import es.pcomida.proyectocomida.metodopago.dto.MetodoPagoCreateDto;
import es.pcomida.proyectocomida.metodopago.dto.MetodoPagoDto;
import es.pcomida.proyectocomida.metodopago.dto.MetodoPagoUpdateDto;
import es.pcomida.proyectocomida.usuario.models.Usuario;

import java.math.BigDecimal;
import java.util.List;

public interface MetodoPagoService {
    List<MetodoPagoDto> getMetodosPago(Usuario usuario);
    MetodoPagoDto getMetodoPagoById(Long id, Usuario usuario);
    MetodoPagoDto createMetodoPago(MetodoPagoCreateDto metodoPagoDto, Usuario usuario);
    MetodoPagoDto updateMetodoPago(Long id, MetodoPagoUpdateDto metodoPagoUpdateDto, Usuario usuario);
    void deleteMetodoPago(Long id, Usuario usuario);
    void setDefaultMetodoPago(Long id, Usuario usuario);
    MetodoPagoDto cobrar(Long id, BigDecimal monto, Usuario usuario);
}
