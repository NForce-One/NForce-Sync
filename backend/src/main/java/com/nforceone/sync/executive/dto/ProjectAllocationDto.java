package com.nforceone.sync.executive.dto;

public record ProjectAllocationDto(
        Long projectId,
        String projectName,
        long allocatedResources,
        int allocationPctTotal
) {}
