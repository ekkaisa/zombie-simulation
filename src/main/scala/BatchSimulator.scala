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

object BatchSimulator extends App:


  for (x <- 1 to 2) do
    val threads = for (d <- 0 to 25) yield new Thread(Simulation())
    threads.foreach(_.start())
    threads.foreach(_.join())








