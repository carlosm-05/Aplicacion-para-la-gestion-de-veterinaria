package com.example.Proyecto_Vet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Proyecto_Vet.model.DetalleVenta;
import com.example.Proyecto_Vet.model.Venta;
import com.example.Proyecto_Vet.repository.ClienteRepository;
import com.example.Proyecto_Vet.repository.EmpleadoRepository;
import com.example.Proyecto_Vet.repository.ProductoRepository;
import com.example.Proyecto_Vet.repository.VentaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepo;
    private final ClienteRepository clienteRepo;
    private final EmpleadoRepository empleadoRepo;
    private final ProductoRepository productoRepo;

    public List<Venta> findAll() {
        return ventaRepo.findAll();
    }

    public Venta findById(Integer id) {
        return ventaRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + id));
    }

    @Transactional
    public Venta save(Venta venta) {
        // Resolver relaciones
        if (venta.getCliente() != null && venta.getCliente().getIdCliente() != null)
            venta.setCliente(clienteRepo.findById(venta.getCliente().getIdCliente()).orElse(null));
        if (venta.getEmpleado() != null && venta.getEmpleado().getIdEmpleado() != null)
            venta.setEmpleado(empleadoRepo.findById(venta.getEmpleado().getIdEmpleado()).orElse(null));

        // Vincular detalles a la venta y resolver producto
        for (DetalleVenta detalle : venta.getDetalles()) {
            detalle.setVenta(venta);
            if (detalle.getProducto() != null && detalle.getProducto().getIdProducto() != null)
                detalle.setProducto(productoRepo.findById(detalle.getProducto().getIdProducto()).orElse(null));
        }
        return ventaRepo.save(venta);
    }

    @Transactional
    public Venta update(Integer id, Venta datos) {
        Venta venta = findById(id);

        // Actualizar fecha si viene
        if (datos.getFecha() != null) {
            venta.setFecha(datos.getFecha());
        }

        // Actualizar cliente
        if (datos.getCliente() != null && datos.getCliente().getIdCliente() != null) {
            venta.setCliente(clienteRepo.findById(datos.getCliente().getIdCliente()).orElse(null));
        }

        // Actualizar empleado
        if (datos.getEmpleado() != null && datos.getEmpleado().getIdEmpleado() != null) {
            venta.setEmpleado(empleadoRepo.findById(datos.getEmpleado().getIdEmpleado()).orElse(null));
        }

        // Si vienen detalles nuevos, reemplazar los existentes
        if (datos.getDetalles() != null && !datos.getDetalles().isEmpty()) {
            venta.getDetalles().clear();
            for (DetalleVenta detalle : datos.getDetalles()) {
                detalle.setVenta(venta);
                if (detalle.getProducto() != null && detalle.getProducto().getIdProducto() != null)
                    detalle.setProducto(productoRepo.findById(detalle.getProducto().getIdProducto()).orElse(null));
                venta.getDetalles().add(detalle);
            }
        }

        return ventaRepo.save(venta);
    }

    @Transactional
    public void delete(Integer id) {
        ventaRepo.deleteById(id);
    }
}
