// src/main/java/ec/edu/ups/per66/services/BancoService.java
package ec.edu.ups.per66.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ec.edu.ups.per66.dao.CuentaDAO;
import ec.edu.ups.per66.dao.PersonaDAO;
import ec.edu.ups.per66.dto.CuentaDTO;
import ec.edu.ups.per66.dto.TransferenciaDTO;
import ec.edu.ups.per66.business.CuentasON;
import ec.edu.ups.per66.modelo.Persona;
import ec.edu.ups.per66.modelo.Cuenta;
import ec.edu.ups.per66.modelo.Transferencia;

@Path("/banco")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BancoService {

    @Inject private PersonaDAO personaDAO;
    @Inject private CuentasON cuentasON;
    @Inject private CuentaDAO cuentaDAO;

    // ----------------------------
    // Personas
    // ----------------------------

    @POST @Path("/personas")
    public Response crearPersona(Persona p) {
        // 1. Validar cédula única
        if (personaDAO.read(p.getCedula()) != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ApiResponse(400, "Cédula ya registrada"))
                           .build();
        }
        // 2. Validar email único
        if (!personaDAO.findByEmail(p.getEmail()).isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ApiResponse(400, "Email ya registrado"))
                           .build();
        }

        // 3. Insertar
        personaDAO.insert(p);
        // Romper ciclo antes de serializar
        p.setCuentas(null);
        return Response.status(Response.Status.CREATED).entity(p).build();
    }

    @GET @Path("/personas")
    public Response listarPersonas() {
        List<Persona> lista = personaDAO.getAll();
        // Romper ciclo en cada persona
        lista.forEach(p -> p.setCuentas(null));
        return Response.ok(lista).build();
    }

    @GET @Path("/personas/{cedula}")
    public Response getPersona(@PathParam("cedula") String cedula) {
        Persona p = personaDAO.read(cedula);
        if (p == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity(new ApiResponse(404, "Persona no encontrada"))
                           .build();
        }
        p.setCuentas(null);
        return Response.ok(p).build();
    }

    // ----------------------------
    // Resto de endpoints (cuentas y transferencias)
    // ----------------------------

    @POST @Path("/cuentas")
    public Response crearCuenta(Cuenta c) {
        try {
            Cuenta nueva = cuentasON.crearCuenta(
                c.getTitular().getCedula(), c.getNumeroCuenta());
            nueva.getTitular().setCuentas(null);
            return Response.status(Response.Status.CREATED).entity(nueva).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ApiResponse(400, e.getMessage()))
                           .build();
        }
    }


        // LISTAR **DTO** de cuentas:
    @GET @Path("/cuentas")
    public Response listarCuentas() {
        List<Cuenta> lista = cuentasON.listarCuentas();

        // mapeamos cada Cuenta a CuentaDTO, incluyendo saldo
        List<CuentaDTO> dtos = lista.stream()
            .map(c -> new CuentaDTO(
                c.getTitular().getCedula(),
                c.getNumeroCuenta(),
                c.getSaldo()
            ))
            .collect(Collectors.toList());

        return Response.ok(dtos).build();
    }

        // OBTENER UNA CUENTA concreta (dto):
    @GET @Path("/cuentas/{numero}")
    public Response getCuenta(@PathParam("numero") String numero) {
        Cuenta c = cuentasON.getCuenta(numero);
        if (c == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity(new ApiResponse(404, "Cuenta no encontrada"))
                           .build();
        }
        CuentaDTO dto = new CuentaDTO(
            c.getTitular().getCedula(),
            c.getNumeroCuenta(),
            c.getSaldo()
        );
        return Response.ok(dto).build();
    }

    @POST @Path("/cuentas/{numero}/deposito")
    public Response depositar(@PathParam("numero") String num, BigDecimal monto) {
        try {
            cuentasON.depositar(num, monto);
            return Response.ok(new ApiResponse(200, "Depósito exitoso")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ApiResponse(400, e.getMessage()))
                           .build();
        }
    }

    @POST @Path("/cuentas/{numero}/retiro")
    public Response retirar(@PathParam("numero") String num, BigDecimal monto) {
        try {
            cuentasON.retirar(num, monto);
            return Response.ok(new ApiResponse(200, "Retiro exitoso")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ApiResponse(400, e.getMessage()))
                           .build();
        }
    }

    @POST @Path("/transferencias")
    public Response transferir(TransferenciaDTO dto) {
        try {
            Transferencia t = cuentasON.transferir(
                dto.getOrigen(),    // <- coincide con getter/setter de TransferenciaDTO
                dto.getDestino(),
                dto.getMonto()
            );
            // armamos el DTO de respuesta completo:
            TransferenciaDTO salida = new TransferenciaDTO(
                t.getId(),
                t.getCuentaOrigen().getNumeroCuenta(),
                t.getCuentaDestino().getNumeroCuenta(),
                t.getMonto(),
                t.getFecha()
            );
            return Response
                     .status(Response.Status.CREATED)
                     .entity(salida)
                     .build();
        } catch (Exception e) {
            return Response
                     .status(Response.Status.BAD_REQUEST)
                     .entity(new ApiResponse(400, e.getMessage()))
                     .build();
        }
    }

    // GET /rest/banco/cuentas/{numero}/transferencias   lista DTOs
    @GET @Path("/cuentas/{numero}/transferencias")
    public Response historialPorCuenta(@PathParam("numero") String num) {
      List<TransferenciaDTO> dtos = cuentasON.listarTransferenciasDTOPorCuenta(num);
      if (dtos.isEmpty()) {
          return Response.status(Response.Status.NOT_FOUND)
                         .entity(new ApiResponse(404, "No hay transferencias para esta cuenta"))
                         .build();
      }
      return Response.ok(dtos).build();
    }
}