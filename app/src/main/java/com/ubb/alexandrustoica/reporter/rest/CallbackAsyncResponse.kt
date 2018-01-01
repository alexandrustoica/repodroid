
package com.ubb.alexandrustoica.reporter.rest

interface CallbackAsyncResponse<in R, in E>:
        CallbackTask<AsyncResponse<R, E>>