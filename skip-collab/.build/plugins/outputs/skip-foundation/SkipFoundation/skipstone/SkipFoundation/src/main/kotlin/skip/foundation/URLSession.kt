// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*

// Note: !SKIP code paths used to validate implementation only.
// Not used in applications. See contribution guide for details.

private val logger: SkipLogger = SkipLogger(subsystem = "skip", category = "URLSession")

class URLSession {

    var configuration: URLSessionConfiguration

    constructor(configuration: URLSessionConfiguration) {
        this.configuration = configuration
    }

    private fun openConnection(request: URLRequest): java.net.URLConnection {
        val config = this.configuration
        val url_0 = request.url.sref()
        if (url_0 == null) {
            throw NoURLInRequestError()
        }

        // note that `openConnection` does not actually connect(); we do that below in a Dispatchers.IO coroutine
        val connection = url_0.platformValue.openConnection()

        when (request.cachePolicy) {
            URLRequest.CachePolicy.useProtocolCachePolicy -> connection.setUseCaches(true)
            URLRequest.CachePolicy.returnCacheDataElseLoad -> connection.setUseCaches(true)
            URLRequest.CachePolicy.returnCacheDataDontLoad -> connection.setUseCaches(true)
            URLRequest.CachePolicy.reloadRevalidatingCacheData -> connection.setUseCaches(true)
            URLRequest.CachePolicy.reloadIgnoringLocalCacheData -> connection.setUseCaches(false)
            URLRequest.CachePolicy.reloadIgnoringLocalAndRemoteCacheData -> connection.setUseCaches(false)
        }

        //connection.setDoInput(true)
        //connection.setDoOutput(true)

        (connection as? java.net.HttpURLConnection).sref()?.let { httpConnection ->
            request.httpMethod?.let { httpMethod ->
                httpConnection.setRequestMethod(httpMethod)
            }

            httpConnection.connectTimeout = if (request.timeoutInterval > 0) (request.timeoutInterval * 1000.0).toInt() else (config.timeoutIntervalForRequest * 1000.0).toInt()
            httpConnection.readTimeout = config.timeoutIntervalForResource.toInt()
        }

        for ((headerKey, headerValue) in (request.allHTTPHeaderFields ?: dictionaryOf()).sref()) {
            connection.setRequestProperty(headerKey, headerValue)
        }

        return connection.sref()
    }

    private fun connect(request: URLRequest): Tuple2<java.net.URLConnection, HTTPURLResponse> {
        val connection = openConnection(request = request)

        var statusCode = -1
        (connection as? java.net.HttpURLConnection).sref()?.let { httpConnection ->
            statusCode = httpConnection.getResponseCode()
        }

        val headerFields = connection.getHeaderFields()

        val httpVersion: String? = null // TODO: extract version from response
        var headers: Dictionary<String, String> = dictionaryOf()
        for ((key, values) in headerFields.sref()) {
            if ((key != null) && (values != null)) {
                for (value in values.sref()) {
                    if (value != null) {
                        headers[key] = value
                    }
                }
            }
        }

        val response = (try { HTTPURLResponse(url = request.url!!, statusCode = statusCode, httpVersion = httpVersion, headerFields = headers) } catch (_: NullReturnException) { null })
        return Tuple2(connection.sref(), response!!)
    }

    suspend fun data(for_: URLRequest): Tuple2<Data, URLResponse> = Async.run l@{
        val request = for_
        val (data, response) = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) l@{
            val (connection, response) = connect(request = request)
            val inputStream = connection.getInputStream()
            val outputStream = java.io.ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while ((inputStream.read(buffer).also { it -> bytesRead = it } != -1)) {
                outputStream.write(buffer, 0, bytesRead)
            }
            inputStream.close()

            val bytes = outputStream.toByteArray()
            return@l Tuple2(Data(platformValue = bytes), response as HTTPURLResponse)
        }

