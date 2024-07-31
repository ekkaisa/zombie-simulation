package zombies

import zombies.AgeGroup.*

import java.io.{BufferedReader, BufferedWriter, File, FileNotFoundException, FileReader, FileWriter, IOException, Reader, StringReader}
import scala.util.Random


object StatLogger {


  def logGeneralStats(stats: Vector[(Int,Int)]) =
    val name = System.currentTimeMillis()
    val writer = new FileWriter(new File(s"generalstats/$name.txt"))
    val lineWriter = new BufferedWriter(writer)

    try
      for line <- stats do
        lineWriter.write(""+ line._1 + "," + line._2 + "\n")



    catch
      case e: IOException => throw e


    finally
      lineWriter.close()
      writer.close()



  def logDetailStats(stats: Map[(Int, Int), Vector[(Int, Int)]]) =
    val name = System.currentTimeMillis() + Random.nextInt(100)
    val writer = new FileWriter(new File(s"detailedstats/$name.txt"))
    val lineWriter = new BufferedWriter(writer)

    try
      for line <- stats do
        lineWriter.write(""+ line._1._1 + "," + line._1._2)
        line._2.foreach(x => lineWriter.write(";" + x._1 + "," + x._2))
        lineWriter.write("\n")



    catch
      case e: IOException => throw e


    finally
      lineWriter.close()
      writer.close()

















}
