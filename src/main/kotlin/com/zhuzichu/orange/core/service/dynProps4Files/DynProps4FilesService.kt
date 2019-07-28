package com.zhuzichu.orange.core.service.dynProps4Files


import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

import java.io.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 动态配置文件,可以设置更新周期
 * 配置读取读取服务
 */
@Component
class DynProps4FilesService {
    private val _log = LoggerFactory.getLogger(DynProps4FilesService::class.java)
    /**
     * 属性文件
     */
    private lateinit var fileArray: Array<File>
    /**
     * 启动延时
     */
    private var delay: Long = 0
    /**
     * 更新周期
     */
    private var period: Long = 0
    /**
     * 属性对象
     */
    private var property = Properties()
    /**
     * 文件监控器
     */
    private var monitors: MutableList<FileMonitor>? = null

    /**
     * @param files  属性文件
     * @param delay  从`DynProps`被创建到第一次动态监视的时间间隔. 约束范围delay > 0
     * @param period 动态监视的时间间隔. 约束范围period >= 0；等于0表示不执行动态监视，退化为静态配置文件．
     */
    @Throws(IOException::class)
    constructor(files: Array<File>, delay: Long, period: Long) {
        this.fileArray = files
        this.delay = delay
        this.period = period
        init()
    }

    @Throws(IOException::class)
    constructor(fileNames: List<String>, delay: Long, period: Long) {
        this.delay = delay
        this.period = period
        val list = mutableListOf<File>()
        var index = 0
        for (oriFileName in fileNames) {
            val fileName = oriFileName.trim { it <= ' ' }
            if (StringUtils.indexOfIgnoreCase(fileName, "classpath:") == 0) {
                list[index++] = File(
                        this.javaClass.classLoader.getResource("")!!.path + File.separator +
                                fileName.substring("classpath:".length))
            } else {
                list[index++] = File(fileName)
            }
        }
        fileArray = list.toTypedArray()
        init()
    }


