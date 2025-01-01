package com.etablissement.fullstack_backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")  // This enforces that only ADMIN users can access any method in this controller
public class AdminController {

    @GetMapping("/dashboard")
    public String getAdminDashboard() {
        return "Admin Dashboard";
    }

    @PostMapping("/create")
    public String createNewAdminResource() {
        return "New Admin Resource Created";
    }

    // Other API methods here...
}