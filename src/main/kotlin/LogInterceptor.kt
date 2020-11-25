import io.grpc.ForwardingServerCall.SimpleForwardingServerCall
import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import io.grpc.Status
import java.util.logging.Logger

class LogInterceptor : ServerInterceptor {

    companion object {
        val logger: Logger = Logger.getLogger(LogInterceptor::class.java.toString())
    }

    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val logServerCall = LogServerCall(call)
        return object : SimpleForwardingServerCallListener<ReqT>(next.startCall(logServerCall, headers)) {
            override fun onMessage(message: ReqT) {
                logger.info("[INTERCEPTOR-IN]: $headers - $message")
                super.onMessage(message)
            }
        }
    }

    private class LogServerCall<ReqT, RestT>(
        delegate: ServerCall<ReqT, RestT>
    ) : SimpleForwardingServerCall<ReqT, RestT>(delegate) {
        override fun close(status: Status, trailers: Metadata) {
            if (status.isOk) {
                logger.info("[INTERCEPTOR-OUT]: $status")
            } else {
                logger.warning("[INTERCEPTOR-OUT]: $status")
            }
            super.close(status, trailers)
        }
    }
}
