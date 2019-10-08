package therealfarfetchd.commoncfg.client.font

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.texture.TextureUtil
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.nio.ByteBuffer
import java.util.*

private val buffer = ByteBuffer.allocateDirect(10000000).asIntBuffer()

class FontWrapper(val font: BDF) {
  var textures: List<Int> = emptyList(); private set

  var glyphPosY: Map<Char, Int> = emptyMap(); private set
  var textureIndex: Map<Char, Int> = emptyMap(); private set

  var widthMap: Map<Int, Int> = emptyMap(); private set
  var heightMap: Map<Int, Int> = emptyMap(); private set

  var charWidth = 0; private set
  var charHeight = 0; private set

  private val maxTexSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE)

  private var destroyed = false

  init {
    println("Max texture size: $maxTexSize")
    allocateTextures()

    var currentHeight = 0
    var currentTex = 0
    for ((k, v) in font.glyphs) {
      charWidth = maxOf(charWidth, v.width)
      charHeight = maxOf(charHeight, v.height)

      if (currentHeight + v.height > heightMap[currentTex]!!) {
        currentHeight = 0
        currentTex++
        if (currentTex > textures.size)
          error("A fatal error occurred while writing texture sheet. This shouldn't ever happen unless this code has a bug. RIP")
      }

      glyphPosY += k to currentHeight
      textureIndex += k to currentTex

      setTexture(v.bits, currentTex, currentHeight, v.width, v.height)
      currentHeight += v.height
    }
  }

  /**
   * Draw the specified char into the buffer at the specified position.
   * Returns the offset of the next glyph for convenience
   */
  fun draw(c: Char, x: Int, y: Int, color: Color = Color(0.75f, 0.75f, 0.75f, 1f), respectPosition: Boolean = true): Pair<Int, Int> {
    val t = Tessellator.getInstance()
    val buf = t.bufferBuilder
    buf.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR)

    val texIndex = textureIndex[c] ?: return Pair(0, 0)
    RenderSystem.bindTexture(textures[texIndex])
    val height = heightMap[texIndex]!!
    val width = widthMap[texIndex]!!
    RenderSystem.color4f(1f, 1f, 1f, 1f)
    val ty = glyphPosY[c] ?: glyphPosY[font.defaultGlyph] ?: return Pair(0, 0)
    val g = font.glyphs[c] ?: font.glyphs[font.defaultGlyph] ?: return Pair(0, 0)
    val tix = 0.0f
    val tiy = ty / height.toFloat()
    val tiw = g.width / width.toFloat()
    val tih = g.height / height.toFloat()
    val px = x.toDouble() + if (respectPosition) g.xOff else 0
    val py = y.toDouble() + if (respectPosition) g.yOff else 0

    buf.vertex(px, py, 0.0).texture(tix, tiy).color(color.red, color.green, color.blue, color.alpha).next()
    buf.vertex(px, py + g.height, 0.0).texture(tix, tiy + tih).color(color.red, color.green, color.blue, color.alpha).next()
    buf.vertex(px + g.width, py + g.height, 0.0).texture(tix + tiw, tiy + tih).color(color.red, color.green, color.blue, color.alpha).next()
    buf.vertex(px + g.width, py, 0.0).texture(tix + tiw, tiy).color(color.red, color.green, color.blue, color.alpha).next()

    t.draw()
    return Pair(g.dwidthX, g.dwidthY)
  }

  /**
   * Draws a string at the specified position, respecting device width.
   * This is unused, as the terminal uses a different implementation.
   */
  fun drawString(str: String, x: Int, y: Int, color: Color = Color(0.75f, 0.75f, 0.75f, 1f)): Pair<Int, Int> {
    var dw = Pair(x, y)
    for (c in str) {
      dw = draw(c, dw.first, dw.second, color).let { Pair(it.first + dw.first, it.second + dw.second) }
    }
    return dw
  }

  private fun setTexture(bitmap: BitSet, texture: Int, y: Int, width: Int, height: Int) {
    buffer.clear()
    for (i in 0 until width * height)
      buffer.put(if (bitmap[i]) -1 else 0)
    buffer.flip()
    RenderSystem.bindTexture(textures[texture])
    GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, y, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer)
  }

  private fun allocateTextures() {
    val textureWidth = (font.glyphs.values.maxBy { it.width }?.width ?: 0).powerOf2()

    var heightTmp = font.glyphs.values.sumBy { it.height }
    var texcount = 1
    while (heightTmp > maxTexSize) {
      heightTmp -= maxTexSize
      texcount++
    }
    for (i in 0 until texcount) {
      val height = if (i < texcount - 1) maxTexSize else heightTmp.powerOf2()

      val texid = TextureUtil.generateTextureId()
      textures += texid
      widthMap += i to textureWidth
      heightMap += i to height

      RenderSystem.bindTexture(texid)
      TextureUtil.initTexture(null, textureWidth, height)

      println("Allocated $textureWidth*$height texture ($texid)")
    }
  }

  // No idea how this works, found on StackOverflow :P
  private fun Int.powerOf2(): Int {
    var n = this - 1
    n = n or n.ushr(1)
    n = n or n.ushr(2)
    n = n or n.ushr(4)
    n = n or n.ushr(8)
    n = n or n.ushr(16)
    return if (n < 0) 1 else if (n >= Integer.MAX_VALUE) Integer.MAX_VALUE else n + 1
  }

  fun destroy() {
    textures.forEach(TextureUtil::releaseTextureId)
    println("Destroyed textures $textures")
    destroyed = true
  }
}