        return@l Tuple2(data.sref(), response)
    }

    suspend fun data(from: URL): Tuple2<Data, URLResponse> = Async.run l@{
        val url = from
        return@l this.data(for_ = URLRequest(url = url))
    }

    suspend fun download(for_: URLRequest): Tuple2<URL, URLResponse> = Async.run l@{
        val request = for_
        val url_1 = request.url.sref()
        if (url_1 == null) {
            throw NoURLInRequestError()
        }

        // seems to be the typical way of converting from java.net.URL into android.net.Uri (which is needed by the DownloadManager)
        val uri = android.net.Uri.parse(url_1.description)

        val downloadManager = (ProcessInfo.processInfo.androidContext.getSystemService(android.content.Context.DOWNLOAD_SERVICE) as android.app.DownloadManager).sref()

        val downloadRequest = android.app.DownloadManager.Request(uri)
            .setAllowedOverMetered(this.configuration.allowsExpensiveNetworkAccess)
            .setAllowedOverRoaming(this.configuration.allowsConstrainedNetworkAccess)
            .setShowRunningNotification(true)

        for ((headerKey, headerValue) in (request.allHTTPHeaderFields ?: dictionaryOf()).sref()) {
            downloadRequest.addRequestHeader(headerKey, headerValue)
        }

        val downloadId = downloadManager.enqueue(downloadRequest)
        val query = android.app.DownloadManager.Query()
            .setFilterById(downloadId)

        // Query the DownloadManager for the response, which returns a SQLite cursor with the current download status of all the outstanding downloads.
        fun queryDownload(): Result<Tuple2<URL, URLResponse>, Error>? {
            var deferaction_0: (() -> Unit)? = null
            try {
                val cursor = downloadManager.query(query)

                deferaction_0 = {
                    cursor.close()
                }

                if (!cursor.moveToFirst()) {
                    // download not found
                    val error = UnableToStartDownload()
                    return Result.failure(error)
                }

                val status = cursor.getInt(cursor.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_STATUS))
                val uri = cursor.getString(cursor.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_URI)) // URI to be downloaded.

                // STATUS_FAILED, STATUS_PAUSED, STATUS_PENDING, STATUS_RUNNING, STATUS_SUCCESSFUL
                if (status == android.app.DownloadManager.STATUS_PAUSED) {
                    return null
                }
                if (status == android.app.DownloadManager.STATUS_PENDING) {
                    return null
                }

                //let desc = cursor.getString(cursor.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_DESCRIPTION)) // The client-supplied description of this download // NPE
                //let id = cursor.getString(cursor.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_ID)) // An identifier for a particular download, unique across the system. // NPE
                // let lastModified = cursor.getLong(cursor.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)) // Timestamp when the download was last modified, in System.currentTimeMillis() (wall clock time in UTC).
                val localFilename = cursor.getString(cursor.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_LOCAL_FILENAME)) // Path to the downloaded file on disk.
                //let localURI = cursor.getString(cursor.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_LOCAL_URI)) // Uri where downloaded file will be stored. // NPE
                // let mediaproviderURI = cursor.getString(cursor.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_MEDIAPROVIDER_URI)) // The URI to the corresponding entry in MediaProvider for this downloaded entry. // NPE
                //let mediaType = cursor.getString(cursor.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_MEDIA_TYPE)) // Internet Media Type of the downloaded file.
                val reason = cursor.getString(cursor.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_REASON)) // Provides more detail on the status of the download.
                //let title = cursor.getString(cursor.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_TITLE)) // The client-supplied title for this download.
                val totalSizeBytes = cursor.getLong(cursor.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_TOTAL_SIZE_BYTES)) // Total size of the download in bytes.
                val bytesDownloaded = cursor.getLong(cursor.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)) // Number of bytes download so far.

                if (status == android.app.DownloadManager.STATUS_RUNNING) {
                    // TODO: update progress
                    //  if let progress = Progress.current() {
                    //  }
                    return null
                } else if (status == android.app.DownloadManager.STATUS_SUCCESSFUL) {
                    val httpVersion: String? = null // TODO: extract version from response
                    var headers: Dictionary<String, String> = dictionaryOf()
                    val statusCode = 200 // TODO: extract status code
                    headers["Content-Length"] = totalSizeBytes?.description
                    //headers["Last-Modified"] = lastModified // TODO: convert to Date
                    val response = (try { HTTPURLResponse(url = url_1, statusCode = statusCode, httpVersion = httpVersion, headerFields = headers) } catch (_: NullReturnException) { null })
                    val localURL = URL(fileURLWithPath = localFilename)
                    return Result.success(Tuple2((localURL as URL).sref(), response as URLResponse))
                } else if (status == android.app.DownloadManager.STATUS_FAILED) {
                    // File download failed
                    // TODO: create error from error
                    val error = FailedToDownloadURLError()
                    return Result.failure(error)
                } else {
                    // no known android.app.DownloadManager.STATUS_*
                    // this can happen with Robolectric tests, since ShadowDownloadManager is just a stub and it sets 0 for the status
                    val error = DownloadUnsupportedWithRobolectric(status = status)
                    return Result.failure(error)
                }

                return null
            } finally {
                deferaction_0?.invoke()
            }
        }

        val isRobolectric = (try { Class.forName("org.robolectric.Robolectric") } catch (_: Throwable) { null }) != null

        val response: Result<Tuple2<URL, URLResponse>, Error> = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) l@{
            if (isRobolectric) {
                // Robolectric's ShadowDownloadManager doesn't actually download anything, so we fake it for testing by just getting the data in-memory (hoping it isn't too large!) and saving it to a temporary file
                try {
                    val (data, response) = data(for_ = request)
                    val outputFileURL: URL = FileManager.default.temporaryDirectory.appendingPathComponent(UUID().uuidString)
                    data.write(to = outputFileURL)
                    return@l Result.success(Tuple2(outputFileURL.sref(), response))
                } catch (error: Throwable) {
                    @Suppress("NAME_SHADOWING") val error = error.aserror()
                    return@l Result.failure(error)
                }
            } else {
                // initiate using android.app.DownloadManager
                // TODO: rather than polling in a loop, we could do android.registerBroadcastReceiver(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE, handleDownloadEvent)
                while (true) {
                    queryDownload()?.let { downloadResult ->
                        return@l downloadResult
                    }
                    kotlinx.coroutines.delay(250) // wait and poll again
                }
            }
            return@l Result.failure(FailedToDownloadURLError()) // needed for Kotlin type checking
        }

        when (response) {
            is Result.FailureCase -> {
                val error = response.associated0
                throw error as Throwable
            }
            is Result.SuccessCase -> {
                val urlResponseTuple = response.associated0
                return@l urlResponseTuple.sref()
            }
        }

    }

    suspend fun download(from: URL): Tuple2<URL, URLResponse> = Async.run l@{
        val url = from
        return@l this.download(for_ = URLRequest(url = url))
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    suspend fun upload(for_: URLRequest, fromFile: URL): Tuple2<Data, URLResponse> = Async.run l@{
        val request = for_
        val fileURL = fromFile
        return@l fatalError("TODO: URLSession.data")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    suspend fun upload(for_: URLRequest, from: Data): Tuple2<Data, URLResponse> = Async.run l@{
        val request = for_
        val bodyData = from
        return@l fatalError("TODO: URLSession.data")
    }

    suspend fun bytes(from: URL): Tuple2<URLSessionAsyncBytes, URLResponse> = Async.run l@{
        val url = from
        return@l bytes(for_ = URLRequest(url = url))
    }

    suspend fun bytes(for_: URLRequest): Tuple2<URLSessionAsyncBytes, URLResponse> = Async.run l@{
        val request = for_
        return@l kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) l@{
            val (connection, response) = connect(request = request)
            val stream: kotlinx.coroutines.flow.Flow<UByte> = kotlinx.coroutines.flow.flow {
                connection.getInputStream().use { inputStream ->
                    while (true) {
                        val byte = inputStream.read()
                        if (byte == -1) {
                            break
                        } else {
                            emit(byte.toUByte())
                        }
                    }
                }
            }

            return@l Tuple2(URLSessionAsyncBytes(stream = stream), response)
        }
    }

    enum class DelayedRequestDisposition(override val rawValue: Int, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): Sendable, RawRepresentable<Int> {
        continueLoading(0),
        useNewRequest(1),
        cancel(2);

        companion object {
        }
    }

    fun DelayedRequestDisposition(rawValue: Int): URLSession.DelayedRequestDisposition? {
        return when (rawValue) {
            0 -> DelayedRequestDisposition.continueLoading
            1 -> DelayedRequestDisposition.useNewRequest
            2 -> DelayedRequestDisposition.cancel
            else -> null
        }
    }

    enum class AuthChallengeDisposition(override val rawValue: Int, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): Sendable, RawRepresentable<Int> {
        useCredential(0),
        performDefaultHandling(1),
        cancelAuthenticationChallenge(2),
        rejectProtectionSpace(3);

        companion object {
        }
    }

    fun AuthChallengeDisposition(rawValue: Int): URLSession.AuthChallengeDisposition? {
        return when (rawValue) {
            0 -> AuthChallengeDisposition.useCredential
            1 -> AuthChallengeDisposition.performDefaultHandling
            2 -> AuthChallengeDisposition.cancelAuthenticationChallenge
            3 -> AuthChallengeDisposition.rejectProtectionSpace
            else -> null
        }
    }

    enum class ResponseDisposition(override val rawValue: Int, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): Sendable, RawRepresentable<Int> {
        cancel(0),
        allow(1),
        becomeDownload(2),
        becomeStream(3);

        companion object {
        }
    }

    fun ResponseDisposition(rawValue: Int): URLSession.ResponseDisposition? {
        return when (rawValue) {
            0 -> ResponseDisposition.cancel
            1 -> ResponseDisposition.allow
            2 -> ResponseDisposition.becomeDownload
            3 -> ResponseDisposition.becomeStream
            else -> null
        }
    }

    companion object {
        private val _shared = URLSession(configuration = URLSessionConfiguration.default)

        val shared: URLSession
            get() = _shared
    }
}

