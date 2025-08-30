package com.abelpinheiro.mousephoneapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

sealed interface ConnectionState{
    object Idle: ConnectionState
    object Connecting: ConnectionState
    data class Connected(val serverIp: String): ConnectionState
    data class Error(val message: String): ConnectionState
    object Disconnected: ConnectionState
}

/**
 * Repository interface for managing the connection to a server.
 */
interface ConnectionRepository{
    val connectionState: Flow<ConnectionState>
    fun connect(ip: String, port: String)
    fun sendMessage(message: String)
    fun disconnect()
}

@Singleton
class ConnectionRepositoryImpl @Inject constructor(
    private val webSocketDataSource: WebSocketDataSource,
): ConnectionRepository{
    override val connectionState: Flow<ConnectionState> = webSocketDataSource.connectionState
    override fun connect(ip: String, port: String) {
        webSocketDataSource.connect("ws://$ip:$port/ws")
    }

    override fun sendMessage(message: String) {
        webSocketDataSource.sendMessage(message)
    }

    override fun disconnect() {
        webSocketDataSource.disconnect()
    }

}

@Singleton
class WebSocketDataSource @Inject constructor(){
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient().newBuilder()
        .readTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Idle)
    val connectionState: Flow<ConnectionState> = _connectionState.asStateFlow()

    fun connect(url: String){
        _connectionState.value = ConnectionState.Connecting
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener(){
            override fun onOpen(webSocket: WebSocket, response: Response) {
                _connectionState.value = ConnectionState.Connected(response.request.url.host)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _connectionState.value = ConnectionState.Error(t.message ?: "Unknown connection error")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                _connectionState.value = ConnectionState.Disconnected
            }
        })
    }

    fun sendMessage(message: String){
        webSocket?.send(message)
    }

    fun disconnect(){
        webSocket?.close(1000, "User disconnected")
    }
}