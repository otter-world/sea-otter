package io.mustelidae.seaotter.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app")
class AppEnvironment {
    var uploader: String = "local"
    var localStorage = LocalStorage()
    var awsS3 = AwsS3()

    class LocalStorage {
        lateinit var path: String
        var url: String = "http://localhost"
        var shardType: String? = "date"
    }

    class AwsS3 {
        var bucket: String = "sea-otter"
        var cloudFront = CloudFront()
        lateinit var path: String
        var shardType: String? = "date"

        class CloudFront {
            var enabled: Boolean = false
            var url: String = "http://localhost"
        }
    }
}
