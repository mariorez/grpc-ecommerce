package order

import io.grpc.ForwardingServerCallListener
import io.grpc.ServerCall.Listener
import java.util.logging.Logger

class OrderCallListener<R>(
    private val delegate: Listener<R>
) : ForwardingServerCallListener<R>() {

    companion object {
        val logger: Logger = Logger.getLogger(OrderCallListener::class.java.toString())
    }

    override fun delegate(): Listener<R> {
        return delegate
    }

    override fun onMessage(message: R) {
        logger.info("LISTENER from Client -> Service $message");
        super.onMessage(message)
    }
}