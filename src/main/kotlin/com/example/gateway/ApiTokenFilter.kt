package com.example.gateway

import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class ApiTokenFilter(
    private val securityProperties: GatewaySecurityProperties
) : GlobalFilter, Ordered {

    override fun filter(exchange: ServerWebExchange, chain: org.springframework.cloud.gateway.filter.GatewayFilterChain): Mono<Void> {
        val requestPath = exchange.request.path.pathWithinApplication().value()
        if (isExcluded(requestPath)) {
            return chain.filter(exchange)
        }

        val token = exchange.request.headers.getFirst(securityProperties.headerName)
        if (token.isNullOrBlank() || !securityProperties.allowedTokens.contains(token)) {
            return unauthorized(exchange)
        }

        return chain.filter(exchange)
    }

    override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE

    private fun isExcluded(path: String): Boolean {
        return securityProperties.excludedPaths.any { excluded ->
            path == excluded || path.startsWith("${excluded}/")
        }
    }

    private fun unauthorized(exchange: ServerWebExchange): Mono<Void> {
        val response = exchange.response
        response.statusCode = HttpStatus.UNAUTHORIZED
        response.headers.contentType = MediaType.APPLICATION_JSON
        val body = """{"message":"Invalid or missing API token"}"""
        val buffer: DataBuffer = response.bufferFactory().wrap(body.toByteArray())
        return response.writeWith(Mono.just(buffer))
    }
}
