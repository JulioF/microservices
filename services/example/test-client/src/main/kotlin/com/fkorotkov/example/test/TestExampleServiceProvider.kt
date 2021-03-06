package com.fkorotkov.example.test

import com.fkorotkov.example.ExampleServiceImpl
import com.fkorotkov.example.client.ExampleServiceClient
import com.fkorotkov.example.client.impl.ExampleServiceClientImpl
import com.fkorotkov.services.example.grpc.ExampleGrpcKt
import io.grpc.ManagedChannel
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import java.util.concurrent.TimeUnit


class TestExampleServiceProvider() {
  companion object {
    private val testServiceName = "example"
  }

  private val serviceImpl: ExampleServiceImpl = ExampleServiceImpl()

  private val inProcessServer = InProcessServerBuilder.forName(testServiceName)
    .addService(serviceImpl)
    .directExecutor()
    .build()

  fun start() = inProcessServer.start()

  fun stop() {
    inProcessServer.shutdownNow().awaitTermination(1, TimeUnit.MINUTES)
    inprocessChannel.shutdownNow().awaitTermination(1, TimeUnit.MINUTES)
  }

  private val inprocessChannel: ManagedChannel by lazy {
    InProcessChannelBuilder.forName(testServiceName).directExecutor().build()
  }

  val client: ExampleServiceClient by lazy {
    val futureStub = ExampleGrpcKt.ExampleCoroutineStub(inprocessChannel)
    ExampleServiceClientImpl(futureStub)
  }
}
