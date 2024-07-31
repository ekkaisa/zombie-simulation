package zombies

import scala.swing.*
import java.awt.{Color, ComponentOrientation, Shape}
import java.awt.event.{ActionEvent, ActionListener}
import java.awt.geom.{AffineTransform, Ellipse2D, PathIterator, Point2D, Rectangle2D}
import java.io.{BufferedReader, BufferedWriter, File, FileNotFoundException, FileReader, FileWriter, IOException, Reader, StringReader}
import javax.imageio.ImageIO
import javax.swing.{JOptionPane, Timer}
import scala.collection.mutable.*
import scala.math.*

object Simulator extends SimpleSwingApplication:


  val map = SimMap(mapWidth, mapHeight)
  var hours = 0
  val generalStats = Buffer[(Int, Int)]()
  val detailedStats = Map[(Int, Int), Vector[(Int, Int)]]()



  def baseStats() =
    for i <- 0 until mapHeight do
      for j <- 0 until mapWidth do
        detailedStats += (i, j) -> Vector[(Int, Int)]()


  def releaseZombies(count: Int, i: Int, j: Int) =
      map(i, j).content.take(count).foreach(_.zombiefy())
      (0 until count).foreach(x => map(i, j).addZombieCount)

  releaseZombies(5, 73, 118)
  baseStats()




  val window = Frame()
  window.contents = SimPanel()

  def top = window

  class SimPanel() extends Panel:
    val width = 2110
    val height = 1100
    preferredSize = new Dimension(width,height)


 //Tässä metodissa piirretään objektit
    override def paint(g: Graphics2D) = {
      g.clearRect(0,0,width,height)


      for (i <- 0 until map.height ) {
        for (j <- 0 until map.width) {
          val tile = map(i,j)
          if tile.water then
            g.setColor(new Color(97,149,232, 255))
          else if tile.passable then
            val zombies = tile.zombieRatio

            val amount = min(1,(map(i,j).content.size + 100) / 2000.0)
            g.setColor(new Color((255 * zombies).toInt,(204 * (1 - zombies)).toInt,0, (amount * 255).toInt))
          else
            g.setColor(new Color(160,160,160, 255))

          g.fill(new Rectangle2D.Double(j*10,i*10, 10, 10))
        }


      }
//      g.setColor(new Color(10,0,0, 255))
//      for (c <- map.borders) {
//        g.fill(new Rectangle2D.Double(c._2*10,c._1*10, 10, 10))
//      }
//
//      g.setColor(new Color(180,150,0, 255))
//      for (c <- map.insideBorders) {
//        g.fill(new Rectangle2D.Double(c._2*10,c._1*10, 10, 10))
//      }

    }


 //Timerin avulla saadaan näkymää päivitettyä

    new Timer(10, new ActionListener {
       def actionPerformed(e: ActionEvent) = {

         val z = map.zombies
         if(z._1 < population && z._1 != z._2) {
           map.passTime()
           repaint()
           generalStats += z
           detailedStats.foreach( x =>
             val tile = map(x._1._1, x._1._2)
             detailedStats.update((x._1._1, x._1._2), x._2.appended((tile.zombieCount, tile.populationCount))))

           println(hours)
           hours += 1
           if (hours >= 1) then
             map.activateBorder
         } else {
           StatLogger.logGeneralStats(generalStats.toVector)
           StatLogger.logDetailStats(detailedStats.toMap)
           quit()
         }

      }
    }).start()






