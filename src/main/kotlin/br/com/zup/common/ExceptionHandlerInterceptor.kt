package br.com.zup.common

import io.grpc.BindableService
import io.grpc.stub.StreamObserver
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExceptionHandlerInterceptor(@Inject private val resolver: ExceptionHandlerResolver) :
    MethodInterceptor<BindableService, Any?> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun intercept(context: MethodInvocationContext<BindableService, Any?>): Any? {
        return try {
            context.proceed()
        } catch (e: Exception) {

            logger.error("Handling the exception '${e.javaClass.name}' while processing the call: ${context.targetMethod}", e)

            @Suppress("UNCHECKED_CAST")
            val handler = resolver.resolve(e) as ExceptionHandler<Exception>
            val status = handler.handle(e)

            GrpcEndpointArguments(context).response()
                .onError(status.asRuntimeException())

            null
        }
    }

    /**
     * Represents the endpoint method arguments
     */
    private class GrpcEndpointArguments(val context : MethodInvocationContext<BindableService, Any?>) {

        fun response(): StreamObserver<*> {
            return context.parameterValues[1] as StreamObserver<*>
        }

    }
}