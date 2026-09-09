package com.nforceone.sync.projectdashboard.dto;

/** {@code client} is null for internal work (a project with no client by design). */
public record ProjectOptionDto(Long id, String name, String client) {}
