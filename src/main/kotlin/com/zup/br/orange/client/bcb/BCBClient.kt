package com.zup.br.orange.client.bcb

import com.zup.br.orange.client.bcb.request.CreatePixClientRequest
import com.zup.br.orange.client.bcb.request.DeletePixClientRequest
import com.zup.br.orange.client.bcb.response.CreatePixClientResponse
import com.zup.br.orange.client.bcb.response.DeletePixClientResponse
import com.zup.br.orange.client.itau.ItauClient
import com.zup.br.orange.client.itau.response.ConsultAccountResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Fallback

@Client("\${bcb.pix.url}/api/v1/pix/keys")
interface BCBClient {

    @Post
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    fun createPix(@Body createPixClientRequest: CreatePixClientRequest): CreatePixClientResponse?

    @Delete("/{key}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    fun deletePix(key: String, @Body deletePixClientRequest: DeletePixClientRequest): DeletePixClientResponse?

    @Fallback
    class BCBClientFallBack: BCBClient {
        override fun createPix(@Body createPixClientRequest: CreatePixClientRequest): CreatePixClientResponse? = null

        override fun deletePix(key: String, @Body deletePixClientRequest: DeletePixClientRequest)
        : DeletePixClientResponse? = null
    }
}