package zombies

import zombies.AgeGroup.*
import zombies.CompassDir
import zombies.CompassDir.{East, North, NorthEast, NorthWest, South, SouthEast, SouthWest, West}

import scala.collection.mutable.*
import scala.util.Random
import scala.math.*


//A grid-representation of a map

class SimMap(val width: Int, val height: Int) {
  
  val grid = MapLoader.loadMap(this)
  val borders = MapLoader.loadOutsideBorders
  val insideBorders = MapLoader.loadInsideBorders






  def apply(i: Int, j: Int) =
    grid(i)(j)

  //This function moves every agent in the simulation
  def moveAgents() =

    var i = 0
    var j = 0
    while i < height - 1 do
      while j < width - 1 do
        val tile = this(i, j)
        
        if tile.passable then
          val tileNeighbors = tile.neighbors()
          val directions = tileNeighbors.keys.toVector
          
          val size = tileNeighbors.size
          val contentVec = tile.content.toVector
          
          var k = 0
          
          while k < contentVec.size do
            
            val agent = contentVec(k)
            agent.decrementLag()
            
            if agent.isZombie && agent.isAlive then
              val newTile = tileNeighbors(directions(Random.nextInt(size)))
              
              if newTile._2 then
                if Random.nextFloat() <= borderPassingProb then
                  tile.removeAgent(agent)
                  tile.subtrZombieCount
                  newTile._1.addAgent(agent)
                  newTile._1.addZombieCount
                else
                  agent.kill()
              else 
                tile.removeAgent(agent)
                tile.subtrZombieCount
                newTile._1.addAgent(agent)
                newTile._1.addZombieCount

            else if agent.isHuman then
              val firstTile = moveDirection(agent, tile)
              val secondTile = moveDirection(agent, firstTile)
              tile.removeAgent(agent)
              secondTile.addAgent(agent)

            k += 1

        j += 1
      j = 0
      i += 1


  //This method calculates a direction for a human agent to move towards, it calculates first the direction of home of the agent, 
  // and samples then which is the next tile that the human will move to.
  def moveDirection(agent: Agent, location: MapTile): MapTile =


    val v1 = agent.home.j - location.j   //x coord
    val v2 = agent.home.i - location.i   //y coord
    val vnorm = sqrt(v1 * v1 + v2 * v2)
    val probs = movingProbabilityDist(vnorm.toInt)

    val dot = - v2 / vnorm    // since we are taking dot product with a vector (0, -1), the x dimension of v "disappears"
    val angle = acos(dot).toDegrees
    val multiple = (angle / 45).toInt
    val remainder = angle % 45


    val directions = if v1 >= 0 then North.clockwiseHalf else North.counterClockwiseHalf
    val mainDirection = directions(multiple + (remainder / 22.5).toInt)
    val homeDirections = mainDirection.neighborDirections.appended(mainDirection)

    val possibleTiles = location.neighbors()
    val possibleHomeDirections = possibleTiles.keys.toVector.intersect(homeDirections)
    val possibleAwayDirections = possibleTiles.keys.toSet.removedAll(possibleHomeDirections).toVector

    if Random.nextFloat() <= probs._1 && possibleHomeDirections.nonEmpty then  
      val newTile = possibleTiles(possibleHomeDirections(Random.nextInt(possibleHomeDirections.size)))
      if newTile._2 then
        if Random.nextFloat() <= borderPassingProb then
          newTile._1
        else
          location
      else 
        newTile._1
        
    else if possibleAwayDirections.nonEmpty then
      val newTile = possibleTiles(possibleAwayDirections(Random.nextInt(possibleAwayDirections.size)))
      if newTile._2 then
        if Random.nextFloat() <= borderPassingProb then
          newTile._1
        else
          location
      else 
        newTile._1
        
    else 
      val newTile = possibleTiles(possibleHomeDirections(Random.nextInt(possibleHomeDirections.size)))
      if newTile._2 then
        if Random.nextFloat() <= borderPassingProb then
          newTile._1
        else
          location
      else 
        newTile._1



