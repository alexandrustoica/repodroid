
package com.ubb.alexandrustoica.reporter.rest

interface OnAsyncResponseReadyCallback<in R, in E>:
        OnTaskResponseCompletedCallback<AsyncResponse<R, E>>