interface URLSessionTask {
}

interface URLSessionDataTask: URLSessionTask {
}

interface URLSessionDelegate {
}

interface URLSessionTaskDelegate: URLSessionDelegate {
}

interface URLSessionDataDelegate: URLSessionTaskDelegate {
}

class NoURLInRequestError: Exception(), Error {

    companion object {
    }
}

class FailedToDownloadURLError: Exception(), Error {

    companion object {
    }
}

class UnableToStartDownload: Exception(), Error {

    companion object {
    }
}

class DownloadUnsupportedWithRobolectric: Exception, Error {
    internal val status: Int

    internal constructor(status: Int): super() {
        this.status = status
    }

    companion object {
    }
}

// -------------------------------
// TODO: add Flow support to SkipLib.AsyncSequence and combine implementations
// -------------------------------

typealias PlatformAsyncStream<Element> = kotlinx.coroutines.flow.Flow<Element>

interface SkipAsyncSequence<Element> {

    /// The underlying `AsyncStream` or `kotlinx.coroutines.flow.Flow` for this element
    val stream: kotlinx.coroutines.flow.Flow<Element>

    // Skip FIXME: Cannot declare both forms of `reduce` due to JVM signature clash:
    // The following declarations have the same JVM signature (reduce(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;):
    //func reduce<Result>(_ initialResult: Result, _ nextPartialResult: (_ partialResult: Result, Element) async throws -> Result) async rethrows -> Result {
    //    fatalError("TODO: SkipAsyncSequence extension functions")
    //}