  //Activates the border, meaning a border is established on the borders of Helsinki-region
  def activateBorder =
    val outsideTiles = borders.map(x => this(x._1,x._2))
    val insideTiles = insideBorders.map(x => this(x._1,x._2))
    for (tile <- outsideTiles) do
      insideTiles.foreach(tile.removeNeighbor(_))
    for (tile <- insideTiles) do
      outsideTiles.foreach(tile.updateNeighborToBorder(_))












  //This calls for all the agents to interact with eachother within their maptile, it ignores maptiles with no zombies in them
  def takeAction() = 
    var i = 0
    var j = 0
    while i < height - 1 do
      while j < width - 1 do
        if this(i, j).zombieCount != 0 then
          this(i, j).fight()
        j += 1
      j = 0
      i += 1

  
  def passTime() =
      takeAction()
      moveAgents()

      
  //Calculates the amount of zombies, dead and the entire population
  def zombies =
    val zombies = grid.flatMap(x => x.map(_.zombieCount)).sum
    val pop = grid.flatMap(x => x.map(_.content.size)).sum
    val dead = grid.flatMap(x => x.map(_.content.count(x => x.isZombie && !x.isAlive))).sum

    println("Zombies: " + zombies + " Population: " + pop )
    (zombies, dead)


}

//Represents a single 1km x 1km tile in the map, passable means if the tile can be moved into, water if there is water (these are mostly used to determine graphics and neighbors of said tile)

class MapTile(val i: Int, val j: Int, val map: SimMap, var passable: Boolean, var water: Boolean):
  val content = HashSet[Agent]()

  private var zombies = 0
  
  //Here we store the maptiles that are the neighbors or this maptile, you can access them by their compass direction. 
  // The boolean represents if there is a condition (borders) related to the movement from this tile to the said tile. False if no border between these tiles and vice-versa
  private var neighborSquares = Map[CompassDir, (MapTile, Boolean)]()

  //Responsible for generating the neighbors at first and after for public access of them.
  def neighbors() =

    if neighborSquares.isEmpty && passable then
      val coords = Vector((North,(i - 1, j)),
                           (West,(i, j - 1)),
                           (South,(i + 1, j)),
                           (East, (i, j + 1)),
                           (NorthEast, (i - 1, j + 1)),
                           (SouthWest, (i + 1, j - 1)),
                           (NorthWest, (i - 1, j - 1)),
                           (SouthEast,(i + 1, j + 1)))
      neighborSquares.addAll(coords.map(a => (a._1, (map(a._2._1, a._2._2), false))).filter(_._2._1.passable))
      neighborSquares
    else
      neighborSquares

  //Remove neighbor - meaning no passage is allowed from this tile to the one given as a parameter
  def removeNeighbor(tile: MapTile) =
    neighborSquares.toVector.find(x => x._2._1 == tile).foreach(x => neighborSquares.remove(x._1))
    
  //Establish a conditional border between this and the parameter tile
  def updateNeighborToBorder(tile: MapTile) =
    neighborSquares.toVector.find(x => x._2._1 == tile).foreach(x => neighborSquares.update(x._1, (x._2._1, true)))
    
  //All agents in this tile fight/interact with eachother
  def fight() =
    var i = 0
    var j = 0
    val len = content.size
    val iterable = content.toVector

    while i < len do
      while j < len do
        if i != j then
          val fighter1 = iterable(i)
          val fighter2 = iterable(j)
          if fighter1.isAlive && fighter2.isAlive then
            if fighter1.isHuman && fighter2.canInfect  then
              if fighter1.fightZombie(fighter2) then
                addZombieCount
                j = len
                
            else if fighter1.canInfect && fighter2.isHuman then
              if fighter2.fightZombie(fighter1) then
                addZombieCount
                j = len
          j += 1
        j += 1
      j = 0
      i += 1
    
    
  def zombieRatio =
    zombies.toDouble / content.size
  
  def zombieCount =
    zombies
    
  def populationCount =
    content.size
    
  def removeAgent(agent: Agent) =
    content.remove(agent)
    
  def addAgent(agent: Agent) =
    content.add(agent)

  def makeBorder() =
    passable = false
    water = false

  def addZombieCount =
    zombies += 1


  def subtrZombieCount =
    zombies -= 1


  def makeWater() =
    passable = false
    water = true

    




