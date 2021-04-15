package com.zup.br.orange.client.itau

import com.zup.br.orange.client.itau.response.ConsultAccountResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Fallback
import io.micronaut.retry.annotation.Recoverable

@Client("\${itau.contas.url}/api/v1/clientes")
@Recoverable
interface ItauClient {

    @Get("/{clientId}/contas")
    fun consultAccount(clientId: String, @QueryValue tipo:String) : ConsultAccountResponse?

    @Fallback
    class ItauClientFallBack: ItauClient{
        override fun consultAccount(clientId: String, tipo: String): ConsultAccountResponse? = null
    }
}