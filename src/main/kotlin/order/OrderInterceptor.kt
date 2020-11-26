package order

import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCall.Listener
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OrderInterceptor : ServerInterceptor {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(OrderInterceptor::class.java)
    }

    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata?,
        next: ServerCallHandler<ReqT, RespT>
    ): Listener<ReqT> {
        LOG.info("[Server Interceptor] : Remote Method Invoked - ${call.methodDescriptor.fullMethodName}")
        val serverCall = OrderServerCall(call)
        return OrderCallListener(next.startCall(serverCall, headers))
    }
}
