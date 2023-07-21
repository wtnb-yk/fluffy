package com.fluffy.fluffyapi.gateway


import com.fluffy.fluffyapi.driver.fluffydb.tables.Owners.Companion.OWNERS
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import java.util.*

@Component
class OwnersRepository (private val create: DSLContext) {
    fun fetchAll() = create.selectFrom(OWNERS).orderBy(OWNERS.NAME).fetch()
    fun delete(id: UUID) = create.deleteFrom(OWNERS).where(OWNERS.ID.eq(id)).execute()
    fun create(name: String) = create.insertInto(OWNERS, OWNERS.NAME).values(name).execute()
}