    suspend fun <Result> reduce(into: Result, updateAccumulatingResult: suspend (InOut<Result>, Element) -> Unit): Result = Async.run l@{
        val initialResult = into
        var result = initialResult.sref()
        stream.collect { element -> updateAccumulatingResult(InOut({ result }, { result = it }), element) }
        return@l result.sref()
    }

    suspend fun contains(where: suspend (Element) -> Boolean): Boolean = Async.run l@{
        val predicate = where
        return@l fatalError("TODO: SkipAsyncSequence extension functions")
    }

    suspend fun allSatisfy(predicate: suspend (Element) -> Boolean): Boolean = Async.run l@{
        return@l fatalError("TODO: SkipAsyncSequence extension functions")
    }

    suspend fun contains(search: Element): Boolean = Async.run l@{
        return@l fatalError("TODO: SkipAsyncSequence extension functions")
    }

    suspend fun first(where: suspend (Element) -> Boolean): Element? = Async.run l@{
        val predicate = where
        return@l fatalError("TODO: SkipAsyncSequence extension functions")
    }

    suspend fun min(by: suspend (Element, Element) -> Boolean): Element? = Async.run l@{
        val areInIncreasingOrder = by
        return@l fatalError("TODO: SkipAsyncSequence extension functions")
    }

    suspend fun max(by: suspend (Element, Element) -> Boolean): Element? = Async.run l@{
        val areInIncreasingOrder = by
        return@l fatalError("TODO: SkipAsyncSequence extension functions")
    }

