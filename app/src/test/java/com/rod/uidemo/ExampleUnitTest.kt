package com.rod.uidemo

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.locks.ReentrantLock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_printABC() {
        val a = Object()
        val b = Object()
        val c = Object()

        val t1 = PrintThread("A", c, a)
        val t2 = PrintThread("B", a, b)
        val t3 = PrintThread("C", b, c)

        println("start")
        t1.start()
        Thread.sleep(100)
        t2.start()
        Thread.sleep(100)
        t3.start()

        t3.join()
        print("\nend")
    }

    class PrintThread(val content: String, val pre: Object, val self: Object) : Thread() {

        override fun run() {
            var count = 10;
            while (count > 0) {
                synchronized(pre) {
                    synchronized(self) {
                        count--
                        print(content)
                        self.notify()
                    }


                    if (count == 0) {
                        return
                    }
                    pre.wait()
                }
            }
        }
    }

    @Test
    fun printAbcByReentrantLock() {

        val printer = PrintCondition()
        Thread() {
            kotlin.run {
                (0 until 10).forEach {
                    printer.printA()
                }
            }
        }.start()
        Thread() {
            kotlin.run {
                (0 until 10).forEach {
                    printer.printB()
                }
            }
        }.start()
        Thread() {
            kotlin.run {
                (0 until 10).forEach {
                    printer.printC()
                }
            }
        }.start()

        Thread.sleep(3000)
    }

    class PrintCondition {
        private val lock = ReentrantLock()
        private val condA = lock.newCondition()
        private val condB = lock.newCondition()
        private val condC = lock.newCondition()
        private var curCond = condA

        fun printA() {
            try {
                lock.lock()
                while (curCond != condA) {
                    condA.await()
                }

                print("A")
                curCond = condB
                condB.signal()
            } finally {
                lock.unlock()
            }
        }

        fun printB() {
            try {
                lock.lock()
                while (curCond != condB) {
                    condB.await()
                }

                print("B")
                curCond = condC
                condC.signal()
            } finally {
                lock.unlock()
            }
        }

        fun printC() {
            try {
                lock.lock()
                while (curCond != condC) {
                    condC.await()
                }

                print("C")
                curCond = condA
                condA.signal()
            } finally {
                lock.unlock()
            }
        }
    }
}
