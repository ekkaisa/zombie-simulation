package zombies

import scala.util.Random

//Used to represent different age groups within the simulation
//!!CURRENTLY ONLY ADULTS ARE USED!!
enum AgeGroup(val actionProb: Vector[Double], val effectiveness: Vector[Double]):

  case Child extends AgeGroup(actionDist("child"), actionEff("child"))
  case Adult extends AgeGroup(actionDist("adult"), actionEff("adult"))
  case Elder extends AgeGroup(actionDist("elder"), actionEff("elder"))

  
  //Choose an action randomly from the action distribution
  def chooseAction =
    val sample = Random.nextFloat()
    if sample <= actionProb(0) then
      "fight"
    else if sample <= (actionProb(0) + actionProb(1)) then
      "flight"
    else
      "freeze"

  //Fight response, returns true in case of killing the zombie
  def fight =           
    val sample = Random.nextFloat()
    (sample <= pFight * effectiveness(0), "fight")

  // Flight response, the human either escapes or fights the zombie with bad odds
  
  def flight =
    val sample = Random.nextFloat()
    if sample <= pFlight * effectiveness(1) then
      (true, "flight")
    else
      val newSample = Random.nextFloat()
      if newSample <= pFlightFight * effectiveness(0) then
        (true, "fight")
      else
        (false, "fight")
  
  // Freeze response, technically a fight with very bad odds at winning
  def freeze =
    val sample = Random.nextFloat()
    (sample <= pFreeze * effectiveness(0), "fight")
    
    
  def takeAction =
    chooseAction match
      case "fight" => fight
      case "flight" => flight
      case "freeze" => freeze

