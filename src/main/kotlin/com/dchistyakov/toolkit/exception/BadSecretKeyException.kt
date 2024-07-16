package com.dchistyakov.toolkit.exception

class BadSecretKeyException(
    message: String = "Received invalid secret key",
    exception: RuntimeException = RuntimeException(message),
) :
    RuntimeException(message, exception)