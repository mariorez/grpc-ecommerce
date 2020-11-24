package order

import io.grpc.ForwardingServerCall.SimpleForwardingServerCall
import io.grpc.ServerCall
import java.util.logging.Logger

class OrderServerCall<ReqT, RestT>(
    delegate: ServerCall<ReqT, RestT>?
) : SimpleForwardingServerCall<ReqT, RestT>(delegate) {

    companion object {
        val logger: Logger = Logger.getLogger(OrderServerCall::class.java.toString())
    }

    override fun sendMessage(message: RestT) {
        logger.info("CALL from Service -> Client : $message");
        super.sendMessage(message)
    }
}
