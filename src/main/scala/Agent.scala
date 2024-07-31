package zombies

import scala.math.max

//* Represents one agent in the simulation
// Constructor parameters:
// initialHuman: describe if the agent is initially a human
// age: describes the age of the agent, possible are child, adult and elder.
// home: the maptile where the agent is placed in the beginning
//
//
//
//
//
// *//

class Agent(initialHuman: Boolean, val age: AgeGroup, val home: MapTile) {
  
  private var alive = true
  private var human = initialHuman
  private var lag = 0

  


  def fightZombie(zombie: Agent) =
    val act = age.takeAction
    if act._1 then
      if act._2 == "fight" then
        zombie.kill()
      false
    else
      this.zombiefy()
      true

  def turnHuman() =
    human = true
  
 
  def kill() =
    alive = false
  
  def zombiefy() =
    human = false
    alive = true
    lag = infectingLag
    
  def canInfect =
    isZombie && lag == 0
    
  def decrementLag() =
    lag = max(0, lag-1)
    
  def isHuman =
    human
  
  def isZombie =
    !human
  
  def isAlive =
    alive



}
