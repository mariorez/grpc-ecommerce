package order

import io.grpc.ForwardingServerCallListener
import io.grpc.ServerCall.Listener
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OrderCallListener<R>(
    private val delegate: Listener<R>
) : ForwardingServerCallListener<R>() {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(OrderCallListener::class.java)
    }

    override fun delegate(): Listener<R> {
        return delegate
    }

    override fun onMessage(message: R) {
        LOG.info("LISTENER from Client -> Service $message");
        super.onMessage(message)
    }
}