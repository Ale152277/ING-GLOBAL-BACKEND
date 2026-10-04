package com.ingenieraglobal.ecommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ingenieraglobal.ecommerce.dtos.admin.AdminDashboardDTO;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.services.AdminDashboardService;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;


    public AdminDashboardController(
            AdminDashboardService adminDashboardService) {

        this.adminDashboardService = adminDashboardService;
    }


    @GetMapping
    public ResponseEntity<ApiResponse<AdminDashboardDTO>> obtenerDashboard() {

        AdminDashboardDTO dashboard =
                adminDashboardService.obtenerDashboard();

        return ResponseEntity.ok(
                ApiResponse.success(dashboard));
    }
}