    suspend fun min(): Element? = Async.run l@{
        return@l fatalError("TODO: SkipAsyncSequence extension functions")
    }

    suspend fun max(): Element? = Async.run l@{
        return@l fatalError("TODO: SkipAsyncSequence extension functions")
    }

}

/// An asynchronous sequence generated from a closure that calls a continuation
/// to produce new elements.
///
/// `AsyncStream` conforms to `AsyncSequence`, providing a convenient way to
/// create an asynchronous sequence without manually implementing an
/// asynchronous iterator. In particular, an asynchronous stream is well-suited
/// to adapt callback- or delegation-based APIs to participate with
/// `async`-`await`.
/// You initialize an `AsyncStream` with a closure that receives an
/// `AsyncStream.Continuation`. Produce elements in this closure, then provide
/// them to the stream by calling the continuation's `yield(_:)` method. When
/// there are no further elements to produce, call the continuation's
/// `finish()` method. This causes the sequence iterator to produce a `nil`,
/// which terminates the sequence. The continuation conforms to `Sendable`, which permits
/// calling it from concurrent contexts external to the iteration of the
/// `AsyncStream`.
class SkipAsyncStream<Element>: SkipAsyncSequence<Element> {
    // Swift and Kotlin treat types nested within generic types in incompatible ways, and Skip cannot translate between the two. Consider moving this type out of its generic outer type
    //public struct Continuation : Sendable {
    //}

    override val stream: kotlinx.coroutines.flow.Flow<Element>
    constructor(stream: kotlinx.coroutines.flow.Flow<Element>) {
        this.stream = stream
    }

    companion object {
    }
}

// Wrap a kotlinx.coroutines.flow.Flow and provide an async interface
// Mirrors the interface of Foundation.AsyncBytes, which extends AsyncSequence
// Note that there could also be `URLAsyncBytes` and `SkipFileHandleAsyncBytes` for `URL.bytes` and `FileHandle.bytes`.
class URLSessionAsyncBytes: SkipAsyncSequence<UByte> {
    //public typealias Element = UInt8
    override val stream: kotlinx.coroutines.flow.Flow<UByte>

    //#if !SKIP
    //public var lines: SkipAsyncLineSequence<URLSessionAsyncBytes> {
    //    return SkipAsyncLineSequence(stream: self)
    //}
    //#endif

    override suspend fun allSatisfy(condition: suspend (UByte) -> Boolean): Boolean = Async.run l@{
        var satisfied = false
        stream.collect { b -> satisfied = condition(b) && satisfied }
        return@l satisfied
    }

    constructor(stream: kotlinx.coroutines.flow.Flow<UByte>) {
        this.stream = stream
    }

    companion object {
    }
}

