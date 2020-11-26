import io.grpc.ForwardingServerCall.SimpleForwardingServerCall
import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import io.grpc.Status
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LogInterceptor : ServerInterceptor {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(LogInterceptor::class.java)
    }

    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val logServerCall = LogServerCall(call)
        return object : SimpleForwardingServerCallListener<ReqT>(next.startCall(logServerCall, headers)) {
            override fun onMessage(message: ReqT) {
                LOG.info("[IN] $message > $headers")
                super.onMessage(message)
            }
        }
    }

    private class LogServerCall<ReqT, RestT>(
        delegate: ServerCall<ReqT, RestT>
    ) : SimpleForwardingServerCall<ReqT, RestT>(delegate) {
        override fun close(status: Status, trailers: Metadata) {
            if (status.isOk) {
                LOG.info("[OUT] $status")
            } else {
                LOG.warn("[OUT] code=${status.code}, description=${status.description}, cause=${status.cause.toString()}")
            }
            super.close(status, trailers)
        }
    }
}
