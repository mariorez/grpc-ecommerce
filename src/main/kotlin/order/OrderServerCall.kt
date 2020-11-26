package order

import io.grpc.ForwardingServerCall.SimpleForwardingServerCall
import io.grpc.ServerCall
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class OrderServerCall<ReqT, RestT>(
    delegate: ServerCall<ReqT, RestT>
) : SimpleForwardingServerCall<ReqT, RestT>(delegate) {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(OrderServerCall::class.java)
    }

    override fun sendMessage(message: RestT) {
        LOG.info("CALL from Service -> Client : $message");
        super.sendMessage(message)
    }
}
