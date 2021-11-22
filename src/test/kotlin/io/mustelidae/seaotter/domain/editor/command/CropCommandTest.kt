package io.mustelidae.seaotter.domain.editor.command

import io.kotlintest.shouldBe
import io.mustelidae.seaotter.domain.delivery.Image
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.File

internal class CropCommandTest {

    private val inputPath = getTestImageFileAsAbsolutePath("denden_CMYK_2784x1856.jpg")

    @Test
    @DisplayName("Test to cut from the center of the picture.")
    fun cropPosition() {
        // Given
        val option = CropOption.Builder().position(CropOption.Position.CENTER, 400, 200).build()
        var bufferedImage = Image.from(File(inputPath)).bufferedImage
        val cropCommand = CropCommand(bufferedImage)
        // When
        cropCommand.execute(option)
        bufferedImage = cropCommand.getBufferedImage()
        // Then
        bufferedImage.width shouldBe 400
        bufferedImage.height shouldBe 200

        val validateRGBTable = listOf(
            Triple(20, 30, -398450),
            Triple(40, 50, -463719),
            Triple(17, 26, -529268)
        )

        validateRGBTable.forEach { (x, y, rgb) ->
            bufferedImage.getRGB(x, y) shouldBe rgb
        }
    }

    @Test
    @DisplayName("Test to cut based on the coordinates of the picture.")
    fun cropCoordinates() {
        // Given
        val option = CropOption.Builder().coordinates(300, 300, 700, 900).build()
        var bufferedImage = Image.from(File(inputPath)).bufferedImage
        val cropCommand = CropCommand(bufferedImage)
        // When
        cropCommand.execute(option)
        bufferedImage = cropCommand.getBufferedImage()
        // Then
        bufferedImage.width shouldBe 400
        bufferedImage.height shouldBe 600

        val validateRGBTable = listOf(
            Triple(20, 30, -8550252),
            Triple(40, 50, -9864054),
            Triple(160, 567, -7765629)
        )

        validateRGBTable.forEach { (x, y, rgb) ->
            bufferedImage.getRGB(x, y) shouldBe rgb
        }
    }

    @Test
    fun cropPointScale() {
        val option = CropOption.Builder().pointScale(300, 300, 400, 600).build()
        var bufferedImage = Image.from(File(inputPath)).bufferedImage
        val cropCommand = CropCommand(bufferedImage)
        // When
        cropCommand.execute(option)
        bufferedImage = cropCommand.getBufferedImage()
        // Then
        bufferedImage.width shouldBe 400
        bufferedImage.height shouldBe 600

        val validateRGBTable = listOf(
            Triple(20, 30, -8550252),
            Triple(40, 50, -9864054),
            Triple(160, 567, -7765629)
        )

        validateRGBTable.forEach { (x, y, rgb) ->
            bufferedImage.getRGB(x, y) shouldBe rgb
        }
    }
}
