package com.ingenieraglobal.ecommerce.dtos.admin;

public class PedidoMesDTO {

    private String mes;
    private Long total;

    public PedidoMesDTO(String mes, Long total) {
        this.mes = mes;
        this.total = total;
    }

    public String getMes() {
        return mes;
    }

    public Long getTotal() {
        return total;
    }

}
