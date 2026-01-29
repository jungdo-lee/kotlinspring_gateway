package com.example.gateway

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "gateway.security")
data class GatewaySecurityProperties(
    val headerName: String = "X-API-TOKEN",
    val allowedTokens: Set<String> = emptySet(),
    val excludedPaths: Set<String> = setOf("/actuator/health", "/actuator/info")
)
