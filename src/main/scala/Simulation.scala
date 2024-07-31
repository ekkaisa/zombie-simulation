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

class Simulation extends Runnable:

  def run() = 
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
  
    releaseZombies(1, 73, 118)
    baseStats()
  

  
    var running = true
    var timeoutCriteria = false
    var timeoutBound = 50
    var lastZombieamount = 0
    while running do
       var z = map.zombies
       if(z._1 < population && z._1 != z._2 && !timeoutCriteria) {
         map.passTime()
         z = map.zombies

         generalStats += z
         detailedStats.foreach( x =>
           val tile = map(x._1._1, x._1._2)
           detailedStats.update((x._1._1, x._1._2), x._2.appended((tile.zombieCount, tile.populationCount))))

         if (lastZombieamount == z._1) {
           timeoutBound -= 1
         }

         lastZombieamount = z._1

         if (timeoutBound <= 0) {
           timeoutCriteria = true
         }

         println(hours)

         hours += 1
         if (hours == 13) {
           map.activateBorder
         }

       } else {
         StatLogger.logGeneralStats(generalStats.toVector)
         StatLogger.logDetailStats(detailedStats.toMap)
         running = false
  
       }








