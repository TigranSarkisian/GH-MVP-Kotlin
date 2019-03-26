package com.sarkisian.gh.util.error

import java.lang.Exception

class ObjectNotFoundException(override var message: String) : Exception()