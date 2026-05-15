package es.pcomida.proyectocomida.metodopago.services;

import es.pcomida.proyectocomida.metodopago.dto.MetodoPagoCreateDto;
import es.pcomida.proyectocomida.metodopago.dto.MetodoPagoDto;
import es.pcomida.proyectocomida.metodopago.dto.MetodoPagoUpdateDto;
import es.pcomida.proyectocomida.metodopago.exceptions.InvalidNumeroTarjetaException;
import es.pcomida.proyectocomida.metodopago.exceptions.MetodoPagoNotFoundException;
import es.pcomida.proyectocomida.metodopago.mapper.MetodoPagoMapper;
import es.pcomida.proyectocomida.metodopago.models.MetodoPago;
import es.pcomida.proyectocomida.metodopago.repositories.MetodoPagoRepository;
import es.pcomida.proyectocomida.usuario.models.Usuario;
import es.pcomida.proyectocomida.usuario.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetodoPagoServiceImpl implements MetodoPagoService {

    private final MetodoPagoRepository metodoPagoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MetodoPagoMapper metodoPagoMapper;

    @Override
    public List<MetodoPagoDto> getMetodosPago(Usuario usuario) {
        return metodoPagoRepository.findByUsuarioIdAndIsDeletedFalse(usuario.getId()).stream()
                .map(metodoPagoMapper::toMetodoPagoDto)
                .collect(Collectors.toList());
    }

    @Override
    public MetodoPagoDto getMetodoPagoById(Long id, Usuario usuario) {
        MetodoPago metodoPago = metodoPagoRepository.findByIdAndUsuarioIdAndIsDeletedFalse(id, usuario.getId())
                .orElseThrow(() -> new MetodoPagoNotFoundException(id));
        return metodoPagoMapper.toMetodoPagoDto(metodoPago);
    }

    @Override
    @Transactional
    public MetodoPagoDto createMetodoPago(MetodoPagoCreateDto metodoPagoDto, Usuario usuario) {
        String numeroTarjeta = metodoPagoDto.getNumeroTarjeta();
        validateAndMaskNumeroTarjeta(numeroTarjeta);
        metodoPagoDto.setNumeroTarjeta(maskNumeroTarjeta(numeroTarjeta));

        if (Boolean.TRUE.equals(metodoPagoDto.getIsDefault())) {
            metodoPagoRepository.unsetAllDefaultMetodosPagoForUser(usuario.getId());
        }

        MetodoPago nuevoMetodoPago = metodoPagoMapper.toMetodoPago(metodoPagoDto);
        nuevoMetodoPago.setUsuario(usuario);

        MetodoPago savedMetodoPago = metodoPagoRepository.save(nuevoMetodoPago);
        if (Boolean.TRUE.equals(savedMetodoPago.getIsDefault())) {
            usuario.setMetodoPagoPorDefecto(savedMetodoPago);
            usuarioRepository.save(usuario);
        }

        return metodoPagoMapper.toMetodoPagoDto(savedMetodoPago);
    }

    @Override
    @Transactional
    public MetodoPagoDto updateMetodoPago(Long id, MetodoPagoUpdateDto metodoPagoUpdateDto, Usuario usuario) {
        MetodoPago metodoPago = metodoPagoRepository.findByIdAndUsuarioIdAndIsDeletedFalse(id, usuario.getId())
                .orElseThrow(() -> new MetodoPagoNotFoundException(id));

        if (metodoPagoUpdateDto.getNumeroTarjeta() != null) {
            String numeroTarjeta = metodoPagoUpdateDto.getNumeroTarjeta();
            validateAndMaskNumeroTarjeta(numeroTarjeta);
            metodoPagoUpdateDto.setNumeroTarjeta(maskNumeroTarjeta(numeroTarjeta));
        }

        if (Boolean.TRUE.equals(metodoPagoUpdateDto.getIsDefault())) {
            metodoPagoRepository.unsetAllDefaultMetodosPagoForUser(usuario.getId());
        }

        MetodoPago updatedMetodoPago = metodoPagoMapper.toMetodoPago(metodoPagoUpdateDto, metodoPago);
        MetodoPago savedMetodoPago = metodoPagoRepository.save(updatedMetodoPago);

        if (Boolean.TRUE.equals(savedMetodoPago.getIsDefault())) {
            usuario.setMetodoPagoPorDefecto(savedMetodoPago);
        } else if (usuario.getMetodoPagoPorDefecto() != null && usuario.getMetodoPagoPorDefecto().getId().equals(id)) {
            usuario.setMetodoPagoPorDefecto(null);
        }
        usuarioRepository.save(usuario);

        return metodoPagoMapper.toMetodoPagoDto(savedMetodoPago);
    }

    @Override
    @Transactional
    public void deleteMetodoPago(Long id, Usuario usuario) {
        MetodoPago metodoPago = metodoPagoRepository.findByIdAndUsuarioIdAndIsDeletedFalse(id, usuario.getId())
                .orElseThrow(() -> new MetodoPagoNotFoundException(id));
        metodoPago.setIsDeleted(true);
        metodoPagoRepository.save(metodoPago);

        if (usuario.getMetodoPagoPorDefecto() != null && usuario.getMetodoPagoPorDefecto().getId().equals(id)) {
            usuario.setMetodoPagoPorDefecto(null);
            usuarioRepository.save(usuario);
        }
    }

    @Override
    @Transactional
    public void setDefaultMetodoPago(Long id, Usuario usuario) {
        MetodoPago metodoPago = metodoPagoRepository.findByIdAndUsuarioIdAndIsDeletedFalse(id, usuario.getId())
                .orElseThrow(() -> new MetodoPagoNotFoundException(id));

        metodoPagoRepository.unsetAllDefaultMetodosPagoForUser(usuario.getId());
        metodoPago.setIsDefault(true);
        usuario.setMetodoPagoPorDefecto(metodoPago);

        metodoPagoRepository.save(metodoPago);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public MetodoPagoDto cobrar(Long id, BigDecimal monto, Usuario usuario) {
        MetodoPago metodoPago = metodoPagoRepository.findByIdAndUsuarioIdAndIsDeletedFalse(id, usuario.getId())
                .orElseThrow(() -> new MetodoPagoNotFoundException(id));

        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto debe ser mayor que 0");
        }

        if (metodoPago.getSaldoDisponible().compareTo(monto) < 0) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Saldo insuficiente");
        }

        metodoPago.setSaldoDisponible(metodoPago.getSaldoDisponible().subtract(monto));
        return metodoPagoMapper.toMetodoPagoDto(metodoPagoRepository.save(metodoPago));
    }

    private void validateAndMaskNumeroTarjeta(String numero) {
        if (numero == null || !numero.matches("^[0-9]{13,19}$")) {
            throw new InvalidNumeroTarjetaException("El número de tarjeta debe contener entre 13 y 19 dígitos.");
        }
    }

    private String maskNumeroTarjeta(String numero) {
        return "************" + numero.substring(numero.length() - 4);
    }
}
