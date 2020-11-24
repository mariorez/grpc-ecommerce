package order

import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCall.Listener
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import java.util.logging.Logger

class OrderInterceptor : ServerInterceptor {

    companion object {
        val logger: Logger = Logger.getLogger(OrderInterceptor::class.java.toString())
    }

    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata?,
        next: ServerCallHandler<ReqT, RespT>
    ): Listener<ReqT> {
        logger.info("[Server Interceptor] : Remote Method Invoked - ${call.methodDescriptor.fullMethodName}")
        val serverCall = OrderServerCall(call)
        return OrderCallListener(next.startCall(serverCall, headers))
    }
}
