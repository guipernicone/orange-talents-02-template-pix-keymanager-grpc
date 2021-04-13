package com.zup.br.orange.grpc

import com.zup.br.orange.PixKeyDeleteServiceGrpc
import com.zup.br.orange.PixKeyRegisterServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel

@Factory
class GrpcClients {

    @Bean
    fun PixKeyRegisterGrpcRegister(
        @GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel
    ): PixKeyRegisterServiceGrpc.PixKeyRegisterServiceBlockingStub {
        return PixKeyRegisterServiceGrpc.newBlockingStub(channel)
    }

    @Bean
    fun PixKeyDeleteGrpcRegister(
        @GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel
    ): PixKeyDeleteServiceGrpc.PixKeyDeleteServiceBlockingStub {
        return PixKeyDeleteServiceGrpc.newBlockingStub(channel)
    }
}