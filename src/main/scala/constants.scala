package zombies


val infectingLag = 1
val mapHeight = 109
val mapWidth = 211
val population = 1704456
val movingProbabilityDist = Vector.tabulate(13)(x => (x * 0.05 + 3 * 0.125, 5 * 0.125 - x * 0.05)).appended((1.0,0.0))
val borderPassingProb = 0.001




// ACTION PROBABILITY DISTRIBUTIONS //
// ORDER: FIGHT, FLIGHT, FREEZE //

val actionDist = Map("child" -> Vector(0.05, 0.6, 0.35),
                     "adult" -> Vector(0.25, 0.55, 0.20),
                     "elder" -> Vector(0.45, 0.15, 0.40))

// ACTION EFFECTIVENESS //

val actionEff = Map("child" -> Vector(0.5, 0.7, 1.0),
                     "adult" -> Vector(1.0, 1.0, 1.0),
                     "elder" -> Vector(0.7, 0.5, 1.0))

// ACTIONS
// probabilities are always success probabilities of humans succeeding at surviving/beating the zombie

val pFight = 0.5

val pFlight = 0.7
val pFlightFight = 0.1

val pFreeze = 0.05