package com.zup.br.orange.grpc

import com.zup.br.orange.PixKeyRegisterServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel

@Factory
class GrpcClients {

    @Bean
    fun PixKeyGrpcRegister(
        @GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel
    ): PixKeyRegisterServiceGrpc.PixKeyRegisterServiceBlockingStub {
        return PixKeyRegisterServiceGrpc.newBlockingStub(channel)
    }
}