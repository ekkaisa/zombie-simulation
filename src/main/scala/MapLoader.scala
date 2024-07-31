package zombies

import zombies.AgeGroup.*

import java.io.{BufferedReader, BufferedWriter, File, FileNotFoundException, FileReader, FileWriter, IOException, Reader, StringReader}
import scala.collection.mutable.*

//Used to read a map from the files

object MapLoader {


  def loadMap(simMap: SimMap) =
    val reader = new FileReader(new File("maps/uusimaa_mapuusimaa_map.txt"))
    val lineReader = new BufferedReader(reader)
    val grid = Array.tabulate(mapHeight,mapWidth)( (x, y)  => new MapTile(x,y,simMap, true, false))
    try
      var currentLine = lineReader.readLine().trim.split(';')
      println(currentLine.size)


      var i = 0
      var j = 1
      while i < mapHeight - 1 do
        println(currentLine.size)
        grid(i)(0).makeBorder()
        while j < mapWidth - 2 do
          currentLine(j) match
            case "w" => grid(i)(j).makeWater()
            case "x" => grid(i)(j).makeBorder()
            case number =>
              val pop = number.toIntOption
              if pop.isDefined then
                val tile = grid(i)(j)
                val hum = Vector.tabulate(pop.get)(x => new Agent(true, Adult, tile))
                hum.foreach(grid(i)(j).addAgent(_))
          j += 1
          
        grid(i)(j).makeBorder()
        
        currentLine = lineReader.readLine().trim.split(';')
        println(i)
        
        j = 1
        i += 1

    catch
      case e: IOException => throw e


    finally
      reader.close()
      
    grid




  def loadOutsideBorders =
    val reader = new FileReader(new File("maps/borders.txt"))
    val lineReader = new BufferedReader(reader)
    val coordinates = Buffer[(Int,Int)]()
    try
      var currentLine = lineReader.readLine()
      println(currentLine.size)


      while currentLine != null do
        val currentLineSplit = currentLine.trim.split(',')
        coordinates.addOne( (currentLineSplit(0).toInt - 1, currentLineSplit(1).toInt - 1))
        currentLine = lineReader.readLine()


    catch
      case e: IOException => throw e


    finally
      reader.close()

    coordinates



  def loadInsideBorders =
    val reader = new FileReader(new File("maps/insideborders.txt"))
    val lineReader = new BufferedReader(reader)
    val coordinates = Buffer[(Int,Int)]()
    try
      var currentLine = lineReader.readLine()
      println(currentLine.size)


      while currentLine != null do
        val currentLineSplit = currentLine.trim.split(',')
        coordinates.addOne( (currentLineSplit(0).toInt, currentLineSplit(1).toInt))
        currentLine = lineReader.readLine()


    catch
      case e: IOException => throw e


    finally
      reader.close()

    coordinates






}
