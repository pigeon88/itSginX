package com.pigeon.it.sginx

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.zip.GZIPInputStream

object ITalentSign {

    @JvmStatic
    fun main(args: Array<String>) {
        var content = """POST /api/v1/106454/111835773/Signin/AddV4 HTTP/1.1
X-Tingyun-Id: udY9AqA5jY4;c=2;r=118968189;
User-Agent: Mobile Build JMM-AL00,Android 7.0 Beisen BeisenApp NativeApp/Web UPaaSApp/4.0.1
Connection: close
Authorization: d40PoNCByJDIlaYlZg3WCTkQcJCZDV68S6uWTyBpu0IFQ537PpScAQ
Content-Type: application/json
Content-Length: 191
Host: www.italent.cn
Accept-Encoding: gzip
Cookie: Italent_Mobile=d40PoNCByJDIlaYlZg3WCTkQcJCZDV68S6uWTyBpu0IFQ537PpScAQ

{"device_code":"867556035051120","once_tag":"436fedbd-903b-4d00-9f31-44098a62ce43","signModel":0,"signinKey":"CD8A8702A41D0CC129F508C8B7784121CBDC7440716E6D57BA09ACE3E93DDD91","signinType":0}"""

        var url: String = "http://www.baidu.com"
        httpRequestRaw(url, content) { l: Long, i: Int, s: String ->

        }
    }

    fun httpRequest(_url: String, response: (Long, Int, String) -> Unit) {
        var httpConnect = openConnection(_url)

        httpConnect.connectTimeout = 8 * 1000  // 设置连接超时时间
        httpConnect.readTimeout = 8 * 1000  //设置从主机读取数据超时
        httpConnect.doOutput = true
        httpConnect.doInput = true
        httpConnect.useCaches = false
        httpConnect.requestMethod = "POST" // 设置为Post请求

        httpConnect.setRequestProperty("User-Agent", "Mobile Build JMM-AL00,Android 7.0 Beisen BeisenApp NativeApp/Web UPaaSApp/4.0.1")
        httpConnect.setRequestProperty("Connection", "close")
        httpConnect.setRequestProperty("Authorization", "d40PoNCByJDIlaYlZg3WCTkQcJCZDV68S6uWTyBpu0IFQ537PpScAQ")
        httpConnect.setRequestProperty("Content-Type", "application/json")
        httpConnect.setRequestProperty("Host", "www.italent.cn")
        httpConnect.setRequestProperty("Accept-Encoding", "gzip")
        httpConnect.setRequestProperty("Cookie", "Italent_Mobile=d40PoNCByJDIlaYlZg3WCTkQcJCZDV68S6uWTyBpu0IFQ537PpScAQ")

        httpConnect.connect() // 开始连接

        httpConnect.outputStream.write("{\"device_code\":\"867556035051120\",\"once_tag\":\"436fedbd-903b-4d00-9f31-44098a62ce43\",\"signModel\":0,\"signinKey\":\"CD8A8702A41D0CC129F508C8B7784121CBDC7440716E6D57BA09ACE3E93DDD91\",\"signinType\":0}".toByteArray())

        val responseCode = httpConnect.responseCode;
        var inputStream: InputStream = if (httpConnect.responseCode == HttpURLConnection.HTTP_OK) httpConnect.inputStream else httpConnect.errorStream

        if ("gzip".equals(httpConnect.contentEncoding, true)) {
            inputStream = GZIPInputStream(inputStream)
        }

        var reader = BufferedReader(InputStreamReader(inputStream))

        var strBuilder = StringBuilder()
        reader.forEachLine {
            strBuilder.append(it)
            println(it)
        }

        response(httpConnect.date, responseCode, strBuilder.toString())

        inputStream.close()
        reader.close()
    }

    /*fun httpRequestRaw(requestRaw: String, response: (Long, Int, String) -> Unit) {
        var requestRaw = "POST /api/v1/106454/111835773/Signin/AddV4 HTTP/1.1\n" +
                "User-Agent: Mobile Build JMM-AL00,Android 7.0 Beisen BeisenApp NativeApp/Web UPaaSApp/3.5.3\n" +
                "Connection: close\n" +
                "Authorization: XlT-NMFbKYplWash4IgGmCu3EwYnkOzlSJdagpuwbPSkZpWsqoqldA\n" +
                "Content-Type: application/json\n" +
                "Content-Length: 141\n" +
                "Host: www.italent.cn\n" +
                "Accept-Encoding: gzip\n" +
                "\n" +
                "{\"device_code\":\"867556035051120\",\"signModel\":0,\"signinKey\":\"EA769945B61606849F479EB2A771FCA592A7ACA83DCFBFA51CC65B8E114A6CB3\",\"signinType\":0}"
        requestRaw.reader().readLines().forEach { line ->
            var requestLine = line.split(" ")
            var head = line.split(": ")
        }
    }*/

    fun httpRequestRaw(url: String, content: String, response: (Long, Int, String) -> Unit) {
        var method = "GET"
        var methodLine = -1
        var heads = mutableMapOf<String, String>()
        var headsEnd = false
        var body: String? = null

        content.lines().forEachIndexed { index, s ->
            if (s.isNotBlank() && methodLine == -1) {
                var requestLine = s.split(" ")
                method = requestLine[0]
                //url = requestLine[1]
                methodLine = index
            } else {
                if (s.isBlank()) {
                    headsEnd = true
                } else {
                    if (s.isNotBlank()) {
                        if (!headsEnd) {
                            var head = s.split(":")
                            heads[head[0].trim()] = head[1].trim()
                        } else {
                            body = s
                        }
                    }
                }
            }
        }

        httpRequest(method, url, heads, body, response)
    }

    fun httpRequest(method: String, _url: String, heads: Map<String, String>, body: String?, response: (Long, Int, String) -> Unit) {
        var httpConnect = openConnection(_url)

        httpConnect.connectTimeout = 5 * 1000  // 设置连接超时时间
        httpConnect.readTimeout = 5 * 1000  //设置从主机读取数据超时
        httpConnect.doOutput = true
        httpConnect.doInput = true
        httpConnect.useCaches = false
        httpConnect.requestMethod = method // 设置为Post请求

        heads.forEach { key, value ->
            httpConnect.setRequestProperty(key, value)
        }

        httpConnect.connect() // 开始连接

        httpConnect.outputStream.write(body?.toByteArray())

        val responseCode = httpConnect.responseCode;
        var inputStream: InputStream = if (httpConnect.responseCode == HttpURLConnection.HTTP_OK) httpConnect.inputStream else httpConnect.errorStream

        if ("gzip".equals(httpConnect.contentEncoding, true)) {
            inputStream = GZIPInputStream(inputStream)
        }

        var reader = BufferedReader(InputStreamReader(inputStream))

        var strBuilder = StringBuilder()
        reader.forEachLine {
            strBuilder.append(it)
            println(it)
        }

        response(httpConnect.date, responseCode, strBuilder.toString())

        inputStream.close()
        reader.close()
    }

    private fun openConnection(_url: String): HttpURLConnection {
        var url = URL(_url)
        if (url.toString().toLowerCase(Locale.getDefault()).startsWith("https://")) {
            HTTPSTrustManager.allowAllSSL()
        }
        return url.openConnection() as HttpURLConnection
    }
}