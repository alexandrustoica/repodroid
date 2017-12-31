
package com.ubb.alexandrustoica.reporter.task

import com.ubb.alexandrustoica.reporter.domain.AsyncResponse

interface CallbackAsyncResponse<in R, in E>:
        CallbackTask<AsyncResponse<R, E>>