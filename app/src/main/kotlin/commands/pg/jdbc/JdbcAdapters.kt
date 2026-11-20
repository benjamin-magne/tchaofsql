package net.unotec.app.commands.pg.jdbc

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Proxy
import java.sql.Connection
import java.sql.DatabaseMetaData
import java.sql.ResultSet

/** JDBC compatibility at the command boundary; services remain unchanged. */
internal inline fun <reified T> jdbcProxy(target: T, crossinline call: (java.lang.reflect.Method, Array<out Any?>) -> Any?): T =
    Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java)) { _, method, args ->
        try { call(method, args ?: emptyArray()) }
        catch (e: InvocationTargetException) { throw e.targetException }
    } as T

internal fun metadataConnection(connection: Connection): Connection = jdbcProxy(connection) { method, args ->
    if (method.name == "getMetaData") {
        val metadata = connection.metaData
        jdbcProxy<DatabaseMetaData>(metadata) { m, a ->
            val result = m.invoke(metadata, *a)
            if (m.name == "getColumns") {
                val rows = result as ResultSet
                jdbcProxy<ResultSet>(rows) { r, values ->
                    if (r.name == "getInt" && values.firstOrNull() == "SIZE") rows.getInt("COLUMN_SIZE")
                    else r.invoke(rows, *values)
                }
            } else result
        }
    } else method.invoke(connection, *args)
}