    @Throws(IOException::class)
    constructor(fileNames: String, delay: Long, period: Long) {
        var fileNames = fileNames
        this.delay = delay
        this.period = period
        var isClassPath = false
        if (fileNames.startsWith("classpath")) {
            fileNames = fileNames.substring("classpath:".length)
            isClassPath = true
        }
        val fileName = fileNames.split("[,|，|;|；]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val list = mutableListOf<File>()
        if (isClassPath) {
            for (i in fileName.indices) {
                list[i] = File(this.javaClass.classLoader.getResource("")!!.path + fileName[i])
            }
        } else {
            for (i in fileName.indices) {
                list[i] = File(fileName[i])
            }
        }
        fileArray = list.toTypedArray()
        init()
    }

    @Throws(IOException::class)
    constructor(files: Array<File>, period: Long) : this(files, 0, period)

    @Throws(IOException::class)
    constructor(fileNames: String, period: Long) {
        this.period = period
        this.delay = 0
        val fileName = fileNames.split("[,|，|;|；]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val files = arrayOfNulls<File>(fileName.size)
        for (i in fileName.indices) {
            files[i] = File(fileName[i])
        }
        init()
    }

    constructor()

    /**
     * 加载属性文件,启动监控
     *
     * @throws IOException 加载文件时出现IO异常
     */
    @Throws(IOException::class)
    protected fun load() {
        update()
        if (monitors == null) {
            monitors = ArrayList(fileArray.size)
        } else {
            for (monitor in monitors!!) {
                try {
                    monitor.timer.cancel()
                } catch (e: Exception) {
                    _log.warn(String.format("Timer for file [%s] cancelling failed.", monitor.file.absolutePath))
                }

            }
        }

        for (file in fileArray) {
            val lastModify = file.lastModified()
            val monitor = FileMonitor(file, lastModify)
            this.monitors!!.add(monitor)
            monitor.doTask()
        }
    }

    /**
     * 如果文件有更新调用此方法载入
     *
     * @throws IOException 没有找到文件或读文件错误时抛出
     */
    @Throws(IOException::class)
    protected fun update() {
        for (file in fileArray!!) {
            var `in`: InputStream? = null
            try {
                `in` = FileInputStream(file)
                this.property.load(`in`)
            } catch (e: Exception) {
                if (e is IOException) {
                    throw e
                }

                throw IOException(e)
            } finally {
                IOUtils.closeQuietly(`in`)
            }
        }
    }

    /**
     * @param key 需要获取属性值的KEY
     * @param def 默认值
     *
     * @return 属性值
     */
    fun getProperty(key: String, def: String): String {
        val `val` = this.property.getProperty(key)
        return `val`?.trim { it <= ' ' } ?: def
    }

    fun getProperty(key: String): String? {
        val `val` = this.property.getProperty(key)
        return `val`?.trim { it <= ' ' }
    }

    /**
     * 设置属性值
     *
     * @param key
     * @param value
     */
    fun setProperty(key: String, value: String) {
        this.property.setProperty(key, value)
    }

    /**
     * @param key 需要获取属性值的KEY
     * @param def 默认值
     *
     * @return 属性值
     *
     * @throws NumberFormatException 如果属性值不是整数形式
     */
    @Throws(NumberFormatException::class)
    @JvmOverloads
    fun getInt(key: String, def: Int = 0): Int {
        val `val` = this.getProperty(key)
        return if (`val` == null) def else Integer.parseInt(`val`)
    }

    @Throws(NumberFormatException::class)
    @JvmOverloads
    fun getFloat(key: String, def: Float = 0.0f): Float {
        val `val` = this.getProperty(key)
        return if (`val` == null) def else java.lang.Float.parseFloat(`val`)
    }

    @JvmOverloads
    fun getDouble(key: String, def: Double = 0.0): Double {
        val `val` = this.getProperty(key)
        return if (`val` == null) def else java.lang.Double.parseDouble(`val`)
    }


    @JvmOverloads
    fun getLong(key: String, def: Long = 0L): Long {
        val `val` = this.getProperty(key)
        return if (`val` == null) def else java.lang.Long.parseLong(`val`)
    }

    @Throws(IOException::class)
    private fun init() {
        for (file in fileArray) {
            if (!file.exists() || file.length() == 0L) {
                throw IllegalArgumentException("动态配置文件 " + file.absolutePath + " 不存在,或是空文件！")
            }
            if (delay <= 0) {
                throw IllegalArgumentException("定时器延时时间不能为负数！")
            }
            if (period <= 0) {
                throw IllegalArgumentException("定时器更新周期不能为负数！")
            }
            this.property = Properties()
            this.load()// 初始构造时，执行第一次加载.
        }
        //当进程终止时，取消定时任务
        Runtime.getRuntime().addShutdownHook(Thread(ShutdownHook()))
    }

    private inner class ShutdownHook : Runnable {
        private val dynProps4FilesService: DynProps4FilesService? = null

        override fun run() {
            println("Monitors cancelling start ...")
            if (monitors != null) {
                for (monitor in monitors!!) {
                    try {
                        monitor.timer.cancel()
                    } catch (e: Exception) {
                        _log.warn(String.format("Timer for file [%s] cancelling failed.",
                                monitor.file.absolutePath))
                    }

                }
            }
        }
    }

    /**
     * 描述：一个内部私有类，实时监控文件有没有更新，如果更新则自动载入
     */
    private inner class FileMonitor
    /**
     * @param lastMonitorTime 最后的更新时间
     */
    (val file: File, private var lastModifiedTime: Long) {
        /**
         * 定时器，以守护线程方式启动
         */
        val timer = Timer(true)

        /**
         * 对文件进行实时监控，有更新则自动载入
         */
        fun doTask() {
            if (delay < 0) {
                delay = 0L
            }
            if (period <= 0) {
                return // 如果更新周期非正数，则退化成静态配置文件.
            }
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val t = file.lastModified()
                    // 文件被删除
                    // 如果动态更新过程中，配置文件被强制删除了，本次不执行任何更新.或者对配置文件进行恢复
                    if (t == 0L) {
                        try {
                            if (file.createNewFile()) {
                                val fos = FileOutputStream(file)
                                property.store(fos, "文件被删除，自动恢复．")
                                fos.close()
                            }
                        } catch (ioe2: IOException) {
                            // 这里基本上只有磁盘空间满才会发生，暂时不处理
                        }

                        return
                    }
                    // 文件被更新
                    if (t > lastModifiedTime) {
                        lastModifiedTime = t
                        // 2秒后还在改变，则本次更新不做处理
                        try {
                            TimeUnit.SECONDS.sleep(2)
                        } catch (e: InterruptedException) {
                            // do nothing
                        }

                        if (t != file.lastModified()) {
                            _log.info("文件可能未更新完成，本次不更新！")
                        } else {
                            try {
                                property.clear()
                                update()
                                _log.info("UPDATED " + file.absolutePath)
                            } catch (ioe: IOException) {
                                _log.error("UPDATING " + file.absolutePath + " failed", ioe)
                            }

                        }
                        _log.debug("-----------------------:" + property.keys)
                    }
                }// end run()
            }, delay, period)
        }
    }
}
