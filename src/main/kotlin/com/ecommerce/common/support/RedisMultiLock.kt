package com.ecommerce.common.support

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

interface DistributeLock {
    fun multiLock(
        vararg keys: String,
        waitTime: Long = 3,
        leaseTime: Long = 5,
        unit: TimeUnit = TimeUnit.SECONDS,
        command: () -> Unit
    )
}

@Component
class RedissonMultiLock(
    private val redissonClient: RedissonClient
) : DistributeLock {
    companion object {
        private const val LOCK_PREFIX = "lock:"
    }

    override fun multiLock(
        vararg keys: String,
        waitTime: Long,
        leaseTime: Long,
        unit: TimeUnit,
        command: () -> Unit
    ) {
        val rLocks = keys
            .map { redissonClient.getLock("$LOCK_PREFIX$it") }
            .toTypedArray()

        val multiLock = redissonClient.getMultiLock(*rLocks)
        val locked = multiLock.tryLock(waitTime, leaseTime, unit)

        if (locked) {
            try {
                command()
            } finally {
                multiLock.unlock()
            }
        }
    }
}
