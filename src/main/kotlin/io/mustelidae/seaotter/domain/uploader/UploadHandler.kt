package io.mustelidae.seaotter.domain.uploader

import io.mustelidae.seaotter.config.AppEnvironment
import io.mustelidae.seaotter.domain.delivery.Image
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.net.URL
import java.util.Locale

@Service
class UploadHandler
@Autowired constructor(
    private val appEnvironment: AppEnvironment
) {

    private val uploader = UploadTarget.valueOf(appEnvironment.uploader.uppercase(Locale.getDefault()))

    fun upload(image: Image): URL {
        image.reviseFormat()
        val uploader = getUploader()
        val pathOfImage = uploader.upload(image)
        return makeUrl(pathOfImage)
    }

    private fun getUploader(): Uploader {
        val topicCode = findTopicCode()

        return when (uploader) {
            UploadTarget.S3 -> {
                S3Uploader(appEnvironment.awsS3, topicCode)
            }
            UploadTarget.LOCAL -> {
                LocalStorageUploader(appEnvironment.localStorage, topicCode)
            }
        }
    }
    private fun makeUrl(pathOfImage: String): URL {
        return when (uploader) {
            UploadTarget.S3 -> {
                S3Uploader.makeUrl(appEnvironment.awsS3, pathOfImage)
            }
            UploadTarget.LOCAL -> {
                LocalStorageUploader.makeUrl(appEnvironment.localStorage, pathOfImage)
            }
        }
    }

    /**
     * use magic.
     */
    private fun findTopicCode(): String? {
        if (RequestContextHolder.getRequestAttributes() == null)
            return null

        return (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes)
            .request.getParameter("topic")
    }
}
